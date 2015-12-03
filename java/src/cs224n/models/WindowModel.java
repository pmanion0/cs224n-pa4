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
  
  private WordMap wordMap;

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
    
    Iterator<Document> iter = docs.iterator();
    while (iterCount < maxIters) {
      // If we ran through the DocumentSet, shuffle, and relaunch the iterator
      if (!iter.hasNext()) {
        docs.shuffle();
        iter = docs.iterator();
      }
      Document d = iter.next();
      WordWindow window = new WordWindow(d, conf.getWindowSize(), wordMap);
      trainDocument(window);
      iterCount++;
      // Evaluate the model at the requested frequency during training
      if (trainEvalFreq > 0 && (iterCount % trainEvalFreq) == 0) {
        System.out.println("  [Test Performance at Iteration " + iterCount + "]");
        List<String> predictions = this.test(trainData);
        tester.eval(predictions);
      }
    }
  }
  
  /**
   * Train the model based on a specific sequence of WordWindows
   */
  public void trainDocument(WordWindow window) {
    do {
      int targetID = window.getTargetLabelID();
      int[] windowIDs = window.getIDArray();
      
      SimpleMatrix X = idsToWordVector(windowIDs);
      SimpleMatrix Y = targetToVector(targetID);
      
      // Get the updated X with the gradient
      PairOfSimpleMatrixArray nabla = model.backprop(X, Y);
      SimpleMatrix updatedX = model.updateGradient(X, nabla).transpose();
      
      if (true) { //gradientCheck) {
        /*// Get List of Gradient Matrices
        SimpleMatrix[] nabla_w = nabla.getFirstSimpleMatrixArray();
        List<SimpleMatrix> gradients = new ArrayList<SimpleMatrix>();
        for (int i=0; i < nabla_w.length; i++)
          gradients.add(nabla_w[i]);
        gradients.add(X.minus(updatedX.transpose()));
        
        // Get List of Weight Matrices
        List<SimpleMatrix> weights = model.getWeightList();
        weights.add(X);
        
        boolean result = GradientCheck.check(Y, weights, gradients, model);
        System.out.println("Pass Gradient Check? " + result);*/
        SimpleMatrix[] nabla_w = nabla.getFirstSimpleMatrixArray();
        SimpleMatrix[] emp_nabla_w = model.empiricalNabla(X, Y);
        model.checkGradient(nabla_w, emp_nabla_w);
      }
      
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