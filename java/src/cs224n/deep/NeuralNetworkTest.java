package cs224n.deep;

import static org.junit.Assert.*;

import org.ejml.simple.SimpleMatrix;
import org.junit.Test;

import cs224n.util.Configuration;

public class NeuralNetworkTest {


  @Test
  public void testInitializeMatrices() {
    fail("Not yet implemented");
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
    fail("Not yet implemented");
  }

  @Test
  public void testGetWeightedInputAndActivation() {
    fail("Not yet implemented");
  }

  @Test
  public void testCrossEntropyCost() {
    fail("Not yet implemented");
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
  public void testEmpiricalNabla() {
    fail("Not yet implemented");
  }

  @Test
  public void testCheckGradient() {
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
  
  
}
