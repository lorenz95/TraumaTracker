package it.unibo.disi.pslab.traumatracker;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

class TTAgentLoggerView extends JFrame  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextArea log;
	
	public TTAgentLoggerView() {
		super("TraumaTracker Log");
				
		setSize(600, 400);
		setResizable(false);
		
		/*
		JButton button1 = new JButton("Event #1");
		button1.addActionListener(this);

		JButton button2 = new JButton("Event #2");
		button2.addActionListener(this);

		JPanel panel = new JPanel();
		panel.add(button1);		
		panel.add(button2);	
		*/
		log = new JTextArea("");
		log.setSize(300,300);    

		log.setLineWrap(false);
		log.setEditable(false);
		log.setVisible(true);

		JScrollPane scroll = new JScrollPane (log);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		          scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		
		setLayout(new BorderLayout());
	    add(scroll, BorderLayout.CENTER);
		// add(panel,BorderLayout.NORTH);
	    // add(panel,BorderLayout.NORTH);
	    		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent ev) {
				System.exit(-1);
			}
		});
	}
	
	public void log(String msg){
		SwingUtilities.invokeLater(() -> {
			log.append(msg+"\n");
		});
		
		
	}
	/*
	public void actionPerformed(ActionEvent ev) {
		try {
			controller.processEvent(ev.getActionCommand());
		} catch (Exception ex) {
		}
	}
	 */

}
