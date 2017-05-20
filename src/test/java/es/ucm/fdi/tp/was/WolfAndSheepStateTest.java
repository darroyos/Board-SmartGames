package es.ucm.fdi.tp.was;

import static org.junit.Assert.*;

import org.junit.Test;

import es.ucm.fdi.tp.was.WolfAndSheepState;

/**
 * Wolf and Sheep tests 
 */
public class WolfAndSheepStateTest {

	/**
	 * Check the fact that when a wolf is blocked the sheeps win
	 */
	@Test
	public void blockedWolf() {
		WolfAndSheepState blockedWolfState = new WolfAndSheepState();
		
		int[][] board = blockedWolfState.getBoard();
		// Make some movements
		board[0][1] = -1;
		// Block the wolf's possible actions
		board[WolfAndSheepState.DIM - 2][1] = 1;
		// Create the new game state
		blockedWolfState = new WolfAndSheepState(blockedWolfState, board, false, -1);
		
		// Sheeps should win
		assertEquals("Sheeps win", true, blockedWolfState.isWinner(1));
	}
	
	/**
	 * Place the wolf at y = 0 and check if it's the winner
	 */
	@Test
	public void wolfAtFirstRow() {
		WolfAndSheepState wolfAtFirstRowState = new WolfAndSheepState();
		
		int[][] board = wolfAtFirstRowState.getBoard();
		// Place the wolf at y = 0
		board[0][1] = 0;
		// Create the new game state
		wolfAtFirstRowState = new WolfAndSheepState(wolfAtFirstRowState, board, false, -1);
		
		// Wolf should win
		assertEquals("Wolf wins", true, wolfAtFirstRowState.isWinner(0));
	}
	
	/**
	 * The wolf at its initial position just has one valid action; after applying it,
	 * in the next turn, it has four valid actions
	 */
	@Test
	public void wolfIntialPosActions() {
		WolfAndSheepState initialState= new WolfAndSheepState();
		
		assertEquals("Just one valid action", 1, initialState.validActions(0).size());
		
		// Apply the available action and check again the amount of available actions
		assertEquals("Four valid actions", 4, 
				initialState.validActions(0).get(0).applyTo(initialState).validActions(0).size());
	}
	
	/**
	 * A sheep at its initial position just has two valid actions; sidewards has one valid action
	 */
	@Test
	public void sheepInitialPosActions() {
		WolfAndSheepState initialState= new WolfAndSheepState();
		
		int[][] board = initialState.getBoard();
		// Clear the first row except the first sheep
		for (int i = 2; i < WolfAndSheepState.DIM; i++)
			board[0][i] = WolfAndSheepState.EMPTY;
		
		// Create the new state
		initialState = new WolfAndSheepState(initialState, board, false, -1);
		assertEquals("Just two valid action", 2, initialState.validActions(1).size());
		
		// Remove the first sheep
		board[0][1] = WolfAndSheepState.EMPTY;
		// Place a sheep sidewards
		board[0][WolfAndSheepState.DIM - 1] = 1;		
		
		assertEquals("Sidewards just two valid actions", 1, initialState.validActions(1).size());
	}	
	
}
