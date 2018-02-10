package it.unibo.disi.pslab.traumatracker;

import java.io.FileWriter;
import java.util.Date;

class TTAgentLogger  {

	private TTAgentLoggerView view;
	private FileWriter logFile;
	
	public TTAgentLogger() {		
		view = new TTAgentLoggerView();
		view.setVisible(true);
		try {
			String nameFile = "date: " + "logTT-"+(new Date().toString())+".txt";
			String replNameFile = nameFile.replace(':', '-');
			
			logFile = new FileWriter(replNameFile);
		} catch (Exception ex){
			System.out.println("Exception = " + ex);			
		}
	}
	
	public void log(String msg){
		String logmsg = "["+new Date()+"] "+msg;
		view.log(logmsg);	
		new Thread(() -> {
			try {
				synchronized(logFile){
					logFile.write(logmsg+"\n");
					logFile.flush();
				}
			} catch (Exception ex){
				ex.printStackTrace();
			}
		}).start();
	}

}
