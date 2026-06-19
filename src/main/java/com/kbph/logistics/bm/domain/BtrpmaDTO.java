package com.kbph.logistics.bm.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kbph.logistics.common.domain.ComboDataDTO;
import com.kbph.logistics.common.domain.CommonColumnDTO;
import com.kbph.logistics.md.domain.McodemDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BtrpmaDTO extends CommonColumnDTO {

	private String warekey;		//창고키
	private String btrpkey; 	//운송요율코드
	private String btrcate; 	//정산유형
	private String regikey;		//목적지코드
	private String btrpitc; 	//품목코드
	private String btrpitn; 	//폼목명
	private String btrpigb; 	//품목구분
	private String btrpict; 	//품목계열
	private String btrpvaf; 	//차량소속
	private String btrpitm; 	//운송정산항목
	private int btrpchr; 	//청구단가
	private int btrppay; 	//지급단가
	private String btrpeyn; 	//할증여부
	private String sizgbyn; 	//사이즈 구분여부(지급)
	private String btrdfyn; 	//부적운임기준여부
	private String btrpdaf; 	//계약년월From
	private String btrpdat; 	//계약년월To
	private String useract;
	private String stnweig;

	private String whnamlc;		//창고명
	private String renamlc;		//목적지명
	private String btrplsq;		//운송요율 로그번호
	private String crudmod;		//CRUD 방식

	@JsonProperty("pq_ri")
	private String pqRi;
	private List<String> regikeys;

	private List<BtrcatDTO> btrcatList;
	private List<McodemDTO> btrpItemList; //운송정산비 항목
	private List<McodemDTO> vowaffiList;	//차량소속 리스트
	private List<ComboDataDTO> regionList; //목적지 리스트
	private List<McodemDTO> stnweigList;

}
