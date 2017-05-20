package es.ucm.fdi.tp.view.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.extra.jcolor.ColorChooser;

public class JPlayerInfoTable<S extends GameState<S, A>, A extends GameAction<S, A>> extends JPanel {
	
	private static final long serialVersionUID = 3194399250650600708L;
	private PlayersTableModel tModel;
	private Map<Integer, Color> colors; // Line -> Color
	private ColorChooser colorChooser;
	private GameView<S, A> view;
	private boolean initialized;
	
	public JPlayerInfoTable(GameView<S, A> view) {
		this.view = view;
		
		/* We call the initGUI method in notifyEvent() because we don't know
		 * how many players the game will have until that point
		 */
		
		this.initialized = false;
	}
	
	public void initGUI(int numPlayers) {
		this.initialized = true;
		
		colors = new HashMap<>();
		colorChooser = new ColorChooser(new JFrame(), "Choose Line Color", Color.BLACK);
		
		// names table
		tModel = new PlayersTableModel(numPlayers);
		tModel.getRowCount();
		JTable table = new JTable(tModel) {
			private static final long serialVersionUID = 1L;

			// THIS IS HOW WE CHANGE THE COLOR OF EACH ROW
			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
				Component comp = super.prepareRenderer(renderer, row, col);

				// the color of row 'row' is taken from the colors table, if
				// 'null' setBackground will use the parent component color.
				if (col == 1)
					comp.setBackground(colors.get(row));
				else
					comp.setBackground(Color.WHITE);
				comp.setForeground(Color.BLACK);
				return comp;
			}
		};
		
		table.setToolTipText("Click on a row to change the color of a player");
		table.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				int row = table.rowAtPoint(evt.getPoint());
				int col = table.columnAtPoint(evt.getPoint());
				if (row >= 0 && col >= 0 && col != 2) {
					changeColor(row);
				}
			}

		});

		JScrollPane scrollTable = new JScrollPane(table);
		scrollTable.setPreferredSize(new Dimension(250, 100));
		table.setFocusable(false); // to avoid problems with the JFrame KeyListener
		this.add(scrollTable);
	}
	
	public void setInitialColor(int row, Color color) {
		colors.put(row, color);
		repaint();
	}
	
	public boolean isInitialized() {
		return this.initialized;
	}
	
	private void changeColor(int row) {
		colorChooser.setSelectedColorDialog(colors.get(row));
		colorChooser.openDialog();
		if (colorChooser.getColor() != null) {
			colors.put(row, colorChooser.getColor());
			repaint();
			view.changeColor(row, colorChooser.getColor());
			view.refresh();
		}

	}
	
	private class PlayersTableModel extends AbstractTableModel {
		
		private static final long serialVersionUID = 5706526962020791870L;
		private String[] colNames;
		private final List<String> players = new ArrayList<>();
		
		public PlayersTableModel(int numPlayers) {
			this.colNames = new String[] { "#Player", "Color" };
			for (int i = 0; i < numPlayers; i++)
				players.add("");
		}

		@Override
		public String getColumnName(int col) {
			return colNames[col];
		}

		@Override
		public int getColumnCount() {
			return colNames.length;
		}

		@Override
		public int getRowCount() {
			return players != null ? players.size() : 0;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			if (columnIndex == 0) {
				return rowIndex;
			} else {
				return players.get(rowIndex);
			}
		}

	}
	
}