package it.unibo.disi.pslab.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;

import io.vertx.core.json.JsonObject;
import it.unibo.disi.pslab.traumatracker.util.InvalidConfigFileException;

public class Config {
	
	protected JsonObject config;
	
	public Config(String fileName) throws InvalidConfigFileException {
		StringBuffer buffer = new StringBuffer();
		try {
			BufferedReader fr = new BufferedReader(new FileReader(fileName));
			fr.lines().forEach(line -> {
				buffer.append(line);
			});
			fr.close();
		} catch (Exception ex){
			try {
				new FileWriter(fileName).close();
			} catch (Exception ex2){
			}
		}
		config = new JsonObject(buffer.toString());
	}
	
	public Config() {
		config = new JsonObject();
	}
	
	
	public int getInt(String key){
		return config.getInteger(key);
	}

	public String getString(String key){
		return config.getString(key);
	}

	public boolean getBoolean(String key){
		return config.getBoolean(key);
	}
	
	public JsonObject getJsonObj(String key){
		return config.getJsonObject(key);
	}

}
