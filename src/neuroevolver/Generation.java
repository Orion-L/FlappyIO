package neuroevolver;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

class Generation {
	private double crossoverRate, mutationRate, mutationRange;
	private int numChildren;
	private LinkedList<Genome> genomes;
	
	public Generation(int numChildren, double crossoverRate, double mutationRate, double mutationRange) {
		this.crossoverRate = crossoverRate;
		this.mutationRange = mutationRange;
		this.mutationRate = mutationRate;
		this.numChildren = numChildren;
		this.genomes = new LinkedList<Genome>();
	}
	
	public void addGenome(Genome g) {
		genomes.add(g);
	}
	
	public void sortGenomes() {
		Collections.sort((List<Genome>) genomes);
	}
	
	public Genome getGenome(int i) {
		return genomes.get(i);
	}
	
	public int genomeLength() {
		return genomes.size();
	}
	
	public Genome[] breedGenomes(Genome g, Genome h) {
		LinkedList<Genome> ret = new LinkedList<Genome>();
		for (int i = 0; i < this.numChildren; i++) {
			Genome child = g.clone();
			
			NeuralNet hNet = h.getNetwork();
			double[] hNetWeights = hNet.getWeights();
			
			NeuralNet childNet = child.getNetwork();
			double[] childWeights = childNet.getWeights();
			
			for (int j = 0; j < hNetWeights.length; j++) {
				if (Math.random() > this.crossoverRate) {
					childWeights[j] = hNetWeights[j];
				}
			}
			
			for (int j = 0; j < childWeights.length; j++) {
				if (Math.random() > this.mutationRate) {
					childWeights[j] += Math.random() * this.mutationRange * 2 - this.mutationRange;
				}
			}
			
			childNet.setWeights(childWeights);
			
			ret.add(child);
		}
		
		Genome[] retArr = new Genome[ret.size()];
		for (int i = 0; i < ret.size(); i++) {
			retArr[i] = ret.get(i);
		}
		
		return retArr;
	}
}
