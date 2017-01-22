
/**
 * Author: Austin Patel
 * Project: Handwritten Recognition
 * File Name: GeneticAlgorithmLearningMethod.java
 * Created: 01/21/17
 */

package neural_network;

/**
 * Learning method for the genetic algorithm. This algorithm does not use the
 * functions though and runs independently in the "GeneticAlgorithm" class.
 */
public class GeneticAlgorithmLearningMethod extends LearningMethod{

	private final String NAME = "GeneticAlgorithm";

	public GeneticAlgorithmLearningMethod() {
		
	}

	@Override
	public double getWeightDelta(double error, int input) {
		return 0;
	}

	@Override
	public String getName() {
		return NAME;
	}

}
