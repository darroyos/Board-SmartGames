package es.ucm.fdi.tp.view.gui;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;

import es.ucm.fdi.tp.base.Utils;
import es.ucm.fdi.tp.was.WolfAndSheepAction;
import es.ucm.fdi.tp.was.WolfAndSheepState;

public class WasView extends RectBoardGameView<WolfAndSheepState, WolfAndSheepAction> {
	
	private static final long serialVersionUID = 6482459627531550550L;
	
	private Color sheepColor = getColor();
	private Color wolfColor = getColor();
	
	private int sourceCol;
	private int sourceRow;
	private int destCol;
	private int destRow;
	private int player;
	private WolfAndSheepState state;
	private JMessagePanel statusPanel;
	
	public WasView(int player) {
		super(WolfAndSheepState.DIM, WolfAndSheepState.DIM);
		this.sourceCol = -1;
		this.sourceRow = -1;
		this.destCol = -1;
		this.destCol = -1;
		this.player = player;
		this.state = null;
		this.statusPanel = null;
	}
	

	@Override
	protected int getNumCols() {
		return WolfAndSheepState.DIM;
	}

	@Override
	protected int getNumRows() {
		return WolfAndSheepState.DIM;
	}

	@Override
	protected void mouseClicked(JBoardField field) {

		if (sourceFieldNotSelected()) {	// First click
			
			/* Get the source coordinates */
			this.sourceCol = field.getCoordX();
			this.sourceRow = field.getCoordY();
			
			disable(); // Disable all the fields
			field.setEnabled(true); // Enable the selected field
			List<WolfAndSheepAction> actions = state.validActions(player); // Get the valid actions list
			
			/* Just enable the fields which have valid actions for the source field */
			for (WolfAndSheepAction a : actions) {
				if (a.getSourceRow() == this.sourceRow && a.getSourceCol() == this.sourceCol) {
					getField(a.getRow(), a.getCol()).setEnabled(true);
					getField(a.getRow(), a.getCol()).setBorder(BorderFactory.createLineBorder(Color.BLUE));
				}
			}
			
			statusPanel.setStatusMessage("Selected (" + this.sourceCol + ", " + this.sourceRow + "). "
					+ "Click on destination field or ESC to cancel selection.");
		}
		
		else {	// Second click
			this.destCol = field.getCoordX();
			this.destRow = field.getCoordY();
			
			if (cancelMovement())
				update(state); // Board update
			else
				// Apply the movement
				windowCtrl.makeMove(new WolfAndSheepAction
						(this.player, this.sourceRow, this.sourceCol, destRow, destCol));
			
			// We're now ready for new clicks!
			restartCoordinates();
		}
		
	}

	@Override
	protected void keyHandler(int keyCode) {
		/* Just attend key actions when:
		 * 		- The ESC key was released
		 * 		- The GameView has the turn
		 * 		- A source field has been pressed before
		 */
		if (keyCode == KeyEvent.VK_ESCAPE && this.player == state.getTurn()
				&& this.sourceRow != -1 && this.sourceCol != -1) {
			update(state);
			restartCoordinates();
		}
	}
	
	@Override
	public void refresh() {
		update(state);
		restartCoordinates();
	}
	

	@Override
	public void update(WolfAndSheepState state) {
		this.state = state;
		
		disable(); // Disable all fields
		
		for (int row = 0; row < getNumRows(); row++) {
			for (int col = 0; col < getNumCols(); col++) {
				
				JBoardField field = getField(row, col);
				
				switch (state.at(row, col)) {
				
				case WolfAndSheepState.EMPTY:					
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
							
				case WolfAndSheepState.SHEEP:
					
					if (player == WolfAndSheepState.SHEEP && state.getTurn() == WolfAndSheepState.SHEEP && !state.isFinished())
						field.setEnabled(true);
						
					ImageIcon sheepIcon = new ImageIcon(Utils.loadImage("sheep.png"));
					field.setIcon(sheepIcon);
					field.setBackground(sheepColor);

					break;
					
				case WolfAndSheepState.WOLF:
					
					if (player == WolfAndSheepState.WOLF && state.getTurn() == WolfAndSheepState.WOLF && !state.isFinished())
						field.setEnabled(true);
						
					ImageIcon wolfIcon = new ImageIcon(Utils.loadImage("wolf.png"));
					field.setIcon(wolfIcon);
					field.setBackground(wolfColor);
					
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
	public void setInitialColors(JPlayerInfoTable<WolfAndSheepState, WolfAndSheepAction> playerInfo) {
		playerInfo.setInitialColor(WolfAndSheepState.WOLF, this.wolfColor);
		playerInfo.setInitialColor(WolfAndSheepState.SHEEP, this.sheepColor);
	}
	
	@Override
	public void changeColor(int player, Color color) {
		switch (player) {
		case WolfAndSheepState.WOLF:
			wolfColor = color;
			break;
		case WolfAndSheepState.SHEEP:
			sheepColor = color;
			break;
		}
	}
	
	
	private boolean sourceFieldNotSelected() {
		return this.sourceCol == -1 && this.sourceRow == -1;
	}
	
	private boolean cancelMovement() {
		return this.sourceCol == this.destCol && this.sourceRow == this.destRow;
	}
	
	private void restartCoordinates() {
		this.sourceCol = -1;
		this.sourceRow = -1;
		this.destCol = -1;
		this.destRow = -1;
	}
	
}
