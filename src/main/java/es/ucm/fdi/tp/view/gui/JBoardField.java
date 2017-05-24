package es.ucm.fdi.tp.view.gui;

import java.awt.Color;

import javax.swing.*;

public class JBoardField extends JButton {
	
	
	private static final long serialVersionUID = -2458407282715384853L;
	private int coordX;
	private int coordY;
	
	
	public JBoardField(int x, int y, ImageIcon icon, Color color) {
		this(x, y);
		
		this.setIcon(icon);
		this.setBackground(color);
	}
	
	public JBoardField(int x, int y, Color color) {
		this(x, y);
		
		this.setBackground(color);
	}
	
	public JBoardField(int x, int y) {
		this.coordX = x;
		this.coordY = y;
		
		this.setBorder(null); // remove the border
		this.setFocusable(false); // otherwise KeyListener wouldn't work
	}

	public int getCoordX() {
		return coordX;
	}

	public int getCoordY() {
		return coordY;
	}
	
}
