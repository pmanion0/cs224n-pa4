package cs224n.deep;

import java.util.List;

import org.ejml.simple.SimpleMatrix;

public class WindowModel {

  protected SimpleMatrix L, W, Wout;
  //
  public int windowSize, wordSize, hiddenSize;

  public WindowModel(int _windowSize, int _hiddenSize, double _lr) {
    // TODO
  }

  /**
   * Initializes the weights randomly.
   */
  public void initWeights() {
    // TODO
    // initialize with bias inside as the last column
    // W = SimpleMatrix...
    // U for the score
    // U = SimpleMatrix...
  }

  /**
   * Simplest SGD training
   */
  public void train(List<Datum> _trainData) {
    // TODO
  }

  public void test(List<Datum> testData) {
    // TODO
  }

}