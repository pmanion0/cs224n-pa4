package cs224n.deep;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.ejml.simple.SimpleMatrix;
import cs224n.util.Configuration;
import cs224n.util.FileIO;
import cs224n.util.Nabla;
import cs224n.util.PairOfSimpleMatrixArray;
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
  private double lambda, alpha;
  

  
  public NeuralNetwork(Configuration conf) {
    this.inputDim = conf.getWindowSize() * conf.getWordVecDim();
    this.outputDim = conf.getOutputDim();
    this.hiddenDims = conf.getHiddenDimensions();
    this.lambda = conf.getLambda();
    this.alpha = conf.getLearningRate();
    this.W = new ArrayList<SimpleMatrix>();
    this.b1 = new ArrayList<SimpleMatrix>();
    
    //System.out.println(W.get(0));
    initializeMatrices();
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

		double[][] allOnes = new double [inputDim][1];
		for (double[] row : allOnes)
			Arrays.fill(row, 1.0);
		W.add(0, new SimpleMatrix(allOnes)); 
		b1.add(0, new SimpleMatrix(inputDim,1));
		
  	// initialize non-final layer
    for (int hiddenDim : hiddenDims) {
      // [Input:Hidden#1] [Hidden#1:Hidden#2] ... [Hidden#n:Output]
    	int fanOut = hiddenDim;
    	epsilon = Math.sqrt(6) / Math.sqrt(fanIn + fanOut);   	
    	W.add(layer+1, SimpleMatrix.random(fanOut, fanIn, -epsilon, epsilon, rand)); 
    	// initialize to 0;
    	b1.add(layer+1, new SimpleMatrix(fanOut, 1));
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
  			tanhOfZ.set(i, j, Math.tanh(z.get(i, j)));
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
  			tanhPrimeOfZ.set(i, j, 1- Math.tanh(z.get(i, j)) * Math.tanh(z.get(i, j)) );
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
  	
  	PairOfSimpleMatrixArray weightedInputAndActivation = getWeightedInputAndActivation(X);
  	SimpleMatrix probsMatrix = weightedInputAndActivation.getSecondSimpleMatrixArray()[hiddenDims.length + 1];

  	double[] probs = new double[outputDim];
  	
  	for ( int i = 0; i < outputDim; i++) {
  		probs[i] = probsMatrix.get(i, 0);
  	}
  	
  	return probs;
  }
  

  public PairOfSimpleMatrixArray getWeightedInputAndActivation(SimpleMatrix X) {
  	int hiddenLayerSize = hiddenDims.length;
  	// a^l
  	SimpleMatrix [] activation = new SimpleMatrix[hiddenLayerSize + 2];
  	// z^l
  	SimpleMatrix [] weightedInput = new SimpleMatrix[hiddenLayerSize + 2];
  	
  	// forward feed
  	for (int i = 0; i < hiddenLayerSize + 2; i++) {
  		// input layer
  		if (i == 0) {
  			double[][] allOnes = new double [inputDim][1];
  			for (double[] row : allOnes)
  				Arrays.fill(row, 1.0);
  			weightedInput[i] = new SimpleMatrix(allOnes); 
  			activation[i] = X.transpose();
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
  	
  	return new PairOfSimpleMatrixArray(weightedInput, activation);
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
  public PairOfSimpleMatrixArray backprop(SimpleMatrix X, SimpleMatrix Y) {
    epochCount++;
    int hiddenLayerSize = hiddenDims.length;
    // \partial J / \partial b
    SimpleMatrix[] nabla_b = new SimpleMatrix[hiddenLayerSize+2];
    // \partial J / \partial W (including U)
    SimpleMatrix[] nabla_w = new SimpleMatrix[hiddenLayerSize+2]; 
    SimpleMatrix delta;
    
    PairOfSimpleMatrixArray weightedInputAndActivation;
    weightedInputAndActivation = getWeightedInputAndActivation(X);
    SimpleMatrix[] weightedInput = weightedInputAndActivation.getFirstSimpleMatrixArray();
    SimpleMatrix[] activation = weightedInputAndActivation.getSecondSimpleMatrixArray();
    
    
    // output layer: delta_L 
    delta = activation[hiddenLayerSize+1].minus(Y.transpose()); // 5 by 1
    nabla_b[hiddenLayerSize+1] = delta;
    //System.out.println("delta: " + delta);
    //System.out.println("activation[hiddenLayerSize]: " + activation[hiddenLayerSize]);
    nabla_w[hiddenLayerSize+1] = delta.mult(activation[hiddenLayerSize].transpose());
    
    // hidden layer, backprop
    SimpleMatrix W_l_plus_one;
    SimpleMatrix weightedInput_l; //z^l
    SimpleMatrix activation_l_minus_one;
    SimpleMatrix delta_l;    
    
    for (int l = hiddenLayerSize; l>=1; l--) {
    	  	
    	if (l == hiddenLayerSize) {
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
    return new PairOfSimpleMatrixArray(nabla_w, nabla_b);
  }
  
  // TODO: need a method to update gradient
  public SimpleMatrix updateGradient(SimpleMatrix X, PairOfSimpleMatrixArray nabla, int trainingDataSize) {
  	SimpleMatrix[] nabla_w = nabla.getFirstSimpleMatrixArray();
  	SimpleMatrix[] nabla_b = nabla.getSecondSimpleMatrixArray();
  	int hiddenLayerSize = hiddenDims.length;
  	
  	// update output layer U, b2
  	double coef = alpha * (lambda / trainingDataSize);
  	U = U.scale(1 - coef).minus(nabla_w[hiddenLayerSize+1].scale(alpha));
  	b2 = b2.minus(nabla_b[hiddenLayerSize+1].scale(alpha));
  	
  	// update hidden layer W and b1
  	for (int l = 1; l <= hiddenLayerSize; l++) {
  		W.set(l, W.get(l).scale(1 - coef).minus(nabla_w[l].scale(alpha)));
  		b1.set(l, b1.get(l).minus(nabla_b[l].scale(alpha)));
  	}
  	
  	// update X
  	SimpleMatrix X_transpose = X.transpose().minus(nabla_b[0].scale(alpha));
  	return X_transpose.transpose();
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
