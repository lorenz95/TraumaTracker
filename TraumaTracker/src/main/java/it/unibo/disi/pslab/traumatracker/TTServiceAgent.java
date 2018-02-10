package it.unibo.disi.pslab.traumatracker;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.json.JsonObject;
import it.unibo.disi.pslab.traumatracker.util.TTConfig;

public class TTServiceAgent extends Thread {

	// private String ttHome = "/Users/aricci/Development/Git-Projects/traumatracker/TraumaTrackerService";
	// private String gradleCmd = "/Users/aricci/Development/Tools/gradle-2.5/bin/gradle";
	private String ttServiceAddress = "localhost";
	// private HttpClient client;
	private TTAgentLogger view;
	private TTConfig config;
	private Vertx vertx;
	
	public TTServiceAgent(TTAgentLogger view, TTConfig config) {
		this.view = view;
		this.config = config;
		vertx = Vertx.vertx();
	}
	
	public void run(){
		log("Setting up TT Service ...");
		setupService();

		try {
			sleep(2000);
			checkService();

			while (true){
				sleep(3600000);
				checkService();
			}
		} catch (Exception ex){
			ex.printStackTrace();
		}
			
		/*
		log("Keep it.");
		try {
	
			Vertx vertx = Vertx.vertx();
			client = vertx
					.createHttpClient(new HttpClientOptions().setDefaultHost(ttServiceAddress).setDefaultPort(8080));

			checkService();
			
		} catch (Exception ex){
			ex.printStackTrace();
		}
		*/
	}
	
	protected void checkService(){
		try {
			log("Check service.");

			HttpClient client = vertx
				.createHttpClient(new HttpClientOptions().setDefaultHost(ttServiceAddress).setDefaultPort(8080));
				
			client.get("http://"+ttServiceAddress + "/gt2/traumatracker/api/state")
				.handler(this::handleGetReportInfo)
				.exceptionHandler(this::handleException)
			.end();
		} catch (Exception ex){
			ex.printStackTrace();
		}
		
	}
	protected void handleGetReportInfo(HttpClientResponse res) {
		log("Check service result: " + res.statusCode());
		res.bodyHandler(data -> {
			JsonObject ob = data.toJsonObject();
			
			log("Check service state - version: " + ob.getString("version")+ " | startTime: "+ob.getString("startTime"));
		});
	}

	protected void handleException(Throwable th) {
		log("exception " + th);
	}
	
	private void setupService(){
		try {
			Vertx vertx = Vertx.vertx();
			TTService ttService = new TTService(view,config);
			vertx.deployVerticle(ttService);
		} catch (Exception ex){
			ex.printStackTrace();
		}
	}

	private void log(String msg){
		System.out.println("[TTServiceAgent] "+msg);
		view.log("[TTServiceAgent] "+msg);
	}
	
	
}
