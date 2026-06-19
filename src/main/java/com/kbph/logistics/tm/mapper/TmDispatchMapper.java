package com.kbph.logistics.tm.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kbph.logistics.common.domain.ComboDataDTO;
import com.kbph.logistics.md.domain.MgrpmaDTO;
import com.kbph.logistics.md.domain.MregmaDTO;
import com.kbph.logistics.om.domain.TpwpwgDTO;
import com.kbph.logistics.sy.domain.UserVO;
import com.kbph.logistics.tm.domain.TmDispatchDTO;
import com.kbph.logistics.tm.domain.TplnhdDTO;
import com.kbph.logistics.tm.domain.TplnitDTO;

@Mapper
public interface TmDispatchMapper {
	
	//운송현황 헤드 그리드
	List<TplnhdDTO> selectDispatchHeadOrder(@Param("param") TplnhdDTO tplnhd, @Param("userInfo") UserVO userData);
	//운송현황 아이템 그리드
	List<TplnitDTO> selectDispatchItemOrder(@Param("param") TplnitDTO tplnit, @Param("userInfo") UserVO userData);
	//운송현황(TPWPWG JOIN 다름)
	List<TplnhdDTO> selectDispatchCurrentHeadOrder(@Param("param") TplnhdDTO tplnhd, @Param("userInfo") UserVO userData);
	
	//배차 승인, 취소 업데이트
	int updateDispatchAprovyn(@Param("param") TplnhdDTO tplnhd, @Param("userInfo") UserVO userData);
	
	//헤더, 아이템 배차상태 업데이트
	int updateTplnhd(@Param("param") TplnhdDTO tplnhd, @Param("userInfo") UserVO userData);
	int updateTpistat(@Param("param") TplnitDTO tplnit, @Param("userInfo") UserVO userData);
	//헤더 승인여부 업데이트
	int updateTplnhdAprovyn(@Param("param") TplnhdDTO tplnhd, @Param("userInfo") UserVO userData);
	//헤더 배차 업데이트
	int updateTplnhdTpnstat(@Param("param") TplnhdDTO tplnhd, @Param("userInfo") UserVO userData);
	//중량 테이블 업데이트
	int updateTmTpwpwg(@Param("param") TpwpwgDTO tpwpwg, @Param("userInfo") UserVO userData);
	
	//운송오더 헤드 조회
	List<TmDispatchDTO> selectVehicleOrderList(@Param("param") TmDispatchDTO dispatch, @Param("userInfo") UserVO userData);
	//운송오더 헤드 조회(단건)
	TmDispatchDTO selectTmWplnhd(@Param("param") TmDispatchDTO dispatch, @Param("userInfo") UserVO user);
	//운송오더 아이템 조회
	List<TmDispatchDTO> selectVehicleOrderItemList(@Param("param") TmDispatchDTO dispatch, @Param("userInfo") UserVO userData);
	//중량테이블 조회
	List<TpwpwgDTO> selectTpwpwgList(@Param("param") TpwpwgDTO tpwpwg, @Param("userInfo") UserVO user);
	
	//목적지 조회
	List<ComboDataDTO> selectRegikeyList(@Param("param") MregmaDTO mregma, @Param("userInfo") UserVO uservo);
	
	//차량조회
	List<TmDispatchDTO> selectVehicleList(@Param("param") TmDispatchDTO dispatch, @Param("userInfo") UserVO userData);
	//차량-기사 매핑
	List<TmDispatchDTO> selectVehicleDriverMapping(@Param("param") TmDispatchDTO dispatch);
	//베드 리스트 (SelectBox)
	List<TmDispatchDTO> selectLocationList(@Param("param") TmDispatchDTO dispatch, @Param("userInfo") UserVO userData);
	//베드 리스트 (필터)
	List<TmDispatchDTO> selectLocationFilterList(@Param("param") TmDispatchDTO dispatch, @Param("userInfo") UserVO userData);
	//재고조인 리스트
	List<TmDispatchDTO> selectTplnitWstkkyJoinList(@Param("param") TmDispatchDTO dispatch, @Param("userInfo") UserVO userData);
	//제품 그룹키 리스트 (SelectBox)
	List<MgrpmaDTO> selectPlnsizeList(@Param("param") MgrpmaDTO mgrpma, @Param("userInfo") UserVO user);
	
	//운행일지(일단위)
	List<TplnhdDTO> selectOperationDayLog(@Param("param") TplnhdDTO tplnhd, @Param("userInfo") UserVO userData);
	//운행일지(월단위)
	List<TplnhdDTO> selectOperationMonthLog(@Param("param") TplnhdDTO tplnhd, @Param("userInfo") UserVO userData);
	
	//배차계획키 채번
	String selectVhplnky(@Param("userInfo") UserVO userData);
	//배차계획아이템번호 채번
	Integer selectVhplnit(@Param("param") TplnitDTO tplnit, @Param("userInfo") UserVO userData);
	
	//배차계획 insert
	int insertTplnhd(@Param("param") TplnhdDTO tplnhd, @Param("userInfo") UserVO userData);
	//배차계획별 insert
	int insertTpwpwg(@Param("param") TpwpwgDTO tpwpwg, @Param("userInfo") UserVO userData);
	//배차계획아이템 insert
	int insertTplnit(@Param("param") TplnitDTO tplnit, @Param("userInfo") UserVO userData);
	//출고아이템 update
	int tmUpdateWplnit(@Param("param") TmDispatchDTO dispatch, @Param("userInfo") UserVO userData);
	//출고헤더 update
	int tmUpdateWplnhd(@Param("param") TmDispatchDTO dispatch, @Param("userInfo") UserVO userData);

	//출고중량 delete
	int deleteTmTpwpwg(@Param("param") TpwpwgDTO tpwpwg, @Param("userInfo") UserVO user);
	
	//차량정보
	TmDispatchDTO selectVehicleInformation(@Param("param") TmDispatchDTO dispatch);
	
	//맥스단위치
	Integer selectMaxLayerByLocation(@Param("param") TmDispatchDTO dispatch, @Param("userInfo") UserVO userData);
	//재고생성
	int insertWstkkyData(@Param("param") TplnitDTO tplnit, @Param("userInfo") UserVO userData);
	
	//출고예정 단건 조회
	TmDispatchDTO selectTmWplnit(@Param("param") TmDispatchDTO dispatch, @Param("userInfo") UserVO userData);
}
