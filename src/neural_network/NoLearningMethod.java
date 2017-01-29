
/**
 * Author: Austin Patel
 * Project: Handwritten Recognition
 * File Name: NoLearningMethod.java
 * Created: 01/08/17
 */

package neural_network;

/**Represents the control learning method; no change in weights.*/
public class NoLearningMethod extends LearningMethod {

	private final String NAME = "Control";
	
	public NoLearningMethod() {
		
	}

	@Override
	public double getWeightDelta(double error, int input, int neuronId, int weightId) {
		return 0;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void onLearningCycleStart() {
		
	}

	@Override
	public String getFileName() {
		return "NLR";
	}
	
}
