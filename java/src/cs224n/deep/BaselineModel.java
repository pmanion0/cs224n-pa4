package cs224n.deep;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class BaselineModel implements NERModel {
  
  public static HashMap<String, String> trainNER = new HashMap<String, String>();
  private static Random random = new Random();
  
  public static int getRandomInt(int min, int max){
    return random.nextInt(max - min + 1) + min;
  }

  @Override
  public void train(List<Datum> trainData) {
    for (Datum train : trainData){
      //System.out.println(train);
      String word = train.word;
      String label = train.label;
      if (!trainNER.containsKey(word)){
        trainNER.put(word, label);
      }
    }
  }

  @Override
  public void test(List<Datum> testData, String outfile) {
    List<String> predictions = new ArrayList<String>();
    
    for (Datum test : testData){
      String predLabel = predictDatum(test);
      predictions.add(predLabel);
    }
    FeatureFactory.outputScoringToFile(testData, predictions, outfile);
  }
  
  
  /**
   * Output the predicted entity label for a given input datum
   * @param d - Input datum to score
   * @return Entity label
   */
  public String predictDatum(Datum d) {
    String word = d.word;
    String labelPredicted;
    
    if (!word.equals("-DOCSTART-") && trainNER.containsKey(word)) {
      labelPredicted = trainNER.get(word);
    } else {
      // Randomly assign label if we did not see it in training
      //int size = FeatureFactory.targetToNum.size();
      //int randomIndx = getRandomInt(0, size-1);
      //labelPredicted = FeatureFactory.numToTarget.get(randomIndx);
      labelPredicted = "O";
    }
    return labelPredicted;
  }

}
