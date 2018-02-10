package it.unibo.disi.pslab.traumatracker;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Stream;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import it.unibo.disi.pslab.traumatracker.util.TTConfig;

public class VPNManagerAgentOSX extends Thread{

	static final int DELAY_FOR_RECONNECT = 60000;
	static final int KEEP_ALIVE_PERIOD = 60000*10;
	
	TTAgentLogger view;
	TTConfig config;
	long lastPublishingTime;
	String vpnIP;
	Vertx vertx = Vertx.vertx();

	public VPNManagerAgentOSX(TTAgentLogger view, TTConfig config){
		this.view = view;
		this.config = config;
		vertx = Vertx.vertx();
	}
	
	public void run() {
		try {
			boolean connected = false;
			boolean published = false;
			int nLongWaitingTime = DELAY_FOR_RECONNECT;
			 
			/* keep the connection */
			while (true){
				if (!checkForConnection()){
					log("Connection down. Going to connect...");
					connected = false;
					while (!connected){
						requestConnection();
						int nTrials = 0;
						while (!connected && nTrials < 10){
							connected = checkForConnection();
							if (connected){
								log("connected.");
								String vpnIP = getVPNIPAddress().get();
								log("VPN address: "+vpnIP);
								keepAlive(vpnIP);
							} else {
								// System.out.print(".");
								log("retrying...");
								nTrials++;
								sleep(500);
							}
						}
						if (!connected){
							log("Impossible to reconnect => Waiting a bit ("+nLongWaitingTime+" ms) and retry. ");
							sleep(nLongWaitingTime);
							log("Ok, retrying...");
						}
					}
				} else {
					if (!published){
						vpnIP = getVPNIPAddress().get();
						log("VPN address: "+vpnIP);
						keepAlive(vpnIP);
						published = true;
					} else if (System.currentTimeMillis() - lastPublishingTime > KEEP_ALIVE_PERIOD){
						String ip = getVPNIPAddress().get();
						if (!ip.equals(vpnIP)){
							log("** NEW** VPN address: "+vpnIP);
							vpnIP = ip;
						}
						keepAlive(vpnIP);
					}
					
					sleep(1000);
				}
			}
		} catch (Exception ex){
			ex.printStackTrace();
		}
		
	}
	

	private void requestConnection(){
		try {
			Process start = Runtime.getRuntime().exec(new String[]{"scutil","--nc","start","TTNET-VPN"});
			BufferedReader reader = new BufferedReader(new InputStreamReader(start.getInputStream()));
			reader.lines().forEach( s -> {
				System.out.println(s);
			});
		} catch (Exception ex){
			ex.printStackTrace();
		}
	}

	/*
	private boolean waitForConnection(int nMaxTrials, int period){
		try {
			boolean isConnected = false;
			int nTrials = 0;
			while (!isConnected && nTrials < nMaxTrials){
				log("retrying...");
				Thread.sleep(period);
				Process check = Runtime.getRuntime().exec(new String[]{"scutil","--nc","status","TTNET-VPN"});
				isConnected = new BufferedReader(new InputStreamReader(check.getInputStream())).lines().filter(s -> s.indexOf("Connected") != -1).count() > 0;
				nTrials++;
			}
			if (isConnected){
				return true;
			} else {
				return false;
			}
		} catch (Exception ex){
			ex.printStackTrace();
			return false;
		}
	}
	*/

	private boolean checkForConnection(){
		try {
			Process check = Runtime.getRuntime().exec(new String[]{"scutil","--nc","status","TTNET-VPN"});
			return  new BufferedReader(new InputStreamReader(check.getInputStream())).lines().filter(s -> s.indexOf("Connected") != -1).count() > 0;
		} catch (Exception ex){
			ex.printStackTrace();
			return false;
		}
	}

	private Optional<String> getVPNIPAddress(){
		try {
			Process check = Runtime.getRuntime().exec(new String[]{"ifconfig"});
			Stream<String> st = new BufferedReader(new InputStreamReader(check.getInputStream())).lines();
			Optional<String> line = st.filter(s -> s.indexOf("inet 10.") != -1).findFirst();
			if (line.isPresent()){
				String addr = line.get();
				String addr1 = addr.substring(addr.indexOf("inet")+5);
				String ipaddr = addr1.substring(0,addr1.indexOf(' '));
				// System.out.println(ipaddr);
				return Optional.of(ipaddr);
			} else {
				return Optional.empty();
			}
		} catch (Exception ex){
			ex.printStackTrace();
			return Optional.empty();
		}
	}
	
	private void keepAlive(String ip){
		//https://dweet.io/dweet/for/my-thing-name?hello=world
		// https://dweet.io/dweet/for/ttservice?address="10.90.0.94"		
		WebClient client = WebClient.create(vertx);		
		
		String req = "https://dweet.io/dweet/for/ttservice?address="+ip+"&time=\""+new Date().toString().replace(' ','-')+"\"";
		log("Keep alive at "+req);
	    client
		  .getAbs(req)
		  .send(ar -> {
		    if (ar.succeeded()) {
		      // Obtain response
		      HttpResponse<Buffer> response = ar.result();
		      log("Received response with status code " + response.statusCode());
		      lastPublishingTime = System.currentTimeMillis();
		    } else {
		      log("Something went wrong: " + ar.cause().getMessage());
		    }
		  });		
		
	    lastPublishingTime = System.currentTimeMillis();
		
	}

	protected void handleDweetReply(HttpClientResponse res) {
		log("DWEET response status " + res.statusCode());
	}
	
	private void log(String msg){
		System.out.println("[VPNManagerAgent] "+msg);
		view.log("[VPNManagerAgent] "+msg);
	}
	
}
