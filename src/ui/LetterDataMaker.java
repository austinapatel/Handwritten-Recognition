
/**
 * Author: Austin Patel
 * Project: Handwritten Recognition
 * File Name: LetterDataMaker.java
 * Created: 01/03/17
 */

package ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import data.Constants;
import data.FileManager;
import data.LetterData;
import ui.CharacterPicker.Style;

/**
 * Displays an interface for creating/viewing stock, experimental and training
 * letters.
 */
@SuppressWarnings("serial")
public class LetterDataMaker extends JPanel {

	private final int CONTROL_COLUMN_WIDTH = 200;
	private final boolean ALLOW_STOCK_EDITTING = false;

	private DrawingGrid drawingGrid;
	private CharacterPicker characterPicker;
	private JButton viewButton, saveButton, deleteButton;
	private JList<String> datasetPicker;
	private JComboBox<Integer> indexPicker;

	public LetterDataMaker() {
		setLayout(null);

		initializeDrawingGrid();
		initializeDatasetPicker();
		initializeCharacterPicker();
		initializeIndexPicker();
		initilaizeViewButton();
		initializeSaveButton();
		initializeDeleteButton();

		initializeFrame();
	}

	private void initializeDrawingGrid() {
		drawingGrid = new DrawingGrid(Constants.GRID_WIDTH,
				Constants.GRID_HEIGHT, Color.BLACK);
		drawingGrid.setLocation(0, 0);
		add(drawingGrid);
	}

	private void initializeDatasetPicker() {
		datasetPicker = new JList<String>(
				new String[] { Constants.STOCK_LETTERS_FOLDER,
						Constants.EXPERIMENTAL_LETTERS_FOLDER,
						Constants.TRAINING_LETTERS_FOLDER });
		datasetPicker.setLocation(
				(int) (drawingGrid.getX() + drawingGrid.getWidth()), 0);
		datasetPicker.setBackground(getBackground());
		datasetPicker
				.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
		datasetPicker.setSelectedIndex(0);

		DefaultListCellRenderer datasetPickerRenderer = (DefaultListCellRenderer) datasetPicker
				.getCellRenderer();
		datasetPickerRenderer
				.setHorizontalAlignment(DefaultListCellRenderer.CENTER);
		datasetPicker.setSize(CONTROL_COLUMN_WIDTH,
				(int) (datasetPickerRenderer.getPreferredSize().getHeight())
						* 3);

		datasetPicker.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				String filePath = getCurrentPath();
				String[] fileContent = FileManager.readFileContent(filePath);

				if (fileContent.length == 0)
					viewButton.setEnabled(false);
				else {
					viewButton.setEnabled(true);
					updateIndexPickerData();
				}

				if (datasetPicker.getSelectedValue()
						.equals(Constants.STOCK_LETTERS_FOLDER)) {
					saveButton.setEnabled(false);
					indexPicker.setEnabled(false);
					deleteButton.setEnabled(false);
				} else {
					saveButton.setEnabled(true);
					indexPicker.setEnabled(true);
					deleteButton.setEnabled(true);

					// Lets the character picker determine whether the choice is
					// valid
					updateControlVisibility();
				}

				if (ALLOW_STOCK_EDITTING)
					saveButton.setEnabled(true);
			}
		});

		add(datasetPicker);
	}

	private void initializeCharacterPicker() {
		characterPicker = new CharacterPicker(Style.Grid) {
			@Override
			public void onValueChange(ListSelectionEvent e) {
				updateControlVisibility();
			}
		};

		characterPicker.setLocation(datasetPicker.getX(),
				datasetPicker.getY() + datasetPicker.getHeight());
		characterPicker.setBackground(getBackground());
		characterPicker.setSize(CONTROL_COLUMN_WIDTH,
				characterPicker.getPrefferedHeight() * 3);

		add(characterPicker);
	}

	private void initializeSaveButton() {
		saveButton = new JButton("Save");
		saveButton.setLocation(viewButton.getX(),
				viewButton.getY() + viewButton.getHeight());
		saveButton.setSize(CONTROL_COLUMN_WIDTH, Constants.BUTTON_HEIGHT);
		saveButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String filePath = getCurrentPath();
				String content = LetterData.colorsToText(
						drawingGrid.getColors(), drawingGrid.getDrawingColor());

				// Don't let the user save a letter if it is blank
				if (!content.contains("1"))
					return;
				
				FileManager.writeFileContent(filePath, content,
						!datasetPicker.getSelectedValue()
								.equals(Constants.STOCK_LETTERS_FOLDER));

				updateIndexPickerData();

				indexPicker.setEnabled(true);
				viewButton.setEnabled(true);
				deleteButton.setEnabled(true);

				indexPicker.setSelectedIndex(indexPicker.getItemCount() - 1);
				drawingGrid.clear();
			}
		});
		saveButton.setEnabled(ALLOW_STOCK_EDITTING);

		add(saveButton);
	}

	private void initilaizeViewButton() {
		viewButton = new JButton("View");
		viewButton.setLocation(indexPicker.getX(),
				indexPicker.getY() + indexPicker.getHeight());
		viewButton.setSize(CONTROL_COLUMN_WIDTH, Constants.BUTTON_HEIGHT);
		viewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String[] fileContent = FileManager
						.readFileContent(getCurrentPath());

				drawingGrid.setColors(LetterData.textToColorArray(
						fileContent[(int) indexPicker.getSelectedItem() - 1],
						Constants.GRID_WIDTH, Color.BLACK));
			}
		});
		add(viewButton);
	}

	private void initializeDeleteButton() {
		deleteButton = new JButton("Delete") {
			{
				setLocation(saveButton.getX(),
						saveButton.getY() + saveButton.getHeight());
				setSize(saveButton.getWidth(), Constants.BUTTON_HEIGHT);
				addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						FileManager.deleteLine(getCurrentPath(),
								(int) indexPicker.getSelectedItem());

						updateIndexPickerData();
						updateControlVisibility();
					}
				});
				setEnabled(false);
			}
		};

		add(deleteButton);
	}

	private String getCurrentPath() {
		return LetterData.getDestinationPath(datasetPicker.getSelectedValue(),
				characterPicker.getSelectedValue());
	}

	private void updateControlVisibility() {
		String[] fileContent = FileManager.readFileContent(getCurrentPath());

		if (fileContent.length == 0) {
			viewButton.setEnabled(false);
			indexPicker.setEnabled(false);
			deleteButton.setEnabled(false);
		} else {
			viewButton.setEnabled(true);
			indexPicker.setEnabled(!datasetPicker.getSelectedValue()
					.equals(Constants.STOCK_LETTERS_FOLDER));
			deleteButton.setEnabled(indexPicker.isEnabled());

			updateIndexPickerData();
		}

		if (ALLOW_STOCK_EDITTING)
			saveButton.setEnabled(true);
	}

	private void initializeIndexPicker() {
		// Initialize the index picker
		indexPicker = new JComboBox<Integer>(new Integer[0]) {
			{
				setLocation(characterPicker.getX(),
						characterPicker.getY() + characterPicker.getHeight());
				setEnabled(false);
			}
		};

		updateIndexPickerData();

		add(indexPicker);
	}

	private void updateIndexPickerData() {
		// Determine the data to put in the index picker
		String[] fileContent = FileManager.readFileContent(getCurrentPath());

		Integer[] indexes = new Integer[fileContent.length];

		for (int i = 0; i < indexes.length; i++)
			indexes[i] = i + 1;

		DefaultComboBoxModel<Integer> model = new DefaultComboBoxModel<>(
				indexes);
		indexPicker.setModel(model);

		DefaultListCellRenderer cellRenderer = (DefaultListCellRenderer) characterPicker
				.getCellRenderer();
		indexPicker.setSize(CONTROL_COLUMN_WIDTH,
				(int) cellRenderer.getPreferredSize().getHeight());

		indexPicker.setSelectedIndex(indexPicker.getItemCount() - 1);
	}

	private void initializeFrame() {
		int frameWidth = (int) (viewButton.getX() + viewButton.getWidth());
		int frameHeight = drawingGrid.getHeight();
		setSize(frameWidth, frameHeight);
		setVisible(true);
	}

}
