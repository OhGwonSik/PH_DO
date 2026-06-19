package com.kbph.logistics.tm.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import com.kbph.logistics.api.config.ApiConfig;
import com.kbph.logistics.api.enums.ApiInfo;
import com.kbph.logistics.common.util.RequestUtil;
import com.kbph.logistics.common.util.SecurityUtils;
import com.kbph.logistics.configuration.error.DeleteCheckedException;
import com.kbph.logistics.configuration.error.InsertCheckedException;
import com.kbph.logistics.configuration.error.NoSaveDataException;
import com.kbph.logistics.configuration.error.UpdateCheckedException;
import com.kbph.logistics.im.mapper.InboundMapper;
import com.kbph.logistics.md.domain.McodemDTO;
import com.kbph.logistics.md.domain.MdocmaDTO;
import com.kbph.logistics.md.domain.MgrpmaDTO;
import com.kbph.logistics.md.domain.MlocmaDTO;
import com.kbph.logistics.md.mapper.OrganizationMapper;
import com.kbph.logistics.md.service.CodeService;
import com.kbph.logistics.md.service.DocumentService;
import com.kbph.logistics.om.domain.TpwpwgDTO;
import com.kbph.logistics.sy.domain.UserVO;
import com.kbph.logistics.tm.domain.TmDispatchDTO;
import com.kbph.logistics.tm.domain.TplnhdDTO;
import com.kbph.logistics.tm.domain.TplnitDTO;
import com.kbph.logistics.tm.mapper.TmDispatchMapper;
import com.kbph.logistics.tm.type.Dispatch;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TmDispatchService {

	private final TmDispatchMapper tmDispatchMapper;
	private final CodeService codeService;
	private final DocumentService documentService;
	private final OrganizationMapper organizationMapper;
	private final InboundMapper inboundMapper;
	private final ApiConfig apiConfig;
	
	//공통
	public TmDispatchDTO getTmCommonInit(TmDispatchDTO dispatch) {
		
		MdocmaDTO mdocma = new MdocmaDTO();
		mdocma.setDoccate(Dispatch.DOCCATE.getString());
		mdocma.setWarekey(dispatch.getWarekey());
		dispatch.setDoctypeList(documentService.getDocumentTypeSelectBox(mdocma));
		
		return dispatch;
	}
	
	//운송현황 헤드 그리드
	public List<TplnhdDTO> getDispatchHeadOrder(TplnhdDTO tplnhd) {
		UserVO userInfo = SecurityUtils.getCustomUserDetails().getUserInfo();
		return tmDispatchMapper.selectDispatchHeadOrder(tplnhd, userInfo);
	}
	
	//운송현황 (TPWPWG 조인 다름)
	public List<TplnhdDTO> getDispatchCurrentHeadOrder(TplnhdDTO tplnhd) {
		UserVO userInfo = SecurityUtils.getCustomUserDetails().getUserInfo();
		return tmDispatchMapper.selectDispatchCurrentHeadOrder(tplnhd, userInfo);
	}
	
	//운송현황 아이템 그리드
	public List<TplnitDTO> getDispatchItemOrder(TplnitDTO tplnit) {
		UserVO userInfo = SecurityUtils.getCustomUserDetails().getUserInfo();
		return tmDispatchMapper.selectDispatchItemOrder(tplnit, userInfo);
	}
	
	//doe064 (배차승인내역조회) init
	public TmDispatchDTO getTmDoe064Init(TmDispatchDTO dispatch) {
		
		McodemDTO mcodem = new McodemDTO();
		mcodem.setComcdky(Dispatch.APROVYN.getString());
		dispatch.setAprovynList(codeService.getCommonCodeSelectBox(mcodem));
		
		return dispatch;
	}
	
	//배차 승인, 취소 업데이트
	public int updateDispatchAprovyn(TplnhdDTO tplnhd) {
		UserVO userInfo = SecurityUtils.getCustomUserDetails().getUserInfo();
		int updateCnt = 0;
		int deleteCnt = 0;
		
		for(TplnhdDTO data : tplnhd.getCheckList()) {
			int tplnhdUpdateCnt = 0;
			//헤더 승인여부 변경
			tplnhd.setVhplnky(data.getVhplnky());
			tplnhd.setWarekey(data.getWarekey());
			tplnhdUpdateCnt = tmDispatchMapper.updateTplnhdAprovyn(tplnhd, userInfo);
			
			updateCnt += tplnhdUpdateCnt;
			
			if(tplnhdUpdateCnt == 0) {
				throw new UpdateCheckedException();
			}
			
			if(tplnhd.getAprovyn().equals(Dispatch.CANCEL.getString())) {
				//아이템 테이블 SELECT
				TplnitDTO item = new TplnitDTO();
				item.setVhplnky(data.getVhplnky());
				item.setWarekey(data.getWarekey());
				List<TplnitDTO> itemList = tmDispatchMapper.selectDispatchItemOrder(item, userInfo);
				
				if(!CollectionUtils.isEmpty(itemList)) {
					//아이템이 존재한다면 FOR문 
					for(TplnitDTO itemData : itemList) {
						int tpistatUpdateCnt = 0;
						int wplnitUpdateCnt = 0;
						
						//아이템 배차상태 CANCEL로 변경
						item.setTpistat(Dispatch.CANCEL.getString());
						item.setUpdtchk(itemData.getUpdtchk());
						item.setVhplnit(itemData.getVhplnit());
						item.setWarekey(itemData.getWarekey());
						tpistatUpdateCnt = tmDispatchMapper.updateTpistat(item, userInfo);
						
						updateCnt += tpistatUpdateCnt;
						
						if(tpistatUpdateCnt == 0) {
							throw new UpdateCheckedException();
						}
						
						TmDispatchDTO dispatch = new TmDispatchDTO();
						dispatch.setShpplky(itemData.getShpplky());
						dispatch.setShpplit(itemData.getShpplit());
						dispatch.setWarekey(itemData.getWarekey());
						
						//출고계획아이템테이블 배차여부 N
						dispatch.setChkdisp(Dispatch.DISABLED.getString());
						wplnitUpdateCnt = tmDispatchMapper.tmUpdateWplnit(dispatch, userInfo);
						
						updateCnt += wplnitUpdateCnt;
						
						if(wplnitUpdateCnt == 0) {
							throw new UpdateCheckedException();
						}
					}
				}
				
				//중량 테이블 SELECT
				TpwpwgDTO tpwpwg = new TpwpwgDTO();
				tpwpwg.setVhplnky(data.getVhplnky());
				
				List<TpwpwgDTO> tpwpwgList = tmDispatchMapper.selectTpwpwgList(tpwpwg, userInfo);
				
				//for문을 돌리는 이유는 해당 배차계획번호를 가진 tpwpwg데이터를 다 삭제하기 위해서
				//wplnhd에 있는 출고예정번호 데이터 remweig를 업데이트 해줘야하기때문에 
				for(TpwpwgDTO tpwdata : tpwpwgList) {
					int deleteTpwpwgCnt = 0;
					
					TmDispatchDTO tmWplnhd = new TmDispatchDTO();
					tmWplnhd.setShpplky(tpwdata.getShpplky());
					
					//출고계획 데이터 조회
					TmDispatchDTO wplnhd = tmDispatchMapper.selectTmWplnhd(tmWplnhd, userInfo);
					
					//ALCWEIG 출고계획테이블 잔여중량 UPDATE
					wplnhd.setRemweig(wplnhd.getRemweig() + (tpwdata.getAlcweig() * 1000));
					updateCnt = tmDispatchMapper.tmUpdateWplnhd(wplnhd, userInfo);
					
					//중량 테이블 DELETE
					deleteTpwpwgCnt = tmDispatchMapper.deleteTmTpwpwg(tpwdata, userInfo);
					
					deleteCnt += deleteTpwpwgCnt;
					
					if(deleteTpwpwgCnt == 0) {
						throw new DeleteCheckedException();
					}
				}
			}
		}
		
		return updateCnt + deleteCnt;
	}
	
	//doe070 (배차변경) init
	public TmDispatchDTO getTmDoe070Init(TmDispatchDTO dispatch) {
		
		McodemDTO mcodem = new McodemDTO();
		mcodem.setComcdky(Dispatch.TPISTAT.getString());
		mcodem.setComcdsy("Y");
		dispatch.setTpistatList(codeService.getCommonCodeSelectBox(mcodem));
		
		mcodem.setComcdky(Dispatch.TPNSTAT.getString());
		mcodem.setComcdsy("Y");
		dispatch.setTpnstatList(codeService.getCommonCodeSelectBox(mcodem));
		
		MdocmaDTO mdocma = new MdocmaDTO();
		mdocma.setDoccate(Dispatch.DOCCATE.getString());
		dispatch.setDoctypeList(documentService.getDocumentTypeSelectBox(mdocma));
		
		return dispatch;
	}
	
	public int selectTpwpwgAndUpdate(TpwpwgDTO tpwpwgDTO, UserVO userInfo, String arrivyn) {
		int updateCnt = 0;
		//중량테이블 업데이트
		for(TpwpwgDTO tpwpwg : tmDispatchMapper.selectTpwpwgList(tpwpwgDTO, userInfo)) {
			int tpwpwgUpdateCnt = 0;
			
			tpwpwg.setArrivyn(arrivyn);
			tpwpwgUpdateCnt = tmDispatchMapper.updateTmTpwpwg(tpwpwg, userInfo);
			
			updateCnt += tpwpwgUpdateCnt;
			
			if(tpwpwgUpdateCnt == 0) {
				throw new UpdateCheckedException();
			}
		}
		
		return updateCnt;
	}
	
	//doe070 운송상태 변경
	public int doe070Update(TmDispatchDTO dispatch) {
		UserVO userInfo = SecurityUtils.getCustomUserDetails().getUserInfo();
		
		TplnitDTO tplnit = new TplnitDTO();
		TpwpwgDTO tpwpwgDTO = new TpwpwgDTO();
		List<MlocmaDTO> tempBedList = new ArrayList<>();
		List<TplnitDTO> wstkkyTemp = new ArrayList<>();
		
		int updateCnt = 0;
		int insertCnt = 0;
		
		if(!dispatch.getUpdateTpnstatList().isEmpty()) {
			for(TplnhdDTO tplnhd : dispatch.getUpdateTpnstatList()) {
				int tplnhdUpdateCnt = 0;
				
				//필수값 체크
				if(tplnhd.getTpnstat() == null || tplnhd.getTpnstat().equals("")) {
					throw new UpdateCheckedException();
				}
				
				//헤드 update
				tplnhdUpdateCnt = tmDispatchMapper.updateTplnhdTpnstat(tplnhd, userInfo);
				
				updateCnt += tplnhdUpdateCnt;
				
				if(tplnhdUpdateCnt == 0) {
					throw new UpdateCheckedException();
				}
				
				if(tplnhd.getTpnstat().equals(Dispatch.FINISH.getString())) {
					tpwpwgDTO.setVhplnky(tplnhd.getVhplnky());
					updateCnt += selectTpwpwgAndUpdate(tpwpwgDTO, userInfo, Dispatch.ENABLED.getString());
				}else if(tplnhd.getTpnstat().equals(Dispatch.START.getString())) {
					tpwpwgDTO.setVhplnky(tplnhd.getVhplnky());
					updateCnt += selectTpwpwgAndUpdate(tpwpwgDTO, userInfo, Dispatch.DISABLED.getString());
				}else if(tplnhd.getTpnstat().equals(Dispatch.REFUSAL.getString())) {
					//임시베드 찾기
					MlocmaDTO mlocma = new MlocmaDTO();
					mlocma.setWarekey(tplnhd.getWarekey());
					mlocma.setLoctype(Dispatch.TMP.getString());
					mlocma.setLouseyn("Y");
					
					tempBedList = organizationMapper.selectMlocmaList(mlocma, userInfo);
					if(tempBedList.isEmpty()) {
						throw new NoSaveDataException("임시베드가 존재하지 않습니다.");
					}
				}
					
				tplnit.setVhplnky(tplnhd.getVhplnky());
				tplnit.setWarekey(tplnhd.getWarekey());
				
				List<TplnitDTO> tplnitList = tmDispatchMapper.selectDispatchItemOrder(tplnit, userInfo);
				
				for(TplnitDTO item : tplnitList) {
					//아이템 그리드 내용도 같이 업데이트 해줌
					int tpistatUpdateCnt = 0;
					
					if(tplnhd.getTpnstat().equals(Dispatch.FINISH.getString())) {
						if(!item.getTpistat().equals(Dispatch.REFUSAL.getString())) {
							item.setTpistat(Dispatch.ARRIVE.getString());
						}
					}else if(tplnhd.getTpnstat().equals(Dispatch.REFUSAL.getString())) {
						
						//맥스 단위치
						TmDispatchDTO dispatchDTO = new TmDispatchDTO();
						dispatchDTO.setWarekey(item.getWarekey());
						dispatchDTO.setAreakey(tempBedList.get(0).getAreakey());
						dispatchDTO.setLocakey(tempBedList.get(0).getLocakey());
						//결과값을 int로 해놔서 null 체크 따로 안함.
						Integer maxlayer = tmDispatchMapper.selectMaxLayerByLocation(dispatchDTO, userInfo);
						
						if(maxlayer == null) {
							maxlayer = 0;
						}
						
						String stockky = inboundMapper.selectStockky(userInfo);
						String lotnmky = inboundMapper.selectLotnmky(userInfo);
						
						//재고생성
						item.setStockky(stockky);
						item.setLotnmky(lotnmky);
						item.setAreakey(tempBedList.get(0).getAreakey());
						item.setLocakey(tempBedList.get(0).getLocakey());
						item.setLolayer(maxlayer + 1);
						item.setStotqty(1);
						item.setUpinfst("Y");
						
						//api 값
						item.setStlayer(item.getLolayer());
						item.setSkustat("00");
						
						//재고생성
						int wstkdyInsertCnt = tmDispatchMapper.insertWstkkyData(item, userInfo);
						
						if(wstkdyInsertCnt == 0) {
							throw new InsertCheckedException();
						}
						
						insertCnt += wstkdyInsertCnt;
						
						wstkkyTemp.add(item);
						
						item.setTpistat(tplnhd.getTpnstat());
					}else {
						item.setTpistat(tplnhd.getTpnstat());
					}
					
					tpistatUpdateCnt = tmDispatchMapper.updateTpistat(item, userInfo);
					
					updateCnt += tpistatUpdateCnt;
					
					if(tpistatUpdateCnt == 0) {
						throw new UpdateCheckedException();
					}
				}
				
				if(!wstkkyTemp.isEmpty() && tplnitList.size() == wstkkyTemp.size()) {
					//api 호출
					apiConfig.requestToDC(wstkkyTemp, ApiInfo.API_SM_WPHYIT_CREATE, HttpMethod.POST); 
				}else if(!wstkkyTemp.isEmpty() && tplnitList.size() != wstkkyTemp.size()) {
					throw new UpdateCheckedException("생성된 재고의 갯수가 맞지 않습니다.");
				}
			}
		}
		
		//아이템 개별 업데이트
		if(!dispatch.getUpdateTpistatList().isEmpty()) {
			for(TplnitDTO item : dispatch.getUpdateTpistatList()) {
				tplnit.setVhplnky(item.getVhplnky());
				tplnit.setWarekey(item.getWarekey());
				int tpistatUpdateCnt = 0;
				
				tpistatUpdateCnt = tmDispatchMapper.updateTpistat(item, userInfo);
				
				updateCnt += tpistatUpdateCnt;
				
				if(tpistatUpdateCnt == 0) {
					throw new UpdateCheckedException();
				}
			}
		}

		return updateCnt + insertCnt;
	}
	
	//운송오더 헤드 조회
	public List<TmDispatchDTO> getVehicleOrderList(TmDispatchDTO dispatch) {
		UserVO userInfo = SecurityUtils.getCustomUserDetails().getUserInfo();
		return tmDispatchMapper.selectVehicleOrderList(dispatch, userInfo);
	}
	
	//로케이션 리스트
	public List<TmDispatchDTO> getLocationList(TmDispatchDTO dispatch) {
		UserVO userInfo = SecurityUtils.getCustomUserDetails().getUserInfo();
		return tmDispatchMapper.selectLocationList(dispatch, userInfo);
	}
	
	//로케이션 필터 리스트
	public List<TmDispatchDTO> getLocationFilterList(TmDispatchDTO dispatch) {
		UserVO userInfo = SecurityUtils.getCustomUserDetails().getUserInfo();
		return tmDispatchMapper.selectLocationFilterList(dispatch, userInfo);
	}
	
	public List<TmDispatchDTO> getDoe061LocationList(TmDispatchDTO dispatch) {
		UserVO userInfo = SecurityUtils.getCustomUserDetails().getUserInfo();
		
		List<TmDispatchDTO> tplnitList = tmDispatchMapper.selectTplnitWstkkyJoinList(dispatch, userInfo);
		
		if(dispatch.getEmergencyList() != null && !dispatch.getEmergencyList().isEmpty()) {
			if(!tplnitList.isEmpty()) {
				List<TmDispatchDTO> stockkyList = new ArrayList<>();
				stockkyList.addAll(tplnitList);
				stockkyList.addAll(dispatch.getEmergencyList());
				
				dispatch.setStockkyList(stockkyList);
			}else {
				dispatch.setStockkyList(dispatch.getEmergencyList());
			}
			
			return tmDispatchMapper.selectLocationFilterList(dispatch, userInfo);
		}else {
			if(!tplnitList.isEmpty()) {
				dispatch.setStockkyList(tplnitList);
				return tmDispatchMapper.selectLocationFilterList(dispatch, userInfo);
			}else {
				return tmDispatchMapper.selectLocationList(dispatch, userInfo);
			}
		}
	}
	
	//운송오더 아이템 조회
	public List<TmDispatchDTO> getVehicleOrderItemList(TmDispatchDTO dispatch) {
		UserVO userInfo = SecurityUtils.getCustomUserDetails().getUserInfo();
		return tmDispatchMapper.selectVehicleOrderItemList(dispatch, userInfo);
	}
	
	//차량조회
	public List<TmDispatchDTO> getVehicleList(TmDispatchDTO dispatch) {
		UserVO userInfo = SecurityUtils.getCustomUserDetails().getUserInfo();
		dispatch.setDmwplnm(RequestUtil.extractSubDomain());
		return tmDispatchMapper.selectVehicleList(dispatch, userInfo);
	}
	
	//차량-기사 매핑
	public List<TmDispatchDTO> getVehicleDriverMapping(TmDispatchDTO dispatch) {
		return tmDispatchMapper.selectVehicleDriverMapping(dispatch);
	}
	
	//doe058 init
	public TmDispatchDTO getDoe058Init(TmDispatchDTO dispatch) {
		UserVO userInfo = SecurityUtils.getCustomUserDetails().getUserInfo();
		
		MdocmaDTO mdocma = new MdocmaDTO();
		mdocma.setDoccate(Dispatch.WMDOCCATE.getString());
		mdocma.setWarekey(dispatch.getWarekey());
		dispatch.setDoctypeList(documentService.getDocumentTypeSelectBox(mdocma));
		
		MgrpmaDTO mgrpma = new MgrpmaDTO();
		dispatch.setPlnsizeList(tmDispatchMapper.selectPlnsizeList(mgrpma, userInfo));
		
		return dispatch;
	}
	
	//배차사이즈 리스트
	public List<MgrpmaDTO> getPlnsizeList(MgrpmaDTO mgrpma) {
		UserVO userInfo = SecurityUtils.getCustomUserDetails().getUserInfo();
		return tmDispatchMapper.selectPlnsizeList(mgrpma, userInfo);
	}
	
	//운행일지(일단위)
	public List<TplnhdDTO> getOperationDayLog(TplnhdDTO tplnhd) {
		UserVO userInfo = SecurityUtils.getCustomUserDetails().getUserInfo();
		
		//날짜 01 - 31 셋팅
		tplnhd.setDates(Arrays.asList("01", "02", "03", "04", "05", "06", "07", "08", "09", "10",
                "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
                "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"));
		
		return tmDispatchMapper.selectOperationDayLog(tplnhd, userInfo);
	}
	
	//운행일지(월단위)
	public List<TplnhdDTO> getOperationMonthLog(TplnhdDTO tplnhd) {
		UserVO userInfo = SecurityUtils.getCustomUserDetails().getUserInfo();
		return tmDispatchMapper.selectOperationMonthLog(tplnhd, userInfo);
	}
	
	//배차계획 insert
	public int insertTplnhd(TplnhdDTO tplnhd) {
		UserVO userInfo = SecurityUtils.getCustomUserDetails().getUserInfo();
		int insertCnt = 0;
		int updateCnt = 0;
		
		int tplnhdInsertCnt = 0;
		
		String vhplnky = tmDispatchMapper.selectVhplnky(userInfo);
		tplnhd.setVhplnky(vhplnky);
		tplnhd.setDoccate(Dispatch.DOCCATE.getString());
		tplnhd.setDoctype(Dispatch.DOCTYPE.getString());
		tplnhd.setLoadkey(Dispatch.WAREHOUSE.getString());
		
		int emerweig = tplnhd.getEmerweig();
		int totalWeig = tplnhd.getTotalWeight();
		
		if(totalWeig < emerweig) {
			tplnhd.setCorweig(emerweig);
		}else {
			tplnhd.setCorweig(totalWeig);
		}
		
		tplnhdInsertCnt = tmDispatchMapper.insertTplnhd(tplnhd, userInfo);
		
		insertCnt += tplnhdInsertCnt;
		
		if(tplnhdInsertCnt == 0) {
			throw new InsertCheckedException();
		}
		
		for(TmDispatchDTO order : tplnhd.getOrderGridCheckedList()) {
			int tpwpwgInsertCnt = 0;
			
			TpwpwgDTO tpwpwg = new TpwpwgDTO();
			tpwpwg.setAlcweig(order.getOdweig());
			tpwpwg.setVhplnky(vhplnky);
			tpwpwg.setShpplky(order.getShpplky());
			tpwpwg.setPlnsize(order.getPlnsize());
			tpwpwg.setRegikey(order.getRegikey());
			
			tpwpwgInsertCnt = tmDispatchMapper.insertTpwpwg(tpwpwg, userInfo);
			
			insertCnt += tpwpwgInsertCnt;
			
			if(tpwpwgInsertCnt == 0) {
				throw new InsertCheckedException();
			}
			
			int wplnhdUpdateCnt = 0;
			
			TmDispatchDTO wplnhd = new TmDispatchDTO();
			wplnhd.setShpplky(order.getShpplky());
			wplnhd.setWarekey(order.getWarekey());
			int remweig = order.getRemweig() * 1000;
			int odweig = order.getOdweig() * 1000;
			wplnhd.setRemweig(remweig - odweig);
			wplnhd.setUpdtchk(order.getUpdtchk());
			
			wplnhdUpdateCnt = tmDispatchMapper.tmUpdateWplnhd(wplnhd, userInfo);
			
			updateCnt += wplnhdUpdateCnt;
			
			if(wplnhdUpdateCnt == 0) {
				throw new UpdateCheckedException();
			}
		}
		
		if(tplnhd.getEmergencyList() != null && !tplnhd.getEmergencyList().isEmpty()) {
			for(TplnitDTO emergency : tplnhd.getEmergencyList()) {
				int tplnitInsertCnt = 0;
				
				emergency.setVhplnky(vhplnky);
				emergency.setVhplnit(tmDispatchMapper.selectVhplnit(emergency, userInfo));
				
				tplnitInsertCnt = tmDispatchMapper.insertTplnit(emergency, userInfo);
				
				insertCnt += tplnitInsertCnt;
				
				if(tplnitInsertCnt == 0) {
					throw new InsertCheckedException();
				}
				
				int wplnitUpdateCnt = 0;
				
				TmDispatchDTO wplnit = new TmDispatchDTO();
				wplnit.setShpplky(emergency.getShpplky());
				wplnit.setShpplit(emergency.getShpplit());
				wplnit.setChkdisp(Dispatch.ENABLED.getString());
				wplnit.setWarekey(emergency.getWarekey());
				wplnit.setUpdtchk(emergency.getUpdtchk());
				
				wplnitUpdateCnt = tmDispatchMapper.tmUpdateWplnit(wplnit, userInfo);
				
				updateCnt += wplnitUpdateCnt;
				
				if(wplnitUpdateCnt == 0) {
					throw new UpdateCheckedException();
				}
			}
		}
		
		return insertCnt + updateCnt;
	}

	//추가배차
	public int doe061AddTransportPlan(TplnhdDTO tplnhd) {
		UserVO userInfo = SecurityUtils.getCustomUserDetails().getUserInfo();
		int insertCnt = 0;
		int updateCnt = 0;
		
		int tplnhdUpdateCnt = 0;
		
		tplnhd.setLoadkey(Dispatch.WAREHOUSE.getString());
		
		int emerweig = tplnhd.getEmerweig();
		int totalWeig = tplnhd.getTotalWeight();
		
		if(totalWeig < emerweig) {
			tplnhd.setCorweig(emerweig);
		}else {
			tplnhd.setCorweig(totalWeig);
		}
		
		//배차 업데이트
		tplnhdUpdateCnt = tmDispatchMapper.updateTplnhd(tplnhd, userInfo);
		
		updateCnt += tplnhdUpdateCnt;
		
		if(tplnhdUpdateCnt == 0) {
			throw new UpdateCheckedException();
		}
		
		if(tplnhd.getOrderGridCheckedList() != null && !tplnhd.getOrderGridCheckedList().isEmpty()) {
			for(TmDispatchDTO order : tplnhd.getOrderGridCheckedList()) {
				//중량 테이블 추가
				TpwpwgDTO tpwpwg = new TpwpwgDTO();
				tpwpwg.setVhplnky(tplnhd.getVhplnky());
				tpwpwg.setShpplky(order.getShpplky());
				
				//tpwpwg 단건 조회
				List<TpwpwgDTO> tpwpwgList = tmDispatchMapper.selectTpwpwgList(tpwpwg, userInfo);
				
				if(!tpwpwgList.isEmpty()) {
					boolean isUpdated = false;
					
					for(TpwpwgDTO tpw : tpwpwgList) {
						if(tpw.getPlnsize().equals(order.getPlnsize())) {
							int tpwpwgUpdateCnt = 0;
							
							int tpwpwgOdweig = tpw.getAlcweig() + order.getOdweig();
							tpw.setAlcweig(tpwpwgOdweig);
							
							tpwpwgUpdateCnt = tmDispatchMapper.updateTmTpwpwg(tpw, userInfo);
							
							updateCnt += tpwpwgUpdateCnt;
							
							if(tpwpwgUpdateCnt == 0) {
								throw new UpdateCheckedException();
							}
							
							isUpdated = true;
							
							break;
						}
					}
					
					if(!isUpdated) {
						int tpwpwgInsertCnt = 0;
						
						tpwpwg.setAlcweig(order.getOdweig());
						tpwpwg.setPlnsize(order.getPlnsize());
						tpwpwg.setRegikey(order.getRegikey());
						
						tpwpwgInsertCnt = tmDispatchMapper.insertTpwpwg(tpwpwg, userInfo);
						
						insertCnt += tpwpwgInsertCnt;
						
						if(tpwpwgInsertCnt == 0) {
							throw new InsertCheckedException();
						}
					}
					
				}else {
					int tpwpwgInsertCnt = 0;
					
					tpwpwg.setAlcweig(order.getOdweig());
					tpwpwg.setPlnsize(order.getPlnsize());
					tpwpwg.setRegikey(order.getRegikey());
					
					tpwpwgInsertCnt = tmDispatchMapper.insertTpwpwg(tpwpwg, userInfo);
					
					insertCnt += tpwpwgInsertCnt;
					
					if(tpwpwgInsertCnt == 0) {
						throw new InsertCheckedException();
					}
				}
				
				int wplnhdUpdateCnt = 0;
				
				//wplnhd update (잔여중량 update)
				TmDispatchDTO wplnhd = new TmDispatchDTO();
				wplnhd.setShpplky(order.getShpplky());
				wplnhd.setWarekey(order.getWarekey());
				int remweig = order.getRemweig() * 1000;
				int odweig = order.getOdweig() * 1000;
				wplnhd.setRemweig(remweig - odweig);
				wplnhd.setUpdtchk(order.getUpdtchk());
				
				wplnhdUpdateCnt = tmDispatchMapper.tmUpdateWplnhd(wplnhd, userInfo);
				
				updateCnt += wplnhdUpdateCnt;
				
				if(wplnhdUpdateCnt == 0) {
					throw new UpdateCheckedException();
				}
			}
			
			if(tplnhd.getEmergencyList() != null && !tplnhd.getEmergencyList().isEmpty()) {
				for(TplnitDTO emergency : tplnhd.getEmergencyList()) {
					int tplnitInsertCnt = 0;
					
					//tplnit insert
					emergency.setVhplnky(tplnhd.getVhplnky());
					emergency.setVhplnit(tmDispatchMapper.selectVhplnit(emergency, userInfo));
					
					tplnitInsertCnt = tmDispatchMapper.insertTplnit(emergency, userInfo);
					
					insertCnt += tplnitInsertCnt;
					
					if(tplnitInsertCnt == 0) {
						throw new InsertCheckedException();
					}
					
					int wplnitUpdateCnt = 0;
					
					//wplnit update (배차상태여부 y 변경)
					TmDispatchDTO wplnit = new TmDispatchDTO();
					wplnit.setShpplky(emergency.getShpplky());
					wplnit.setShpplit(emergency.getShpplit());
					wplnit.setChkdisp(Dispatch.ENABLED.getString());
					wplnit.setWarekey(emergency.getWarekey());
					wplnit.setUpdtchk(emergency.getUpdtchk());
					
					wplnitUpdateCnt = tmDispatchMapper.tmUpdateWplnit(wplnit, userInfo);
					
					updateCnt += wplnitUpdateCnt;
					
					if(wplnitUpdateCnt == 0) {
						throw new UpdateCheckedException();
					}
				}
			}	
		}
		
		return insertCnt + updateCnt;
	}
	
	//출고중량 조회
	public List<TpwpwgDTO> getTpwpwgList(TpwpwgDTO tpwpwg) {
		UserVO userInfo = SecurityUtils.getCustomUserDetails().getUserInfo();
		return tmDispatchMapper.selectTpwpwgList(tpwpwg, userInfo);
	}
	
	//차량정보
	public TmDispatchDTO getVehicleInformation(TmDispatchDTO dispatch) {
		return tmDispatchMapper.selectVehicleInformation(dispatch);	
	}
	
	
}
