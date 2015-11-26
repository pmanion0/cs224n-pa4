package cs224n.models;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.ejml.simple.SimpleMatrix;

import cs224n.deep.Datum;
import cs224n.deep.FakeNeuralNetwork;
import cs224n.util.Configuration;
import cs224n.util.FileIO;
import cs224n.util.WordMap;
import cs224n.util.WordWindow;

public class WindowModel implements Model {
  
  private WordMap wordMap;

  Configuration conf;
  //public NeuralNetwork model;
  public FakeNeuralNetwork model;
  
  public WindowModel(Configuration configuration) throws IOException {
    if (configuration.getWindowSize() % 2 == 0) {
      System.err.println("ERROR: I'm sorry Dave. I'm afraid I can't let you use even windowSizes.");
      configuration.setWindowSize(3);
    }
    conf = configuration;
    //model = new NeuralNetwork(conf);
    model = new FakeNeuralNetwork(conf);
    wordMap = new WordMap(conf);
  }

  /**
   * Simplest SGD training
   */
  public void train(List<Datum> trainData) {
    WordWindow window = new WordWindow(trainData, conf.getWindowSize(), wordMap);
    
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
    WordWindow window = new WordWindow(testData, conf.getWindowSize(), wordMap);
    
    do {
      int[] windowIDs = window.getIDArray();
      SimpleMatrix X = idsToWordVector(windowIDs);
      int pred = model.getBestOutputClass(X);
      String predStr = wordMap.getTargetName(pred);
      predictions.add(predStr);
    } while (window.rollWindow());
    
    FileIO.outputScoringToFile(testData, predictions, outfile);
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