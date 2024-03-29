package es.ucm.fdi.tp.view.gui;

import java.util.List;

import javax.swing.SwingUtilities;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameError;
import es.ucm.fdi.tp.base.model.GamePlayer;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.mvc.GameObserver;
import es.ucm.fdi.tp.mvc.GameTable;

public class WindowController<S extends GameState<S, A>, A extends GameAction<S, A>> {

	private List<GamePlayer> players;
	private GameTable<S, A> game;
	
	public WindowController(List<GamePlayer> players, GameTable<S, A> game) {
		this.players = players;
		this.game = game;
	}
	
	public void makeMove(A action) {
		try {
			game.execute(action);
		} catch (GameError e) {
			System.out.println("[WCONTROLLER EXCEPTION]: " + e.getMessage());
		}
	}
	
	/**
	 * Makes a random or smart movement
	 * 
	 * @param player The player
	 */
	public void makeSingleMove(GamePlayer player) {
		A action = player.requestAction(game.getState());
		
		try {
			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					if (action != null && player.getPlayerNumber() == game.getState().getTurn())
						game.execute(action); 
				}
				
			});
			
		} catch (GameError e) {
			System.out.println("[WCONTROLLER EXCEPTION]: " + e.getMessage());
		}
	}
	
	public void stopGame() {
		try {
			game.stop();
		} catch (GameError e) {
			System.out.println("[WCONTROLLER EXCEPTION]: " + e.getMessage());
		}
	}

	public void startGame() {
		int playerCount = 0;
		
		for (GamePlayer p : players) {
			p.join(playerCount++);
		}
		
		game.start();
	}
	
	public void addObserver(GameObserver<S, A> o) {
		game.addObserver(o);
	}
	
	public void removeObserver(GameObserver<S, A> o) {
		game.removeObserver(o);
	}
		
}
