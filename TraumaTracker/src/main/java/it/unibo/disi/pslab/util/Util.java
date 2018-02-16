package it.unibo.disi.pslab.util;

public class Util {
	private final static String COMMON_PATH = "/gt2/traumatracker/api/stats/";
	
	public enum Commands {
		TraumaMonth("TraumaMonth"), TraumaWeek("TraumaWeek"), TraumaDay("TraumaDay"),
		TraumaTime("TraumaTime"), TimeRoom("TimeRoom"), FinalDestination("FinalDestination"),
		TypeDrug("TypeDrug"), MaxProcedure("MaxProcedure");
		
		private final String cmd;
		
		Commands(final String cmd) {
			this.cmd = cmd;
		}

		public String getCmd() {		
			return cmd;
		}
	}
	
	public enum RoutePaths {
		NumTraumaMonth(COMMON_PATH+"numTraumiMonth"),
		NumTraumaWeek(COMMON_PATH+"numTraumiWeek"),
		NumTraumaDay(COMMON_PATH+"numTraumaDay"),
		NumAvarageTraumaTime(COMMON_PATH+"numAvarageTraumaTime"),
		NumAvarageTimeRoom(COMMON_PATH+"numAvarageTimeRoom"),
		NumFinalDestination(COMMON_PATH+"numFinalDestination"),
		ConsumptionTypeDrug(COMMON_PATH+"consumptionTypeDrug"),
		NumMaxProcedure(COMMON_PATH+"numMaxProcedure");
		
		private final String path;
		
		RoutePaths(final String path) {
			this.path = path;
		}
		
		public String getPath() {		
			return path;
		}
	}
}