
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
import neural_network.genetics.GeneticAlgorithm;

/** Tests performance of the Artificial Neural Network */
public class Experimenter {

	ArrayList<ArrayList<LetterData>> individualLetterData;
	private NeuralNetwork neuralNetwork;
	private ArrayList<LetterData> experimentalData;

	public Experimenter(NeuralNetwork neuralNetwork) {
		this.neuralNetwork = neuralNetwork;

		// Load letter data
		individualLetterData = new ArrayList<ArrayList<LetterData>>();
		for (int i = 0; i < Alphabet.getLength(); i++)
			individualLetterData
					.add(getExperimentalData(Alphabet.getCharacter(i)));

		experimentalData = Experimenter.getExperimentalData();
	}

	public static void main(String[] args) {
		NeuralNetwork neuralNetwork = new NeuralNetwork(
				 new GeneticAlgorithmLearningMethod(
				 new GeneticAlgorithm(100, 100, 0.9, 0)),
				// new WeightDecayBackpropagationAlgorithm(0.1, 0),
//				new BackpropagationAlgorithm(0.1),
				// new MomentumBackpropatationAlgorithm(0.1),
				// new LearningDecayBackpropagationAlgorithm(0.1, 1.00000001),
				Constants.GRID_WIDTH * Constants.GRID_HEIGHT,
				Alphabet.getLength());

		 GeneticAlgorithmLearningMethod learningMethod =
		 ((GeneticAlgorithmLearningMethod) neuralNetwork
		 .getLearningMethod());
		 learningMethod.getGeneticAlgorithm().setNeuralNetwork(neuralNetwork);

		Trainer trainer = new Trainer(neuralNetwork);
		trainer.trainNetwork();
		// trainer.trainNetwork();
		// trainer.trainNetwork();
		// trainer.trainNetwork();

		Experimenter experimenter = new Experimenter(neuralNetwork);
		ExperimentalData experimentalData = experimenter.testNetwork();

		System.out.println("Accuracy: "
				+ (int) (experimentalData.getAccuracy() * 100) + '%');

		// learningMethod.getGeneticAlgorithm().commitWeights();
		// experimentalData =
		// experimenter.testNetwork(Experimenter.getExperimentalData());
		//
		// System.out.println("Accuracy: " + (int)
		// (experimentalData.getAccuracy() * 100) + '%');

		
		// Now learn with the backpropagation algorithm
		
		neuralNetwork.setLearningMethod(new BackpropagationAlgorithm(0.1));
		
		
		// Now learn with the genetic algorithm
//		neuralNetwork.setLearningMethod(new GeneticAlgorithmLearningMethod(
//				new GeneticAlgorithm(100, 100, 0.9, 0.0)));
//
//		GeneticAlgorithm geneticAlgorithm = ((GeneticAlgorithmLearningMethod) (neuralNetwork
//				.getLearningMethod())).getGeneticAlgorithm();
//		
//		geneticAlgorithm.setNeuralNetwork(neuralNetwork);
//
		trainer.trainNetwork();
		trainer.trainNetwork();
		trainer.trainNetwork();
		trainer.trainNetwork();
		trainer.trainNetwork();
		trainer.trainNetwork();
		

		experimentalData = experimenter.testNetwork();

		System.out.println("Accuracy: "
				+ (int) (experimentalData.getAccuracy() * 100) + '%');
	}

	public static ArrayList<LetterData> getExperimentalData() {
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

	private ArrayList<LetterData> getExperimentalData(char letter) {
		ArrayList<LetterData> experimentalData = new ArrayList<LetterData>();

		String filePath = Constants.RESOURCES_PATH
				+ Constants.EXPERIMENTAL_LETTERS_FOLDER + "\\" + letter
				+ ".txt";

		LetterData[] currentLetterData = LetterData.getLetterData(filePath,
				letter);

		for (LetterData letterData : currentLetterData)
			experimentalData.add(letterData);

		return experimentalData;
	}

	/**
	 * Tests the network with given experimental data and returns accuracy and
	 * cost.
	 */
	public ExperimentalData testNetwork() {
		int numCorrect = 0;
		int[] letterCorrect = new int[Alphabet.getLength()];
		double[] costs = new double[experimentalData.size()];
		int numberOfEach = experimentalData.size() / Alphabet.getLength();

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

		// System.out.println(numCorrect + " correct out of "
		// + experimentalData.size() + " ("
		// + (int) (((double) numCorrect / experimentalData.size()) * 100)
		// + "%)");
		//
		// for (int i = 0; i < letterCorrect.length; i++) {
		// System.out.println(Alphabet.getCharacter(i) + ": "
		// + (int) ((double) letterCorrect[i] / numberOfEach * 100)
		// + "%");
		// }

		double accuracy = (double) numCorrect / experimentalData.size();
		double cost = 0;

		for (double c : costs)
			cost += c;

		cost /= experimentalData.size();

		return new ExperimentalData(accuracy, cost);
	}

	public double testLetter(char letter) {
		ArrayList<LetterData> experimentalData = individualLetterData
				.get(Alphabet.getLocation(letter));

		int numCorrect = 0;
		int count = 0;

		for (LetterData data : experimentalData) {
			count++;

			double[] output = neuralNetwork.getOutput(data.getData1D());

			int largestValueIndex = 0;
			for (int i = 0; i < output.length; i++)
				largestValueIndex = (output[i] > output[largestValueIndex]) ? i
						: largestValueIndex;

			char outputLetter = Alphabet.getCharacter(largestValueIndex);

			if (outputLetter == letter)
				numCorrect++;
		}

		return (double) numCorrect / count;
	}

}
