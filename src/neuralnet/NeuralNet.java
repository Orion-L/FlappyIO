
package neuralnet;

import java.util.ArrayList;

public class NeuralNet implements Comparable<NeuralNet> {
	private Neuron[] inputLayer;
	private Neuron[] outputLayer;
	private Neuron[][] hiddenLayers;
	private int score;
	
	/**
	 * Initialise a new neural network
	 * @param numInput Number of input neurons
	 * @param numOutput Number of output neurons
	 * @param numHidden Number of neurons in each hidden layer
	 */
	public NeuralNet(int numInput, int numOutput, int[] numHidden) {
		this.score = 0;
		
		// Initialise the layer arrays
		this.inputLayer = new Neuron[numInput];
		this.outputLayer = new Neuron[numOutput];
		this.hiddenLayers = new Neuron[numHidden.length][];
		
		// Populate the input layer
		for (int i = 0; i < this.inputLayer.length; i++) {
			this.inputLayer[i] = new Neuron(0);
		}
		
		// Loop through the hidden layers
		for (int i = 0; i < this.hiddenLayers.length; i++) {
			// Initialise each layer
			this.hiddenLayers[i] = new Neuron[numHidden[i]];
			
			for (int j = 0; j < this.hiddenLayers[i].length; j++) {
				// Populate each layer, use size of previous layer as input size
				if (i == 0) {
					this.hiddenLayers[i][j] = new Neuron(this.inputLayer.length);
				} else {
					this.hiddenLayers[i][j] = new Neuron(this.hiddenLayers[i - 1].length);
				}
			}
		}
		
		// Populate the output layer
		for (int i = 0; i < this.outputLayer.length; i++) {
			if (this.hiddenLayers.length > 0) {
				this.outputLayer[i] = new Neuron(this.hiddenLayers[this.hiddenLayers.length - 1].length);
			} else {
				this.outputLayer[i] = new Neuron(this.inputLayer.length);
			}
		}
		
	}
	
	/**
	 * Evaluate the output of the network with given inputs
	 * @param inputs Array of inputs corresponding to input neurons
	 * @return Array corresponding to values of output neurons
	 */
	public double[] evaluateNetwork(double[] inputs) {
		// Assign values to input layer
		for (int i = 0; i < this.inputLayer.length; i++) {
			this.inputLayer[i].setValue(inputs[i]);
		}
		
		if (this.hiddenLayers.length > 0) {
			// Evaluate each neuron in first hidden layer
			// Input layer acts as input of first hidden layer
			for (int i = 0; i < this.hiddenLayers[0].length; i++) {
				evaluateNeuron(this.inputLayer, this.hiddenLayers[0][i]);
			}
			
			// Evaluate each neuron in subsequent hidden layers
			// Previous layer acts as input into next layer
			for (int i = 1; i < this.hiddenLayers.length; i++) {
				for (int j = 0; j < this.hiddenLayers[i].length; j++) {
					evaluateNeuron(this.hiddenLayers[i - 1], this.hiddenLayers[i][j]);
				}
			}
			
			// Evaluate each neuron in output layer
			// Last hidden layer acts as input
			for (int i = 0; i < this.outputLayer.length; i++) {
				evaluateNeuron(this.hiddenLayers[this.hiddenLayers.length - 1], this.outputLayer[i]);
			}
		} else {
			// No hidden layer, use input layer as inputs to output layer
			for (int i = 0; i < this.outputLayer.length; i++) {
				evaluateNeuron(this.inputLayer, this.outputLayer[i]);
			}
		}
		
		// Place output values into return array
		double[] ret = new double[this.outputLayer.length];
		for (int i  = 0; i < this.outputLayer.length; i++) {
			ret[i] = this.outputLayer[i].getValue();
		}
		
		return ret;
		
	}
	
	/**
	 * Get a flat array representation of network weights
	 * @return An ArrayList of weights
	 */
	public ArrayList<Double> getWeights() {
		ArrayList<Double> weights = new ArrayList<Double>();
		
		// Consider all neurons in every hidden layer
		for (int i = 0; i < this.hiddenLayers.length; i++) {
			for (int j = 0; j < this.hiddenLayers[i].length; j++) {
				if (i == 0) {
					// First hidden layer, get corresponding weights from all neurons in input layer
					for (int k = 0; k < this.inputLayer.length; k++) {
						weights.add(this.hiddenLayers[i][j].getWeight(k));
					}
				} else {
					// Get corresponding weights from all neurons in previous layer 
					for (int k = 0; k < this.hiddenLayers[i - 1].length; k++) {
						weights.add(this.hiddenLayers[i][j].getWeight(k));
					}
				}
			}
		}
		
		// Consider all neurons in output layer
		for (int i = 0; i < this.outputLayer.length; i++) {
			if (this.hiddenLayers.length > 0) {
				// Get weights from last hidden layer
				for (int j = 0; j < this.hiddenLayers[this.hiddenLayers.length - 1].length; j++) {
					weights.add(this.outputLayer[i].getWeight(j));
				}
			} else {
				// No hidden layer, get weights from input layer
				for (int j = 0; j < this.inputLayer.length; j++) {
					weights.add(this.outputLayer[i].getWeight(j));
				}
			}
		}
		
		return weights;
	}
	
	/**
	 * Update the network to represent the given network weights
	 * @param weights ArrayList corresponding to the new network weights
	 */
	public void setWeights(ArrayList<Double> weights) {
		int arrCounter = 0;
		
		// Visits all neurons in same order as getWeights
		// Assigns weights instead of retrieving
		
		for (int i = 0; i < this.hiddenLayers.length; i++) {
			for (int j = 0; j < this.hiddenLayers[i].length; j++) {
				if (i == 0) {
					for (int k = 0; k < this.inputLayer.length; k++) {
						// Bail if weight array is smaller than network
						if (arrCounter > weights.size() - 1) return;
						
						this.hiddenLayers[i][j].setWeight(k, weights.get(arrCounter));
						arrCounter++;
					}
				} else {
					for (int k = 0; k < this.hiddenLayers[i - 1].length; k++) {
						if (arrCounter > weights.size() - 1) return;
						
						this.hiddenLayers[i][j].setWeight(k, weights.get(arrCounter));
						arrCounter++;
					}
				}
			}
		}
		
		for (int i = 0; i < this.outputLayer.length; i++) {
			if (this.hiddenLayers.length > 0) {
				for (int j = 0; j < this.hiddenLayers[this.hiddenLayers.length - 1].length; j++) {
					if (arrCounter > weights.size() - 1) return;
					
					this.outputLayer[i].setWeight(j, weights.get(arrCounter));
					arrCounter++;
				}
			} else {
				for (int j = 0; j < this.inputLayer.length; j++) {
					if (arrCounter > weights.size() - 1) return;
					
					this.outputLayer[i].setWeight(j, weights.get(arrCounter));
					arrCounter++;
				}
			}
		}
	}
	
	public int getScore() {
		return this.score;
	}
	
	
	public void setScore(int score) {
		this.score = score;
	}
	
	@Override
	public NeuralNet clone() {
		// Set up the hidden layer array
		int[] hidden = new int[this.hiddenLayers.length];
		for (int i = 0; i < this.hiddenLayers.length; i++) {
			hidden[i] = this.hiddenLayers[i].length;
		}
		
		// Initialise a new neural net
		NeuralNet n = new NeuralNet(this.inputLayer.length, this.outputLayer.length, hidden);
		
		// Clone each neuron into new neural net
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
		
		// Set the score
		n.score = this.score;
		
		return n;
	}

	@Override
	public int compareTo(NeuralNet n) {
		return n.score - this.score;
	}
	
	private double activate(double x) {
		return 1 / (1 + Math.exp(-x));
	}
	
	private void evaluateNeuron(Neuron[] inputs, Neuron output) {
		// Sum weighted inputs and apply activation function
		double inputSum = 0;
		for (int i = 0; i < inputs.length; i++) {
			inputSum += inputs[i].getValue() * output.getWeight(i);
		}
		
		output.setValue(activate(inputSum));
	}
}
