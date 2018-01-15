package neuralnet;

public class NodeHandle {
	private int layer;
	private int nodeNum;
	
	public NodeHandle(int layer, int nodeNum) {
		this.layer = layer;
		this.nodeNum = nodeNum;
	}
	
	public int getLayer() {
		return this.layer;
	}
	
	public int getNodeNum() {
		return this.nodeNum;
	}
}
