package cs224n.deep;

import java.util.*;
import java.io.*;

import org.ejml.simple.SimpleMatrix;

import cs224n.util.CommandLineUtils;

public class NER {
  
  public static void main(String[] args) throws IOException {
    // Define the default option set and override with any command line args
    Map<String, String> options = new HashMap<String, String>();
    options.put("-train",    "../data/train");
    options.put("-test",     "../data/dev");
    options.put("-outfile",  "../scored.out");
    options.put("-wordvec",  "../data/wordVectors.txt");
    options.put("-vocab",    "../data/vocab.txt");
    options.put("-model",    "cs224n.deep.WindowModel");
    options.putAll(CommandLineUtils.simpleCommandLineParser(args));
    printOptions(options);

    // Convert the model name into an instance of the specified class
    NERModel model = getModelClass(options.get("-model"));
    System.out.println("Using parser: " + model);

    // Read in the requested data files and initialize necessary data structures
    List<Datum> trainData = FeatureFactory.readTrainData(options.get("-train"));
    List<Datum> testData = FeatureFactory.readTestData(options.get("-test"));
    
    // Run setup for the requested model
    if (model instanceof WindowModel) {
      // All of this is awful!
      WindowModel windowmodel = (WindowModel) model;
      windowmodel.setupWindowModel(50, 5, new int[]{20});
      FeatureFactory.readWordVectors(options.get("-wordvec"));
      FeatureFactory.initializeVocab(options.get("-vocab"));      
    } else if (model instanceof BaselineModel) {
      // Do something?
    } else {
      // Do something?
    }
    
    model.train(trainData);
    System.out.println("Training Complete.");
    
    model.test(testData, options.get("-outfile"));
    System.out.println("Testing Complete.");
  }
  
  
  /**
   * Print all the runtime options from the options map
   */
  public static void printOptions(Map<String, String> options) {
    System.out.println("NER Run Parameters:");
    for (Map.Entry<String, String> entry: options.entrySet()) {
      System.out.printf("  %-12s: %s%n", entry.getKey(), entry.getValue());
    }
    System.out.println();
  }
  
  
  /**
   * Convert the modelName string into an instance of the model class
   */
  public static NERModel getModelClass(String modelName) {
    NERModel model;
    try {
      Class<?> modelClass = Class.forName(modelName);
      model = (NERModel) modelClass.newInstance();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return model;
  }
}
