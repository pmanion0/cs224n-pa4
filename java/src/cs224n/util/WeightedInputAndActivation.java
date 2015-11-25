package cs224n.util;

import org.ejml.simple.SimpleMatrix;

public class WeightedInputAndActivation {
	public SimpleMatrix [] weightedInput;
	public SimpleMatrix [] activation;
	
	public WeightedInputAndActivation( SimpleMatrix[] weightedInput, 
							SimpleMatrix[] activation) {
		this.weightedInput = weightedInput;
		this.activation = activation;
	}
	
	public SimpleMatrix[] getWeightedInput() {
		return weightedInput;
	}
	public void setWeightedInput(SimpleMatrix[] weightedInput) {
		this.weightedInput = weightedInput;
	}
	public SimpleMatrix[] getActivation() {
		return activation;
	}
	public void setActivation(SimpleMatrix[] activation) {
		this.activation = activation;
	}
	
	
}
