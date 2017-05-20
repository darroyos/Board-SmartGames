package es.ucm.fdi.tp.was;

import es.ucm.fdi.tp.base.model.GameAction;

/**
 * An action for WolfAndSheep.
 */
public class WolfAndSheepAction implements GameAction<WolfAndSheepState, WolfAndSheepAction> {
	
	private static final long serialVersionUID = -7393105354486248741L;
	private int player;
	private int previousRow;
	private int previousCol;
    private int row;
    private int col;
    
    /**
     * @param player The player who is making the action / movement
     * @param previousRow The player's previous row
     * @param previousCol The player's previous column
     * @param row The affected row
     * @param col The affected column
     */
    public WolfAndSheepAction(int player, int previousRow, int previousCol, int row, int col) {
        this.player = player;
        this.previousRow = previousRow;
        this.previousCol = previousCol;
        this.row = row;
        this.col = col;
    }

    @Override
    public int getPlayerNumber() {
        return player;
    }

    @Override
    public WolfAndSheepState applyTo(WolfAndSheepState state) {
        if (player != state.getTurn()) {
            throw new IllegalArgumentException("Not the turn of this player");
        }

        // Take the board
        int[][] board = state.getBoard();
        
        // We need to update the board deleting the player's previous position
       board[previousRow][previousCol] = WolfAndSheepState.EMPTY;
        
        // Make move
        board[row][col] = player;

        // Update state
        WolfAndSheepState next;
        
        // Has the player won?
        if (new WolfAndSheepState(state, board, false, -1).isWinner(state.getTurn())) {
            next = new WolfAndSheepState(state, board, true, state.getTurn());
        } else {
            next = new WolfAndSheepState(state, board, false, -1);
        }
        return next;
    }

    /**
     * @return The action's row
     */
    public int getRow() {
        return row;
    }
    
    /**
     * @return The action's source row
     */
    public int getSourceRow() {
    	return previousRow;
    }
    
    /**
     * @return The action's source col
     */
    public int getSourceCol() {
    	return previousCol;
    }

    /**
     * @return The action's column
     */
    public int getCol() {
        return col;
    }

    @Override
    public String toString() {
        return "place " + player + " from (" + previousRow + ", " + previousCol + ") to (" + row + ", " + col + ")";
    }
    
    
    public boolean equals(WolfAndSheepAction action) {
    	return this.player == action.player && this.previousRow == action.previousRow &&
    			this.previousCol == action.previousCol && this.row == action.row &&
    			this.col == action.col;
    }
    
}
