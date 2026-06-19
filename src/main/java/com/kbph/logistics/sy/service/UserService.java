package com.kbph.logistics.sy.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.kbph.logistics.common.domain.ComboDataDTO;
import com.kbph.logistics.common.domain.GridDTO;
import com.kbph.logistics.common.domain.InitDataDTO;
import com.kbph.logistics.common.enums.Useyn;
import com.kbph.logistics.common.util.SecurityUtils;
import com.kbph.logistics.configuration.BeanConfig;
import com.kbph.logistics.configuration.I18nConfig;
import com.kbph.logistics.configuration.error.InsertCheckedException;
import com.kbph.logistics.configuration.error.NoSaveDataException;
import com.kbph.logistics.configuration.error.UpdateCheckedException;
import com.kbph.logistics.md.domain.MvhcmaDTO;
import com.kbph.logistics.md.domain.MvowmaDTO;
import com.kbph.logistics.md.mapper.PartnerMapper;
import com.kbph.logistics.sy.domain.GridColumnLayoutDTO;
import com.kbph.logistics.sy.domain.GridSettingLayoutDTO;
import com.kbph.logistics.sy.domain.SchemaDTO;
import com.kbph.logistics.sy.domain.SmnghdDTO;
import com.kbph.logistics.sy.domain.SmnumaDTO;
import com.kbph.logistics.sy.domain.SusrapDTO;
import com.kbph.logistics.sy.domain.SusrshDTO;
import com.kbph.logistics.sy.domain.SusrvhDTO;
import com.kbph.logistics.sy.domain.UserDTO;
import com.kbph.logistics.sy.domain.UserVO;
import com.kbph.logistics.sy.mapper.UserMapper;
import com.kbph.logistics.sy.type.MvowmaFlag;
import com.kbph.logistics.sy.type.StandardUser;
import com.kbph.logistics.sy.type.SusrmaFlag;
import com.kbph.logistics.sy.type.SusrvhFlag;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserMapper userMapper;
	private final PartnerMapper partnerMapper;
	private final BeanConfig beanConfig;
	private final I18nConfig i18nConfig;

	public String getUserApplication(UserVO userVO) {
		return userMapper.selectUserApplication(userVO);
	}

	public List<SchemaDTO> getSchemaList(String apnamen) {
		return userMapper.selectInformationSchema(apnamen);
	}

	public UserVO selectUserCompanyAccount(UserVO userVO) {
		return userMapper.selectUserCompanyAccount(userVO);
	}

	public List<SmnghdDTO> getMainHeaderList(UserVO userVO) {
		List<SmnghdDTO> userHeaderList = userMapper.selectMainHeaderList(userVO);
		MessageSourceAccessor messageSourceAccessor = i18nConfig.getMessageSourceAccessor();

		for (SmnghdDTO userHeader : userHeaderList) {
			userHeader.setMnhdnam(messageSourceAccessor.getMessage(userHeader.getMnhdlky(), LocaleContextHolder.getLocale()));
		}

		return userHeaderList;
	}

	public List<SmnumaDTO> getUserMenu(UserVO userVO) {
		List<SmnumaDTO> userMenuList = userMapper.selectUserMenuList(userVO);
		MessageSourceAccessor messageSourceAccessor = i18nConfig.getMessageSourceAccessor();

		for (SmnumaDTO userMenu : userMenuList) {
			userMenu.setMenunam(messageSourceAccessor.getMessage(userMenu.getMenulky(), LocaleContextHolder.getLocale()));
		}

		return userMenuList;
	}

	public void insertUserProgram(Map<String, Object> params, UserVO userInfo) {
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("useract", userInfo.getUseract());
		hashMap.put("progrid", params.get("gprogrid"));
		hashMap.put("menunam", params.get("gnatitle"));
		hashMap.put("creuser", userInfo.getUseract());

		if (userMapper.insertUserProgram(hashMap) == 0) {
			throw new InsertCheckedException();
		}
	}

	public List<GridSettingLayoutDTO> gridSettingLayout(Map<String, Object> params) {
		return userMapper.selectGridSettingLayout(params, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	public List<GridColumnLayoutDTO> gridColumnLayout(Map<String, Object> params) {
		return userMapper.selectGridColumnLayout(params, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	// 그리드 저장
	public int setSYU10Save(Map<String, Object> params) {
		ArrayList dataList = (ArrayList) params.get("data");
		List<GridColumnLayoutDTO> list = new ArrayList<>();

		int insertResult = 0;
		int updateResult = 0;

		for (int i = 0; i < dataList.size(); i++) {
			GridColumnLayoutDTO gcl = new GridColumnLayoutDTO();
			LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) dataList.get(i);
			gcl.setDataidx(String.valueOf(map.get("dataIndx")));
			gcl.setPhidden((Boolean) map.get("hidden"));
			gcl.setPqalign(String.valueOf(map.get("align")));
			gcl.setDatatyp(String.valueOf(map.get("dataType")));
			if (map.containsKey("width")) {
				gcl.setPqwidth((int) map.get("width"));
			}
			list.add(gcl);
		}

		IntStream.range(0, list.size()).forEach(i -> list.get(i).setSortnum(i));

		HashMap<String, Object> map = new HashMap<>();
		map.put("userData", SecurityUtils.getCustomUserDetails().getUserInfo());
		map.put("progrid", params.get("progrid"));
		map.put("pgridid", params.get("pgridid"));
		map.put("list", list);

		insertResult = userMapper.mergeColumnLayout(map);
		if (insertResult == 0) {
			throw new InsertCheckedException();
		}

		GridSettingLayoutDTO dto = new GridSettingLayoutDTO();
		dto.setProgrid((String) params.get("progrid"));
		dto.setPgridid((String) params.get("pgridid"));
		dto.setNubrcel((Boolean) params.get("nubrcel"));
		dto.setHovermd((String) params.get("hovermd"));
		dto.setFrezcol((int) params.get("frezcol"));
		dto.setFrezrow((int) params.get("frezrow"));
		dto.setColbodr((Boolean) params.get("colbodr"));
		dto.setRowbodr((Boolean) params.get("rowbodr"));
		dto.setStrprow((Boolean) params.get("strprow"));
		dto.setGheight((int) params.get("gheight"));

		updateResult = userMapper.mergeSettingLayout(dto, SecurityUtils.getCustomUserDetails().getUserInfo());
		if (updateResult == 0) {
			throw new InsertCheckedException();
		}

		return insertResult + updateResult;
	}

	// 그리드 리셋
	public int setSYU10Reset(Map<String, Object> params) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("userData", SecurityUtils.getCustomUserDetails().getUserInfo());
		map.put("progrid", params.get("progrid"));
		map.put("pgridid", params.get("pgridid"));

		return userMapper.deleteColumnLayout(map) + userMapper.deleteSettingLayout(map);
	}

	public boolean isValidProgrid(String progrid) {
		UserVO userInfo = SecurityUtils.getCustomUserDetails().getUserInfo();

		if (userInfo == null) {
			return false;
		}
		userInfo.setProgrid(progrid);
		List<SmnumaDTO> userMenuList = this.getUserMenu(userInfo);

		for (SmnumaDTO usermenu : userMenuList) {
			if (progrid.equals(usermenu.getProgrid())) {
				return true;
			}
		}

		return false;
	}

	// syu04 초기화
	public InitDataDTO getSyu04InitData(UserDTO param) {
		Map<String, Object> item = new HashMap<>();
		UserVO userData = SecurityUtils.getCustomUserDetails().getUserInfo();

		MvhcmaDTO mvhcmaDTO = new MvhcmaDTO();
		item.put("vehickyList", partnerMapper.selectMvhcmaSelectBox(mvhcmaDTO, userData));

		MvowmaDTO mvowmaDTO = new MvowmaDTO();
		mvowmaDTO.setSigupyn(MvowmaFlag.UNSIGNED_VEHICLE_OWNER.getString());
		mvowmaDTO.setVouseyn(Useyn.USE.getString());
		item.put("vownkeyList", partnerMapper.selectMvowmaList(mvowmaDTO, userData));

		InitDataDTO initDataDTO = new InitDataDTO();
		initDataDTO.setItem(item);
		return initDataDTO;
	}

	// 사용자 정보 조회
	public List<UserDTO> getUserList(UserDTO param) {
		UserVO userData = SecurityUtils.getCustomUserDetails().getUserInfo();
		param.setApnamen(userData.getApnamen());
		return userMapper.selectUserList(param, userData);
	}

	// 가입승인요청 모달 조회
	public List<UserDTO> getUserApproveList(UserDTO param) {
		return userMapper.selectUserApproveList(param, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	// 아이디 중복 확인
	public int getUseractCnt(String useract) {
		return userMapper.selectUserCntByUseract(useract);
	}

	// 차주 및 기사 회원가입
	public int signUpUser(UserDTO saveData) {
		if (saveData == null) {
			throw new NoSaveDataException();
		}
		saveData.setUserkey(userMapper.selectUserkey());
		saveData.setPasswrd(beanConfig.passwordEncoder().encode(saveData.getPasswrd()));

		// 플래그 처리
		saveData.setApproyn(SusrmaFlag.UNAPPROVE.getString());
		saveData.setSigupyn(SusrmaFlag.SIGNED.getString());
		saveData.setApuseyn(SusrmaFlag.APP_USER.getString());
		saveData.setActivyn(SusrmaFlag.DEACTIVE.getString());

		saveData.setLangkey("DY"); // 하드코딩 (임시)

		int result = userMapper.insertSusrma(saveData);
		if (result == 0) {
			throw new InsertCheckedException();
		}
		return result;
	}

	// 앱사용자 가입 승인
	public int approveAppUser(GridDTO<UserDTO> saveList) {
		if (saveList == null) {
			throw new NoSaveDataException();
		}

		UserVO userData = SecurityUtils.getCustomUserDetails().getUserInfo();
		int result = 0;
		for (UserDTO saveData : saveList.getUpdateList()) {
			saveData.setActivyn(SusrmaFlag.ACTIVE.getString());

			// 차량 저장
			if (saveData.getVownkey() != null && !"".equals(saveData.getVownkey())) {
				saveVehicleOwner(saveData, userData);
				result++;
			} else if (saveData.getVehicky() != null && !"".equals(saveData.getVehicky())){
				saveDriver(saveData, userData);
				result++;
			}

			// 어플리케이션키 저장
			if (saveApplication(saveData, userData)) {
				result++;
			}

			// 유저정보 플래그 저장
			if (userMapper.updateSusrma(saveData, userData) == 0) {
				throw new UpdateCheckedException();
			}
			result++;
		}

		return result;
	}

	// 사용자 정보 그리드 수정
	public int saveUser(@RequestBody GridDTO<UserDTO> saveList) {
		if (saveList == null) {
			throw new NoSaveDataException();
		}
		UserVO userData = SecurityUtils.getCustomUserDetails().getUserInfo();
		int result = 0;
		if (saveList.getUpdateList() != null && saveList.getOldList() != null) {
			for (int i = 0; i < saveList.getUpdateList().size(); i++) {
				UserDTO updateData = saveList.getUpdateList().get(i);

				// 유저정보 저장
				updateData.setOldUserKey(saveList.getOldList().get(i).getUserkey());
				if (userMapper.updateSyu04Susrma(updateData, userData) == 0) {
					throw new UpdateCheckedException();
				}
				result++;

				// 사용자타입, 권한 저장
				if (saveList.getOldList().get(i).getUsertyp() != null || saveList.getOldList().get(i).getRolgkey() != null) {
					SusrshDTO susrshDTO = new SusrshDTO();
					susrshDTO.setOldUserkey(saveList.getOldList().get(i).getUserkey());
					susrshDTO.setUserkey(updateData.getUserkey());
					susrshDTO.setUseract(updateData.getUseract());
					susrshDTO.setUpdtchk(updateData.getUpdtchk2());
					susrshDTO.setUsertyp(updateData.getUsertyp());
					susrshDTO.setRolgkey(updateData.getRolgkey());
					if (userMapper.updateSusrsh(susrshDTO, userData) == 0) {
						throw new UpdateCheckedException();
					}
					result++;
				}
			}
		}
		return result;
	}

	// 신규사용자 추가
	public int addNewUser(GridDTO<UserDTO> saveList) {
		if (saveList.getAddList() == null) {
			throw new NoSaveDataException();
		}

		int result = 0;
		UserVO userData = SecurityUtils.getCustomUserDetails().getUserInfo();
		for (UserDTO addData : saveList.getAddList()) {
			addData.setUserkey(userMapper.selectUserkey());
			addData.setPasswrd(beanConfig.passwordEncoder().encode(addData.getPasswrd()));

			// 플래그 처리
			addData.setSigupyn(SusrmaFlag.UNSIGNED.getString());
			addData.setApproyn(SusrmaFlag.APPROVE.getString());
			addData.setActivyn(SusrmaFlag.ACTIVE.getString());

			addData.setLangkey(userData.getSchema().substring(userData.getSchema().length() - 2).toUpperCase());

			// 사용자타입, 권한 등록
			if (addData.getUsertyp() != null && addData.getRolgkey() != null) {
				addData.setApuseyn(SusrmaFlag.APP_UNUSER.getString()); // 앱사용자여부
				SusrshDTO susrshDTO = new SusrshDTO();
				susrshDTO.setUserkey(addData.getUserkey());
				susrshDTO.setUseract(addData.getUseract());
				susrshDTO.setUsertyp(addData.getUsertyp());
				susrshDTO.setRolgkey(addData.getRolgkey());
				if (userMapper.insertSusrsh(susrshDTO, userData) == 0) {
					throw new InsertCheckedException();
				}
				result++;

				addData.setStandardUseract(StandardUser.getStandardUserBySchemaAndUserType(userData.getSchema(), addData.getUsertyp()).getStandardUseract());
				if(userMapper.insertCopyGridHeader(addData, userData) == 0 || userMapper.insertCopyGridLayout(addData, userData) == 0) {
					throw new InsertCheckedException();
				}
				result++;
			}else {
				addData.setApuseyn(SusrmaFlag.APP_USER.getString()); // 앱사용자여부
			}

			// 유저 정보 등록
			if (userMapper.insertSusrmaAdmin(addData, userData) == 0) {
				throw new InsertCheckedException();
			}
			result++;

			// 어플리케이션키 저장
			if (saveApplication(addData, userData)) {
				result++;
			}

		}
		return result;
	}

	// 어플리케이션 키 저장 (SUSRAP)
	public boolean saveApplication(UserDTO saveData, UserVO userData) {
		SusrapDTO susrapDTO = new SusrapDTO();
		susrapDTO.setUserkey(saveData.getUserkey());
		susrapDTO.setUseract(saveData.getUseract());
		susrapDTO.setApnamen(userData.getApnamen());
		if (userMapper.insertSusrap(susrapDTO, userData) == 0) {
			throw new InsertCheckedException();
		}
		return true;
	}

	// 기사 저장 (SUSRVH)
	public boolean saveDriver(UserDTO saveData, UserVO userData) {
		// 차량의 차주키 불러오기
		MvhcmaDTO mvhcmaDTO = new MvhcmaDTO();
		mvhcmaDTO.setVehicky(saveData.getVehicky());
		String vownkey = partnerMapper.selectMvhcmaList(mvhcmaDTO, userData).get(0).getVownkey();

		SusrvhDTO susrvhDTO = new SusrvhDTO();
		susrvhDTO.setVehicky(saveData.getVehicky());
		susrvhDTO.setUserkey(saveData.getUserkey());
		susrvhDTO.setUseract(saveData.getUseract());
		susrvhDTO.setVownkey(vownkey);
		susrvhDTO.setVhownyn(SusrvhFlag.NOT_VHICLE_OWNER.getString()); // 차주여부
		susrvhDTO.setVhuseyn(SusrvhFlag.MAPPING_USE.getString()); // 차량매핑여부
		if (userMapper.insertSusrvh(susrvhDTO, userData) == 0) {
			throw new InsertCheckedException();
		}
		return true;
	}

	// 차주 저장 (SUSRVH, MVOWMA)
	public boolean saveVehicleOwner(UserDTO saveData, UserVO userData) {
		// 차주가 가지고 있는 차량 불러오기
		MvhcmaDTO mvhcmaDTO = new MvhcmaDTO();
		mvhcmaDTO.setVownkey(saveData.getVownkey());
		List<ComboDataDTO> vehickyList = partnerMapper.selectMvhcmaSelectBox(mvhcmaDTO, userData);

		// 차주 차량 저장
		if (!vehickyList.isEmpty()) {
			SusrvhDTO susrvhDTO = new SusrvhDTO();
			susrvhDTO.setUserkey(saveData.getUserkey());
			susrvhDTO.setUseract(saveData.getUseract());
			susrvhDTO.setVownkey(saveData.getVownkey());
			susrvhDTO.setVhownyn(SusrvhFlag.VEHICLE_OWNER.getString());
			susrvhDTO.setVhuseyn(SusrvhFlag.MAPPING_USE.getString());
			for (ComboDataDTO vehickyData : vehickyList) {
				susrvhDTO.setVehicky(vehickyData.getCombovl());
				if (userMapper.insertSusrvh(susrvhDTO, userData) == 0) {
					throw new InsertCheckedException();
				}
			}
		}

		// 차주 회원등록여부 플래그 수정
		MvowmaDTO mvowmaDTO = new MvowmaDTO();
		mvowmaDTO.setVownkey(saveData.getVownkey());
		mvowmaDTO.setUpdtchk(saveData.getUpdtchk2());
		mvowmaDTO.setSigupyn(MvowmaFlag.SIGNED_VEHICLE_OWNER.getString());
		if (partnerMapper.updateMvowmaSign(mvowmaDTO, userData) == 0) {
			throw new UpdateCheckedException();
		}
		return true;
	}

	// 사용자 비밀번호 초기화
	public int initUserPasswrd(GridDTO<UserDTO> saveList) {
		if (saveList == null) {
			throw new NoSaveDataException();
		}
		int updateCnt = 0;
		UserVO userData = SecurityUtils.getCustomUserDetails().getUserInfo();
		if (saveList.getUpdateList() != null) {
			for (UserDTO updateData : saveList.getUpdateList()) {
				updateData.setPasswrd(beanConfig.passwordEncoder().encode(updateData.getUseract() + "1234!@"));
				if (userMapper.updateSusrma(updateData, userData) == 0) {
					throw new UpdateCheckedException();
				}
				updateCnt++;
			}
		}
		return updateCnt;
	}

	//사용자 로그인 이력 저장
	public void insertUserLoginHistory(UserVO userData) {
		userMapper.insertUserLoginHistory(userData);
	}
}