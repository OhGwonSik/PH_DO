package com.kbph.logistics.sy.mapper;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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

@Mapper
public interface UserMapper {

	// 유저 어플리케이션 조회
	String selectUserApplication(UserVO userVO);

	// 유저 스키마 조회
	List<SchemaDTO> selectInformationSchema(String apnamen);

	// 유저 조회
	UserVO selectUserCompanyAccount(UserVO userVO);

	// 메뉴 헤더 조회
	List<SmnghdDTO> selectMainHeaderList(UserVO userVO);

	// 메뉴 리스트 조회
	List<SmnumaDTO> selectUserMenuList(UserVO userVO);

	int insertUserProgram(HashMap<String, Object> hashMap);

	List<GridSettingLayoutDTO> selectGridSettingLayout(@Param("param") Map<String, Object> params, @Param("userData") UserVO userData);

	List<GridColumnLayoutDTO> selectGridColumnLayout(@Param("param") Map<String, Object> params, @Param("userData") UserVO userData);

	//사용자 Grid Setting Layout 저장
	int mergeSettingLayout(@Param("param") GridSettingLayoutDTO dto, @Param("userData") UserVO userData);
	//사용자 Grid Column Layout 저장
	int mergeColumnLayout(HashMap<String, Object> map);

	//사용자 Grid Setting Layout 삭제
	int deleteColumnLayout(HashMap<String, Object> map);
	//사용자 Grid Column Layout 삭제
	int deleteSettingLayout(HashMap<String, Object> map);

	// 사용자 정보 조회
	List<UserDTO> selectUserList(@Param("param") UserDTO param, @Param("userData") UserVO userData);

	// 가입승인요청 모달 조회
	List<UserDTO> selectUserApproveList(@Param("param") UserDTO param, @Param("userData") UserVO userData);

	// 아이디 중복 확인
	int selectUserCntByUseract(String useract);

	// 유저키 채번
	String selectUserkey();

	// 차주, 기사 회원가입
	int insertSusrma(UserDTO saveData);

	// 신규 사용자 추가 / 승인 (관리자용)
	int insertSusrmaAdmin(@Param("saveData") UserDTO saveData, @Param("userData") UserVO userData);
	int insertSusrsh(@Param("saveData") SusrshDTO saveData, @Param("userData") UserVO userData);
	int insertSusrap(@Param("saveData") SusrapDTO saveData, @Param("userData") UserVO userData);
	int insertSusrvh(@Param("saveData") SusrvhDTO susrvhDTO, @Param("userData") UserVO userData);

	// 사용자 정보 수정
	int updateSyu04Susrma(@Param("saveData") UserDTO saveData, @Param("userData") UserVO userData);
	int updateSusrma(@Param("saveData") UserDTO saveData, @Param("userData") UserVO userData);
	int updateSusrsh(@Param("saveData") SusrshDTO saveData, @Param("userData") UserVO userData);

	//사용자 로그인 이력 저장
	int insertUserLoginHistory(UserVO userData);

	int insertCopyGridHeader(@Param("saveData") UserDTO saveData, @Param("userData") UserVO userData);
	int insertCopyGridLayout(@Param("saveData") UserDTO saveData, @Param("userData") UserVO userData);
}
