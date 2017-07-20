package neuroevolver;

import java.util.LinkedList;

public class Neuroevolver {
	private int genPopulation, genChildren, numInput, numOutput;
	private int[] numHidden;
	private double crossoverRate, mutationRate, mutationRange, randomBehaviour, elitism;
	private Generation currentGeneration;
	
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
		this.currentGeneration.getGenome(genomeNum).setScore(score);
	}
	
	public int generationSize() {
		return this.currentGeneration.genomeLength();
	}
	
	public double[] evaluateGenome(int genomeNum, double[] inputs) {
		if (genomeNum == 0) {
			System.out.println(inputs[0] + " " + inputs[1] + " " + this.currentGeneration.getGenome(genomeNum).evaluate(inputs)[0]);
		}
		return this.currentGeneration.getGenome(genomeNum).evaluate(inputs);
	}
	
	public void nextGeneration() {
		if (this.currentGeneration == null) {
			this.currentGeneration = new Generation(this.genChildren, this.crossoverRate, 
													this.mutationRate, this.mutationRange);
			
			for (int i = 0; i < this.genPopulation; i++) {
				this.currentGeneration.addGenome(new Genome(new NeuralNet(this.numInput, this.numOutput, this.numHidden), 0));
			}
		} else {
			this.currentGeneration.sortGenomes();
			LinkedList<Genome> nextGenomes = new LinkedList<Genome>();
			for (int i = 0; i < Math.round(this.elitism * this.genPopulation); i++) {
				if (nextGenomes.size() < this.genPopulation) {
					nextGenomes.add(this.currentGeneration.getGenome(i).clone());
				}
			}
			
			for (int i = 0; i < Math.round(this.randomBehaviour * this.genPopulation); i++) {
				Genome newGenome = this.currentGeneration.getGenome(0).clone();
				double[] newWeights = newGenome.getNetwork().getWeights();
				
				for (int j = 0; j < newWeights.length; j++) {
					newWeights[j] = Math.random() * 2 - 1;
				}
				
				newGenome.getNetwork().setWeights(newWeights);
				
				if (nextGenomes.size() < this.genPopulation) {
					nextGenomes.add(newGenome);
				}
			}
			
			int max = 0;
			while (true) {
				for (int i = 0; i < max; i++) {
					Genome[] children = this.currentGeneration.breedGenomes(this.currentGeneration.getGenome(i), this.currentGeneration.getGenome(max));
					for (int j = 0; j < children.length; j++) {
						nextGenomes.add(children[j]);
						if (nextGenomes.size() >= this.genPopulation) {
							Generation newGen = new Generation(this.genChildren, this.crossoverRate, 
									this.mutationRate, this.mutationRange);
							for (int k = 0; k < nextGenomes.size(); k++) {
								newGen.addGenome(nextGenomes.get(k));
							}
							
							this.currentGeneration = newGen;
							return;
						}
					}
				}
				
				max++;
				if (max >= this.currentGeneration.genomeLength() - 1) {
					max = 0;
				}
			}
		}
	}
}
