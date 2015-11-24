package cs224n.deep;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import org.ejml.simple.*;


public class FeatureFactory {
  
  public static String UNKNOWN_WORD = "UUUNKKK";

  public static SimpleMatrix wordVector;
  public static List<Datum> trainData, testData;
  private static HashMap<String, Integer> wordToNum = new HashMap<String, Integer>(); 
  private static HashMap<String, Integer> targetToNum = new HashMap<String, Integer>();
  private static HashMap<Integer, String> numToWord = new HashMap<Integer, String>();
  private static HashMap<Integer, String> numToTarget = new HashMap<Integer, String>();
  private static int wordCounter = 0;

  static {
    String[] targetEntities = new String[]{"O","LOC","MISC","ORG","PER"}; 
    FeatureFactory.initializeTargets(targetEntities);
  }
  
	private FeatureFactory() {
	}


	/** Do not modify this method **/
	public static List<Datum> readTrainData(String filename) throws IOException {
        if (trainData==null) trainData= read(filename);
        return trainData;
	}
	
	/** Do not modify this method **/
	public static List<Datum> readTestData(String filename) throws IOException {
        if (testData==null) testData= read(filename);
        return testData;
	}
	
	/**
	 * Read a list of example words from a training file
	 */
	private static List<Datum> read(String filename)
			throws FileNotFoundException, IOException {
	    // TODO: you'd want to handle sentence boundaries
		List<Datum> data = new ArrayList<Datum>();
		BufferedReader in = new BufferedReader(new FileReader(filename));
		for (String line = in.readLine(); line != null; line = in.readLine()) {
			if (line.trim().length() == 0) {
				continue;
			}
			String[] bits = line.split("\\s+");
			String word = bits[0];
			String label = bits[1];

			Datum datum = new Datum(word, label);
			data.add(datum);
		}

		return data;
	}
 
 
	/**
	 * Convert a densely represented file of doubles into a SimpleMatrix that can be
	 * used as a mapping from the vocabulary to each word vector
	 */
	public static SimpleMatrix readWordVectors(String vecFilename) throws IOException {
		if (wordVector != null) {
		  return wordVector;
		}
		
		int rowCount = countLines(vecFilename);
		int colCount = countColumns(vecFilename);
		double[][] wordVec = new double[rowCount][colCount];
		
		// Read in the standard vocabulary file
		BufferedReader reader = new BufferedReader(new FileReader(vecFilename));
    for (int i=0; i < rowCount; i++) {
      String line = reader.readLine();
      fillDoubleWithLine(line, wordVec[i]);
    }
    reader.close();
    
    wordVector = new SimpleMatrix(wordVec); 
		return wordVector;
	}
	
  /**
   * Count the number of columns in a file *based on the first row*
   */
  public static int countColumns(String filename) throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader(filename));
    int colCount = reader.readLine().split(" ").length;
    reader.close();
    return colCount;
  }
	
  /**
   * Count the number of lines in a file
   */
  public static int countLines(String filename) throws IOException {
    int lineCount = 0;
    BufferedReader reader = new BufferedReader(new FileReader(filename));
    while (reader.readLine() != null) {
      lineCount++;
    }
    reader.close();
    return lineCount;
  }
	
	/**
   * Split a delimited string of numbers and place them into a double array
   */
  public static void fillDoubleWithLine(String line, double[] storage) {
    String[] strVals = line.split(" ");
    for (int i=0; i < strVals.length; i++)
      storage[i] = Double.valueOf(strVals[i]);
  }
	
  /**
   * Create useful map functions based on a providede vocabulary file
   * @param vocabFilename - Full path to a vocabulary text file
   * @return 
   */
	public static void initializeVocab(String vocabFilename) throws IOException {
	  BufferedReader in = new BufferedReader(new FileReader(vocabFilename));
    for (String word = in.readLine(); word != null; word = in.readLine()) {
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
	 * Return the ID for a number (using the unknown word if needed)
	 */
	public static int getWordNum(String word) {
	  if (wordToNum.containsKey(word))
	    return wordToNum.get(word);
	  else if (wordToNum.containsKey(UNKNOWN_WORD))
	    return wordToNum.get(UNKNOWN_WORD);
	  else {
	    System.err.println("WARN: Cannot find ID for unknown word - defaulting to 0");
	    return 0; // Hope the first word is unknown
	  }
	}
	
	/**
	 * Return the ID for a target string (throws a nasty result if unknown)
	 */
	public static int getTargetNum(String target) {
	  if (targetToNum.containsKey(target))
	    return targetToNum.get(target);
	  else {
	    System.err.println("ERROR: Target unknown");
	    return -1;
	  }
	}
	
	/**
	 * Initialize mappings between target string and integer representations
	 * @param targetList - List of all possible target variables
	 */
	public static void initializeTargets(String[] targetList) {
    for (int i=0; i < targetList.length; i++) {
      targetToNum.put(targetList[i], i);
      numToTarget.put(i, targetList[i]);
    }
	}
	
	/**
	 * Output a scored file using the input data, the predictions, and an output file path
	 * @param data - List of Datum that were scored
	 * @param predictions - Predicted entities corresponding to every datum in data
	 * @param outfile - Output file that will be written to
	 */
	public static void outputScoringToFile(List<Datum> data, List<String> predictions, String outfile) {
	  PrintWriter out;
    try {
      out =  new PrintWriter(new BufferedWriter(new FileWriter(outfile)));
    } catch (Exception e) {
      System.err.println("ERROR: Cannot open output file");
      return;
    }
    
    for (int i=0; i < data.size(); i++) {
      Datum d = data.get(i);
      String predLabel = predictions.get(i);
      out.println(d.word + "\t" + d.label + "\t" + predLabel);
    }
    out.close();
	}
	
	/**
	 * 
	 */
	public static void setUnknownWord(String s) {
	  UNKNOWN_WORD = s;
	}
	
	/** Return the entire wordToNum HashMap - use getWordNum(String) for individual word lookups */
	public static HashMap<String, Integer> getWordToNum() { return wordToNum; }
	/** Return the entire numToWord HashMap- use getTargetNum(String) for individual target lookups */
	public static HashMap<Integer, String> getNumToWord() { return numToWord; }
}
