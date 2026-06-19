package com.kbph.logistics.api.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE )
public class ConversionWasnifDTO {

    String ownrnam;
    String warekey;
    String fncusky;
    String regikey;
    String deorgnm;
    String demodnm;
    String demodyn;
    String skumkey;
    String btrcate;
    String adjsoky;
    int adjsoit;
    String adjmode;
    String adjgrky;
    String doccate;
    String doctype;
    String useract;
	String adjdcst;
	String adjitst;
	String ifprsts;
	String iferrnm;
    
}
