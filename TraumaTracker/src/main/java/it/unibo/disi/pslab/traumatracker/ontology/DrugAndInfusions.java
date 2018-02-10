package it.unibo.disi.pslab.traumatracker.ontology;

public enum DrugAndInfusions {
	CRYSTALLOID(DrugUnit.MG), // ("cristalloidi", DrugUnit.MG),
    ZERO_NEGATIVE_BLOOD( DrugUnit.MG), // ("zero negativo", DrugUnit.MG),
    HYPERTONIC_SALINE_SOLUTION(DrugUnit.MG), // ("soluzione ipertonica", DrugUnit.MG),
    PACKED_CELLS(DrugUnit.MG), // EMAZIE_CONCENTRATE("emazie concentrate", DrugUnit.MG),
    FRESH_FROZEN_PLASMA(DrugUnit.MG), //("plasma fresco congelato", DrugUnit.MG),
    FIBRINOGEN(DrugUnit.MG), // ("fibrinogeno", DrugUnit.MG),
    TRANEX(DrugUnit.MG), // ("tranex", DrugUnit.MG),
    TRANEXAMIC_ACID(DrugUnit.MG), // ("acido tranexamico", DrugUnit.MG),
    PLATELETS_POOL(DrugUnit.MG), //("poolpiastrine", DrugUnit.MG),
    MANNITOL(DrugUnit.MG), //("mannitolo", DrugUnit.MG),
    MIDAZOLAM(DrugUnit.MG), // ("midazolam", DrugUnit.MG),
    PROPOFOL(DrugUnit.MG), // ("propofol", DrugUnit.MG),
    FENTANIL(DrugUnit.MG), //("fentanil", DrugUnit.MG),
    KETAMINE(DrugUnit.MG), // ("ketamina", DrugUnit.MG),
    ROCURONIUM(DrugUnit.MG), // ("rocuronio", DrugUnit.MG),
    SUCCINYLCHOLINE(DrugUnit.MG), // ("succinilcolina", DrugUnit.MG),
    THIOPENTONE(DrugUnit.MG), //  ("tiopentone", DrugUnit.MG),
    MORPHINE(DrugUnit.MG), // ("morfina", DrugUnit.MG),
    CRYOPRECIPITATE(DrugUnit.MG); //  Cryoprecipitate("crioprecipitati", DrugUnit.MG);

    // private String sRepresentation;
    private DrugUnit unit;
    
    private static MedicalTermsType type = MedicalTermsType.DRUG_AND_INFUSIONS;

    DrugAndInfusions(DrugUnit unit){
        this.unit = unit;
    }

    /*
    public String toString(){
        return this.sRepresentation;
    }

    public static List<String> getStringValues(){
        List<String> res = new ArrayList<String>();
        for (DrugAndInfusions elem: DrugAndInfusions.values()) {
            res.add(elem.toString());
        }
        return res;
    }

	*/
    public DrugUnit getUnit(){
    	return this.unit;
    }
    
    public static MedicalTermsType getType(){
        return type;
    }
    
    /**
     * Get the DrugAndInfusion value instance that corresponds to the string representation specified in sRepresentation
     * @param sRepresentation the string representation of the Drug or Infusion term
     * @return the DrugAndInfusion instance if sRepresentation match with one value of the enum, null otherwise
     */
    /*
    public static DrugAndInfusions valueOfByStringRepresentation(String sRepresentation){
    	for(DrugAndInfusions drug : DrugAndInfusions.values()){
    		if(drug.toString().equals(sRepresentation)){
    			return drug;
    		}
    	}
    	return null;    	
    }
    */
}
