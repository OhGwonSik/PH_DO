package com.kbph.logistics.md.domain;

import java.util.List;

import com.kbph.logistics.common.domain.CommonColumnDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MeqlocDTO extends CommonColumnDTO {
	// MEQLOC : 설비-베드 매핑

	// PK
	private String warekey; // VARCHAR(20) - 창고키
	private String areakey; // VARCHAR(20) - 창고동키
	private String equipky; // VARCHAR9(20) - 설비키
	private String locakey; // VARCHAR(20) - 베드키

	// Columns
	private String maneqyn; // VARCHAR(1) - 메인설비여부
	private String eluseyn; // VARCHAR(1) - 매핑사용여부

	// Data
	private String oldWarekey; // 이전 창고키
	private String oldAreakey; // 이전 창고동키

	private String whnamlc; // Warehouse Name<커스텀>
	private String areanam; // Area Name<커스텀>
	private String locanam; // 베드명<커스텀>
	private String equinam; // 설비명칭<커스텀>

	// api
	private String equipkyList;
	private List<String> locakeyList;
	private String loctype;
	private String areatyp;
	private String louseyn; // 베드 사용여부
}
