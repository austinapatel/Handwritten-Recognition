
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
	
	public static double sigma(double input) {
		return 1 / (1 + Math.exp(-input));
	}

}
