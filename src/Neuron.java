
public class Neuron {
	private double value;
	private double inputWeights[];
	
	public Neuron(int numInputs) {
		value = 0;
		inputWeights = new double[numInputs];
	}
	
	public void setValue(double value) {
		this.value = value;
	}
	
	public void setWeight(int input, double weight) {
		inputWeights[input] = weight;
	}
	
	public double getValue() {
		return this.value;
	}
	
	public double getWeight(int input) {
		return inputWeights[input];
	}
}
