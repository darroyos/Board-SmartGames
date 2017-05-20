package es.ucm.fdi.tp.mvc;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameError;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.mvc.GameEvent.EventType;

import java.util.ArrayList;

/**
 * An event-driven game engine.
 * Keeps a list of players and a state, and notifies observers
 * of any changes to the game.
 */
public class GameTable<S extends GameState<S, A>, A extends GameAction<S, A>> implements GameObservable<S, A> {

    private ArrayList<GameObserver<S, A>> observers;
    private S initialState;
    private S currentState;
    private boolean isRunning;

    /**
     * @param initState Game's initial state
     */
    public GameTable(S initState) {
        this.observers = new ArrayList<GameObserver<S,A>>();
        this.initialState = initState;
        this.currentState = initState;
        this.isRunning = false;
    }
    
    /**
     * Starts or restarts the game if it was already started
     */
    public void start() {
    	String desc = "The game has just started";
    	
    	this.isRunning = true;
    	
        this.currentState = this.initialState; // Copy the initial state into the current one
        GameEvent<S, A> startEvent = new GameEvent<S, A>(EventType.Start, null, this.currentState, null, desc);
        // Notify observers
        sendGameEvent(startEvent);
    }
    
    /**
     * Stops the game
     */
    public void stop() {
    	GameEvent<S, A> event;
    	String desc;
    	
        if (this.isRunning) {
        	this.isRunning = false;
        	desc = "The game has been stopped";
        	event = new GameEvent<S, A>(EventType.Stop, null, this.currentState, null, desc);
        	sendGameEvent(event);
        }
        // The game was already running
        else {
        	desc = "The game was already stopped";
        	GameError error = new GameError(desc);
        	event = new GameEvent<S, A>(EventType.Error, null, this.currentState, error, desc);
        	// Notify observers
        	sendGameEvent(event);
        	throw error;
        }
    }
    
    /**
     * Executes an action on the current state and updates it
     * 
     * @param action The action to be executed on the current state
     */
    public void execute(A action) {
    	GameEvent<S, A> event;
    	String desc;
    	
    	// Fires an error because the game wasn't already running
    	if (!this.isRunning) {
    		desc = "The game was stopped";
        	GameError error = new GameError(desc);
    		event = new GameEvent<S, A>(EventType.Error, null, this.currentState, error, desc);
    		sendGameEvent(event);
    		throw error;
    	}
    	else {
    		// Executes the given action
	        try {
	        	this.currentState = action.applyTo(this.currentState);
	        	
	        	desc = action.toString(); // action's description
	        	event = new GameEvent<S, A>(EventType.Change, action, this.currentState, null, desc);
	        	sendGameEvent(event);
	        		                 	
	        } catch (IllegalArgumentException e) {
	        	// Error when applying the action so we send a GameError
	        	desc = e.getMessage();
	        	GameError error = new GameError(desc);
	        	event = new GameEvent<S, A>(EventType.Error, action, this.currentState, error, desc);
	        	sendGameEvent(event);
	        	throw error;
	        }
    	}
    }
    
    /**
     * @return The current state
     */
    public S getState() {
        return this.currentState;
    }

    /* (non-Javadoc)
     * @see es.ucm.fdi.tp.mvc.GameObservable#addObserver(es.ucm.fdi.tp.mvc.GameObserver)
     */
    public void addObserver(GameObserver<S, A> o) {
        this.observers.add(o);
    }
    
    /* (non-Javadoc)
     * @see es.ucm.fdi.tp.mvc.GameObservable#removeObserver(es.ucm.fdi.tp.mvc.GameObserver)
     */
    public void removeObserver(GameObserver<S, A> o) {
        this.observers.remove(o);
    }
    
    /**
     * Notifies an event to everyone subscribed to the observers list
     * 
     * @param event
     */
    private void sendGameEvent(GameEvent<S, A> event) {
    	for (GameObserver<S, A> ob : observers)
    		ob.notifyEvent(event);
    }
}
