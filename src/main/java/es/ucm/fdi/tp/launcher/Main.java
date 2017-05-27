package es.ucm.fdi.tp.launcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.SwingUtilities;

import es.ucm.fdi.tp.base.console.ConsolePlayer;
import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GamePlayer;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.base.player.ConcurrentAiPlayer;
import es.ucm.fdi.tp.base.player.RandomPlayer;
import es.ucm.fdi.tp.chess.ChessAction;
import es.ucm.fdi.tp.chess.ChessState;
import es.ucm.fdi.tp.mvc.GameTable;
import es.ucm.fdi.tp.ttt.TttAction;
import es.ucm.fdi.tp.ttt.TttState;
import es.ucm.fdi.tp.view.console.ConsoleController;
import es.ucm.fdi.tp.view.console.ConsoleView;
import es.ucm.fdi.tp.view.gui.ChessView;
import es.ucm.fdi.tp.view.gui.GameView;
import es.ucm.fdi.tp.view.gui.TttView;
import es.ucm.fdi.tp.view.gui.WasView;
import es.ucm.fdi.tp.view.gui.WindowController;
import es.ucm.fdi.tp.view.gui.WindowView;
import es.ucm.fdi.tp.was.WolfAndSheepAction;
import es.ucm.fdi.tp.was.WolfAndSheepState;

public class Main {
	private static java.util.Scanner sc = new java.util.Scanner(System.in);
	private static final int TTT_DIM = 3;

		private static GameTable<?, ?> createGame(String gType) {
			GameTable<?, ?> game = null;
			
			switch (gType) {
				case "ttt":
					game = new GameTable<TttState, TttAction>(new TttState(TTT_DIM));
					break;
				case "was":
					game = new GameTable<WolfAndSheepState, WolfAndSheepAction>(new WolfAndSheepState());
					break;
					
				case "chess":
					game = new GameTable<ChessState, ChessAction>(new ChessState());
					break;
	
				}
			
			return game;
		}
		
		private static <S extends GameState<S, A>, A extends GameAction<S, A>>
						void startConsoleMode(String gType, GameTable<S, A> game, String playerModes[]) {
			
			List<GamePlayer> players = new ArrayList<GamePlayer>();
			
			for (int i = 0; i < playerModes.length; i++) {
				GamePlayer player = createPlayer(gType, playerModes[i], Integer.toString(i));
				
				if (player != null)
					players.add(player);
				else {
					// Player not recognized => bad syntax
					System.err.println("Error: player (" + playerModes[i] + ") undefined!");
					usage();
					System.exit(1);
				}
			}
			
			
			new ConsoleView<S, A>(game);
			new ConsoleController<S, A>(players, game).run();
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
		        case "manual":
		            player = new ConsolePlayer(gameName + " " + playerType + "(" + playerName + ")", sc);
		            break;
		        case "random":
		            player = new RandomPlayer(gameName + "(" + playerName + ")");
		            break;
		        case "smart":
		            player = new ConcurrentAiPlayer(gameName + "(" + playerName + ")");
		            break;
			}
			
			return player;
		}
		
		@SuppressWarnings("unchecked")
		private static <S extends GameState<S, A>, A extends GameAction<S, A>>
						void startGUIMode(String gType, GameTable<S, A> game, String playerModes[]) {
			List<GamePlayer> players = new ArrayList<GamePlayer>();
			
			for (int i = 0; i < playerModes.length; i++) {
				GamePlayer player = createPlayer(gType, playerModes[i], Integer.toString(i));
				
				if (player != null)
					players.add(player);
				else {
					// Player not recognized => bad syntax
					System.err.println("Error: player (" + playerModes[i] + ") undefined!");
					usage();
					System.exit(1);
				}
			}
	
			
			WindowController<S, A> control = new WindowController<S, A>(players, game);
			
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					
					for (int i = 0; i <	(gType.equals("ttt") ? new TttState(TTT_DIM).getPlayerCount()
									  : gType.equals("was") ? new WolfAndSheepState().getPlayerCount()
									  : gType.equals("chess") ? new WolfAndSheepState().getPlayerCount() : 0)
							; i++) {
						
						int playerId = i;
						
						switch (gType) {
						case "ttt":
							new WindowView<S, A>(playerId, new RandomPlayer(Integer.toString(playerId)), 
							new ConcurrentAiPlayer(Integer.toString(playerId)), (GameView<S, A>) new TttView(TTT_DIM, playerId), control);
						break;
						
						case "was":
							
							new WindowView<S, A>(playerId, new RandomPlayer(Integer.toString(playerId)), 
							new ConcurrentAiPlayer(Integer.toString(playerId)), (GameView<S, A>) new WasView(playerId), control);
								
							
							break;
							
						case "chess":
							new WindowView<S, A>(playerId, new RandomPlayer(Integer.toString(playerId)),
							new ConcurrentAiPlayer(Integer.toString(playerId)), (GameView<S, A>) new ChessView(new ChessState(), playerId), control);
							
							break;
						}
					}
					
					control.startGame();
				}
				
			});
			
		}
		
		private static void usage() {
			System.out.println("*******************************");
			System.out.println("        LAUNCHER SYNTAX");
			System.out.println("*******************************");
			System.out.println("game mode player1 player2 ... playerN");
			System.lineSeparator();
			System.out.println("\t- game: ttt (for TicTacToe) or was (for Wolf And Sheep)");
			System.out.println("\t- mode: gui (for Swing usage) or console (for console interface)");
			System.out.println("\t- player i: manual (for a manual player), random (a random one) or smart (an intelligent player)");
		}
		
		public static void main(String[] args) {
			if (args.length < 4) {
				usage();
				System.exit(1);
			}
			
			GameTable<?, ?> game = createGame(args[0]);
			
			if (game == null) {
				System.err.println("Invalid game");
				usage();
				System.exit(1);
			}
			
			String[] otherArgs = Arrays.copyOfRange(args, 2, args.length);
			
			switch (args[1]) {
			case "console":
				startConsoleMode(args[0], game, otherArgs);
				break;
			case "gui":
				startGUIMode(args[0], game, otherArgs);				
				break;
			default:
				System.err.println("Invalid view mode: " + args[1]);
				usage();
				System.exit(1);
			}
		}
}
