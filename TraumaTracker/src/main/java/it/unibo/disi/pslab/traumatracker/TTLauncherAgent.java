package it.unibo.disi.pslab.traumatracker;

//import io.vertx.core.Vertx;
import it.unibo.disi.pslab.traumatracker.util.TTConfig;

public class TTLauncherAgent {

	private static final String TT_CONFIG_FILE = "./config.json";
	
	public static void main(String[] args) throws Exception {
		
		TTAgentLogger logger = new TTAgentLogger();

		logger.log("Booting TraumaTracker System...");
		TTConfig config;
		try {
			config = new TTConfig(TT_CONFIG_FILE);
		} catch (Exception ex){
			logger.log("Config file not found or invalid");
			config = new TTConfig();
		}
		
		//new VPNManagerAgentOSX(logger,config).start();

		Thread.sleep(2000);

		new MongoDBAgent(logger,config).start();
	 
		Thread.sleep(5000);
		
		new TTServiceAgent(logger,config).start();

		logger.log("Booting Completed.");
		
	}
	
}
