
/**
 * Author: Austin Patel
 * Project: Handwritten Recognition
 * File Name: RaspberryPiInterface.java
 * Created: 02/11/17
 */

package ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import data.Alphabet;
import data.Constants;
import data.FileManager;
import data.LetterData;
import neural_network.BackpropagationAlgorithm;
import neural_network.NeuralNetwork;
import neural_network.Trainer;

/**
 * A scaled down version of the Interface to fit on a TFT display for the
 * Raspberry Pi.
 */
public class RaspberryPiInterface extends JFrame {

	private final static int WIDTH = 320, HEIGHT = 193,
			MINIFIED_PIXEL_GRID_SIZE = 6, BUTTON_WIDTH = 173, BUTTON_HEIGHT = 61;

	private DrawingGrid drawingGrid;
	private ColorGrid predictionGrid;
	private JButton predictButton, backButton, clearButton;
	private NeuralNetwork neuralNetwork;

	public static void main(String[] args) {
		new RaspberryPiInterface();
	}

	public RaspberryPiInterface() {
		ColorGrid.PIXEL_SIZE = MINIFIED_PIXEL_GRID_SIZE;
		Constants.BUTTON_HEIGHT = BUTTON_HEIGHT;

		setSize(WIDTH, HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(null);

		neuralNetwork = new NeuralNetwork(new BackpropagationAlgorithm(0.1),
				Constants.GRID_WIDTH * Constants.GRID_HEIGHT,
				Alphabet.getLength());

		Trainer trainer = new Trainer(neuralNetwork);
		trainer.trainNetwork();
		trainer.trainNetwork();
		trainer.trainNetwork();

		initializeComponents();

		setVisible(true);
	}

	private void initializeComponents() {
		drawingGrid = new DrawingGrid(Constants.GRID_WIDTH,
				Constants.GRID_HEIGHT, Color.BLACK, false);
		drawingGrid.setLocation(0, 0);
		drawingGrid.setVisible(true);

		add(drawingGrid);
		
		clearButton = new JButton("Clear");
		clearButton.setLocation(drawingGrid.getX() + drawingGrid.getWidth(),0);
		clearButton.setVisible(true);
		clearButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
		clearButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				drawingGrid.clear();
			}
		});
		
		add(clearButton);

		predictButton = new JButton("Predict");
		predictButton.setLocation(clearButton.getX(),
				clearButton.getY() + clearButton.getHeight());
		predictButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
		predictButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (!drawingGrid.isEmpty())
					showPrediction();
			}
		});

		add(predictButton);

		predictionGrid = new ColorGrid(Constants.GRID_WIDTH,
				Constants.GRID_HEIGHT);
		predictionGrid.setLocation(0, 0);
		predictionGrid.setVisible(false);

		add(predictionGrid);
		
		backButton = new JButton("Back");
		backButton.setLocation(predictionGrid.getX() + predictionGrid.getWidth(), 0);
		backButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
		backButton.setVisible(false);
		
		backButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				showDrawingGrid();
			}
		});
		
		add(backButton);
	}

	private void showPrediction() {
		drawingGrid.setVisible(false);
		predictButton.setVisible(false);
		predictionGrid.setVisible(true);
		backButton.setVisible(true);
		clearButton.setVisible(false);

		Color[][] colors = drawingGrid.getColors();

		int[][] data2D = LetterData.colorDataToRaw(colors,
				drawingGrid.getBackground());
		int[] data1D = LetterData.data2DTo1D(data2D);

		double[] predictions = neuralNetwork.getOutput(data1D);

		// Draw prediction to "ColorGrid"
		int highestIndex = 0;
		for (int i = 0; i < predictions.length; i++)
			highestIndex = (predictions[i] > predictions[highestIndex]) ? i
					: highestIndex;

		char letterPrediction = Alphabet.getCharacter(highestIndex);
		String predictionFilePath = Constants.RESOURCES_PATH
				+ Constants.STOCK_LETTERS_FOLDER + "\\" + letterPrediction
				+ ".txt";
		String content = FileManager.readFileContent(predictionFilePath)[0];

		Color[][] colorData = LetterData.textToColorArray(content,
				Constants.GRID_WIDTH, Color.BLACK);
		predictionGrid.setColors(colorData);
	}
	
	private void showDrawingGrid() {
		drawingGrid.setVisible(true);
		predictButton.setVisible(true);
		backButton.setVisible(false);
		predictionGrid.setVisible(false);
		clearButton.setVisible(true);
		
		drawingGrid.clear();
	}

}
