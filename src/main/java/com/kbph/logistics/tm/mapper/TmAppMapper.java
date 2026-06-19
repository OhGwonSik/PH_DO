package com.kbph.logistics.tm.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kbph.logistics.om.domain.TpwpwgDTO;
import com.kbph.logistics.tm.domain.TmAppDTO;
import com.kbph.logistics.tm.domain.TplnhdDTO;
import com.kbph.logistics.tm.domain.TplnitDTO;

@Mapper
public interface TmAppMapper {
	
	//운송이력 조회
	List<TplnhdDTO> selectTransportRecordList(@Param("param") TplnhdDTO tplnhd);
	//운송이력(tpwpwg join 다름)
	List<TplnhdDTO> selectTransportCurrentRecord(@Param("param") TplnhdDTO tplnhd);
	
	//목적지 리스트
	List<TmAppDTO> selectRegikeyList(@Param("param") TmAppDTO app);
	
	//공통코드 리스트
	List<TmAppDTO> selectMcodemList(@Param("param") TmAppDTO app);
	
	//운송 상세이력
	List<TplnitDTO> selectTransportDetailHistoryItem(@Param("param") TplnitDTO tplnit);
	
	//유저-차량 리스트
	List<TmAppDTO> selectVehicleList(@Param("param") TmAppDTO app);
	
	//토큰 값 갱신
	int userFcmTokenUpdate(@Param("param") TmAppDTO app);
	//토큰 값 가져오기
	TmAppDTO selectUserFcmToken(@Param("param") TmAppDTO app);
	
	//배차계획(승인여부) 가져오기
	List<TplnhdDTO> selectRequestApprovalList(@Param("param") TplnhdDTO tplnhd);
	//배차계획(승인업데이트) 업데이트
	int updateAppAprovyn(@Param("param") TplnhdDTO tplnhd);
	
	//운송현황(운송상태) 헤드 업데이트
	int updateAppVehicleStatusHead(@Param("param") TplnhdDTO tplnhd);
	//운송현황(운송상태) 아이템 업데이트
	int updateAppVehicleStatusItem(@Param("param") TplnitDTO tplnit);
	//중량 테이블 상태값 변경
	int updateAppTpwpwgArrivyn(@Param("param") TpwpwgDTO app);
	
	//입차리스트 가져오기
	List<TmAppDTO> selectAppComeinVehicleList(@Param("param") TmAppDTO app);
	List<TmAppDTO> selectAppReleaseList(@Param("param") TmAppDTO app);
	
	//입차여부 업데이트
	int updateAppComeinVehicle(@Param("param") TmAppDTO app);
	int updateAppReleaseVehicle(@Param("param") TmAppDTO app);
	
	//알림내역 조회
	List<TmAppDTO> selectAlarmList(@Param("param") TmAppDTO app);
	//알림내역 삭제
	int deleteAlarm(@Param("param") TmAppDTO app);
	
	//입고예정내역 가져오기
	List<TmAppDTO> selectWasnhdList(@Param("param") TmAppDTO app);
	//입고예정내역(아이템) 가져오기
	List<TmAppDTO> selectWasnitList(@Param("param") TmAppDTO app);
	//입고검수 업데이트
	int updateCarryCheck(@Param("param") TmAppDTO app);
	//입고검수 단 체인지
	int changeStlayer(@Param("param") TmAppDTO app);
	//입고검수 헤드상태값 변경
	int updateCarryHeadStatus(@Param("param") TmAppDTO app);
	
	//운행일지
	List<TplnhdDTO> selectAppOperationLog(@Param("param") TplnhdDTO tplnhd);
	
	//알림키 채번
	String selectAlarmky(@Param("schema") String schema);
	//알람내역 insert
	int insertAlrmhi(@Param("param") TmAppDTO app);
	
	//출고이력 헤더
	List<TmAppDTO> selectAppReleaseHeadList(@Param("param") TmAppDTO app);
	//출고이력 아이템
	List<TmAppDTO> selectAppReleaseItemList(@Param("param") TmAppDTO app);
	
	//출고계획 아이템(단건)
	TmAppDTO selectAppWplnit(@Param("param") TmAppDTO app);
	//출고계획 (단건)
	TmAppDTO selectAppWplnhd(@Param("param") TmAppDTO app);
	//출고계획 아이템 업데이트
	int tmUpdateWplnit(@Param("param") TmAppDTO app);
	//출고계획 업데이트
	int updateAppWplnhd(@Param("param") TmAppDTO app);
	
	//출고중량 조회
	List<TpwpwgDTO> selectAppTpwpwgList(@Param("param") TpwpwgDTO tpwpwg);
	//출고중량 삭제
	int deleteAppTpwpwg(@Param("param") TpwpwgDTO tpwpwg);
	
	//로케이션
	TmAppDTO selectAlarmControlParameterName(@Param("param") TmAppDTO app);
	
	//입고이력
	List<TmAppDTO> selectAppCarryRecordList(@Param("param") TmAppDTO app);
	//입고이력 - 아이템
	List<TmAppDTO> selectAppCarryRecordItemList(@Param("param") TmAppDTO app);
}
