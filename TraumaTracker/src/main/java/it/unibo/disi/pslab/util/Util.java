package it.unibo.disi.pslab.util;

public class Util {
	private static final String COMMON_PATH = "/gt2/traumatracker/api/stats/";
	
	public static final String strVoid = "";
	public static final String formatString1 = "yyyy-MM-dd";
	public static final String DecimalFormatString = "00";
	
	public static final int numDayYear = 365;
	public static final int numDayMonth = 30;
	
	public static final int numMinHour = 60;
	public static final int numHourDay = 24;
	
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
	
	public enum InfoReports {
		START_DATE("startDate"), END_DATE("endDate"), START_TIME("startTime"), END_TIME("endTime");
		
		private final String path;
		
		InfoReports(final String path) {
			this.path = path;
		}
		
		public String getPath() {		
			return path;
		}
	}
	
	public enum InfoEvents {
		EVENTS("events"), CONTENT_EVENT("content"), EVENT_TIME("time"), EVENT_TYPE("type"),
		DRUG("drug"), DRUG_ID("drugId"), UNIT("unit"), QUANTITY("qty"), DURATION("duration");
		
		private final String path;
		
		InfoEvents(final String path) {
			this.path = path;
		}
		
		public String getPath() {		
			return path;
		}
	}
}