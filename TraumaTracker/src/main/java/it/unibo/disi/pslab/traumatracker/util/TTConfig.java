package it.unibo.disi.pslab.traumatracker.util;

import it.unibo.disi.pslab.util.Config;

public class TTConfig extends Config {

	private static final String dbNameKey = "dbName";
	private static final String reportsCollectionNameKey = "reportsCollectionName";
	private static final String usersCollectionNameKey = "usersCollectionName";
	
	public static final String TT_VERSION = "v0.9.8-20170906";
	public static final String TT_CONFIG_FILE = "./config.json";
	
	public static final String server = "localhost";
	public static final int port = 27017;
	
	public static final String dbName = "ttservicedb";
	public static final String collectionName = "reports";
	
	public static final int MAX_RECENT_REPORTS = 20;
	
	public TTConfig(String fileName) throws InvalidConfigFileException {
		super(fileName);
	}
	
	/* default */
	public TTConfig(){
		config.put(dbNameKey, "ttservicedb");
		config.put(reportsCollectionNameKey, "reports");
		config.put(usersCollectionNameKey, "users");
	}
	
	public String getDBName(){
		return config.getString(dbNameKey);
	}
	
	public String getUsersCollectionName(){
		return config.getString(usersCollectionNameKey);
	}

	public String getReportsCollectionName(){
		return config.getString(reportsCollectionNameKey);
	}

}
