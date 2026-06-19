package com.kbph.logistics.md.service;

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
import com.kbph.logistics.common.enums.SubDomain;
import com.kbph.logistics.common.enums.Useyn;
import com.kbph.logistics.common.util.SecurityUtils;
import com.kbph.logistics.configuration.error.InsertCheckedException;
import com.kbph.logistics.configuration.error.NoSaveDataException;
import com.kbph.logistics.configuration.error.UpdateCheckedException;
import com.kbph.logistics.md.domain.McodemDTO;
import com.kbph.logistics.md.domain.McusmaDTO;
import com.kbph.logistics.md.domain.MdesmaDTO;
import com.kbph.logistics.md.domain.MowrmaDTO;
import com.kbph.logistics.md.domain.MregmaDTO;
import com.kbph.logistics.md.domain.MrscmaDTO;
import com.kbph.logistics.md.domain.MvhcmaDTO;
import com.kbph.logistics.md.domain.MvowmaDTO;
import com.kbph.logistics.md.mapper.CodeMapper;
import com.kbph.logistics.md.mapper.PartnerMapper;
import com.kbph.logistics.sy.domain.UserVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PartnerService {
	private final PartnerMapper partnerMapper;
	private final CodeMapper codeMapper;
	private final ApiConfig apiConfig;

	/* ************************ DOE006 : 차주 등록 ************************ */
	// 차주 리스트 조회
	public List<MvowmaDTO> getMvowmaList(MvowmaDTO params) {
		return partnerMapper.selectMvowmaList(params, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	// 차주 데이터 저장
	public int saveDoe006(GridDTO<MvowmaDTO> saveList, String subDomain) {
		if (saveList == null) {
			throw new NoSaveDataException();
		}

		UserVO userData = SecurityUtils.getCustomUserDetails().getUserInfo();

		int insertCnt = 0;
		if (CollectionUtils.isNotEmpty(saveList.getAddList())) {
			for (MvowmaDTO addData : saveList.getAddList()) {
				addData.setVownkey(partnerMapper.selectVownkey(userData)); // 차주키 채번
				addData.setDmwplnm(addData.getPublcyn().equals(Useyn.UNUSE.getString()) ? subDomain : "");
				if (partnerMapper.insertMvowma(addData, userData) == 0) {
					throw new InsertCheckedException("차주등록 실패");
				}
				insertCnt++;
			}
		}

		int updateCnt = 0;
		if (CollectionUtils.isNotEmpty(saveList.getUpdateList()) && CollectionUtils.isNotEmpty(saveList.getOldList())) {
			for (int i = 0; i < saveList.getUpdateList().size(); i++) {
				MvowmaDTO updateData = saveList.getUpdateList().get(i);
				updateData.setOldVownkey(saveList.getOldList().get(i).getVownkey());
				updateData.setDmwplnm(updateData.getPublcyn().equals(Useyn.UNUSE.getString()) ? subDomain : "");
				if (partnerMapper.updateMvowma(updateData, userData) == 0) {
					throw new UpdateCheckedException("차주업데이트 실패");
				}
				updateCnt++;
			}
		}
		return insertCnt + updateCnt;
	}

	// 차주 셀렉트박스 조회
	public List<ComboDataDTO> getVehicleOwnerSelectBox(MvowmaDTO params) {
		return partnerMapper.selectMvowmaSelectBox(params, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	/* ************************ DOE007 : 차량 등록 ************************ */
	// 차량 리스트 조회
	public List<MvhcmaDTO> getMvhcmaList(MvhcmaDTO params) {
		return partnerMapper.selectMvhcmaList(params, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	// 차량 데이터 저장
	public int saveDoe007(GridDTO<MvhcmaDTO> saveList, String subDomain) {
		if (saveList == null) {
			throw new NoSaveDataException();
		}

		// 로컬일시 subDomain 값 처리
		if(subDomain == null) {
			String targetSchema = SecurityUtils.getSchema();
			if(targetSchema != null) {
				subDomain = SecurityUtils.getSchema().replace("_", "-").toLowerCase();
			} else {
				subDomain = "";
			}
		}

		UserVO userData = SecurityUtils.getCustomUserDetails().getUserInfo();

		int insertCnt = 0;
		if (CollectionUtils.isNotEmpty(saveList.getAddList())) {
			for (MvhcmaDTO addData : saveList.getAddList()) {

				// 차량번호가 중복되는지 확인
				if(partnerMapper.selectVhcfnamCnt(addData, userData) > 0) {
					// 중복된다면 차량, 차주 모두 공용여부 Y로 변경
					MvhcmaDTO selectMvhcma = partnerMapper.selectOneMvhcma(addData, userData);
					addData.setVownkey(selectMvhcma.getVownkey());
					addData.setVehicky(selectMvhcma.getVehicky());
					addData.setVhcfnam(selectMvhcma.getVhcfnam());
					addData.setVhctype(selectMvhcma.getVhctype());
					addData.setVhctncd(selectMvhcma.getVhctncd());
					addData.setVhcopty(selectMvhcma.getVhcopty());
					addData.setDrvernm(selectMvhcma.getDrvernm());
					addData.setDrverph(selectMvhcma.getDrverph());
					addData.setPoscoyn(selectMvhcma.getPoscoyn());
					addData.setVhcvryn(selectMvhcma.getVhcvryn());
					addData.setVhuseyn(selectMvhcma.getVhuseyn());
					addData.setVonamlc(selectMvhcma.getVonamlc());
					addData.setUpdtchk(selectMvhcma.getUpdtchk());
					addData.setVoudtck(selectMvhcma.getVoudtck());
					addData.setPublcyn(Useyn.USE.getString());
					addData.setDmwplnm("");

					saveVehiclePublicSign(addData);
					saveOwnerPublicSign(addData);

					insertCnt++;
					continue;
				}

				addData.setVehicky(partnerMapper.selectVehicky(userData)); // 차량키 채번
				addData.setDmwplnm(addData.getPublcyn().equals(Useyn.UNUSE.getString()) ? subDomain : "");
				addData.setVhcvryn(subDomain.equals(SubDomain.DO_DR.getString()) ? Useyn.USE.getString() : addData.getVhcvryn());

				// 차량 최대중량을 디폴트(차량톤수)로 set
				if(addData.getVhcmaxw() == 0) {
					int maxWeight = 0;
					if(subDomain.equals(SubDomain.DO_DR.getString())) {
						maxWeight = 30; // 두루 최대톤수 30으로 set 요청
					} else {
						McodemDTO mcodemDTO = new McodemDTO();
						mcodemDTO.setComcdky("VHCTNCD");
						mcodemDTO.setComcdvl(addData.getVhctncd());
						maxWeight = Integer.parseInt(codeMapper.selectCode(mcodemDTO, userData).getCdattr1()) / 1000;
					}
					addData.setVhcmaxw(maxWeight);
				}

				if (partnerMapper.insertMvhcma(addData, userData) == 0) {
					throw new InsertCheckedException("차량등록 실패");
				}
				insertCnt++;

				if(addData.getPublcyn().equals(Useyn.USE.getString())) {
					saveOwnerPublicSign(addData);
				}
			}
			if(insertCnt > 0) {
				apiConfig.requestToDC(saveList.getAddList(), ApiInfo.API_MD_MVHCMA, HttpMethod.POST);
			}
		}

		int updateCnt = 0;
		if (CollectionUtils.isNotEmpty(saveList.getUpdateList()) && CollectionUtils.isNotEmpty(saveList.getOldList())) {
			for (int i = 0; i < saveList.getUpdateList().size(); i++) {
				MvhcmaDTO updateData = saveList.getUpdateList().get(i);
				updateData.setOldVownkey(saveList.getOldList().get(i).getVownkey());
				updateData.setOldVehicky(saveList.getOldList().get(i).getVehicky());
				updateData.setDmwplnm(updateData.getPublcyn().equals(Useyn.UNUSE.getString()) ? subDomain : "");

				if (partnerMapper.updateMvhcma(saveList.getUpdateList().get(i), userData) == 0) {
					throw new UpdateCheckedException("차량 업데이트 실패");
				}
				updateCnt++;

				if(updateData.getPublcyn().equals(Useyn.USE.getString())) {
					saveOwnerPublicSign(updateData);
				}

			}
			if(updateCnt > 0) {
				apiConfig.requestToDC(saveList.getUpdateList(), ApiInfo.API_MD_MVHCMA, HttpMethod.PATCH);
			}
		}
		return insertCnt + updateCnt;
	}

	// 차량 공용여부 플래그 업데이트 (차량번호가 중복되는 경우)
	public boolean saveVehiclePublicSign(MvhcmaDTO saveVehicle) {
		UserVO userData = SecurityUtils.getCustomUserDetails().getUserInfo();
		if(partnerMapper.updateMvhcmaPublicStat(saveVehicle, userData) == 0) {
			throw new UpdateCheckedException("차량 공용처리에 실패했습니다.");
		}
		return true;
	}

	// 차주 공용여부 플래그 업데이트 (차량 공용여부가 Y가 되는 경우 또는 차량번호가 중복되는 경우)
	public boolean saveOwnerPublicSign(MvhcmaDTO saveVehicle) {
		MvowmaDTO owner = new MvowmaDTO();
		UserVO userData = SecurityUtils.getCustomUserDetails().getUserInfo();
		owner.setVownkey(saveVehicle.getVownkey());
		owner.setUpdtchk(saveVehicle.getVoudtck());
		owner.setPublcyn(saveVehicle.getPublcyn());
		owner.setDmwplnm("");
		if(partnerMapper.updateMvowmaPublicStat(owner, userData) == 0) {
			throw new UpdateCheckedException("차주 공용처리에 실패했습니다.");
		}
		return true;
	}

	// doe007 초기화
	public InitDataDTO getDoe007InitData(Map<String, Object> item) {
		UserVO userData = SecurityUtils.getCustomUserDetails().getUserInfo();
		MvowmaDTO mvowmaDTO = new MvowmaDTO();
		item.put("vownkeyList", partnerMapper.selectMvowmaSelectBox(mvowmaDTO, userData));
		InitDataDTO initDataDTO = new InitDataDTO();
		initDataDTO.setItem(item);
		return initDataDTO;
	}

	// 차량 셀렉트박스 조회
	public List<ComboDataDTO> getVehicleSelectBox(MvhcmaDTO params) {
		return partnerMapper.selectMvhcmaSelectBox(params, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	// 차주-차량 관계리스트 조회
	public List<MrscmaDTO> getVehicleRelationList(MvhcmaDTO params) {
		return partnerMapper.selectVehicleRelationList(params, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	/* ************************ DOE008 : 목적지 등록 ************************ */
	// 목적지 리스트 조회
	public List<MregmaDTO> getMregmaList(MregmaDTO params) {
		return partnerMapper.selectMregmaList(params, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	// 목적지 데이터 저장
	public int saveDoe008(GridDTO<MregmaDTO> saveList) {
		if (saveList == null) {
			throw new NoSaveDataException();
		}

		UserVO userData = SecurityUtils.getCustomUserDetails().getUserInfo();

		int insertCnt = 0;
		if (CollectionUtils.isNotEmpty(saveList.getAddList())) {
			for (MregmaDTO addData : saveList.getAddList()) {
				if (partnerMapper.insertMregma(addData, userData) == 0) {
					throw new InsertCheckedException();
				}
				insertCnt++;
			}
		}

		int updateCnt = 0;
		if (CollectionUtils.isNotEmpty(saveList.getUpdateList()) && CollectionUtils.isNotEmpty(saveList.getOldList())) {
			for (int i = 0; i < saveList.getUpdateList().size(); i++) {
				saveList.getUpdateList().get(i).setOldRegikey(saveList.getOldList().get(i).getRegikey());
				if (partnerMapper.updateMregma(saveList.getUpdateList().get(i), userData) == 0) {
					throw new UpdateCheckedException();
				}
				updateCnt++;
			}
		}
		return insertCnt + updateCnt;
	}

	// 목적지 셀렉트박스 조회
	public List<ComboDataDTO> getRegionSelectBox(MregmaDTO params) {
		return partnerMapper.selectMregmaSelectBox(params, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	/* ************************ DOE009 : 고객 등록 ************************ */
	// 고객 리스트 조회
	public List<McusmaDTO> getMcusmaList(McusmaDTO params) {
		return partnerMapper.selectMcusmaList(params, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	// 고객 데이터 저장
	public int saveDoe009(GridDTO<McusmaDTO> saveList) {
		if (saveList == null) {
			throw new NoSaveDataException();
		}

		UserVO userData = SecurityUtils.getCustomUserDetails().getUserInfo();

		int insertCnt = 0;
		if (CollectionUtils.isNotEmpty(saveList.getAddList())) {
			for (McusmaDTO addData : saveList.getAddList()) {
				if (partnerMapper.insertMcusma(addData, userData) == 0) {
					throw new InsertCheckedException();
				}
				insertCnt++;
			}
			if(insertCnt > 0) {
				apiConfig.requestToDC(saveList.getAddList(), ApiInfo.API_MD_MCUSMA, HttpMethod.POST);
			}
		}

		int updateCnt = 0;
		if (CollectionUtils.isNotEmpty(saveList.getUpdateList()) && CollectionUtils.isNotEmpty(saveList.getOldList())) {
			for (int i = 0; i < saveList.getUpdateList().size(); i++) {
				saveList.getUpdateList().get(i).setOldCustkey(saveList.getOldList().get(i).getCustkey());
				if (partnerMapper.updateMcusma(saveList.getUpdateList().get(i), userData) == 0) {
					throw new UpdateCheckedException();
				}
				updateCnt++;
			}
			if(updateCnt > 0) {
				apiConfig.requestToDC(saveList.getUpdateList(), ApiInfo.API_MD_MCUSMA, HttpMethod.PATCH);
			}
		}
		return insertCnt + updateCnt;
	}

	// 고객 셀렉트박스 조회
	public List<ComboDataDTO> getCustomerSelectBox(McusmaDTO params) {
		return partnerMapper.selectMcusmaSelectBox(params, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	/* ************************ DOE010 : 상세착지 등록 ************************ */
	// 상세착지 리스트 조회
	public List<MdesmaDTO> getMdesmaList(MdesmaDTO params) {
		return partnerMapper.selectMdesmaList(params, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	// 상세착지 데이터 저장
	public int saveDoe010(GridDTO<MdesmaDTO> saveList) {
		if (saveList == null) {
			throw new NoSaveDataException();
		}

		UserVO userData = SecurityUtils.getCustomUserDetails().getUserInfo();

		int insertCnt = 0;
		if (CollectionUtils.isNotEmpty(saveList.getAddList())) {
			for (MdesmaDTO addData : saveList.getAddList()) {
				if (partnerMapper.insertMdesma(addData, userData) == 0) {
					throw new InsertCheckedException();
				}
				insertCnt++;

				// api를 위한 set
				addData.setReuseyn(partnerMapper.selectMregmaReuseyn(addData.getRegikey(), userData));
			}
			if(insertCnt > 0) {
				apiConfig.requestToDC(saveList.getAddList(), ApiInfo.API_MD_MDESMA, HttpMethod.POST);
			}
		}

		int updateCnt = 0;
		if (CollectionUtils.isNotEmpty(saveList.getUpdateList()) && CollectionUtils.isNotEmpty(saveList.getOldList())) {
			for (int i = 0; i < saveList.getUpdateList().size(); i++) {
				saveList.getUpdateList().get(i).setOldRegikey(saveList.getOldList().get(i).getRegikey());
				saveList.getUpdateList().get(i).setOldCustkey(saveList.getOldList().get(i).getCustkey());
				saveList.getUpdateList().get(i).setOldDestkey(saveList.getOldList().get(i).getDestkey());
				if (partnerMapper.updateMdesma(saveList.getUpdateList().get(i), userData) == 0) {
					throw new UpdateCheckedException();
				}
				updateCnt++;
			}
			if(updateCnt > 0) {
				apiConfig.requestToDC(saveList.getUpdateList(), ApiInfo.API_MD_MDESMA, HttpMethod.PATCH);
			}
		}
		return insertCnt + updateCnt;
	}

	// 상세착지 셀렉트박스 조회
	public List<ComboDataDTO> getDestinationSelectBox(MdesmaDTO params) {
		return partnerMapper.selectMdesmaSelectBox(params, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	// 고객 - 목적지 관계리스트 조회
	public List<MdesmaDTO> getRegistrationRelationList(MdesmaDTO params) {
		return partnerMapper.selectRegistrationRelationList(params, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	// 고객 - 목적지 - 상세착지 관계리스트 조회
	public List<MdesmaDTO> getDestinationRelationList(MdesmaDTO params) {
		return partnerMapper.selectDestinationRelationList(params, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	/* ************************ DOE013 : 판매계약사 등록 (보류) ************************ */
	// 판매계약사 리스트 조회
	public List<MowrmaDTO> getMowrmaList(MowrmaDTO params) {
		return partnerMapper.selectMowrmaList(params, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	// 판매계약사 데이터 저장
	public int saveDoe013(GridDTO<MowrmaDTO> saveList) {
		if (saveList == null) {
			throw new NoSaveDataException();
		}

		UserVO userData = SecurityUtils.getCustomUserDetails().getUserInfo();

		int insertCnt = 0;
		if (CollectionUtils.isNotEmpty(saveList.getAddList())) {
			for (MowrmaDTO addData : saveList.getAddList()) {
				if (partnerMapper.insertMowrma(addData, userData) == 0) {
					throw new InsertCheckedException();
				}
				insertCnt++;
			}
		}
		int updateCnt = 0;
		if (CollectionUtils.isNotEmpty(saveList.getUpdateList()) && CollectionUtils.isNotEmpty(saveList.getOldList())) {
			for (int i = 0; i < saveList.getUpdateList().size(); i++) {
				saveList.getUpdateList().get(i).setOldOwnerky(saveList.getOldList().get(i).getOwnerky());
				if (partnerMapper.updateMowrma(saveList.getUpdateList().get(i), userData) == 0) {
					throw new UpdateCheckedException();
				}
				updateCnt++;
			}
		}
		return insertCnt + updateCnt;
	}

	// 판매계약사 셀렉트박스 조회
	public List<ComboDataDTO> getOwnerSelectBox(MowrmaDTO params) {
		return partnerMapper.selectMowrmaSelectBox(params, SecurityUtils.getCustomUserDetails().getUserInfo());
	}
}
