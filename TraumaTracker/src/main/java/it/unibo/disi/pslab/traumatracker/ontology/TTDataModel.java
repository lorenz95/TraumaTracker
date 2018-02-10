package it.unibo.disi.pslab.traumatracker.ontology;

import java.util.List;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class TTDataModel {

	private int idRepoCount;
	private static TTDataModel instance;
	
	static public TTDataModel instance(){
		synchronized (TTDataModel.class){
			if (instance == null){
				instance = new TTDataModel();
				return instance;
			} else {
				return instance;
			}
		}
	}
	
	protected TTDataModel(){
		setIdRepoCount(1);
	}
	
	public JsonObject makeReport(
			String startOperatorId, 
			String secondaryActivation,
			String[] traumaTeamMembers, 
			String startDate, String startTime, String endDate, String endTime,
			JsonObject patientInfo,
			String dest, JsonObject startVitalSigns, 
			JsonObject anamnesi,
			List<JsonObject> nlist) {

		JsonArray events = new JsonArray();
		for (JsonObject obj : nlist) {
			events.add(obj);
		}

		String date = startDate.replace("-", "");
		String time = startTime.replace(":", "");
		String repId = "rep-" + date + "-" +time;

		JsonArray ttTeamMembers = new JsonArray();
		for (String s: traumaTeamMembers){
			ttTeamMembers.add(s);
		}
		
		JsonObject report = new JsonObject();
		report
			.put("_id", repId)
		    .put("secondaryActivation", secondaryActivation)
		    .put("startOperatorId", startOperatorId)
		    .put("traumaTeamMembers", ttTeamMembers)
			.put("startDate", startDate)
			.put("startTime", startTime)
			.put("endDate", endDate)
			.put("endTime", endTime)
			.put("patientInfo", patientInfo)
			.put("finalDestination", dest)
			.put("startVitalSigns",startVitalSigns)
			.put("anamnesi", anamnesi)
			.put("events", events);
		return report;
	}

	public JsonObject makePatientInfo(String code, String sdo, String gender, String type, String age, String accidentDate, String accidentTime, String vehicle, String fromOtherEmergency){
		JsonObject info = new JsonObject();
		info
			.put("code", code)
			.put("sdo", sdo)
			.put("gender", gender)
			.put("type", type)
			.put("age", age)
			.put("accidentDate", accidentDate)
			.put("accidentTime", accidentTime)
			.put("vehicle",vehicle)
		    .put("fromOtherEmergency",fromOtherEmergency);
		return info;
	}	
	
	public JsonObject makeUser(String surname, String name, String userId){
		JsonObject user = new JsonObject();
		user
			.put("name", name)
			.put("surname", surname)
			.put("userId", userId);
		return user;
	}	

	public JsonObject makeBasicEventNote(int id, String date, String time, String place) {
		JsonObject note = new JsonObject()
				.put("eventId", id)
				.put("date", date)
				.put("time", time)
				.put("place", place);
		return note;
	}

	public JsonObject makeAnamnesi(String antiplatelets, String anticoagulants, String nao) {
		JsonObject info = new JsonObject()
				.put("antiplatelets", antiplatelets)
				.put("anticoagulants", anticoagulants)
				.put("nao", nao);
		return info;
	}
	
	public JsonObject makeStartVitalSigns(
			String Temp, String tempValue, String hr, String hrValue, String bp, String bpValue, String spO2, String spO2Value, String etCO2, String etCO2Value,
			String airway, String tracheo, String inhalation, String intubationFailed, String chestTube, String oxygenPercentage, String pupils, String gcsTotal, String gcsMotor, String gcsVerbal, String gcsEyes, String sedated, String extBleeding,
			String eyesDeviation, String earsBlood, 
			String upperLimbsRightMotility,String upperLimbsLeftMotility, String lowerLimbsRightMotility, String lowerLimbsLeftMotility,
			String upperLimbsRightSensitivity,String upperLimbsLeftSensitivity, String lowerLimbsRightSensitivity, String lowerLimbsLeftSensitivity,
			String sphincterTone, String priapism, 
			String upperLimbsRightPeripheralWrists,String upperLimbsLeftPeripheralWrists, String lowerLimbsRightPeripheralWrists, String lowerLimbsLeftPeripheralWrists,
			String peripherals, String intraosseous, String cvc,
			String limbsFracture, String fractureExposition, String fractureGustiloDegree, 
			String unstableBasin, 
			String burn, String burnDegree, String burnPercentage 
			) {
		return new JsonObject()
				.put("Temp", Temp)
				.put("TempValue", tempValue)
				.put("HR", hr)
				.put("HRValue", hrValue)
				.put("BP",bp)
				.put("BPValue",bpValue)
				.put("SpO2", spO2)
				.put("SpO2Value", spO2Value)
				.put("EtCO2", etCO2)
				.put("EtCO2Value", etCO2Value)
				.put("Airway", airway)
				.put("Tracheo", tracheo)
				.put("Inhalation", inhalation)
				.put("IntubationFailed", intubationFailed)
				.put("ChestTube", chestTube)
				.put("OxygenPercentage", oxygenPercentage)
				.put("Pupils", pupils)
				.put("GCSTotal", gcsTotal)
				.put("GCSMotor", gcsMotor)
				.put("GCSVerbal", gcsVerbal)
				.put("GCSEyes", gcsEyes)
				.put("Sedated", sedated)
				.put("ExtBleeding", extBleeding)
				.put("EyesDeviation",eyesDeviation)
				.put("EarsBlood",earsBlood)
				.put("UpperLimbsRightMotility", upperLimbsRightMotility)
				.put("UpperLimbsLeftMotility", upperLimbsLeftMotility)
				.put("LowerLimbsRightMotility", lowerLimbsRightMotility)
				.put("LowerLimbsLeftMotility", lowerLimbsLeftMotility)
				.put("UpperLimbsRightSensitivity", upperLimbsRightSensitivity)
				.put("UpperLimbsLeftSensitivity", upperLimbsLeftSensitivity)
				.put("LowerLimbsRightSensitivity", lowerLimbsRightSensitivity)
				.put("LowerLimbsLeftSensitivity", lowerLimbsLeftSensitivity)
				.put("SphincterTone",sphincterTone)
				.put("Priapism",priapism)
				.put("UpperLimbsRightPeripheralWrists", upperLimbsRightPeripheralWrists)
				.put("UpperLimbsLeftPeripheralWrists", upperLimbsLeftPeripheralWrists)
				.put("LowerLimbsRightPeripheralWrists", lowerLimbsRightPeripheralWrists)
				.put("LowerLimbsLeftPeripheralWrists", lowerLimbsLeftPeripheralWrists)
				.put("Peripherals", peripherals)
				.put("Intraosseous", intraosseous)
				.put("Cvc",cvc)
				.put("LimbsFracture",limbsFracture)
				.put("FractureExposition", fractureExposition)
				.put("FractureGustiloDegree", fractureGustiloDegree)
				.put("UnstableBasin", unstableBasin)
				.put("Burn", burn)
				.put("BurnDegree", burnDegree)
				.put("BurnPercentage", burnPercentage);
				
	}
	
	
	public JsonObject makeOneShotProcedureEventNote(int id, String date, String time, String place, String what, String desc) {
		return makeBasicEventNote(id, date, time, place)
				.put("type", "procedure")
				.put("content", new JsonObject()
						.put("procedureId",what)
						.put("procedureDescription", desc)
						.put("procedureType", "one-shot"));
	}

	protected JsonObject makeTimeDependentProcedureEventNote(int id, String date, String time, String place, String what, String desc, String event) {
		return makeBasicEventNote(id, date, time, place)
				.put("type", "procedure")
				.put("content", new JsonObject()
						.put("procedureId",what)
						.put("procedureDescription", desc)
						.put("procedureType", "time-dependent")
						.put("event",event));
	}

	public JsonObject makeTimeDependentProcedureStartEventNote(int id, String date, String time, String place, String what, String desc) {
		return makeTimeDependentProcedureEventNote(id, date, time, place, what, desc, "start");
	}

	public JsonObject makeTimeDependentProcedureStopEventNote(int id, String date, String time, String place, String what, String desc) {
		return makeTimeDependentProcedureEventNote(id, date, time, place, what, desc, "stop");
	}
	
	
	public JsonObject makeIntubationProcedureEventNote
		(int id, String date, String time, String place, String desc, 
				String difficultAirway, String inhalation, String videolaringo, String frova) {
		return makeOneShotProcedureEventNote(id, date, time, place, "intubation", desc)
				.put("content", new JsonObject()
						.put("difficultAirway",difficultAirway)
						.put("inhalation", inhalation)
						.put("videolaringo",videolaringo)
						.put("frova", frova));					
	}
	
	public JsonObject makeDrainageProcedureEventNote
				(int id, String date, String time, String place, String desc, 
						String left, String right) {
		JsonObject note =  makeOneShotProcedureEventNote(id, date, time, place, "drainage", desc);
		note.getJsonObject("content")
					.put("left",left)
					.put("right", right);					
		return note;
	}

	public JsonObject makeChestTubeProcedureEventNote
				(int id, String date, String time, String place, String desc, 
						String left, String right) {
		JsonObject note =  makeOneShotProcedureEventNote(id, date, time, place, "chest-tube", desc);
		note.getJsonObject("content")
						.put("left",left)
						.put("right", right);	
		return note;
	}

	public JsonObject makeHemostasisProcedureEventNote
				(int id, String date, String time, String place, String desc, 
						String epistat, String suture, String compression) {
		JsonObject note =   makeOneShotProcedureEventNote(id, date, time, place, "hemostasis", desc);
		note.getJsonObject("content")
								.put("epistat",epistat)
								.put("suture", suture)
								.put("compression", compression);				
		return note;
	}
	
	
	public JsonObject makeDiagnosticEventNote(int id, String date, String time, String place, String what, String desc) {
		return makeBasicEventNote(id, date, time, place)
				.put("type", "diagnostic")
				.put("content", new JsonObject()
						.put("diagnosticId",what)
						.put("diagnosticDescription", desc));
	}
	
	public JsonObject makeSkeletonRxDiagnosticEventNote
				(int id, String date, String time, String place, String desc,
				String topLeft, String topRight, String bottomLeft, String bottomRight) {
		JsonObject note =   makeDiagnosticEventNote(id, date, time, place, "skeleton-rx", desc);
		
		note.getJsonObject("content")
						.put("topLeft", topLeft)
						.put("topRight", topRight)
						.put("bottomLeft", bottomLeft)
						.put("bottomRight", bottomRight);
		return note;
	}

	public JsonObject makeEgaDiagnosticEventNote
			(int id, String date, String time, String place, String desc,
				String lactates, String be, String ph, String hb, String glycemia) {
		JsonObject note =   makeDiagnosticEventNote(id, date, time, place, "ega", desc);
		
		note.getJsonObject("content")
					.put("lactates", lactates)
					.put("be", be)
					.put("ph",ph)
					.put("hb",hb)
					.put("glycemia",glycemia);
		return note;
	}
	
	public JsonObject makeRotemDiagnosticEventNote
			(int id, String date, String time, String place, String desc,
					String fibtem, String extem, String hyperfibrinolysis) {
		JsonObject note =   makeDiagnosticEventNote(id, date, time, place, "rotem", desc);
		note.getJsonObject("content")
					.put("fibtem", fibtem)
					.put("extem", extem)
					.put("hyperfibrinolysis", hyperfibrinolysis);
		return note;
	}


	public  JsonObject makeOneShotDrugEventNote(int id, String date, String time, String place, String drugId, String desc, double qty,
			String unit) {
		return makeBasicEventNote(id, date, time, place)
				.put("type", "drug")
				.put("content", new JsonObject()
						.put("drugId", drugId)
						.put("drugDescription",desc)
						.put("administrationType", "one-shot")
						.put("unit", unit)
						.put("qty", qty));
	}
	
	protected  JsonObject makeContinuousInfusionDrugEventNote(int id, String date, String time, String place, String drugId, String desc) {
		return makeBasicEventNote(id, date, time, place)
				.put("type", "drug")
				.put("content", new JsonObject()
						.put("drugId", drugId)
						.put("drugDescription",desc)
						.put("administrationType", "continuous-infusion"));
	}

	public  JsonObject makeContinuousInfusionDrugStartEventNote(int id, String date, String time, String place, String drugId, String desc, double qty,
			String unit) {
		JsonObject note =   makeContinuousInfusionDrugEventNote(id, date, time, place, drugId, desc);
		note.getJsonObject("content")
					.put("event", "start")
					.put("unit", unit)
					.put("qty", qty);					
		return note;
	}

	public  JsonObject makeContinuousInfusionDrugVariationEventNote(int id, String date, String time, String place, String drugId, String desc, double qty,
			String unit) {
		JsonObject note =   makeContinuousInfusionDrugEventNote(id, date, time, place, drugId, desc);
		note.getJsonObject("content")
					.put("event", "variation")
					.put("unit", unit)
					.put("qty", qty);					
		return note;
	}
	
	public  JsonObject makeContinuousInfusionDrugStopEventNote(int id, String date, String time, String place, String drugId, String desc, int duration) {
		JsonObject note =   makeContinuousInfusionDrugEventNote(id, date, time, place, drugId, desc);
		note.getJsonObject("content")
					.put("duration", duration)
					.put("event", "stop");
		return note;
	}

	
	public JsonObject makeVitalSignsMonEventNote(int id, String date, String time, String place,
			double Temp, int hr, int dia, int sys,  double spO2, double etCO2) {
		return makeBasicEventNote(id, date, time, place)
				.put("type", "vital-signs-mon")
				.put("content", new JsonObject()
					.put("Temp", Temp)
					.put("HR", hr)
					.put("DIA",dia)
					.put("SYS",sys)
					.put("SpO2", spO2)
					.put("EtCO2", etCO2));
	}

	public  JsonObject makeVitalSignEventNote(int id, String date, String time, String place, 
			String name, String value) {
		return makeBasicEventNote(id, date, time, place)
				.put("type", "vital-sign")
				.put("content", new JsonObject()
						.put("name", name)
						.put("value", value));
	}

	public  JsonObject makeReportReactivationEventNote(int id, String date, String time, String place) {
		try {
			return makeBasicEventNote(id, date, time, place)
					.put("type", "report-reactivation");
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	public  JsonObject makePhotoEventNote(int id, String date, String time, String place) {
		try {
			return makeBasicEventNote(id, date, time, place)
					.put("type", "photo");
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public  JsonObject makeVideoEventNote(int id, String date, String time, String place) {
		try {
			return makeBasicEventNote(id, date, time, place)
					.put("type", "video");
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public  JsonObject makeAudioEventNote(int id, String date, String time, String place) {
		try {
			return makeBasicEventNote(id, date, time, place)
					.put("type", "vocal-note");
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public  JsonObject makeTextNoteEvent(int id, String date, String time, String place, String text) {
		return makeBasicEventNote(id, date, time, place)
				.put("type", "vital-sign")
				.put("content", new JsonObject()
						.put("text", text));
	}
	
	/*
	public  JsonObject makeALSStartEventNote(int id, String date, String time, String place) {
		return makeBasicEventNote(id, date, time, place)
				.put("type", "als-start");
	}

	public  JsonObject makeALSStopEventNote(int id, String date, String time, String place, int duration) {
		return makeBasicEventNote(id, date, time, place)
				.put("type", "als-stop")
				.put("content",new JsonObject()
						.put("duration", duration));
	}*/

	public  JsonObject makeRoomInEventNote(int id, String date, String time, String place, String roomInPlace) {
		return makeBasicEventNote(id, date, time, place)
				.put("type", "room-in")
				.put("content",new JsonObject()
						.put("place", roomInPlace));
	}

	public  JsonObject makeRoomOutEventNote(int id, String date, String time, String place, String roomOutPlace) {
		return makeBasicEventNote(id, date, time, place)
				.put("type", "room-out")
				.put("content",new JsonObject()
						.put("place", roomOutPlace));

	}


	
	/**
	 * Check report syntax & semantics
	 * 
	 * @TODO to be completed
	 * @param report
	 * @return
	 */
	public  boolean checkReportCorrectness(JsonObject report) {
		if (report != null) {
			return true;
		} else {
			return false;
		}
	}
	
	
	/**
	 * Check user syntax & semantics
	 * 
	 * @TODO to be completed
	 * @param user
	 * @return
	 */
	public  boolean checkUserCorrectness(JsonObject report) {
		/* TO BE COMPLETED */
		if (report != null) {
			return true;
		} else {
			return false;
		}
	}	
	public  String parseDate(JsonObject date) {
		return date.getString("date");
	}

	public  String parseTime(JsonObject time) {
		return time.getString("time");
	}

	public int getIdRepoCount() {
		return idRepoCount;
	}

	public void setIdRepoCount(int idRepoCount) {
		this.idRepoCount = idRepoCount;
	}

}
