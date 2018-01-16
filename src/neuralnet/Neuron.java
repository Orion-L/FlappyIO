
package neuralnet;

class Neuron {
	private double inputValue;
	private double outputValue;
	private double inputWeights[];
	
	/**
	 * Create a new neuron
	 * @param numInputs Number of inputs of the neuron
	 */
	public Neuron(int numInputs) {
		this.inputValue = 0;
		this.outputValue = 0;
		this.inputWeights = new double[numInputs];
		
		// Set weights of input nodes randomly between -1 and 1 
		for (int i = 0; i < this.inputWeights.length; i++) {
			this.inputWeights[i] = Math.random() * 2 - 1;
		}
	}
	
	/**
	 * Set the input value (pre-activation) of the neuron
	 * @param value The new input value
	 */
	public void setInputValue(double value) {
		this.inputValue = value;
	}

	/**
	 * Set the output value (post-activation) of the neuron
	 * @param value The new output value
	 */
	public void setOutputValue(double value) {
		this.outputValue = value;
	}
	
	/**
	 * Set the weight of a particular input
	 * @param input The input edge to modify
	 * @param weight The new weight of the edge
	 */
	public void setWeight(int input, double weight) {
		this.inputWeights[input] = weight;
	}
	
	/**
	 * Get the input (pre-activation) value of the neuron
	 * @return The input value of the neuron
	 */
	public double getInputValue() {
		return this.inputValue;
	}
	
	/**
	 * Get the output (post-activation) value of the neuron
	 * @return The output value of the neuron
	 */
	public double getOutputValue() {
		return this.outputValue;
	}
	
	/**
	 * Get the weight of an input edge
	 * @param input The input to retrieve the weight of
	 * @return The weight of the corresponding edge
	 */
	public double getWeight(int input) {
		return this.inputWeights[input];
	}
	
	@Override
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
