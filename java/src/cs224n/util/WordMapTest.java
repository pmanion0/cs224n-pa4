package cs224n.util;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;

import org.junit.BeforeClass;
import org.junit.Test;

public class WordMapTest {
  
  public static WordMap map;

  @BeforeClass
  public static void oneTimeSetUp() throws IOException {
    Configuration conf = new Configuration();
    conf.setVocabFilepath("../testdata/vocab.txt");
    conf.setUnknownWord("UNK");
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
    fail("Not yet implemented");
  }

}
