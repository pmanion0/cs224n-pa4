package main;

import java.io.IOException;
import java.util.List;

import cs224n.deep.Datum;
import cs224n.models.WindowModel;
import cs224n.util.Configuration;
import cs224n.util.FileIO;

public class Launcher {
  
  public static void main(String[] args) throws IOException {
    if (args.length < 2) {
      System.out.println("USAGE: java -cp classes TestLauncher <test_config_file>");
      return;
    }
    String commandLineArgString = String.join(" ", args);
    Configuration conf = new Configuration(commandLineArgString);
    run(conf);
  }
  
  /**
   * Run an NER classifier instance based on an argument string 
   * @param commandLineArguments
   */
  public static void run(Configuration conf) throws IOException {
    List<Datum> trainData = FileIO.read(conf.getTrainFilepath());
    List<Datum> testData = FileIO.read(conf.getTestFilepath());
    WindowModel model = new WindowModel(conf);
      
    model.train(trainData);
    model.test(testData, "../ner.out");
  }

}
