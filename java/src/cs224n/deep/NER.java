package cs224n.deep;

import java.util.*;
import java.io.*;

import org.ejml.simple.SimpleMatrix;


public class NER {
    
    public static void main(String[] args) throws IOException {
	if (args.length < 2) {
	    System.out.println("USAGE: java -cp classes NER ../data/train ../data/dev");
	    return;
	}	    

	// this reads in the train and test datasets
//	List<Datum> trainData = FeatureFactory.readTrainData(args[0]);
//	List<Datum> testData = FeatureFactory.readTestData(args[1]);	
	List<Datum> trainData = FeatureFactory.readTrainData("/Users/ldi020/Playground/cs224n/pa4/data/train");
	List<Datum> testData = FeatureFactory.readTestData("/Users/ldi020/Playground/cs224n/pa4/data/dev");	
	//	read the train and test data
	//TODO: Implement this function (just reads in vocab and word vectors)
	FeatureFactory.initializeVocab("/Users/ldi020/Playground/cs224n/pa4/data/vocab.txt");
	SimpleMatrix allVecs= FeatureFactory.readWordVectors("/Users/ldi020/Playground/cs224n/pa4/data/wordVectors.txt");

	// initialize model 
	WindowModel model = new WindowModel(5, 100,0.001);
	model.initWeights();

	//TODO: Implement those two functions
	model.trainBaseLine(trainData);
	model.testBaseLine(testData, "/Users/ldi020/Playground/cs224n/pa4/data/output/baseline.txt");
	//model.train(trainData);
	//model.test(testData);
    }
}
