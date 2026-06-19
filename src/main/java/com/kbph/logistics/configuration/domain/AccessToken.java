package com.kbph.logistics.configuration.domain;

import groovy.transform.ToString;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ToString
public class AccessToken {
	private String userkey;
	private String useract;
	private String usernam;
	private String langkey;
	private String usertyp;
	private String commonSchema;
	private String schema;
	private String authorities;
}
