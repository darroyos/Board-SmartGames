package es.ucm.fdi.tp.was;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.tp.base.model.GameState;

/**
 * A WolfAndSheep state.
 * Describes a board of WolfAndSheep that is either being
 * played or is already finished.
 */
public class WolfAndSheepState extends GameState<WolfAndSheepState, WolfAndSheepAction> {
	
	private static final long serialVersionUID = 7837811407805484141L;
	
	private final int turn;
    private final boolean finished;
    private final int[][] board;
    private final int winner;

    public final static int WOLF = 0;
    public final static int SHEEP = 1;
    public final static int DIM = 8;
    public final static int EMPTY = -1;
    final static int OUTSIDE = -2;
    
    public WolfAndSheepState() {
    	super(2); // two players
    	
    	this.board = new int[DIM][DIM];
    	for (int i = 0; i < DIM; i++)
    		for (int j = 0; j < DIM; j++)
    			board[i][j] = EMPTY;
    	
    	// Put a Wolf on the board
    	board[DIM - 1][0] = 0;
    	
    	// Put the Sheeps on the board (odd numbers)
    	for (int i = 1; i < DIM; i += 2) board[0][i] = 1;
    	
    	this.turn = 0;
    	this.winner = -1;
    	this.finished = false;
    }
    
    /**
     * @param prev The previous WolfAndSheepState
     * @param board The board we want to set
     * @param finished Indicates if the game has ended
     * @param winner The winner player
     */
    public WolfAndSheepState(WolfAndSheepState prev, int[][] board, boolean finished, int winner) {
    	super(2); // two players
    	this.board = board;
    	this.turn = (prev.turn + 1) % 2;
    	this.finished = finished;
    	this.winner = winner;
    }
    
    @Override
    public List<WolfAndSheepAction> validActions(int playerNumber) {
        ArrayList<WolfAndSheepAction> valid = new ArrayList<>();
        if (finished) {
            return valid;
        }

        /*
         * Check the possible movements for every player
         */
        
        for (int row = 0; row < DIM; row++) {
            for (int col = 0; col < DIM; col++) {
            	
            	if (at(row, col) == playerNumber) {
            		
            		if (playerNumber == WOLF) {
            			// Wolf's possible movements / actions
            			// One diagonal square upwards and downwards movements 
            			
            			if (at(row - 1, col + 1) == EMPTY)
            				valid.add(new WolfAndSheepAction(playerNumber, row, col, row - 1, col + 1));
            			if (at(row - 1, col - 1) == EMPTY)
            				valid.add(new WolfAndSheepAction(playerNumber, row, col, row - 1, col - 1));
            		}           		
        			// Sheep's possible movements / actions
        			// Just downwards movements towards the wolf's initial position
        			
        			if (at(row + 1, col - 1) == EMPTY)
        				valid.add(new WolfAndSheepAction(playerNumber, row, col, row + 1, col - 1));
        			if (at(row + 1, col + 1) == EMPTY)
        				valid.add(new WolfAndSheepAction(playerNumber, row, col, row + 1, col + 1));
            	}
            }
        }
    
        return valid;
    }
   
    
    /**
     * Shows if a playerNumber is winner
     * 
     * @param playerNumber The player (0: wolf, 1: sheep)
     * @return true: winner, false: loser
     */
    public boolean isWinner(int playerNumber) {
    	boolean won = false;
    	
    	// The player who control the wolf wins when reaches the top of the board
       if (playerNumber == 0) {
    	   for (int i = 0; i < DIM && !won; i++)
    		   if (board[0][i] == 0) won = true;
    	   
    	   // The wolf also wins if the sheeps cannot make any action / move
    	   if (!won) won = validActions(1).isEmpty();
       }
       else
    	   // The player who control the sheeps wins when the wolf cannot make any action / move
    	   won = validActions(0).isEmpty();
       
       return won;
    }    
    
    
    /**
     * Returns the content of the asked coordinates
     * 
     * @param row X Axis
     * @param col Y Axis
     * @return the coords content
     */
    public int at(int row, int col) { 	
    	return row >= 0 && row < DIM && col >= 0 && col < DIM ? board[row][col] : OUTSIDE;
    }
    
    @Override
    public int getTurn() {
        return turn;
    }

    @Override
    public boolean isFinished() {
        return finished;
    }

    @Override
    public int getWinner() {
        return winner;
    }
    
    /**
     * Copies the board of a WolfAndSheep state
     * 
     * @return the board copy of a WolfAndSheep state
     */
    public int[][] getBoard() {
        int[][] copy = new int[DIM][DIM];
        
        for (int i = 0; i < DIM; i++) {
        	for (int j = 0; j < DIM; j++) {
        		copy[i][j] = board[i][j];
        	}
        }
        
        return copy;
    }
    
    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	
    	
    	/* Print the columns coords */
    	sb.append("   ");
    	for (int i = 0; i < DIM; i++) sb.append(" " + i + " ");
    	sb.append(System.lineSeparator());
    	sb.append("   ");
    	for (int i = 0; i < DIM; i++) sb.append("---");
    	sb.append(System.lineSeparator());
    	
    	
    	for (int i = 0; i < DIM; i++) {
    		sb.append(i + " |");	// Print the row coord
    		for (int j = 0; j < DIM; j++) {
    			
    			if (board[i][j] == EMPTY) {
    				if ((i + j) % 2 != 0) sb.append(" . "); // Just print the diagonal movs
    				else sb.append("   ");
    			}
    			else if (board[i][j] == 0) sb.append(" W ");
    			else sb.append(" S ");
    		}			
    		sb.append("|");
    		sb.append(System.lineSeparator());
    	}
    	
    	sb.append("   ");
    	for (int i = 0; i < DIM; i++) sb.append("---");
    	sb.append(System.lineSeparator());
    	
    	return sb.toString();
    }

}
 
