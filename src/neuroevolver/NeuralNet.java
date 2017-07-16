package neuroevolver;

class NeuralNet {
	private Neuron[] inputLayer;
	private Neuron[] outputLayer;
	private Neuron[][] hiddenLayers;
	
	private static final int population = 50;
	
	
	public NeuralNet(int numInput, int numOutput, int[] numHidden) {
		inputLayer = new Neuron[numInput];
		outputLayer = new Neuron[numOutput];
		hiddenLayers = new Neuron[numHidden.length][];
		
		for (int i = 0; i < numHidden.length; i++) {
			hiddenLayers[i] = new Neuron[numHidden[i]];
		}
	}
	
	public double[] evaluateNetwork(double[] inputs) {
		for (int i = 0; i < inputLayer.length; i++) {
			inputLayer[i].setValue(inputs[i]);
		}
		
		if (hiddenLayers.length > 0) {
			for (int i = 0; i < hiddenLayers[0].length; i++) {
				evaluateNeuron(inputLayer, hiddenLayers[0][i]);
			}
			
			for (int i = 1; i < hiddenLayers.length; i++) {
				for (int j = 0; j < hiddenLayers[i].length; j++) {
					evaluateNeuron(hiddenLayers[i - 1], hiddenLayers[i][j]);
				}
			}
			
			for (int i = 0; i < outputLayer.length; i++) {
				evaluateNeuron(hiddenLayers[hiddenLayers.length - 1], outputLayer[i]);
			}
		} else {
			for (int i = 0; i < outputLayer.length; i++) {
				evaluateNeuron(inputLayer, outputLayer[i]);
			}
		}
		
		double[] ret = new double[outputLayer.length];
		for (int i  = 0; i < outputLayer.length; i++) {
			ret[i] = outputLayer[i].getValue();
		}
		
		return ret;
		
	}
	
	public NeuralNet clone() {
		int[] hidden = new int[hiddenLayers.length];
		for (int i = 0; i < hiddenLayers.length; i++) {
			hidden[i] = hiddenLayers[i].length;
		}
		
		NeuralNet n = new NeuralNet(this.inputLayer.length, this.outputLayer.length, hidden);
		
		for (int i = 0; i < n.inputLayer.length; i++) {
			n.inputLayer[i] = this.inputLayer[i].clone();
		}
		
		for (int i = 0; i < n.outputLayer.length; i++) {
			n.outputLayer[i] = this.outputLayer[i].clone();
		}
		
		for (int i = 0; i < n.hiddenLayers.length; i++) {
			for (int j = 0; j < n.hiddenLayers[i].length; j++) {
				n.hiddenLayers[i][j] = this.hiddenLayers[i][j].clone();
			}
		}
		
		return n;
	}
	
	private double activate(double x) {
		return 1 / (1 + Math.exp(x * -1));
	}
	
	private void evaluateNeuron(Neuron[] inputs, Neuron output) {
		double inputSum = 0;
		for (int i = 0; i < inputs.length; i++) {
			inputSum += inputLayer[i].getValue() * output.getWeight(i);
		}
		
		output.setValue(activate(inputSum));
	}
}
