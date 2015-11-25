package cs224n.deep;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.ejml.simple.SimpleMatrix;
import cs224n.util.Nabla;
import cs224n.util.WeightedInputAndActivation;

public class NeuralNetwork {
  
  private final int inputDim, outputDim;
  private final int[] hiddenDims;
  private int epochCount = 0;
  // weight matrix for non-final layer
  private List<SimpleMatrix> W;
  // weight matrix for final layer
  private SimpleMatrix U;
  // bias for non-final layer
  private List<SimpleMatrix> b1;
  // bias for final layer
  private SimpleMatrix b2;
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
  	Random rand = new Random();
  	int fanIn = inputDim;
  	int layer = 0;
  	double epsilon;

  	// initialize non-final layer
    for (int hiddenDim : hiddenDims) {
      // [Input:Hidden#1] [Hidden#1:Hidden#2] ... [Hidden#n:Output]
    	int fanOut = hiddenDim;
    	epsilon = Math.sqrt(6) / Math.sqrt(fanIn + fanOut);
    	W.add(layer, SimpleMatrix.random(fanOut, fanIn, -epsilon, epsilon, rand)); 
    	// initialize to 0;
    	b1.add(layer, new SimpleMatrix(fanOut, 1));
    	layer++;
      // update fanIn
    	fanIn = hiddenDim;
    }
    
    // initialize final layer
    int lastHiddenDim = hiddenDims[hiddenDims.length - 1];
    double epsilonLast = Math.sqrt(6) / Math.sqrt(outputDim + lastHiddenDim);
    U = SimpleMatrix.random(outputDim, lastHiddenDim, -epsilonLast, epsilonLast, rand);
    b2 = new SimpleMatrix(outputDim,1);
  }
  
  /**
   * Return tanh of input matrix z
   * @param z - input SimpleMatrix
   * @return a Simple Matrix of tanh(z)
   */
  public SimpleMatrix tanh(SimpleMatrix z) {
  	int numOfRows = z.numRows(), numOfCols = z.numCols();
  	SimpleMatrix tanhOfZ = new SimpleMatrix(numOfRows, numOfCols);
  	for (int i = 0; i < numOfRows; i++) {
  		for (int j = 0; j < numOfCols; j++) {
  			tanhOfZ.set(numOfRows, numOfCols, Math.tanh(z.get(i, j)));
  		} // end j
  	} // end i
  	return tanhOfZ;
  }
  
  
  /**
   * Return the derivative of tanh of a given input z
   * @param z
   * @return a Simple Matrix of tanh'(z) = 1 - tanh^2(z)
   */
  public SimpleMatrix tanhPrime(SimpleMatrix z) {
  	int numOfRows = z.numRows(), numOfCols = z.numCols();
  	SimpleMatrix tanhPrimeOfZ = new SimpleMatrix(numOfRows, numOfCols);
  	for (int i = 0; i < numOfRows; i++) {
  		for (int j = 0; j < numOfCols; j++) {
  			tanhPrimeOfZ.set(numOfRows, numOfCols, 1- Math.tanh(z.get(i, j)) * Math.tanh(z.get(i, j)) );
  		} // end j
  	} // end i
  	return tanhPrimeOfZ;
  }
  
  
  /**
   * Return softmax of input matrix z
   * 
   */
  public SimpleMatrix softmax(SimpleMatrix z) {
  	// check input dimension of z
  	if (!(z.numRows() == outputDim && z.numCols() == 1)) {
  		System.err.println("ERROR: Dimenson error for computing softmax");
  	}
  	
  	SimpleMatrix probs = new SimpleMatrix(outputDim, 1);
  	double bottom = 0;
  	
  	for (int i = 0; i < outputDim; i++) {
  		bottom += Math.exp(z.get(i,0));
  	}
  	for (int i = 0; i < outputDim; i++) {
  		probs.set(i, 0, Math.exp(z.get(i,0)) / bottom);
  	}
  	
  	return probs;
  }
  
  
  /**
   * Return the scores for each output class after scoring the an input X
   * @param X - input array of dimension {Window Size} x {Word Vector Dimension}
   * @return array of scores for each possible output class
   */
  // feed forward 
  public double[] score(SimpleMatrix X) {
    // Run X through the entire network and return the score for each output class
    // iterate through weight matrix W
  	int numOfLayers = W.size();
  	SimpleMatrix Z_i = W.get(0).mult(X).plus(b1.get(0)), Z_i_next;
  	
  	// h = f(wx + b1) 
  	for (int i = 0; i < numOfLayers; i++) {
  		Z_i_next = tanh(W.get(i).mult(Z_i).plus(b1.get(i)));
  		Z_i = Z_i_next;
  	}  
  	
  	SimpleMatrix probsMatrix = softmax(U.mult(Z_i).plus(b2));
  	double[] probs = new double[outputDim];
  	
  	for ( int i = 0; i < outputDim; i++) {
  		probs[i] = probsMatrix.get(i, 0);
  	}
  	
  	return probs;
  }
  

  public WeightedInputAndActivation getWeightedInputAndActivation(SimpleMatrix X) {
  	int hiddenLayerSize = hiddenDims.length;
  	// a^l
  	SimpleMatrix [] activation = new SimpleMatrix[hiddenLayerSize + 2];
  	// z^l
  	SimpleMatrix [] weightedInput = new SimpleMatrix[hiddenLayerSize + 2];
  	
  	// forward feed
  	for (int i = 0; i < hiddenLayerSize + 1; i++) {
  		// input layer
  		if (i == 0) {
  			double[][] allOnes = new double [inputDim][1];
  			Arrays.fill(allOnes, 1);
  			weightedInput[i] = new SimpleMatrix(allOnes); 
  			activation[i] = X;
  		} 
  		// output layer
  		else if (i == hiddenLayerSize + 1) {
  			weightedInput[i] = U.mult(activation[i-1]).plus(b2);
  			activation[i] = softmax(weightedInput[i]);
  		}
  		// hidden layers
  		else {
  			weightedInput[i] = W.get(i).mult(activation[i-1]).plus(b1.get(i));
  			activation[i] = tanh(weightedInput[i]);
  		}
  	} // end for
  	
  	return new WeightedInputAndActivation(weightedInput, activation);
  }
  
  
  public SimpleMatrix crossEntropyPrime(SimpleMatrix outputActivation, SimpleMatrix Y) {
  	return outputActivation.minus(Y);
  }
  
  /**
   * Run one iteration of Backpropagation based on input X and correct output Y
   * @param X - Input 
   * @param Y - Correct output labels for the input example X
   * @return layer-by-layer of the gradient for the cost function
   */
  public Nabla backprop(SimpleMatrix X, SimpleMatrix Y) {
    epochCount++;
    int hiddenLayerSize = hiddenDims.length;
    // \partial J / \partial b
    SimpleMatrix[] nabla_b = new SimpleMatrix[hiddenLayerSize+2];
    // \partial J / \partial W (including U)
    SimpleMatrix[] nabla_w = new SimpleMatrix[hiddenLayerSize+2]; 
    
    WeightedInputAndActivation weightedInputAndActivation;
    weightedInputAndActivation = getWeightedInputAndActivation(X);
    SimpleMatrix[] weightedInput = weightedInputAndActivation.weightedInput;
    SimpleMatrix[] activation = weightedInputAndActivation.activation;
    
    
    // output layer: delta_L 
    SimpleMatrix delta = activation[hiddenLayerSize+2].minus(Y);
    nabla_b[hiddenLayerSize+2] = delta;
    nabla_w[hiddenLayerSize+2] = delta.mult(activation[hiddenLayerSize+1].transpose());
    
    // hidden layer, backprop
    SimpleMatrix W_l_plus_one;
    SimpleMatrix weightedInput_l; //z^l
    SimpleMatrix activation_l_minus_one;
    SimpleMatrix delta_l;    
    
    for (int l = hiddenLayerSize+1; l>=1; l--) {
    	  	
    	if (l == (hiddenLayerSize+1)) {
    		W_l_plus_one = U;
    	}
    	else {
    		W_l_plus_one = W.get(l+1);
    	}
    	
    	weightedInput_l = weightedInput[l]; //z^l
    	activation_l_minus_one = activation[l-1]; //a^{l-1}
    	delta_l = W_l_plus_one.transpose().mult(delta).elementMult(tanhPrime(weightedInput_l));
    	nabla_b[l] = delta_l;
    	nabla_w[l] = delta_l.mult(activation_l_minus_one.transpose());
    	delta = delta_l;   	
    }
    
    // input layer;
    SimpleMatrix delta_0 = W.get(1).transpose().mult(delta);
    nabla_b[0] = delta_0;
    nabla_w[0] = delta_0;
    return new Nabla(nabla_w, nabla_b);
  }
  
  // TODO: need a method to update gradient

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
