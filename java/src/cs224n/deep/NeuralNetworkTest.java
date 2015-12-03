package cs224n.deep;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.ejml.simple.SimpleMatrix;
import org.junit.Before;
import org.junit.Test;

import cs224n.util.Configuration;
import cs224n.util.PairOfSimpleMatrixArray;

public class NeuralNetworkTest {
  
  NeuralNetwork nn, nn_lambda0;

  @Before
  public void setUp() {
    Configuration conf = new Configuration();
    conf.setTargetEntities("A,B,C"); // Force Output Dimension = 3
    conf.setHiddenDimensions("4");
    conf.setLambda(0.1);
    
    // Final Output Layer
    double[][] arrU = new double[][]{
      {0.0786,  0.2659,  0.4643,  0.9167},
      {0.4063,  0.4590,  0.8579,  0.1534},
      {0.3202,  0.7000,  0.5802,  0.8243}};
    double[][] arrb2 = new double[][]{{-0.5}, {2}, {0}};
    
    // Hidden Layer
    double[][] arrW = new double[][]{
      {0.6084,  0.4024,  0.5827,  0.2394},
      {0.3723,  0.7816,  0.0588,  0.8247},
      {0.5970,  0.4722,  0.4992,  0.8065},
      {0.2073,  0.1346,  0.5692,  0.3756}};
    double[][] arrb1 = new double[][]{{1}, {0}, {0.5}, {-1}};
    
    // Convert these into the SimpleMatrix representations
    SimpleMatrix U = new SimpleMatrix(arrU);
    SimpleMatrix b2 = new SimpleMatrix(arrb2);
    
    List<SimpleMatrix> W = new ArrayList<SimpleMatrix>();
    W.add(new SimpleMatrix(arrW));
    List<SimpleMatrix> b1 = new ArrayList<SimpleMatrix>();
    b1.add(new SimpleMatrix(arrb1));
    
    // Create the Neural Network with these matrices
    nn = new NeuralNetwork(conf, U, b2, W, b1);
    
    // Create a second NN where Lambda = 0 for other tests
    Configuration conf_lambda0 = new Configuration();
    conf_lambda0.setTargetEntities("A,B,C");
    conf_lambda0.setHiddenDimensions("4");
    conf_lambda0.setLambda(0.0);
    nn_lambda0 = new NeuralNetwork(conf_lambda0, U, b2, W, b1);
  }

  @Test
  public void testTanh() {
    NeuralNetwork tester = new NeuralNetwork(new Configuration());
    
    double[][] input = new double[][]{
      {-1, 4},
      {-3, 0.5},
      {-0.112, 0.32}
    };
    SimpleMatrix simpleInput = new SimpleMatrix(input);
    
    SimpleMatrix simpleOutput = tester.tanh(simpleInput);
    double[][] output = simpleToDouble(simpleOutput);
    
    double [][] rightOutput = new double[][]{
      {-0.7615941559557648881194582826, 0.9993292997390670437922433443},
      {-0.9950547536867304513318801852, 0.4621171572600097585023184836},
      {-0.1115340285852550977324280534, 0.3095069212126384621817278440}
    };
    
    doubleArrayEquals(rightOutput, output, 1e-8);
  }

  @Test
  public void testTanhPrime() {
    NeuralNetwork tester = new NeuralNetwork(new Configuration());
    
    double[][] input = new double[][]{
      {-1, 4},
      {-3, 0.5},
      {-0.112, 0.32}
    };
    SimpleMatrix simpleInput = new SimpleMatrix(input);
    
    SimpleMatrix simpleOutput = tester.tanhPrime(simpleInput);
    double[][] output = simpleToDouble(simpleOutput);
    
    double [][] rightOutput = new double[][]{
      {0.4199743416140260693944967390, 0.0013409506830258968799702189},
      {0.0098660371654401912731561696, 0.7864477329659274101496989343},
      {0.9875601604675434987422137365, 0.9042054657214736075227018294}
    };
    
    doubleArrayEquals(rightOutput, output, 1e-8);
  }

  @Test
  public void testSoftmax() {
    NeuralNetwork tester = new NeuralNetwork(new Configuration());
    
    double[][] input = new double[][]{{0.1, 3, 0.5, 9, 4}};
    SimpleMatrix simpleInput = new SimpleMatrix(input);
    
    SimpleMatrix simpleOutput = tester.softmax(simpleInput.transpose());
    double[][] output = simpleToDouble(simpleOutput);
    
    double [][] rightOutput = new double[][]{
      {0.00013509785619018587}, {0.002455288077500551}, {0.00020154231846290785},
      {0.99053390678314446}, {0.0066741649647018919}};
    
    doubleArrayEquals(rightOutput, output, 1e-8);
  }

  @Test
  public void testScore() {
    double[][] arrX = new double[][]{{2, 0, 0.4, 1}};
    SimpleMatrix X = new SimpleMatrix(arrX);
    
    double[] outputs = nn.score(X);
    double[] rightOutputs = new double[]{0.029554632, 0.867061259, 0.103384109};
    
    doubleArrayEquals(new double[][]{outputs}, new double[][]{rightOutputs}, 1e-8);
  }

  @Test
  public void testGetWeightedInputAndActivation() {
    double[][] arrX = new double[][]{{2, 0, 0.4, 1}};
    SimpleMatrix X = new SimpleMatrix(arrX);
    
    PairOfSimpleMatrixArray outputs = nn.getWeightedInputAndActivation(X);
    SimpleMatrix[] weightedInput = outputs.getFirstSimpleMatrixArray();
    SimpleMatrix[] activations = outputs.getSecondSimpleMatrixArray();
    
    double[] answerWeightedHidden = new double[] {2.689280000, 1.592820000, 2.700180000, 0.017880000};
    double[] answerWeightedOutput = new double[] {0.299175423, 3.678044561, 1.551386192};
    doubleArrayEquals(new double[][]{answerWeightedHidden}, simpleToDouble(weightedInput[1].transpose()), 1e-8);
    doubleArrayEquals(new double[][]{answerWeightedOutput}, simpleToDouble(weightedInput[2].transpose()), 1e-8);
    
    double[] answerActivationHidden = new double[] {0.990813467, 0.920580589, 0.991010676, 0.017878095};
    double[] answerActivationOutput = new double[] {0.029554632, 0.867061259, 0.103384109};
    doubleArrayEquals(new double[][]{answerActivationHidden}, simpleToDouble(activations[1].transpose()), 1e-8);
    doubleArrayEquals(new double[][]{answerActivationOutput}, simpleToDouble(activations[2].transpose()), 1e-8);
  }

  @Test
  public void testCrossEntropyCost() {
    double[][] arrX = new double[][]{{2, 0, 0.4, 1}};
    SimpleMatrix X = new SimpleMatrix(arrX);
    
    double[][] arrY = new double[][]{{0, 1, 0}};
    SimpleMatrix Y = new SimpleMatrix(arrY);
    
    double cost = nn_lambda0.crossEntropyCost(X, Y);
    double answerCost = -Math.log(1-0.029554632) - Math.log(0.867061259) - Math.log(1-0.103384109);
    
    assertEquals(answerCost, cost, 1e-8);
    
    double costLambda = nn.crossEntropyCost(X, Y);
    double answerCostLambda = answerCost + 0.1 / 2 * (4.367200530 + 3.876709380);
    
    assertEquals(answerCostLambda, costLambda, 1e-8);
  }

  @Test
  public void testBackprop() {
    fail("Not yet implemented");
  }

  @Test
  public void testUpdateGradient() {
    fail("Not yet implemented");
  }

  @Test
  public void testGetBestOutputClass() {
    fail("Not yet implemented");
  }

  @Test
  public void testValueAt() {
    fail("Not yet implemented");
  }

  
  /**********************************************************
   *  Utility Functions for Testing
   **********************************************************/
  
  public double[][] simpleToDouble(SimpleMatrix x) {
    double[][] out = new double[x.numRows()][x.numCols()];
    for (int i=0; i < x.numRows(); i++)
      for (int j=0; j < x.numCols(); j++)
        out[i][j] = x.get(i,j);
    return out;
  }
  
  public void doubleArrayEquals(double[][] a, double[][] b, double tolerance) {
    for (int i=0; i < a.length; i++)
      for (int j=0; j < a[i].length; j++)
        assertTrue(Math.abs(a[i][j] - b[i][j]) < tolerance);
  }
  
  public void simpleMatrixEquals(SimpleMatrix a, SimpleMatrix b, double tolerance) {
    double[][] arrA = simpleToDouble(a);
    double[][] arrB = simpleToDouble(b);
    doubleArrayEquals(arrA, arrB, tolerance);
  }
  
  
}
