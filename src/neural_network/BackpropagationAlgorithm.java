
/**
 * Author: Austin Patel
 * Project: Handwritten Recognition
 * File Name: BackpropagationAlgorithm.java
 * Created: 01/02/17
 */

package neural_network;

/**Implements the backpropagation artificial neural network learning method.*/
public class BackpropagationAlgorithm extends LearningMethod {

	public BackpropagationAlgorithm() {
		
	}

	@Override
	public double getWeightDelta(double error, int input){
		return error * input * NeuralNetwork.LEARNING_RATE;
	}

}
