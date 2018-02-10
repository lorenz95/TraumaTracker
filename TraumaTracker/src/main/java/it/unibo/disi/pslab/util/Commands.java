package it.unibo.disi.pslab.util;

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
