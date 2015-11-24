package cs224n.deep;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.ejml.simple.SimpleMatrix;

import cs224n.util.WordWindow;

public class WindowModel implements NERModel {

  public int wordVecSize, windowSize;
  public int[] hiddenSizes;
  //public NeuralNetwork model;
  public FakeNeuralNetwork model;
  
  public WindowModel(int wordVecSize, int windowSize, int[] hiddenSizes) {
    if (windowSize % 2 == 0) {
      System.err.println("ERROR: I'm sorry Dave. I'm afraid I can't let you use even windowSizes.");
      windowSize = 3;
    }
    this.wordVecSize = wordVecSize;
    this.windowSize = windowSize;
    this.hiddenSizes = hiddenSizes;
    //model = new NeuralNetwork(wordVecSize*windowSize, hiddenSizes, 5);
    model = new FakeNeuralNetwork();
  }

  /**
   * Simplest SGD training
   */
  public void train(List<Datum> trainData) {
    WordWindow window = new WordWindow(trainData, windowSize);
    
    do {
      int targetID = window.getTargetLabelID();
      int[] windowIDs = window.getIDArray();
      
      SimpleMatrix X = idsToWordVector(windowIDs);
      SimpleMatrix Y = targetToVector(targetID);
      model.runBackprop(X, Y);
    } while (window.rollWindow());
  }
  
  public void test(List<Datum> testData, String outfile) {
    List<String> predictions = new ArrayList<String>();
    WordWindow window = new WordWindow(testData, windowSize);
    
    do {
      int[] windowIDs = window.getIDArray();
      SimpleMatrix X = idsToWordVector(windowIDs);
      int pred = model.getBestOutputClass(X);
      String predStr = FeatureFactory.getTargetName(pred);
      predictions.add(predStr);
    } while (window.rollWindow());
    
    FeatureFactory.outputScoringToFile(testData, predictions, outfile);
  }
  
  
  /**
   * Convert a list of Word IDs into a Neural Network input vector; this
   * concatenates the word vector representation of each word into one
   * @param idList - List of integer word IDs
   * @return Combined word vector, e.g. [wordVec1 wordVec2 ... wordVecN]
   */
  public SimpleMatrix idsToWordVector(int[] idList) {
    SimpleMatrix windowVector = new SimpleMatrix(1, windowSize*wordVecSize);
    for (int i=0; i < idList.length; i++) {
      int wordID = idList[i];
      SimpleMatrix wordVec = FeatureFactory.wordVector.extractVector(true, wordID); 
      windowVector.insertIntoThis(0, i*wordVecSize, wordVec);
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