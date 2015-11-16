package cs224n.deep;

import java.util.List;

/**
 * NER Model Interface:
 *  Models are required to have a train and test function
 * @author afg479
 *
 */
public interface NERModel {
  public void train(List<Datum> trainData);
  public void test(List<Datum> testData, String outfile);
}