package com.kbph.logistics.sy.domain;

import com.kbph.logistics.common.domain.CommonColumnDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SusrvhDTO extends CommonColumnDTO  {

	private String userkey;
	private String vehicky;
	private String vownkey;
	private String vonamlc;
	private String vhownyn;
	private String vhuseyn;
	private String vhcsnam;
	private String vhcfnam;
    private String dlvtype;
    private String vhcopty;
    private String vhctype;
    private String vhcstat;
    private String vhctncd;
    private int vhcmaxw;
    private double vhccapa;
    private String drvernm;
    private String drverph;
    private String inbvhyn;
    private String oubvhyn;
    private String poscoyn;
    private String vhcvryn;
    private String useract;
    private String activyn;

    private String oldveky; // old vehicky
    private String oldvoky; // old vownkey
    private String subDomain; // 서브도메인 (사업장명)
}
