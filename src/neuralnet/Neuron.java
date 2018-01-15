
package neuralnet;

class Neuron {
	private double inputValue;
	private double outputValue;
	private double inputWeights[];
	
	public Neuron(int numInputs) {
		this.inputValue = 0;
		this.outputValue = 0;
		this.inputWeights = new double[numInputs];
		
		for (int i = 0; i < this.inputWeights.length; i++) {
			this.inputWeights[i] = Math.random() * 2 - 1;
		}
	}
	
	public void setInputValue(double value) {
		this.inputValue = value;
	}

	public void setOutputValue(double value) {
		this.outputValue = value;
	}
	
	public void setWeight(int input, double weight) {
		this.inputWeights[input] = weight;
	}
	
	public double getInputValue() {
		return this.inputValue;
	}
	
	public double getOutputValue() {
		return this.outputValue;
	}
	
	public double getWeight(int input) {
		return this.inputWeights[input];
	}
	
	public Neuron clone() {
		Neuron n = new Neuron(this.inputWeights.length);
		n.inputValue = this.inputValue;
		n.outputValue = this.outputValue;
		
		for (int i = 0; i < this.inputWeights.length; i++) {
			n.inputWeights[i] = this.inputWeights[i];
		}
		
		return n;
	}
}
