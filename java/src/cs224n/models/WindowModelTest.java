package cs224n.models;

import static org.junit.Assert.*;

import java.io.IOException;

import org.ejml.simple.SimpleMatrix;
import org.junit.Test;

import cs224n.util.Configuration;

public class WindowModelTest {

  @Test
  public void testUpdateWordVector() {
    fail("Not yet implemented");
  }

  @Test
  public void testIdsToWordVector() throws IOException {
    Configuration conf = new Configuration();
    conf.setVocabFilepath("../testdata/vocab.txt");
    conf.setWordVecFilepath("../testdata/windowmodel_wordvec.txt");
    conf.setWordVecDim(4);
    WindowModel wm = new WindowModel(conf);
    
    SimpleMatrix vector = wm.idsToWordVector(new int[]{4,1,6});
    double[] vectorArr = simpleToDouble(vector);
    double[] answerVectorArr = new double[] {3, -0.2, 0.32, -0, 0.1, -1, 3, 2, 1, 0.2, -0.1, 0.4};
    doubleArrayEquals(new double[][]{answerVectorArr}, new double[][]{vectorArr}, 1e-8);
    
    SimpleMatrix vector2 = wm.idsToWordVector(new int[]{1,1,5});
    double[] vectorArr2 = simpleToDouble(vector2);
    double[] answerVectorArr2 = new double[] {0.1, -1, 3, 2, 0.1, -1, 3, 2, 0.1, 3, -4, 0.3};
    doubleArrayEquals(new double[][]{answerVectorArr2}, new double[][]{vectorArr2}, 1e-8);
  }

  @Test
  public void testTargetToVector() {
    fail("Not yet implemented");
  }
  
  
  
  /**********************************************************
   *  Utility Functions for Testing
   **********************************************************/
  
  public double[] simpleToDouble(SimpleMatrix x) {
    if (x.numRows() < x.numCols())
      x = x.transpose();
    double[] out = new double[x.numRows()];
    for (int i=0; i < x.numRows(); i++)
      out[i] = x.get(i,0);
    return out;
  }
  
  public double[][] simpleToDoubleDouble(SimpleMatrix x) {
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
