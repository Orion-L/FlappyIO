package neuroevolver;

class Genome {
	private int score;
	private NeuralNet network;
	
	public Genome(NeuralNet network, int score) {
		this.score = score;
		this.network = network;
	}
	
	public void setScore(int score) {
		this.score = score;
	}
	
	public void setNetwork(NeuralNet network) {
		this.network = network;
	}
	
	public int getScore() {
		return this.score;
	}
	
	public NeuralNet getNetwork() {
		return this.network;
	}
	
	public Genome clone() {
		return new Genome(this.network.clone(), this.score);
	}
}
