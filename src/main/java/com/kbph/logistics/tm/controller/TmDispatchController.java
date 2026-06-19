package com.kbph.logistics.tm.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kbph.logistics.md.domain.MgrpmaDTO;
import com.kbph.logistics.om.domain.TpwpwgDTO;
import com.kbph.logistics.tm.domain.TmDispatchDTO;
import com.kbph.logistics.tm.domain.TplnhdDTO;
import com.kbph.logistics.tm.domain.TplnitDTO;
import com.kbph.logistics.tm.service.TmDispatchService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TmDispatchController {

	private final TmDispatchService tmDispatchService;
	
	//공통
	@GetMapping("/tm/dispatch/common/init")
	public TmDispatchDTO getTmCommonInit(TmDispatchDTO dispatch) {
		return tmDispatchService.getTmCommonInit(dispatch);
	}
	
	//운송현황 헤드그리드
	@GetMapping("/tm/dispatch/common/headgrid")
	public List<TplnhdDTO> getDispatchHeadOrder(TplnhdDTO tplnhd) {
		return tmDispatchService.getDispatchHeadOrder(tplnhd);
	}
	
	//운송현황 헤드 (tpwpwg 조인 다름)
	@GetMapping(path = {"/tm/dispatch/doe070/current-record", "/tm/dispatch/doe071/current-record", "/tm/dispatch/doe072/current-record", "/tm/dispatch/doe061/headgrid"})
	public List<TplnhdDTO> getDispatchCurrentHeadOrder(TplnhdDTO tplnhd) {
		return tmDispatchService.getDispatchCurrentHeadOrder(tplnhd);
	}
	
	//운송현황 아이템그리드
	@GetMapping("/tm/dispatch/common/itemgrid")
	public List<TplnitDTO> getDispatchItemOrder(TplnitDTO tplnit) {
		return tmDispatchService.getDispatchItemOrder(tplnit);
	}
	
	//doe064 (배차승인내역조회) init
	@GetMapping(path = {"/tm/dispatch/doe064/init", "/tm/dispatch/doe068/init"})
	public TmDispatchDTO getTmDoe064Init(TmDispatchDTO dispatch) {
		return tmDispatchService.getTmDoe064Init(dispatch);
	}
	
	//배차 승인, 취소 업데이트
	@PatchMapping(path = {"/tm/dispatch/doe062/update", "/tm/dispatch/doe066/update"})
	public int updateDispatchAprovyn(@RequestBody TplnhdDTO tplnhd) {
		return tmDispatchService.updateDispatchAprovyn(tplnhd);
	}
	
	//doe070 (배차변경) init
	@GetMapping(path = {"/tm/dispatch/doe070/init", "/tm/dispatch/doe071/init", "/tm/dispatch/doe072/init"})
	public TmDispatchDTO getTmDoe070Init(TmDispatchDTO dispatch) {
		return tmDispatchService.getTmDoe070Init(dispatch);
	}
	
	//운송상태 업데이트
	@PatchMapping(path = {"/tm/dispatch/doe070/update", "/tm/dispatch/doe071/update", "/tm/dispatch/doe072/update"})
	public int doe070Update(@RequestBody TmDispatchDTO dispatch) {
		return tmDispatchService.doe070Update(dispatch);
	}
	
	//운송오더 헤드 조회
	@GetMapping(path = {"/tm/dispatch/doe058/headgrid", "/tm/dispatch/doe061/ordergrid"})
	public List<TmDispatchDTO> getVehicleOrderList(TmDispatchDTO dispatch) {
		return tmDispatchService.getVehicleOrderList(dispatch);
	}
	
	//운송오더 아이템 조회
	@GetMapping("/tm/dispatch/doe058/itemgrid")
	public List<TmDispatchDTO> getVehicleOrderItemList(TmDispatchDTO dispatch) {
		return tmDispatchService.getVehicleOrderItemList(dispatch);
	}
//	
//	//운송오더 init
//	@GetMapping("/tm/dispatch/doe061/init")
//	public TmDispatchDTO getDoe061Init(TmDispatchDTO dispatch) {
//		return tmDispatchService.getDoe061Init(dispatch);
//	}
	
	//차량조회
	@GetMapping("/tm/dispatch/doe058/vehiclegrid")
	public List<TmDispatchDTO> getVehicleList(TmDispatchDTO dispatch) {
		return tmDispatchService.getVehicleList(dispatch);
	}
	
	//차량-기사 매핑
	@GetMapping("/tm/dispatch/doe058/driverlist")
	public List<TmDispatchDTO> getVehicleDriverMapping(TmDispatchDTO dispatch) {
		return tmDispatchService.getVehicleDriverMapping(dispatch);
	}
	
	@GetMapping("/tm/dispatch/doe058/init")
	public TmDispatchDTO getDoe058Init(TmDispatchDTO dispatch) {
		return tmDispatchService.getDoe058Init(dispatch);
	}
	
	//운행일지(일단위)
	@GetMapping("/tm/dispatch/doe076/daylog")
	public List<TplnhdDTO> getOperationDayLog(TplnhdDTO tplnhd) {
		return tmDispatchService.getOperationDayLog(tplnhd);
	}
	
	//운행일지(월단위)
	@GetMapping("/tm/dispatch/doe076/monthlog")
	public List<TplnhdDTO> getOperationMonthLog(TplnhdDTO tplnhd) {
		return tmDispatchService.getOperationMonthLog(tplnhd);
	}
	
	//배차
	@PostMapping("/tm/dispatch/doe058/insert")
	public int insertTplnhd(@RequestBody TplnhdDTO tplnhd) {
		return tmDispatchService.insertTplnhd(tplnhd);
	}
	
	//배차리스트
	@GetMapping("/tm/dispatch/common/plnsizelist")
	public List<MgrpmaDTO> getPlnsizeList(MgrpmaDTO mgrpma) {
		return tmDispatchService.getPlnsizeList(mgrpma);
	}
	
	//운송오더 update
	@PostMapping("/tm/dispatch/doe061/add/transport")
	public int updateDoe061(@RequestBody TplnhdDTO tplnhd) {
		return tmDispatchService.doe061AddTransportPlan(tplnhd);
	}
	
	//출고중량 조회
	@GetMapping("/tm/dispatch/doe061/tpwpwg")
	public List<TpwpwgDTO> getTpwpwgList(TpwpwgDTO tpwpwg) {
		return tmDispatchService.getTpwpwgList(tpwpwg);
	}
	
	//로케이션 리스트
	@GetMapping("/tm/dispatch/common/location/list")
	public List<TmDispatchDTO> getLocationList(TmDispatchDTO dispatch) {
		return tmDispatchService.getLocationList(dispatch);
	}
	
	//로케이션 필터 리스트
	@PostMapping("/tm/dispatch/common/location/filter-list")
	public List<TmDispatchDTO> getLocationFilterList(@RequestBody TmDispatchDTO dispatch) {
		return tmDispatchService.getLocationFilterList(dispatch);
	}
	
	@PostMapping("/tm/dispatch/doe061/location/filter-list")
	public List<TmDispatchDTO> getDoe061LocationList(@RequestBody TmDispatchDTO dispatch) {
		return tmDispatchService.getDoe061LocationList(dispatch);
	}
}
