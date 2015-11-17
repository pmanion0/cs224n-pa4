package cs224n.util;

import java.util.LinkedList;
import java.util.List;

import cs224n.deep.Datum;
import cs224n.deep.FeatureFactory;

public class WordWindow {

  private int tailSize, windowSize, currentWordIndex, nextWordIndex;
  private List<String> windowStr;
  private List<Integer> windowIDs;
  private List<Datum> data;
  private String startTag = "<s>", endTag = "</s>";
  
  /**
   * Create a new word window to handle rolling windows
   * @param data - List of Datum that we want to roll through
   * @param windowSize - Full size of the rolling window
   */
  public WordWindow(List<Datum> data, int windowSize) {
    this.tailSize = (windowSize-1)/2;
    this.windowSize = windowSize;
    this.windowStr = new LinkedList<String>();
    this.windowIDs = new LinkedList<Integer>();
    this.data = data;
    this.currentWordIndex = 0;
    
    // Pad with start tags
    for (int i=0; i < tailSize; i++) {
      windowStr.add(startTag);
      windowIDs.add(FeatureFactory.wordToNum.get(startTag));
    }
    // Fill the remaining spots with words
    for (int j=0; j < windowSize-tailSize; j++) {
      String newWord = data.get(j).word;
      windowStr.add(newWord);
      windowIDs.add(FeatureFactory.wordToNum.get(newWord));
      nextWordIndex++;
    }
  }

  
  /**
   * Move the rolling window forward one position
   * @return - TRUE if rolled successfully, FALSE if we reached the end
   */
  public boolean rollWindow() {
    boolean result;
    // Remove the oldest word on the queue
    windowStr.remove(0);
    
    if (nextWordIndex < data.size()) {
      // Add the next word if its within the data
      String newWord = data.get(nextWordIndex).word;
      windowStr.add(newWord);
      windowIDs.add(FeatureFactory.wordToNum.get(newWord));
      result = true;
    } else if (currentWordIndex < data.size()) {
      // Pad the back with an END tag if current word is within the data
      windowStr.add(endTag);
      result = true;
    } else {
      // Otherwise, return FALSE as we reached the end of the data
      result = false;
    }
    // Increment the counters and return a success
    currentWordIndex++;
    nextWordIndex++;
    return result;
  }
  
  

  /** Return a list of the words in the window **/
  public List<String> getWordList() {
    return windowStr;
  }
  /** Return an array of the words in the window **/
  public String[] getWordArray() {
    String[] out = new String[windowSize];
    for(int i=0; i < windowStr.size(); i++)
      out[i] = windowStr.get(i);
    return out;
  }
  /** Return a list of the word IDs in the window **/
  public List<Integer> getIDList() {
    return windowIDs;
  }
  /** Return an array of the word IDs in the window **/
  public int[] getIDArray() {
    int[] out = new int[windowSize];
    for(int i=0; i < windowStr.size(); i++)
      out[i] = windowIDs.get(i);
    return out;
  }
  /** Return the current target word **/
  public String getTargetWord() {
    return data.get(currentWordIndex).word;
  }
  /** Return the current target's label **/
  public String getTargetLabel() {
    return data.get(currentWordIndex).label;
  }
  /** Return the current target word **/
  public int getTargetLabelID() {
    return FeatureFactory.targetToNum.get(this.getTargetLabel());
  }
  
}
