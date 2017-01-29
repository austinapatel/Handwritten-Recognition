
/**
 * Author: Austin Patel
 * Project: Handwritten Recognition
 * File Name: WeightGrid.java
 * Created: 01/07/17
 */

package ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
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
			SLIDER_MIN = 0, SLIDER_MAX = 500, SLIDER_INITIAL = 500,
			EXPORT_WIDTH = 7, EXPORT_HEIGHT = 4;

	private NeuralNetwork neuralNetwork;
	private CharacterPicker characterPicker;
	private JSlider scaleSlider;
	private JButton exportButton;
	private double scale;
	private char letter;

	public WeightGrid(int width, int height, NeuralNetwork neuralNetwork) {
		super(width, height);
		setSize(getWidth() + LETTER_COLUMN_WIDTH,
				getHeight() + SLIDER_HEIGHT + Constants.BUTTON_HEIGHT);

		this.neuralNetwork = neuralNetwork;

		scale = SLIDER_INITIAL;

		initializeCharacterPicker();
		initializeJSlider();
		initializeExportButton();

		setLetter('A', true);
	}

	private void initializeCharacterPicker() {
		characterPicker = new CharacterPicker(Style.List) {
			@Override
			public void onValueChange(ListSelectionEvent e) {
				setLetter(characterPicker.getSelectedValue(), true);
			}
		};

		characterPicker.setLocation(getWidth() - LETTER_COLUMN_WIDTH, 0);
		characterPicker.setBackground(getBackground());
		characterPicker.setSize(LETTER_COLUMN_WIDTH,
				characterPicker.getPrefferedHeight() * Alphabet.getLength());

		add(characterPicker);
	}

	/**
	 * Either draws a letter's weights visually or returns a BufferedImage of
	 * the weights.
	 */
	private BufferedImage setLetter(char letter, boolean visual) {
		this.letter = letter;
		Neuron neuron = neuralNetwork.getNeurons()[Alphabet.indexOf(letter)];
		double[] weights = neuron.getWeights().clone();
		BufferedImage image = null;

		if (!visual)
			image = new BufferedImage(Constants.GRID_WIDTH,
					Constants.GRID_HEIGHT, BufferedImage.TYPE_INT_RGB);

		for (int i = 0; i < weights.length; i++)
			weights[i] *= scale;

		for (int i = 0; i < weights.length; i++) {
			int posY = i / Constants.GRID_WIDTH;
			int posX = i % Constants.GRID_WIDTH;

			Color color;
			int currentWeight = (int) weights[i];

			// if (Math.abs(currentWeight) > Constants.MAX_COLOR_VALUE) {
			// drawPixel(posX, posY, Color.WHITE);
			//
			// continue;
			// }

			try {
				if (currentWeight < -255)
					color = Color.BLUE;
				else if (currentWeight > 255)
					color = Color.RED;
				else if (currentWeight < 0)
					color = new Color(0, 0, -currentWeight);
				else
					color = new Color(currentWeight, 0, 0);

				if (visual)
					super.drawPixel(posX, posY, color);
				else
					image.setRGB(posX, posY, color.getRGB());
			} catch (Exception e) {
				System.out.println("exception: " + currentWeight);
			}

		}

		return image;
	}

	private void initializeJSlider() {
		scaleSlider = new JSlider(SwingConstants.HORIZONTAL, SLIDER_MIN,
				SLIDER_MAX, SLIDER_INITIAL);
		scaleSlider.setLocation(0,
				getHeight() - SLIDER_HEIGHT - Constants.BUTTON_HEIGHT);
		scaleSlider.setSize(getWidth(), SLIDER_HEIGHT);

		scaleSlider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				scale = scaleSlider.getValue();

				setLetter(letter, true);
			}
		});

		add(scaleSlider);
	}

	private void initializeExportButton() {
		exportButton = new JButton("Export");

		exportButton.setLocation(0, getHeight() - Constants.BUTTON_HEIGHT);
		exportButton.setSize(getWidth(), Constants.BUTTON_HEIGHT);

		exportButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				exportImages();
			}
		});

		add(exportButton);
	}

	/** Exports all of the heat map images to a single image. */
	private void exportImages() {
		char curLetter = letter;
		
		BufferedImage image = new BufferedImage(
				Constants.GRID_WIDTH * EXPORT_WIDTH,
				Constants.GRID_HEIGHT * EXPORT_HEIGHT,
				BufferedImage.TYPE_INT_RGB);

		Graphics2D graphics2d = image.createGraphics();

		for (int i = 0; i < Alphabet.getLength(); i++) {
			int posY = i / EXPORT_WIDTH;
			int posX = i % EXPORT_WIDTH;

			BufferedImage letterImage = setLetter(Alphabet.getCharacter(i),
					false);

			graphics2d.drawImage(letterImage, posX * Constants.GRID_WIDTH,
					posY * Constants.GRID_HEIGHT, null);
		}
		
		File outputfile = new File("heatMaps.png");
		try {
			ImageIO.write(image, "png", outputfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		setLetter(curLetter, true);
	}

}
