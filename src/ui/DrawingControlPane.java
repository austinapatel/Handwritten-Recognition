
/**
 * Author: Austin Patel
 * Project: Handwritten Recognition
 * File Name: DrawingControlPane.java
 * Created: 01/02/17
 */

package ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import data.Constants;
import data.LetterData;
import neural_network.NeuralNetwork;

/**Controls for the "DrawingPane".*/
@SuppressWarnings("serial")
public class DrawingControlPane extends JPanel {
	
	private static int PANE_WIDTH = 150;

	public DrawingControlPane(DrawingGrid drawingGrid, NeuralNetwork neuralNetwork, PredictionPane predictionPane) {
		setLayout(null);
		setSize(PANE_WIDTH, drawingGrid.getHeight());
		
		int height = getHeight();
		
		add(new JButton("Predict") {{
			addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {					
					Color[][] colors = drawingGrid.getColors();					
			
					int[][] data2D = LetterData.colorDataToRaw(colors, drawingGrid.getBackground());
					int[] data1D = LetterData.data2DTo1D(data2D);
					
					if (!drawingGrid.isEmpty())
						predictionPane.init(neuralNetwork.getOutput(data1D));
				}
			});
		
			setSize(PANE_WIDTH, Constants.BUTTON_HEIGHT);
			setLocation(0, height / 2 - Constants.BUTTON_HEIGHT / 2);
		}});
	}

}
