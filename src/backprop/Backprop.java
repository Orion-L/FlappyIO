package backprop;

import neuralnet.NeuralNet;

public class Backprop {
	static private final int goal = 10000;
	private int score;
    private NeuralNet network;

    public Backprop(int numInput, int numOutput,
    				int[] numHidden) {
    	this.score = 0;
    	this.network = new NeuralNet(numInput, numOutput, numHidden);
    }
    
    public double[] evaluate(double[] inputs) {
    	return this.network.evaluateNetwork(inputs);
    }
    
    public void updateScore(int score) {
    	this.score = score;
    }
    
    public void updateNetwork() {
    	// do backprop
    }

    private double costFunction(double y, double yEst) {
        return ((1 / 2) * Math.pow(yEst - y, 2));
    }
}
