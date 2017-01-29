
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
		ExperimentalData experimentalData = experimenter.testNetwork();
		System.out.println(experimentalData.getAccuracy() * 100 + "%");

		new GeneticAlgorithm(100, 100, 0.9d, 0) {
			{
				setNeuralNetwork(neuralNetwork);
				beginEvolution();
			}
		};

		experimentalData = experimenter.testNetwork();
		System.out.println(experimentalData.getAccuracy() * 100 + "%");
	}

	private NeuralNetwork neuralNetwork;
	private Genome<Double>[] genomes;
	private Experimenter experimenter;
	private ArrayList<LetterData[]> experimentalLetterData;
	private int chromsosomeCount, generationCount;
	private double breedRate, deathRate;

	public GeneticAlgorithm(int chromosomeCount, int generationCount,
			double breedRate, double deathRate) {
		this.chromsosomeCount = chromosomeCount;
		this.generationCount = generationCount;
		this.breedRate = breedRate;
		this.deathRate = deathRate;
	}

	/** Add the neural network and begins the setup procedure. */
	public void setNeuralNetwork(NeuralNetwork neuralNetwork) {
		this.neuralNetwork = neuralNetwork;

		genomes = new Genome[neuralNetwork.getNeurons().length];
		experimenter = new Experimenter(neuralNetwork);

		// Initialize each genome
		for (int i = 0; i < genomes.length; i++)
			genomes[i] = new Genome<Double>(chromsosomeCount,
					Constants.GRID_WIDTH * Constants.GRID_HEIGHT, breedRate,
					deathRate);

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
		for (int generation = 0; generation < generationCount; generation++) {
			// System.out.println("GENERATION #" + (generation + 1));
			nextGeneration();
		}
	}

	/**
	 * Simulates a generation of evolution for each genome. Utilizes
	 * multithreading for increased performance.
	 */
	public void nextGeneration() {
		long startTime = System.currentTimeMillis();
		// System.out.println("B" + startTime);
		// Calculate fitness values
		// Per neuron/letter
		Thread[] workers = new Thread[neuralNetwork.getNeurons().length];

		for (int letterIndex = 0; letterIndex < genomes.length; letterIndex++) {
			final int letterIndexFinal = letterIndex;

			workers[letterIndex] = new Thread(new Runnable() {
				@Override
				public void run() {
					Genome<Double> genome = genomes[letterIndexFinal];

					for (Chromosome<Double> chromosome : genome
							.getChromosomes())
						calculateFitness(chromosome, letterIndexFinal);

					genome.nextGeneration();
				}
			});

			workers[letterIndexFinal].start();
		}

		try {
			for (Thread thread : workers)
				thread.join();
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println((System.currentTimeMillis() - startTime));
	}

	/** Evaluates the fitness of a specific chromosome. */
	private void calculateFitness(Chromosome<Double> chromosome,
			int letterIndex) {
		// Set the weights of the current neuron
		double[] weights = new double[chromosome.size()];

		for (int i = 0; i < weights.length; i++)
			weights[i] = chromosome.get(i);

		neuralNetwork.getNeurons()[letterIndex].setWeights(weights);

		// Evaluate the fitness
		double fitness = experimenter
				.testLetter(Alphabet.getCharacter(letterIndex));

		// Set the chromosomes fitness
		chromosome.setFitness(fitness);
	}

	/** Assign the best chomosomes to the weights in the neural network */
	// public void commitWeights() {
	// for (int i = 0; i < genomes.length; i++) {
	// Genome<Double> genome = genomes[i];
	// Chromosome<Double> chromosome = genome
	// .getChromosomes()[chromsosomeCount - 1];
	// double[] weights = new double[chromosome.size()];
	//
	// for (int k = 0; k < weights.length; k++)
	// weights[k] = chromosome.get(k);
	//
	// neuralNetwork.getNeurons()[i].setWeights(weights);
	// }
	// }

	public int getChromsosomeCount() {
		return chromsosomeCount;
	}

	public int getGenerationCount() {
		return generationCount;
	}

	public double getBreedRate() {
		return breedRate;
	}

	public double getDeathRate() {
		return deathRate;
	}

}
