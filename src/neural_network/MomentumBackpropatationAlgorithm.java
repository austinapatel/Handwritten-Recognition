
/**
 * Author: Austin Patel
 * Project: Handwritten Recognition
 * File Name: MomentumBackpropatationAlgorithm.java
 * Created: 01/28/17
 */

package neural_network;

/**
 * Adds momentum functionality to the backpropagation learning algorithm to
 * reduce fluctuations in weight changes by remebering the last changes to the
 * newtwork.
 */
public class MomentumBackpropatationAlgorithm extends BackpropagationAlgorithm {

	public static final String NAME = "Momentum";
	
	private double[][] momentum;
	private int[][] count;
	
	public MomentumBackpropatationAlgorithm(double learningRate, int neuronCount, int weightCount) {
		super(learningRate);
		
		momentum = new double[neuronCount][weightCount];
		count = new int[neuronCount][weightCount];
	}
	
	@Override
	public double getWeightDelta(double error, int input, int neuronId, int weightId) {
		double delta = super.getWeightDelta(error, input, neuronId, weightId);
		
		momentum[neuronId][weightId] += delta;
		count[neuronId][weightId]++;
		
		return momentum[neuronId][weightId] / count[neuronId][weightId];
	}

}
