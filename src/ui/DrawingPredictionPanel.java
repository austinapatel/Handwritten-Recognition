
/**
 * Author: Austin Patel
 * Project: Handwritten Recognition
 * File Name: DrawingPredictionPanel.java
 * Created: 01/04/17
 */

package ui;

import java.awt.Color;

import javax.swing.JPanel;

import data.Constants;
import neural_network.NeuralNetwork;

/**
 * Combines a "DrawingGrid" a "DrawingControlPane"* and a "PredictionPane into a
 * single JPanel."
 */
@SuppressWarnings("serial")
public class DrawingPredictionPanel extends JPanel {

	private int PREDICTION_PANEL_WIDTH = 90;
	
	private DrawingGrid drawingGrid;
	private ColorGrid predictionColorGrid;
	private DrawingControlPane drawingControlPane;
	private PredictionPane predictionPane;
	private NeuralNetwork drawingNetwork;
	
	public DrawingPredictionPanel(NeuralNetwork drawingNetwork) {
		setLayout(null);
		
		this.drawingNetwork = drawingNetwork;
		
		predictionColorGrid = new ColorGrid(Constants.GRID_WIDTH, Constants.GRID_HEIGHT);
		predictionPane = new PredictionPane(predictionColorGrid);
		
		addDrawingGrid();
		addPredictionGrid();
		addPredictionPane();
		
		setSize(predictionPane.getX() + predictionPane.getWidth(), drawingGrid.getHeight());
	}
	
	private void addDrawingGrid() {
		drawingGrid = new DrawingGrid(Constants.GRID_WIDTH, Constants.GRID_HEIGHT, Color.BLACK);
		drawingGrid.setLocation(0, 0);

		drawingControlPane = new DrawingControlPane(drawingGrid,
				drawingNetwork, predictionPane);
		drawingControlPane.setLocation(
				(int) (drawingGrid.getLocation().getX()
						+ drawingGrid.getSize().getWidth()),
				(int) drawingGrid.getLocation().getY());

		add(drawingControlPane);
		add(drawingGrid);
	}
	
	private void addPredictionGrid() {
		predictionColorGrid
				.setLocation((int) (drawingControlPane.getLocation().getX()
						+ drawingControlPane.getWidth()), 0);

		add(predictionColorGrid);
	}

	private void addPredictionPane() {
		predictionPane.setSize(PREDICTION_PANEL_WIDTH, predictionColorGrid.getHeight());
		predictionPane.setLocation((int) (predictionColorGrid.getLocation().getX() + predictionColorGrid.getWidth()) + 20, 0);		
		
		add(predictionPane);
	}

}
