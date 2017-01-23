
/**
 * Author: Austin Patel
 * Project: Handwritten Recognition
 * File Name: Chromosome.java
 * Created: 01/20/17
 */

package neural_network.genetics;

import java.util.ArrayList;

/** Represents a specific chromosome in a genome. */
@SuppressWarnings("serial")
public class Chromosome<E> extends ArrayList<E>
		implements Comparable<Chromosome<E>> {

	private double fitness;

	public Chromosome(E[] values) {
		super();

		fitness = 0;

		for (int i = 0; i < values.length; i++)
			add(values[i]);
	}

	/**
	 * Simulates the crossing of genetic information with another "Chromosome".
	 */
	public Chromosome<E> cross(Chromosome<E> mate) {
		E[] newContent = (E[]) new Object[size()];
		int location = (int) (Math.random() * size());

		for (int i = 0; i < size(); i++)
			newContent[i] = (i < location) ? get(i) : mate.get(i);

		return new Chromosome<>(newContent);
	}

	/**
	 * Simulates a mutation in a specific gene in the chromosome by changing a
	 * random gene to a random decimal value.
	 */
	public void mutate() {
		set((int) (Math.random() * size()), (E) (Object) Math.random());
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}

	public double getFitness() {
		return fitness;
	}

	/**
	 * Returns positive if fitness > other, 0 if fitness = other and negative if
	 * fitness < other
	 */
	@Override
	public int compareTo(Chromosome<E> o) {
		return Double.compare(fitness, o.getFitness());
	}

}