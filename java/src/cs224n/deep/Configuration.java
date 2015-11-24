package cs224n.deep;

import java.util.Map;

import cs224n.util.CommandLineUtils;

public class Configuration {
  
  String trainFilepath, testFilepath, wordVecFilepath, vocabFilepath, outputFile;
  Double lambda, learningRate;
  Integer maxIterations, windowSize, wordVecDim;
  Boolean learnWordVec, allLowercase;
  Integer[] hiddenDimensions;

  public Configuration(String[] args) {
    trainFilepath = "../data/train";
    testFilepath = "../data/dev";
    wordVecFilepath = "../data/wordVectors.txt";
    vocabFilepath = "../data/vocab.txt";
    outputFile = "../scored.out";
    
    lambda = 0.000000;
    learningRate = 0.0000000;
    
    maxIterations = 0;
    windowSize = 3;
    wordVecDim = 50;
    
    learnWordVec = false;
    allLowercase = true;
    
    hiddenDimensions = new Integer[]{10};
  }
  
  private void parseConfigString(String args) {
    String[] options = args.split(" ");
    Map<String,String> optMap = CommandLineUtils.simpleCommandLineParser(options);
    for (String key : optMap.keySet()) {
      // TODO: Call the right getter/setters based on the string
    }
  }
  
  public String getTrainFilepath() {  return trainFilepath;  }
  public void setTrainFilepath(String trainFilepath) {  this.trainFilepath = trainFilepath;  }

  public String getTestFilepath() {  return testFilepath;  }
  public void setTestFilepath(String testFilepath) {  this.testFilepath = testFilepath;  }

  public String getWordVecFilepath() {  return wordVecFilepath;  }
  public void setWordVecFilepath(String wordVecFilepath) {  this.wordVecFilepath = wordVecFilepath;  }

  public String getVocabFilepath() {  return vocabFilepath;  }
  public void setVocabFilepath(String vocabFilepath) {  this.vocabFilepath = vocabFilepath;  }

  public String getOutputFile() {  return outputFile;  }
  public void setOutputFile(String outputFile) {  this.outputFile = outputFile;  }

  public Double getLambda() {  return lambda;  }
  public void setLambda(Double lambda) {  this.lambda = lambda;  }

  public Double getLearningRate() {  return learningRate;  }
  public void setLearningRate(Double learningRate) {  this.learningRate = learningRate;  }

  public Integer getMaxIterations() {  return maxIterations;  }
  public void setMaxIterations(Integer maxIterations) {  this.maxIterations = maxIterations;  }

  public Integer getWindowSize() {  return windowSize;  }
  public void setWindowSize(Integer windowSize) {  this.windowSize = windowSize;  }

  public Integer getWordVecDim() {  return wordVecDim;  }
  public void setWordVecDim(Integer wordVecDim) {  this.wordVecDim = wordVecDim;  }

  public Boolean getLearnWordVec() {  return learnWordVec;  }
  public void setLearnWordVec(Boolean learnWordVec) {  this.learnWordVec = learnWordVec;  }

  public Boolean getAllLowercase() {  return allLowercase;  }
  public void setAllLowercase(Boolean allLowercase) {  this.allLowercase = allLowercase;  }

  public Integer[] getHiddenDimensions() {  return hiddenDimensions;  }
  public void setHiddenDimensions(Integer[] hiddenDimensions) {  this.hiddenDimensions = hiddenDimensions;  }
  
}
