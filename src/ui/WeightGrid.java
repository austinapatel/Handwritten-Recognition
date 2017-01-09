
/**
 * Author: Austin Patel
 * Project: Handwritten Recognition
 * File Name: WeightGrid.java
 * Created: 01/07/17
 */

package ui;

import java.awt.Color;

import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;

import data.Alphabet;
import data.Constants;
import neural_network.NeuralNetwork;
import neural_network.Neuron;
import ui.CharacterPicker.Style;

/**
 * A "ColorGrid" that displays the current weights in the neural network with a
 * visual representation. Adds functionality for picking a letter to display.
 */
@SuppressWarnings("serial")
public class WeightGrid extends ColorGrid {

	private static final int LETTER_COLUMN_WIDTH = 24, SLIDER_HEIGHT = 20,
			SLIDER_MIN = 0, SLIDER_MAX = 400, SLIDER_INITIAL = 200;

	private NeuralNetwork neuralNetwork;
	private CharacterPicker characterPicker;
	private JSlider scaleSlider;
	private double scale;
	private char letter;

	public WeightGrid(int width, int height, NeuralNetwork neuralNetwork) {
		super(width, height);
		setSize(getWidth() + LETTER_COLUMN_WIDTH, getHeight() + SLIDER_HEIGHT);

		this.neuralNetwork = neuralNetwork;

		scale = SLIDER_INITIAL;
		letter = 'A';

		initializeCharacterPicker();
		initializeJSlider();

		setLetter(letter);
	}

	private void initializeCharacterPicker() {
		characterPicker = new CharacterPicker(Style.List) {
			@Override
			public void onValueChange(ListSelectionEvent e) {
				letter = characterPicker.getSelectedValue();

				setLetter(letter);
			}
		};

		characterPicker.setLocation(getWidth() - LETTER_COLUMN_WIDTH, 0);
		characterPicker.setBackground(getBackground());
		characterPicker.setSize(LETTER_COLUMN_WIDTH,
				characterPicker.getPrefferedHeight() * Alphabet.getLength());

		add(characterPicker);
	}

	private void setLetter(char letter) {
		Neuron neuron = neuralNetwork.getNeurons()[Alphabet.indexOf(letter)];
		double[] weights = neuron.getWeights().clone();

		// double smallestValue = weights[0];
		// double largestValue = weights[0];
		//
		// for (int i = 0; i < weights.length; i++) {
		// smallestValue = Math.min(weights[i], smallestValue);
		// largestValue = Math.max(weights[i], largestValue);
		// }
		//
		// double scaleMin = Math.abs(Constants.MAX_COLOR_VALUE /
		// smallestValue);
		// double scaleMax = Math.abs(Constants.MAX_COLOR_VALUE / largestValue);
		//
		// double scale = Math.min(scaleMin, scaleMax);

		// scale = 5000;

		for (int i = 0; i < weights.length; i++)
			weights[i] *= scale;
//		
//		System.out.println(letter);
//		LetterData[] stockData = LetterData.getStockData();
//		
//		int[] inputs = stockData[Alphabet.indexOf(letter)].getData1D();
		
//		for (int i = 0; i < weights.length; i++)
//			weights[i] *= Math.abs(1 - inputs[i]);
//			weights[i] = inputs[i] * 255;
			
//		double[] weights = new double[400];
		
//		for (int i = 0; i < weights.length; i++)
//			weights[i] = 255 * inputs[i];
		
		for (int i = 0; i < weights.length; i++) {
			int posY = i / Constants.GRID_HEIGHT;
			int posX = i % Constants.GRID_WIDTH;
			
			Color color;
			int currentWeight = (int) weights[i];

			if (Math.abs(currentWeight) > Constants.MAX_COLOR_VALUE) {
				drawPixel(posX, posY, Color.WHITE);
				
				continue;
			}

			try {
				if (currentWeight < 0)
					color = new Color(0, 0, -currentWeight);
				else
					color = new Color(currentWeight, 0, 0);

				super.drawPixel(posX, posY, color);
			} catch (Exception e) {
				System.out.println("exception: " + currentWeight);
			}

		}
	}

	private void initializeJSlider() {
		scaleSlider = new JSlider(SwingConstants.HORIZONTAL, SLIDER_MIN,
				SLIDER_MAX, SLIDER_INITIAL);
		scaleSlider.setLocation(0, getHeight() - SLIDER_HEIGHT);
		scaleSlider.setSize(getWidth(), SLIDER_HEIGHT);

		scaleSlider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				scale = scaleSlider.getValue();

				setLetter(letter);
			}
		});

		add(scaleSlider);
	}

}
