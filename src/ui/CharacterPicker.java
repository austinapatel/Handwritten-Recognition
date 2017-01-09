
/**
 * Author: Austin Patel
 * Project: Handwritten Recognition
 * File Name: CharacterPicker.java
 * Created: 01/07/17
 */

package ui;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import data.Alphabet;

/**
 * Implementation of letter picker that allows for selection a single letter
 * from the alphabet. Usage: 1) Override onValueChange method 2) set size,
 * location and background color
 */
@SuppressWarnings("serial")
public abstract class CharacterPicker extends JList<Character> {

	public enum Style {
		Grid, List
	}

	private Style style;

	public CharacterPicker(Style style) {
		super(Alphabet.getCharacterArray());

		this.style = style;

		initialize();
	}

	private void initialize() {
		setSelectedIndex(0);
		
		switch (style) {
		case Grid:
			setLayoutOrientation(JList.HORIZONTAL_WRAP);
			break;

		case List:
			break;
		}
		
		setVisibleRowCount(-1);
		setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
		
		addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				onValueChange(e);
			}
		});
	}

	public abstract void onValueChange(ListSelectionEvent e);

	public int getPrefferedHeight() {
		DefaultListCellRenderer characterPickerRenderer = (DefaultListCellRenderer) getCellRenderer();
		characterPickerRenderer
				.setHorizontalAlignment(DefaultListCellRenderer.CENTER);
				
		return (int) (characterPickerRenderer.getPreferredSize().getHeight());
	}

}
