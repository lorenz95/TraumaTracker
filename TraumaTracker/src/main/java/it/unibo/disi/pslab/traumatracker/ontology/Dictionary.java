package it.unibo.disi.pslab.traumatracker.ontology;

public interface Dictionary {
	
	String get(DrugAndInfusions term);
	
	String get(VitalSigns term);
	
	String get(ResuscitationManeuvers term);

	String get(Place term);
	
	String get(FinalDestination dest);
	
}
