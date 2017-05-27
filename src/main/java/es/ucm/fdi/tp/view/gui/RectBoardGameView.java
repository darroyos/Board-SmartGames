package es.ucm.fdi.tp.view.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Iterator;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import es.ucm.fdi.tp.base.Utils;
import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;


public abstract class RectBoardGameView<S extends GameState<S, A>, A extends GameAction<S, A>>
										extends GameView<S, A> {
	
	private static final long serialVersionUID = -2770094889066902427L;
	
	/**
	 * Colors iterator which generates random colors
	 */
	private static final Iterator<Color> color = Utils.colorsGenerator();
	
	/**
	 * The board fields
	 */
	private JBoardField[][] fields;
	
	/**
	 * Sound file's name without extension (all sounds are .wav)
	 */
	private String soundName;
	
	
	public RectBoardGameView(int rows, int cols) {
		this.fields = new JBoardField[rows][cols];
		this.soundName = "disabled";
		initGUI(rows, cols);
	}
	
	@Override
	public void enable() {
		for (int i = 0; i < this.getNumRows(); i++)
			for (int j = 0; j < this.getNumCols(); j++)
				fields[i][j].setEnabled(true);
	}
	
	@Override
	public void disable() {
		for (int i = 0; i < this.getNumRows(); i++)
			for (int j = 0; j < this.getNumCols(); j++) {
				fields[i][j].setEnabled(false);
				fields[i][j].setBorder(null);
				
			}
	}

	
	private void initGUI(int rows, int cols) {
		
		this.setPreferredSize(new Dimension(450, 450));
		/* Create a GridLayout to store all our fields */
		this.setLayout(new GridLayout(rows, cols, 1, 1));
		
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {

				fields[row][col] = new JBoardField(col, row);
				
				/* Set an action listener for the field / button */
				JBoardField listenerField = fields[row][col];
				fields[row][col].addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						playButtonSound();
						mouseClicked(listenerField); // get the source field
					}
					
				});	
				
				/* Add it to the board */
				this.add(fields[row][col]);
				
			}		
		}
	}	
	
	@Override
	public void setController(WindowController<S, A> windowCtrl) {
		this.windowCtrl = windowCtrl;
	}
	
	/**
	 * Returns the asked board field
	 * 
	 * @param row The field's row
	 * @param col The field's col
	 * @return The field
	 */
	public JBoardField getField(int row, int col) {
		if ((row >= 0 && row < getNumRows()) && (col >= 0 && col < getNumCols()))
			return fields[row][col];
		else
			return null;		
	}

	/**
	 * @return A random color
	 */
	protected static Color getColor() {
		return color.next();
	}
	
	@Override
	public void setSoundEffect(String sound) {
		this.soundName = sound;
	}
	
	private void playButtonSound() {
		
		if (!this.soundName.equals("disabled")) {
			
			try {
				AudioInputStream audioInputStream = 
						AudioSystem.getAudioInputStream(
								new File(Utils.class.getClassLoader().getResource
										("sounds/" + this.soundName + ".wav").getFile()));
				
				Clip clip = AudioSystem.getClip();
				clip.open(audioInputStream);
				clip.start();
			} catch (Exception e) {
				System.out.println(e.getMessage()); 
			}
			
		}
	}

	protected abstract int getNumCols();
	protected abstract int getNumRows();
	protected abstract void mouseClicked(JBoardField field);

}
