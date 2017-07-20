package neuroevolver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Neuroevolver {
	private int genPopulation, genChildren, numInput, numOutput;
	private int[] numHidden;
	private double crossoverRate, mutationRate, mutationRange, randomBehaviour, elitism;
	private ArrayList<NeuralNet> currentGeneration;
	
	public Neuroevolver(int generationPopulation, int generationChildren, 
						double crossoverRate, double mutationRate, 
						double mutationRange, double randomBehaviour, 
						double elitism, int numInput, int numOutput, int[] numHidden) {
		this.genPopulation = generationPopulation;
		this.genChildren = generationChildren;
		this.crossoverRate = crossoverRate;
		this.mutationRange = mutationRange;
		this.mutationRate = mutationRate;
		this.randomBehaviour = randomBehaviour;
		this.elitism = elitism;
		this.currentGeneration = null;
		this.numInput = numInput;
		this.numOutput = numOutput;
		this.numHidden = numHidden;
	}
	
	public void updateScore(int genomeNum, int score) {
		this.currentGeneration.get(genomeNum).setScore(score);
	}
	
	public int generationSize() {
		return this.currentGeneration.size();
	}
	
	public double[] evaluateGenome(int genomeNum, double[] inputs) {
		if (genomeNum == 0) {
			System.out.println(inputs[0] + " " + inputs[1] + " " + this.currentGeneration.get(genomeNum).evaluateNetwork(inputs)[0]);
		}
		return this.currentGeneration.get(genomeNum).evaluateNetwork(inputs);
	}
	
	public void nextGeneration() {
		if (this.currentGeneration == null) {
			this.currentGeneration = new ArrayList<NeuralNet>();
			
			for (int i = 0; i < this.genPopulation; i++) {
				this.currentGeneration.add(new NeuralNet(this.numInput, this.numOutput, this.numHidden));
			}
		} else {
			Collections.sort((List<NeuralNet>) this.currentGeneration);
			ArrayList<NeuralNet> nextGen = new ArrayList<NeuralNet>();
			for (int i = 0; i < Math.round(this.elitism * this.genPopulation); i++) {
				if (nextGen.size() < this.genPopulation) {
					nextGen.add(this.currentGeneration.get(i).clone());
					nextGen.get(nextGen.size() - 1).setScore(0);
				}
			}
			
			for (int i = 0; i < Math.round(this.randomBehaviour * this.genPopulation); i++) {
				NeuralNet newGenome = this.currentGeneration.get(0).clone();
				double[] newWeights = newGenome.getWeights();
				
				for (int j = 0; j < newWeights.length; j++) {
					newWeights[j] = Math.random() * 2 - 1;
				}
				
				newGenome.setWeights(newWeights);
				
				if (nextGen.size() < this.genPopulation) {
					nextGen.add(newGenome);
					nextGen.get(nextGen.size() - 1).setScore(0);
				}
			}
			
			int max = 0;
			while (true) {
				for (int i = 0; i < max; i++) {
					NeuralNet[] children = breedGenomes(this.currentGeneration.get(i), this.currentGeneration.get(max));
					for (int j = 0; j < children.length; j++) {
						nextGen.add(children[j]);
						nextGen.get(nextGen.size() - 1).setScore(0);
						if (nextGen.size() >= this.genPopulation) {				
							this.currentGeneration = nextGen;
							return;
						}
					}
				}
				
				max++;
				if (max >= this.currentGeneration.size() - 1) {
					max = 0;
				}
			}
		}
	}
	
	private NeuralNet[] breedGenomes(NeuralNet g, NeuralNet h) {
		ArrayList<NeuralNet> ret = new ArrayList<NeuralNet>();
		for (int i = 0; i < this.genChildren; i++) {
			NeuralNet child = g.clone();
			
			double[] hWeights = h.getWeights();
			
			double[] childWeights = child.getWeights();
			
			for (int j = 0; j < hWeights.length; j++) {
				if (Math.random() > this.crossoverRate) {
					childWeights[j] = hWeights[j];
				}
			}
			
			for (int j = 0; j < childWeights.length; j++) {
				if (Math.random() > this.mutationRate) {
					childWeights[j] += Math.random() * this.mutationRange * 2 - this.mutationRange;
				}
			}
			
			child.setWeights(childWeights);
			
			ret.add(child);
		}
		
		NeuralNet[] retArr = new NeuralNet[ret.size()];
		for (int i = 0; i < ret.size(); i++) {
			retArr[i] = ret.get(i);
			retArr[i].setScore(0);
		}
		
		return retArr;
	}
}
