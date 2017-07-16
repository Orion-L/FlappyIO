
public class NeuralNet {
	private Neuron[] inputLayer;
	private Neuron[] outputLayer;
	private Neuron[][] hiddenLayers;
	
	private int population;
	
	
	public NeuralNet(int numInput, int numOutput, int[] numHidden) {
		inputLayer = new Neuron[numInput];
		outputLayer = new Neuron[numOutput];
		hiddenLayers = new Neuron[numHidden.length][];
		
		for (int i = 0; i < numHidden.length; i++) {
			hiddenLayers[i] = new Neuron[numHidden[i]];
		}
	}
}
