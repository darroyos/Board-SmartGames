package es.ucm.fdi.tp.view.console;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.mvc.GameEvent;
import es.ucm.fdi.tp.mvc.GameObservable;
import es.ucm.fdi.tp.mvc.GameObserver;

public class ConsoleView<S extends GameState<S, A>, A extends GameAction<S, A>>
						implements GameObserver<S, A> {
	
	
	
	public ConsoleView(GameObservable<S, A> gameTable) {
		// Subscribe the view to the observers list
		gameTable.addObserver(this);
	}

	@Override
	public void notifyEvent(GameEvent<S, A> e) {
		switch (e.getType()) {
		
		case Start:
			System.out.println("The game has just started!");
			break;
			
		case Change:
			
			System.out.println(e.getState());
			
			/* We have a winner */
			if (e.getState().isFinished()) {
				// game over
				String endText = "The game ended: ";
				
				int winner = e.getState().getWinner();
				
				if (winner == -1) endText += "draw!";
				else endText += "player " + (winner + 1) + " won!";
				
				System.out.println(endText);
			}
			
			break;
			
		case Error:
			System.out.println("There has been an error!");
			System.out.println(e.getError().getMessage());
			break;
			
		case Stop:
			System.out.println("The game has been stopped!");
			break;
			
		case Info:
			System.out.println(e);
			break;
			
		}
		
	}

}
