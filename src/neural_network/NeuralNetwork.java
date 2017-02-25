
/**
 * Author: Austin Patel
 * Project: Handwritten Recognition
 * File Name: NeuralNetwork.java
 * Created: 01/01/17
 */

package neural_network;

/**
 * Artificial Neural Network structure with learning and prediction
 * capabilities.
 */
public class NeuralNetwork {

	private Neuron[] neurons;
	private LearningMethod learningMethod;

	public NeuralNetwork(LearningMethod learningMethod, int inputCount,
			int outputCount) {
		neurons = new Neuron[outputCount];

		this.learningMethod = learningMethod;

		for (int i = 0; i < neurons.length; i++)
			neurons[i] = new Neuron(learningMethod, inputCount, i);
	}

	public double[] getOutput(int[] inputs) {
		double[] outputs = new double[neurons.length];

		for (int i = 0; i < neurons.length; i++)
			outputs[i] = neurons[i].getOutput(inputs);

		return outputs;
	}

	public void learn(int[] inputs, int[] actualResults) {
		double[] experimentalResults = getOutput(inputs);

		for (int i = 0; i < neurons.length; i++)
			neurons[i].learn(actualResults[i] - experimentalResults[i], inputs);
	}

	public Neuron[] getNeurons() {
		return neurons;
	}

	public LearningMethod getLearningMethod() {
		return learningMethod;
	}

	/**
	 * Allows for changing the learning method of a neuron netork after it has
	 * already been used or received training from another algorithm. This
	 * allows for semi-superivsed / semi-unsupervised learning algorithms.
	 */
	public void setLearningMethod(LearningMethod learningMethod) {
		this.learningMethod = learningMethod;
		
		for (Neuron neuron : neurons)
			neuron.setLearningMethod(learningMethod);
	}

}
