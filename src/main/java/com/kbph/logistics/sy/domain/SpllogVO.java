package com.kbph.logistics.sy.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SpllogVO {

	String pllogsq;
	String pllogmg;
	String credate;
	String cretime;
	String creuser;
	String lmodate;
	String lmotime;
	String lmouser;
	int updtchk;
}
