package es.ucm.fdi.tp.view.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import es.ucm.fdi.tp.base.Utils;
import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;

public class JGameSettings<S extends GameState<S, A>, A extends GameAction<S, A>> extends JDialog {
	
	private static final long serialVersionUID = 7017422697682458771L;
	
	private static String title = "Game Settings customizer";
	private GameView<S, A> view;
		
	public JGameSettings(JFrame frame, GameView<S, A> view) {
		super(frame, title, true);
		this.view = view;
		
		JPanel main = new JPanel();
		main.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		this.setPreferredSize(new Dimension(400, 320));
		this.setResizable(false);

		BorderLayout layout = new BorderLayout();
		layout.setHgap(100);
		layout.setVgap(10);
		
		this.setContentPane(main);
		main.setLayout(layout);
		
		// Create the tabs
		JTabbedPane tabs = new JTabbedPane(JTabbedPane.TOP);
		tabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		
		tabs.addTab("Sound effects", new EffectsPanel());
		tabs.addTab("About the game", new AboutPanel());
		
		main.add(tabs, BorderLayout.NORTH);
		
		// Create the save button
		JButton save = new JButton("Save changes!", new ImageIcon(Utils.loadImage("ok.png")));
		save.setFocusable(false);
		save.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JGameSettings.this.setVisible(false);
			}
			
		});
		
		main.add(new JLabel(""), BorderLayout.WEST); // Blank space
		main.add(save, BorderLayout.CENTER);
		main.add(new JLabel(""), BorderLayout.EAST);
		
		this.pack();
		this.setLocationRelativeTo(null); // To center the dialog
	}
	
	private class EffectsPanel extends JPanel implements ActionListener {
		
		private static final long serialVersionUID = -5362384555319763901L;
		
		private static final String disable = "Disable sound effects";
		private static final String effectOne = "Effect #1";
		private static final String effectTwo = "Effect #2";
		private static final String effectThree = "Effect #3";
		private static final String effectFour = "Effect #4";
		private static final String effectFive = "Effect #5";
		private static final String effectSix = "Effect #6";
		
		private JLabel picture;
		
		public EffectsPanel() {
			super(new BorderLayout());
			
			// Create the radio buttons
			JRadioButton noSound = new JRadioButton(disable);
			noSound.setActionCommand("disabled");
			noSound.setSelected(true);
			
			JRadioButton one = new JRadioButton(effectOne);
			one.setActionCommand("effect1");
			
			JRadioButton two = new JRadioButton(effectTwo);
			two.setActionCommand("effect2");
			
			JRadioButton three = new JRadioButton(effectThree);
			three.setActionCommand("effect3");
			
			JRadioButton four = new JRadioButton(effectFour);
			four.setActionCommand("effect4");
			
			JRadioButton five = new JRadioButton(effectFive);
			five.setActionCommand("effect5");
			
			JRadioButton six = new JRadioButton(effectSix);
			six.setActionCommand("effect6");
			
			// Group the radio buttons
			ButtonGroup group = new ButtonGroup();	
			group.add(noSound);
			group.add(one);
			group.add(two);
			group.add(three);
			group.add(four);
			group.add(five);
			group.add(six);
			
			// Register a listener for the radio buttons
			noSound.addActionListener(this);
			one.addActionListener(this);
			two.addActionListener(this);
			three.addActionListener(this);
			four.addActionListener(this);
			five.addActionListener(this);
			six.addActionListener(this);
			
			// Set up the picture label
			picture = new JLabel(new ImageIcon(Utils.loadImage("speaker.png")));
			
			//Put the radio buttons in a column in a panel.
	        JPanel radioPanel = new JPanel(new GridLayout(0, 1));
	        radioPanel.add(noSound);
	        radioPanel.add(one);
	        radioPanel.add(two);
	        radioPanel.add(three);
	        radioPanel.add(four);
	        radioPanel.add(five);
	        radioPanel.add(six);

	        add(radioPanel, BorderLayout.LINE_START);
	        add(picture, BorderLayout.CENTER);
	        setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JGameSettings.this.view.setSoundEffect(e.getActionCommand());			
		}
		
	}
	
	private class AboutPanel extends JPanel {
		
		private static final long serialVersionUID = -1573801226687308407L;
		
		private String about1 = "This is a Java program to play up to 2 board games. ";
		private String about2 = "Developed using MVC (Model View Controller) pattern";
		private String about3 = "and Swing for GUI (Graphical User Interface).";
		private String credits = "Developed by David Arroyo";
		
		public AboutPanel() {
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			
			addLabel(about1);
			addLabel(about2);
			addLabel(about3);
			
			// Add UCM logo centered
			JLabel ucm = new JLabel(new ImageIcon(Utils.loadImage("ucm.png")));
			ucm.setAlignmentX(Component.CENTER_ALIGNMENT);
			this.add(ucm);
			
			addLabel(credits);
		}
		
		/**
		 * Add a centered JLabel to the panel
		 * 
		 * @param text The JLabel's text
		 */
		private void addLabel(String text) {
			JLabel label = new JLabel(text);
			label.setAlignmentX(Component.CENTER_ALIGNMENT);
			this.add(label);
		}
	}

}
