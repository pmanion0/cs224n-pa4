package cs224n.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import org.ejml.simple.SimpleMatrix;

public class WordMap {
  private HashMap<String, Integer> wordToNum, targetToNum;
  private HashMap<Integer, String> numToWord, numToTarget;
  private SimpleMatrix wordVector;
  private String unknownWord;
  private boolean allLowercase;
  
  /**
   * Create useful map functions based on a provided vocabulary file
   * @param vocabFilename - Full path to a vocabulary text file
   * @return 
   */
  public WordMap(Configuration conf) throws IOException {
    wordVector = FileIO.readWordVectors(conf.getWordVecFilepath());
    allLowercase = conf.getAllLowercase();
    
    this.setUnknownWord(conf.getUnknownWord());
    this.initializeTargetMap(conf.getTargetEntities());
    this.initializeWordMap(conf.getVocabFilepath());
  }
  
  /**
   * Return the ID for a number (using the unknown word if needed)
   */
  public int getWordNum(String word) {
    word = formatWord(word);
    
    if (wordToNum.containsKey(word))
      return wordToNum.get(word);
    else if (wordToNum.containsKey(unknownWord))
      return wordToNum.get(unknownWord);
    else {
      System.err.println("WARN: Cannot find ID for unknown word - defaulting to 0");
      return 0; // Hope the first word is unknown
    }
  }
  
  /**
   * Return the ID for a target string (throws a nasty result if unknown)
   */
  public int getTargetNum(String target) {
    if (targetToNum.containsKey(target))
      return targetToNum.get(target);
    else {
      System.err.println("ERROR: Target string unknown");
      return -1;
    }
  }
  
  /**
   * Get the string representation of a given target ID
   */
  public String getTargetName(int id) {
    if (numToTarget.containsKey(id))
      return numToTarget.get(id);
    else 
      return "TARGET_CLASS_UNKNOWN";
  }
  
  
  /**
   * Initialize mappings between target string and integer representations
   * @param targetList - List of all possible target variables
   */
  public void initializeTargetMap(String[] targetList) {
    targetToNum = new HashMap<String, Integer>();
    numToTarget = new HashMap<Integer, String>();
    
    for (int i=0; i < targetList.length; i++) {
      targetToNum.put(targetList[i], i);
      numToTarget.put(i, targetList[i]);
    }
  }
  
  
  public void initializeWordMap(String filepath) throws IOException {
    int wordCounter = 0;
    wordToNum = new HashMap<String, Integer>(); 
    numToWord = new HashMap<Integer, String>();
    
    BufferedReader in = new BufferedReader(new FileReader(filepath));
    for (String word = in.readLine(); word != null; word = in.readLine()) {
      word = formatWord(word);
      // Skip any empty lines or words that have already appeared
      if (word.trim().length() == 0 || wordToNum.containsKey(word)) {
        continue;
      }
      wordToNum.put(word, wordCounter);
      numToWord.put(wordCounter, word);
      wordCounter++;
    }
    in.close();
  }
  
  /**
   * Return the word vector for a given word
   */
  public SimpleMatrix getWordVector(int wordID) {
    return wordVector.extractVector(true, wordID);
  }
  
  /**
   * Set the Unknown Word
   */
  public void setUnknownWord(String s) {
    unknownWord = formatWord(s);
  }
  
  /**
   * Apply any necssary transformations to the input word (e.g. lowcase)
   */
  public String formatWord(String word) {
    return (allLowercase ? word.toLowerCase() : word);
  }
  
  /** Return the entire wordToNum HashMap - use getWordNum(String) for individual word lookups */
  public HashMap<String, Integer> getWordToNum() { return wordToNum; }
  /** Return the entire numToWord HashMap- use getTargetNum(String) for individual target lookups */
  public HashMap<Integer, String> getNumToWord() { return numToWord; }
}
