package it.unibo.disi.pslab.traumatracker;

import java.io.File;
import java.util.Date;
import java.util.Set;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.file.FileSystem;
import io.vertx.core.http.*;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;
import it.unibo.disi.pslab.traumatracker.ontology.TTDataModel;
import it.unibo.disi.pslab.traumatracker.util.*;
import it.unibo.disi.pslab.util.TTStatistic;
import it.unibo.disi.pslab.util.Util.Commands;
import it.unibo.disi.pslab.util.Util.RoutePaths;

public class TTService extends AbstractVerticle {

	private static final boolean noSTORE = true; // attivata per mancanza di dialogo con mongoDB
	
	private MongoClient store;
	private MongoCollection<Document> collection;
	
//	private TTConfig config;
	private TTAgentLogger view;
	
	private Router router;
	private Logger logger = LoggerFactory.getLogger(TTService.class);
	private Date startTime;

	public TTService(TTAgentLogger view, TTConfig config){
//		this.config = config;
		this.view = view;
	}
	
	@Override
	public void start(Future<Void> fut) {
		initResources();		
		initDB();
		vertx.executeBlocking(future -> {
			future.complete(vertx.createHttpServer());
		}, res -> {
			log("Server created.");
			vertx.executeBlocking(fut2 -> {
				fut2.complete(((HttpServer) res.result()).requestHandler(router::accept));
			}, res2 -> {
				log("Accept registered.");
				((HttpServer) res.result()).listen(8080, result -> {
					if (result.succeeded()) {
						startTime = new Date();
						log("Listening.");
					} else {
						log("Failed: "+result.cause());
					}
				});
			});
		});
	}

	protected void handleGetVersion(RoutingContext routingContext) {
		log("Handling Get Version from "+routingContext.request().absoluteURI());
		HttpServerResponse response = routingContext.response();
		log("handle get version");
		response.putHeader("content-type", "application/text").end(TTConfig.TT_VERSION);
	}	
	
	protected void handleGetReports(RoutingContext routingContext) {
		if (noSTORE) {return;}
		
		// log("Handling Get Reports from "+routingContext.request().absoluteURI());
		HttpServerResponse response = routingContext.response();
		// log("handle get reports");
		JsonObject query = new JsonObject();
		JsonObject options = new JsonObject();
		
		String sdate = routingContext.request().getParam("date");
		if (sdate != null && !sdate.equals("")) {
			query.put("startDate", sdate);
		}

		String opID = routingContext.request().getParam("opId");
		if (opID != null && !opID.equals("")) {
			query.put("startOperatorId", opID);
		}

		int numRecentReports = -1;	
		String mostRecent = routingContext.request().getParam("mostRecent");
		if (mostRecent != null && !mostRecent.equals("")) {
			try {
				numRecentReports = Integer.parseInt(mostRecent);
				if (numRecentReports > 0 && numRecentReports < TTConfig.MAX_RECENT_REPORTS){
					options.put("limit", numRecentReports);
				} else {
					sendError(400, response);
					return;
				}
			} catch (Exception ex){
				sendError(400, response);
				return;
			}
		}		
		/* sorted by date - ascending */
		options.put("sort", new JsonObject().put("startDate",-1).put("startTime",-1));
		
		// log("doing a query: \n" + query.encodePrettily());
		/*
		store.findWithOptions(config.getReportsCollectionName(), query, new FindOptions(options), res -> {
			if (res.succeeded()) {
				JsonArray reports = new JsonArray();
				for (JsonObject rep : res.result()) {
					reports.add(rep);
				}
				response.putHeader("content-type", "application/json").end(reports.encodePrettily());
			} else {
				sendError(404, response);
			}
		});
		*/
	}

	protected void handleDeleteReport(RoutingContext routingContext) {
		if (noSTORE) {return;}
		
		log("Handling Delete Report from "+routingContext.request().absoluteURI());
		String reportID = routingContext.request().getParam("reportID");
		log("Deleting report: " + reportID);
		/*
		HttpServerResponse response = routingContext.response();
		JsonObject doc = new JsonObject().put("_id", reportID);

		store.removeDocument(config.getReportsCollectionName(), doc, res -> {
			if (res.succeeded()) {
				log("Report deleted.");
				response.end();
			} else {
				log("Report not found.");
				sendError(404, response);
			}
		});
		*/
	}

	protected void handleDeleteEvent(RoutingContext routingContext) {
		if (noSTORE) {return;}
		
		log("Handling Delete event from "+routingContext.request().absoluteURI());
		String reportID = routingContext.request().getParam("reportID");
		String eventID = routingContext.request().getParam("eventID");
		log("Deleting event: " + eventID +" from report:" + reportID);

		HttpServerResponse response = routingContext.response();

		try {
			/*
			int evId = Integer.parseInt(eventID);
			JsonObject rep = new JsonObject().put("_id", reportID);

			store.find(config.getReportsCollectionName(), rep, res -> {
				if (res.succeeded()) {
					log("Report found. Going to remove the event.. ");
					List<JsonObject> list = res.result();
					if (list.size() > 0){
						JsonObject repo = list.get(0);
						JsonArray events = repo.getJsonArray("events");
						for (int i = 0; i < events.size(); i++){
							if (events.getJsonObject(i).getInteger("eventId") == evId){
								log("Event found.");
								events.remove(i);
								try {
									store.save(config.getReportsCollectionName(), repo, res2 -> {
											if (res2.succeeded()) {
												log("Report updated: \n" + repo.encodePrettily());
												try {
													response.putHeader("content-type", "application/json").end(repo.encodePrettily());
												} catch (Exception ex){
													log("Error in closing the connection after updating a report: ");
													for (StackTraceElement e: ex.getStackTrace()){
														log("  "+e.toString());
													}
												}
											} else {
												log("Error in updating the report: ");
												for (StackTraceElement e: res2.cause().getStackTrace()){
													log("  "+e.toString());
												}
												sendError(400, response);
											}
										});
								} catch (Exception ex) {
									logger.error("Malformed data when adding a report:\n " + routingContext.getBodyAsString());
									sendError(400, response);
								}							
							}
						}
					} else {
						log("Report not found.");
						sendError(404, response);
					}
				} else {
					log("Report not found.");
					sendError(404, response);
				}
			});
			*/
		} catch (Exception ex){
			log("Report not found.");
			sendError(404, response);
		}
	}
	
	protected void handleAddReport(RoutingContext routingContext) {
		if (noSTORE) {return;}
		
		log("Handling add report from "+routingContext.request().absoluteURI());
		HttpServerResponse response = routingContext.response();
		JsonObject report = routingContext.getBodyAsJson();
		TTDataModel model = TTDataModel.instance();
		boolean ok = model.checkReportCorrectness(report);
		if (!ok) {
			logger.error("Malformed report:\n " + routingContext.getBodyAsString());
			sendError(400, response);
		} else {
			/*
			try {
				store.save(config.getReportsCollectionName(), report, res2 -> {
						if (res2.succeeded()) {
							log("New report saved: \n" + report.encodePrettily());
							try {
								response.end();
							} catch (Exception ex){
								log("Error in closing the connection after saving a report");
								for (StackTraceElement e: res2.cause().getStackTrace()){
									log("  "+e.toString());
								}
							}
						} else {
							log("Error in saving the report: ");
							for (StackTraceElement e: res2.cause().getStackTrace()){
								log("  "+e.toString());
							}
							sendError(400, response);
						}
					});
			} catch (Exception ex) {
				logger.error("Malformed data when adding a report:\n " + routingContext.getBodyAsString());
				sendError(400, response);
			}
			*/
		}
	}

	protected void handleUpdateReport(RoutingContext routingContext) {
		if (noSTORE) {return;}
		
		log("Handling Update report from "+routingContext.request().absoluteURI());
		String reportID = routingContext.request().getParam("reportID");
		log("Updating report: " + reportID);
		HttpServerResponse response = routingContext.response();
		if (reportID == null) {
			sendError(400, response);
		} else {
			JsonObject report = routingContext.getBodyAsJson();
			log("Report data: " + report);
			if (report == null) {
				sendError(400, response);
			} else {
				/*
				report.put("_id", reportID);
				store.save(config.getReportsCollectionName(), report, res -> {
					if (res.succeeded()) {
						log("Report updated.");
						response.end();
					} else {
						log("Report update error.");
						sendError(400, response);
					}
				});
				*/
			}
		}
	}

	protected void handleGetReportInfo(RoutingContext routingContext) {
		if (noSTORE) {return;}
		
		log("Handling Get report info from "+routingContext.request().absoluteURI());
		String reportID = routingContext.request().getParam("reportID");
		log("Getting report: " + reportID);
		HttpServerResponse response = routingContext.response();
		if (reportID == null) {
			sendError(400, response);
		} else {
			/*
			JsonObject query = new JsonObject().put("_id", reportID);
			MongoCursor<Document> cursor = collection.find().iterator();
			store.find(config.getReportsCollectionName(), query, res -> {
				if (res.succeeded()) {
					List<JsonObject> list = res.result();
					if (!list.isEmpty()) {
						JsonObject report = list.get(0);
						response.putHeader("content-type", "application/json").end(report.encodePrettily());
					} else {
						sendError(404, response);
					}
				} else {
					sendError(404, response);
				}
			});
			*/
		}
	}
	
	protected void handleGetUsers(RoutingContext routingContext) {
		if (noSTORE) {return;}
		
		log("Handling Get Users from "+routingContext.request().absoluteURI());
		/*
		HttpServerResponse response = routingContext.response();
		JsonObject query = new JsonObject();
		
		store.find(config.getUsersCollectionName(), query, res -> {
			if (res.succeeded()) {
				JsonArray reports = new JsonArray();
				for (JsonObject rep : res.result()) {
					reports.add(rep);
				}
				response.putHeader("content-type", "application/json").end(reports.encodePrettily());
			} else {
				sendError(404, response);
			}
		});
		*/
	}
	
	protected void handleAddUser(RoutingContext routingContext) {
		if (noSTORE) {return;}
		
		log("Handling Add User from "+routingContext.request().absoluteURI());
		HttpServerResponse response = routingContext.response();
		JsonObject user = routingContext.getBodyAsJson();
		TTDataModel model = TTDataModel.instance();
		boolean ok = model.checkUserCorrectness(user);
		if (!ok) {
			logger.error("Malformed user:\n " + routingContext.getBodyAsString());
			sendError(400, response);
		} else {
			/*
			try {
				// @TODO check if the user is already there
				String id = user.getString("userId");
				JsonObject query = new JsonObject().put("userId", id);
				store.find(config.getUsersCollectionName(), query, res -> {
					if (res.succeeded()) {
						List<JsonObject> list = res.result();
						if (list.isEmpty()) {
							user.put("_id", id);
							store.insert(config.getUsersCollectionName(), user, res2 -> {
								if (res2.succeeded()) {
									log("New user inserted: \n" + user.encodePrettily());
									response.end();
								} else {
									log("User already present: "+id);
									sendError(400, response);
								}
							});				
						} else {
							log("User already present: "+id);
							sendError(400, response);
						}
					} else {
						sendError(404, response);
					}
				});
			} catch (Exception ex) {
				logger.error("Malformed data when adding a user:\n " + routingContext.getBodyAsString());
				sendError(400, response);
			}
			*/
		}
	}
	
	protected void handleGetUserInfo(RoutingContext routingContext) {
		if (noSTORE) {return;}
		
		log("Handling Get User Info from "+routingContext.request().absoluteURI());
		String reportID = routingContext.request().getParam("reportID");
		log("Getting report: " + reportID);
		HttpServerResponse response = routingContext.response();
		if (reportID == null) {
			sendError(400, response);
		} else {

			return;
			//JsonObject query = new JsonObject().put("_id", reportID);
			/*
			store.find(config.getReportsCollectionName(), query, res -> {
				if (res.succeeded()) {
					List<JsonObject> list = res.result();
					if (!list.isEmpty()) {
						JsonObject report = list.get(0);
						response.putHeader("content-type", "application/json").end(report.encodePrettily());
					} else {
						sendError(404, response);
					}
				} else {
					sendError(404, response);
				}
			});
			*/
		}
	}

	protected void handleDeleteAllReports(RoutingContext routingContext) {
		if (noSTORE) {return;}
		
		log("Handling Delete all Reports from "+routingContext.request().absoluteURI());
		/*
		HttpServerResponse response = routingContext.response();
		try {
			store.dropCollection(config.getReportsCollectionName(), res -> {
					if (res.succeeded()) {
						log("Reports collection reset done.");
						JsonObject reply = new JsonObject().put("req","delete all reports").put("res", "done");
						response.putHeader("content-type", "application/json").end(reply.encodePrettily());
					} else {
						log("Reports collection reset error.");
						JsonObject reply = new JsonObject().put("req","delete all reports").put("res", "error");
						response.putHeader("content-type", "application/json").end(reply.encodePrettily());
					}
			}); 
		} catch (Exception ex){
			log("Malformed request");
			sendError(400, response);
		}
		*/
	}
	
	protected void handleDeleteAllUsers(RoutingContext routingContext) {
		if (noSTORE) {return;}
		
		log("Handling Delete all Users from "+routingContext.request().absoluteURI());
		/*
		HttpServerResponse response = routingContext.response();
		try {
			store.dropCollection(config.getUsersCollectionName(), res -> {
					if (res.succeeded()) {
						log("Users collection reset done.");
						JsonObject reply = new JsonObject().put("req","delete all users").put("res", "done");
						response.putHeader("content-type", "application/json").end(reply.encodePrettily());
					} else {
						log("Reports collection reset error.");
						JsonObject reply = new JsonObject().put("req","delete all users").put("res", "error");
						response.putHeader("content-type", "application/json").end(reply.encodePrettily());
					}
			}); 
		} catch (Exception ex){
			log("Malformed request");
			sendError(400, response);
		}
		*/
	}
		
	protected void handlePhotoUpload(RoutingContext routingContext){
		if (noSTORE) {return;}
		
		log("Handling Uploading Photos from "+routingContext.request().absoluteURI());
		HttpServerResponse response = routingContext.response();
		Set<FileUpload> uploads = routingContext.fileUploads();
		log("Num photos received: "+uploads.size());
		FileSystem fs = vertx.fileSystem();
		for (FileUpload f: uploads){
			log(f.fileName()+"\n"+f.contentTransferEncoding()+"\n"+f.contentType()+"\n"+f.uploadedFileName()+"\n");
			String fileTo =new File(".").getAbsolutePath()
					+File.separatorChar+"webroot"
					+File.separatorChar+"gt2"
					+File.separatorChar+"traumatracker"
					+File.separatorChar+"photo"
					+File.separatorChar+f.fileName();
			fs.move(f.uploadedFileName(), fileTo, res -> {
				log("Photo "+f.fileName()+" moved.");
			});
		}
		response.end();
	}	
	
	protected void handleVideoUpload(RoutingContext routingContext){
		if (noSTORE) {return;}
		
		log("Handling Uploading Videos from "+routingContext.request().absoluteURI());
		HttpServerResponse response = routingContext.response();
		Set<FileUpload> uploads = routingContext.fileUploads();
		log("Num videos  received: "+uploads.size());
		FileSystem fs = vertx.fileSystem();
		for (FileUpload f: uploads){
			log(f.fileName()+"\n"+f.contentTransferEncoding()+"\n"+f.contentType()+"\n"+f.uploadedFileName()+"\n");
			String fileTo =new File(".").getAbsolutePath()
					+File.separatorChar+"webroot"
					+File.separatorChar+"gt2"
					+File.separatorChar+"traumatracker"
					+File.separatorChar+"video"
					+File.separatorChar+f.fileName();
			fs.move(f.uploadedFileName(), fileTo, res -> {
				log("Video "+f.fileName()+" moved.");
			});
		}
		response.end();
	}	

	protected void handleAudioUpload(RoutingContext routingContext){
		if (noSTORE) {return;}
		
		log("Handling Uploading Audios from "+routingContext.request().absoluteURI());
		HttpServerResponse response = routingContext.response();
		Set<FileUpload> uploads = routingContext.fileUploads();
		log("Num audio received: "+uploads.size());
		FileSystem fs = vertx.fileSystem();
		for (FileUpload f: uploads){
			log(f.fileName()+"\n"+f.contentTransferEncoding()+"\n"+f.contentType()+"\n"+f.uploadedFileName()+"\n");
			String fileTo =new File(".").getAbsolutePath()
					+File.separatorChar+"webroot"
					+File.separatorChar+"gt2"
					+File.separatorChar+"traumatracker"
					+File.separatorChar+"audio"
					+File.separatorChar+f.fileName();
			fs.move(f.uploadedFileName(), fileTo, res -> {
				log("Video "+f.fileName()+" moved.");
			});
		}
		response.end();
	}	

	protected void handleGetState(RoutingContext routingContext) {
		if (noSTORE) {return;}
		
		log("Handling Uploading GetState from "+routingContext.request().absoluteURI());
		HttpServerResponse response = routingContext.response();
		JsonObject reply = new JsonObject()
							.put("version", TTConfig.TT_VERSION)	
							.put("startTime", startTime.toString());
		response.putHeader("content-type", "application/json").end(reply.encodePrettily());		
	}

	// STATISTICS HANDLES
	private void handleStatisticGlobal(RoutingContext routingContext, Commands command) {
		HttpServerResponse response = routingContext.response();
		JsonObject stat = new JsonObject();
		try {
			final Document query = new Document();
			final FindIterable<Document> search = collection.find(query);		
			
			stat = TTStatistic.handleFunction(command, search);
		} catch (Exception ex) {
			System.out.println("Exception = " + ex);
			this.sendError(404, response);
		} finally {
			response.
			putHeader("content-type", "application/json").
			end(stat.encodePrettily());
		}
	}
	
	protected void handleGetNumTraumaMonth(RoutingContext routingContext) {
		handleStatisticGlobal(routingContext, Commands.TraumaMonth);
	}
	
	protected void handleGetNumTraumaWeek(RoutingContext routingContext) {
		handleStatisticGlobal(routingContext, Commands.TraumaWeek);
	}
	
	protected void handleGetNumTraumaDay(RoutingContext routingContext) {
		handleStatisticGlobal(routingContext, Commands.TraumaDay);
	}
	
	protected void handleGetAvarageTraumaTime(RoutingContext routingContext) {
		handleStatisticGlobal(routingContext, Commands.TraumaTime);
	}
	
	protected void handleGetAvarageTimeRoom(RoutingContext routingContext) {
		handleStatisticGlobal(routingContext, Commands.TimeRoom);
	}
	
	protected void handleGetConsumptionTypeDrug(RoutingContext routingContext) {		
		handleStatisticGlobal(routingContext, Commands.TypeDrug);
	}
	
	protected void handleGetNumFinalDestination(RoutingContext routingContext) {
		handleStatisticGlobal(routingContext, Commands.FinalDestination);
	}
	
	protected void handleGetMaxProcedure(RoutingContext routingContext) {
		handleStatisticGlobal(routingContext, Commands.MaxProcedure);
	}
	
	// -----------
	private void initResources() {
		router = Router.router(vertx);
		router.route().handler(BodyHandler.create());
		router.get("/gt2/traumatracker/api/version").handler(this::handleGetVersion);
		
		// management 
		router.get("/gt2/traumatracker/api/state").handler(this::handleGetState);
		
		// reports
		router.get("/gt2/traumatracker/api/reports").handler(this::handleGetReports);
		router.post("/gt2/traumatracker/api/reports").handler(this::handleAddReport);
		router.delete("/gt2/traumatracker/api/reports").handler(this::handleDeleteAllReports);
		router.put("/gt2/traumatracker/api/reports/:reportID").handler(this::handleUpdateReport);
		router.get("/gt2/traumatracker/api/reports/:reportID").handler(this::handleGetReportInfo);
		router.delete("/gt2/traumatracker/api/reports/:reportID").handler(this::handleDeleteReport);
		router.delete("/gt2/traumatracker/api/reports/:reportID/events/:eventID").handler(this::handleDeleteEvent);
		
		// users
		router.get("/gt2/traumatracker/api/users").handler(this::handleGetUsers);
		router.post("/gt2/traumatracker/api/users").handler(this::handleAddUser);
		router.delete("/gt2/traumatracker/api/users").handler(this::handleDeleteAllUsers);
		router.get("/gt2/traumatracker/api/users/:userID").handler(this::handleGetUserInfo);

		// setting dir upload for photos
		router.post("/gt2/traumatracker/photo").handler(this::handlePhotoUpload);	
		router.post("/gt2/traumatracker/video").handler(this::handleVideoUpload);	
		router.post("/gt2/traumatracker/audio").handler(this::handleAudioUpload);	
		
		// statistics		
		router.get(RoutePaths.NumTraumaMonth.getPath()).handler(this::handleGetNumTraumaMonth);
		router.get(RoutePaths.NumTraumaWeek.getPath()).handler(this::handleGetNumTraumaWeek);  
		router.get(RoutePaths.NumTraumaDay.getPath()).handler(this::handleGetNumTraumaDay); 
		
		router.get(RoutePaths.NumAvarageTraumaTime.getPath()).handler(this::handleGetAvarageTraumaTime);
		router.get(RoutePaths.NumAvarageTimeRoom.getPath()).handler(this::handleGetAvarageTimeRoom); // ok
		router.get(RoutePaths.NumFinalDestination.getPath()).handler(this::handleGetNumFinalDestination);
		
		router.get(RoutePaths.ConsumptionTypeDrug.getPath()).handler(this::handleGetConsumptionTypeDrug);
		router.get(RoutePaths.NumMaxProcedure.getPath()).handler(this::handleGetMaxProcedure);
		
		// config
		router.route().handler(StaticHandler.create().setWebRoot("webroot").setCachingEnabled(false));
	}
	
	/**
	 * @TODO do all checks about DB availability
	 * 
	 */
	private void initDB() {
		vertx.executeBlocking(future -> {
			  store = new MongoClient(TTConfig.server, TTConfig.port); // MongoClient.createNonShared(vertx, mongoConfig);
			  
				MongoDatabase db = store.getDatabase(TTConfig.dbName);
				collection = db.getCollection(TTConfig.collectionName);
			  
			  future.complete();
			}, res -> {
				log("mongodb client created.");				
			});		
	}

	private void sendError(int statusCode, HttpServerResponse response) {
		response.setStatusCode(statusCode).end();
	}

	public static void main(String[] args) {
		try {
			TTAgentLogger logger = new TTAgentLogger();
			TTConfig config;

			try {
				config = new TTConfig(TTConfig.TT_CONFIG_FILE);
			} catch (Exception ex){
				logger.log("Config file not found or invalid");
				config = new TTConfig();
			}

			Vertx vertx = Vertx.vertx();
			TTService ttService = new TTService(logger,config);
			vertx.deployVerticle(ttService);
		} catch (Exception ex){
			ex.printStackTrace();
		}
	}
	
	private void log(String msg){
		logger.info("[TTService] "+msg);
		view.log("[TTService] "+msg);
	}
}
