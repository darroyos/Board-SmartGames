package es.ucm.fdi.tp.launcher;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.tp.base.console.ConsolePlayer;
import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GamePlayer;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.base.player.RandomPlayer;
import es.ucm.fdi.tp.base.player.SmartPlayer;
import es.ucm.fdi.tp.ttt.TttState;
import es.ucm.fdi.tp.was.WolfAndSheepState;

public class MainPr4 {
	private static java.util.Scanner sc = new java.util.Scanner(System.in);

	
	/**
	 * Creates the initial state for a given game name
	 * @param gameName The game name for which we'll create a initial state
	 * @return the initial game state
	 */
	public static GameState<?,?> createInitialState(String gameName) {
		GameState<?, ?> state = null;
		
		switch (gameName) {
	        case "ttt":
	            state = new TttState(3);
	            break;
	        case "was":
	            state =  new WolfAndSheepState();
	            break;
		}
		
		return state;
	}
	
	/**
	 * Creation of player
	 * @param gameName The game which is playing the player
	 * @param playerType The player type (console, random or smart)
	 * @param playerName The player name (maybe an index)
	 * @return A player
	 */
	public static GamePlayer createPlayer(String gameName, String playerType, String playerName) {
		GamePlayer player = null;
		
		switch (playerType) {
	        case "console":
	            player = new ConsolePlayer(gameName + "(" + playerName + ")", sc);
	            break;
	        case "random":
	            player = new RandomPlayer(gameName + "(" + playerName + ")");
	            break;
	        case "smart":
	            player = new SmartPlayer(gameName + "(" + playerName + ")", 5);
	            break;
		}
		
		return player;
	}
	
	/**
	 * Game controller. Controls the game flow using game states and applying actions
	 * @param initialState The game initial state
	 * @param players The list of players
	 * @return Returns the winner when the game hasn't ended
	 */
	public static <S extends GameState<S, A>, A extends GameAction<S, A>> int playGame(GameState<S, A> initialState,
			List<GamePlayer> players) {
		int playerCount = 0;
		for (GamePlayer p : players) {
			p.join(playerCount++); // welcome each player, and assign
									// playerNumber
		}
		@SuppressWarnings("unchecked")
		S currentState = (S) initialState;

		while (!currentState.isFinished()) {
			// request move
			A action = players.get(currentState.getTurn()).requestAction(currentState);
			// apply move
			currentState = action.applyTo(currentState);
			System.out.println("After action:\n" + currentState);

			if (currentState.isFinished()) {
				// game over
				String endText = "The game ended: ";
				int winner = currentState.getWinner();
				if (winner == -1) {
					endText += "draw!";
				} else {
					endText += "player " + (winner + 1) + " (" + players.get(winner).getName() + ") won!";
				}
				System.out.println(endText);
			}
		}
		return currentState.getWinner();
	}
	
	public static void main(String[] args) {
				
		if (args.length < 3 || args.length > 3) {
			// Too less or too many arguments
			System.err.println("Error: bad syntax error!" + System.lineSeparator());
			showCorrectSyntax();
			System.exit(1);
		}
		else{
			
			GameState<?, ?> state;
			List<GamePlayer> players = new ArrayList<GamePlayer>();
			
			state = createInitialState(args[0]);
			
			if (state != null) {
				
				for (int i = 1; i < args.length; i++) {
					GamePlayer player = createPlayer(args[0], args[i], Integer.toString(i));
					
					if (player != null)
						players.add(player);
					else {
						// Player not recognized => bad syntax
						System.err.println("Error: player (" + args[i] + ") undefined!");
						showCorrectSyntax();
						System.exit(1);
					}
				}
			
				playGame(state, players);
			}
			else {
				// State not recognized => bad syntax
				System.err.println("Error: game (" + args[0] + ") undefined!");
				showCorrectSyntax();
				System.exit(1);
			}
		}
		// close the scanner
		sc.close();
	}
	
	/**
	 * Prints the correct syntax to fire the game up
	 */
	private static void showCorrectSyntax() {
		System.out.println("The launcher syntax is: [ game player1 player2 ]" + System.lineSeparator() + 
				"\t + game: ttt (Tic Tac Toe) or was (Wolf And Sheep)" + 
				System.lineSeparator() + 
				"\t + player: console (the player is controlled by the console), rand (random player)" +
				" or smart (an intelligent player)." + System.lineSeparator());
	}

}
