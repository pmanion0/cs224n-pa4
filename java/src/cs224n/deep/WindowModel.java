package cs224n.deep;

import java.util.LinkedList;
import java.util.List;

import org.ejml.simple.SimpleMatrix;

import cs224n.util.WordWindow;

public class WindowModel {

  public FeatureFactory ff;
  public int wordVecSize, windowSize;
  public int[] hiddenSizes;
  public NeuralNetwork model;

  public WindowModel(int wordVecSize, int windowSize, int[] hiddenSizes, FeatureFactory ff) {
    if (windowSize % 2 == 0) {
      System.err.println("ERROR: I'm sorry, Dave. I can't let you use even windowSizes.");
      windowSize = 3;
    }
    this.wordVecSize = wordVecSize;
    this.windowSize = windowSize;
    this.hiddenSizes = hiddenSizes;
    this.ff = ff;
    model = new NeuralNetwork(wordVecSize*windowSize, hiddenSizes, 5);
  }

  /**
   * Simplest SGD training
   */
  public void train(List<Datum> trainData) {
    WordWindow window = new WordWindow(trainData, windowSize);
    
    while(window.rollWindow()) {
      int[] windowIDs = window.getWindowNum();
    }
  }
  
  public void test(List<Datum> testData) {
    // TODO
  }

}