package com.kbph.logistics.sy.domain;

import com.kbph.logistics.common.domain.CommonColumnDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SrolgrDTO extends CommonColumnDTO {
	private String compkey; //임시
	private String useract; //임시
	private String cnfrmyn;

	private String rolgkey;	// 롤 그룹 키
	private String rolgnam;	// 롤 그룹 명
	private String roldesc;	// 롤 그룹 설명
	private String rluseyn; // 롤 그룹 사용여부

	private String oldrlky; // 변경 전 롤 그룹 키

}
