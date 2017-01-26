
/**
 * Author: Austin Patel
 * Project: Handwritten Recognition
 * File Name: GeneticAlgorithmLearningMethod.java
 * Created: 01/21/17
 */

package neural_network;

import neural_network.genetics.GeneticAlgorithm;

/**
 * Learning method for the genetic algorithm. This algorithm does not modify
 * weights through the "getWeightDelta" method, but rather through the
 * "GeneticAlgorithm class". It runs independently in the "GeneticAlgorithm"
 * class.
 */
public class GeneticAlgorithmLearningMethod extends LearningMethod {

	public static final String NAME = "GeneticAlgorithm";

	private GeneticAlgorithm geneticAlgorithm;

	public GeneticAlgorithmLearningMethod(GeneticAlgorithm geneticAlgorithm) {
		this.geneticAlgorithm = geneticAlgorithm;
	}

	@Override
	public double getWeightDelta(double error, int input) {
		return 0;
	}
	
	public GeneticAlgorithm getGeneticAlgorithm() {
		return geneticAlgorithm;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void onLearningCycleComplete() {
		geneticAlgorithm.nextGeneration();		
	}

	@Override
	public String getFileName() {
		return "GA-" + "Chromosomes"
					 + geneticAlgorithm.getChromsosomeCount() + "Generations"
					 + geneticAlgorithm.getGenerationCount() + "Breed"
					 + geneticAlgorithm.getBreedRate() + "Death"
					 + geneticAlgorithm.getDeathRate();
	}

}
