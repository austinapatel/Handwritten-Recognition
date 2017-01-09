
/**
 * Author: Austin Patel
 * Project: Handwritten Recognition
 * File Name: ColorGrid.java
 * Created: 01/02/17
 */

package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

/** Displays a grid with a given size with drawing functionality. */
@SuppressWarnings("serial")
public class ColorGrid extends JPanel {

	protected static int PIXEL_SIZE = 35;

	protected int width, height;
	protected Color[][] colors;

	public ColorGrid(int width, int height) {
		setLayout(null);
		
		this.width = width;
		this.height = height;

		colors = new Color[width][height];

		setSize(width * PIXEL_SIZE + 1, height * PIXEL_SIZE + 1);
		setVisible(true);
	}

	@Override
	public void paintComponent(Graphics graphics) {
		Graphics2D graphics2d = (Graphics2D) graphics;

		graphics2d.setColor(getBackground());
		graphics2d.fillRect(0, 0, getWidth(), getHeight());
		
		graphics2d.setColor(Color.BLACK);

		for (int x = 0; x <= width; x++)
			graphics2d.drawLine(x * PIXEL_SIZE, 0, x * PIXEL_SIZE, height * PIXEL_SIZE);

		for (int y = 0; y <= height; y++)
			graphics2d.drawLine(0, y * PIXEL_SIZE, width * PIXEL_SIZE, y * PIXEL_SIZE);

		for (int x = 0; x < width; x++)
			for (int y = 0; y < height; y++) {
				if (colors[x][y] != null) {
					graphics2d.setColor(colors[x][y]);
					graphics2d.fillRect(x * PIXEL_SIZE + 1, y * PIXEL_SIZE + 1,
							PIXEL_SIZE - 1, PIXEL_SIZE - 1);
				}

			}
	}
	
	/**Determines whether there are no colors drawn to the grid.*/
	public static boolean isEmpty(Color[][] colorData, int width, int height) {
		for (int x = 0; x < width; x++)
			for (int y = 0; y < height; y++)
				if (colorData[x][y] != null)
					return false;
		
		return true;
	}
	
	public boolean isEmpty() {
		return isEmpty(colors, width, height);
	}

	public void drawPixel(int x, int y, Color color) {
		colors[x][y] = color;

		repaint();
	}
	
	public Color[][] getColors() {
		return colors;
	}
	
	public void setColors(Color[][] colors) {
		this.colors = colors;
		
		repaint();
	}
	
	public void clear() {
		colors = new Color[width][height];
		
		repaint();
	}
	
	public int getPixelWidth() {
		return width;
	}
	
	public int getPixelHeight() {
		return height;
	}

}
