package cs224n.util;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;

import org.ejml.simple.SimpleMatrix;
import org.junit.BeforeClass;
import org.junit.Test;

public class WordMapTest {
  
  public static WordMap map;
  public static Configuration conf;

  @BeforeClass
  public static void oneTimeSetUp() throws IOException {
    conf = new Configuration();
    conf.setVocabFilepath("../testdata/vocab.txt");
    conf.setWordVecFilepath("../testdata/wordvec.txt");
    conf.setUnknownWord("UNK");
    conf.setAllLowercase(true);
    map = new WordMap(conf);
  }
  

  @Test
  public void testGetWordNum() {
    HashMap<String, Integer> w2n = map.getWordToNum();
    HashMap<Integer, String> n2w = map.getNumToWord();
    assertTrue(w2n.size() == 7 && n2w.size() == 7);
    assertTrue(map.getWordNum("a") == 0
        && map.getWordNum("short") == 1
        && map.getWordNum("vocab") == 2
        && map.getWordNum("to") == 3
        && map.getWordNum("check") == 4
        && map.getWordNum("UNK") == 5
        && map.getWordNum("functions") == 6);
    assertEquals(n2w.get(0), "a");
    assertEquals(n2w.get(1), "short");
    assertEquals(n2w.get(2), "vocab");
    assertEquals(n2w.get(3), "to");
    assertEquals(n2w.get(4), "check");
    assertEquals(n2w.get(5), "unk");  // NOTE: UNK is lowercase as FF currently lowercases everything
    assertEquals(n2w.get(6), "functions");
  }
  
  @Test
  public void testGetWordNumUnknown() {
    int id = map.getWordNum("()#$UWEOIFJWE)(#J!@");
    assertTrue(id == 5);
  }


  @Test
  public void testInitializeVocab() throws IOException {
    HashMap<String, Integer> w2n = map.getWordToNum();
    HashMap<Integer, String> n2w = map.getNumToWord();
    assertTrue(w2n.size() == 7 && n2w.size() == 7);
    assertTrue(map.getWordNum("a") == 0
        && map.getWordNum("short") == 1
        && map.getWordNum("vocab") == 2
        && map.getWordNum("to") == 3
        && map.getWordNum("check") == 4
        && map.getWordNum("UNK") == 5
        && map.getWordNum("functions") == 6);
    assertEquals(n2w.get(0), "a");
    assertEquals(n2w.get(1), "short");
    assertEquals(n2w.get(2), "vocab");
    assertEquals(n2w.get(3), "to");
    assertEquals(n2w.get(4), "check");
    assertEquals(n2w.get(5), "unk");  // NOTE: UNK is lowercase as FF currently lowercases everything
    assertEquals(n2w.get(6), "functions");
  }

  @Test
  public void testGetWordVector() {
    double tolerance = 1e-5;
    SimpleMatrix wordvec, answer;
    
    wordvec = map.getWordVector(0);
    answer = simpleAnswer(8, 2, 5, 3);
    assertTrue(wordvec.isIdentical(answer, tolerance));
    
    wordvec = map.getWordVector(1);
    answer = simpleAnswer(9, -1, 3, 2);
    assertTrue(wordvec.isIdentical(answer, tolerance));
    
    wordvec = map.getWordVector(2);
    answer = simpleAnswer(0, 0, 0, 0);
    assertTrue(wordvec.isIdentical(answer, tolerance));
    
    wordvec = map.getWordVector(3);
    answer = simpleAnswer(-1, -3, -7, -9);
    assertTrue(wordvec.isIdentical(answer, tolerance));
    
    wordvec = map.getWordVector(4);
    answer = simpleAnswer(3, -0.2, 999, -0);
    assertTrue(wordvec.isIdentical(answer, tolerance));
  }
  
  public SimpleMatrix simpleAnswer(double... args) {
    return new SimpleMatrix(new double[][]{args});
  }
  
  @Test
  public void testCaseSensitivity() throws IOException {
    // Test the allLowerCase configuration (from oneTimeSetUp)
    assertEquals(map.getWordNum("Vocab"), map.getWordNum("vocab"));
    
    // Test configuration WITH case
    conf.setAllLowercase(false);
    WordMap map2 = new WordMap(conf);
    
    HashMap<String, Integer> w2n = map2.getWordToNum();
    
    assertFalse(w2n.containsKey("unk"));
    assertTrue(w2n.containsKey("UNK"));
    
    // File contains "Vocab" but not "vocab" so it should map to unknown
    assertEquals(map2.getWordNum("Vocab"), 2);
    assertEquals(map2.getWordNum("vocab"), map2.getWordNum("UNK"));
  }

}
