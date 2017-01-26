
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

	private static int TRIALS = 3;
	private static int DEFAULT_GENERATIONS = 100, DEFAULT_CHROMOSOMES = 100;
	private static double DEFAULT_BREED_RATE = 0.9, DEFAULT_DEATH_RATE = 0.00;

	public static void main(String[] args) {
		LearningMethod[] learningMethods = new LearningMethod[] {
				new BackpropagationAlgorithm(0.1),
				new BackpropagationAlgorithm(0.2),
				new BackpropagationAlgorithm(0.3),
				new BackpropagationAlgorithm(0.4),
				new BackpropagationAlgorithm(0.5),
				new BackpropagationAlgorithm(0.6),
				new BackpropagationAlgorithm(0.6),
				new BackpropagationAlgorithm(0.7),
				new BackpropagationAlgorithm(0.8),
				new BackpropagationAlgorithm(0.9),
				new BackpropagationAlgorithm(1),

				new GeneticAlgorithmLearningMethod(
						new GeneticAlgorithm(25, DEFAULT_GENERATIONS,
								DEFAULT_BREED_RATE, DEFAULT_DEATH_RATE)),
				new GeneticAlgorithmLearningMethod(
						new GeneticAlgorithm(50, DEFAULT_GENERATIONS,
								DEFAULT_BREED_RATE, DEFAULT_DEATH_RATE)),
				new GeneticAlgorithmLearningMethod(
						new GeneticAlgorithm(75, DEFAULT_GENERATIONS,
								DEFAULT_BREED_RATE, DEFAULT_DEATH_RATE)),
				new GeneticAlgorithmLearningMethod(
						new GeneticAlgorithm(100, DEFAULT_GENERATIONS,
								DEFAULT_BREED_RATE, DEFAULT_DEATH_RATE)),

				new GeneticAlgorithmLearningMethod(
						new GeneticAlgorithm(DEFAULT_CHROMOSOMES, 25,
								DEFAULT_BREED_RATE, DEFAULT_DEATH_RATE)),
				new GeneticAlgorithmLearningMethod(
						new GeneticAlgorithm(DEFAULT_CHROMOSOMES, 50,
								DEFAULT_BREED_RATE, DEFAULT_DEATH_RATE)),
				new GeneticAlgorithmLearningMethod(
						new GeneticAlgorithm(DEFAULT_CHROMOSOMES, 75,
								DEFAULT_BREED_RATE, DEFAULT_DEATH_RATE)),
				new GeneticAlgorithmLearningMethod(
						new GeneticAlgorithm(DEFAULT_CHROMOSOMES, 100,
								DEFAULT_BREED_RATE, DEFAULT_DEATH_RATE)),

				new GeneticAlgorithmLearningMethod(
						new GeneticAlgorithm(DEFAULT_CHROMOSOMES,
								DEFAULT_GENERATIONS, 0.25, DEFAULT_DEATH_RATE)),
				new GeneticAlgorithmLearningMethod(
						new GeneticAlgorithm(DEFAULT_CHROMOSOMES,
								DEFAULT_GENERATIONS, 0.50, DEFAULT_DEATH_RATE)),
				new GeneticAlgorithmLearningMethod(
						new GeneticAlgorithm(DEFAULT_CHROMOSOMES,
								DEFAULT_GENERATIONS, 0.75, DEFAULT_DEATH_RATE)),
				new GeneticAlgorithmLearningMethod(
						new GeneticAlgorithm(DEFAULT_CHROMOSOMES,
								DEFAULT_GENERATIONS, 1, DEFAULT_DEATH_RATE)),

				new GeneticAlgorithmLearningMethod(
						new GeneticAlgorithm(DEFAULT_CHROMOSOMES,
								DEFAULT_GENERATIONS, DEFAULT_BREED_RATE, 0.01)),
				new GeneticAlgorithmLearningMethod(
						new GeneticAlgorithm(DEFAULT_CHROMOSOMES,
								DEFAULT_GENERATIONS, DEFAULT_BREED_RATE, 0.02)),
				new GeneticAlgorithmLearningMethod(
						new GeneticAlgorithm(DEFAULT_CHROMOSOMES,
								DEFAULT_GENERATIONS, DEFAULT_BREED_RATE, 0.03)),
				new GeneticAlgorithmLearningMethod(
						new GeneticAlgorithm(DEFAULT_CHROMOSOMES,
								DEFAULT_GENERATIONS, DEFAULT_BREED_RATE, 0.04)),
				new GeneticAlgorithmLearningMethod(new GeneticAlgorithm(
						DEFAULT_CHROMOSOMES, DEFAULT_GENERATIONS,
						DEFAULT_BREED_RATE, 0.05)) };

		for (int trial = 1; trial <= TRIALS; trial++)
			for (LearningMethod learningMethod : learningMethods) {
				NeuralNetwork neuralNetwork = new NeuralNetwork(learningMethod,
						Constants.GRID_WIDTH * Constants.GRID_HEIGHT,
						Alphabet.getLength());	
				
				Trainer trainer = new Trainer(neuralNetwork, true, learningMethod.getFileName() + "T" + trial);
				
				trainer.trainNetwork();
			}
	}

	private NeuralNetwork neuralNetwork;
	private Experimenter experimenter;
	private ArrayList<LetterData> experimentalLetterData;
	private String accuracyPath, costPath;
	private String fileName;
	private boolean debug;

	public Trainer(NeuralNetwork neuralNetwork, boolean debug,
			String fileName) {
		this.neuralNetwork = neuralNetwork;
		this.debug = debug;
		this.fileName = fileName;

		experimenter = new Experimenter(neuralNetwork);

		if (debug) {
			for (char letter : Alphabet.getCharacterArray()) {
				String path = "/" + Constants.OUTPUT_DATA_FOLDER + "/" + letter
						+ ".txt";
				File file = new File(path);
				if (file.exists())
					file.delete();

				accuracyPath = "/" + Constants.OUTPUT_DATA_FOLDER + "/"
						+ fileName + "Accuracy" + ".txt";
				file = new File(accuracyPath);
				if (file.exists())
					file.delete();

				costPath = "/" + Constants.OUTPUT_DATA_FOLDER + "/" + fileName
						+ "Cost" + ".txt";
				file = new File(costPath);
				if (file.exists())
					file.delete();
			}

		}

		experimentalLetterData = experimenter.getExperimentalData();
	}

	// Assumes debug false
	public Trainer(NeuralNetwork neuralNetwork) {
		this(neuralNetwork, false, "");
	}

	public void trainNetwork() {
		System.out.println(fileName);

		boolean isGenetic = false;
		if (neuralNetwork.getLearningMethod().getName()
				.equals(GeneticAlgorithmLearningMethod.NAME))
			isGenetic = true;

		String accuracyFileContent = "", costFileContent = "";
		ArrayList<LetterData> trainingData = new ArrayList<LetterData>();
		LearningMethod learningMethod = neuralNetwork.getLearningMethod();

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

		if (isGenetic) {
			GeneticAlgorithm geneticAlgorithm = ((GeneticAlgorithmLearningMethod) neuralNetwork
					.getLearningMethod()).getGeneticAlgorithm();

			shuffledData = new ArrayList<LetterData>();
			for (int i = 0; i < geneticAlgorithm.getGenerationCount(); i++)
				shuffledData.add(null);
		}

		int count = 0;
		int lastPercent = -1;
		for (LetterData letterData : shuffledData) {
			// if (count % 10 == 0)
			// System.out.println(
			// ((double) count / shuffledData.size()) * 100 + "%");

			int percent = (int) ((double) count / shuffledData.size() * 100);

			if (percent % 10 == 0 && percent != lastPercent) {
				System.out.println(percent + "%");
				lastPercent = percent;
			}

			count++;

			if (!isGenetic) {
				int[] actualValues = new int[Alphabet.getAlphabet().length()];

				for (int i = 0; i < actualValues.length; i++)
					actualValues[i] = 0;

				actualValues[Alphabet
						.getLocation(letterData.getCharacter())] = 1;

				neuralNetwork.learn(letterData.getData1D(), actualValues);
			}

			if (debug) {
				ExperimentalData experimentalData = experimenter
						.testNetwork(experimentalLetterData);

				accuracyFileContent += String
						.valueOf(experimentalData.getAccuracy()) + "\n";

				costFileContent += String.valueOf(experimentalData.getCost())
						+ "\n";
			}

			learningMethod.onLearningCycleComplete();
		}

		if (debug) {
			System.out.println("100%");
			FileManager.writeFileContent(accuracyPath, accuracyFileContent,
					true);

			FileManager.writeFileContent(costPath, costFileContent, true);
		}
	}

}