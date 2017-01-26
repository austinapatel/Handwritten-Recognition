
/**
 * Author: Austin Patel
 * Project: Handwritten Recognition
 * File Name: BackpropagationAlgorithm.java
 * Created: 01/02/17
 */

package neural_network;

/**Implements the backpropagation artificial neural network learning method.*/
public class BackpropagationAlgorithm extends LearningMethod {

	public static final String NAME = "Backpropagation";
	private double learningRate;
	
	public BackpropagationAlgorithm(double learningRate) {
		this.learningRate = learningRate;
	}

	@Override
	public double getWeightDelta(double error, int input) {
		return error * input * learningRate;
	}
	
	public void setLearningRate(double learningRate) {
		this.learningRate = learningRate;
	}

	@Override
	public String getName() {
		return NAME;
	}
		
	public double getLearningRate() {
		return learningRate;
	}

	@Override
	public void onLearningCycleComplete() {
		
	}

	@Override
	public String getFileName() {
		return "BP-LR" + learningRate;
	}

}
