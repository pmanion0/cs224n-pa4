package cs224n.util;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import cs224n.deep.Datum;

public class WordWindowTest {
  
  static WordMap map;
  static List<Datum> example = new ArrayList<Datum>();

  @BeforeClass
  public static void oneTimeSetUp() throws IOException {
    // Create the example for testing
    Datum John   = new Datum("John",   "PER");
    Datum joined = new Datum("joined", "O");
    Datum PETA   = new Datum("PETA",   "ORG");
    Datum in     = new Datum("in",     "O");
    Datum Dallas = new Datum("Dallas", "LOC");
    Datum Texas  = new Datum("Texas", "LOC");
    example.add(John);
    example.add(joined);
    example.add(PETA);
    example.add(in);
    example.add(Dallas);
    example.add(Texas); // Unknown Word
    
    Configuration conf = new Configuration();
    conf.setVocabFilepath("../testdata/wordwindow_vocab.txt");
    conf.setUnknownWord("UUUNKKK");
    conf.setTargetEntities("PER,O,LOC,ORG");
    map = new WordMap(conf);
  }

  @Test
  public void testWordWindow3() {
    // Check the initial word windows are correct (SIZE == 3)
    WordWindow test = new WordWindow(example, 3, map);
    
    String[] windowStrAnswer = new String[]{"<s>","John","joined"};
    assertArrayEquals(windowStrAnswer, test.getWordArray());
    
    int[] windowIntAnswer = new int[]{5,0,1};
    assertArrayEquals(windowIntAnswer, test.getIDArray());
    
    assertTrue(test.getTargetLabel().equals("PER"));
    assertTrue(test.getTargetLabelID() == 0);
  }

  @Test
  public void testWordWindow5() {
    // Check the initial word windows are correct (SIZE == 5)
    WordWindow test = new WordWindow(example, 5, map);
    
    String[] windowStrAnswer = new String[]{"<s>","<s>","John","joined","PETA"};
    assertArrayEquals(windowStrAnswer, test.getWordArray());
    
    int[] windowIntAnswer = new int[]{5,5,0,1,2};
    assertArrayEquals(windowIntAnswer, test.getIDArray());
    
    assertTrue(test.getTargetLabel().equals("PER"));
    assertTrue(test.getTargetLabelID() == 0);
  }

  
  @Test
  public void testRollWindow() {
    // Check the rolling windows work for String and Integer
    String[] windowStrAnswer;
    int[] windowIntAnswer;
    WordWindow test = new WordWindow(example, 5, map);
    
    // Roll 1
    test.rollWindow();
    windowIntAnswer = new int[]{5,0,1,2,3};
    windowStrAnswer = new String[]{"<s>","John","joined","PETA","in"};
    assertArrayEquals(windowStrAnswer, test.getWordArray());
    assertArrayEquals(windowIntAnswer, test.getIDArray());
    assertTrue(test.getTargetLabel().equals("O"));
    assertTrue(test.getTargetLabelID() == 1);
    
    // Roll 2
    test.rollWindow();
    windowIntAnswer = new int[]{0,1,2,3,4};
    windowStrAnswer = new String[]{"John","joined","PETA","in","Dallas"};
    assertArrayEquals(windowStrAnswer, test.getWordArray());
    assertArrayEquals(windowIntAnswer, test.getIDArray());
    assertTrue(test.getTargetLabel().equals("ORG"));
    assertTrue(test.getTargetLabelID() == 3);
    
    // Roll 3
    test.rollWindow();
    windowIntAnswer = new int[]{1,2,3,4,5};
    windowStrAnswer = new String[]{"joined","PETA","in","Dallas","Texas"};
    assertArrayEquals(windowStrAnswer, test.getWordArray());
    assertArrayEquals(windowIntAnswer, test.getIDArray());
    assertTrue(test.getTargetLabel().equals("O"));
    assertTrue(test.getTargetLabelID() == 1);
    
    // Roll 4
    test.rollWindow();
    windowIntAnswer = new int[]{2,3,4,5,5};
    windowStrAnswer = new String[]{"PETA","in","Dallas","Texas","</s>"};
    assertArrayEquals(windowStrAnswer, test.getWordArray());
    assertArrayEquals(windowIntAnswer, test.getIDArray());
    assertTrue(test.getTargetLabel().equals("LOC"));
    assertTrue(test.getTargetLabelID() == 2);
    
    // Roll 5
    test.rollWindow();
    windowIntAnswer = new int[]{3,4,5,5,5};
    windowStrAnswer = new String[]{"in","Dallas","Texas","</s>","</s>"};
    assertArrayEquals(windowStrAnswer, test.getWordArray());
    assertArrayEquals(windowIntAnswer, test.getIDArray());
    assertTrue(test.getTargetLabel().equals("LOC"));
    assertTrue(test.getTargetLabelID() == 2);
    
    // Roll should return false as window is complete
    assertFalse(test.rollWindow());
  }
  
  @Test
  public void testOneWordWindow3() {
    List<Datum> tinyExample = new ArrayList<Datum>();
    tinyExample.add(new Datum("in", "O"));
    
    WordWindow test = new WordWindow(tinyExample, 3, map);
    
    String[] windowStrAnswer = new String[]{"<s>","in","</s>"};
    assertArrayEquals(windowStrAnswer, test.getWordArray());
    
    
    int[] windowIntAnswer = new int[]{5,3,5};
    assertArrayEquals(windowIntAnswer, test.getIDArray());
    
    String out = test.getTargetLabel();
    assertTrue(test.getTargetLabel().equals("O"));
    assertTrue(test.getTargetLabelID() == 1);
    
    assertFalse(test.rollWindow());
  }
}
