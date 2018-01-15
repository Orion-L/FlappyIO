package backprop;

import neuralnet.NeuralNet;
import neuralnet.NodeHandle;

public class Backprop {
	private static final double learnRate = 0.5;
	
	private int numInput, numOutput;
	private int[] numHidden;
    private double[] yExp;
    private NeuralNet network;
    private double[][] deltaArray;
    private boolean[][] deltaCalculated;
    
    public Backprop(int numInput, int numOutput,
    				int[] numHidden) {
    	this.numInput = numInput;
    	this.numOutput = numOutput;
    	this.numHidden = numHidden.clone();
    	this.yExp = new double[numOutput];
    	
    	this.network = new NeuralNet(numInput, numOutput, numHidden);
    	
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
    }
    
    public double[] evaluate(double[] inputs) {
    	return this.network.evaluateNetwork(inputs);
    }
    
    public void updateNetwork(double[] yExp) {
    	this.yExp = yExp.clone();
    	
    	for (int i = 0; i < this.deltaCalculated.length; i++) {
    		for (int j = 0; j < this.deltaCalculated[i].length; j++) {
    			this.deltaCalculated[i][j] = false;
    		}
    	}
    	
    	for (int i = 0; i < this.numInput; i++) {
    		calcDelta(new NodeHandle(0, i));
    	}
    	
    	for (int i = 0; i < this.deltaArray.length; i++) {
    		for (int j = 0; j < this.deltaArray[i].length; j++) {
    			NodeHandle parent = new NodeHandle(i, j);
    			NodeHandle[] children = this.network.getChildren(parent);
    			for (NodeHandle c : children) {
    				double newWeight = this.network.getWeight(parent, c);
    				newWeight -= Backprop.learnRate * this.deltaArray[c.getLayer()][c.getNodeNum()] *
    							 this.network.getOutputValue(parent);
    				
    				this.network.setWeight(parent, c, newWeight);
    			}
    		}
    	}
    }
    
    private void calcDelta(NodeHandle node) {
    	if (node.getLayer() == this.numHidden.length + 1) {
    		double yInput = this.network.getInputValue(node);
    		double yOutput = this.network.getOutputValue(node);
    		this.deltaArray[node.getLayer()][node.getNodeNum()] = 
    				(yOutput - this.yExp[node.getNodeNum()]) * this.network.activateDerivative(yInput);
    	} else {
    		NodeHandle[] children = this.network.getChildren(node);
    		double deltaSum = 0;
    		for (int i = 0; i < children.length; i++) {
    			if (!this.deltaCalculated[children[i].getLayer()][children[i].getNodeNum()]) {
    				calcDelta(children[i]);
    			}
    			
				deltaSum += this.deltaArray[children[i].getLayer()][children[i].getNodeNum()] * 
							this.network.getWeight(node, children[i]);
    		}
    		
    		this.deltaArray[node.getLayer()][node.getNodeNum()] = 
    				this.network.activateDerivative(this.network.getInputValue(node)) * deltaSum;
    	}
    }
}
