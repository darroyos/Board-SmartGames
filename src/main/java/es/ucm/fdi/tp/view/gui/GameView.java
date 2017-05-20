package es.ucm.fdi.tp.view.gui;

import java.awt.Color;

import javax.swing.*;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;

public abstract class GameView<S extends GameState<S, A>, A extends GameAction<S, A>>
								 extends JPanel {

	private static final long serialVersionUID = -8132469725488364274L;
	protected WindowController<S, A> windowCtrl;
	
	/**
	 * Enables the game board
	 */
	public abstract void enable();
	
	/**
	 * Disables the game board
	 */
	public abstract void disable();
	
	/**
	 * Updates the game board with the received state by parameter
	 * @param state The state we want to show on the board
	 */
	public abstract void update(S state);
	
	/**
	 * Change the color of a player
	 * 
	 * @param player The player number
	 * @param color The color we want to set
	 */
	public abstract void changeColor(int player, Color color);
	
	/**
	 * Refresh the board with the current state (e.g. after a color selection)
	 */
	public abstract void refresh();
	
	/**
	 * Decides what to do when the keyCode key is pressed
	 * @param keyCode The pressed key
	 */
	protected abstract void keyHandler(int keyCode);
	
	/**
	 * Links the JMessagePanel with the GameView with a reference
	 * @param panel The JMessagePanel
	 */
	public abstract void setMessagePanel(JMessagePanel panel);
	
	/**
	 * Links the WindowController with the GameView with a reference
	 * @param windowCtrl The WindowController
	 */
	public abstract void setController(WindowController<S, A> windowCtrl);
	
	/**
	 * Set the randomly generated player colors in the JPlayerInfoTable
	 * @param playerInfo The JPlayerInfoTable with the player's list
	 */
	public abstract void setInitialColors(JPlayerInfoTable<S, A> playerInfo);
	
	/**
	 * Set the desired sound effect for each field click
	 * @param sound The chosen sound efect
	 */
	public abstract void setSoundEffect(String sound);

}
