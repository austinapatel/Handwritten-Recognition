
/**
 * Author: Austin Patel
 * Project: Handwritten Recognition
 * File Name: PredictionPane.java
 * Created: 01/03/17
 */

package ui;

import java.awt.Color;
import java.text.DecimalFormat;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import data.Alphabet;
import data.Constants;
import data.FileManager;
import data.LetterData;

/**
 * Displays the neural network's predictions with a numerical representation.
 */
@SuppressWarnings("serial")
public class PredictionPane extends JPanel {

	private DefaultListModel<String> predictionsJListModel;
	private JList<String> predictionsJList;
	private DecimalFormat numberFormat;
	private ColorGrid predictionGrid;

	public PredictionPane(ColorGrid predictionGrid) {
		setLayout(null);
		this.predictionGrid = predictionGrid;

		numberFormat = new DecimalFormat("0.000");
		predictionsJListModel = new DefaultListModel<String>();
		predictionsJList = new JList<String>(predictionsJListModel) {
			{
				setLayoutOrientation(JList.VERTICAL_WRAP);
				setVisibleRowCount(-1);
			}
		};

		String[] characterHeader = new String[Alphabet.getAlphabet().length()];

		char[] alphabet = Alphabet.getAlphabet().toCharArray();
		for (int i = 0; i < alphabet.length; i++)
			characterHeader[i] = alphabet[i] + ": ";

		add(predictionsJList);
	}

	public void init(double[] predictions) {
		// Initialize the JList that displays the prediction values
		predictionsJList.setSize(getWidth(), getHeight());
		predictionsJList.setLocation(0, 0);

		predictionsJListModel.clear();
		for (int i = 0; i < predictions.length; i++)
			predictionsJListModel.addElement(Alphabet.getCharacter(i) + ": "
					+ String.valueOf(numberFormat.format(predictions[i])));

		predictionsJList.setBackground(getBackground());

		DefaultListCellRenderer predictionsRenderer = (DefaultListCellRenderer) predictionsJList
				.getCellRenderer();
		predictionsRenderer.setHorizontalAlignment(SwingConstants.LEFT);

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

}
