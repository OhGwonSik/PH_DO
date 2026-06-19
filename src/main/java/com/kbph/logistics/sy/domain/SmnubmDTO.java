package com.kbph.logistics.sy.domain;

import com.kbph.logistics.common.domain.CommonColumnDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SmnubmDTO extends CommonColumnDTO {
	private String userkey;
	private String useract;
	private String progrid;
}
