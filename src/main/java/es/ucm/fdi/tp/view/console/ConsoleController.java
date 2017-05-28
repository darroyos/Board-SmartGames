package es.ucm.fdi.tp.view.console;

import java.util.List;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameError;
import es.ucm.fdi.tp.base.model.GamePlayer;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.mvc.GameTable;

public class ConsoleController<S extends GameState<S, A>, A extends GameAction<S, A>>
							  implements Runnable {
	
	private List<GamePlayer> players;
	private GameTable<S, A> game;
	
	
	public ConsoleController(List<GamePlayer> players, GameTable<S, A> game) {
		this.players = players;
		this.game = game;
	}

	@Override
	public void run() {
		
		joinPlayers();

		this.game.start();
		
		/* Loop requesting actions while the game hasn't finished yet */
		while (!this.game.getState().isFinished()) {
			
			// request move
			A action = players.get(this.game.getState().getTurn()).requestAction(this.game.getState());
			// apply move
			
			try {
				this.game.execute(action);
			} catch(GameError e) {
				System.out.println("[CCONTROLLER EXCEPTION]: " + e.getMessage());
			};
			
		}

	}
	
	/**
	 * Welcome each player, and assign playerNumber
	 */
	private void joinPlayers() {
		int playerCount = 0;
		
		for (GamePlayer p : players) {
			p.join(playerCount++);
		}
	}

}
