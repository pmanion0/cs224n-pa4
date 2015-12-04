package cs224n.models;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.ejml.simple.SimpleMatrix;
import cs224n.deep.Datum;
import cs224n.deep.FakeNeuralNetwork;
import cs224n.deep.NeuralNetwork;
import cs224n.document.Document;
import cs224n.document.DocumentSet;
import cs224n.gradcheck.GradientCheck;
import cs224n.util.CoNLLEval;
import cs224n.util.Configuration;
import cs224n.util.FileIO;
import cs224n.util.PairOfSimpleMatrixArray;
import cs224n.util.WordMap;
import cs224n.util.WordWindow;

public class WindowModel implements Model {
  
  public WordMap wordMap;

  Configuration conf;
  public NeuralNetwork model;
  //public FakeNeuralNetwork model;
  
  public WindowModel(Configuration configuration) throws IOException {
    if (configuration.getWindowSize() % 2 == 0) {
      System.err.println("ERROR: I'm sorry Dave. I'm afraid I can't let you use even windowSizes.");
      configuration.setWindowSize(3);
    }
    conf = configuration;
    model = new NeuralNetwork(conf);
    //model = new FakeNeuralNetwork(conf);
    wordMap = new WordMap(conf);
  }

  /**
   * Simplest SGD training
   */
  public void train(List<Datum> trainData) {
    DocumentSet docs = new DocumentSet(trainData);
    int iterCount = 0;
    int trainingObs = docs.size();
    int trainEvalFreq = conf.getTrainEvalFreq() * trainingObs;
    int maxIters = conf.getMaxIterations() * trainingObs;
    CoNLLEval tester = new CoNLLEval(conf.getConllevalPath());
    
    J = 0; count = 0; epochCount = 1;
    
    Iterator<Document> iter = docs.iterator();
    while (iterCount < maxIters) {
      // If we ran through the DocumentSet, shuffle, and relaunch the iterator
      if (!iter.hasNext()) {
        docs.shuffle();
        iter = docs.iterator();
        epochCount++;
      }
      Document d = iter.next();
      WordWindow window = new WordWindow(d, conf.getWindowSize(), wordMap);
      trainDocument(window);
      
      iterCount++;
      // Evaluate the model at the requested frequency during training
      if (trainEvalFreq > 0 && (iterCount % trainEvalFreq) == 0) {
        System.out.println("  [Training Performance at Iteration " + iterCount + "]");
        System.out.println("  [COST J: " + J/count + "]");
        J = 0; count = 0;
        List<String> predictions = this.test(trainData);
        tester.eval(predictions);
      }
    }
  }
  
  public double J, count, epochCount;
  
  /**
   * Train the model based on a specific sequence of WordWindows
   */
  public void trainDocument(WordWindow window) {
    do {
      int targetID = window.getTargetLabelID();
      int[] windowIDs = window.getIDArray();
      
      SimpleMatrix X = idsToWordVector(windowIDs);
      SimpleMatrix Y = targetToVector(targetID);
      
      J += model.crossEntropyCost(X, Y);
      count++;
      
      // Get the updated X with the gradient
      PairOfSimpleMatrixArray nabla = model.backprop(X, Y);
      
//      System.out.println("Predicted Class: " + model.getBestOutputClass(X) 
//          + " --- Actual Class: "
//          + (Y.get(0,0)==1 ? 0 : Y.get(0,1)==1 ? 1 : Y.get(0,2)==1 ? 2 : Y.get(0,3)==1 ? 3 : Y.get(0,4)==1 ? 4 : -1));
//      double wgrad = nabla.getFirstSimpleMatrixArray()[0].elementSum();
//      double bgrad = nabla.getSecondSimpleMatrixArray()[0].elementSum();
//      System.out.println("Sum of WGradient: " + wgrad + " --- Sum of BGradient" + bgrad);
//      double[] scores = model.score(X);
//      for (double d : scores)
//        System.out.print(" " + d);
//      System.out.print("\n");
//      if (Math.abs(wgrad) > 8)
//        System.out.print("");
      
      if (false) { //gradientCheck) {
        // Get List of Gradient Matrices
        SimpleMatrix[] nabla_w = nabla.getFirstSimpleMatrixArray();
        List<SimpleMatrix> gradients = new ArrayList<SimpleMatrix>();
        for (int i=1; i < nabla_w.length; i++)
          gradients.add(nabla_w[i]);
        gradients.add(nabla_w[0].transpose());
        
        // Get List of Weight Matrices
        List<SimpleMatrix> weights = model.getWeightList();
        weights.add(X);
        
        boolean result = GradientCheck.check(Y, weights, gradients, model);
        System.out.println("Pass Gradient Check? " + result);
        /*SimpleMatrix[] nabla_w = nabla.getFirstSimpleMatrixArray();
        SimpleMatrix[] emp_nabla_w = model.empiricalNabla(X, Y);
        model.checkGradient(nabla_w, emp_nabla_w);*/
      }
      SimpleMatrix updatedX = model.updateGradient(X, nabla, epochCount).transpose();
      
      // Update the word vectors if option is turned on 
      if (conf.getLearnWordVec())
        updateWordVector(windowIDs, updatedX);
    } while (window.rollWindow());
  }
  
   
  public List<String> test(List<Datum> testData) {
    List<String> predictions = new ArrayList<String>();
    DocumentSet docs = new DocumentSet(testData);
    
    for (Document d : docs) {
      WordWindow window = new WordWindow(d, conf.getWindowSize(), wordMap);
      
      do {
        int[] windowIDs = window.getIDArray();
        SimpleMatrix X = idsToWordVector(windowIDs);
        int pred = model.getBestOutputClass(X);
        String predStr = wordMap.getTargetName(pred);
        // Output the scored string:  `word`  `gold_label`  `pred_label`
        predictions.add(window.getTargetWord() + "\t" + window.getTargetLabel() + "\t" + predStr);
      } while (window.rollWindow());
    }
    
    return predictions;
  }
  
  
  /**
   * Update word vector
   * @param idList
   * @param X
   */
  public void updateWordVector(int[] idList, SimpleMatrix X) {
    for (int i=0; i < idList.length; i++) {
      int wordID = idList[i];
      // now each row represents a word vector
      X.reshape(conf.getWindowSize(), conf.getWordVecDim());
      double X_i[] = X.extractVector(true, i).getMatrix().getData();
      for (int j = 0; j < conf.getWordVecDim(); j++) {
      	wordMap.setWordVector(wordID, j, X_i[j]);
      }
    }
  }
  
  /**
   * Convert a list of Word IDs into a Neural Network input vector; this
   * concatenates the word vector representation of each word into one
   * @param idList - List of integer word IDs
   * @return Combined word vector, e.g. [wordVec1 wordVec2 ... wordVecN]
   */
  public SimpleMatrix idsToWordVector(int[] idList) {
    SimpleMatrix windowVector = new SimpleMatrix(1, conf.getWindowSize()*conf.getWordVecDim());
    for (int i=0; i < idList.length; i++) {
      int wordID = idList[i];
      SimpleMatrix wordVec = wordMap.getWordVector(wordID);
      windowVector.insertIntoThis(0, i*conf.getWordVecDim(), wordVec);
    }
    return new SimpleMatrix(windowVector);
  }
  
  /**
   * Convert the target ID into a one-hot encoded vector
   * @param targetID - Entity ID of the target
   * @return One-hot encoded SimpleMatrix vector, e.g. [0 0 0 1 0]
   */
  public SimpleMatrix targetToVector(int targetID) {
    SimpleMatrix out = new SimpleMatrix(1, 5);
    out.set(targetID, 1);
    return out;
  }
}