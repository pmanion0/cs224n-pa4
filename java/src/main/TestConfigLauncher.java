package main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import cs224n.util.Configuration;
import cs224n.util.TestConfigReader;


public class TestConfigLauncher {

  public static void main(String[] args) throws IOException {
    if (args.length < 1) {
      System.out.println("USAGE: java -Xmx2g -cp \"classes:extlib/*\" main.TestConfigLauncher <test_config_file>");
      return;
    }
    
    List<Configuration> runConfigs = TestConfigReader.parseFile(args[0]);
    System.out.println("Read " + runConfigs.size() + " test configurations.");
    
    String outputDir = "";
    if (args.length > 1) {
      outputDir = args[1];
      System.out.println("Output directory specified: " + outputDir);
      updateConfigOutputs(runConfigs, outputDir);
      createSummaryTxt(runConfigs, outputDir);
    }
    
    for (int i=0; i < runConfigs.size(); i++) {
      System.out.println("\n--- Configuration #"+i+" ---");
      Configuration conf = runConfigs.get(i);
      Launcher.run(conf);
    }
  }
  
  
  
  /**
   * Update all output files to use the outputDirectory
   */
  public static void updateConfigOutputs(List<Configuration> configs, String outputDir) {
    for (int i=0; i < configs.size(); i++) {
      Configuration conf = configs.get(i);
      conf.setOutputFile(outputDir + "/test_" + i);
    }
  }
  
  /**
   * Create a summary of each test configuration (to later find out what test_# was)
   */
  public static void createSummaryTxt(List<Configuration> configs, String outputDir) throws IOException {
    BufferedWriter out = new BufferedWriter(new FileWriter(outputDir + "/summary.txt"));
    for (int i=0; i < configs.size(); i++) {
      Configuration conf = configs.get(i);
      out.write("--- Configuration #"+i+" ---\n");
      out.write(conf.toString());
      out.write("\n\n");
    }
    out.close();
  }
}
