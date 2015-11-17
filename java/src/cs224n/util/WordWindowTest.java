package cs224n.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
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
    FeatureFactory.wordToNum.put("John",   0);
    FeatureFactory.wordToNum.put("joined", 1);
    FeatureFactory.wordToNum.put("PETA",   2);
    FeatureFactory.wordToNum.put("in",     3);
    FeatureFactory.wordToNum.put("Dallas", 4);
    FeatureFactory.wordToNum.put("<s>",    5);
    FeatureFactory.wordToNum.put("</s>",   6);
    FeatureFactory.initializeTargets(new String[]{"O","LOC","MISC","ORG","PER"});
  }

  @Test
  public void testWordWindow3() {
    // Check the initial word windows are correct (SIZE == 3)
    WordWindow test = new WordWindow(example, 3);
    
    List<String> windowStr = test.getWindowStr();
    String[] windowStrAnswer = new String[]{"<s>","John","joined"};
    assert(listEqualsArray(windowStr, windowStrAnswer));
    
    List<Integer> windowInt = test.getWindowIDs();
    Integer[] windowIntAnswer = new Integer[]{5,0,1};
    assert(listEqualsArray(windowInt, windowIntAnswer));
  }

  @Test
  public void testWordWindow5() {
    // Check the initial word windows are correct (SIZE == 5)
    WordWindow test = new WordWindow(example, 5);
    
    List<String> windowStr = test.getWindowStr();
    String[] windowStrAnswer = new String[]{"<s>","<s>","John","joined","PETA"};
    assert(listEqualsArray(windowStr, windowStrAnswer));
    
    List<Integer> windowInt = test.getWindowIDs();
    Integer[] windowIntAnswer = new Integer[]{5,5,0,1,2};
    assert(listEqualsArray(windowInt, windowIntAnswer));
  }

  
  @Test
  public void testRollWindow() {
    String[] windowStrAnswer;
    WordWindow test = new WordWindow(example, 5);
    // Roll 1
    test.rollWindow();
    windowStrAnswer = new String[]{"<s>","John","joined","PETA","in"};
    assert(listEqualsArray(test.getWindowStr(), windowStrAnswer));
    // Roll 2
    test.rollWindow();
    windowStrAnswer = new String[]{"John","joined","PETA","in","Dallas"};
    assert(listEqualsArray(test.getWindowStr(), windowStrAnswer));
    // Roll 3
    test.rollWindow();
    windowStrAnswer = new String[]{"joined","PETA","in","Dallas","</s>"};
    assert(listEqualsArray(test.getWindowStr(), windowStrAnswer));
    // Roll 4
    test.rollWindow();
    windowStrAnswer = new String[]{"PETA","in","Dallas","</s>","</s>"};
    assert(listEqualsArray(test.getWindowStr(), windowStrAnswer));
    // Roll should return false as window is complete
    assert(!test.rollWindow());
  }

  @Test
  public void testGetWindow() {
    fail("Not yet implemented");
  }

  @Test
  public void testGetWindowNum() {
    fail("Not yet implemented");
  }

  @Test
  public void testGetTargetID() {
    fail("Not yet implemented");
  }
  
  public static <T> boolean listEqualsArray(List<T> a, T[] b) {
    if (a.size() != b.length) {
      return false;
    } else {
      for (int i=0; i < b.length; i++) {
        if (!a.get(i).equals(b[i]))
            return false;
      }
    }
    return true;
  }

}
