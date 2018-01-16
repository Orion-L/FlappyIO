package backprop;

import neuralnet.NeuralNet;
import neuralnet.NodeHandle;

public class Backprop {
	private int numInput, numOutput;
	private int[] numHidden;
    private double[] yExp;
    private NeuralNet network;
    private double[][] deltaArray;
    private boolean[][] deltaCalculated;
	private double learnRate;
    
	/**
	 * Initialise a new backpropagation instance
	 * @param numInput Number of nodes in the input layer
	 * @param numOutput Number of nodes in the output layer
	 * @param numHidden Number of nodes in the hidden layers
	 * @param learnRate Learning rate of the algorithm
	 */
    public Backprop(int numInput, int numOutput,
    				int[] numHidden, double learnRate) {
    	this.numInput = numInput;
    	this.numOutput = numOutput;
    	this.numHidden = numHidden.clone();
    	this.yExp = new double[numOutput];
    	
    	this.network = new NeuralNet(numInput, numOutput, numHidden);
    	
    	// Setup the delta arrays
    	this.deltaArray = new double[numHidden.length + 2][];
    	this.deltaCalculated = new boolean[numHidden.length + 2][];
    	for (int i = 0; i < this.deltaArray.length; i++) {
    		if (i == 0) {
    			this.deltaArray[i] = new double[numInput];
    			this.deltaCalculated[i] = new boolean[numInput];
    		} else if (i == this.deltaArray.length - 1) {
    			this.deltaArray[i] = new double[numOutput];
    			this.deltaCalculated[i] = new boolean[numOutput];
    		} else {
    			this.deltaArray[i] = new double[numHidden[i - 1]];
    			this.deltaCalculated[i] = new boolean[numHidden[i - 1]];
    		}
    	}
    	
    	this.learnRate = learnRate;
    }
    
    /**
     * Evaluate the network
     * @param inputs Array of network inputs
     * @return Array of network outputs
     */
    public double[] evaluate(double[] inputs) {
    	return this.network.evaluateNetwork(inputs);
    }
    
    /**
     * Perform backpropagation
     * @param yExp Array of expected network outputs
     */
    public void updateNetwork(double[] yExp) {
    	this.yExp = yExp.clone();
    	
    	// Reset the delta flag array
    	for (int i = 0; i < this.deltaCalculated.length; i++) {
    		for (int j = 0; j < this.deltaCalculated[i].length; j++) {
    			this.deltaCalculated[i][j] = false;
    		}
    	}
    	
    	// Recursively calculate the delta's of all nodes
    	for (int i = 0; i < this.numInput; i++) {
    		calcDelta(new NodeHandle(0, i));
    	}
    	
    	// Loop through all nodes
    	for (int i = 0; i < this.deltaArray.length - 1; i++) {
    		for (int j = 0; j < this.deltaArray[i].length; j++) {
    			NodeHandle parent = new NodeHandle(i, j);
    			NodeHandle[] children = this.network.getChildren(parent);
    			
    			// Loop through all children for each node
    			for (NodeHandle c : children) {
    				// Perform the weight update
    				double newWeight = this.network.getWeight(parent, c);
    				newWeight -= this.learnRate * this.deltaArray[c.getLayer()][c.getNodeNum()] *
    							 this.network.getOutputValue(parent);
    				this.network.setWeight(parent, c, newWeight);
    			}
    		}
    	}
    }
    
    /**
     * Calculate the delta recursively for a given node
     * @param node The node to calculate the delta for
     */
    private void calcDelta(NodeHandle node) {
    	if (node.getLayer() == this.numHidden.length + 1) {
    		// Node is in the output layer
    		double yInput = this.network.getInputValue(node);
    		double yOutput = this.network.getOutputValue(node);
    		this.deltaArray[node.getLayer()][node.getNodeNum()] = 
    				(yOutput - this.yExp[node.getNodeNum()]) * this.network.activateDerivative(yInput);
    	} else {
    		// Calculate delta sum of children
    		NodeHandle[] children = this.network.getChildren(node);
    		double deltaSum = 0;
    		for (NodeHandle c : children) {
				// Calculate delta of child if not already calculated
    			if (!this.deltaCalculated[c.getLayer()][c.getNodeNum()]) {
    				calcDelta(c);
    			}
    			
				deltaSum += this.deltaArray[c.getLayer()][c.getNodeNum()] * 
							this.network.getWeight(node, c);
    		}
    		
    		this.deltaArray[node.getLayer()][node.getNodeNum()] = 
    				this.network.activateDerivative(this.network.getInputValue(node)) * deltaSum;
    	}
    	
    	// Set the delta flag
    	this.deltaCalculated[node.getLayer()][node.getNodeNum()] = true;
    }
}
