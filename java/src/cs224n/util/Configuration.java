package cs224n.util;

import java.util.Map;

public class Configuration {
  
  String trainFilepath, testFilepath, wordVecFilepath, vocabFilepath,
    outputFile, unknownWord, targetEntities;
  Double lambda, learningRate;
  Integer maxIterations, windowSize, wordVecDim;
  Boolean learnWordVec, allLowercase;
  Integer[] hiddenDimensions;

  public Configuration() {
    trainFilepath = "../data/train";
    testFilepath = "../data/dev";
    wordVecFilepath = "../data/wordVectors.txt";
    vocabFilepath = "../data/vocab.txt";
    outputFile = "../scored.out";
    unknownWord = "UUUNKKK";
    targetEntities = "O,LOC,MISC,ORG,PER";
    
    lambda = 0.000000;
    learningRate = 0.0000000;
    
    maxIterations = 0;
    windowSize = 3;
    wordVecDim = 50;
    
    learnWordVec = false;
    allLowercase = true;
    
    hiddenDimensions = new Integer[]{10};
  }
  
  public Configuration(String opts) {
    this(); // Initialize defaults with Configuration()
    this.parseConfigString(opts);
  }
  
  private void parseConfigString(String args) {
    String[] options = args.split(" ");
    Map<String,String> optMap = CommandLineUtils.simpleCommandLineParser(options);
    for (String key : optMap.keySet()) {
      String value = optMap.get(key);
      if (key.equals("-train"))
        setTrainFilepath(value);
      else if (key.equals("-test"))
        setTestFilepath(value);
      else if (key.equals("-wordvec"))
        setWordVecFilepath(value);
      else if (key.equals("-vocab"))
        setVocabFilepath(value);
      else if (key.equals("-outfile"))
        setOutputFile(value);
      else if (key.equals("-lambda"))
        setLambda(Double.valueOf(value));
      else if (key.equals("-learnrate"))
        setLearningRate(Double.valueOf(value));
      else if (key.equals("-iters"))
        setMaxIterations(Integer.valueOf(value));
      else if (key.equals("-windowsize"))
        setWindowSize(Integer.valueOf(value));
      else if (key.equals("-wordvecdim"))
        setWordVecDim(Integer.valueOf(value));
      else if (key.equals("-learnwordvec"))
        setLearnWordVec(Boolean.valueOf(value));
      else if (key.equals("-lowcaseall"))
        setAllLowercase(Boolean.valueOf(value));
      else if (key.equals("-hiddendim"))
        setHiddenDimensions(value);
      else if (key.equals("-unkword"))
        setUnknownWord(value);
      else if (key.equals("-targetEntities"))
        setTargetEntities(value);
      else
        System.err.println("ERROR: Arugment "+key+" "+value+" is not recognized");
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

  public String getUnknownWord() {  return unknownWord;  }
  public void setUnknownWord(String unknownWord) {  this.unknownWord = unknownWord;  }
  
  public String[] getTargetEntities() {  return targetEntities.split(",");  }
  public void setTargetEntities(String entities) {  this.targetEntities = entities;  }

  public Integer[] getHiddenDimensions() {  return hiddenDimensions;  }
  public void setHiddenDimensions(Integer[] hiddenDimensions) {  this.hiddenDimensions = hiddenDimensions;  }
  public void setHiddenDimensions(String dimString) {
    String[] splitString = dimString.split(",");
    Integer[] dims = new Integer[splitString.length];
    for (int i=0; i < splitString.length; i++)
      dims[i] = Integer.valueOf(splitString[i]);
    setHiddenDimensions(dims);
  }
  
}
