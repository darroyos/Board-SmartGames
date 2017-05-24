package es.ucm.fdi.tp.extra.jButtonBoard;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public abstract class JButtonBoard extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4518722262994516431L;

	private JButton btnArray[][];

	public JButtonBoard() {
		fillBoard();
	}

	public void fillBoard() {
		// Si el tablero tenía algún componente, lo eliminamos
		this.removeAll();

		int cols = getNumCols();
		int rows = getNumRows();
		if (cols <= 0 || rows <= 0) {
			JLabel lbl = new JLabel("Waiting for game to start!");
			lbl.setForeground(Color.red);
			this.add(lbl);
			return;
		}
		
		int sep = getSepPixels();
		setLayout(new GridLayout(rows, cols,sep,sep));

		btnArray = new JButton[rows][cols];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				final int row = i;  // Variables final necesarias para el
				final int col = j;  // ActionListener. 
				btnArray[row][col] = new JButton("");
				Integer pos = getPosition(row,col);
				if (pos != null) {
					btnArray[row][col].setBackground(getColor(pos));
					btnArray[row][col].setIcon(getIcon(pos));
				} else {
					btnArray[row][col].setBackground(getBackground(row,col));
				}
				btnArray[row][col].addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						// Aquí  no ha sido necesario utilizar una clase que extienda JButton
						// porque utilizamos dos variables locales final para indicar fila y columna.
						// Pero puedes hacerlo si te parece más claro.
						JButtonBoard.this.cellClicked(row, col);
					}

				});
				this.add(btnArray[row][col]);
			}
		}

		this.setPreferredSize(new Dimension(400,400));
	}

	protected abstract void cellClicked(int row, int col);

	protected abstract Color getColor(int player);
	
	protected abstract ImageIcon getIcon(int player);
	
	protected abstract Integer getPosition(int row, int col);

	protected abstract Color getBackground(int row, int col);

	protected abstract int getNumRows();

	protected abstract int getNumCols();

	protected int getSepPixels() {
		return 2;
	}

}
