package cs224n.deep;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;

import org.ejml.simple.SimpleMatrix;
import org.junit.Test;

public class FeatureFactoryTest {

  @Test
  public void testReadWordVectors() {
    try {
      SimpleMatrix guess = FeatureFactory.readWordVectors("../testdata/wordvec.txt");
      SimpleMatrix answer = new SimpleMatrix(5,4,true,
           8,  2,  5,  3,
           9, -1,  3,  2,
           0,  0,  0,  0,
          -1, -3, -7, -9,
           3, -0.2, 999, -0);
      assert(guess.isIdentical(answer, 1e-10));
    } catch (IOException e) {
      fail("IO Exception");
    }
  }

  @Test
  public void testRowColumnCount() {
    try {
      int rowCount = FeatureFactory.countLines("../testdata/initarrayfromfile.txt");
      int colCount = FeatureFactory.countColumns("../testdata/initarrayfromfile.txt");
      assert(rowCount == 11);
      assert(colCount == 8);
    } catch (IOException e) {
      fail("IO Exception");
    }
  }

  @Test
  public void testCountLines() {
    try {
      int lineCount = FeatureFactory.countLines("../testdata/linecount.txt");
      assert(lineCount == 8);
    } catch (IOException e) {
      fail("IO Exception");
    }
  }

  @Test
  public void testFillDoubleWithLine() {
    double[] box = new double[8];
    String testLine = "-0.3 0.7 8 -0.003 0 99 -1 2732";
    FeatureFactory.fillDoubleWithLine(testLine, box);
    assert(box[0] == -0.3 && box[1] ==  0.7 && box[2] == 8 &&
           box[3] == -0.003 && box[4] == 0 && box[5] == 99 &&
           box[6] == -1 && box[7] == 2732);
  }

  @Test
  public void testInitializeVocab() {
    try {
      FeatureFactory.initializeVocab("../testdata/vocab.txt");
      HashMap<String, Integer> w2n = FeatureFactory.getWordToNum();
      HashMap<Integer, String> n2w = FeatureFactory.getNumToWord();
      assert(w2n.size() == 7 && n2w.size() == 7);
      assert(w2n.get("a") == 0 && w2n.get("short") == 1 && w2n.get("vocab") == 2 &&
             w2n.get("to") == 3 && w2n.get("check") == 4 && w2n.get("UUUNKKK") == 6 &&
             w2n.get("functions") == 7);
      assert(n2w.get(0) == "a" && n2w.get(1) == "short" && n2w.get(2) == "vocab" &&
             n2w.get(3) == "to" && n2w.get(4) == "check" && n2w.get(6) == "UUUNKKK" &&
             n2w.get(7) == "functions");
    } catch (IOException e) {
      fail("IO Exception");
    }
  }
  
  @Test
  public void testGetWordNum() {
    try {
      FeatureFactory.initializeVocab("../testdata/vocab.txt");
      FeatureFactory.setUnknownWord("UNK");
      int id = FeatureFactory.getWordNum("()#$UWEOIFJWE)(#J!@");
      assert(id == 6);
    } catch (IOException e) {
      fail("IO Exception");
    }
  }

}
