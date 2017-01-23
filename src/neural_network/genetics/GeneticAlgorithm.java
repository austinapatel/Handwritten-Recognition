
/**
 * Author: Austin Patel
 * Project: Handwritten Recognition
 * File Name: GeneticAlgorithm.java
 * Created: 01/08/17
 */

package neural_network.genetics;

import java.util.ArrayList;

import data.Alphabet;
import data.Constants;
import data.ExperimentalData;
import data.LetterData;
import neural_network.Experimenter;
import neural_network.NeuralNetwork;
import neural_network.Neuron;
import neural_network.NoLearningMethod;

/**
 * Performs functions for the genetic algorithm (unsupervised learning method).
 */
public class GeneticAlgorithm {

	public static void main(String[] args) {
		NeuralNetwork neuralNetwork = new NeuralNetwork(new NoLearningMethod(),
				Constants.GRID_WIDTH * Constants.GRID_HEIGHT,
				Alphabet.getLength());
		
		Experimenter experimenter = new Experimenter(neuralNetwork);
		ExperimentalData experimentalData = experimenter.testNetwork(experimenter.getExperimentalData());
		System.out.println(experimentalData.getAccuracy() * 100 + "%");

		new GeneticAlgorithm() {
			{
				setNeuralNetwork(neuralNetwork);
				beginEvolution();
			}
		};
		
		experimentalData = experimenter.testNetwork(experimenter.getExperimentalData());
		System.out.println(experimentalData.getAccuracy() * 100 + "%");
	}

	private static final int CHROMOSOME_COUNT = 100, GENERATION_COUNT = 10;
	private static final double BREED_RATE = 0.5d, DEATH_RATE = 0.1;

	private NeuralNetwork neuralNetwork;
	private Genome<Double>[] genomes;
	private Experimenter experimenter;
	private ArrayList<LetterData[]> experimentalLetterData;

	public GeneticAlgorithm() {

	}

	/** Add the neural network and begins the setup procedure. */
	public void setNeuralNetwork(NeuralNetwork neuralNetwork) {
		this.neuralNetwork = neuralNetwork;

		genomes = new Genome[neuralNetwork.getNeurons().length];
		experimenter = new Experimenter(neuralNetwork);

		// Initialize each genome
		for (int i = 0; i < genomes.length; i++)
			genomes[i] = new Genome<Double>(CHROMOSOME_COUNT,
					Constants.GRID_WIDTH * Constants.GRID_HEIGHT, BREED_RATE, DEATH_RATE);

		// Load all experimental letters
		experimentalLetterData = new ArrayList<LetterData[]>();
		
		for (char c : Alphabet.getAlphabet().toCharArray()) {
			String filePath = Constants.RESOURCES_PATH
					+ Constants.EXPERIMENTAL_LETTERS_FOLDER + "\\" + c + ".txt";
			LetterData[] currentLetterData = LetterData.getLetterData(filePath,
					c);

			experimentalLetterData.add(currentLetterData);
		}

		// beginEvolution();

		// Test the genetic algorithm
		// experimenter.testNetwork(experimenter.getExperimentalData());
	}

	/**
	 * Evaluates the fitness of each "Chromosome" in each "Genome" (letter) and
	 * then evolve them by breeding the most fit.
	 */
	public void beginEvolution() {
		for (int generation = 0; generation < GENERATION_COUNT; generation++) {
			// System.out.println("GENERATION #" + (generation + 1));
			nextGeneration();
		}
	}

	public void nextGeneration() {
		// Calculate fitness values
		// Per neuron/letter
		for (int letterIndex = 0; letterIndex < genomes.length; letterIndex++) {
			Genome<Double> genome = genomes[letterIndex];

			// Evaluate the fitness of each chromosome/weight set (same
			// letter)
			for (Chromosome<Double> chromosome : genome.getChromosomes())
				calculateFitness(chromosome, letterIndex);

			// Begin the breeding process of the current genome
			genome.nextGeneration();
		}

//		 for (Chromosome<Double> chromosome : genomes[0].getChromosomes())
//		 {
//		 System.out.println(chromosome.getFitness());
//		 }
//		 System.out.println(
//		 genomes[0].getChromosomes()[CHROMOSOME_COUNT - 1].getFitness());

		commitWeights();
	}

	/** Evaluates the fitness of a specific chromosome. */
	private void calculateFitness(Chromosome<Double> chromosome,
			int letterIndex) {
		// Set the weights of the current neuron
		double[] weights = new double[chromosome.size()];

		for (int i = 0; i < weights.length; i++)
			weights[i] = chromosome.get(i);

		Neuron neuron = neuralNetwork.getNeurons()[letterIndex];
		neuron.setWeights(weights);

		// Evaluate the average fitness
		double fitness = 0;

//		for (LetterData letterData : experimentalLetterData.get(letterIndex))
//			fitness += neuron.getOutput(letterData.getData1D());

		fitness = experimenter.testLetter(Alphabet.getCharacter(letterIndex));
		
//		fitness = fitness / (experimentalLetterData.get(letterIndex).length);

		// Set the chromosomes fitness
		chromosome.setFitness(fitness);
	}

	/** Assign the best chomosomes to the weights in the neural network */
	private void commitWeights() {
		for (int i = 0; i < genomes.length; i++) {
			Genome<Double> genome = genomes[i];
			Chromosome<Double> chromosome = genome
					.getChromosomes()[CHROMOSOME_COUNT - 1];
			double[] weights = new double[chromosome.size()];

			for (int k = 0; k < weights.length; k++)
				weights[k] = chromosome.get(k);

			neuralNetwork.getNeurons()[i].setWeights(weights);
		}
	}

}
