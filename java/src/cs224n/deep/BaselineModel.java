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
    ArrayList<String> labelList = new ArrayList<String>();
    labelList.add("O");
    labelList.add("LOC");
    labelList.add("MISC");
    labelList.add("ORG");
    labelList.add("PER");
    //{O, LOC, MISC, ORG, PER}
    int size = labelList.size();
    int randomIndx = getRandomInt(0, size);
    
    PrintWriter out;
    try {
      out =  new PrintWriter(new BufferedWriter(new FileWriter(outfile)));
    } catch (Exception e) {
      System.err.println("ERROR: Cannot open output file");
      return;
    }
    
    for (Datum test : testData){
      String word = test.word;
      String labelGold = test.label;
      String labelPredicted;
      
      //-DOCSTART-
      if (!word.equals("-DOCSTART-") && trainNER.containsKey(word)) {
        labelPredicted = trainNER.get(word);
      } else {
        // randomly assign
        labelPredicted = labelList.get(randomIndx);
      }     
       out.println(word + "\t" + labelGold+ "\t" + labelPredicted);
    } // end test
    out.close();
  }

}
