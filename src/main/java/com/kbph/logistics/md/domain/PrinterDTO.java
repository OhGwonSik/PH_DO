package com.kbph.logistics.md.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PrinterDTO {
	private String wmptrnm; // VARCHAR(100) - 창고 메인프린터 명칭
	private String wsptrnm; // VATCHAR(100) - 창고 서브프린터 명칭
}
