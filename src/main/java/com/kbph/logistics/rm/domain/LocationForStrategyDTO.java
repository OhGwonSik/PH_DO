package com.kbph.logistics.rm.domain;

import groovy.transform.ToString;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ToString
public class LocationForStrategyDTO {
	// P.K.
	private String warekey; // VARCHAR(20) - 창고키
	private String areakey; // VARCHAR(20) - 창고동키
	private String locakey; // VARCHAR(20) - 베드키

	// Data
	private String locanam; // VARCHAR(60) - 베드명
	private String locstat; // VARCHAR(10) - 베드 상태
	private String loctype; // VARCHAR(10) - 베드 타입
//	private String locmemo; // VARCHAR(100) - 비고
	private int locwidt; // INT(11) - 베드의 폭(Width)
	private int locleng; // INT(11) - 베드의 길이(Length)
	private int locheig; // INT(11) - 베드의 높이(Height)
	private double loccubi; // decimal(10,3) - Cubic Meter
	private String sizepri; // VARCHAR(20) - 사이즈 우선순위
	private String btrcpri; // VARCHAR(20) - 정산유형 우선순위
	private String itdtpri; // VARCHAR(20) - 품목세부 우선순위
	private String custpri; // VARCHAR(20) - 고객사 우선순위
	private String regipri; // VARCHAR(20) - 목적지 우선순위
	private String destpri; // VARCHAR(20) - 상세착지 우선순위
	private Integer inbopri; // INT(11) - 동일속성 시 입고 우선순위
	private Integer oubopri; // INT(11) - 출고오더 우선순위
	private String louseyn; // VARCHAR(1) - 사용여부
	private Integer stockcnt; // 현재 재고 수량
	private Integer taskcnt; // 현재 작업 개수
	private Integer wplcnt; // 입고예정 개수
	private Integer asncnt; // 출고예정 개수
	private Integer oubcnt; // 출고오더 개수
	private Integer priorityScore; // 우선순위 점수
	private String ullocky; // 하차포인트
	private String ullocnm; // 하차포인트명
//	private String whnamlc; // Warehouse Name<커스텀>
	private String areanam; // Area Name<커스텀>
	private String equipky;
//	private String maneqyn; // VARCHAR(1) - 메인설비여부

}
