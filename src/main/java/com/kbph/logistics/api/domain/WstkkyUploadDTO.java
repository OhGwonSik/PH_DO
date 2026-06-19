package com.kbph.logistics.api.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class WstkkyUploadDTO {

	private int datacnt;
	// Procedure Outparam
	private String omsgkey; // procedure 결과 메세지
	private Integer oresult; // procedure 결과
}
