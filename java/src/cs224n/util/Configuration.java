package cs224n.util;

import java.util.Arrays;
import java.util.Map;

public class Configuration {
  
  private final String PARAMETER_DELIMITER = ",";
  
  private String trainFilepath, testFilepath, wordVecFilepath, vocabFilepath,
    outputFile, unknownWord, targetEntities;
  private Double lambda, learningRate;
  private Integer maxIterations, windowSize, wordVecDim;
  private Boolean learnWordVec, allLowercase;
  private int[] hiddenDimensions;

  public Configuration() {
    trainFilepath = "../data/train";
    testFilepath = "../data/dev";
    wordVecFilepath = "../data/wordVectors.txt";
    vocabFilepath = "../data/vocab.txt";
    outputFile = "../scored.out";
    unknownWord = "UUUNKKK";
    targetEntities = "O,LOC,MISC,ORG,PER";
    
    lambda = 0.0;
    learningRate = 1e-4;
    
    maxIterations = 10;
    windowSize = 3;
    wordVecDim = 50;
    
    learnWordVec = false;
    allLowercase = true;
    
    hiddenDimensions = new int[]{10};
  }
  
	public Configuration(String opts) {
    this(); // Initialize defaults with Configuration()
    Map<String,String> optMap = CommandLineUtils.simpleCommandLineParser(opts.split(" "));
    this.parseConfigMap(optMap);
  }
  
  public Configuration(Map<String,String> optMap) {
    this(); // Initialize defaults with Configuration()
    this.parseConfigMap(optMap);
  }
  
  private void parseConfigMap(Map<String,String> map) {
    for (String key : map.keySet()) {
      String value = map.get(key).trim();
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
      else if (key.equals("-entities"))
        setTargetEntities(value);
      else
        System.err.println("ERROR: Argument "+key+" "+value+" is not recognized");
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
  
  public String[] getTargetEntities() {  return targetEntities.split(PARAMETER_DELIMITER);  }
  public void setTargetEntities(String entities) {  this.targetEntities = entities;  }

  public int[] getHiddenDimensions() {  return hiddenDimensions;  }
  public void setHiddenDimensions(int[] hiddenDimensions) {  this.hiddenDimensions = hiddenDimensions;  }
  public void setHiddenDimensions(String dimString) {
    String[] splitString = dimString.split(PARAMETER_DELIMITER);
    int[] dims = new int[splitString.length];
    for (int i=0; i < splitString.length; i++)
      dims[i] = Integer.valueOf(splitString[i]);
    setHiddenDimensions(dims);
  }

  /** Return the output dimension implied by targetEntities */
  public Integer getOutputDim() {
    return targetEntities.split(PARAMETER_DELIMITER).length;
  }
  
  /**** Automatically Generated hashCode + equals Operators ****/
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((allLowercase == null) ? 0 : allLowercase.hashCode());
    result = prime * result + Arrays.hashCode(hiddenDimensions);
    result = prime * result + ((lambda == null) ? 0 : lambda.hashCode());
    result = prime * result + ((learnWordVec == null) ? 0 : learnWordVec.hashCode());
    result = prime * result + ((learningRate == null) ? 0 : learningRate.hashCode());
    result = prime * result + ((maxIterations == null) ? 0 : maxIterations.hashCode());
    result = prime * result + ((outputFile == null) ? 0 : outputFile.hashCode());
    result = prime * result + ((targetEntities == null) ? 0 : targetEntities.hashCode());
    result = prime * result + ((testFilepath == null) ? 0 : testFilepath.hashCode());
    result = prime * result + ((trainFilepath == null) ? 0 : trainFilepath.hashCode());
    result = prime * result + ((unknownWord == null) ? 0 : unknownWord.hashCode());
    result = prime * result + ((vocabFilepath == null) ? 0 : vocabFilepath.hashCode());
    result = prime * result + ((windowSize == null) ? 0 : windowSize.hashCode());
    result = prime * result + ((wordVecDim == null) ? 0 : wordVecDim.hashCode());
    result = prime * result + ((wordVecFilepath == null) ? 0 : wordVecFilepath.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Configuration other = (Configuration) obj;
    if (allLowercase == null) {
      if (other.allLowercase != null)
        return false;
    } else if (!allLowercase.equals(other.allLowercase))
      return false;
    if (!Arrays.equals(hiddenDimensions, other.hiddenDimensions))
      return false;
    if (lambda == null) {
      if (other.lambda != null)
        return false;
    } else if (!lambda.equals(other.lambda))
      return false;
    if (learnWordVec == null) {
      if (other.learnWordVec != null)
        return false;
    } else if (!learnWordVec.equals(other.learnWordVec))
      return false;
    if (learningRate == null) {
      if (other.learningRate != null)
        return false;
    } else if (!learningRate.equals(other.learningRate))
      return false;
    if (maxIterations == null) {
      if (other.maxIterations != null)
        return false;
    } else if (!maxIterations.equals(other.maxIterations))
      return false;
    if (outputFile == null) {
      if (other.outputFile != null)
        return false;
    } else if (!outputFile.equals(other.outputFile))
      return false;
    if (targetEntities == null) {
      if (other.targetEntities != null)
        return false;
    } else if (!targetEntities.equals(other.targetEntities))
      return false;
    if (testFilepath == null) {
      if (other.testFilepath != null)
        return false;
    } else if (!testFilepath.equals(other.testFilepath))
      return false;
    if (trainFilepath == null) {
      if (other.trainFilepath != null)
        return false;
    } else if (!trainFilepath.equals(other.trainFilepath))
      return false;
    if (unknownWord == null) {
      if (other.unknownWord != null)
        return false;
    } else if (!unknownWord.equals(other.unknownWord))
      return false;
    if (vocabFilepath == null) {
      if (other.vocabFilepath != null)
        return false;
    } else if (!vocabFilepath.equals(other.vocabFilepath))
      return false;
    if (windowSize == null) {
      if (other.windowSize != null)
        return false;
    } else if (!windowSize.equals(other.windowSize))
      return false;
    if (wordVecDim == null) {
      if (other.wordVecDim != null)
        return false;
    } else if (!wordVecDim.equals(other.wordVecDim))
      return false;
    if (wordVecFilepath == null) {
      if (other.wordVecFilepath != null)
        return false;
    } else if (!wordVecFilepath.equals(other.wordVecFilepath))
      return false;
    return true;
  }
  
}
