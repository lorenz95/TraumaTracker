package it.unibo.disi.pslab.traumatracker.ontology;

public enum VitalSigns {
    // PARAMETRI_VITALI("parametri vitali"),

    RESPIRATION_SYSTEM, // ("vie aeree"),
    CHEST_DECOMPRESSION, // ("decompressione pleurica"),
    DIASTOLIC_PRESSURE, // "pressione diastolica"),
    SYSTOLIC_PRESSURE, // "pressione sistolica"),
    HEART_RATE, // FREQUENZA_CARDIACA("frequenza cardiaca"),
    SPO2, // "saturazione"),
    ETCO2, // ("etCO2"),
    BODY_TEMPERATURE, // ("temperatura"),
    PUPILS, // ("pupille"),
    EYES, // ("occhi"),
    MOTOR, // MOTORIO("motorio"),
    VERBAL; // VERBALE("verbale");

    // private String sRepresentation;
    private static MedicalTermsType type = MedicalTermsType.VITAL_SIGNS;

    /*
    VitalSigns(String sRepresentation){
        this.sRepresentation = sRepresentation;
    }

    public String toString(){
        return this.sRepresentation;
    }

    public static List<String> getStringValues(){
        List<String> res = new ArrayList<String>();
        for (VitalSigns elem: VitalSigns.values()) {
            res.add(elem.toString());
        }
        return res;
    }
     */
    public static MedicalTermsType getType(){
        return type;
    }
}
