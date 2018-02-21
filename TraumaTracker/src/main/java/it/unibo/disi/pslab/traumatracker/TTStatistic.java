package it.unibo.disi.pslab.traumatracker;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.bson.Document;

import com.mongodb.client.FindIterable;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import it.unibo.disi.pslab.util.Util;
import it.unibo.disi.pslab.util.Util.*;

public class TTStatistic {
	
	private static JsonObject createCartesianChart(final Map<String, Integer> map) {
		List<Entry<String,Integer>> elems = 
				map.entrySet().
				stream().sorted((Entry<String,Integer> a, Entry<String,Integer> b) -> 
				a.getKey().compareTo(b.getKey())).collect(Collectors.toList());

		JsonObject stat = new JsonObject();
		JsonArray dataX = new JsonArray();		
		JsonArray dataY = new JsonArray();

		for (Entry<String, Integer> elem: elems){
			dataX.add(elem.getKey());
			dataY.add(elem.getValue());
		}

		stat.put("x", dataX);
		stat.put("y", dataY);
		return stat;
	}
	
	private static JsonObject createCartesianChart(final JsonArray jsonArray) {
		final int dim = jsonArray.size();
		
		JsonObject stat = new JsonObject();
		
		JsonArray dataX1 = new JsonArray();
		JsonArray dataX2 = new JsonArray();
		JsonArray dataX3 = new JsonArray();
		
		JsonArray dataY = new JsonArray();
		
		for (int i = 0; i < dim; i++) {
			final JsonObject json = jsonArray.getJsonObject(i);
			
			final String drugId = json.getString(InfoEvents.DRUG_ID.getPath());
			final String unit = json.getString(InfoEvents.UNIT.getPath());
			final Double quantita = json.getDouble(InfoEvents.QUANTITY.getPath());
			final Integer durata = json.getInteger(InfoEvents.DURATION.getPath());
			
			if (validityString(drugId)) {
				dataY.add(drugId);
				
				if (validityString(unit)) {
					dataX1.add(unit);
				}
				if (quantita != null) {
					dataX2.add(quantita);
				}
				if (durata != null) {
					dataX3.add(durata);
				}
			}
		}
		
		stat.put("x1", dataX1);
		stat.put("x2", dataX2);
		stat.put("x3", dataX3);
		stat.put("y", dataY);
		
		return stat;
	}
	
	private static JsonObject generateJson(final Map<String, Integer> map) {
		final JsonObject json = createCartesianChart(map);
		json.put("media", (int) avgMapValue(map));
		return json;
	}
	
	private static void addMapValue(Map<String, Integer> map, final String string, int basicValue) {
		final Integer num = map.get(string);
		if (num == null) { map.put(string, basicValue); } 
					else { map.put(string, num+basicValue); }
	}
	
	// STAT AUX
	private static boolean validityString(final String string) {
		return string != null && !string.equals(Util.strVoid);
	}
	
	private static double avgMapValue(Map<String, Integer> map) {
	    // avarage calcolation
	    int sum = map.values().stream().mapToInt(i -> i.intValue()).sum();
	    return (sum / map.keySet().size());
	}
	
	private static JsonObject dataGetNumTraumaMonth(final FindIterable<Document> search) {
		final TreeMap<String,Integer> freqMonth = new TreeMap<>();
		
		for (Document doc : search) {
		    String strDate = doc.getString(InfoReports.START_DATE.getPath());								
			addMapValue(freqMonth, strDate.substring(0, 7), 1);
		}

		return generateJson(freqMonth);
	}
	
	private static JsonObject dataGetNumTraumaWeek(final FindIterable<Document> search) throws ParseException {
		final TreeMap<String,Integer> numWeek = new TreeMap<>();
		
		SimpleDateFormat df = new SimpleDateFormat(Util.formatString1);
		Calendar cal = new GregorianCalendar();
		NumberFormat weekFormat = new DecimalFormat(Util.DecimalFormatString);
		
		for (Document doc : search) {
	    	String strDate = doc.getString(InfoReports.START_DATE.getPath());
	    	Date data = df.parse(strDate);
			
			cal.setTime(data);
			int week = cal.get(Calendar.WEEK_OF_MONTH);
			
			if (week <= 0) { /* conto settimana da 1*/week++;}
			String key = strDate.substring(0, 8).concat(weekFormat.format(week)); // chiave (data, mese, settimana)
			
			addMapValue(numWeek, key, 1);
		}

		return generateJson(numWeek);
	}
	
	private static JsonObject dataGetNumTraumaDay(final FindIterable<Document> search) {
		final TreeMap<String, Integer> numDay = new TreeMap<>();
		
		for (Document doc : search) {
			addMapValue(numDay, doc.getString(InfoReports.START_DATE.getPath()), 1);
		}

		return generateJson(numDay);
	}
	
	private static JsonObject dataAvarageTraumaTime(final FindIterable<Document> search) {
		final TreeMap<String, Integer> avgTraumaTime = new TreeMap<>();
	    
	    LocalDate initDate;
	    LocalDate endDate;
	    LocalTime initTime;
	    LocalTime endTime;
	    
	    int numRep = 0;
	    
	    for (Document doc : search) {
	    	initDate = LocalDate.parse(doc.getString(InfoReports.START_DATE.getPath()));
	    	endDate = LocalDate.parse(doc.getString(InfoReports.END_DATE.getPath()));
	    	
	    	initTime = LocalTime.parse(doc.getString(InfoReports.START_TIME.getPath()));
	    	endTime = LocalTime.parse(doc.getString(InfoReports.END_TIME.getPath()));
	    	
	    	Period periodDate = Period.between(initDate, endDate);
	    	
	    	int periodMinute = (periodDate.getYears() * Util.numDayYear * Util.numHourDay * Util.numMinHour + 
	    			periodDate.getMonths() * Util.numDayMonth * Util.numHourDay * Util.numMinHour +
	    			periodDate.getDays() * Util.numHourDay * Util.numMinHour);
	    	
	    	int diffMinute = (endTime.toSecondOfDay() - initTime.toSecondOfDay()) / Util.numMinHour;		    	
	    	int timeTrauma = periodMinute + diffMinute;
	    	
	    	// formato con tre cifre, per permettere ordinamento migliore
	    	final String key = String.format("%s %03d", "T", (numRep+1));
	    	
	    	avgTraumaTime.put(key, timeTrauma);
	    	numRep++;
	    }
	    
	    return generateJson(avgTraumaTime);
	}
	
	private static JsonObject dataGetAvarageTimeRoom(final FindIterable<Document> search) {
		final TreeMap<String, Integer> timeRoom = new TreeMap<>();
			
		for (Document doc : search) {
		    List<Document> listEvents = (List<Document>) doc.get(InfoEvents.EVENTS.getPath());
		    for (Document event : listEvents) {
		    	
				final String type = event.getString(InfoEvents.EVENT_TYPE.getPath());
				final LocalTime endTime = LocalTime.parse(event.getString(InfoEvents.EVENT_TIME.getPath()));
				
				if (validityString(type) && 
						(type.equals("room-in") || 
								type.equals("room-out"))) {
					
					Document content = event.get(InfoEvents.CONTENT_EVENT.getPath(), Document.class);
					addMapValue(timeRoom, content.getString("place"), 
							endTime.getHour() * Util.numMinHour + endTime.getMinute());
				}
		    }
		}

	    return generateJson(timeRoom);
	}
	
	private static JsonObject dataGetConsumption(final FindIterable<Document> search) {
		final JsonArray results = new JsonArray();
		final List<String> drug = new ArrayList<>();
		
		for (Document doc : search) {
			List<Document> listEvents = (List<Document>) doc.get(InfoEvents.EVENTS.getPath());
			if (listEvents != null && !listEvents.isEmpty()) {
				
				for (Document event : listEvents) {
					final String type = event.getString(InfoEvents.EVENT_TYPE.getPath());						
					if (validityString(type) && type.equals(InfoEvents.DRUG.getPath())) {
						
						Document content = event.get(InfoEvents.CONTENT_EVENT.getPath(), Document.class);						
						if (content != null) {
							
							final String drugId = content.getString(InfoEvents.DRUG_ID.getPath());
							final String unit = content.getString(InfoEvents.UNIT.getPath());										
							final Double qty = content.getDouble(InfoEvents.QUANTITY.getPath());
							final Integer duration = content.getInteger(InfoEvents.DURATION.getPath());
							
							if (validityString(drugId)) {
								if (!drug.contains(drugId)) { //  Add new drugs info
									final JsonObject jsObj = new JsonObject();
									
									if (qty != null) {
										jsObj.put(InfoEvents.QUANTITY.getPath(), qty);
									}
									if (duration != null) {
										jsObj.put(InfoEvents.DURATION.getPath(), duration);
									}
									if (validityString(unit)) {
										jsObj.put(InfoEvents.UNIT.getPath(), unit);
									}
									jsObj.put(InfoEvents.DRUG_ID.getPath(), drugId);
									
									drug.add(drugId);
									results.add(jsObj);
								} else {   //  Update drugs info
									for (int j = 0; j < results.size(); j++) {
										final JsonObject jsonObj = results.getJsonObject(j);
										
										if (jsonObj.getString(InfoEvents.DRUG_ID.getPath()).equals(drugId)) { // Update info											
											final Double tempQuantita = jsonObj.getDouble(InfoEvents.QUANTITY.getPath());
											final Integer tempDurata = jsonObj.getInteger(InfoEvents.DURATION.getPath());
											
											if (qty != null && tempQuantita != null) {
												jsonObj.put(InfoEvents.QUANTITY.getPath(), tempQuantita+qty);
											}
											
											if (duration != null && tempDurata != null) {
												jsonObj.put(InfoEvents.DURATION.getPath(), tempDurata+duration);
											} else if (duration != null) {
												jsonObj.put(InfoEvents.DURATION.getPath(), duration);
											} else if (tempDurata != null) {
												jsonObj.put(InfoEvents.DURATION.getPath(), tempDurata);
											}
											break;
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return createCartesianChart(results);
	}
	
	private static JsonObject dataNumFinalDestination(final FindIterable<Document> search) {
		final TreeMap<String, Integer> finalDestInterv = new TreeMap<>();
		JsonObject jsonObject = new JsonObject();
		for (Document doc : search) {
			final String finalDest = doc.getString("finalDestination");

			addMapValue(finalDestInterv, finalDest, 1);
		}
		
		final JsonArray keyArray = new JsonArray();
		final JsonArray intArray = new JsonArray();
		
		for (String key : finalDestInterv.keySet()) {
			keyArray.add(key);
			intArray.add(finalDestInterv.get(key));
		}

		jsonObject.put("keyArray", keyArray);
		jsonObject.put("intArray", intArray);
		jsonObject.put("totIntervString", finalDestInterv.values().size());
		
		return jsonObject;
	}
	
	private static JsonObject dataGetMaxProcedure(final FindIterable<Document> search) {
		final TreeMap<String, Integer> maxProcedure = new TreeMap<>();
		
		for (Document doc : search) {
			List<Document> listEvents = (List<Document>) doc.get(InfoEvents.EVENTS.getPath());
			
			for (Document event : listEvents) {
				final String type = event.getString(InfoEvents.EVENT_TYPE.getPath());
				if (validityString(type) && type.equals("procedure")) {
					Document content = event.get(InfoEvents.CONTENT_EVENT.getPath(), Document.class);
					
					final String procId = content.getString("procedureId");
					if (validityString(procId)) {
						addMapValue(maxProcedure, procId, 1);
					}
				}
			}
		}

	    return generateJson(maxProcedure);
	}
	
	public static JsonObject handleFunction(final Commands command, final FindIterable<Document> search) throws ParseException {
		JsonObject jsonObj = new JsonObject();
		
		switch (command) {
			case TraumaMonth: 
				jsonObj = dataGetNumTraumaMonth(search);
				break;
			case TraumaWeek: 
				jsonObj = dataGetNumTraumaWeek(search);
				break;
			case TraumaDay: 
				jsonObj = dataGetNumTraumaDay(search);
				break;
			case TraumaTime: 
				jsonObj = dataAvarageTraumaTime(search);
				break;
			case TimeRoom: 
				jsonObj = dataGetAvarageTimeRoom(search);
				break;
			case TypeDrug:
				jsonObj = dataGetConsumption(search);
				break;
			case FinalDestination: 
				jsonObj = dataNumFinalDestination(search);
				break;
			case MaxProcedure:
				jsonObj = dataGetMaxProcedure(search);
				break;
		}
		
		return jsonObj;
	}
}
