package com.kbph.logistics.md.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import com.kbph.logistics.common.domain.GridDTO;
import com.kbph.logistics.common.domain.InitDataDTO;
import com.kbph.logistics.common.util.SecurityUtils;
import com.kbph.logistics.configuration.error.InsertCheckedException;
import com.kbph.logistics.configuration.error.NoSaveDataException;
import com.kbph.logistics.configuration.error.UpdateCheckedException;
import com.kbph.logistics.md.domain.McodemDTO;
import com.kbph.logistics.md.domain.MdocmaDTO;
import com.kbph.logistics.md.domain.MrscmaDTO;
import com.kbph.logistics.md.mapper.CodeMapper;
import com.kbph.logistics.md.mapper.DocumentMapper;
import com.kbph.logistics.sy.domain.UserVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CodeService {
	private final CodeMapper codeMapper;
	private final DocumentMapper documentMapper;

	/* ************************ DOE014 : 공통코드 등록 ************************ */
	// 공통코드 리스트 조회
	public List<McodemDTO> getMcodemList(McodemDTO params) {
		return codeMapper.selectMcodemList(params, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	// 공통코드 조회 (키 파라미터)
	public List<McodemDTO> getMcodemListByKey(McodemDTO params) {
		return codeMapper.selectMcodem(params, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	// 공통코드 데이터 저장
	public int saveDoe014(GridDTO<McodemDTO> saveList) {
		if (saveList == null) {
			throw new NoSaveDataException();
		}

		UserVO userData = SecurityUtils.getCustomUserDetails().getUserInfo();

		int insertCnt = 0;
		if(CollectionUtils.isNotEmpty(saveList.getAddList())) {
			for (McodemDTO addData : saveList.getAddList()) {
				if (codeMapper.insertMcodem(addData, userData) == 0) {
					throw new InsertCheckedException();
				}
				insertCnt++;
			}
		}

		int updateCnt = 0;
		if(CollectionUtils.isNotEmpty(saveList.getUpdateList()) && CollectionUtils.isNotEmpty(saveList.getOldList())) {
			for (int i = 0; i < saveList.getUpdateList().size(); i++) {
				saveList.getUpdateList().get(i).setOldComcdky(saveList.getOldList().get(i).getComcdky());
				saveList.getUpdateList().get(i).setOldComcdvl(saveList.getOldList().get(i).getComcdvl());
				if (codeMapper.updateMcodem(saveList.getUpdateList().get(i), userData) == 0) {
					throw new UpdateCheckedException();
				}
				updateCnt++;
			}
		}
		return insertCnt + updateCnt;
	}

	// 공통코드 셀렉트박스 조회
	public List<McodemDTO> getCommonCodeSelectBox(McodemDTO params) {
		if (params.getComcdky() != null) {
			params.setComcdkys(Arrays.asList(params.getComcdky().split(",")));
			params.setComcdky(null);
		}
		if (params.getComcdvl() != null) {
			params.setComcdvls(Arrays.asList(params.getComcdvl().split(",")));
			params.setComcdvl(null);
		}
		// 더 추가될 수 있음
		return codeMapper.selectMcodemSelectBox(params, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	/* ************************ DOE015 : 사유코드 등록 ************************ */
	// 사유코드 리스트 조회
	public List<MrscmaDTO> getMrscmaList(MrscmaDTO params) {
		return codeMapper.selectMrscmaList(params, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	// 사유코드 데이터 저장
	public int saveDoe015(GridDTO<MrscmaDTO> saveList) {
		if (saveList == null) {
			throw new NoSaveDataException();
		}

		UserVO userData = SecurityUtils.getCustomUserDetails().getUserInfo();

		int insertCnt = 0;
		if(CollectionUtils.isNotEmpty(saveList.getAddList())) {
			for (MrscmaDTO addData : saveList.getAddList()) {
				if (codeMapper.insertMrscma(addData, userData) == 0) {
					throw new InsertCheckedException();
				}
				insertCnt++;
			}
		}

		int updateCnt = 0;
		if(CollectionUtils.isNotEmpty(saveList.getUpdateList()) && CollectionUtils.isNotEmpty(saveList.getOldList())) {
			for (int i = 0; i < saveList.getUpdateList().size(); i++) {
				saveList.getUpdateList().get(i).setOldWarekey(saveList.getOldList().get(i).getWarekey());
				saveList.getUpdateList().get(i).setOldDoccate(saveList.getOldList().get(i).getDoccate());
				saveList.getUpdateList().get(i).setOldDoctype(saveList.getOldList().get(i).getDoctype());
				saveList.getUpdateList().get(i).setOldRsncode(saveList.getOldList().get(i).getRsncode());
				if (codeMapper.updateMrscma(saveList.getUpdateList().get(i), userData) == 0) {
					throw new UpdateCheckedException();
				}
				updateCnt++;
			}
		}
		return insertCnt + updateCnt;
	}

	// 사유코드 셀렉트박스 조회
	public List<MrscmaDTO> getReasonCodeSelectBox(MrscmaDTO params) {
		return codeMapper.selectMrscmaSelectBox(params, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	// doe015 초기화
	public InitDataDTO getDoe015InitData(MdocmaDTO params) {
		Map<String, Object> item = new HashMap<>();
		UserVO userData = SecurityUtils.getCustomUserDetails().getUserInfo();

		item.put("doccate", documentMapper.selectMdocmaDoccateList(params, userData));
		item.put("doctype", documentMapper.selectWareCateTypeRelations(params, userData));

		InitDataDTO initDataDTO = new InitDataDTO();
		initDataDTO.setItem(item);
		return initDataDTO;
	}

	public List<MrscmaDTO> getReasonCodeRelationList(MrscmaDTO params) {
		return codeMapper.selectMrscmaRelationList(params, SecurityUtils.getCustomUserDetails().getUserInfo());
	}
}

