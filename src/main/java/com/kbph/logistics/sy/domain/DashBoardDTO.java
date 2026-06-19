package com.kbph.logistics.sy.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DashBoardDTO {

	private int totalOrder;
	private int completeOrder;
	private int totalWeight;
	private int completeWeight;
	private int totalGrossWeight;
	private int completeGrossWeight;
	private int sumWeight;

	private int inbStayedThisMonth;
	private int inbStayedLastMonth;
	private int inbStayedTwoMonthAgo;
	private int oubStayedThisMonth;
	private int oubStayedLastMonth;
	private int oubStayedTwoMonthAgo;
	private float monthOverMonthInbRate;
	private float monthOverMonthOubRate;
	
	private int sumQty;
	private int readyTrans;
	private int drivingTrans;
	private int completeTrans;
	private int cntTrans;
	private String result;
	private String renamlc;
	private String demdadr;
	private String demodnm;
	private String doctynm;
	
	private String equinam;
	private String equipky;
	private String tasksts;
	private String equtype;
	private String docctnm;
	private String areanam;
}