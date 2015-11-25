package cs224n.deep;

import java.io.IOException;
import java.util.List;

public class NER {
  
  public static void main(String[] args) throws IOException {
    
    if (args.length < 2) {
        System.out.println("USAGE: java -cp classes NER ../data/train ../data/dev");
        return;
    }     
  
    // Read in the requested data files and initialize necessary data structures
    List<Datum> trainData = FeatureFactory.readTrainData(args[0]);
    List<Datum> testData = FeatureFactory.readTestData(args[1]);

    // RUN THE WINDOWMODEL
    WindowModel model = new WindowModel(50, 5, new int[]{20});

    FeatureFactory.readWordVectors("../data/wordVectors.txt");
    FeatureFactory.initializeVocab("../data/vocab.txt", "UUUNKKK");
      
    model.train(trainData);
    model.test(testData, "../ner.out");
  }
}
