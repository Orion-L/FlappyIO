package neuroevolver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Neuroevolver {
	private int genPopulation, genChildren, numInput, numOutput;
	private int[] numHidden;
	private double crossoverRate, mutationRate, mutationRange, 
				   randomRate, elitismRate;
	private ArrayList<NeuralNet> currentGeneration;
	
	/**
	 * Initialise a new Neuroevolver
	 * @param generationPopulation The population size of each generation
	 * @param generationChildren The number of children produced by breeding genomes
	 * @param crossoverRate The rate at which network weights crossover during breeding
	 * @param mutationRate The rate at which mutations appear during breeding
	 * @param mutationRange The maximum amount a mutation can change a network weight by
	 * @param elitismRate The rate at which well performing genomes are preserved through each generation
	 * @param randomRate The rate at which new genomes are introduced to each generation
	 * @param numInput The number of neurons in the network's input layer
	 * @param numOutput The number of neurons in the network's output layer
	 * @param numHidden The number of neurons in each of the network's hidden layers
	 */
	public Neuroevolver(int generationPopulation, int generationChildren, 
						double crossoverRate, double mutationRate, 
						double mutationRange, double elitismRate, 
						double randomRate, int numInput, int numOutput, 
						int[] numHidden) {
		this.genPopulation = generationPopulation;
		this.genChildren = generationChildren;
		this.crossoverRate = crossoverRate;
		this.mutationRange = mutationRange;
		this.mutationRate = mutationRate;
		this.randomRate = randomRate;
		this.elitismRate = elitismRate;
		this.currentGeneration = null;
		this.numInput = numInput;
		this.numOutput = numOutput;
		this.numHidden = numHidden;
	}
	
	/**
	 * Update the score of a particular genome
	 * @param genomeNum The genome to update
	 * @param score The genome's new score
 	 */
	public void updateScore(int genomeNum, int score) {
		this.currentGeneration.get(genomeNum).setScore(score);
	}
	
	public int generationSize() {
		return this.currentGeneration.size();
	}
	
	/**
	 * Evaluate a particular genome's network
	 * @param genomeNum The genome to evaluate
	 * @param inputs The input values of the network
	 * @return The corresponding output values of the network
	 */
	public double[] evaluateGenome(int genomeNum, double[] inputs) {
		return this.currentGeneration.get(genomeNum).evaluateNetwork(inputs);
	}
	
	public void nextGeneration() {
		if (this.currentGeneration == null) {
			// Generate the first generation
			this.currentGeneration = new ArrayList<NeuralNet>();
			
			// Populate the generation with new networks
			for (int i = 0; i < this.genPopulation; i++) {
				this.currentGeneration.add(
					new NeuralNet(this.numInput, this.numOutput, this.numHidden));
			}
		} else {
			// Sort the generation by descending score
			Collections.sort((List<NeuralNet>) this.currentGeneration);
			
			// Initialise next generation
			ArrayList<NeuralNet> nextGen = new ArrayList<NeuralNet>();
			
			// Select best genomes for next generation
			// Number selected based upon elitism parameter
			for (int i = 0; i < Math.round(this.elitismRate * this.genPopulation); i++) {
				if (nextGen.size() < this.genPopulation) {
					// Clone the i'th best genome
					nextGen.add(this.currentGeneration.get(i).clone());
				} else {
					break;
				}
			}
			
			// Generate new genomes
			// Number of new genomes generated based upon random parameter
			for (int i = 0; i < Math.round(this.randomRate * this.genPopulation); i++) {
				NeuralNet newGenome = new NeuralNet(
					this.numInput, this.numOutput, this.numHidden);
				
				if (nextGen.size() < this.genPopulation) {
					nextGen.add(newGenome);
				} else {
					break;
				}
			}
			
			// Produce remaining number of genomes by breeding
			int j = 0;
			while (nextGen.size() < this.genPopulation) {
				// Continuously breed better genomes
				for (int i = 0; i < j && nextGen.size() < this.genPopulation; i++) {
					NeuralNet[] children = breedGenomes(
						this.currentGeneration.get(i), this.currentGeneration.get(j));
					
					// Add children to the next generation
					for (int k = 0; k < children.length; k++) {
						if (nextGen.size() < this.genPopulation) {
							nextGen.add(children[k]);
						} else {
							break;
						}
					}
				}
				
				j = (j + 1) % (this.currentGeneration.size() - 1);
			}		

			// Set scores of each genome in next generation to 0
			for (int i = 0; i < nextGen.size(); i++) {
				nextGen.get(i).setScore(0);
			}
		
			// Set current generation to next generation
			this.currentGeneration = nextGen;
		}
	}
	
	private NeuralNet[] breedGenomes(NeuralNet g, NeuralNet h) {
		ArrayList<NeuralNet> ret = new ArrayList<NeuralNet>();
		
		// Generate the desired number of children
		for (int i = 0; i < this.genChildren; i++) {
			// Clone one of the genomes
			NeuralNet child = g.clone();
			
			ArrayList<Double> hWeights = h.getWeights();			
			ArrayList<Double> childWeights = child.getWeights();

			// Randomly add some of the other genome's weights to the child
			// and randomly mutate some of the weights
			for (int j = 0; j < childWeights.size(); j++) {
				if (Math.random() > this.crossoverRate) {
					childWeights.set(j, hWeights.get(j));
				}
				
				if (Math.random() > this.mutationRate) {
					double newVal = childWeights.get(j) 
						+ (Math.random() * this.mutationRange * 2) 
						- this.mutationRange;
					
					childWeights.set(j, newVal);
				}
			}
			
			// Set the child's new weights
			child.setWeights(childWeights);
			ret.add(child);
		}
		
		// Set up the return array
		NeuralNet[] retArr = new NeuralNet[ret.size()];
		for (int i = 0; i < ret.size(); i++) {
			retArr[i] = ret.get(i);
			retArr[i].setScore(0);
		}
		
		return retArr;
	}
}
