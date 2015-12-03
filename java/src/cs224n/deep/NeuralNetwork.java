package cs224n.deep;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.ejml.simple.SimpleMatrix;

import cs224n.gradcheck.ObjectiveFunction;
import cs224n.util.Configuration;
import cs224n.util.PairOfSimpleMatrixArray;

public class NeuralNetwork implements ObjectiveFunction {
  
  private final int inputDim, outputDim;
  private final int[] hiddenDims;
  private List<SimpleMatrix> W;  // Input-to-Hidden and Hidden-to-Hidden Weights
  private SimpleMatrix U;        // Final Hidden-to-Output Weights
  private List<SimpleMatrix> b1; // Bias for Input + All Hidden Layers
  private SimpleMatrix b2;       // Bias for Output Layer
  private double lambda, alpha;
  private Random randomGenerator = new Random(34983);

  /** Create a NeuralNetwork with the provided configuration */
  public NeuralNetwork(Configuration conf) {
    // Force other constructor to initialize all matrices
    this(conf, null, null, null, null);
  }
  
  /** Create a Neural Network using the user defined matrices
   *  NOTE: You must ensure the Configuration matches the matrix dimensions  */
  public NeuralNetwork(Configuration conf, SimpleMatrix U, SimpleMatrix b2,
      List<SimpleMatrix> W, List<SimpleMatrix> b1) {
    this.inputDim = conf.getWindowSize() * conf.getWordVecDim();
    this.outputDim = conf.getOutputDim();
    this.hiddenDims = conf.getHiddenDimensions();
    this.lambda = conf.getLambda();
    this.alpha = conf.getLearningRate();

    // Initialize any matrices not provided by the user
    this.U  = (U==null  ? initializeU()  : U);
    this.W  = (W==null  ? initializeW()  : W);
    this.b1 = (b1==null ? initializeB1() : b1);
    this.b2 = (b2==null ? initializeB2() : b2);
  }
  
  
  /** Initialize the final hidden-to-output layer weights U
   *  - Requires hiddenDims and outputDim to be defined  */
  public SimpleMatrix initializeU() {
    int lastHiddenDim = hiddenDims[hiddenDims.length - 1];
    double epsilonLast = Math.sqrt(6) / Math.sqrt(outputDim + lastHiddenDim);
    SimpleMatrix output = SimpleMatrix.random(outputDim, lastHiddenDim, -epsilonLast, epsilonLast, randomGenerator);
    return output;
  }
  
  /** Initialize the input-to-hidden and hidden-to-hidden layer weights W
   *  - Requires inputDim and hiddenDims to be defined  */
  public List<SimpleMatrix> initializeW() {
    List<SimpleMatrix> output = new ArrayList<SimpleMatrix>();
    
    // Initialize All Hidden Layer Weights (random weights)
    int fanIn = inputDim;
    for (int layer=0; layer < hiddenDims.length; layer++) {
      int fanOut = hiddenDims[layer];
      double epsilon = Math.sqrt(6) / Math.sqrt(fanIn + fanOut);  
      output.add(layer, SimpleMatrix.random(fanOut, fanIn, -epsilon, epsilon, randomGenerator));
      fanIn = fanOut;
    }
    return output;
  }
  
  /** Initialize the bias terms for all hidden layers b1
   *  - Requires inputDim and hiddenDims to be defined  */
  public List<SimpleMatrix> initializeB1() {
    List<SimpleMatrix> output = new ArrayList<SimpleMatrix>();
    
    // Initialize All Hidden Bias Layer Terms (all 0's)
    for (int layer=0; layer < hiddenDims.length; layer++) {
      int layerSize = hiddenDims[layer];
      output.add(layer, new SimpleMatrix(layerSize, 1));
    }
    return output;
  }
  
  /** Initialize the final layer bias terms b2 
   *  - Requires outputDim to be defined  */
  public SimpleMatrix initializeB2() {
    return new SimpleMatrix(outputDim,1);
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
  public double[] score(SimpleMatrix X) { // Feed Forward + Return Output Layer Probabilities
  	
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
  	
  	SimpleMatrix [] activation = new SimpleMatrix[hiddenLayerSize + 2]; // a^l
  	SimpleMatrix [] weightedInput = new SimpleMatrix[hiddenLayerSize + 2]; // z^l
  	
  	for (int i = 0; i < hiddenLayerSize + 2; i++) {
  		if (i == 0) {
  		  // Input Layer
  			weightedInput[i] = X.transpose();
  			activation[i] = X.transpose();
  		} else if (i == hiddenLayerSize + 1) {
  		  // Output Layer
  			weightedInput[i] = U.mult(activation[i-1]).plus(b2);
  			activation[i] = softmax(weightedInput[i]);
  		} else {
  		  // Hidden Layer
  			weightedInput[i] = W.get(i-1).mult(activation[i-1]).plus(b1.get(i-1));
  			activation[i] = tanh(weightedInput[i]);
  		}
  	}
  	return new PairOfSimpleMatrixArray(weightedInput, activation);
  }
  
  /**
   * Calculate the network's cross entropy error for the input X and output Y
   */
  public double crossEntropyCost(SimpleMatrix X, SimpleMatrix Y) {
  	double[] probs = score(X);
  	double error = 0.0;
  	// Calculate Negative Log-Likelihood -- J(theta)
  	for (int i = 0; i < outputDim; i++) {
  		double y_i = Y.get(i);
  		double p_i = probs[i];
  		error -= y_i * Math.log(p_i);
  	}
  	// Include Regularization Cost -- J_R(theta)
  	for (int i = 0; i < W.size(); i++) {
  		error += lambda / 2 * W.get(i).elementMult(W.get(i)).elementSum();
  	}
  	error += lambda / 2 * U.elementMult(U).elementSum();
  	
  	return error;
  }
  
  /**
   * Run one iteration of Backpropagation based on input X and correct output Y
   * @param X - Input 
   * @param Y - Correct output labels for the input example X
   * @return layer-by-layer of the gradient for the cost function
   */
  public PairOfSimpleMatrixArray backprop(SimpleMatrix X, SimpleMatrix Y) {
    int hiddenLayerSize = hiddenDims.length;
    SimpleMatrix[] nabla_b = new SimpleMatrix[hiddenLayerSize+2]; // Bias Gradient (b1 and b2)
    SimpleMatrix[] nabla_w = new SimpleMatrix[hiddenLayerSize+2]; // Weight Gradients (W and U)
    
    // Find the WeightedInputs and Activations
    PairOfSimpleMatrixArray weightedInputAndActivation = getWeightedInputAndActivation(X);
    SimpleMatrix[] weightedInput = weightedInputAndActivation.getFirstSimpleMatrixArray();
    SimpleMatrix[] activation = weightedInputAndActivation.getSecondSimpleMatrixArray();
    
    // Output Layer Gradients
    SimpleMatrix predictions = activation[hiddenLayerSize+1];
    SimpleMatrix activationBelow = activation[hiddenLayerSize];
    SimpleMatrix deltaCurrent = predictions.minus(Y.transpose());

    nabla_b[hiddenLayerSize+1] = deltaCurrent;
    nabla_w[hiddenLayerSize+1] = deltaCurrent.mult(activationBelow.transpose());
    
    // Hidden Layer Gradients:
    SimpleMatrix weightToLayerAbove;
    SimpleMatrix weightedInputCurrent; //z^l
    
    
    for (int l = hiddenLayerSize; l>=1; l--) {
      weightToLayerAbove = (l==hiddenLayerSize) ? U : W.get(l);
    	
      weightedInputCurrent = weightedInput[l]; //z^l
    	activationBelow = activation[l-1]; //a^{l-1}
    	deltaCurrent = (weightToLayerAbove.transpose().mult(deltaCurrent)).elementMult(tanhPrime(weightedInputCurrent));
    	nabla_b[l] = deltaCurrent;
    	nabla_w[l] = deltaCurrent.mult(activationBelow.transpose());
    }
    
    // Input Layer Gradient
    weightToLayerAbove = W.get(0);
    deltaCurrent = weightToLayerAbove.transpose().mult(deltaCurrent);
    nabla_b[0] = deltaCurrent;
    nabla_w[0] = deltaCurrent;
    
    return new PairOfSimpleMatrixArray(nabla_w, nabla_b);
  }
  
  // TODO: need a method to update gradient
  public SimpleMatrix updateGradient(SimpleMatrix X, PairOfSimpleMatrixArray nabla) {
  	SimpleMatrix[] nabla_w = nabla.getFirstSimpleMatrixArray();
  	SimpleMatrix[] nabla_b = nabla.getSecondSimpleMatrixArray();
  	int hiddenLayerSize = hiddenDims.length;
  	
  	// update output layer U, b2
  	double coef = alpha * lambda;
  	U = U.scale(1 - coef).minus(nabla_w[hiddenLayerSize+1].scale(alpha));
  	b2 = b2.minus(nabla_b[hiddenLayerSize+1].scale(alpha));
  	
  	// update hidden layer W and b1
  	for (int l=0; l < hiddenLayerSize; l++) {
  		W.set(l, (W.get(l).scale(1 - coef)).minus(nabla_w[l+1].scale(alpha)));
  		b1.set(l, b1.get(l).minus(nabla_b[l+1].scale(alpha)));
  	}
  	
  	// update X
  	SimpleMatrix X_transpose = X.transpose().minus(nabla_b[0].scale(alpha));
  	return X_transpose.transpose();
  }

  public SimpleMatrix[] empiricalNabla(SimpleMatrix X, SimpleMatrix Y) {
    double EPSILON = 1e-4;
    int hiddenLayerSize = hiddenDims.length;
    SimpleMatrix[] nabla_w = new SimpleMatrix[hiddenLayerSize + 2];
  
    for (int i = 0; i < hiddenLayerSize+2; i++) {
      SimpleMatrix temp = null;
      
      if (i == 0) {
        nabla_w[i] = new SimpleMatrix(inputDim, 1);
        for (int nr = 0; nr < inputDim; nr++) {
          SimpleMatrix increment = new SimpleMatrix(inputDim, 1);
          increment.set(nr, 0, EPSILON);
          SimpleMatrix X_plus = X.transpose().plus(increment);
          SimpleMatrix X_minus = X.transpose().minus(increment);
          double error_plus = crossEntropyCost(X_plus.transpose(), Y);
          double error_minus = crossEntropyCost(X_minus.transpose(), Y);
          nabla_w[i].set(nr, 0, (error_plus - error_minus) / 2 / EPSILON);
        }
        continue;
      }
      else if ( i == hiddenLayerSize + 1) {
        temp = U;
      }
      else {
        temp = W.get(i-1);
      }
      
      nabla_w[i] = new SimpleMatrix(temp.numRows(), temp.numCols());
    
      // loop through element in nabla_w
      for (int nr = 0; nr < temp.numRows(); nr++) {
        for (int nc = 0; nc < temp.numCols(); nc++) {
          double base = temp.get(nr, nc);
          temp.set(nr, nc, base + EPSILON);
          double error_plus = crossEntropyCost(X, Y);
          temp.set(nr, nc, base - EPSILON);
          double error_minus = crossEntropyCost(X, Y);
          // reset
          temp.set(nr, nc, base);
          nabla_w[i].set(nr, nc, (error_plus - error_minus) / (2 * EPSILON));
        }
      }
    }
    return nabla_w; 
  }
  
  public void checkGradient(SimpleMatrix[] nabla, SimpleMatrix[] empNabla) {
    boolean flag = true;
    for (int i = 0; i < nabla.length; i++) {
      //System.out.println("nabla: " + nabla[i]);
      //System.out.println("empNabla: " + empNabla[i]);
      double diff = nabla[i].minus(empNabla[i]).elementMaxAbs();
      if (diff >= 1e-8) {
        System.err.println("Gradient check failed!");
        System.err.println(diff);
        flag = false;
      }
    }
    
    if (flag) System.err.println("Gradient check passed!");
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
  
  public double valueAt(SimpleMatrix Y, SimpleMatrix X) { 
    return crossEntropyCost(X, Y);
  }
  
  public List<SimpleMatrix> getWeightList() {
    List<SimpleMatrix> weights = new ArrayList<SimpleMatrix>();
    for (SimpleMatrix w_i : this.W)
      weights.add(w_i);
    weights.add(U);
    return weights;
  }

}
