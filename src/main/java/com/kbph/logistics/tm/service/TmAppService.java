package com.kbph.logistics.tm.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kbph.logistics.configuration.error.DeleteCheckedException;
import com.kbph.logistics.configuration.error.UpdateCheckedException;
import com.kbph.logistics.om.domain.TpwpwgDTO;
import com.kbph.logistics.tm.domain.TmAppDTO;
import com.kbph.logistics.tm.domain.TplnhdDTO;
import com.kbph.logistics.tm.domain.TplnitDTO;
import com.kbph.logistics.tm.mapper.TmAppMapper;
import com.kbph.logistics.tm.type.Dispatch;
import com.kbph.logistics.tm.type.TmApp;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TmAppService {
	
	private final TmAppMapper tmAppMapper;

	//운송이력
	public List<TplnhdDTO> getTransportRecordList(TplnhdDTO tplnhd) {
		List<TplnhdDTO> mergeData = new ArrayList<>();
		
		for(String schema : tplnhd.getSchemaList()) {
			tplnhd.setSchema(schema);
			mergeData.addAll(tmAppMapper.selectTransportRecordList(tplnhd));
		}
		
		return mergeData;
	}
	
	//운송이력 (tpwpwg join 다름)
	public List<TplnhdDTO> getTransportCurrentRecord(TplnhdDTO tplnhd) {
		List<TplnhdDTO> mergeData = new ArrayList<>();
		
		for(String schema : tplnhd.getSchemaList()) {
			tplnhd.setSchema(schema);
			mergeData.addAll(tmAppMapper.selectTransportCurrentRecord(tplnhd));
		}
		
		return mergeData;
	}
	
	//운송이력 init
	public TmAppDTO getTransportRecordInit(TmAppDTO app) {
		List<TmAppDTO> mergeInitData = new ArrayList<>();
		
		app.setComcdky(TmApp.TPNSTAT.getString());
		
		for(String schema : app.getSchemaList()) {
			app.setSchema(schema);
			mergeInitData.addAll(tmAppMapper.selectMcodemList(app));
		}
		
	    mergeInitData = mergeInitData.stream()
	            .collect(Collectors.collectingAndThen(
	                Collectors.toMap(TmAppDTO::getCombovl, Function.identity(), (existing, replacement) -> existing),
	                map -> new ArrayList<>(map.values())
	            ));
		
		app.setTpnstatList(mergeInitData);
		
		return app;
	}
	
	//목적지 리스트
	public List<TmAppDTO> getRegikeyList(TmAppDTO app) {
		List<TmAppDTO> mergeRegikeyData = new ArrayList<>();
		
		for(String schema : app.getSchemaList()) {
			app.setSchema(schema);
			
			mergeRegikeyData.addAll(tmAppMapper.selectRegikeyList(app));
		}
		
		mergeRegikeyData = mergeRegikeyData.stream()
			    .collect(Collectors.collectingAndThen(
			        Collectors.toMap(TmAppDTO::getRegikey, Function.identity(), (existing, replacement) -> existing),
			        map -> {
			            List<TmAppDTO> sortedList = new ArrayList<>(map.values());
			            sortedList.sort(Comparator.comparing(TmAppDTO::getRenamlc, String.CASE_INSENSITIVE_ORDER));
			            return sortedList;
			        }
			    ));
		
		
		return mergeRegikeyData;
	}
	
	//운송 상세이력 아이템
	public List<TplnitDTO> getTransportDetailHistoryItem(TplnitDTO tplnit) {
		return tmAppMapper.selectTransportDetailHistoryItem(tplnit);
	}
	
	//유저-차량 리스트
	public List<TmAppDTO> getUserVehicleMappingList(TmAppDTO app) {
		return tmAppMapper.selectVehicleList(app);
	}
	
	//승인이력 init
	public TmAppDTO getApprovalRecordInit(TmAppDTO app) {
		List<TmAppDTO> mergeApprovalData = new ArrayList<>();
		
		app.setComcdky(TmApp.APROVYN.getString());
		
		for(String schema : app.getSchemaList()) {
			app.setSchema(schema);
			
			mergeApprovalData.addAll(tmAppMapper.selectMcodemList(app));
		}
		
		app.setAprovynList(mergeApprovalData);
		
		return app;
	}
	
	//토큰 값 갱신
	public int userFcmTokenUpdate(TmAppDTO app) {
		int updateCnt = 0;
		
		updateCnt = tmAppMapper.userFcmTokenUpdate(app);
		
		if(updateCnt == 0) {
			throw new UpdateCheckedException();
		}
		
		return updateCnt;
	}
	
	//토큰 값 가져오기
	public TmAppDTO getUserFcmToken(TmAppDTO app) {
		return tmAppMapper.selectUserFcmToken(app);
	}
	
	//배차계획(승인여부) 가져오기
	public List<TplnhdDTO> getRequestApprovalList(TplnhdDTO tplnhd) {
		List<TplnhdDTO> mergeRequestData = new ArrayList<>();
		
		for(String schema : tplnhd.getSchemaList()) {
			tplnhd.setSchema(schema);
			
			mergeRequestData.addAll(tmAppMapper.selectRequestApprovalList(tplnhd));
		}
		
		return mergeRequestData;
	}
	
	//배차계획(승인업데이트) 업데이트
	public int updateAppAprovyn(TplnhdDTO tplnhd) {
		int updateCnt = 0;
		int deleteCnt = 0;
		String schema = tplnhd.getSchema();
		
		int aprovynUpdateCnt = 0;
		
		//헤더 승인여부 변경
		aprovynUpdateCnt = tmAppMapper.updateAppAprovyn(tplnhd);
		
		updateCnt += aprovynUpdateCnt;
		
		if(aprovynUpdateCnt == 0) {
			throw new UpdateCheckedException();
		}
		
		if(tplnhd.getAprovyn().equals(TmApp.CANCEL.getString())) {
			
			TplnitDTO tplnit = new TplnitDTO();
			tplnit.setVhplnky(tplnhd.getVhplnky());
			tplnit.setWarekey(tplnhd.getWarekey());
			tplnit.setSchema(schema);
			//아이템 테이블 조회
			List<TplnitDTO> itemList = tmAppMapper.selectTransportDetailHistoryItem(tplnit);
			
			//아이템이 존재한다면 for문
			if(!CollectionUtils.isEmpty(itemList)) {
				for(TplnitDTO item : itemList) {
					int itemUpdateCnt = 0;
					
					//아이탬 배차상태 CANCEL로 변경
					item.setUseract(tplnhd.getUseract());
					item.setTpistat(TmApp.CANCEL.getString());
					item.setSchema(schema);
					itemUpdateCnt = tmAppMapper.updateAppVehicleStatusItem(item);
					
					updateCnt += itemUpdateCnt;
					
					if(itemUpdateCnt == 0) {
						throw new UpdateCheckedException();
					}
					
					int wplnitUpdateCnt = 0;
					
					//출고계획아이템 테이블에서 UPDTCHK 확인
					TmAppDTO app = new TmAppDTO();
					app.setShpplky(item.getShpplky());
					app.setShpplit(item.getShpplit());
					app.setWarekey(item.getWarekey());
					app.setSchema(schema);
					
					TmAppDTO wplnit = tmAppMapper.selectAppWplnit(app);
					wplnit.setChkdisp(TmApp.DISABLED.getString());
					wplnit.setSchema(schema);
					wplnit.setUseract(tplnhd.getUseract());
					//출고계획아이템테이블 배차여부 N
					wplnitUpdateCnt = tmAppMapper.tmUpdateWplnit(wplnit);
					
					updateCnt += wplnitUpdateCnt;
					
					if(wplnitUpdateCnt == 0) {
						throw new UpdateCheckedException();
					}
				}
			}
			
			//중량 테이블 SELECT
			TpwpwgDTO tpwpwg = new TpwpwgDTO();
			tpwpwg.setVhplnky(tplnhd.getVhplnky());
			tpwpwg.setSchema(schema);
			
			List<TpwpwgDTO> tpwpwgList = tmAppMapper.selectAppTpwpwgList(tpwpwg);
			
			for(TpwpwgDTO tpwdata : tpwpwgList) {
				int wplnhdUpdateCnt = 0;
				
				TmAppDTO app = new TmAppDTO();
				app.setShpplky(tpwdata.getShpplky());
				app.setSchema(schema);
				//출고계획 조회
				TmAppDTO wplnhd = tmAppMapper.selectAppWplnhd(app);
				
				wplnhd.setRemweig(wplnhd.getRemweig() + (tpwdata.getAlcweig() * 1000));
				wplnhd.setSchema(schema);
				wplnhd.setUseract(tplnhd.getUseract());
				wplnhdUpdateCnt = tmAppMapper.updateAppWplnhd(wplnhd);
				
				updateCnt += wplnhdUpdateCnt;
				
				if(wplnhdUpdateCnt == 0) {
					throw new UpdateCheckedException();
				}
				
				int tpwpwgDeleteCnt = 0;
				
				//중량 테이블 삭제
				tpwdata.setSchema(schema);
				tpwpwgDeleteCnt = tmAppMapper.deleteAppTpwpwg(tpwdata);
				
				deleteCnt += tpwpwgDeleteCnt;
				
				if(tpwpwgDeleteCnt == 0) {
					throw new DeleteCheckedException();
				}
			}
		}
		
		return updateCnt + deleteCnt;
	}
	
	//운송현황(운송상태) 업데이트
	public int updateAppVehicleStatus(TplnhdDTO tplnhd) {
		int updateCnt = 0;
		String schema = tplnhd.getSchema();
		
		TplnitDTO tplnit = new TplnitDTO();
		tplnit.setVhplnky(tplnhd.getVhplnky());
		tplnit.setWarekey(tplnhd.getWarekey());
		tplnit.setDemodnm(tplnhd.getDemodnm());
		tplnit.setCustnam(tplnhd.getCustnam());
		tplnit.setCustcod(tplnhd.getCustcod());
		tplnit.setSchema(schema);
		
		if(tplnhd.getStatus().equals(TmApp.START.getString()) || tplnhd.getStatus().equals(TmApp.FINISH.getString())) {
			int headUpdateCnt = 0;
			
			tplnhd.setTpnstat(tplnhd.getStatus());
			
			//헤더 업데이트
			headUpdateCnt = tmAppMapper.updateAppVehicleStatusHead(tplnhd);
			
			updateCnt += headUpdateCnt;
			
			if(headUpdateCnt == 0) {
				throw new UpdateCheckedException();
			}
			
			//중량테이블 업데이트
			//운송도착일경우 도착상태값 업데이트
			if(tplnhd.getStatus().equals(TmApp.FINISH.getString())) {
				TpwpwgDTO tpwpwgDTO = new TpwpwgDTO();
				tpwpwgDTO.setVhplnky(tplnhd.getVhplnky());
				tpwpwgDTO.setSchema(schema);
				
				for(TpwpwgDTO tpwpwg : tmAppMapper.selectAppTpwpwgList(tpwpwgDTO)) {
					int arrivynUpdateCnt = 0;
					
					tpwpwg.setLmouser(tplnhd.getUseract());
					tpwpwg.setSchema(schema);
					arrivynUpdateCnt = tmAppMapper.updateAppTpwpwgArrivyn(tpwpwg);
					
					updateCnt += arrivynUpdateCnt;
					
					if(arrivynUpdateCnt == 0) {
						throw new UpdateCheckedException();
					}
				}
				
			}
		}
		
		if(tplnhd.getStatus().equals(TmApp.ARRIVE.getString()) || tplnhd.getStatus().equals(TmApp.REFUSAL.getString()) || tplnhd.getStatus().equals(TmApp.START.getString())) {
			for(TplnitDTO itemCheck : tmAppMapper.selectTransportDetailHistoryItem(tplnit)) {
				int itemUpdateCnt = 0;
				
				itemCheck.setTpistat(tplnhd.getStatus());
				itemCheck.setUseract(tplnhd.getUseract());
				itemCheck.setSchema(schema);
				
				itemUpdateCnt = tmAppMapper.updateAppVehicleStatusItem(itemCheck);
				
				updateCnt += itemUpdateCnt;
				
				if(itemUpdateCnt == 0) {
					throw new UpdateCheckedException();
				}
			}
		}
		
		return updateCnt;
	}
	
	//입차리스트 가져오기
	public List<TmAppDTO> getAppComeinVehicleList(TmAppDTO app) {
		List<TmAppDTO> mergeComeinList = new ArrayList<>();
		
		for(String schema : app.getSchemaList()) {
			app.setSchema(schema);
			
			mergeComeinList.addAll(tmAppMapper.selectAppComeinVehicleList(app));
		}
		
		return mergeComeinList;
	}
	
	//입차여부 업데이트
	public int updateAppComeinVehicle(TmAppDTO app) {
		int vehicleUpdateCnt = 0;
		
		vehicleUpdateCnt = tmAppMapper.updateAppComeinVehicle(app);
		
		if(vehicleUpdateCnt == 0) {
			throw new UpdateCheckedException();
		}
		
		return vehicleUpdateCnt;
	}
	
	//알림내역 조회
	public List<TmAppDTO> getAlarmList(TmAppDTO app) {
		List<TmAppDTO> mergeAlarmList = new ArrayList<>();
		
		for(String schema : app.getSchemaList()) {
			app.setSchema(schema);
			
			mergeAlarmList.addAll(tmAppMapper.selectAlarmList(app));
		}
		
		return mergeAlarmList;
	}
	
	//알림내역 삭제
	public int deleteAlarm(TmAppDTO app) {
		int deleteCnt = 0;
		
		for(TmAppDTO alarm : app.getAlarmCheckList()) {
			int alaramDeleteCnt = 0;
			alarm.setSchema(alarm.getSchemaName());
			
			alaramDeleteCnt = tmAppMapper.deleteAlarm(alarm);
			
			deleteCnt += alaramDeleteCnt;
			
			if(alaramDeleteCnt == 0) {
				throw new DeleteCheckedException();
			}
		}
		
		return deleteCnt;
	}
	
	//입고예정내역 가져오기
	public List<TmAppDTO> getWasnhdList(TmAppDTO app) {
		List<TmAppDTO> mergeWasnhdList = new ArrayList<>();
		
		for(String schema : app.getSchemaList()) {
			app.setSchema(schema);
			
			mergeWasnhdList.addAll(tmAppMapper.selectWasnhdList(app));
		}
		
		return mergeWasnhdList;
	}
	
	//입고예정내역(아이템) 가져오기
	public List<TmAppDTO> getWasnitList(TmAppDTO app) {
		List<TmAppDTO> mergeWasnitList = new ArrayList<>();
		
		for(String schema : app.getSchemaList()) {
			app.setSchema(schema);
			
			mergeWasnitList.addAll(tmAppMapper.selectWasnitList(app));
		}
		return mergeWasnitList;
	}
	
	//입고검수 업데이트
	public int updateCarryCheck(TmAppDTO app) {
		app.setSchema(app.getSchema());
		
		int stlayerUpdateCnt = 0;
		int carryUpdateCnt = 0;
		int headUpdateCnt = 0;
		
		stlayerUpdateCnt = tmAppMapper.changeStlayer(app);
		
		if(stlayerUpdateCnt == 0) {
			throw new UpdateCheckedException();
		}
		
		carryUpdateCnt = tmAppMapper.updateCarryCheck(app);
		
		if(carryUpdateCnt == 0) {
			throw new UpdateCheckedException();
		}
		
		headUpdateCnt = tmAppMapper.updateCarryHeadStatus(app);
		
		if(headUpdateCnt == 0) {
			throw new UpdateCheckedException();
		}
		
		return stlayerUpdateCnt + carryUpdateCnt + headUpdateCnt;
	}
	
	//운행일지
	public List<TplnhdDTO> getAppOperationLog(TplnhdDTO tplnhd) {
		List<TplnhdDTO> mergeOperationLog = new ArrayList<>();
		
		for(String schema : tplnhd.getSchemaList()) {
			tplnhd.setSchema(schema);
			
			mergeOperationLog.addAll(tmAppMapper.selectAppOperationLog(tplnhd));
		}
		
		return mergeOperationLog;
	}
	
	//출고이력 헤더
	public List<TmAppDTO> getAppReleaseHeadList(TmAppDTO app) {
		List<TmAppDTO> mergeReleaseHeadList = new ArrayList<>();
		
		for(String schema : app.getSchemaList()) {
			app.setSchema(schema);
			
			mergeReleaseHeadList.addAll(tmAppMapper.selectAppReleaseHeadList(app));
		}
		
		return mergeReleaseHeadList;
	}
	
	//출고이력 아이템
	public List<TmAppDTO> getAppReleaseItemList(TmAppDTO app) {
		return tmAppMapper.selectAppReleaseItemList(app);
	}
	
	//입차리스트(출고)
	public List<TmAppDTO> getAppReleaseList(TmAppDTO app) {
		List<TmAppDTO> mergeReleaseList = new ArrayList<>();
		
		for(String schema : app.getSchemaList()) {
			app.setSchema(schema);
			
			mergeReleaseList.addAll(tmAppMapper.selectAppReleaseList(app));
		}
		
		return mergeReleaseList;
 	}
	
	//입차리스트(출고) 업데이트
	public int updateAppReleaseList(TmAppDTO app) {
		int vehicleUpdateCnt = 0;
		
		vehicleUpdateCnt = tmAppMapper.updateAppReleaseVehicle(app);
		
		if(vehicleUpdateCnt == 0) {
			throw new UpdateCheckedException();
		}
		
		return vehicleUpdateCnt;
	}
	
	//입고이력
	public List<TmAppDTO> getAppCarryRecordList(TmAppDTO app) {
		List<TmAppDTO> mergeReleaseList = new ArrayList<>();
		
		for(String schema : app.getSchemaList()) {
			app.setSchema(schema);
			
			mergeReleaseList.addAll(tmAppMapper.selectAppCarryRecordList(app));
		}
		
		return mergeReleaseList;
	}
	
	//입고이력 아이템
	public List<TmAppDTO> getAppCarryRecordItemList(TmAppDTO app) {
		return tmAppMapper.selectAppCarryRecordItemList(app);
	}
	
	//앱 버전 관리
	public ResponseEntity<Map<String, String>> getAppVersion() throws IOException {
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		
		Resource[] resources = resolver.getResources("classpath:static/appapk/*");
		
		String fileName = "";
		
		//폴더내에 파일은 무조건 1개만 있어야함
		if(resources.length > 0) {
			fileName = resources[0].getFilename();
		}
		
		String appVersion = "";
		
		if(fileName != null && !fileName.equals("")) {
			int lastIndex = fileName.lastIndexOf(".");
			
			String appName = fileName.substring(0, lastIndex);
			
			appVersion = appName.replace("PH_APP_", "");
		}
		
		return ResponseEntity.ok(Map.of("appVersion", appVersion));
	}
	
	//앱 업데이트
	public ResponseEntity<Resource> appDownload() throws IOException {
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		
		Resource[] resources = resolver.getResources("classpath:static/appapk/*");
		
		Resource apkFile = null;
        String fileName = "";
		
		if(resources.length == 0) {
            apkFile = new ByteArrayResource(new byte[0]);
            fileName = "empty.apk";
		}else {
			apkFile = resources[0];
			fileName = apkFile.getFilename();
		}
		
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM)
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
				.body(apkFile);
	}
}
