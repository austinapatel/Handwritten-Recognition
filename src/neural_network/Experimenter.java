
/**
 * Author: Austin Patel
 * Project: Handwritten Recognition
 * File Name: Experimenter.java
 * Created: 01/02/17
 */

package neural_network;

import java.util.ArrayList;

import data.Alphabet;
import data.Constants;
import data.LetterData;

/**Tests performance of the Artificial Neural Network*/
public class Experimenter {
	
	private NeuralNetwork neuralNetwork;
	
	public Experimenter(NeuralNetwork neuralNetwork) {
		this.neuralNetwork = neuralNetwork;
	}
	
	public void testNetwork() {
		ArrayList<LetterData> experimentalData = new ArrayList<LetterData>();
		
		for (char c : Alphabet.getAlphabet().toCharArray()) {
			String filePath = Constants.RESOURCES_PATH + Constants.EXPERIMENTAL_LETTERS_FOLDER + "\\" + c + ".txt";
			LetterData[] currentLetterData = LetterData.getLetterData(filePath, c);
						
			for (LetterData letterData : currentLetterData)
				experimentalData.add(letterData);
		}
		
		int numCorrect = 0;
		int[] letterCorrect = new int[Alphabet.getLength()];
		int numberOfEach = experimentalData.size() / Alphabet.getLength();
				
		for (LetterData letterData : experimentalData) {
			int[] actualValues = new int[Alphabet.getAlphabet().length()];
			
			for (int i = 0; i < actualValues.length; i++)
				actualValues[i] = 0;
			
			actualValues[Alphabet.getLocation(letterData.getCharacter())] = 1;
			
			int[] inputs = letterData.getData1D();
			
			double[] output = neuralNetwork.getOutput(inputs);
			
			int largestValueIndex = 0;
			for (int i = 0; i < output.length; i++)
				largestValueIndex = (output[i] > output[largestValueIndex]) ? i : largestValueIndex;
			
			char prediction = Alphabet.getCharacter(largestValueIndex);
			char actual = letterData.getCharacter();
			
			if (prediction == actual) {
				letterCorrect[Alphabet.indexOf(prediction)]++;
				numCorrect++;
			}
			
//			System.out.println("Prediction: " + prediction + " Actual: " + actual);
		}
		
		System.out.println(numCorrect + " correct out of " + experimentalData.size() + " (" + (int) (((double) numCorrect / experimentalData.size()) * 100) + "%)");
		
		for (int i = 0; i < letterCorrect.length; i++) {
			System.out.println(Alphabet.getCharacter(i) + ": " + ((double) letterCorrect[i] / numberOfEach * 100) + "%");
		}
	}
	
	public static void main(String[] args) {
		NeuralNetwork drawingNetwork = new NeuralNetwork(new BackpropagationAlgorithm(), 400,
				26);
		
		Trainer trainer = new Trainer(drawingNetwork);
		trainer.trainNetwork();
		
		Experimenter experimenter = new Experimenter(drawingNetwork);
		experimenter.testNetwork();
	}

}
