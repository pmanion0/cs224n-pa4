package cs224n.deep;

import org.ejml.simple.SimpleMatrix;
import java.util.Random;

public class FakeNeuralNetwork {
  
  Random gen;

  public FakeNeuralNetwork() {
    // Parameters? Who cares!
    gen = new Random(3409284);
  }
  
  public double runBackprop(SimpleMatrix X, SimpleMatrix Y) {
    return 0.0;
  }
  
  public int getBestOutputClass(SimpleMatrix X) {
    return gen.nextInt(7);
  }
}
