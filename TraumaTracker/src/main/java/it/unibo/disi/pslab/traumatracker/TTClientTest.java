package it.unibo.disi.pslab.traumatracker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import it.unibo.disi.pslab.traumatracker.ontology.Dictionary;
import it.unibo.disi.pslab.traumatracker.ontology.DrugAndInfusions;
import it.unibo.disi.pslab.traumatracker.ontology.FinalDestination;
import it.unibo.disi.pslab.traumatracker.ontology.ItalianDictionary;
import it.unibo.disi.pslab.traumatracker.ontology.Place;
import it.unibo.disi.pslab.traumatracker.ontology.TTDataModel;
import it.unibo.disi.pslab.traumatracker.ontology.VitalSigns;
import it.unibo.disi.pslab.traumatracker.util.TTConfig;

public class TTClientTest {

	private static final MongoClient clientDB = new MongoClient(TTConfig.server, TTConfig.port);
	private static final int NUM_MAX_DAYS = 465;
	private static final int MAX_TRAUMA_DAY = 4;
	private static MongoCollection<Document> collection;
	
	private static Calendar calendar = Calendar.getInstance();
	private static final SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
	private static final SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
	
	private static void initDb() {
		MongoDatabase db = clientDB.getDatabase(TTConfig.dbName);
		collection = db.getCollection(TTConfig.collectionName);
	}
	
	public static String dataEvent(final int numDays) {
		calendar.add(Calendar.DAY_OF_MONTH, numDays);
		return sdfDate.format(calendar.getTime());
	}
	
	public static String timeEvent(final int numMinute) {
		int numMinuteRandom = (int) (Math.random() * numMinute * 2); 
		calendar.add(Calendar.MINUTE, numMinuteRandom);
		final String time = sdfTime.format(calendar.getTime());
		return time;
	}

	public static String getFinalDestination(final Dictionary dict, final FinalDestination finalDestination) {
		final FinalDestination[] destValues = FinalDestination.values();
		final int posRand = (int) (Math.random() * destValues.length);
		return dict.get(destValues[posRand]);
	}
	
	public static String getPlace(final Dictionary dict, final Place place) {
		final Place[] placeValues = Place.values();
		final int posRand = (int) (Math.random() * placeValues.length);
		return dict.get(placeValues[posRand]);
	}
	
	public static String getDrugs(final Dictionary dict, final DrugAndInfusions drugs) {
		final DrugAndInfusions[] drugValues = DrugAndInfusions.values();
		final int posRand = (int) (Math.random() * drugValues.length);
		return dict.get(drugValues[posRand]);
	}
	
	private static List<JsonObject> createEvents(final TTDataModel model, final Dictionary dict) {
		return Arrays.asList(
				model.makeIntubationProcedureEventNote
				(1, dataEvent(0), timeEvent(0), getPlace(dict,Place.SHOCK_ROOM),
						"<desc>","<diff_air>","<inhalat>","<videolaringo>","<frova>"),
				
				model.makeAudioEventNote(2, dataEvent(0), timeEvent(10), getPlace(dict,Place.TAC)),

				model.makeDrainageProcedureEventNote
					(3, dataEvent(0), timeEvent(20), getPlace(dict,Place.SHOCK_ROOM),"<desc>","<right>","<left>"),

				model.makeVideoEventNote(4, dataEvent(0), timeEvent(30), getPlace(dict,Place.SHOCK_ROOM)),
				
				model.makeVideoEventNote(4, dataEvent(0), timeEvent(10), getPlace(dict,Place.SHOCK_ROOM)),
				
				model.makeHemostasisProcedureEventNote
					(5, dataEvent(0), timeEvent(20), getPlace(dict,Place.SHOCK_ROOM),"<desc>","<epistat>","<suture>","<compr>"),
				
				model.makeVitalSignsMonEventNote(6, dataEvent(0), timeEvent(30), getPlace(dict,Place.ANGIOGRAPHY_ROOM), 37.5,
						100, 180, 90, 95, 100),
				model.makePhotoEventNote(7, dataEvent(0), timeEvent(10), getPlace(dict,Place.TAC)),
				model.makeVitalSignsMonEventNote(8, dataEvent(0), timeEvent(20), getPlace(dict,Place.ANGIOGRAPHY_ROOM), 37.5,
						100, 180, 90, 95, 100),
				model.makePhotoEventNote(9, dataEvent(0), timeEvent(30), getPlace(dict,Place.TAC)),
				model.makeSkeletonRxDiagnosticEventNote
						(10, dataEvent(0), timeEvent(10), getPlace(dict,Place.SHOCK_ROOM), "<desc>","<tl>","<tr>","<bl>","<br>"),
				model.makeEgaDiagnosticEventNote(11, dataEvent(0), timeEvent(20), getPlace(dict,Place.OPERATING_THREATRE),
						"<desc>","<lactates>","<be>","<ph>","<hb>","<glycemia>"),
				model.makeVitalSignsMonEventNote(12, dataEvent(0), timeEvent(20), getPlace(dict,Place.ANGIOGRAPHY_ROOM), 37.5,
						100, 180, 90, 95, 100),
				model.makePhotoEventNote(13, dataEvent(0), timeEvent(10), getPlace(dict,Place.TAC)),
				model.makeRotemDiagnosticEventNote(14, dataEvent(0), timeEvent(20), getPlace(dict,Place.OPERATING_THREATRE),
						"<desc>","<fibtem>","<extem>","<hyperfibrinolysis>"),
				model.makeOneShotDrugEventNote(14, dataEvent(0), timeEvent(30), getPlace(dict,Place.OPERATING_THREATRE),
						getDrugs(dict,DrugAndInfusions.SUCCINYLCHOLINE), "<desc>", 20, "mg"),
				model.makeVitalSignsMonEventNote(15, dataEvent(0), timeEvent(30), getPlace(dict,Place.ANGIOGRAPHY_ROOM), 37.5,
						100, 180, 90, 95, 100),
				model.makePhotoEventNote(16, dataEvent(0), timeEvent(10), getPlace(dict,Place.TAC)),
				model.makeVitalSignEventNote(17, dataEvent(0), timeEvent(20), getPlace(dict,Place.TAC),
						dict.get(VitalSigns.EYES), "!test! non dilatate"),
				model.makeTimeDependentProcedureStartEventNote(18, dataEvent(0), timeEvent(30), getPlace(dict,Place.OPERATING_THREATRE),"als","als"),
				model.makeTimeDependentProcedureStopEventNote(18, dataEvent(0), timeEvent(10), getPlace(dict,Place.OPERATING_THREATRE),"als","als"),
				model.makeRoomInEventNote(20, dataEvent(0), timeEvent(20), getPlace(dict,Place.ANGIOGRAPHY_ROOM), getPlace(dict,Place.ANGIOGRAPHY_ROOM)),
				model.makeRoomOutEventNote(21, dataEvent(0), timeEvent(30), getPlace(dict,Place.SHOCK_ROOM), getPlace(dict,Place.OPERATING_THREATRE)),
				model.makeContinuousInfusionDrugStartEventNote(22, dataEvent(0), timeEvent(10), getPlace(dict,Place.OPERATING_THREATRE),
						"adrenaline-continuous-inf", "<desc>", 20, "mg"),
				model.makeContinuousInfusionDrugVariationEventNote(22, dataEvent(0), timeEvent(20), getPlace(dict,Place.OPERATING_THREATRE),
						"adrenaline-continuous-inf", "<desc>", 20, "mg"),
				model.makeContinuousInfusionDrugStopEventNote(22, dataEvent(0), timeEvent(30), getPlace(dict,Place.OPERATING_THREATRE),
						"adrenaline-continuous-inf", "<desc>",100),
				model.makeChestTubeProcedureEventNote
					(4, dataEvent(0), timeEvent(10), getPlace(dict,Place.SHOCK_ROOM),"<desc>","<right>","<left>")
				);
	}
	
	public static void main(String[] args) throws Exception {
		initDb();
		
		TTDataModel model = TTDataModel.instance();
		Dictionary dict = ItalianDictionary.instance();

		JsonObject startVitalSign = model.makeStartVitalSigns(
				"Normotermico", "bla bla","Bradicardico", "bla bla","Ipoteso", "bla bla","Ipossico","bla bla","Ipocapnico", "bla bla",
				"TT", "DX", "Normale", "bla bla", "3","4","5","si","si","bla bla",
				"bla bla","bla bla","bla bla","bla bla","bla bla","bla bla","bla bla","bla bla","bla bla","bla bla","bla bla","bla bla","bla bla",
				"bla bla","bla bla","bla bla","bla bla","bla bla","bla bla","bla bla","bla bla","bla bla","bla bla",
				"bla bla","bla bla", "bla bla", "bla bla", "bla bla", "bla bla"
				);

		Date initDate = sdfDate.parse("2017-01-01");
		calendar.setTime(initDate);
		
		for (int i = 0; i < NUM_MAX_DAYS; i++) {
//			impostazione dell'ora alle 07:00:00 di mattina			
			calendar.set(Calendar.HOUR_OF_DAY, 7);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			
			final int numTraumaDay = (int) (Math.random() * MAX_TRAUMA_DAY);
			
			List<JsonObject> events = new ArrayList<>();
			String startDate = dataEvent(1);
			
			for (int k = 0; k < numTraumaDay; k++) {
				final String startTime = timeEvent(30);
				startDate = dataEvent(0);			
				
				events = createEvents(model, dict);
				
				final String endTime = timeEvent(30);
				final String endDate = dataEvent(0);
				
				JsonObject report = model.makeReport(
						"alba", 
						"no",
						new String[]{"anestesista","medico ps","neurochirurgo"},
						startDate, startTime, endDate, endTime,
						model.makePatientInfo("76848", "788787", "maschio","adulto", "65-70","2017-11-10","12:10:20","bicicletta","sì: timbuctu"),
						getFinalDestination(dict, FinalDestination.INTENSIVE_CARE),
						startVitalSign, 
						model.makeAnamnesi("54", "3.3", "12"),
						events);
				
				final Document insDoc = Document.parse(report.encode());
				collection.insertOne(insDoc);
			}			
		}
	}

	
	public static void clearReports(String address, HttpClient client) {
		client
		.delete(address + "/gt2/traumatracker/api/reports").handler(res -> {
			System.out.println("response status " + res.statusCode());
		})
		.end();
	}

	
	public static void submitReport(String address, HttpClient client, JsonObject report) {
		Buffer buffer = Buffer.buffer(report.toString());
		client
				.post(address + "/gt2/traumatracker/api/reports").handler(res -> {
					System.out.println("response status " + res.statusCode());

					// submit photos
					System.out.println("Submitting event photos and audios ...");

					JsonArray events = report.getJsonArray("events");
					for (int i = 0; i < events.size(); i++) {
						JsonObject ev = events.getJsonObject(i);
						if (ev.getString("type").equals("photo")) {
							int evid = ev.getInteger("eventId");
							String photoRef = report.getString("_id") + "-" + evid + ".jpg";
							uploadPhoto(address, client, "data/photo.jpg", photoRef);
						} else if (ev.getString("type").equals("audio")){
							int evid = ev.getInteger("eventId");
							String audioRef = report.getString("_id") + "-" + evid + ".mp3";
							uploadAudio(address, client, "data/audio.mp3", audioRef);
						} else if (ev.getString("type").equals("video")){
							int evid = ev.getInteger("eventId");
							String videoRef = report.getString("_id") + "-" + evid + ".mp4";
							uploadVideo(address, client, "data/video.mp4", videoRef);
						}
					}

				})
				.setTimeout(1000).putHeader("content-type", "application/json")
				.putHeader(HttpHeaders.CONTENT_LENGTH, buffer.length() + "").write(buffer).end();

	}

	public static void resetReportsDB(HttpClient client) {
		client.delete("http://localhost:8080/gt2/traumatracker/api/reports").handler(res -> {
			System.out.println("response status " + res.statusCode());
			res.bodyHandler(data -> {
				JsonObject ob = data.toJsonObject();
				System.out.println("response body: " + ob);
			});
		}).setTimeout(1000).end();
	}

	public static void uploadPhoto(String address, HttpClient client, String sourceFileName, String targetFileName) {
		final HttpClientRequest req = client
				.post(address + "/gt2/traumatracker/photo", new Handler<HttpClientResponse>() {
					public void handle(HttpClientResponse response) {
						System.out.println("File " + sourceFileName + " => " + targetFileName + "  done: "
								+ response.statusCode());
					}
				}).setChunked(false).setTimeout(2000);

		Buffer bodyBuffer = getBody(sourceFileName, targetFileName);
		req.putHeader("Content-Type", "multipart/form-data; boundary=MyBoundary");
		req.putHeader("accept", "application/json");
		req.end(bodyBuffer);
		System.out.println("Update image request done.");
	}

	public static void uploadAudio(String address, HttpClient client, String sourceFileName, String targetFileName) {
		final HttpClientRequest req = client
				.post(address + "/gt2/traumatracker/audio", new Handler<HttpClientResponse>() {
					public void handle(HttpClientResponse response) {
						System.out.println("File " + sourceFileName + " => " + targetFileName + "  done: "
								+ response.statusCode());
					}
				}).setChunked(false).setTimeout(2000);

		Buffer bodyBuffer = getBody(sourceFileName, targetFileName);
		req.putHeader("Content-Type", "multipart/form-data; boundary=MyBoundary");
		req.putHeader("accept", "application/json");
		req.end(bodyBuffer);
		System.out.println("Update image request done.");
	}
	
	public static void uploadVideo(String address, HttpClient client, String sourceFileName, String targetFileName) {
		final HttpClientRequest req = client
				.post(address + "/gt2/traumatracker/video", new Handler<HttpClientResponse>() {
					public void handle(HttpClientResponse response) {
						System.out.println("File " + sourceFileName + " => " + targetFileName + "  done: "
								+ response.statusCode());
					}
				}).setChunked(false).setTimeout(2000);

		Buffer bodyBuffer = getBody(sourceFileName, targetFileName);
		req.putHeader("Content-Type", "multipart/form-data; boundary=MyBoundary");
		req.putHeader("accept", "application/json");
		req.end(bodyBuffer);
		System.out.println("Update video request done.");
	}

	private static Buffer getBody(String srcFileName, String targetFileName) {
		Buffer buffer = Buffer.buffer();
		buffer.appendString("--MyBoundary\r\n");
		buffer.appendString("Content-Disposition: form-data; name=\"image\"; filename=\"" + targetFileName + "\"\r\n");
		buffer.appendString("Content-Type: application/octet-stream\r\n");
		buffer.appendString("Content-Transfer-Encoding: binary\r\n");
		buffer.appendString("\r\n");
		try {
			buffer.appendBytes(Files.readAllBytes(Paths.get(srcFileName)));
			buffer.appendString("\r\n");
		} catch (IOException e) {
			e.printStackTrace();

		}
		buffer.appendString("--MyBoundary--\r\n");
		return buffer;
	}
}
