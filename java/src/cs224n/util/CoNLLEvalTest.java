package cs224n.util;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class CoNLLEvalTest {
  
  ByteArrayOutputStream stdoutCatcher;
  String correctStdout = "" +
      "processed 9 tokens with 3 phrases; found: 4 phrases; correct: 2.\n" +
      "accuracy:  77.78%; precision:  50.00%; recall:  66.67%; FB1:  57.14\n" +
      "              LOC: precision:   0.00%; recall:   0.00%; FB1:   0.00  1\n" +
      "             MISC: precision: 100.00%; recall:  50.00%; FB1:  66.67  1\n" +
      "              ORG: precision:  50.00%; recall: 100.00%; FB1:  66.67  2\n";
  
  @Before
  public void setUp() {
    // Create a ByteArray to store the normal STDOUT output
    stdoutCatcher = new ByteArrayOutputStream();
    System.setOut(new PrintStream(stdoutCatcher));
  }

  @Test
  public void stringTest() {
    CoNLLEval tester = new CoNLLEval("../conlleval");
    
    List<String> scored = new ArrayList<String>();
    scored.add("EU\tORG\tORG");
    scored.add("rejects\tO\tO");
    scored.add("German\tMISC\tLOC");
    scored.add("call\tO\tO");
    scored.add("to\tO\tO");
    scored.add("boycott\tO\tO");
    scored.add("British\tMISC\tMISC");
    scored.add("lamb\tO\tORG");
    scored.add(".\tO\tO");
    
    tester.eval(scored);
    
    // Check that STDOUT output matches the manual version
    String stdoutString = stdoutCatcher.toString();
    assertEquals(correctStdout, stdoutString);
  }
  
  @Test
  public void fileTest() {
    CoNLLEval tester = new CoNLLEval("../conlleval");
    tester.eval("../example.out");
    
    // Check that STDOUT output matches the manual version
    String stdoutString = stdoutCatcher.toString();
    assertEquals(correctStdout, stdoutString);
  }

}
