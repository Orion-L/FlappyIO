package neuralnet;

public class NodeHandle {
	private int layer;
	private int nodeNum;
	
	/**
	 * Create a new node handle
	 * @param layer The layer the node resides in, zero-indexed
	 * @param nodeNum The number of the node within the layer, zero-indexed
	 */
	public NodeHandle(int layer, int nodeNum) {
		this.layer = layer;
		this.nodeNum = nodeNum;
	}
	
	/**
	 * Get the layer the node is in
	 * @return The node's layer
	 */
	public int getLayer() {
		return this.layer;
	}
	
	/**
	 * Get the number of the node
	 * @return The node's number
	 */
	public int getNodeNum() {
		return this.nodeNum;
	}
}
