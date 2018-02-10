package it.unibo.disi.pslab.traumatracker;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.mongodb.*;
import com.mongodb.event.*;

import it.unibo.disi.pslab.traumatracker.util.TTConfig;

public class MongoDBAgent extends Thread {

	private String dbPath;
	private String mongoDBPath;
	private int port;
	private String host;
    private MongoClient client;
    //# private Date lastPingTime;
	private TTAgentLogger view;
	//# private TTConfig config;
	
	public MongoDBAgent(TTAgentLogger view, TTConfig config){
		dbPath = "C:/mongoData/db"; //"./data/db";
		mongoDBPath = "C:/Programmi/MongoDB/Server/3.6/bin"; // "/usr/local/bin/mongod";
		host = "localhost";
		port = 27017;
		this.view = view;
		//# this.config = config;
	}
	
	public void run() {
		
		log("Setting up the server.");
		setupServer();
		
        try {
    		log("Connecting to the DB...");
            MongoClientOptions clientOptions = new MongoClientOptions.Builder()	
                .addServerMonitorListener(new ServerListener())	                
                .build();

            client = new MongoClient(new ServerAddress(host, port), clientOptions);
            log(""+client.getAddress());
            
        } catch (Exception ex) {
        	ex.printStackTrace();
        }

        while (true){
			try {
				Thread.sleep(10000);
			} catch (Exception ex){}
		}
	}
		
	class ServerListener implements ServerMonitorListener {
	    @Override
	    public void serverHearbeatStarted(ServerHeartbeatStartedEvent serverHeartbeatStartedEvent) {
	        // Ping Started
	    	// log("Checking is mongodb is alive..");
	    }

	    @Override
	    public void serverHeartbeatSucceeded(ServerHeartbeatSucceededEvent serverHeartbeatSucceededEvent) {
	        // Ping Succeed, Connected to server
	    	//# lastPingTime = new Date();
	    	// log("Server is alive.");
	    }

	    @Override
	    public void serverHeartbeatFailed(ServerHeartbeatFailedEvent serverHeartbeatFailedEvent) {
	        // Ping failed, server down or connection lost
	    	log("Server down or connection lost => Setting up the server");
	    	setupServer();	    	
	    }
	}

	private void log(String msg){
		System.out.println("[TTMongoDBAgent] "+msg);
		view.log("[TTMongoDBAgent] "+msg);
	}
	
	private void setupServer(){
		try {
			Process start = Runtime.getRuntime().exec(new String[]{mongoDBPath,"--dbpath",dbPath});
			BufferedReader reader = new BufferedReader(new InputStreamReader(start.getInputStream()));
			new Thread(() -> {
				reader.lines().forEach( s -> {
					System.out.println(s);
				});
			}).start();
		} catch (Exception ex){
			ex.printStackTrace();
		}
	}
	
	
}
