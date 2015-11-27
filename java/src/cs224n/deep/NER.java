package cs224n.deep;

import java.io.IOException;
import java.util.List;

import cs224n.models.WindowModel;
import cs224n.util.Configuration;
import cs224n.util.FileIO;

public class NER {
  
  public static void main(String[] args) throws IOException {
  
    Configuration conf = new Configuration();
    
    // Read in the requested data files and initialize necessary data structures
    List<Datum> trainData = FileIO.read(conf.getTrainFilepath());
    List<Datum> testData = FileIO.read(conf.getTestFilepath());

    // RUN THE WINDOWMODEL
    
    WindowModel model = new WindowModel(conf);
      
    model.train(trainData);
    model.test(testData, "../ner.out");
  }
}
