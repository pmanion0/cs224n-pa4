package cs224n.deep;

import java.io.IOException;
import java.util.List;

import cs224n.models.WindowModel;
import cs224n.util.Configuration;
import cs224n.util.FileIO;

public class NER {
  
  public static void main(String[] args) throws IOException {
    
    if (args.length < 2) {
      System.out.println("USAGE: java -cp classes NER ../data/train ../data/dev");
      return;
    }    
  
    Configuration conf = new Configuration();
    conf.setTrainFilepath(args[0]);
    //System.out.println("args[0]: " + args[1]);
    conf.setTestFilepath(args[1]);
    conf.setOutputFile("../ner.out");
    
    // Read in the requested data files and initialize necessary data structures
    List<Datum> trainData = FileIO.read(conf.getTrainFilepath());
    List<Datum> testData = FileIO.read(conf.getTestFilepath());

    // RUN THE WINDOWMODEL
    
    WindowModel model = new WindowModel(conf);
      
    model.train(trainData);
    
    List<String> predictions = model.test(testData);
    FileIO.outputScoringToFile(testData, predictions, conf.getOutputFile());
  }
}
