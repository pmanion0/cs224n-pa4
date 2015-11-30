package cs224n.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;

public class CoNLLEval {
  
  String CoNLLEvalScript;
  
  public CoNLLEval(String path) {
    CoNLLEvalScript = path;
  }
  
  public Process init() throws IOException {
    Runtime r = Runtime.getRuntime();
    Process p = r.exec(CoNLLEvalScript + " -r -d \\t");
    return p;
  }
  
  /** Evaluate a list of scored triplets using the CoNLL evaluation script */
  public void eval(List<String> scored) {
    try {
      Process p = init();
      launchProcess(p, scored);
      printOutput(p);
    } catch (Exception e) {
      System.err.println("Evaluation Error: " + e);
    }
  }
  
  /** Evaluate a scored output file using the CoNLL evaluation script */
  public void eval(String scoredFile) {
    try {
      Process p = init();
      launchProcess(p, scoredFile);
      printOutput(p);
    } catch (Exception e) {
      System.err.println("Evaluation Error: " + e);
    }
  }

  
  public void launchProcess(Process p, List<String> scored) throws IOException {
    // Write list of  to the process's STDIN
    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
    for (String line : scored)
      writer.write(line + "\n");
    writer.close();
  }
  
  public void launchProcess(Process p, String scoredFile) throws IOException {
    // Read in the scored file and send it to the process's STDIN
    BufferedReader fileIn = new BufferedReader(new FileReader(scoredFile));
    BufferedWriter processIn = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
    for (String line = fileIn.readLine(); line != null; line = fileIn.readLine())
      processIn.write(line + "\n");
    fileIn.close();
    processIn.close();
  }
  
  public void printOutput(Process p) throws IOException, InterruptedException {
    // Wait for the scoring to finish
    p.waitFor(); 
    // Read in the process's STDOUT and print to console
    BufferedReader b = new BufferedReader(new InputStreamReader(p.getInputStream()));
    String line = "";
    while ((line = b.readLine()) != null)
      System.out.println(line);
    b.close();
  }

}
