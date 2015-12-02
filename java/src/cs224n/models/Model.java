package cs224n.models;

import java.util.List;

import cs224n.deep.Datum;

/**
 * NER Model Interface:
 *  Models are required to have a train and test function
 * @author afg479
 *
 */
public interface Model {
  public void train(List<Datum> trainData);
  public List<String> test(List<Datum> testData);
}