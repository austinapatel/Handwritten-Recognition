
/**
 * Author: Austin Patel
 * Project: Handwritten Recognition
 * File Name: NetworkMath.java
 * Created: 01/01/17
 */

package math;

/**Performs mathematical calculations for the neural network.*/
public class NetworkMath {

	public NetworkMath() {
		
	}
	
	public static double sigmoid(double input) {
		return 1 / (1 + Math.exp(-input));
	}
	
	public static double cost(double[] experimental, int[] actual) {
		double variance2 = 0;
		
		for (int i = 0; i < experimental.length; i++)
			variance2 += Math.pow(actual[i] - experimental[i], 2);
		
		return variance2 / (experimental.length);
	}

}
