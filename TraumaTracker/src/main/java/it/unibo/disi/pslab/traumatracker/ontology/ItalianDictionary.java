package it.unibo.disi.pslab.traumatracker.ontology;

import java.util.HashMap;

public class ItalianDictionary implements Dictionary {

	HashMap<DrugAndInfusions,String> drugAndInfusions;
	HashMap<ResuscitationManeuvers,String> maneuvers;
	HashMap<VitalSigns,String> vitalSigns;
	HashMap<Place,String> places;
	HashMap<FinalDestination,String> dest;
	
	static ItalianDictionary instance;
	
	static public ItalianDictionary instance(){
		synchronized (ItalianDictionary.class){
			if (instance == null){
				instance = new ItalianDictionary();
			}
		}
		return instance;
	}
	
	private ItalianDictionary(){
		drugAndInfusions = new HashMap<>();
		drugAndInfusions.put(DrugAndInfusions.CRYSTALLOID, "cristalloidi");
		drugAndInfusions.put(DrugAndInfusions.ZERO_NEGATIVE_BLOOD, "zero negativo");
		drugAndInfusions.put(DrugAndInfusions.HYPERTONIC_SALINE_SOLUTION, "soluzione ipertonica");
		drugAndInfusions.put(DrugAndInfusions.PACKED_CELLS, "emazie concentrate");
		drugAndInfusions.put(DrugAndInfusions.FRESH_FROZEN_PLASMA, "emazie concentrate");
		drugAndInfusions.put(DrugAndInfusions.FIBRINOGEN, "fibrinogeno");
		drugAndInfusions.put(DrugAndInfusions.TRANEX, "tranex");
		drugAndInfusions.put(DrugAndInfusions.TRANEXAMIC_ACID, "acido tranexamico");
		drugAndInfusions.put(DrugAndInfusions.PLATELETS_POOL, "poolpiastrine");
		drugAndInfusions.put(DrugAndInfusions.MANNITOL, "mannitolo");
		drugAndInfusions.put(DrugAndInfusions.MIDAZOLAM, "midazolam");
		drugAndInfusions.put(DrugAndInfusions.PROPOFOL, "propofol");
		drugAndInfusions.put(DrugAndInfusions.FENTANIL, "fentanil");
		drugAndInfusions.put(DrugAndInfusions.KETAMINE, "ketamina");
		drugAndInfusions.put(DrugAndInfusions.ROCURONIUM, "rocuronio");
		drugAndInfusions.put(DrugAndInfusions.SUCCINYLCHOLINE, "succinilcolina");
		drugAndInfusions.put(DrugAndInfusions.THIOPENTONE, "tiopentone");
		drugAndInfusions.put(DrugAndInfusions.MORPHINE, "succinilcolina");
		drugAndInfusions.put(DrugAndInfusions.CRYOPRECIPITATE, "crioprecipitati");
	
		maneuvers = new HashMap<>();
		maneuvers.put(ResuscitationManeuvers.TRACHEAL_INTUBATION,"intubazione orotracheale");
		maneuvers.put(ResuscitationManeuvers.CHEST_TUBE,"drenaggio toracico");
		maneuvers.put(ResuscitationManeuvers.PELVIC_BINDER,"pelvic binder");
		maneuvers.put(ResuscitationManeuvers.TPOD,"tpod");
		maneuvers.put(ResuscitationManeuvers.HIGH_FLOW_CATHETER,"catetere alto flusso");
		maneuvers.put(ResuscitationManeuvers.PRESSURE_INFUSER,"infusore a pressione");
		maneuvers.put(ResuscitationManeuvers.HEMORRHAGIC_WOUND_SUTURE,"sutura ferita emorragica");
		maneuvers.put(ResuscitationManeuvers.EXTERNAL_FIXATION,"posizionamento fissatore esterno");
		maneuvers.put(ResuscitationManeuvers.RESUSCITATIVE_THORACOTOMY,"toracotomia resuscitativa");
		maneuvers.put(ResuscitationManeuvers.REBOA,"reboa");
		maneuvers.put(ResuscitationManeuvers.SUPRAGLOTTIC_PRESIDIUM,"supraglottic-presidium");
		maneuvers.put(ResuscitationManeuvers.FIBROSCOPY,"fibroscopia");
		maneuvers.put(ResuscitationManeuvers.DRAINAGE,"drenaggio");
		maneuvers.put(ResuscitationManeuvers.ARTERIAL_CATHETER,"arterial-catheter");
		maneuvers.put(ResuscitationManeuvers.HEMOSTASIS,"hemostasis");
		maneuvers.put(ResuscitationManeuvers.TOURNIQUET,"tourniquet"); 

		
		vitalSigns = new HashMap<>();
		vitalSigns.put(VitalSigns.RESPIRATION_SYSTEM,"vie aeree");
		vitalSigns.put(VitalSigns.CHEST_DECOMPRESSION,"decompressione pleurica");
		vitalSigns.put(VitalSigns.DIASTOLIC_PRESSURE,"pressione diastolica");
		vitalSigns.put(VitalSigns.SYSTOLIC_PRESSURE,"pressione sistolica");
		vitalSigns.put(VitalSigns.HEART_RATE,"frequenza cardiaca");
		vitalSigns.put(VitalSigns.SPO2,"saturazione");
		vitalSigns.put(VitalSigns.BODY_TEMPERATURE,"temperatura");
		vitalSigns.put(VitalSigns.PUPILS,"pupille");
		vitalSigns.put(VitalSigns.EYES,"occhi");
		vitalSigns.put(VitalSigns.MOTOR,"motorio");
		vitalSigns.put(VitalSigns.VERBAL,"verbale");
		
		places = new HashMap<>();
		places.put(Place.TAC, "TAC");
		places.put(Place.SHOCK_ROOM, "Shock room");
		places.put(Place.ANGIOGRAPHY_ROOM, "Sala angiografica");
		places.put(Place.OPERATING_THREATRE, "Sala operatoria");

		dest = new HashMap<>();
		dest.put(FinalDestination.DEPARTMENT, "Reparto");
		dest.put(FinalDestination.INTENSIVE_CARE, "Terapia intensiva");
		dest.put(FinalDestination.INTENSIVE_CARE_SUB, "Terapia subintensiva");
		dest.put(FinalDestination.MORTUARY, "Camera mortuaria");
	}
	
	public String get(DrugAndInfusions term){
		return drugAndInfusions.get(term);
	}

	public String get(VitalSigns term){
		return vitalSigns.get(term);
	}

	public String get(FinalDestination term){
		return dest.get(term);
	}

	public String get(ResuscitationManeuvers term){
		return maneuvers.get(term);
	}

	public String get(Place term){
		return places.get(term);		
	}

}
