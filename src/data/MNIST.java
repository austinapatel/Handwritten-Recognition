
/**
 * Author: Austin Patel
 * Project: Handwritten Recognition
 * File Name: MNIST.java
 * Created: 01/06/17
 */

package data;

import java.util.Arrays;

/**
 * Handles functions for reading from the MNIST handwritten letter dataset. Also
 * has functions for converting the data from the format in the dataset to a
 * format usable by a "ColorGrid".
 */
public class MNIST {

	private static final String TEST_BASE_NAME = "t10k",
			TRAINING_BASE_NAME = "train", IMAGE_PART_NAME = "images",
			LABEL_PART_NAME = "labels", IMAGE_FILE_ENDING = ".idx3-ubyte",
			LABEL_FILE_ENDING = ".idx1-ubyte";

	private static final int TRAINING_SET_SIZE = 60000, TEST_SET_SIZE = 10000,
			IMAGE_DATA_BEGINNING = 16, PIXELS_IN_LETTER = 784;

	public enum Dataset {
		Training, Test
	}

	public enum Category {
		IMAGE, LABEL
	}

	public MNIST() {

	}

	/**
	 * Reads all of the letters of a given dataset from the MNIST database and
	 * puts them into their correct folder.
	 */
	public static void saveToLetterDataFormat(Dataset dataset) {
		// Get Integer representations of byte data
		byte[] byteImageData = MNIST.getRawData(dataset, Category.IMAGE);
		int[] intImageData = new int[byteImageData.length];

		for (int i = 0; i < byteImageData.length; i++)
			intImageData[i] = MNIST.clean((int) byteImageData[i]);

		int numLetters = 0;
		String datasetFolder = "";

		// Determine the number of letters in file
		switch (dataset) {
		case Training:
			numLetters = TRAINING_SET_SIZE;
			datasetFolder = Constants.TRAINING_LETTERS_FOLDER;
			break;

		case Test:
			numLetters = TEST_SET_SIZE;
			datasetFolder = Constants.EXPERIMENTAL_LETTERS_FOLDER;
			break;
		}

		// Read through each letter and save it to a text file
		for (int letter = 0; letter < numLetters; letter++) {
			int startingImageLocation = MNIST.IMAGE_DATA_BEGINNING
					+ letter * MNIST.PIXELS_IN_LETTER;
			int endingImageLocation = startingImageLocation + MNIST.PIXELS_IN_LETTER;

			int[] curImageData = Arrays.copyOfRange(intImageData,
					startingImageLocation, endingImageLocation);

			LetterData currentLetter = new LetterData(curImageData);
			currentLetter.setCharacter(' ');
			currentLetter.save(datasetFolder);
		}
	}

	/** Scales an Integer from 1-255 to 0 or 1 */
	private static int clean(int i) {
		return (i < 122) ? 0 : 1;
	}

	private static byte[] getRawData(Dataset dataset, Category category) {
		String filePath = getFilePath(dataset, category);

		return FileManager.readByteFileContent(filePath);
	}

	private static String getFilePath(Dataset dataset, Category category) {
		String datasetName = "";

		switch (dataset) {
		case Training:
			datasetName += TRAINING_BASE_NAME;
			break;

		case Test:
			datasetName += TEST_BASE_NAME;
			break;
		}

		datasetName += '-';

		switch (category) {
		case IMAGE:
			datasetName += IMAGE_PART_NAME + IMAGE_FILE_ENDING;
			break;

		case LABEL:
			datasetName += LABEL_PART_NAME + LABEL_FILE_ENDING;
			break;
		}

		return Constants.RESOURCES_PATH + Constants.MNIST_DATASET_FOLDER
				+ datasetName;
	}

	public static void main(String[] args) {
		MNIST.saveToLetterDataFormat(Dataset.Test);
	}

}
