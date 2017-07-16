package neuroevolver;

import java.util.ArrayList;

class Generation {
	private static final double crossover = 0.5;
	private ArrayList<Genome> genomes;
	
	public Generation() {
		this.genomes = new ArrayList<Genome>();
	}
	
	public void addGenome(Genome g) {
		if (this.genomes.size() > 0) {
			int i = 0;
			while (i < this.genomes.size() && this.genomes.get(i).getScore() < g.getScore()) {
				i++;
			}
			this.genomes.add(i, g);
		} else {
			this.genomes.add(g);
		}
	}
	
	public Genome[] breedGenomes(Genome g, Genome h, int numChildren) {
		ArrayList<Genome> ret = new ArrayList<Genome>();
		for (int i = 0; i < numChildren; i++) {
			Genome child = g.clone();
			
			// crossover
			
			// mutation
			
			ret.add(child);
		}
		
		return (Genome[]) ret.toArray();
	}
}
