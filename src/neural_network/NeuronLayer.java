
/**
 * Author: Austin Patel
 * Project: Handwritten Recognition
 * File Name: HiddenLayer.java
 * Created: 01/31/17
 */

package neural_network;

/** Represents a neuron layer in the neural network. */
public class NeuronLayer {

	private Neuron[] neurons;
	private LearningMethod learningMethod;
	private int inputCount;
	
	public NeuronLayer(int neuronCount, int inputCount) {
		this.inputCount = inputCount;
		
		neurons = new Neuron[neuronCount];
	}
	
	public void setLearningMethod(LearningMethod learningMethod) {
		this.learningMethod = learningMethod;
		
		for (int i = 0; i < neurons.length; i++)
			neurons[i] = new Neuron(learningMethod, inputCount, i);
	}
	
	public int getOutputCount() {
		return neurons.length;
	}
	
	public Neuron[] getNeurons() {
		return neurons;
	}
	
	public int getInputCount() {
		return inputCount;
	}

}
