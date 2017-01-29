
/**
 * Author: Austin Patel
 * Project: Handwritten Recognition
 * File Name: WeightDecayBackpropagationAlgorithm.java
 * Created: 01/28/17
 */

package neural_network;

/**
 * Learning method for a backpropagation learning algorithm with weights that
 * decrease over time.
 */
public class WeightDecayBackpropagationAlgorithm extends BackpropagationAlgorithm{

	private double decay;
	
	public WeightDecayBackpropagationAlgorithm(double learningRate, double decay) {
		super(learningRate);
		
		this.decay = decay;
	}
	
	@Override
	public double getWeightDelta(double error, int input, int neuronId, int weightId) {
		return super.getWeightDelta(error, input, neuronId, weightId) + decay;
	}

}
