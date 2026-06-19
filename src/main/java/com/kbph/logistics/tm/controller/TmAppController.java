package com.kbph.logistics.tm.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kbph.logistics.tm.domain.TmAppDTO;
import com.kbph.logistics.tm.domain.TplnhdDTO;
import com.kbph.logistics.tm.domain.TplnitDTO;
import com.kbph.logistics.tm.service.TmAppService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TmAppController {

	private final TmAppService tmAppService;
	
	//운송이력
	@GetMapping("/tm/app/transport/record")
	public List<TplnhdDTO> getTransportRecordList(TplnhdDTO tplnhd) {
		return tmAppService.getTransportRecordList(tplnhd);
	}
	
	//운송이력(tpwpwg join 다름)
	@GetMapping("/tm/app/transport/currnet-record")
	public List<TplnhdDTO> getTransportCurrentRecord(TplnhdDTO tplnhd) {
		return tmAppService.getTransportCurrentRecord(tplnhd);
	}
	
	//운송이력 init
	@GetMapping("/tm/app/transport/record/init")
	public TmAppDTO getTransportRecordInit(TmAppDTO app) {
		return tmAppService.getTransportRecordInit(app);
	}
	
	//목적지 리스트
	@GetMapping("/tm/app/regikeylist")
	public List<TmAppDTO> getRegikeyList(TmAppDTO app) {
		return tmAppService.getRegikeyList(app);
	}
	
	//운송 상세이력 아이템
	@GetMapping("/tm/app/transport/record/detail/item")
	public List<TplnitDTO> getTransportDetailHistoryItem(TplnitDTO tplnit) {
		return tmAppService.getTransportDetailHistoryItem(tplnit);
	}
	
	//유저-차량 리스트
	@GetMapping("/tm/app/user/vehiclelist")
	public List<TmAppDTO> getUserVehicleMappingList(TmAppDTO app) {
		return tmAppService.getUserVehicleMappingList(app);
	}
	
	//승인이력 init
	@GetMapping("/tm/app/transport/aprovyn/init")
	public TmAppDTO getApprovalRecordInit(TmAppDTO app) {
		return tmAppService.getApprovalRecordInit(app);
	}
	
	//토큰 값 갱신
	@PatchMapping("/tm/app/user/update/fcmtoken")
	public int userFcmTokenUpdate(@RequestBody TmAppDTO app) {
		return tmAppService.userFcmTokenUpdate(app);
	}
	
	//배차계획(승인여부) 가져오기
	@GetMapping("/tm/app/transport/aprovyn/list")
	public List<TplnhdDTO> getRequestApprovalList(TplnhdDTO tplnhd) {
		return tmAppService.getRequestApprovalList(tplnhd);
	}
	
	//배차계획(승인업데이트) 업데이트
	@PatchMapping("/tm/app/transport/aprovyn/update")
	public int updateAppAprovyn(@RequestBody TplnhdDTO tplnhd) {
		return tmAppService.updateAppAprovyn(tplnhd);
	}
	
	//운송현황(운송상태) 업데이트
	@PatchMapping("/tm/app/transport/vehiclestatus/update")
	public int updateAppVehicleStatus(@RequestBody TplnhdDTO tplnhd) {
		return tmAppService.updateAppVehicleStatus(tplnhd);
	}
	
	//입차리스트 가져오기
	@GetMapping("/tm/app/logistics/comeinlist")
	public List<TmAppDTO> getAppComeinVehicleList(TmAppDTO app) {
		return tmAppService.getAppComeinVehicleList(app);
	}
	
	//입차여부 업데이트
	@PatchMapping("/tm/app/logistics/comeinlist/update")
	public int updateAppComeinVehicle(@RequestBody TmAppDTO app) {
		return tmAppService.updateAppComeinVehicle(app);
	}
	
	//알림내역 조회
	@GetMapping("/tm/app/alarm/list")
	public List<TmAppDTO> getAlarmList(TmAppDTO app) {
		return tmAppService.getAlarmList(app);
	}
	
	//알림내역 삭제
	@DeleteMapping("/tm/app/alarm/delete")
	public int deleteAlarm(@RequestBody TmAppDTO app) {
		return tmAppService.deleteAlarm(app);
	}
	
	//입고예정내역 가져오기
	@GetMapping("/tm/app/logistics/carry/list")
	public List<TmAppDTO> getWasnhdList(TmAppDTO app) {
		return tmAppService.getWasnhdList(app);
	}
	
	//입고예정내역(아이템) 가져오기
	@GetMapping("/tm/app/logistics/carry/itemlist")
	public List<TmAppDTO> getWasnitList(TmAppDTO app) {
		return tmAppService.getWasnitList(app);
	}
	
	//입고검수 업데이트
	@PatchMapping("/tm/app/logistics/carry/check")
	public int updateCarryCheck(@RequestBody TmAppDTO app) {
		return tmAppService.updateCarryCheck(app);
	}
	
	//운행일지
	@GetMapping("/tm/app/logistics/operation/log")
	public List<TplnhdDTO> getAppOperationLog(TplnhdDTO tplnhd) {
		return tmAppService.getAppOperationLog(tplnhd);
	}
	
	//출고이력 헤더
	@GetMapping("/tm/app/logistics/release/header")
	public List<TmAppDTO> getAppReleaseHeadList(TmAppDTO app) {
		return tmAppService.getAppReleaseHeadList(app);
	}
	
	//출고이력 아이템
	@GetMapping("/tm/app/logistics/release/item")
	public List<TmAppDTO> getAppReleaseItemList(TmAppDTO app) {
		return tmAppService.getAppReleaseItemList(app);
	}
	
	//입차리스트 출고
	@GetMapping("/tm/app/logistics/release/comeinlist")
	public List<TmAppDTO> getAppReleaseList(TmAppDTO app) {
		return tmAppService.getAppReleaseList(app);
	}
	
	//입차리스트 (출고) 업데이트
	@PatchMapping("/tm/app/logistics/relase/cominlist/update")
	public int updateAppReleaseList(@RequestBody TmAppDTO app) {
		return tmAppService.updateAppReleaseList(app);
	}
	
	//입고이력
	@GetMapping("/tm/app/logistics/carry/record")
	public List<TmAppDTO> getAppCarryRecordList(TmAppDTO app) {
		return tmAppService.getAppCarryRecordList(app);
	}
	
	//입고이력-아이템
	@GetMapping("/tm/app/logistics/carry/record-item")
	public List<TmAppDTO> getAppCarryRecordItemList(TmAppDTO app) {
		return tmAppService.getAppCarryRecordItemList(app);
	}
	
	//앱 버전 관리
	@GetMapping("/app/version/get")
	public ResponseEntity<Map<String, String>> getAppVersion() throws IOException {
		return tmAppService.getAppVersion();
	}
	
	//앱 업데이트
	@GetMapping("/app/version/update")
	public ResponseEntity<Resource> appDownload() throws IOException {
		return tmAppService.appDownload();
	}
}
