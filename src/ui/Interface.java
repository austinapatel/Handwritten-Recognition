
/**
 * Author: Austin Patel
 * Project: Handwritten Recognition
 * File Name: Interface.java
 * Created: 01/02/17
 */

package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;

import data.Alphabet;
import data.Constants;
import main.Main;
import neural_network.BackpropagationAlgorithm;
import neural_network.GeneticAlgorithmLearningMethod;
import neural_network.LearningMethod;
import neural_network.MomentumBackpropatationAlgorithm;
import neural_network.NeuralNetwork;
import neural_network.Trainer;
import neural_network.genetics.GeneticAlgorithm;

/**
 * Main interface for the program with options for interfacing with neural
 * network.
 */
@SuppressWarnings("serial")
public class Interface extends JFrame {

	private final int MARGIN = 20;

	private NeuralNetwork drawingNetwork;
	private DrawingPredictionPanel drawingPredictionPanel;
	private LetterDataMaker letterDataMaker;
	private Border panelBorder;
	private JPanel drawingPredictionBorderPanel, letterMakerBorderPanel,
			weightGridBorderPanel;
	private WeightGrid weightGrid;

	public Interface(NeuralNetwork neuralNetwork) {
		panelBorder = BorderFactory.createMatteBorder(5, 5, 5, 5, Color.BLACK);

		initializeNetwork(neuralNetwork);
		addDrawingPredictionPanel();
		addLetterDataMaker();
		addWeightGrid();

		initializeFrame();
		setVisible(true);
	}
	
	public Interface() {
		this(null);
	}

	private void initializeFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		int width = drawingPredictionBorderPanel.getX()
				+ drawingPredictionBorderPanel.getWidth() + MARGIN * 2;
		int height = letterMakerBorderPanel.getY()
				+ weightGridBorderPanel.getHeight() + MARGIN * 3;

		setSize(width, height);
		setResizable(false);
		setLayout(null);

		try {
			Image icon32 = ImageIO.read(new File("res/Icons/icon32.png"));
			Image icon64 = ImageIO.read(new File("res/Icons/icon64.png"));
			Image icon128 = ImageIO.read(new File("res/Icons/icon128.png"));

			setIconImages(new ArrayList<Image>() {
				{
					add(icon32);
					add(icon64);
					add(icon128);
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		;

		setTitle("Neural Network Handwritten Letter Recognition");
		setLocationRelativeTo(null);
	}

	private void initializeNetwork(NeuralNetwork network) {
		if (network == null) {
			LearningMethod learningMethod = 
//					new GeneticAlgorithmLearningMethod(
//					new GeneticAlgorithm(100, 300, 0.5, 0)
//					);
//					new BackpropagationAlgorithm(0.1);
					new MomentumBackpropatationAlgorithm(0.1);
			drawingNetwork = new NeuralNetwork(learningMethod,
					Constants.GRID_WIDTH * Constants.GRID_HEIGHT,
					Alphabet.getLength());

//			drawingNetwork = Trainer.twoLearningAlgorithmTest(false);
//			((GeneticAlgorithmLearningMethod) learningMethod).getGeneticAlgorithm()
//					.setNeuralNetwork(drawingNetwork);
		} else
			drawingNetwork = network;

		Trainer trainer = new Trainer(drawingNetwork);
		trainer.trainNetwork();
	}

	private void addDrawingPredictionPanel() {
		drawingPredictionBorderPanel = new JPanel();
		drawingPredictionBorderPanel.setLayout(new BorderLayout());

		drawingPredictionPanel = new DrawingPredictionPanel(drawingNetwork);

		drawingPredictionBorderPanel.setSize(
				drawingPredictionPanel.getWidth() + MARGIN,
				drawingPredictionPanel.getHeight() + MARGIN * 2);
		drawingPredictionBorderPanel.add(drawingPredictionPanel);
		drawingPredictionBorderPanel.setBorder(BorderFactory
				.createTitledBorder(panelBorder, "Handwritten Prediction"));
		drawingPredictionBorderPanel.setLocation(MARGIN, MARGIN);

		add(drawingPredictionBorderPanel);
	}

	private void addLetterDataMaker() {
		letterMakerBorderPanel = new JPanel();
		letterMakerBorderPanel.setLayout(new BorderLayout());

		letterDataMaker = new LetterDataMaker();

		letterMakerBorderPanel.setLocation(MARGIN,
				drawingPredictionBorderPanel.getY()
						+ drawingPredictionBorderPanel.getHeight() + MARGIN);

		letterMakerBorderPanel.setSize(letterDataMaker.getWidth() + MARGIN,
				letterDataMaker.getHeight() + MARGIN * 2);
		letterMakerBorderPanel.add(letterDataMaker);
		letterMakerBorderPanel.setBorder(
				BorderFactory.createTitledBorder(panelBorder, "Letter Maker"));

		add(letterMakerBorderPanel);
	}

	private void addWeightGrid() {
		weightGridBorderPanel = new JPanel();
		weightGridBorderPanel.setLayout(new BorderLayout());

		weightGrid = new WeightGrid(Constants.GRID_WIDTH, Constants.GRID_HEIGHT,
				drawingNetwork);

		int posX = letterMakerBorderPanel.getX()
				+ letterMakerBorderPanel.getWidth() + MARGIN;
		int posY = letterMakerBorderPanel.getY();
		weightGridBorderPanel.setLocation(posX, posY);

		int width = weightGrid.getWidth() + MARGIN;
		int height = weightGrid.getHeight() + MARGIN * 2;

		weightGridBorderPanel.setSize(width, height);
		weightGridBorderPanel.add(weightGrid);
		weightGridBorderPanel.setBorder(
				BorderFactory.createTitledBorder(panelBorder, "Weight Viewer"));

		add(weightGridBorderPanel);
	}

	public static void main(String[] args) {
		Main.main(null);
	}

}
