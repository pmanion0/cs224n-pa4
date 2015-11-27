package cs224n.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class ConfigurationTest {

  @Test
  public void testConfiguration() {
    String opts = "-train /fake/path "
        + "-test /other/path "
        + "-wordvec /word/path "
        + "-vocab /voc/ab "
        + "-outfile /tmp/file.txt "
        + "-lambda 0.00304 "
        + "-learnrate 0.00342 "
        + "-iters 33 "
        + "-windowsize 72 "
        + "-wordvecdim 394 "
        + "-learnwordvec false "
        + "-lowcaseall true "
        + "-hiddendim 100";
    Configuration conf = new Configuration(opts);
    
    // Test the option string was parsed correctly
    assertEquals(conf.getTrainFilepath(), "/fake/path");
    assertEquals(conf.getTestFilepath(), "/other/path");
    assertEquals(conf.getWordVecFilepath(), "/word/path");
    assertEquals(conf.getVocabFilepath(), "/voc/ab");
    assertEquals(conf.getOutputFile(), "/tmp/file.txt");
    
    assertTrue(conf.getLambda() == 0.00304);
    assertTrue(conf.getLearningRate() == 0.00342);
    
    assertTrue(conf.getMaxIterations() == 33);
    assertTrue(conf.getWindowSize() == 72);
    assertTrue(conf.getWordVecDim() == 394);
    
    assertTrue(conf.getAllLowercase());
    assertFalse(conf.getLearnWordVec());
    
    assertArrayEquals(conf.getHiddenDimensions(), new int[]{100});
    
    // Test a few alternate configurations to ensure its working properly
    conf.setHiddenDimensions("10,5,3,9");
    assertArrayEquals(conf.getHiddenDimensions(), new int[]{10,5,3,9});
    
    conf.setLearnWordVec(true);
    assertTrue(conf.getLearnWordVec());
    
    conf.setAllLowercase(false);
    assertFalse(conf.getAllLowercase());
  }

}
