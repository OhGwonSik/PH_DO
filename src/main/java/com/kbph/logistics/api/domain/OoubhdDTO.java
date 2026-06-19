package com.kbph.logistics.api.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class OoubhdDTO {
	private String outboky;
	private String vehicky;
	private String vhcfnam;
	private String entdate;
	private String enttime;
}
