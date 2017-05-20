package es.ucm.fdi.tp.view.gui;

import java.awt.Color;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;

import es.ucm.fdi.tp.base.Utils;
import es.ucm.fdi.tp.ttt.TttAction;
import es.ucm.fdi.tp.ttt.TttState;

public class TttView extends RectBoardGameView<TttState, TttAction> {
	
	private static final long serialVersionUID = 8824795460166451144L;
	
	private Color playerOneColor = getColor();
	private Color playerTwoColor = getColor();
	
	private int row;
	private int col;
	private int player;
	private TttState state;
	private JMessagePanel statusPanel;
	
	private final static int EMPTY = -1;
	private final static int PLAYER_ONE = 0;
	private final static int PLAYER_TWO = 1;
	
	public TttView(int dim, int player) {
		super(dim, dim);
		this.row = -1;
		this.col = -1;
		this.player = player;
		this.state = null;
		this.statusPanel = null;

	}
	
	@Override
	protected int getNumCols() {
		return this.state.getBoard().length;
	}

	@Override
	protected int getNumRows() {
		return this.state.getBoard().length;
	}

	@Override
	protected void mouseClicked(JBoardField field) {
			
		/* Get the source coordinates */
		this.col = field.getCoordX();
		this.row = field.getCoordY();
		
		disable(); // Disable all the fields
		field.setEnabled(true); // Enable the selected field
		List<TttAction> actions = state.validActions(player); // Get the valid actions list
		
		/* Just enable the fields which have valid actions for the source field */
		for (TttAction a : actions) {
			if (a.getRow() == this.row && a.getCol() == this.col) {
				getField(a.getRow(), a.getCol()).setEnabled(true);
				getField(a.getRow(), a.getCol()).setBorder(BorderFactory.createLineBorder(Color.BLUE));
			}
		}
		
		statusPanel.setStatusMessage("You have placed a " + (this.player == PLAYER_ONE ? "cross X" : "circle O"));
		
		// Apply the movement
		windowCtrl.makeMove(new TttAction
					(this.player, this.row, this.col));
		
	}

	@Override
	protected void keyHandler(int keyCode) {
	}
	
	@Override
	public void refresh() {
		update(state);
	}

	@Override
	public void update(TttState state) {
		this.state = state;
		
		disable(); // Disable all fields
		
		for (int row = 0; row < getNumRows(); row++) {
			for (int col = 0; col < getNumCols(); col++) {
				
				JBoardField field = getField(row, col);
				
				switch (state.at(row, col)) {
				
				case EMPTY:
					if (!state.isFinished())
						field.setEnabled(true);
					
					field.setIcon(null);
					
					if (row % 2 == 0 && col % 2 == 0) 
						field.setBackground(Color.BLACK);

					/* Black fields on odd columns and rows */
					else if ((row + 1) % 2 == 0 && (col + 1) % 2 == 0)
						field.setBackground(Color.BLACK);
					
					/* White fields */
					else
						field.setBackground(null);
		
					break;	
							
				case PLAYER_ONE:
					
					if (player == PLAYER_ONE && state.getTurn() == PLAYER_ONE)
						field.setEnabled(false);
						
					ImageIcon crossIcon = new ImageIcon(Utils.loadImage("cross.png"));
					field.setIcon(crossIcon);
					field.setDisabledIcon(crossIcon);
					field.setBackground(playerOneColor);

					break;
					
				case PLAYER_TWO:
					
					if (player == PLAYER_TWO && state.getTurn() == PLAYER_TWO)
						field.setEnabled(false);
						
					ImageIcon circleIcon = new ImageIcon(Utils.loadImage("circle.png"));
					field.setIcon(circleIcon);
					field.setDisabledIcon(circleIcon);
					field.setBackground(playerTwoColor);

					break;
				}
				
			}
		}
		
		if (state.getTurn() == player)
			statusPanel.setStatusMessage("Your turn. Click on a source field.");
		else
			statusPanel.setStatusMessage("Turn for player " + state.getTurn());
	}
	
	@Override
	public void setMessagePanel(JMessagePanel panel) {
		this.statusPanel = panel;
	}
	
	@Override
	public void setInitialColors(JPlayerInfoTable<TttState, TttAction> playerInfo) {
		playerInfo.setInitialColor(PLAYER_ONE, this.playerOneColor);
		playerInfo.setInitialColor(PLAYER_TWO, this.playerTwoColor);
	}
	
	@Override
	public void changeColor(int player, Color color) {
		switch (player) {
		case PLAYER_ONE:
			playerOneColor = color;
			break;
		case PLAYER_TWO:
			playerTwoColor = color;
			break;
		}
	}
	
}
