
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
import ui.Interface;

/** Uses training "LetterData" as inputs to teach the neural network. */
public class Trainer {

	private static int TRIALS = 1, ITERATION_MULTIPLIER = 3, GA_MULTIPLIER = 1;
	private static int DEFAULT_GENERATIONS = 100, DEFAULT_CHROMOSOMES = 100,
			TEST_COUNT = 100 * ITERATION_MULTIPLIER,
			GA_TEST_COUNT = 100 * GA_MULTIPLIER;
	private static double DEFAULT_BREED_RATE = 0.9, DEFAULT_DEATH_RATE = 0.00;

	private String accuracyFileContent, costFileContent;

	public static void main(String[] args) {
		LearningMethod[] learningMethods = new LearningMethod[] {
				// new BackpropagationAlgorithm(0),
				// new BackpropagationAlgorithm(0.025),
				// new BackpropagationAlgorithm(0.05),
				// new BackpropagationAlgorithm(0.1),
				// new BackpropagationAlgorithm(0.2),
				// new BackpropagationAlgorithm(0.3),
				// new BackpropagationAlgorithm(0.4),
				// new BackpropagationAlgorithm(0.5),
				// new BackpropagationAlgorithm(0.6),
				// new BackpropagationAlgorithm(0.6),
				// new BackpropagationAlgorithm(0.7),
				// new BackpropagationAlgorithm(0.8),
				// new BackpropagationAlgorithm(0.9),
				// new BackpropagationAlgorithm(1),
				// new BackpropagationAlgorithm(1.5),
				// new BackpropagationAlgorithm(2),

				new MomentumBackpropatationAlgorithm(0.1),
				// new MomentumBackpropatationAlgorithm(0.3),
				// new MomentumBackpropatationAlgorithm(0.5),
				// new MomentumBackpropatationAlgorithm(0.7),
				// new MomentumBackpropatationAlgorithm(0.9),
				//
				// new GeneticAlgorithmLearningMethod(new GeneticAlgorithm(
				// DEFAULT_CHROMOSOMES, DEFAULT_GENERATIONS,
				// DEFAULT_BREED_RATE, DEFAULT_DEATH_RATE)),

				// new GeneticAlgorithmLearningMethod(
				// new GeneticAlgorithm(150, DEFAULT_GENERATIONS,
				// DEFAULT_BREED_RATE, DEFAULT_DEATH_RATE)),
				//
				// new GeneticAlgorithmLearningMethod(
				// new GeneticAlgorithm(DEFAULT_CHROMOSOMES,
				// DEFAULT_GENERATIONS, 0, DEFAULT_DEATH_RATE)),
				// new GeneticAlgorithmLearningMethod(new GeneticAlgorithm(
				// DEFAULT_CHROMOSOMES, DEFAULT_GENERATIONS, 0.125,
				// DEFAULT_DEATH_RATE)),
				// new GeneticAlgorithmLearningMethod(
				// new GeneticAlgorithm(DEFAULT_CHROMOSOMES,
				// DEFAULT_GENERATIONS, 0.25, DEFAULT_DEATH_RATE)),
				// new GeneticAlgorithmLearningMethod(
				// new GeneticAlgorithm(DEFAULT_CHROMOSOMES,
				// DEFAULT_GENERATIONS, 0.50, DEFAULT_DEATH_RATE)),
				// new GeneticAlgorithmLearningMethod(
				// new GeneticAlgorithm(DEFAULT_CHROMOSOMES,
				// DEFAULT_GENERATIONS, 0.75, DEFAULT_DEATH_RATE)),
				// new GeneticAlgorithmLearningMethod(
				// new GeneticAlgorithm(DEFAULT_CHROMOSOMES,
				// DEFAULT_GENERATIONS, 0.8, DEFAULT_DEATH_RATE)),
				// new GeneticAlgorithmLearningMethod(
				// new GeneticAlgorithm(DEFAULT_CHROMOSOMES,
				// DEFAULT_GENERATIONS, 0.9, DEFAULT_DEATH_RATE)),
				// new GeneticAlgorithmLearningMethod(
				// new GeneticAlgorithm(DEFAULT_CHROMOSOMES,
				// DEFAULT_GENERATIONS, 1, DEFAULT_DEATH_RATE)),
				//
				// new GeneticAlgorithmLearningMethod(
				// new GeneticAlgorithm(DEFAULT_CHROMOSOMES,
				// DEFAULT_GENERATIONS, DEFAULT_BREED_RATE, 0.01)),
				// new GeneticAlgorithmLearningMethod(
				// new GeneticAlgorithm(DEFAULT_CHROMOSOMES,
				// DEFAULT_GENERATIONS, DEFAULT_BREED_RATE, 0.02)),
				// new GeneticAlgorithmLearningMethod(
				// new GeneticAlgorithm(DEFAULT_CHROMOSOMES,
				// DEFAULT_GENERATIONS, DEFAULT_BREED_RATE, 0.03)),
				// new GeneticAlgorithmLearningMethod(
				// new GeneticAlgorithm(DEFAULT_CHROMOSOMES,
				// DEFAULT_GENERATIONS, DEFAULT_BREED_RATE, 0.04)),
				// new GeneticAlgorithmLearningMethod(
				// new GeneticAlgorithm(DEFAULT_CHROMOSOMES,
				// DEFAULT_GENERATIONS, DEFAULT_BREED_RATE, 0.05)),
				// new GeneticAlgorithmLearningMethod(
				// new GeneticAlgorithm(DEFAULT_CHROMOSOMES,
				// DEFAULT_GENERATIONS, DEFAULT_BREED_RATE, 0.1)),
				// new GeneticAlgorithmLearningMethod(
				// new GeneticAlgorithm(DEFAULT_CHROMOSOMES,
				// DEFAULT_GENERATIONS, DEFAULT_BREED_RATE, 0.15)),
				// new GeneticAlgorithmLearningMethod(new GeneticAlgorithm(
				// DEFAULT_CHROMOSOMES, DEFAULT_GENERATIONS,
				// DEFAULT_BREED_RATE, 0.2))

				// new LearningDecayBackpropagationAlgorithm(0.1,
				// 0.999999999999)
		};

		// for (int trial = 1; trial <= TRIALS; trial++)
		// for (LearningMethod learningMethod : learningMethods) {
		// NeuralNetwork neuralNetwork = new NeuralNetwork(learningMethod,
		// Constants.GRID_WIDTH * Constants.GRID_HEIGHT,
		// Alphabet.getLength());
		//
		// if (learningMethod.getName()
		// .equals(GeneticAlgorithmLearningMethod.NAME))
		// ((GeneticAlgorithmLearningMethod) learningMethod)
		// .getGeneticAlgorithm()
		// .setNeuralNetwork(neuralNetwork);
		//
		// Trainer trainer = new Trainer(neuralNetwork, true,
		// learningMethod.getFileName()
		// + ((LETTER_MULTIPLIER == 1) ? ""
		// : "Multiplier" + LETTER_MULTIPLIER)
		// + "T" + trial);
		//
		// trainer.trainNetwork();
		// }

		twoLearningAlgorithmTest(true);
	}

	public static NeuralNetwork twoLearningAlgorithmTest(boolean debug) {
		NeuralNetwork neuralNetwork = new NeuralNetwork(
				new GeneticAlgorithmLearningMethod(
						new GeneticAlgorithm(100, 100, 0.5, 0)),
				// new WeightDecayBackpropagationAlgorithm(0.1, 0),
				// new BackpropagationAlgorithm(0.1),
				// new MomentumBackpropatationAlgorithm(0.1),
				// new LearningDecayBackpropagationAlgorithm(0.1, 1.00000001),
				Constants.GRID_WIDTH * Constants.GRID_HEIGHT,
				Alphabet.getLength());

		GeneticAlgorithmLearningMethod learningMethod = ((GeneticAlgorithmLearningMethod) neuralNetwork
				.getLearningMethod());
		learningMethod.getGeneticAlgorithm().setNeuralNetwork(neuralNetwork);

		Trainer trainer;
		if (debug)
			trainer = new Trainer(neuralNetwork, true, "combinedGAandBP");
		else
			trainer = new Trainer(neuralNetwork);
		trainer.trainNetwork(true);
		trainer.trainNetwork(true);
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
		// neuralNetwork.setLearningMethod(new GeneticAlgorithmLearningMethod(
		// new GeneticAlgorithm(100, 100, 0.9, 0.0)));
		//
		// GeneticAlgorithm geneticAlgorithm = ((GeneticAlgorithmLearningMethod)
		// (neuralNetwork
		// .getLearningMethod())).getGeneticAlgorithm();
		//
		// geneticAlgorithm.setNeuralNetwork(neuralNetwork);
		//
		trainer.trainNetwork();
		trainer.trainNetwork();
		// trainer.trainNetwork();
		// trainer.trainNetwork();
		// trainer.trainNetwork();
		// trainer.trainNetwork();
		// trainer.trainNetwork();
		// trainer.trainNetwork();

		experimentalData = experimenter.testNetwork();

		System.out.println("Accuracy: "
				+ (int) (experimentalData.getAccuracy() * 100) + '%');

		// new Interface(neuralNetwork);

		return neuralNetwork;
	}

	private static boolean init;
	private static ArrayList<LetterData> trainingLetterData;
	private static int[][] actualValues;

	private NeuralNetwork neuralNetwork;
	private Experimenter experimenter;
	private String accuracyPath, costPath;
	private String fileName;
	private boolean debug;

	/**
	 * Initializes the Trainer with debugging capabilities including file output
	 * and progress measurement.
	 */
	public Trainer(NeuralNetwork neuralNetwork, boolean debug,
			String fileName) {
		this.neuralNetwork = neuralNetwork;
		this.debug = debug;
		this.fileName = fileName;

		File file = new File(fileName);
		if (file.exists())
			file.delete();

		experimenter = new Experimenter(neuralNetwork);

		accuracyPath = "/" + Constants.OUTPUT_DATA_FOLDER + "/" + fileName
				+ "Accuracy" + ".txt";

		costPath = "/" + Constants.OUTPUT_DATA_FOLDER + "/" + fileName + "Cost"
				+ ".txt";

		if (!Trainer.init)
			init();
	}

	/** Assumes debug false. */
	public Trainer(NeuralNetwork neuralNetwork) {
		this(neuralNetwork, false, "");
	}

	/** Initializes data required by the trainer. */
	public static void init() {
		Trainer.loadLetterData();
		Trainer.init = true;

		Trainer.actualValues = new int[Alphabet
				.getLength()][Constants.GRID_WIDTH * Constants.GRID_HEIGHT];

		for (int i = 0; i < actualValues.length; i++) {
			char cur = Alphabet.getCharacter(i);

			Trainer.actualValues[i] = Trainer.getActualValues(cur);
		}
	}

	/** Loads experimental and training data. */
	private static void loadLetterData() {
		// Load training data
		ArrayList<LetterData> tempData = new ArrayList<LetterData>();
		trainingLetterData = new ArrayList<LetterData>();

		for (char c : Alphabet.getAlphabet().toCharArray()) {
			String filePath = Constants.RESOURCES_PATH
					+ Constants.TRAINING_LETTERS_FOLDER + "\\" + c + ".txt";
			LetterData[] currentLetterData = LetterData.getLetterData(filePath,
					c);

			for (LetterData letterData : currentLetterData)
				tempData.add(letterData);
		}

		// Shuffle the training data
		while (!tempData.isEmpty())
			trainingLetterData.add(
					tempData.remove((int) (Math.random() * tempData.size())));
	}

	public void trainNetwork() {
		trainNetwork(false);
	}

	/**
	 * Trains the neural network using its learning method and handles for
	 * Genetic Algorithm learning additionally.
	 */
	public void trainNetwork(boolean append) {
		if (debug)
			System.out.println(fileName);

		accuracyFileContent = "";
		costFileContent = "";

		// Initially test the neural networks's capabilities
		if (debug)
			testNetwork();

		// Prepare for testing the network
		LearningMethod learningMethod = neuralNetwork.getLearningMethod();

		boolean isGenetic = learningMethod.getName()
				.equals(GeneticAlgorithmLearningMethod.NAME);

		int totalIterations = 0;
		if (isGenetic) {
			totalIterations = ((GeneticAlgorithmLearningMethod) learningMethod)
					.getGeneticAlgorithm().getGenerationCount();

			totalIterations *= GA_MULTIPLIER;
		} else {
			totalIterations = trainingLetterData.size();
			totalIterations *= ITERATION_MULTIPLIER;
		}

		// Loop through each letter or generation
		int lastPercent = -1;
		int testIncrement = totalIterations
				/ ((isGenetic) ? GA_TEST_COUNT : TEST_COUNT);
		boolean testThisIteration = false;

		for (int i = 0; i < totalIterations; i++) {
			// Progress calculations
			int percent = (int) ((double) i / totalIterations * 100);

			if (percent % 10 == 0 && percent != lastPercent) {
				if (debug)
					System.out.println(percent + "%");
				lastPercent = percent;
			}

			testThisIteration = (i % testIncrement == 0) && i != 0;

			// Learning process
			learningMethod.onLearningCycleStart();

			if (!isGenetic) {
				// Use modulus to handle overflow if using extra letters
				LetterData letterData = trainingLetterData
						.get(i % trainingLetterData.size());

				neuralNetwork.learn(letterData.getData1D(),
						Trainer.actualValues[Alphabet
								.indexOf(letterData.getCharacter())]);
			}

			if (debug && testThisIteration)
				testNetwork();
		}

		if (debug) {
			testNetwork();

			System.out.println("100%");
			FileManager.writeFileContent(accuracyPath,
					accuracyFileContent.trim(), append);

			FileManager.writeFileContent(costPath, costFileContent.trim(),
					append);
		}
	}

	/** Tests the network and saves its data. */
	private void testNetwork() {
		ExperimentalData experimentalData = experimenter.testNetwork();

		accuracyFileContent += String.valueOf(experimentalData.getAccuracy())
				+ "\n";

		costFileContent += String.valueOf(experimentalData.getCost()) + "\n";
	}

	/**
	 * Returns the results expected from the neural network for a given letter.
	 */
	private static int[] getActualValues(char c) {
		int[] actualValues = new int[Alphabet.getAlphabet().length()];

		for (int i = 0; i < actualValues.length; i++)
			actualValues[i] = 0;

		actualValues[Alphabet.getLocation(c)] = 1;

		return actualValues;
	}

}