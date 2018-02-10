package it.unibo.disi.pslab.traumatracker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
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
//import it.unibo.disi.pslab.traumatracker.ontology.ResuscitationManeuvers;
import it.unibo.disi.pslab.traumatracker.ontology.TTDataModel;
import it.unibo.disi.pslab.traumatracker.ontology.VitalSigns;

public class TTClient {

	public static void main(String[] args) throws Exception {

		String address = "localhost:8080"; //127.0.0.1:8080";
		
		Vertx vertx = Vertx.vertx();
		
		HttpClientOptions options = new HttpClientOptions().setDefaultHost("localhost").setDefaultPort(8080);
		
		HttpClient client = vertx
				.createHttpClient(options);
		
		/* clear reports */
		clearReports(address, client);
		
		Thread.sleep(1000);
		
		TTDataModel model = TTDataModel.instance();
		Dictionary dict = ItalianDictionary.instance();
		
		List<JsonObject> events = Arrays.asList(
				model.makeIntubationProcedureEventNote
				(1, "2016-01-02", "07:00:00", dict.get(Place.SHOCK_ROOM),
						"<desc>","<diff_air>","<inhalat>","<videolaringo>","<frova>"),
				
				model.makeAudioEventNote(2, "2016-01-02", "11:40:00", dict.get(Place.TAC)),

				model.makeDrainageProcedureEventNote
					(3, "2016-01-02", "11:15:00", dict.get(Place.SHOCK_ROOM),"<desc>","<right>","<left>"),

				model.makeVideoEventNote(4, "2016-01-02", "11:15:01", dict.get(Place.SHOCK_ROOM)),

				model.makeHemostasisProcedureEventNote
					(5, "2016-01-02", "11:18:00", dict.get(Place.SHOCK_ROOM),"<desc>","<epistat>","<suture>","<compr>"),
				
				model.makeVitalSignsMonEventNote(6, "2016-01-02", "11:20:00", dict.get(Place.ANGIOGRAPHY_ROOM), 37.5,
						100, 180, 90, 95, 100),
				model.makePhotoEventNote(7, "2016-01-02", "11:40:00", dict.get(Place.TAC)),
				model.makeVitalSignsMonEventNote(8, "2016-01-02", "11:20:00", dict.get(Place.ANGIOGRAPHY_ROOM), 37.5,
						100, 180, 90, 95, 100),
				model.makePhotoEventNote(9, "2016-01-02", "11:40:00", dict.get(Place.TAC)),
				model.makeSkeletonRxDiagnosticEventNote
						(10, "2016-01-02", "11:45:00", dict.get(Place.SHOCK_ROOM), "<desc>","<tl>","<tr>","<bl>","<br>"),
				model.makeEgaDiagnosticEventNote(11, "2016-01-02", "11:10:00", dict.get(Place.OPERATING_THREATRE),
						"<desc>","<lactates>","<be>","<ph>","<hb>","<glycemia>"),
				model.makeVitalSignsMonEventNote(12, "2016-01-02", "11:20:00", dict.get(Place.ANGIOGRAPHY_ROOM), 37.5,
						100, 180, 90, 95, 100),
				model.makePhotoEventNote(13, "2016-01-02", "11:40:00", dict.get(Place.TAC)),
				model.makeRotemDiagnosticEventNote(14, "2016-01-02", "11:10:00", dict.get(Place.OPERATING_THREATRE),
						"<desc>","<fibtem>","<extem>","<hyperfibrinolysis>"),
				model.makeOneShotDrugEventNote(14, "2016-01-02", "11:10:00", dict.get(Place.OPERATING_THREATRE),
						dict.get(DrugAndInfusions.SUCCINYLCHOLINE), "<desc>", 20, "mg"),
				model.makeVitalSignsMonEventNote(15, "2016-01-02", "11:20:00", dict.get(Place.ANGIOGRAPHY_ROOM), 37.5,
						100, 180, 90, 95, 100),
				model.makePhotoEventNote(16, "2016-01-02", "11:40:00", dict.get(Place.TAC)),
				model.makeVitalSignEventNote(17, "2016-01-02", "11:40:00", dict.get(Place.TAC),
						dict.get(VitalSigns.EYES), "!test! non dilatate"),
				model.makeTimeDependentProcedureStartEventNote(18, "2016-01-02", "11:10:00", dict.get(Place.OPERATING_THREATRE),"als","als"),
				model.makeTimeDependentProcedureStopEventNote(18, "2016-01-02", "11:12:00", dict.get(Place.OPERATING_THREATRE),"als","als"),
				model.makeRoomInEventNote(20, "2016-01-02", "11:10:00", dict.get(Place.ANGIOGRAPHY_ROOM), dict.get(Place.ANGIOGRAPHY_ROOM)),
				model.makeRoomOutEventNote(21, "2016-01-02", "11:10:00", dict.get(Place.SHOCK_ROOM), dict.get(Place.OPERATING_THREATRE)),
				model.makeContinuousInfusionDrugStartEventNote(22, "2016-01-02", "11:10:00", dict.get(Place.OPERATING_THREATRE),
						"adrenaline-continuous-inf", "<desc>", 20, "mg"),
				model.makeContinuousInfusionDrugVariationEventNote(22, "2016-01-02", "11:11:00", dict.get(Place.OPERATING_THREATRE),
						"adrenaline-continuous-inf", "<desc>", 20, "mg"),
				model.makeContinuousInfusionDrugStopEventNote(22, "2016-01-02", "11:11:00", dict.get(Place.OPERATING_THREATRE),
						"adrenaline-continuous-inf", "<desc>",100),
				model.makeChestTubeProcedureEventNote
					(4, "2016-01-02", "11:18:00", dict.get(Place.SHOCK_ROOM),"<desc>","<right>","<left>")

				);

		JsonObject startVitalSign = model.makeStartVitalSigns(
				"Normotermico", "bla bla","Bradicardico", "bla bla","Ipoteso", "bla bla","Ipossico","bla bla","Ipocapnico", "bla bla",
				"TT", "DX", "Normale", "bla bla", "3","4","5","si","si","bla bla",
				"bla bla","bla bla","bla bla","bla bla","bla bla","bla bla","bla bla","bla bla","bla bla","bla bla","bla bla","bla bla","bla bla",
				"bla bla","bla bla","bla bla","bla bla","bla bla","bla bla","bla bla","bla bla","bla bla","bla bla",
				"bla bla","bla bla", "bla bla", "bla bla", "bla bla", "bla bla"
				);
		
		JsonObject report01 = model.makeReport(
				"alba", 
				"no",
				new String[]{"anestesista","medico ps","neurochirurgo"},
				"2015-11-15", "09:00:00", "2015-11-15", "11:00:00",
				model.makePatientInfo("76848", "788787", "maschio","adulto", "65-70","2017-11-10","12:10:20","bicicletta","sì: timbuctu"),
				dict.get(FinalDestination.INTENSIVE_CARE), startVitalSign, 
				model.makeAnamnesi("54", "3.3", "12"),
				events);


		System.out.println(report01.encodePrettily());
		
		submitReport(address, client, report01);

		JsonObject report02 = model.makeReport(
				"russo", 
				"no",
				new String[]{"anestesista","medico ps"},
				"2016-12-15", "10:00:00", "2016-12-16", "07:00:00",
				model.makePatientInfo("71848", "1919919", "femmina","adulto", "75-80","2017-11-10","12:10:20","bicicletta","no"),
				dict.get(FinalDestination.INTENSIVE_CARE_SUB), 
				startVitalSign,
				model.makeAnamnesi("54", "3.3", "12"),
				events);

		System.out.println(report02.encodePrettily());

		submitReport(address, client, report02);

		JsonObject report03 = model.makeReport(
				"gambe", 
				"sì",
				new String[]{"medico ps"},
				"2017-01-01", "23:00:00", "2017-01-02", "12:00:00",
				model.makePatientInfo("55555", "66666", "maschio","ragazzo", "24-30","2017-11-10","12:10:20","bicicletta","sì"),
				dict.get(FinalDestination.DEPARTMENT), startVitalSign, 
				model.makeAnamnesi("72", "8", "1"),
				events);

		System.out.println(report03.encodePrettily());

		submitReport(address, client, report03);

		for (int i = 0; i < 9; i++){
			JsonObject report = model.makeReport(
					"gambe", 
					"no",
					new String[]{"medico ps"},
					"2017-01-01", "23:00:0"+i, "2017-01-02", "12:00:00",
					model.makePatientInfo("71848", "891891"+i,"femmina","adulto", "75-80","2017-11-10","12:10:20","bicicletta","no"),
					dict.get(FinalDestination.DEPARTMENT), startVitalSign, 
					model.makeAnamnesi("72", "8", "1"),
					events);
			
			submitReport(address, client, report);
			
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
