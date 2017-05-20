package es.ucm.fdi.tp.view.gui;

import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import es.ucm.fdi.tp.base.Utils;

public class SmartMoves extends JPanel {

	private static final long serialVersionUID = 8577611644379439121L;
	
	private ThreadsConfig config;

	public SmartMoves(ThreadsConfig config) {
		this.config = config;
		
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
		
		numThreads.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				SmartMoves.this.config.setNumThreads(Integer.parseInt(numThreads.getValue().toString()));
			}
			
		});
		threads.add(numThreads);
		threads.add(new JLabel("threads"));
		
		this.add(threads);
		
		// -----------------------------------
		// Timeout chooser
		// -----------------------------------
		JPanel timeOut = new JPanel(new FlowLayout());
		
		JLabel timeIcon = new JLabel(new ImageIcon(Utils.loadImage("timer.png")));
		timeOut.add(timeIcon);
		JSpinner timeOutSel = 
				new JSpinner(new SpinnerNumberModel(500, 500, 5000, 500));
		
		timeOutSel.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				SmartMoves.this.config.setTimeOut(Integer.parseInt(timeOutSel.getValue().toString()));
			}
			
		});
		
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
