package cs224n.deep;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.*;
import java.util.*;
import org.ejml.data.*;
import org.ejml.simple.*;
import java.text.*;

public class WindowModel {

	protected SimpleMatrix L, W, Wout;
	//
	public int windowSize,wordSize, hiddenSize;

	public WindowModel(int _windowSize, int _hiddenSize, double _lr){
		//TODO
	}

	/**
	 * Initializes the weights randomly. 
	 */
	public void initWeights(){
		//TODO
		// initialize with bias inside as the last column
	        // or separately
		// W = SimpleMatrix...
		// U for the score
		// U = SimpleMatrix...
	}

	public static HashMap<String, String> trainNER = new HashMap<String, String>(); 
	//public static HashMap<String, String> testNER = new HashMap<String, String>();

	public void trainBaseLine(List<Datum> _trainData ){
		//	TODO
		// read in training data, have a hashmap to store all words and NER label
		for (Datum train : _trainData){
			//System.out.println(train);
			String word = train.word;
			String label = train.label;
			if (!trainNER.containsKey(word)){
				trainNER.put(word, label);
			}
		}
	}

	private static Random random = new Random();
	
	public static int getRandomInt(int min, int max){
		return random.nextInt(max - min + 1) + min;
	}
	
	public void testBaseLine(List<Datum> testData, String outName) throws IOException{
		// TODO
		ArrayList<String> labelList = new ArrayList<String>();
		labelList.add("O");
		labelList.add("LOC");
		labelList.add("MISC");
		labelList.add("ORG");
		labelList.add("PER");
		//{O, LOC, MISC, ORG, PER}
		int size = labelList.size();
		int randomIndx = getRandomInt(0, size);
		
		PrintWriter out =  new PrintWriter(new BufferedWriter(new FileWriter(outName)));
		
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

	/**
	 * Simplest SGD training 
	 */
	public void train(List<Datum> _trainData ){
		//	TODO
	}

	
	public void test(List<Datum> testData){
		// TODO
	}
	
}
