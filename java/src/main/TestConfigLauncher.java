package main;

import java.io.IOException;
import java.util.List;

import cs224n.util.Configuration;
import cs224n.util.TestConfigReader;


public class TestConfigLauncher {

  public static void main(String[] args) throws IOException {
    if (args.length < 1) {
      System.out.println("USAGE: java -cp classes TestConfigRunner <test_config_file>");
      return;
    }
    
    List<Configuration> runConfigs = TestConfigReader.parseFile(args[0]);
    System.out.println("Read " + runConfigs.size() + " test configurations.");
    
    for (int i=0; i < runConfigs.size(); i++) {
      System.out.println("--- Configuration #"+i+" ---");
      Configuration conf = runConfigs.get(i);
      Launcher.run(conf);
      System.out.println("-------------------------");
    }
  }
}
