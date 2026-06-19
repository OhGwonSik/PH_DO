package com.kbph.logistics.sy.type;

public enum StandardUser {

	DO_DY_WEB_STANDARD("DO_DY", "dyadmin", "WEB"),
	DO_DR_WEB_STANDARD("DO_DR", "dradmin", "WEB"),
	DO_DR_TABLET_STANDARD("DO_DR", "tab2", "TABLET");

    private final String schema;     
    private final String standardUseract; 
    private final String usertyp;
	
    StandardUser(String schema, String standardUseract, String usertyp) {
    	this.schema = schema;
    	this.standardUseract = standardUseract;
    	this.usertyp = usertyp;
    }
    
    public String getSchema() {
        return schema;
    }

    public String getStandardUseract() {
        return standardUseract;
    }
    
    public String getUsertyp() {
        return usertyp;
    }
    
    public static StandardUser getStandardUserBySchemaAndUserType(String schema, String usertyp) {
        for (StandardUser standardUser : StandardUser.values()) {
            if (standardUser.schema.equalsIgnoreCase(schema) && standardUser.usertyp.equalsIgnoreCase(usertyp)) {
                return standardUser;
            }
        }
        throw new IllegalArgumentException("Invalid schema: " + schema);
    }
}
