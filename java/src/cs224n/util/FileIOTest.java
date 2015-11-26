package cs224n.util;

import static org.junit.Assert.*;

import java.io.IOException;

import org.ejml.simple.SimpleMatrix;
import org.junit.Test;

public class FileIOTest {

  @Test
  public void testRead() {
    // Not Needed
  }

  @Test
  public void testReadWordVectors() {
    try {
      SimpleMatrix guess = FileIO.readWordVectors("../testdata/wordvec.txt");
      SimpleMatrix answer = new SimpleMatrix(5,4,true,
           8,  2,  5,  3,
           9, -1,  3,  2,
           0,  0,  0,  0,
          -1, -3, -7, -9,
           3, -0.2, 999, -0);
      assertTrue(guess.isIdentical(answer, 1e-10));
    } catch (IOException e) {
      fail("IO Exception");
    }
  }
  
  @Test
  public void testCountColumns() {
    try {
      int rowCount = FileIO.countLines("../testdata/initarrayfromfile.txt");
      int colCount = FileIO.countColumns("../testdata/initarrayfromfile.txt");
      assertTrue(rowCount == 11);
      assertTrue(colCount == 7);
    } catch (IOException e) {
      fail("IO Exception");
    }
  }

  @Test
  public void testCountLines() {
    try {
      int lineCount = FileIO.countLines("../testdata/linecount.txt");
      assertTrue(lineCount == 8);
    } catch (IOException e) {
      fail("IO Exception");
    }
  }

  @Test
  public void testFillDoubleWithLine() {
    double[] box = new double[8];
    String testLine = "-0.3 0.7 8 -0.003 0 99 -1 2732";
    FileIO.fillDoubleWithLine(testLine, box);
    assertTrue(box[0] == -0.3 && box[1] ==  0.7 && box[2] == 8 &&
               box[3] == -0.003 && box[4] == 0 && box[5] == 99 &&
               box[6] == -1 && box[7] == 2732);
  }

}
