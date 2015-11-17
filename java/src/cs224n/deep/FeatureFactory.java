package cs224n.deep;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import org.ejml.simple.*;


public class FeatureFactory {

  public static SimpleMatrix wordVector;
  public static List<Datum> trainData, testData;
  public static HashMap<String, Integer> wordToNum = new HashMap<String, Integer>(); 
  public static HashMap<Integer, String> numToWord = new HashMap<Integer, String>();
  public static HashMap<String, Integer> targetToNum = new HashMap<String, Integer>();
  public static HashMap<Integer, String> numToTarget = new HashMap<Integer, String>();
  private static int wordCounter = 0;

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
		double[][] wordVec = initArrayFromFile(vecFilename);
		BufferedReader reader = new BufferedReader(new FileReader(vecFilename));
    for (int i=0; i < wordVec.length; i++) {
      String line = reader.readLine();
      fillDoubleWithLine(line, wordVec[i]);
    }
    reader.close();
    wordVector = new SimpleMatrix(wordVec); 
		return wordVector;
	}
	
	/**
	 * Initialize a double array based on the columns/rows in a file. This assumes
	 * every row is non-empty, has the same number of columns, and is dense.
	 */
	public static double[][] initArrayFromFile(String filename) throws IOException {
	  BufferedReader reader = new BufferedReader(new FileReader(filename));
    int colCount = reader.readLine().split(" ").length;
    int rowCount = countLines(filename);
    return new double[rowCount][colCount];
	}
	
  /**
   * Count the number of lines in a file
   */
  public static int countLines(String filename) throws IOException {
    int lineCount = 0;
    BufferedReader reader = new BufferedReader(new FileReader(filename));
    while (reader.readLine() != null)
      lineCount++;
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
	 * Initialize mappings between target string and integer representations
	 * @param targetList - List of all possible target variables
	 */
	public static void initializeTargets(String[] targetList) {
    for (int i=0; i < targetList.length; i++) {
      targetToNum.put(targetList[i], i);
      numToTarget.put(i, targetList[i]);
    }
	}
}
