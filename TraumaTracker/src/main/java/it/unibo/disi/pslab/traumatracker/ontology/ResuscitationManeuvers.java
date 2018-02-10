package it.unibo.disi.pslab.traumatracker.ontology;


public enum ResuscitationManeuvers {
	TRACHEAL_INTUBATION, // ("intubazione orotracheale"
    CHEST_TUBE, // "drenaggio toracico"
    PELVIC_BINDER, // "pelvic binder"
    TPOD, // "tpod"),
    HIGH_FLOW_CATHETER, // "catetere alto flusso"
    PRESSURE_INFUSER, // "infusore a pressione"    
    HEMORRHAGIC_WOUND_SUTURE, // ("sutura ferita emorragica"),
    EXTERNAL_FIXATION, // ("posizionamento fissatore esterno"),
    RESUSCITATIVE_THORACOTOMY, // ("toracotomia resuscitativa"),
    REBOA,
    SUPRAGLOTTIC_PRESIDIUM,
    FIBROSCOPY,
    DRAINAGE,
    ARTERIAL_CATHETER,
    HEMOSTASIS,
    TOURNIQUET; 

    // private String sRepresentation;
    private static MedicalTermsType type = MedicalTermsType.RESUSCITATION_MANEUVERS;

    
    public static MedicalTermsType getType(){
        return type;
    }
    
}
