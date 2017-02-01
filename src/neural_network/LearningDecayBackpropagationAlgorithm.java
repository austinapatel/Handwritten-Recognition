
/**
 * Author: Austin Patel
 * Project: Handwritten Recognition
 * File Name: LearningDecayBackpropagationAlgorithm.java
 * Created: 01/28/17
 */

package neural_network;

import data.Alphabet;
import data.Constants;
import data.LetterData;

/**Backpropagation algorithm with additional functionality of decreasing the rate at which the program learns over time.*/
public class LearningDecayBackpropagationAlgorithm extends BackpropagationAlgorithm {

	public static final String NAME = "LearningDecay";
	private static final int DECAY_CONSTANT = 1000;
	
	private double finalRate, initialRate;
	
	public LearningDecayBackpropagationAlgorithm(double learningRate, double finalRate) {
		super(learningRate);
		
		this.initialRate = learningRate;
		this.finalRate = finalRate;
	}
	
	@Override
	public double getWeightDelta(double error, int input, int neuronId, int weightId) {
		if (learningRate > .00001)
			learningRate -= initialRate / (2600 * Constants.GRID_HEIGHT * Constants.GRID_WIDTH * Alphabet.getLength() * DECAY_CONSTANT);
		
		if (learningRate < 0)
			learningRate = 0;
//		System.out.println(learningRate);
		
		return super.getWeightDelta(error, input, neuronId, weightId);
	}

}
