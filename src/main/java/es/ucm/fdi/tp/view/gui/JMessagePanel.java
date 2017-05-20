package es.ucm.fdi.tp.view.gui;

import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class JMessagePanel extends JScrollPane {

	private static final long serialVersionUID = 3696340689218815391L;
	private JTextArea status;
	
	public JMessagePanel() {
		this.status = new JTextArea(10, 18);
		this.status.setEditable(false);
		this.status.setLineWrap(true);
		this.status.setFocusable(false);
		
		this.setViewportView(status);
		this.setPreferredSize(new Dimension(200, 170));
	}
	
	public void setStatusMessage(String status) {
		this.status.append("* " + status);
		this.status.append(System.lineSeparator());
		
		/* We do this to autoscroll down after append */
		this.getVerticalScrollBar().setValue(this.getVerticalScrollBar().getMaximum());
	}
}
