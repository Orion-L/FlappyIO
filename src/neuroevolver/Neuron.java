package neuroevolver;

class Neuron {
	private double value;
	private double inputWeights[];
	
	public Neuron(int numInputs) {
		this.value = 0;
		this.inputWeights = new double[numInputs];
		
		for (int i = 0; i < this.inputWeights.length; i++) {
			this.inputWeights[i] = Math.random() * 2 - 1;
		}
	}
	
	public void setValue(double value) {
		this.value = value;
	}
	
	public void setWeight(int input, double weight) {
		this.inputWeights[input] = weight;
	}
	
	public double getValue() {
		return this.value;
	}
	
	public double getWeight(int input) {
		return this.inputWeights[input];
	}
	
	public Neuron clone() {
		Neuron n = new Neuron(this.inputWeights.length);
		n.value = this.value;
		
		for (int i = 0; i < this.inputWeights.length; i++) {
			n.inputWeights[i] = this.inputWeights[i];
		}
		
		return n;
	}
}
