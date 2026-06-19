package com.kbph.logistics.md.domain;

import java.util.List;

import com.kbph.logistics.common.domain.CommonColumnDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MequmaDTO extends CommonColumnDTO {
	// MEQUMA : 설비 마스터

	// PK
	private String warekey; // VARCHAR(20) - 창고키
	private String areakey; // VARCHAR(20) - 창고동키
	private String equipky; // VARCHAR(20) - 설비키

	// Columns
	private String equinam; // VARCHAR(60) - 설비명칭
	private String equtype; // VARCHAR(20) - 설비유형
	private String equmemo; // VARCHAR(100) - 비고
	private String equvend; // VARCHAR(40) - 설비공급업체
	private String equpurd; // VARCHAR(8) - 설비구매일자
	private String equstat; // VARCHAR(20) - 설비상태
	private String eqmannm; // VARCHAR(60) - 설비담당자
	private String eqmantl; // VARCHAR(20) - 설비담당자 연락처
	private String eqmanem; // VARCHAR(60) - 설비담당자 EMAIL
	private int equwidt; // INT(11) - 설비 폭(Width)
	private int equleng; // INT(11) - 설비 길이(Length)
	private int equheig; // INT(11) - 설비 높이(Height)
	private int equweig; // INT(11) - 설비중량
	private int eqmaxwg; // INT(11) - 작업시 최대중량
	private String equseyn; // VARCHAR(1) - 사용여부

	// data
	private String oldWarekey; // 이전 창고키
	private String oldAreakey; // 이전 창고동키
	private String oldEquipky; // 이전 설비키

	private List<String> equipkyList;

	private String whnamlc; // Warehouse Name<커스텀>
	private String areanam; // Area Name<커스텀>
}
