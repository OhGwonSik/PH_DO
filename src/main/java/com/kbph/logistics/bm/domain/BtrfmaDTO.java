package com.kbph.logistics.bm.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kbph.logistics.common.domain.CommonColumnDTO;
import com.kbph.logistics.md.domain.McodemDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BtrfmaDTO extends CommonColumnDTO {

	private String warekey; //창고키
	private String btrfkey; //이송요율코드
	private String btrcate; //정산유형
	private String btrfitc; //품목코드
	private String btrfitn; //폼목명
	private String btrfigb; //품목구분
	private String btrfict; //품목계열
	private String btrfitm; //이송정산항목
	private int btrfchr; 	//청구단가
	private int btrfpay; 	//지급단가
	private String btrfstc; //진도코드
	private String btrfeyn; //할증여부
	private String sizgbyn; //사이즈 구분여부(지급)
	private String btrdfyn; //부적운임기준여부
	private String btrfdaf; //계약년월From
	private String btrfdat; //계약년월To
	private String useract;
	private String stnweig;
	
	private String whnamlc;
	private String crudmod;
	private String btrflsq;
	
	@JsonProperty("pq_ri")
	private String pqRi;
	
	private List<BtrcatDTO> btrcatList;
	private List<McodemDTO> codeList;
	private List<BaddcoDTO> baddcoList;
	private List<BsizpaDTO> bsizpaList;
	private List<McodemDTO> stnweigList;
}
