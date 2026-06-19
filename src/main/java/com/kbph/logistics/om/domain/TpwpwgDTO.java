package com.kbph.logistics.om.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kbph.logistics.common.domain.CommonColumnDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class TpwpwgDTO extends CommonColumnDTO {
	private String vhplnky;
	private String shpplky;
	private Integer alcweig;
	private String plnsize;
	private String wplcfyn;
	private String schema;
	private String arrivyn;
	private String regikey;
	
	private String renamlc;
	private String grpkynm;
	private String pageId;
	private String type;
	private String warekey;
}
