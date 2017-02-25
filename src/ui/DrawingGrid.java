
/**
 * Author: Austin Patel
 * Project: Handwritten Recognition
 * File Name: DrawingPane.java
 * Created: 01/02/17
 */

package ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JButton;
import javax.swing.SwingUtilities;

import data.Constants;

/**Adds user drawing functionality to a "ColorGrid".*/
@SuppressWarnings("serial")
public class DrawingGrid extends ColorGrid {
		
	private Color drawingColor;
	
	public DrawingGrid(int width, int height, Color drawingColor) {
		this(width,height,drawingColor, true);
	}

	public DrawingGrid(int width, int height, Color drawingColor, boolean showClearButton) {
		super(width, height);
		setSize(getWidth(), getHeight() + Constants.BUTTON_HEIGHT);
		
		this.drawingColor = drawingColor;
		
		if (showClearButton)
			addClearButton();
		
		addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent e) {
				
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				drawMouseEvent(e);
			}
		});
		
		addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				drawMouseEvent(e);
			}
		});
	}
	
	private void drawMouseEvent(MouseEvent e) {
		int posX = e.getX() / PIXEL_SIZE;
		int posY = e.getY() / PIXEL_SIZE;
		
		if (posX < width && posY < height && posX >= 0 && posY >= 0)
			drawPixel(posX, posY, (SwingUtilities.isLeftMouseButton(e)) ? Color.BLACK : null);
	}
	
	private void addClearButton() {
		JButton clearButton = new JButton("Clear");
		clearButton.setSize(getWidth(), Constants.BUTTON_HEIGHT);
		clearButton.setLocation(0, getHeight() - Constants.BUTTON_HEIGHT);
		clearButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				clear();				
			}
		});
		
		add(clearButton);
	}
	
	public Color getDrawingColor() {
		return drawingColor;
	}
	
}
