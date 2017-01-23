
/**
 * Author: Austin Patel
 * Project: Handwritten Recognition
 * File Name: LearningMethod.java
 * Created: 01/01/17
 */

package neural_network;

/**Structure for an artificial neural network learning method.*/
public abstract class LearningMethod {
		
	public abstract double getWeightDelta(double error, int input);
	
	public abstract String getName();
	
	public abstract void onLearningCycleComplete();

}
