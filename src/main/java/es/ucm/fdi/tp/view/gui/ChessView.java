package es.ucm.fdi.tp.view.gui;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;

import es.ucm.fdi.tp.base.Utils;
import es.ucm.fdi.tp.chess.ChessAction;
import es.ucm.fdi.tp.chess.ChessAction.Special;
import es.ucm.fdi.tp.chess.ChessBoard;
import es.ucm.fdi.tp.chess.ChessState;

public class ChessView extends RectBoardGameView<ChessState, ChessAction> {


	private static final long serialVersionUID = -6569805452054775668L;

	private Color blackPlayer = getColor();
	private Color whitePlayer = getColor();

	private int sourceCol;
	private int sourceRow;
	private int destCol;
	private int destRow;
	private int player;
	private ChessState chessState;
	private JMessagePanel statusPanel;

	public ChessView(ChessState chessState, int player) {
		super(chessState.getDimension(), chessState.getDimension());
		this.sourceCol = -1;
		this.sourceRow = -1;
		this.destCol = -1;
		this.destCol = -1;
		this.chessState = chessState;
		this.player = player;
		this.statusPanel = null;
	}

	@Override
	protected int getNumCols() {
		return chessState.getDimension();
	}

	@Override
	protected int getNumRows() {
		return chessState.getDimension();
	}

	@Override
	protected void mouseClicked(JBoardField field) {
		if (sourceFieldNotSelected()) {	// First click

			/* Get the source coordinates */
			this.sourceCol = field.getCoordX();
			this.sourceRow = field.getCoordY();

			disable(); // Disable all the fields
			field.setEnabled(true); // Enable the selected field
			List<ChessAction> actions = chessState.validActions(player); // Get the valid actions list

			/* Just enable the fields which have valid actions for the source field */
			for (ChessAction a : actions) {
				if (a.getSrcRow() == this.sourceRow && a.getSrcCol() == this.sourceCol) {
					getField(a.getDstRow(), a.getDstCol()).setEnabled(true);
					Color color = new Color(190, 190, 190);
					getField(a.getDstRow(), a.getDstCol()).setBorder(BorderFactory.createLineBorder(Color.GRAY));
					getField(a.getDstRow(), a.getDstCol()).setBackground(color);
				}
			}

			statusPanel.setStatusMessage("Selected (" + this.sourceCol + ", " + this.sourceRow + "). "
					+ "Click on destination field or ESC to cancel selection.");
		}

		else {	// Second click
			this.destCol = field.getCoordX();
			this.destRow = field.getCoordY();

			if (cancelMovement())
				update(chessState); // Board update
			else if (this.destRow == 0) {
				windowCtrl.makeMove(new ChessAction(this.player, this.sourceRow, this.sourceCol, destRow, destCol, Special.QueenQ));
			}
			else {
				// Apply the movement
				windowCtrl.makeMove(new ChessAction
						(this.player, this.sourceRow, this.sourceCol, destRow, destCol));
			}			

			// We're now ready for new clicks!
			restartCoordinates();
		}
	}

	@Override
	public void update(ChessState state) {
		this.chessState = state;

		disable();

		for (int row = 0; row < getNumRows(); row++) {
			for (int col = 0; col < getNumCols(); col++) {

				JBoardField field = getField(row, col);

				byte piece = state.getBoard().get(row, col);

				/* We fetch the icon URL */
				String iconUrl = "chess/" + ChessBoard.Piece.iconName(piece);

				/* We just set the icon for the non-empty fields */
				if (!iconUrl.equals("chess/._w.png")) {
					field.setIcon(new ImageIcon(Utils.loadImage(iconUrl)));
				}

				if (!ChessBoard.empty(piece)) {

					if (ChessBoard.black(piece)) {
						field.setBackground(blackPlayer);
					}
					else {
						field.setBackground(whitePlayer);
					}

					if (ChessBoard.sameTurn(piece, chessState.getTurn())) {
						field.setEnabled(true);
					}
				}
				else {
					field.setIcon(null);

					if (row % 2 == 0 && col % 2 == 0) 
						field.setBackground(Color.BLACK);

					/* Black fields on odd columns and rows */
					else if ((row + 1) % 2 == 0 && (col + 1) % 2 == 0)
						field.setBackground(Color.BLACK);

					/* White fields */
					else
						field.setBackground(null);
				}
			}
		}

		if (state.getTurn() == player)
			statusPanel.setStatusMessage("Your turn. Click on a source field.");
		else
			statusPanel.setStatusMessage("Turn for player " + state.getTurn());
	}

	@Override
	public void changeColor(int player, Color color) {
		switch (player) {
		case ChessState.BLACK:
			blackPlayer = color;
			break;
		case ChessState.WHITE:
			whitePlayer = color;
			break;
		}
	}

	@Override
	public void refresh() {
		update(chessState);
		restartCoordinates();
	}

	@Override
	protected void keyHandler(int keyCode) {
		/* Just attend key actions when:
		 * 		- The ESC key was released
		 * 		- The GameView has the turn
		 * 		- A source field has been pressed before
		 */
		if (keyCode == KeyEvent.VK_ESCAPE && this.player == chessState.getTurn()
				&& this.sourceRow != -1 && this.sourceCol != -1) {
			update(chessState);
			restartCoordinates();
		}
	}

	@Override
	public void setMessagePanel(JMessagePanel panel) {
		this.statusPanel = panel;		
	}

	@Override
	public void setInitialColors(JPlayerInfoTable<ChessState, ChessAction> playerInfo) {
		playerInfo.setInitialColor(ChessState.BLACK, this.blackPlayer);
		playerInfo.setInitialColor(ChessState.WHITE, this.whitePlayer);
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
