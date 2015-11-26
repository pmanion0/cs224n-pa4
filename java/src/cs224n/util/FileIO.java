package cs224n.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.ejml.simple.SimpleMatrix;

import cs224n.deep.Datum;

public class FileIO {
  
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
   * Read a list of example words from a training file
   */
  public static List<Datum> read(String filename)
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
    SimpleMatrix wordVector;
    
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
}
