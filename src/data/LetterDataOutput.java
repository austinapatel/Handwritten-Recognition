
/**
 * Author: Austin Patel
 * Project: Handwritten Recognition
 * File Name: LetterDataOutput.java
 * Created: 01/29/17
 */

package data;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import neural_network.Experimenter;

/** Puts all of the letter data into an image file. */
public class LetterDataOutput {
	
	public static void main(String[] args) {
		new LetterDataOutput();
	}
	
	public LetterDataOutput() {
		ArrayList<LetterData> trainingData = getTrainingData();
		BufferedImage[] trainingImages = new BufferedImage[trainingData.size()];
		
		for (int i = 0; i < trainingData.size(); i++)
			trainingImages[i] = getImage(trainingData.get(i));
		
		exportImages(trainingImages, "TrainingLettersImage");
		
		ArrayList<LetterData> experimentalData = Experimenter.getExperimentalData();
		BufferedImage[] experimentalImages = new BufferedImage[experimentalData.size()];
		
		for (int i = 0; i < experimentalData.size(); i++)
			experimentalImages[i] = getImage(experimentalData.get(i));
		
		exportImages(experimentalImages, "ExperimentalLettersImage");
	}
	
	private ArrayList<LetterData> getTrainingData() {
		ArrayList<LetterData> trainingData = new ArrayList<LetterData>();

		for (char c : Alphabet.getAlphabet().toCharArray()) {
			String filePath = Constants.RESOURCES_PATH
					+ Constants.TRAINING_LETTERS_FOLDER + "\\" + c + ".txt";
			LetterData[] currentLetterData = LetterData.getLetterData(filePath,
					c);

			for (LetterData letterData : currentLetterData)
				trainingData.add(letterData);
		}
		
		return trainingData;
	}
	
	/**Returns a BufferedImage of a single letter.*/
	private BufferedImage getImage(LetterData letterData) {
		BufferedImage image = new BufferedImage(Constants.GRID_WIDTH,
				Constants.GRID_HEIGHT, BufferedImage.TYPE_INT_RGB);
		
		int[][] pixels = letterData.getData2D();
		
		for (int x = 0; x < pixels.length; x++)
			for (int y = 0; y < pixels[x].length; y++)
				image.setRGB(x, y, (pixels[x][y] == 0) ? Color.WHITE.getRGB() : Color.BLACK.getRGB());
		
		return image;
	}
	
	/** Exports all of the images to a single image. */
	private void exportImages(BufferedImage[] images, String fileName) {
		int exportWidth = images.length / Alphabet.getLength();
		
		BufferedImage image = new BufferedImage(
				Constants.GRID_WIDTH * exportWidth,
				Constants.GRID_HEIGHT * (images.length / exportWidth),
				BufferedImage.TYPE_INT_RGB);

		Graphics2D graphics2d = image.createGraphics();

		for (int i = 0; i < images.length; i++) {
			int posY = i / exportWidth;
			int posX = i % exportWidth;

			BufferedImage letterImage = images[i];

			graphics2d.drawImage(letterImage, posX * Constants.GRID_WIDTH,
					posY * Constants.GRID_HEIGHT, null);
		}
		
		File outputfile = new File(fileName + ".png");
		try {
			ImageIO.write(image, "png", outputfile);
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

}
