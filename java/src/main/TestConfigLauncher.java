package main;

import java.io.IOException;
import java.util.List;

import cs224n.util.TestConfigReader;


public class TestConfigLauncher {

  public static void main(String[] args) throws IOException {
    if (args.length < 1) {
      System.out.println("USAGE: java -cp classes TestConfigRunner <test_config_file>");
      return;
    }
    
    List<String> runConfigStrings = TestConfigReader.parseFile(args[0]);
    System.out.println("Read " + runConfigStrings.size() + " test configurations.");
    
    for (int i=0; i < runConfigStrings.size(); i++) {
      System.out.println("--- Configuration #"+i+" ---");
      String confString = runConfigStrings.get(i);
      Launcher.run(confString);
      System.out.println("-------------------------");
    }
  }
}
