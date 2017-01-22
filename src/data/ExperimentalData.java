
/**
 * Author: Austin Patel
 * Project: Handwritten Recognition
 * File Name: ExperimentalData.java
 * Created: 01/21/17
 */

package data;

/**Encapsulates data for the data recorded for running a test on a neural network.*/
public class ExperimentalData {

	private double accuracy, cost;
	
	public ExperimentalData(double accuracy, double cost) {
		this.accuracy = accuracy;
		this.cost = cost;
	}

	public double getAccuracy() {
		return accuracy;
	}

	public double getCost() {
		return cost;
	}
	
}
