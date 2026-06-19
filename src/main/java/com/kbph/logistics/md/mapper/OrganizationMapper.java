package com.kbph.logistics.md.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kbph.logistics.common.domain.ComboDataDTO;
import com.kbph.logistics.md.domain.MaremaDTO;
import com.kbph.logistics.md.domain.MeqlocDTO;
import com.kbph.logistics.md.domain.MequmaDTO;
import com.kbph.logistics.md.domain.MlocmaDTO;
import com.kbph.logistics.md.domain.MwarmaDTO;
import com.kbph.logistics.md.domain.PrinterDTO;
import com.kbph.logistics.sy.domain.UserVO;

public interface OrganizationMapper {
	/*************** MWARMA : 창고 마스터 ***************/
	// 창고 리스트 조회
	List<MwarmaDTO> selectMwarmaList(@Param("params") MwarmaDTO params, @Param("userData") UserVO userData);

	// 창고키 채번
	String selectWarekey(@Param("userData") UserVO userData);

	// 창고 등록
	int insertMwarma(@Param("addData") MwarmaDTO addData, @Param("userData") UserVO userData);

	// 창고 수정
	int updateMwarma(@Param("updateData") MwarmaDTO updateData, @Param("userData") UserVO userData);

	// 창고 셀렉트박스 조회
	List<ComboDataDTO> selectMwarmaSelectBox(@Param("params") MwarmaDTO params, @Param("userData") UserVO userData);

	// 창고 프린터 이름 조회
	List<PrinterDTO> selectWarehousePrinterList(@Param("warekey") String warekey, @Param("userData") UserVO userData);
	PrinterDTO selectWarehousePrinter(@Param("warekey") String warekey, @Param("userData") UserVO userData);

	// 해당 창고 출고지시서 출력여부 조회
	String selectShipOrderYn(@Param("warekey") String warekey, @Param("userData") UserVO userData);

	/*************** MAREMA : 창고동 마스터 ***************/
	// 창고동 리스트 조회
	List<MaremaDTO> selectMaremaList(@Param("params") MaremaDTO params, @Param("userData") UserVO userData);

	// 창고동 등록
	int insertMarema(@Param("addData") MaremaDTO addData, @Param("userData") UserVO userData);

	// 창고동 수정
	int updateMarema(@Param("updateData") MaremaDTO updateData, @Param("userData") UserVO userData);

	// 창고동 셀렉트박스 조회
	List<ComboDataDTO> selectMaremaSelectBox(@Param("params") MaremaDTO params, @Param("userData") UserVO userData);

	// 창고 - 창고동 관계리스트 조회
	public List<MaremaDTO> selectAreaRelationList(@Param("param") MaremaDTO params, @Param("userData") UserVO userData);

	// 창고동 타입 조회
	public String selectMaremaAreatyp(@Param("areakey") String areakey, @Param("userData") UserVO userData);

	/*************** MLOCMA : 베드 마스터 ***************/
	// 베드 리스트 조회
	List<MlocmaDTO> selectMlocmaList(@Param("params") MlocmaDTO params, @Param("userData") UserVO userData);

	// 베드 등록
	int insertMlocma(@Param("addData") MlocmaDTO addData, @Param("userData") UserVO userData);

	// 베드 수정
	int updateMlocma(@Param("updateData") MlocmaDTO updateData, @Param("userData") UserVO userData);

	// 베드 셀렉트박스 조회
	List<ComboDataDTO> selectMlocmaSelectBox(@Param("params") MlocmaDTO params, @Param("userData") UserVO userData);

	// 베드 타입 조회
	String selectLoctypeByKey(@Param("params") MlocmaDTO params, @Param("userData") UserVO userData);

	// 창고 - 창고동 - 베드 관계리스트 조회
	public List<MlocmaDTO> selectLocationRelationList(@Param("param") MlocmaDTO params, @Param("userData") UserVO userData);

	// 베드 관계 리스트 조회
	List<MlocmaDTO> selectWareAreaRelations(@Param("params") MlocmaDTO params, @Param("userData") UserVO userData); // 창고, 창고동

	List<MlocmaDTO> selectWareAreaLocRelations(@Param("params") MlocmaDTO params, @Param("userData") UserVO userData); // 창고, 창고동, 베드

	/*************** MEQUMA : 설비 마스터 ***************/
	// 설비 리스트 조회
	List<MequmaDTO> selectMequemaList(@Param("params") MequmaDTO params, @Param("userData") UserVO userData);

	// 설비 등록
	int insertMequma(@Param("addData") MequmaDTO addData, @Param("userData") UserVO userData);

	// 설비 수정
	int updateMequma(@Param("updateData") MequmaDTO updateData, @Param("userData") UserVO userData);

	// 설비 셀렉트박스 조회
	List<ComboDataDTO> selectMequmaSelectBox(@Param("params") MequmaDTO params, @Param("userData") UserVO userData);

	// 설비 관계 리스트 조회
	List<MequmaDTO> selectWareAreaEquipRelations(@Param("params") MequmaDTO params, @Param("userData") UserVO userData); // 창고, 창고동, 설비

	/*************** MEQLOC : 설비-베드 매핑 ***************/
	// 설비-베드 매핑 조회
	List<MlocmaDTO> selectMlocmaForMapping(@Param("params") MlocmaDTO params, @Param("userData") UserVO userData);

	// 설비-베드 매핑 등록여부 확인
	int selectMeqlocCnt(@Param("params") MeqlocDTO params, @Param("userData") UserVO userData);

	// 설비-베드 매핑 등록
	int insertMeqloc(@Param("addData") MeqlocDTO addData, @Param("userData") UserVO userData);

	// 설비-베드 매핑 수정
	int updateMeqloc(@Param("updateData") MeqlocDTO updateData, @Param("userData") UserVO userData);

	// 베드 사용여부가 N인 경우
	int updateMeqlocUnuse(@Param("locakey") String locakey, @Param("userData") UserVO userData);

	// 귀속 설비 조회 (베드 API)
	String selectEquipkyByLoc(@Param("param") MlocmaDTO param, @Param("userData") UserVO userData);
	<T> String selectEquipkyByLoc(@Param("param") T param, @Param("userData") UserVO userData);

	// 설비 리스트 조회(셀렉트박스용)
	List<MeqlocDTO> selectEquipByLocForSelectBox(@Param("param") MlocmaDTO param, @Param("userData") UserVO userData);
}