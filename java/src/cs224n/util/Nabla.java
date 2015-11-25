package cs224n.util;

import org.ejml.simple.SimpleMatrix;

public class Nabla {
	public SimpleMatrix [] nabla_w;
	public SimpleMatrix [] nabla_b;
	
	public Nabla( SimpleMatrix[] nabla_w, SimpleMatrix[] nabla_b) {
				this.nabla_w = nabla_w;
				this.nabla_b = nabla_b;
				}

	public SimpleMatrix[] getNabla_w() {
		return nabla_w;
	}

	public void setNabla_w(SimpleMatrix[] nabla_w) {
		this.nabla_w = nabla_w;
	}

	public SimpleMatrix[] getNabla_b() {
		return nabla_b;
	}

	public void setNabla_b(SimpleMatrix[] nabla_b) {
		this.nabla_b = nabla_b;
	}
	
	
}
