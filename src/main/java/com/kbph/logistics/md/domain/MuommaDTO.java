package com.kbph.logistics.md.domain;

import com.kbph.logistics.common.domain.CommonColumnDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MuommaDTO extends CommonColumnDTO{
	private String uomekey; // VARCHAR(10) - 단위
	private String uomenam; // VARCHAR(20) - 단위명칭
	private String uouseyn; // VARCHAR(1) - 사용여부
}