package com.kbph.logistics.sy.domain;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TotalDashBoardDTO {

	private DashBoardDTO inboundBarData;
	private List<DashBoardDTO> inboundDoughnutList;
	private DashBoardDTO outboundBarData;
	private List<DashBoardDTO> outboundDoughnutList;
	private DashBoardDTO stayedYardBarData;
	private List<DashBoardDTO> transGridList;
	private List<DashBoardDTO> transPieList;
	private List<DashBoardDTO> equipmentTaskList;
}
