package cs224n.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import cs224n.deep.Datum;
import cs224n.deep.FeatureFactory;

public class WordWindowTest {
  
  static List<Datum> example = new ArrayList<Datum>();

  @BeforeClass
  public static void oneTimeSetUp() {
    // Create the example for testing
    Datum John   = new Datum("John",   "PER");
    Datum joined = new Datum("joined", "O");
    Datum PETA   = new Datum("PETA",   "ORG");
    Datum in     = new Datum("in",     "O");
    Datum Dallas = new Datum("Dallas", "LOC");
    example.add(John);
    example.add(joined);
    example.add(PETA);
    example.add(in);
    example.add(Dallas);
    
    // Initialize necessary book-keeping functions in FeatureFactory
    HashMap<String,Integer> wtn = FeatureFactory.getWordToNum();
    wtn.put("John",   0);
    wtn.put("joined", 1);
    wtn.put("PETA",   2);
    wtn.put("in",     3);
    wtn.put("Dallas", 4);
    wtn.put("<s>",    5);
    wtn.put("</s>",   6);
    FeatureFactory.initializeTargets(new String[]{"O","LOC","MISC","ORG","PER"});
  }

  @Test
  public void testWordWindow3() {
    // Check the initial word windows are correct (SIZE == 3)
    WordWindow test = new WordWindow(example, 3);
    
    String[] windowStrAnswer = new String[]{"<s>","John","joined"};
    assertArrayEquals(windowStrAnswer, test.getWordArray());
    
    int[] windowIntAnswer = new int[]{5,0,1};
    assertArrayEquals(windowIntAnswer, test.getIDArray());
  }

  @Test
  public void testWordWindow5() {
    // Check the initial word windows are correct (SIZE == 5)
    WordWindow test = new WordWindow(example, 5);
    
    String[] windowStrAnswer = new String[]{"<s>","<s>","John","joined","PETA"};
    assertArrayEquals(windowStrAnswer, test.getWordArray());
    
    int[] windowIntAnswer = new int[]{5,5,0,1,2};
    assertArrayEquals(windowIntAnswer, test.getIDArray());
  }

  
  @Test
  public void testRollWindow() {
    // Check the rolling windows work for String and Integer
    String[] windowStrAnswer;
    int[] windowIntAnswer;
    WordWindow test = new WordWindow(example, 5);
    
    // Roll 1
    test.rollWindow();
    windowIntAnswer = new int[]{5,0,1,2,3};
    windowStrAnswer = new String[]{"<s>","John","joined","PETA","in"};
    assertArrayEquals(windowStrAnswer, test.getWordArray());
    assertArrayEquals(windowIntAnswer, test.getIDArray());
    
    // Roll 2
    test.rollWindow();
    windowIntAnswer = new int[]{0,1,2,3,4};
    windowStrAnswer = new String[]{"John","joined","PETA","in","Dallas"};
    assertArrayEquals(windowStrAnswer, test.getWordArray());
    assertArrayEquals(windowIntAnswer, test.getIDArray());
    
    // Roll 3
    test.rollWindow();
    windowIntAnswer = new int[]{1,2,3,4,6};
    windowStrAnswer = new String[]{"joined","PETA","in","Dallas","</s>"};
    assertArrayEquals(windowStrAnswer, test.getWordArray());
    assertArrayEquals(windowIntAnswer, test.getIDArray());
    
    // Roll 4
    test.rollWindow();
    windowIntAnswer = new int[]{2,3,4,6,6};
    windowStrAnswer = new String[]{"PETA","in","Dallas","</s>","</s>"};
    assertArrayEquals(windowStrAnswer, test.getWordArray());
    assertArrayEquals(windowIntAnswer, test.getIDArray());
    
    // Roll should return false as window is complete
    assert(!test.rollWindow());
  }
}
