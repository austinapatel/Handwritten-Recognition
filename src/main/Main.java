
/**
 * Author: Austin Patel
 * Project: Handwritten Recognition
 * File Name: Main.java
 * Created: 01/02/17
 */

package main;

import java.awt.Font;

import javax.swing.UIManager;

import ui.Interface;

/** Driver class */
public class Main {

	public Main() {
	}

	public static void main(String[] args) {
		Main.setUIFont (new javax.swing.plaf.FontUIResource("Arial", Font.BOLD,20));
		
		new Interface();
	}

	private static void setUIFont(javax.swing.plaf.FontUIResource f) {
		@SuppressWarnings("rawtypes")
		java.util.Enumeration keys = UIManager.getDefaults().keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value != null
					&& value instanceof javax.swing.plaf.FontUIResource)
				UIManager.put(key, f);
		}
	}

}
