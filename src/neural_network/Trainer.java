
/**
 * Author: Austin Patel
 * Project: Handwritten Recognition
 * File Name: Trainer.java
 * Created: 01/01/17
 */

package neural_network;

import java.util.ArrayList;

import data.Alphabet;
import data.Constants;
import data.LetterData;

/**Uses training "LetterData" as inputs to teach the neural network.*/
public class Trainer {
	
	private NeuralNetwork neuralNetwork;
	
	public Trainer(NeuralNetwork neuralNetwork) {
		this.neuralNetwork = neuralNetwork;
	}
	
	public void trainNetwork() {
		ArrayList<LetterData> trainingData = new ArrayList<LetterData>();
		
		for (char c : Alphabet.getAlphabet().toCharArray()) {
			String filePath = Constants.RESOURCES_PATH + Constants.TRAINING_LETTERS_FOLDER + "\\" + c + ".txt";
			LetterData[] currentLetterData = LetterData.getLetterData(filePath, c);
			
			for (LetterData letterData : currentLetterData)
				trainingData.add(letterData);
		}
		
		ArrayList<LetterData> shuffledData = new ArrayList<LetterData>();
		
		// Shuffle the data so that the trainer doesn't learn one character at a time and then forget it
		// Need to make the training data in a constant order while actually experimenting
		while (!trainingData.isEmpty())
			shuffledData.add(trainingData.remove((int) (Math.random() * trainingData.size())));
		
		for (LetterData letterData : shuffledData) {
			int[] actualValues = new int[Alphabet.getAlphabet().length()];
			
			for (int i = 0; i < actualValues.length; i++)
				actualValues[i] = 0;
			
			actualValues[Alphabet.getLocation(letterData.getCharacter())] = 1;
			
			neuralNetwork.learn(letterData.getData1D(), actualValues);
		}
	}

}
