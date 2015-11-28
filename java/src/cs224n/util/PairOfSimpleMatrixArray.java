package cs224n.util;

import org.ejml.simple.SimpleMatrix;

public class PairOfSimpleMatrixArray {
	private SimpleMatrix[] firstSimpleMatrixArray;
	private SimpleMatrix[] secondSimpleMatrixArray;
	
	public PairOfSimpleMatrixArray(SimpleMatrix[] firstArray, SimpleMatrix[] secondArray) {
		this.firstSimpleMatrixArray = firstArray;
		this.secondSimpleMatrixArray = secondArray;
	}

	public SimpleMatrix[] getFirstSimpleMatrixArray() {
		return firstSimpleMatrixArray;
	}

	public void setFirstSimpleMatrixArray(SimpleMatrix[] firstSimpleMatrixArray) {
		this.firstSimpleMatrixArray = firstSimpleMatrixArray;
	}

	public SimpleMatrix[] getSecondSimpleMatrixArray() {
		return secondSimpleMatrixArray;
	}

	public void setSecondSimpleMatrixArray(SimpleMatrix[] secondSimpleMatrixArray) {
		this.secondSimpleMatrixArray = secondSimpleMatrixArray;
	}
	
	
}
