package cs224n.util;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class TestConfigReaderTest {

  @Test
  public void testParseFile() throws IOException {
    List<Configuration> configs = TestConfigReader.parseFile("../testdata/testconfig.conf");
    
    Map<String,String> options = new HashMap<String,String>();
    options.put("train","../data/train");
    options.put("test","../data/test");
    options.put("wordvec","../data/vocab.txt");
    options.put("outdir","../outputs/lambda_test");
    options.put("lambda","0.1");
    options.put("learnrate","0.002");
    options.put("iters","100");
    options.put("windowsize","5");
    options.put("wordvecdim","50");
    options.put("lowcaseall","true");
    options.put("hiddendim","50,10");
    options.put("learnwordvec","false");
    
    assertEquals(new Configuration(options), configs.get(0));
    
    options.put("lambda","0.01");
    assertEquals(new Configuration(options), configs.get(1));
    
    options.put("learnrate","0.001");
    options.put("windowsize","10");
    assertEquals(new Configuration(options), configs.get(2));
    
    options.put("lambda","0.05");
    options.put("learnwordvec","true");
    assertEquals(new Configuration(options), configs.get(3));
  }

  @Test
  public void testIsSeparator() {
    String simpleTrue  = "----";
    String simpleFalse = "---";
    String stillTrue   = "-----";
    String stillTrue2  = "---- Test Comment";
    
    assertTrue(TestConfigReader.isSeparator(simpleTrue));
    assertFalse(TestConfigReader.isSeparator(simpleFalse));
    assertTrue(TestConfigReader.isSeparator(simpleTrue));
    assertTrue(TestConfigReader.isSeparator(stillTrue));
    assertTrue(TestConfigReader.isSeparator(stillTrue2));
  }

  @Test
  public void testOptionMapToString() {
    // LinkedHashMap ensures the insertion order is preserved
    Map<String,String> options = new LinkedHashMap<String,String>();
    options.put("file", "/real/path");
    options.put("number", "534");
    options.put("-dinosaur", "trex");
    
    String rightAnswer = "-file /real/path -number 534 --dinosaur trex";
    String actualAnswer = TestConfigReader.optionMapToString(options);
    assertEquals(rightAnswer, actualAnswer);
  }

}
