
/**
 * Author: Austin Patel
 * Project: Handwritten Recognition
 * File Name: Genome.java
 * Created: 01/20/17
 */

package neural_network.genetics;

import java.lang.reflect.Array;
import java.util.Arrays;

/** Simulates a genome and genetic evolutionary algorithms. */
public class Genome<E> {

	private Chromosome<E>[] chromosomes;
	private double breedRate, deathRate;
	private int chromosomeSize;

	public Genome(int chromosomeCount, int chromosomeSize, double breedRate,
			double deathRate) {
		chromosomes = new Chromosome[chromosomeCount];

		this.breedRate = breedRate;
		this.deathRate = deathRate;
		this.chromosomeSize = chromosomeSize;

		generateChromosomes(chromosomeSize);
	}

	/**
	 * Simulates an entire generation of evolution occurring with breeding
	 * procedures.
	 */
	public void nextGeneration() {

		new Thread(new Runnable() {

			@Override
			public void run() {
				// Breed the best chromosomes
				Arrays.sort(chromosomes);

				// Breed the best part of the chromosomes
				for (int i = 0; i < chromosomes.length * breedRate; i++)
					chromosomes[i] = chromosomes[chromosomes.length - i - 1]
							.cross(chromosomes[chromosomes.length - i - 2]);

				// Mutate
				for (int i = 0; i < chromosomes.length * 2; i++)
					chromosomes[(int) (Math.random() * chromosomes.length)]
							.mutate();

				Arrays.sort(chromosomes);
				for (int i = 0; i < chromosomes.length * deathRate; i++)
					chromosomes[i] = new Chromosome<E>(
							(E[]) randomValues(chromosomeSize));
			}
		}).start();

		// chromosomes[0] = chromosomes[9].cross(chromosomes[8]);
		// chromosomes[1] = chromosomes[8].cross(chromosomes[7]);
		// chromosomes[2] = chromosomes[7].cross(chromosomes[6]);
		// chromosomes[4] = chromosomes[6].cross(chromosomes[5]);

		// chromosomes[6].cross(chromosomes[5]);
		// chromosomes[5].cross(chromosomes[4]);
		// chromosomes[4].cross(chromosomes[3]);
		// chromosomes[8].cross(chromosomes[7]);
		// chromosomes[(int) (Math.random() * 10)].cross(chromosomes[(int)
		// (Math.random() * 10)]);
	}

	public Chromosome<E>[] getChromosomes() {
		return chromosomes;
	}

	/** Creates each chromosome in the genome with random values. */
	private void generateChromosomes(int chromosomeSize) {
		for (int i = 0; i < chromosomes.length; i++)
			chromosomes[i] = new Chromosome(randomValues(chromosomeSize));
	}

	private Double[] randomValues(int size) {
		Double[] values = new Double[size];

		for (int k = 0; k < size; k++)
			values[k] = Math.random() - 0.5d;

		return values;
	}

}
