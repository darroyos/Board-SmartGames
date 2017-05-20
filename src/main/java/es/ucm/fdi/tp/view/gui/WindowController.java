package es.ucm.fdi.tp.view.gui;

import java.util.List;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameError;
import es.ucm.fdi.tp.base.model.GamePlayer;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.base.player.ConcurrentAiPlayer;
import es.ucm.fdi.tp.mvc.GameObserver;
import es.ucm.fdi.tp.mvc.GameTable;

public class WindowController<S extends GameState<S, A>, A extends GameAction<S, A>> {

	private List<GamePlayer> players;
	private GameTable<S, A> game;
	private Thread smartThread;
	
	public WindowController(List<GamePlayer> players, GameTable<S, A> game) {
		this.players = players;
		this.game = game;
		this.smartThread = null;
	}
	
	public void makeMove(A action) {
		try {
			game.execute(action);
		} catch (GameError e) { /* Silent exception */ }
	}
	
	public void makeSingleMove(GamePlayer player) {
		try {
			game.execute(player.requestAction(game.getState())); 
		} catch (GameError e) { /* Silent exception */ }
	}
	
	public void makeSmartMove(ConcurrentAiPlayer player, int threads, int timeOut) {
		this.smartThread = new Thread(new Runnable() {

			@Override
			public void run() {
				player.setMaxThreads(threads);
				player.setTimeout(timeOut);
				game.execute(player.requestAction(game.getState()));
			}
			
		});
		
		this.smartThread.start();
	}
	
	public void stopGame() {
		try {
			game.stop();
		} catch (GameError e) { /* Silent exception */ }
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
