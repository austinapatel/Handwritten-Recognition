
/**
 * Author: Austin Patel
 * Project: Handwritten Recognition
 * File Name: Alphabet.java
 * Created: 01/02/17
 */

package data;

/** Functions for an alphabet. */
public class Alphabet {

	private final static String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	public Alphabet() {

	}

	public static String getAlphabet() {
		return ALPHABET;
	}

	public static int getLocation(char c) {
		return ALPHABET.indexOf(c);
	}

	public static char getCharacter(int index) {
		return ALPHABET.charAt(index);
	}

	public static int getLength() {
		return ALPHABET.length();
	}
	
	public static int indexOf(char c) {
		return Alphabet.ALPHABET.indexOf(c);
	}

	public static Character[] getCharacterArray() {
		Character[] alphabetList = new Character[Alphabet.getLength()];

		for (int i = 0; i < alphabetList.length; i++)
			alphabetList[i] = Alphabet.getCharacter(i);

		return alphabetList;
	}

}
