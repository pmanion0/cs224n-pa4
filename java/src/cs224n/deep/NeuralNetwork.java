package cs224n.deep;

import java.util.List;

import org.ejml.simple.SimpleMatrix;

public class NeuralNetwork {
  
  private final int inputDim, outputDim;
  private final int[] hiddenDims;
  private int epochCount = 0;
  private List<SimpleMatrix> parameters;
  private double lambda = 0, alpha = 1e-4;
  
  public NeuralNetwork(int inputDim, int[] hiddenDims, int outputDim) {
    this.inputDim = inputDim;
    this.outputDim = outputDim;
    this.hiddenDims = hiddenDims;
    initializeMatrices();
  }
  
  /**
   * Set the regularization constant lambda
   */
  public void setLambda(double lambda) {
    this.lambda = lambda;
  }
  /**
   * Set the learning rate alpha
   */
  public void setAlpha(double alpha) {
    this.lambda = alpha;
  }

  
  /**
   * Randomly initialize all parameters for the network
   */
  public void initializeMatrices() {
    // Add a SimpleMatrix to parameters list for every layer of the network
    for (int hiddenDim : hiddenDims) {
      // [Input:Hidden#1] [Hidden#1:Hidden#2] ... [Hidden#n:Output]
    }
  }
  
  
  /**
   * Return the scores for each output class after scoring the an input X
   * @param X - input array of dimension {Window Size} x {Word Vector Dimension}
   * @return array of scores for each possible output class
   */
  public double[] score(SimpleMatrix X) {
    // Run X through the entire network and return the score for each output class
    return null;
  }
  
  
  /**
   * Run one iteration of Backpropagation based on input X and correct output Y
   * @param X - Input 
   * @param Y - Correct output labels for the input example X
   * @return the initial error rate on the example
   */
  public double runBackprop(SimpleMatrix X, SimpleMatrix Y) {
    epochCount++;
    return 0.0;
  }
  public double runBackprop(List<SimpleMatrix> Xs, List<SimpleMatrix> Ys) {
    // We could implement another one based on a bit list of examples too if needed
    epochCount++;
    return 0.0;
  }
  
  
  /**
   * Return the index of the best output class for an input example X
   * @return index of the highest scoring output class
   */
  public int getBestOutputClass(SimpleMatrix X) {
    double[] scores = score(X);
    int bestOutputClass = -1;
    double bestScore = 0.0;
    for (int i=0; i < scores.length; i++) {
      if (scores[i] > bestScore) {
        bestScore = scores[i];
        bestOutputClass = i;
      }
    }
    return bestOutputClass;
  }

}
