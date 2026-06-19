package com.kbph.logistics.md.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import com.kbph.logistics.api.config.ApiConfig;
import com.kbph.logistics.api.enums.ApiInfo;
import com.kbph.logistics.common.domain.ComboDataDTO;
import com.kbph.logistics.common.domain.GridDTO;
import com.kbph.logistics.common.domain.InitDataDTO;
import com.kbph.logistics.common.util.SecurityUtils;
import com.kbph.logistics.configuration.error.InsertCheckedException;
import com.kbph.logistics.configuration.error.NoSaveDataException;
import com.kbph.logistics.configuration.error.UpdateCheckedException;
import com.kbph.logistics.md.domain.MgrpmaDTO;
import com.kbph.logistics.md.domain.MskuwcDTO;
import com.kbph.logistics.md.domain.MuommaDTO;
import com.kbph.logistics.md.mapper.UnitsMapper;
import com.kbph.logistics.sy.domain.UserVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UnitsService {
	private final UnitsMapper unitsMapper;
	private final ApiConfig apiConfig;

	/* ************************ DOE011 : 제품 등록 ************************ */
	// 제품 리스트 조회
	public List<MskuwcDTO> getMskuwcList(MskuwcDTO params) {
		return unitsMapper.selectMskuwcList(params, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	// 제품 데이터 저장
	public int saveDoe011(GridDTO<MskuwcDTO> saveList) {
		if (saveList == null) {
			throw new NoSaveDataException();
		}

		UserVO userData = SecurityUtils.getCustomUserDetails().getUserInfo();

		int insertCnt = 0;
		if (CollectionUtils.isNotEmpty(saveList.getAddList())) {
			MskuwcDTO mskuwc = new MskuwcDTO();
			List<String> skumkeyList = new ArrayList<>();
			for (MskuwcDTO addData : saveList.getAddList()) {
				if (unitsMapper.insertMskuwc(addData, userData) == 0) {
					throw new InsertCheckedException();
				}
				insertCnt++;

				skumkeyList.add(addData.getSkumkey());
			}
			if(insertCnt > 0 && !skumkeyList.isEmpty()) {
				mskuwc.setSkumkeyList(skumkeyList);
				apiConfig.requestToDC(unitsMapper.selectMskuwcList(mskuwc, userData), ApiInfo.API_MD_MSKUWC, HttpMethod.POST);
			}
		}

		int updateCnt = 0;
		if(CollectionUtils.isNotEmpty(saveList.getUpdateList()) && CollectionUtils.isNotEmpty(saveList.getOldList())) {
			MskuwcDTO mskuwc = new MskuwcDTO();
			List<String> skumkeyList = new ArrayList<>();
			for(int i=0; i<saveList.getUpdateList().size(); i++) {
				saveList.getUpdateList().get(i).setOldWarekey(saveList.getOldList().get(i).getWarekey());
				saveList.getUpdateList().get(i).setOldOwnerky(saveList.getOldList().get(i).getOwnerky());
				saveList.getUpdateList().get(i).setOldSkumkey(saveList.getOldList().get(i).getSkumkey());
				saveList.getUpdateList().get(i).setOldSkugrky(saveList.getOldList().get(i).getSkugrky());
				if (unitsMapper.updateMskuwc(saveList.getUpdateList().get(i), userData) == 0) {
					throw new UpdateCheckedException();
				}
				updateCnt++;
				skumkeyList.add(saveList.getUpdateList().get(i).getSkumkey());

			}
			if(updateCnt > 0 && !skumkeyList.isEmpty()) {
				mskuwc.setSkumkeyList(skumkeyList);
				apiConfig.requestToDC(saveList.getUpdateList(), ApiInfo.API_MD_MSKUWC, HttpMethod.PATCH);
			}
		}
		return insertCnt + updateCnt;
	}

	// 제품 셀렉트박스 조회
	public List<ComboDataDTO> getSkuSelectBox(MskuwcDTO params) {
		return unitsMapper.selectMskuwcSelectBox(params, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	// doe011 초기화
	public InitDataDTO getDoe011InitData() {
		Map<String, Object> item = new HashMap<>();
		UserVO userData = SecurityUtils.getCustomUserDetails().getUserInfo();

		MuommaDTO muommaDTO = new MuommaDTO();
		item.put("suomkeyList", unitsMapper.selectMuommaSelectBox(muommaDTO, userData));

		InitDataDTO initDataDTO = new InitDataDTO();
		initDataDTO.setItem(item);
		return initDataDTO;
	}

	/* ************************ DOE012 : 제품그룹정보 등록 ************************ */
	// 제품그룹 리스트 조회
	public List<MgrpmaDTO> getMgrpmaList(MgrpmaDTO params) {
		return unitsMapper.selectMgrpmaList(params, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	// 제품그룹 데이터 저장
	public int saveDoe012(GridDTO<MgrpmaDTO> saveList) {
		if (saveList == null) {
			throw new NoSaveDataException();
		}

		UserVO userData = SecurityUtils.getCustomUserDetails().getUserInfo();

		int insertCnt = 0;
		if (CollectionUtils.isNotEmpty(saveList.getAddList())) {
			for (MgrpmaDTO addData : saveList.getAddList()) {
				addData.setSkugrky(unitsMapper.selectSkugrky(userData));
				if (unitsMapper.insertMgrpma(addData, userData) == 0) {
					throw new InsertCheckedException();
				}
				insertCnt++;
			}
		}
		int updateCnt = 0;
		if (CollectionUtils.isNotEmpty(saveList.getUpdateList()) && CollectionUtils.isNotEmpty(saveList.getOldList())) {
			for (int i = 0; i < saveList.getUpdateList().size(); i++) {
				saveList.getUpdateList().get(i).setOldSkugrky(saveList.getOldList().get(i).getSkugrky());
				if (unitsMapper.updateMgrpma(saveList.getUpdateList().get(i), userData) == 0) {
					throw new UpdateCheckedException();
				}
				updateCnt++;
			}
		}
		return insertCnt + updateCnt;
	}

	// 제품 셀렉트박스 조회
	public List<ComboDataDTO> getSkuGroupSelectBox(MgrpmaDTO params) {
		return unitsMapper.selectMgrpmaSelectBox(params, SecurityUtils.getCustomUserDetails().getUserInfo());
	}
}
