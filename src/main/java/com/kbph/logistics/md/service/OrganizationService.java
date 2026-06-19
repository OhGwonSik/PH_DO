package com.kbph.logistics.md.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import com.kbph.logistics.api.config.ApiConfig;
import com.kbph.logistics.api.enums.ApiInfo;
import com.kbph.logistics.bm.domain.BtrcatDTO;
import com.kbph.logistics.bm.mapper.BillingMasterMapper;
import com.kbph.logistics.common.domain.ComboDataDTO;
import com.kbph.logistics.common.domain.GridDTO;
import com.kbph.logistics.common.domain.InitDataDTO;
import com.kbph.logistics.common.util.SecurityUtils;
import com.kbph.logistics.configuration.error.InsertCheckedException;
import com.kbph.logistics.configuration.error.NoSaveDataException;
import com.kbph.logistics.configuration.error.UpdateCheckedException;
import com.kbph.logistics.md.domain.MaremaDTO;
import com.kbph.logistics.md.domain.McodemDTO;
import com.kbph.logistics.md.domain.McusmaDTO;
import com.kbph.logistics.md.domain.MdesmaDTO;
import com.kbph.logistics.md.domain.MeqlocDTO;
import com.kbph.logistics.md.domain.MequmaDTO;
import com.kbph.logistics.md.domain.MgrpmaDTO;
import com.kbph.logistics.md.domain.MlocmaDTO;
import com.kbph.logistics.md.domain.MregmaDTO;
import com.kbph.logistics.md.domain.MwarmaDTO;
import com.kbph.logistics.md.mapper.CodeMapper;
import com.kbph.logistics.md.mapper.OrganizationMapper;
import com.kbph.logistics.md.mapper.PartnerMapper;
import com.kbph.logistics.md.mapper.UnitsMapper;
import com.kbph.logistics.sy.domain.UserVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrganizationService {
	private final OrganizationMapper organizationMapper;
	private final UnitsMapper unitsMapper;
	private final PartnerMapper partnerMapper;
	private final BillingMasterMapper billingMasterMapper;
	private final CodeMapper codeMapper;
	private final ApiConfig apiConfig;

	/* ************************ DOE001 : 창고등록 ************************ */
	// 창고 리스트 조회
	public List<MwarmaDTO> getMwarmaList(MwarmaDTO params) {
		return organizationMapper.selectMwarmaList(params, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	// 창고 데이터 저장
	public int saveDoe001(GridDTO<MwarmaDTO> saveList) {
		if (saveList == null) {
			throw new NoSaveDataException();
		}

		UserVO userData = SecurityUtils.getCustomUserDetails().getUserInfo();

		int insertCnt = 0;
		if(CollectionUtils.isNotEmpty(saveList.getAddList())) {
			for (MwarmaDTO addData : saveList.getAddList()) {
				addData.setWarekey(organizationMapper.selectWarekey(userData));
				if (organizationMapper.insertMwarma(addData, userData) == 0) {
					throw new InsertCheckedException();
				}
				insertCnt++;
			}
		}

		int updateCnt = 0;
		if(CollectionUtils.isNotEmpty(saveList.getUpdateList()) && CollectionUtils.isNotEmpty(saveList.getOldList())) {
			for (int i = 0; i < saveList.getUpdateList().size(); i++) {
				saveList.getUpdateList().get(i).setOldWarekey(saveList.getOldList().get(i).getWarekey());
				if (organizationMapper.updateMwarma(saveList.getUpdateList().get(i), userData) == 0) {
					throw new UpdateCheckedException();
				}
				updateCnt++;
			}
		}

		return insertCnt + updateCnt;
	}

	// 창고 셀렉트박스 조회
	public List<ComboDataDTO> getWarehouseSelectBox(MwarmaDTO params) {
		return organizationMapper.selectMwarmaSelectBox(params, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	// doe001 초기화
	/*public InitDataDTO getDoe001InitData() {
		Map<String, Object> item = new HashMap<>();
		item.put("conPrinterList", customPrintService.findPrinter());
		item.put("myPrinterList", organizationMapper.selectWarehousePrinterList(null, SecurityUtils.getCustomUserDetails().getUserInfo()));
		InitDataDTO initDataDTO = new InitDataDTO();
		initDataDTO.setItem(item);
		return initDataDTO;
	}*/

	/* ************************ DOE002 : 창고동(창고동)등록 ************************ */
	// 창고동 리스트 조회
	public List<MaremaDTO> getMaremaList(MaremaDTO params) {
		UserVO userInfo = SecurityUtils.getCustomUserDetails().getUserInfo();
		return organizationMapper.selectMaremaList(params, userInfo);
	}

	// 창고동 데이터 저장
	public int saveDoe002(GridDTO<MaremaDTO> saveList) {
		if (saveList == null) {
			throw new NoSaveDataException();
		}

		UserVO userData = SecurityUtils.getCustomUserDetails().getUserInfo();

		int insertCnt = 0;
		if (CollectionUtils.isNotEmpty(saveList.getAddList())) {
			for (MaremaDTO addData : saveList.getAddList()) {
				if (organizationMapper.insertMarema(addData, userData) == 0) {
					throw new InsertCheckedException();
				}
				insertCnt++;
			}
			if(insertCnt > 0) {
				apiConfig.requestToDC(saveList.getAddList(), ApiInfo.API_MD_MAREMA, HttpMethod.POST);
			}
		}

		int updateCnt = 0;
		if (CollectionUtils.isNotEmpty(saveList.getUpdateList()) && CollectionUtils.isNotEmpty(saveList.getOldList())) {
			for (int i = 0; i < saveList.getUpdateList().size(); i++) {
				saveList.getUpdateList().get(i).setOldWarekey(saveList.getOldList().get(i).getWarekey());
				saveList.getUpdateList().get(i).setOldAreakey(saveList.getOldList().get(i).getAreakey());
				if (organizationMapper.updateMarema(saveList.getUpdateList().get(i), userData) == 0) {
					throw new UpdateCheckedException();
				}
				updateCnt++;
			}
			if(updateCnt > 0) {
				apiConfig.requestToDC(saveList.getUpdateList(), ApiInfo.API_MD_MAREMA, HttpMethod.PATCH);
			}
		}

		return insertCnt + updateCnt;
	}

	// 창고동 셀렉트박스 조회
	public List<ComboDataDTO> getAreaSelectBox(MaremaDTO params) {
		return organizationMapper.selectMaremaSelectBox(params, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	/* ************************ DOE003 : 베드등록 ************************ */
	// 베드 리스트 조회
	public List<MlocmaDTO> selectMlocmaList(MlocmaDTO params) {
		return organizationMapper.selectMlocmaList(params, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	// 베드 테이터 저장
	public int saveDoe003(GridDTO<MlocmaDTO> saveList) {
		if (saveList == null) {
			throw new NoSaveDataException();
		}

		UserVO userData = SecurityUtils.getCustomUserDetails().getUserInfo();

		int insertCnt = 0;
		if (CollectionUtils.isNotEmpty(saveList.getAddList())) {
			for (MlocmaDTO addData : saveList.getAddList()) {
				if (organizationMapper.insertMlocma(addData, userData) == 0) {
  					throw new InsertCheckedException();
				}
				insertCnt++;
				addData.setEquipkyList(organizationMapper.selectEquipkyByLoc(addData, userData));			}
			if(insertCnt > 0) {
				apiConfig.requestToDC(saveList.getAddList(), ApiInfo.API_MD_MLOCMA, HttpMethod.POST);
			}
		}

		int updateCnt = 0;
		if (CollectionUtils.isNotEmpty(saveList.getUpdateList()) && CollectionUtils.isNotEmpty(saveList.getOldList())) {
			for (int i = 0; i < saveList.getUpdateList().size(); i++) {
				MlocmaDTO updateData = saveList.getUpdateList().get(i);
				updateData.setOldWarekey(saveList.getOldList().get(i).getWarekey());
				updateData.setOldAreakey(saveList.getOldList().get(i).getAreakey());
				updateData.setOldLocakey(saveList.getOldList().get(i).getLocakey());

				// 우선순위 NN 처리
				updateData.setSizepri(updateData.getSizepri() == null ? " " : updateData.getSizepri());
				updateData.setBtrcpri(updateData.getBtrcpri() == null ? " " : updateData.getBtrcpri());
				updateData.setItdtpri(updateData.getItdtpri() == null ? " " : updateData.getItdtpri());
				updateData.setCustpri(updateData.getCustpri() == null ? " " : updateData.getCustpri());
				updateData.setRegipri(updateData.getRegipri() == null ? " " : updateData.getRegipri());
				updateData.setDestpri(updateData.getDestpri() == null ? " " : updateData.getDestpri());

				if (organizationMapper.updateMlocma(updateData, userData) == 0) {
					throw new UpdateCheckedException();
				}
				 //사용여부가 N으로 변경된 경우 매핑도 N 처리
				if("N".equals(updateData.getLouseyn())) {
					organizationMapper.updateMeqlocUnuse(updateData.getLocakey(), userData);
				}
				updateData.setEquipkyList(organizationMapper.selectEquipkyByLoc(updateData, userData));
				updateCnt++;
			}
			if(updateCnt > 0) {
				apiConfig.requestToDC(saveList.getUpdateList(), ApiInfo.API_MD_MLOCMA, HttpMethod.PATCH);
			}
		}

		return insertCnt + updateCnt;
	}

	// doe003 초기화
	public InitDataDTO getDoe003InitData() {
		Map<String, Object> item = new HashMap<>();
		UserVO userData = SecurityUtils.getCustomUserDetails().getUserInfo();

		MgrpmaDTO mgrpmaDTO = new MgrpmaDTO();
		McodemDTO mcodemDTO = new McodemDTO();
		mcodemDTO.setComcdky("ITEMCOD");
		BtrcatDTO btrcatDTO = new BtrcatDTO();
		McusmaDTO mcusmaDTO = new McusmaDTO();
		MregmaDTO mregmaDTO = new MregmaDTO();
		MdesmaDTO mdesmaDTO = new MdesmaDTO();

		item.put("sizepriList", unitsMapper.selectMgrpmaSelectBox(mgrpmaDTO, userData)); // 사이즈 우선순위
		item.put("itdtpriList", codeMapper.selectMcodem(mcodemDTO, userData)); // 품목코드 우선순위
		item.put("btrcpriList", billingMasterMapper.selectBtrcatList(btrcatDTO, userData.getSchema())); // 정산유형 우선순위
		item.put("custpriList", partnerMapper.selectMcusmaSelectBox(mcusmaDTO, userData)); // 고객사 우선순위
		item.put("regipriList", partnerMapper.selectMregmaSelectBox(mregmaDTO, userData)); // 목적지 우선순위
		item.put("destpriList", partnerMapper.selectMdesmaSelectBox(mdesmaDTO, userData)); // 상세착지 우선순위

		InitDataDTO initDataDTO = new InitDataDTO();
		initDataDTO.setItem(item);
		return initDataDTO;
	}

	// 베드 셀렉트박스 조회
	public List<ComboDataDTO> getLocationSelectBox(MlocmaDTO params) {
		return organizationMapper.selectMlocmaSelectBox(params, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	// Location 관계 리스트 조회 (창고, 창고동)
	public List<MlocmaDTO> getWareAreaRelations(MlocmaDTO params) {
		return organizationMapper.selectWareAreaRelations(params, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	// Location 관계 리스트 조회 (창고, 창고동, 베드)
	public List<MlocmaDTO> getWareAreaLocRelations(MlocmaDTO params) {
		return organizationMapper.selectWareAreaLocRelations(params, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	/* ************************ DOE004 : 설비등록 ************************ */
	// 설비 리스트 조회
	public List<MequmaDTO> getMequmaList(MequmaDTO params) {
		return organizationMapper.selectMequemaList(params, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	// 설비 데이터 저장
	public int saveDoe004(GridDTO<MequmaDTO> saveList) {
		if (saveList == null) {
			throw new NoSaveDataException();
		}

		UserVO userData = SecurityUtils.getCustomUserDetails().getUserInfo();

		int insertCnt = 0;
		if (CollectionUtils.isNotEmpty(saveList.getAddList())) {
			for (MequmaDTO addData : saveList.getAddList()) {
				addData.setEqupurd(addData.getEqupurd() == null ? "" : addData.getEqupurd().replace("-", ""));
				if (organizationMapper.insertMequma(addData, userData) == 0) {
					throw new InsertCheckedException();
				}
				insertCnt++;
			}
			if(insertCnt > 0) {
				apiConfig.requestToDC(saveList.getAddList(), ApiInfo.API_MD_MEQUMA, HttpMethod.POST);
			}
		}

		int updateCnt = 0;
		if (CollectionUtils.isNotEmpty(saveList.getUpdateList()) && CollectionUtils.isNotEmpty(saveList.getOldList())) {
			for (int i = 0; i < saveList.getUpdateList().size(); i++) {
				MequmaDTO saveData = saveList.getUpdateList().get(i);
				saveData.setOldWarekey(saveList.getOldList().get(i).getWarekey());
				saveData.setOldAreakey(saveList.getOldList().get(i).getAreakey());
				saveData.setOldEquipky(saveList.getOldList().get(i).getEquipky());
				saveData.setEqupurd(saveData.getEqupurd() == null ? "" : saveData.getEqupurd().replace("-", ""));
				if (organizationMapper.updateMequma(saveData, userData) == 0) {
					throw new UpdateCheckedException();
				}
				updateCnt++;
			}
			if(updateCnt > 0) {
				apiConfig.requestToDC(saveList.getUpdateList(), ApiInfo.API_MD_MEQUMA, HttpMethod.PATCH);
			}
		}
		return insertCnt + updateCnt;
	}

	// doe003 초기화
	public InitDataDTO getDoe004InitData() {
		Map<String, Object> item = new HashMap<>();
		UserVO userData = SecurityUtils.getCustomUserDetails().getUserInfo();

		MlocmaDTO mlocmaDTO = new MlocmaDTO();
		item.put("areaRelList", organizationMapper.selectWareAreaRelations(mlocmaDTO, userData)); // 창고동 관계리스트

		InitDataDTO initDataDTO = new InitDataDTO();
		initDataDTO.setItem(item);
		return initDataDTO;
	}

	// 설비 셀렉트박스 조회
	public List<ComboDataDTO> getEquipmentSelectBox(MequmaDTO params) {
		return organizationMapper.selectMequmaSelectBox(params, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	// 설비 관계 리스트 조회 (창고-설비)
	public List<MequmaDTO> getWareAreaEquipRelations(MequmaDTO params) {
		return organizationMapper.selectWareAreaEquipRelations(params, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	/* ************************ DOE005 : 설비-베드 매핑 ************************ */
	// 설비-베드 매핑 조회
	public List<MlocmaDTO> getMappingList(MlocmaDTO params) {
		return organizationMapper.selectMlocmaForMapping(params, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	// 설비-베드 매핑 데이터 저장
	public int saveDoe005(GridDTO<MeqlocDTO> saveList) {
		if (saveList == null) {
			throw new NoSaveDataException();
		}

		UserVO userData = SecurityUtils.getCustomUserDetails().getUserInfo();
		int saveCnt = 0;
		int updateCnt = 0;
		for (MeqlocDTO saveData : saveList.getAddList()) {
			if (organizationMapper.selectMeqlocCnt(saveData, userData) == 0) {
				if(saveData.getManeqyn() == null) {
					saveData.setManeqyn("N");
				}
				if (organizationMapper.insertMeqloc(saveData, userData) == 0) {
					throw new InsertCheckedException();
				}
				saveCnt++;
			} else {
				if (organizationMapper.updateMeqloc(saveData, userData) == 0) {
					throw new UpdateCheckedException();
				}
				updateCnt++;
			}
			saveData.setEquipkyList(organizationMapper.selectEquipkyByLoc(saveData, userData));
		}
		if(saveCnt > 0 || updateCnt > 0) {
			apiConfig.requestToDC(saveList.getAddList(), ApiInfo.API_MD_MLOCMA, HttpMethod.PATCH);
		}
		return saveCnt;
	}

	// doe005 초기화
	public InitDataDTO getDoe005InitData() {
		Map<String, Object> item = new HashMap<>();
		UserVO userData = SecurityUtils.getCustomUserDetails().getUserInfo();

		McodemDTO mcodemDTO = new McodemDTO();
		mcodemDTO.setComcdky("EQUTYPE");
		item.put("equtypeList", codeMapper.selectMcodem(mcodemDTO, userData));

		mcodemDTO = new McodemDTO();
		mcodemDTO.setComcdky("EQUSTAT");
		item.put("equstatList", codeMapper.selectMcodem(mcodemDTO, userData));

		mcodemDTO = new McodemDTO();
		mcodemDTO.setComcdky("LOCTYPE");
		item.put("loctypeList", codeMapper.selectMcodem(mcodemDTO, userData));

		mcodemDTO = new McodemDTO();
		mcodemDTO.setComcdky("LOCSTAT");
		item.put("locstatList", codeMapper.selectMcodem(mcodemDTO, userData));

		InitDataDTO initDataDTO = new InitDataDTO();
		initDataDTO.setItem(item);

		return initDataDTO;
	}

	// 창고 - 창고동 관계 리스트 조회
	public List<MaremaDTO> getAreaRelationList(MaremaDTO params) {
		return organizationMapper.selectAreaRelationList(params, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	// 창고 - 창고동 - 베드 관계 리스트 조회
	public List<MlocmaDTO> getLocationRelationList(MlocmaDTO params) {
		return organizationMapper.selectLocationRelationList(params, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	// 설비 - 창고 - 창고동 - 베드 관계 리스트 조회
	public List<MeqlocDTO> getEquipByLocForSelectBox(MlocmaDTO mlocmaDTO){
		return organizationMapper.selectEquipByLocForSelectBox(mlocmaDTO, SecurityUtils.getCustomUserDetails().getUserInfo());
	}
}
