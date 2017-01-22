
/**
 * Author: Austin Patel
 * Project: Handwritten Recognition
 * File Name: Trainer.java
 * Created: 01/01/17
 */

package neural_network;

import java.io.File;
import java.util.ArrayList;

import data.Alphabet;
import data.Constants;
import data.ExperimentalData;
import data.FileManager;
import data.LetterData;
import neural_network.genetics.GeneticAlgorithm;

/** Uses training "LetterData" as inputs to teach the neural network. */
public class Trainer {

	public static void main(String[] args) {
		NeuralNetwork neuralNetwork = new NeuralNetwork(
				new BackpropagationAlgorithm(),
				Constants.GRID_WIDTH * Constants.GRID_HEIGHT,
				Alphabet.getLength());

		// NeuralNetwork neuralNetwork = new NeuralNetwork(
		// new GeneticAlgorithmLearningMethod(),
		// Constants.GRID_WIDTH * Constants.GRID_HEIGHT,
		// Alphabet.getLength());
		//
		// GeneticAlgorithm geneticAlgorithm = new
		// GeneticAlgorithm(neuralNetwork);

		Trainer trainer = new Trainer(neuralNetwork);
		trainer.trainNetwork(true);
	}

	private NeuralNetwork neuralNetwork;
	private Experimenter experimenter;
	private ArrayList<LetterData> experimentalLetterData;
	private String accuracyPath, costPath;

	public Trainer(NeuralNetwork neuralNetwork) {
		this.neuralNetwork = neuralNetwork;

		experimenter = new Experimenter(neuralNetwork);

		for (char letter : Alphabet.getCharacterArray()) {
			String path = "/" + Constants.OUTPUT_DATA_FOLDER + "/" + letter
					+ ".txt";
			File file = new File(path);
			if (file.exists())
				file.delete();

			String learningMethodName = neuralNetwork.getLearningMethod()
					.getName();

			accuracyPath = "/" + Constants.OUTPUT_DATA_FOLDER + "/"
					+ learningMethodName + "Accuracy" + ".txt";
			file = new File(accuracyPath);
			if (file.exists())
				file.delete();

			costPath = "/" + Constants.OUTPUT_DATA_FOLDER + "/"
					+ learningMethodName + "Cost" + ".txt";
			file = new File(costPath);
			if (file.exists())
				file.delete();
		}

		experimentalLetterData = experimenter.getExperimentalData();
	}

	public void trainNetwork(boolean debug) {
		String accuracyFileContent = "", costFileContent = "";
		ArrayList<LetterData> trainingData = new ArrayList<LetterData>();

		for (char c : Alphabet.getAlphabet().toCharArray()) {
			String filePath = Constants.RESOURCES_PATH
					+ Constants.TRAINING_LETTERS_FOLDER + "\\" + c + ".txt";
			LetterData[] currentLetterData = LetterData.getLetterData(filePath,
					c);

			for (LetterData letterData : currentLetterData)
				trainingData.add(letterData);
		}

		ArrayList<LetterData> shuffledData = new ArrayList<LetterData>();

		// Shuffle the data so that the trainer doesn't learn one character at a
		// time and then forget it
		// Need to make the training data in a constant order while actually
		// experimenting
		while (!trainingData.isEmpty())
			shuffledData.add(trainingData
					.remove((int) (Math.random() * trainingData.size())));

		for (LetterData letterData : shuffledData) {
			int[] actualValues = new int[Alphabet.getAlphabet().length()];

			for (int i = 0; i < actualValues.length; i++)
				actualValues[i] = 0;

			actualValues[Alphabet.getLocation(letterData.getCharacter())] = 1;

			neuralNetwork.learn(letterData.getData1D(), actualValues);

			char letter = letterData.getCharacter();

			if (debug) {
				// String letterPath = "/" + Constants.OUTPUT_DATA_FOLDER + "/"
				// + letterData.getCharacter() + ".txt";
				//
				// FileManager.writeFileContent(letterPath,
				// String.valueOf(experimenter.testLetter(letter)), true);

				ExperimentalData experimentalData = experimenter
						.testNetwork(experimentalLetterData);

				accuracyFileContent += String
						.valueOf(experimentalData.getAccuracy()) + "\n";

				costFileContent += String.valueOf(experimentalData.getCost())
						+ "\n";
			}
		}

		if (debug) {
			FileManager.writeFileContent(accuracyPath, accuracyFileContent,
					true);

			FileManager.writeFileContent(costPath, costFileContent, true);
		}
	}

}
