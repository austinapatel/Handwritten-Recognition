
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
import data.ExperimentalData;
import data.LetterData;
import math.NetworkMath;

/** Tests performance of the Artificial Neural Network */
public class Experimenter {

	private NeuralNetwork neuralNetwork;

	public Experimenter(NeuralNetwork neuralNetwork) {
		this.neuralNetwork = neuralNetwork;
	}
	
	public static void main(String[] args) {
		NeuralNetwork drawingNetwork = new NeuralNetwork(
				new BackpropagationAlgorithm(),
				Constants.GRID_WIDTH * Constants.GRID_HEIGHT,
				Alphabet.getLength());

		Trainer trainer = new Trainer(drawingNetwork);
		trainer.trainNetwork(false);

		Experimenter experimenter = new Experimenter(drawingNetwork);
		experimenter.testNetwork(experimenter.getExperimentalData());
	}
	
	public ArrayList<LetterData> getExperimentalData() {
		ArrayList<LetterData> experimentalData = new ArrayList<LetterData>();

		for (char c : Alphabet.getAlphabet().toCharArray()) {
			String filePath = Constants.RESOURCES_PATH
					+ Constants.EXPERIMENTAL_LETTERS_FOLDER + "\\" + c + ".txt";
			LetterData[] currentLetterData = LetterData.getLetterData(filePath,
					c);

			for (LetterData letterData : currentLetterData)
				experimentalData.add(letterData);
		}
		
		return experimentalData;
	}
	
	public ArrayList<LetterData> getExperimentalData(char letter) {
		ArrayList<LetterData> experimentalData = new ArrayList<LetterData>();

		String filePath = Constants.RESOURCES_PATH
				+ Constants.EXPERIMENTAL_LETTERS_FOLDER + "\\" + letter + ".txt";
		
		LetterData[] currentLetterData = LetterData.getLetterData(filePath, letter);

		for (LetterData letterData : currentLetterData)
			experimentalData.add(letterData);
		
		return experimentalData;
	}
	
	public ExperimentalData testNetwork(ArrayList<LetterData> experimentalData) {
		int numCorrect = 0;
		int[] letterCorrect = new int[Alphabet.getLength()];
		int numberOfEach = experimentalData.size() / Alphabet.getLength();
		double[] costs = new double[experimentalData.size()];

		for (int k = 0; k < experimentalData.size(); k++) {
			LetterData letterData = experimentalData.get(k);
			
			int[] actualValues = new int[Alphabet.getAlphabet().length()];

			for (int i = 0; i < actualValues.length; i++)
				actualValues[i] = 0;

			actualValues[Alphabet.getLocation(letterData.getCharacter())] = 1;

			int[] inputs = letterData.getData1D();

			double[] output = neuralNetwork.getOutput(inputs);

			int largestValueIndex = 0;
			for (int i = 0; i < output.length; i++)
				largestValueIndex = (output[i] > output[largestValueIndex]) ? i
						: largestValueIndex;

			char prediction = Alphabet.getCharacter(largestValueIndex);
			char actual = letterData.getCharacter();

			if (prediction == actual) {
				letterCorrect[Alphabet.indexOf(prediction)]++;
				numCorrect++;
			}

			costs[k] = NetworkMath.cost(output, actualValues);
			
			// System.out.println("Prediction: " + prediction + " Actual: " +
			// actual);
		}

//		System.out.println(numCorrect + " correct out of "
//				+ experimentalData.size() + " ("
//				+ (int) (((double) numCorrect / experimentalData.size()) * 100)
//				+ "%)");
//
//		for (int i = 0; i < letterCorrect.length; i++) {
//			System.out.println(Alphabet.getCharacter(i) + ": "
//					+  (int) ((double) letterCorrect[i] / numberOfEach * 100) + "%");
//		}
		
		double accuracy = (double) numCorrect / experimentalData.size();
		double cost = 0;
		
		for (double c : costs)
			cost += c;
		
		cost /= experimentalData.size();
		
		return new ExperimentalData(accuracy, cost);
	}
	
//	public double testLetter(char letter) {
//		ArrayList<LetterData> experimentalData = getExperimentalData(letter);
//		int numCorrect = 0;
//		
//		for (LetterData data : experimentalData) {
//			double[] output = neuralNetwork.getOutput(data.getData1D());
//			
//			int largestValueIndex = 0;
//			for (int i = 0; i < output.length; i++)
//				largestValueIndex = (output[i] > output[largestValueIndex]) ? i
//						: largestValueIndex;
//			
//			char outputLetter = Alphabet.getCharacter(largestValueIndex);
//			
//			if (outputLetter == letter)
//				numCorrect++;
//		}
//		
//		return (double) numCorrect / experimentalData.size();
//	}

}
