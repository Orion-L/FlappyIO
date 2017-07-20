package neuroevolver;

import java.util.LinkedList;

class NeuralNet {
	private Neuron[] inputLayer;
	private Neuron[] outputLayer;
	private Neuron[][] hiddenLayers;
	
	public NeuralNet(int numInput, int numOutput, int[] numHidden) {
		inputLayer = new Neuron[numInput];
		outputLayer = new Neuron[numOutput];
		hiddenLayers = new Neuron[numHidden.length][];

		for (int i = 0; i < this.inputLayer.length; i++) {
			this.inputLayer[i] = new Neuron(0);
		}
		
		for (int i = 0; i < this.hiddenLayers.length; i++) {
			this.hiddenLayers[i] = new Neuron[numHidden[i]];
			
			for (int j = 0; j < this.hiddenLayers[i].length; j++) {
				if (i == 0) {
					this.hiddenLayers[i][j] = new Neuron(this.inputLayer.length);
				} else {
					this.hiddenLayers[i][j] = new Neuron(this.hiddenLayers[i - 1].length);
				}
			}
		}
		
		for (int i = 0; i < this.outputLayer.length; i++) {
			if (this.hiddenLayers.length > 0) {
				this.outputLayer[i] = new Neuron(this.hiddenLayers[this.hiddenLayers.length - 1].length);
			} else {
				this.outputLayer[i] = new Neuron(this.inputLayer.length);
			}
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
	
	public double[] getWeights() {
		LinkedList<Double> weights = new LinkedList<Double>();
		
		for (int i = 0; i < hiddenLayers.length; i++) {
			for (int j = 0; j < hiddenLayers[i].length; j++) {
				if (i == 0) {
					for (int k = 0; k < inputLayer.length; k++) {
						weights.add(hiddenLayers[i][j].getWeight(k));
					}
				} else {
					for (int k = 0; k < hiddenLayers[i - 1].length; k++) {
						weights.add(hiddenLayers[i][j].getWeight(k));
					}
				}
			}
		}
		
		for (int i = 0; i < outputLayer.length; i++) {
			if (hiddenLayers.length > 0) {
				for (int j = 0; j < hiddenLayers[hiddenLayers.length - 1].length; j++) {
					weights.add(outputLayer[i].getWeight(j));
				}
			} else {
				for (int j = 0; j < inputLayer.length; j++) {
					weights.add(outputLayer[i].getWeight(j));
				}
			}
		}
		
		double[] ret = new double[weights.size()];
		for (int i = 0; i < weights.size(); i++) {
			ret[i] = weights.get(i);
		}
		
		return ret;
	}
	
	public void setWeights(double[] weights) {
		int arrCounter = 0;
		
		for (int i = 0; i < hiddenLayers.length; i++) {
			for (int j = 0; j < hiddenLayers[i].length; j++) {
				if (i == 0) {
					for (int k = 0; k < inputLayer.length; k++) {
						hiddenLayers[i][j].setWeight(k, weights[arrCounter]);
						arrCounter++;
					}
				} else {
					for (int k = 0; k < hiddenLayers[i - 1].length; k++) {
						hiddenLayers[i][j].setWeight(k, weights[arrCounter]);
						arrCounter++;
					}
				}
			}
		}
		
		for (int i = 0; i < outputLayer.length; i++) {
			if (hiddenLayers.length > 0) {
				for (int j = 0; j < hiddenLayers[hiddenLayers.length - 1].length; j++) {
					outputLayer[i].setWeight(j, weights[arrCounter]);
					arrCounter++;
				}
			} else {
				for (int j = 0; j < inputLayer.length; j++) {
					outputLayer[i].setWeight(j, weights[arrCounter]);
					arrCounter++;
				}
			}
		}
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
		return 1 / (1 + Math.exp(-x));
	}
	
	private void evaluateNeuron(Neuron[] inputs, Neuron output) {
		double inputSum = 0;
		for (int i = 0; i < inputs.length; i++) {
			inputSum += inputs[i].getValue() * output.getWeight(i);
		}
		
		output.setValue(activate(inputSum));
	}
}
