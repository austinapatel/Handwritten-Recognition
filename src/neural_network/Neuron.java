
/**
 * Author: Austin Patel
 * Project: Handwritten Recognition
 * File Name: Neuron.java
 * Created: 01/01/17
 */

package neural_network;

import math.NetworkMath;

/**Neuron structure for Artificial Neural Network.*/
public class Neuron {

	private int inputCount;
	private double[] weights;
	private LearningMethod learningMethod;
	private int id;
	
	public Neuron(LearningMethod learningMethod, int inputCount, int id) {
		this.learningMethod = learningMethod;
		this.inputCount = inputCount;
		this.id = id;
		
		randomizeWeights();
	}
	
	public double getOutput(int[] inputs) {
		double sum = 0d;
		
		for (int i = 0; i < inputCount; i++)
			sum += weights[i] * inputs[i];
				
		return NetworkMath.sigmoid(sum);
	}
	
	private void randomizeWeights() {
		weights = new double[inputCount];
		
		for (int i = 0; i < weights.length; i++)
			weights[i] = Math.random() / 30;
	}
	
	public double[] getWeights() {
		return weights;
	}
	
	public void setWeights(double[] weights) {
		this.weights = weights;
	}
	
	public void learn(double error, int[] inputs) {
		for (int i = 0; i < weights.length; i++)
			weights[i] += learningMethod.getWeightDelta(error, inputs[i], id, i);
	}

}
