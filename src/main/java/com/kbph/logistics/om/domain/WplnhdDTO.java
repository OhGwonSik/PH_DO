package com.kbph.logistics.om.domain;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kbph.logistics.common.domain.CommonColumnDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class WplnhdDTO extends CommonColumnDTO {
	private String shpplky;
	private String warekey;
	private String ownerky;
	private String regikey;
	private String destkey;
	private String denamlc;
	private String desaddr;
	private int shpweig;
	private int remweig;
	private String skugrky;
	private String wpldate;
	private String wpltime;
	private String doccate;
	private String doctype;
	private String wplstat;
	private String rsncode;
	private String parsnnm;

	// Data
	private String wpldateFrom;
	private String wpldateTo;
	private String vhplnky;
	private String alcweig; // 할당 무게
	private String plnsize; // 배차 사이즈
	private String wplcfyn; // 출고 예정 확정 여부
	private String vehicky; // 차량 코드
	private String tpriloc; // 우선 정차 포인트
	private String spriloc; // 차선 정차 포인트
	private String tpnstat; // 배차 계획 상태
	private String whnamlc; // 창고 명칭
	private String docctnm; // 문서 유형 명칭
	private String doctynm; // 문서 타입 명칭
	private String wplstnm; // 출고 계획 상태명
	private String renamlc; // 목적지명
	private String vhcfnam; // 차량번호
	private String grpkynm; // 제품그룹명
	private String rsncdnm; // 사유코드명
	private String plnsznm; // 배차사이즈명
	private List<String> regikeyList; // 목적지코드 리스트
	private List<String> skumkeyList;
	private List<String> orderlnList;

	private String shplimtFrom;
	private String shplimtTo;
	private String demodnm;
	private String skumkey;
	private String orderln;
	private boolean remweigCheck;

	//override
	@Override
	public boolean equals(Object anObject) {
        if (this == anObject) {
            return true;
        }
        return (anObject instanceof WplnhdDTO wplnhd)
               && Objects.equals(this.shpplky, wplnhd.shpplky)
               && Objects.equals(this.warekey, wplnhd.warekey);
    }

	@Override
	public int hashCode() {
		return Objects.hash(shpplky, warekey);
	}

	//vhplnky 중복값 제거
	@Getter
    public static class VhplnkyHashSetWrapper {
		private final String vhplnky;
		private final String warekey;

        public VhplnkyHashSetWrapper(String vhplnky, String warekey) {
            this.vhplnky = vhplnky;
            this.warekey = warekey;
        }

		@Override
		public boolean equals(Object obj) {
			if (this == obj) return true;
			if (!(obj instanceof VhplnkyHashSetWrapper that)) return false;
			return Objects.equals(vhplnky, that.vhplnky) && Objects.equals(warekey, that.warekey);
		}

		@Override
		public int hashCode() {
			return Objects.hash(vhplnky, warekey);
		}

    }
}
