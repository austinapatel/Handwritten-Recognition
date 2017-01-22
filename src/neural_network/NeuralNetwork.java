
/**
 * Author: Austin Patel
 * Project: Handwritten Recognition
 * File Name: Network.java
 * Created: 01/01/17
 */

package neural_network;

/**
 * Artificial Neural Network structure with learning and prediction
 * capabilities.
 */
public class NeuralNetwork {
	
	public static double LEARNING_RATE = 0.1d;
	
	private Neuron[] neurons;
	private LearningMethod learningMethod;
	
	public NeuralNetwork(LearningMethod learningMethod, int inputCount, int outputCount) {		
		neurons = new Neuron[outputCount];
		
		this.learningMethod = learningMethod;
		
		for (int i = 0; i < neurons.length; i++)
			neurons[i] = new Neuron(learningMethod, inputCount);
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
	
}
