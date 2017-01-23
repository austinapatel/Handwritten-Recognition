
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

	private final String NAME = "GeneticAlgorithm";

	private GeneticAlgorithm geneticAlgorithm;

	public GeneticAlgorithmLearningMethod(GeneticAlgorithm geneticAlgorithm) {
		this.geneticAlgorithm = geneticAlgorithm;
	}

	@Override
	public double getWeightDelta(double error, int input) {
		return 0;
	}	

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void onLearningCycleComplete() {
		geneticAlgorithm.nextGeneration();		
	}

}
