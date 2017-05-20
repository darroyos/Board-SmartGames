package es.ucm.fdi.tp.view.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import es.ucm.fdi.tp.base.Utils;
import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GamePlayer;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.mvc.GameEvent;
import es.ucm.fdi.tp.mvc.GameObserver;


public class WindowView<S extends GameState<S, A>, A extends GameAction<S, A>>
					   extends JFrame implements GameObserver<S, A> {
	

	private static final long serialVersionUID = 6968578710150034573L;
	
	/**
	 * The player's number
	 */
	private int playerId;
	
	/**
	 * A random GamePlayer to request random actions
	 */
	private GamePlayer randPlayer;
	
	/**
	 * An smart GamePlayer to request smart actions
	 */
	private GamePlayer smartPlayer;
	
	/**
	 * The game board
	 */
	private GameView<S, A> gameView;
	
	
	/**
	 * The window view controller
	 */
	private WindowController<S, A> winCtrl;
	
	
	/**
	 * ComboBox items
	 */
	private enum Mode {
		Manual,
		Random,
		Smart
	}
	
	private JComboBox<Mode> mode;
	
	/**
	 * The message box which shows relevant information about the current game
	 */
	private JMessagePanel status;
	
	/**
	 * The players table
	 */
	private JPlayerInfoTable<S, A> playerInfo;
	
	/**
	 * We need both ActionListeners to be able to activate or deactivate the buttons
	 */
	private RandomListener randomListener;
	private SmartListener smartListener;
	
	/**
	 * Game view settings customizer (sound effects, and maybe more)
	 */
	private JGameSettings<S, A> settings;
	
	public WindowView(int playerId, GamePlayer randPlayer, GamePlayer smartPlayer, GameView<S, A> gameView, WindowController<S, A> winCtrl) {
		super();
		this.playerId = playerId;
		this.randPlayer = randPlayer;
		this.smartPlayer = smartPlayer;
		this.gameView = gameView;
		this.winCtrl = winCtrl;
		this.settings = new JGameSettings<S, A>(WindowView.this, WindowView.this.gameView);
		
		/* Join random and smart players */
		smartPlayer.join(playerId);
		randPlayer.join(playerId);		

		gameView.setController(winCtrl);
		initGUI();
		winCtrl.addObserver(this);
	}
	
	public void initGUI() {
		Container main = this.getContentPane();
		JPanel top = new JPanel();
		JPanel right = new JPanel();
		
		/*-----------------------------------*/
		/*           MAIN LAYOUT             */
		/*-----------------------------------*/
		main.setLayout(new BorderLayout());
		
		main.add(top, BorderLayout.NORTH);
		main.add(right, BorderLayout.EAST);
		
		/*----------------------------------------------------*/
		/*               MAIN LAYOUT > TOP LAYOUT             */
		/*----------------------------------------------------*/
		FlowLayout topLayout = new FlowLayout();
		topLayout.setAlignment(FlowLayout.LEFT);
		top.setLayout(topLayout);
		
		/*--------------------------------------------------------*/
		/*            MAIN LAYOUT > TOP LAYOUT > TOOLBAR          */
		/*--------------------------------------------------------*/
		JToolbarGame tools = new JToolbarGame();	
		
		// Add the toolbar into the top frame
		top.add(tools);
		
		/*---------------------------------------------------------------*/
		/*            MAIN LAYOUT > TOP LAYOUT > MODE SELECTION          */
		/*---------------------------------------------------------------*/
		top.add(new JLabel("Player Mode: "));
		
		this.mode = new JComboBox<Mode>(Mode.values());
		this.mode.addActionListener(new ModeListener());
		this.mode.setToolTipText("Select the player mode for the future movements");
		top.setFocusable(false);
		
		top.add(mode);
		
		/*---------------------------------------------------------------*/
		/*             MAIN LAYOUT > TOP LAYOUT > SMART MOVES            */
		/*---------------------------------------------------------------*/
		top.add(new SmartMoves());
		
		/*---------------------------------------------------------------*/
		/*                  MAIN LAYOUT > RIGHT LAYOUT                   */
		/*---------------------------------------------------------------*/
		BoxLayout rightLayout = new BoxLayout(right, BoxLayout.Y_AXIS);
		right.setLayout(rightLayout);
		
		/*---------------------------------------------------------------*/
		/*        MAIN LAYOUT > RIGHT LAYOUT > STATUS MESSAGES           */
		/*---------------------------------------------------------------*/
		TitledBorder borderStatus = new TitledBorder("Status Messages");
		borderStatus.setTitleJustification(TitledBorder.LEFT);
		borderStatus.setTitlePosition(TitledBorder.TOP);
	       
		status = new JMessagePanel();
		status.setBorder(borderStatus);
		status.setFocusable(false);

		gameView.setMessagePanel(status);
		
		right.add(status);
		
		/*---------------------------------------------------------------*/
		/*        MAIN LAYOUT > RIGHT LAYOUT > PLAYER INFORMATION        */
		/*---------------------------------------------------------------*/
		TitledBorder borderPlayer = new TitledBorder("Player Information");
		borderPlayer.setTitleJustification(TitledBorder.LEFT);
		borderPlayer.setTitlePosition(TitledBorder.TOP);
	   
		playerInfo = new JPlayerInfoTable<S, A>(this.gameView);
		playerInfo.setFocusable(false);
		
		playerInfo.setBorder(borderPlayer);
		right.add(playerInfo);
	
		
		/*---------------------------------------------------------------*/
		/*             MAIN LAYOUT > RIGHT LAYOUT > GAME VIEW            */
		/*---------------------------------------------------------------*/
		main.add(this.gameView);

		/*---------------------------------------------------------------*/

		/* Add the key listener */
		this.setFocusable(true);
		this.addKeyListener(new BoardKeyListener());
		
		this.setVisible(true);
		this.setResizable(false);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	@Override
	public void notifyEvent(GameEvent<S, A> e) {
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				handleEvent(e); // it updates the visual components
			}
		});
		
	}
	
	private void handleEvent(GameEvent<S, A> e) {
		switch (e.getType()) {
		
		case Start:
			/* Initialize (if it wasn't done before, e.g., not in a game restart) 
			 * the player info panel with the correct number of players */
			
			if (!this.playerInfo.isInitialized())
				this.playerInfo.initGUI(e.getState().getPlayerCount());
			
			/* Set the initial colors in the JPlayerInfoTable */
			this.gameView.setInitialColors(playerInfo);
			
			/* Activate the buttons for the player with the turn */
			if (this.playerId == e.getState().getTurn()) {
				this.toFront(); // Bring the window to the front
				activateButtons();
			}
			
			status.setStatusMessage("The game has just started!");
			gameView.update(e.getState());
			
			/* Disable the waiting player view */
			disableWaitingPlayerView(e.getState().getTurn());
			
			/* Update of the JFrame title */
			String title = e.getState().getGameDescription() + " (view for player " + this.playerId + ")";
			/* Substring() because we don't want to show the package name */
			this.setTitle(title.substring(title.lastIndexOf(".") + 1));
			
			/* Automatic moves (ComboBox) */
			makeModeSelectedMove();
			
			break;
			
		case Change:
			
			/* Activate the toolbar buttons for the player with the turn */
			if (this.playerId == e.getState().getTurn()) {
				this.toFront(); // Bring the window to the front
				activateButtons();
			}
			else
				deactivateButtons();
			
			status.setStatusMessage(e.getAction().toString());
			gameView.update(e.getState());

			
			/* Disable the waiting player view */
			disableWaitingPlayerView(e.getState().getTurn());
			
			/* We have a winner */
			if (e.getState().isFinished()) {
				// game over
				String endText = "The game ended: ";
				
				int winner = e.getState().getWinner();
				
				if (winner == -1) endText += "draw!";
				else endText += "player " + winner + " won!";
				
				status.setStatusMessage(endText);
				
				gameView.disable(); // Disable the board
				
				deactivateButtons();
			}
			
			/* Automatic moves (ComboBox) */
			else
				makeModeSelectedMove();

			break;
			
		case Error:
			status.setStatusMessage("There has been an error!");
			status.setStatusMessage(e.toString());
			
			break;
			
		case Stop:
			status.setStatusMessage(e.toString());
			this.dispose();
			break;
			
		case Info:
			status.setStatusMessage(e.toString());
			break;
		
		}

	}
	
	private void disableWaitingPlayerView(int turn) {
		if (this.playerId != turn)
			gameView.disable();
	}
	
	private void activateButtons() {
		this.randomListener.setActive(true);
		this.smartListener.setActive(true);
	}
	
	private void deactivateButtons() {
		this.randomListener.setActive(false);
		this.smartListener.setActive(false);
	}
	
	private void makeModeSelectedMove() {
		/* We check the randomListener boolean because is the way we know if it's the player's turn */
		
		if (randomListener.active && mode.getSelectedItem().equals(Mode.Random))
			winCtrl.makeSingleMove(randPlayer);
		else if (smartListener.active && mode.getSelectedItem().equals(Mode.Smart))
			winCtrl.makeSingleMove(smartPlayer);
	}
	
	private class JToolbarGame extends JToolBar {
		
		private static final long serialVersionUID = 3920854249378283129L;
		private JButton random;
		private JButton smart;
		private JButton restart;
		private JButton settings;
		private JButton exit;
		
		public JToolbarGame() {
			// -----------------------------------
			// Random movement button
			// -----------------------------------
			ImageIcon randomIcon = new ImageIcon(Utils.loadImage("dice.png"));
			this.random = new JButton(randomIcon);
			this.random.setToolTipText("Make a random movement");
			this.random.setFocusable(false);
			randomListener = new RandomListener();
			this.random.addActionListener(randomListener);
			this.add(random);
			
			// -----------------------------------
			// Smart movement button
			// -----------------------------------
			ImageIcon smartIcon = new ImageIcon(Utils.loadImage("nerd.png"));
			this.smart = new JButton(smartIcon);
			this.smart.setToolTipText("Make a smart movement");
			this.smart.setFocusable(false);
			smartListener = new SmartListener();
			this.smart.addActionListener(smartListener);
			this.add(smart);
			
			// -----------------------------------
			// Game restart button
			// -----------------------------------
			ImageIcon restartIcon = new ImageIcon(Utils.loadImage("restart.png"));
			this.restart = new JButton(restartIcon);
			this.restart.setToolTipText("Restart the game");
			this.restart.setFocusable(false);
			this.restart.addActionListener(new RestartListener());
			this.add(restart);
			
			// -----------------------------------
			// Settings button
			// -----------------------------------
			ImageIcon settingsIcon = new ImageIcon(Utils.loadImage("settings.png"));
			this.settings = new JButton(settingsIcon);
			this.settings.setToolTipText("Game settings");
			this.settings.setFocusable(false);
			this.settings.addActionListener(new SettingsListener());
			this.add(settings);
			
			// -----------------------------------
			// Exit button
			// -----------------------------------
			ImageIcon exitIcon = new ImageIcon(Utils.loadImage("exit.png"));
			this.exit = new JButton(exitIcon);
			this.exit.setToolTipText("Exit the game");
			this.exit.setFocusable(false);
			this.exit.addActionListener(new ExitListener());
			this.add(exit);	
		}
	}
	
	private class SmartMoves extends JPanel {
		
		private static final long serialVersionUID = 8577611644379439121L;

		public SmartMoves() {
			FlowLayout smartLy = new FlowLayout();
			this.setLayout(smartLy);
			this.setBorder(BorderFactory.createTitledBorder("Smart Moves"));
			
			// -----------------------------------
			// Threads chooser
			// -----------------------------------
			JPanel threads = new JPanel(new FlowLayout());
			
			JLabel brainIcon = new JLabel(new ImageIcon(Utils.loadImage("brain.png")));
			threads.add(brainIcon);
			JSpinner numThreads = 
					new JSpinner(new SpinnerNumberModel(1, 1, Runtime.getRuntime().availableProcessors(), 1));
			threads.add(numThreads);
			threads.add(new JLabel("threads"));
			
			this.add(threads);
			
			// -----------------------------------
			// Threads chooser
			// -----------------------------------
			JPanel timeOut = new JPanel(new FlowLayout());
			
			JLabel timeIcon = new JLabel(new ImageIcon(Utils.loadImage("timer.png")));
			timeOut.add(timeIcon);
			JSpinner timeOutSel = 
					new JSpinner(new SpinnerNumberModel(500, 500, 5000, 500));
			timeOut.add(timeOutSel);
			timeOut.add(new JLabel("ms."));
			
			this.add(timeOut);
			
			// -----------------------------------
			// Stop thinking
			// -----------------------------------
			JPanel stop = new JPanel(new FlowLayout());
			JButton stopButton = new JButton(new ImageIcon(Utils.loadImage("stop.png")));
			stop.add(stopButton);
			
			this.add(stop);
			
		}
	}
	
	// -----------------------------------------------------
	// Action Listeners (toolbar butttons and player mode)
	// -----------------------------------------------------
	
	private class RandomListener implements ActionListener {
		private boolean active = false;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			
			if (active) {
			
				status.setStatusMessage("You have requested a random move.");
				winCtrl.makeSingleMove(randPlayer);
				
			}
		}
		
		public void setActive(boolean active) {
			this.active = active;
		}
	}
	
	private class SmartListener implements ActionListener {
		private boolean active = false;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			
			if (active) {
			
				status.setStatusMessage("You have requested a smart move.");
				winCtrl.makeSingleMove(smartPlayer);
				
			}
		}
		
		public void setActive(boolean active) {
			this.active = active;
		}
	}
	
	private class RestartListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			winCtrl.startGame();
		}
	}
	
	private class SettingsListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			settings.setVisible(true);
		}
	}
	
	private class ExitListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String message = "Do you really want to exit?";
			String title = "Exiting game...";

			int exit = 
					JOptionPane.showConfirmDialog(
							null, message, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
			
			if (exit == JOptionPane.OK_OPTION)
				System.exit(0);
		}
	}
	
	private class ModeListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			makeModeSelectedMove();
		}
		
	}
	
	private class BoardKeyListener implements KeyListener {

		@Override
		public void keyTyped(KeyEvent e) {
		}

		@Override
		public void keyPressed(KeyEvent e) {
		}

		@Override
		public void keyReleased(KeyEvent e) {
			gameView.keyHandler(e.getKeyCode());
		}
		
	}

}
