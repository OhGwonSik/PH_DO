package com.kbph.logistics.md.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kbph.logistics.common.domain.ComboDataDTO;
import com.kbph.logistics.md.domain.McusmaDTO;
import com.kbph.logistics.md.domain.MdesmaDTO;
import com.kbph.logistics.md.domain.MowrmaDTO;
import com.kbph.logistics.md.domain.MregmaDTO;
import com.kbph.logistics.md.domain.MrscmaDTO;
import com.kbph.logistics.md.domain.MvhcmaDTO;
import com.kbph.logistics.md.domain.MvowmaDTO;
import com.kbph.logistics.sy.domain.UserVO;

@Mapper
public interface PartnerMapper {
	/*************** MVOWMA : 차주 마스터 ***************/
	// 차주 리스트 조회
	List<MvowmaDTO> selectMvowmaList(@Param("params") MvowmaDTO params, @Param("userData") UserVO userData);

	// 차주키 채번
	String selectVownkey(@Param("userData") UserVO userData);

	// 차주 등록
	int insertMvowma(@Param("addData") MvowmaDTO addData, @Param("userData") UserVO userData);

	// 차주 수정
	int updateMvowma(@Param("updateData") MvowmaDTO updateData, @Param("userData") UserVO userData);

	// 차주 공용여부 관련 칼럼 수정
	int updateMvowmaPublicStat(@Param("updateData") MvowmaDTO updateData, @Param("userData") UserVO userData);

	// 차주 사용자 등록 플래그 수정
	int updateMvowmaSign(@Param("updateData") MvowmaDTO updateData, @Param("userData") UserVO userData);

	// 차주 셀렉트박스 조회
	List<ComboDataDTO> selectMvowmaSelectBox(@Param("params") MvowmaDTO params, @Param("userData") UserVO userData);

	/*************** MVHCMA : 차량 마스터 ***************/
	// 차량 리스트 조회
	List<MvhcmaDTO> selectMvhcmaList(@Param("params") MvhcmaDTO params, @Param("userData") UserVO userData);

	// 차량키 채번
	String selectVehicky(@Param("userData") UserVO userData);

	// 차량 등록
	int insertMvhcma(@Param("addData") MvhcmaDTO addData, @Param("userData") UserVO userData);

	// 차량 수정
	int updateMvhcma(@Param("updateData") MvhcmaDTO updateData, @Param("userData") UserVO userData);

	// 차량 셀렉트박스 조회
	List<ComboDataDTO> selectMvhcmaSelectBox(@Param("params") MvhcmaDTO params, @Param("userData") UserVO userData);

	// 차주-차량 관계리스트 조회
	List<MrscmaDTO> selectVehicleRelationList(@Param("param") MvhcmaDTO params, @Param("userData") UserVO userData);

	//차량 IFPRSTS 상태값 UPDATE
	int updateMvhcmaIfprsts(@Param("updateData") MvhcmaDTO updateData, @Param("userData") UserVO userData);

	// 차량 공용여부 관련 칼럼 수정
	int updateMvhcmaPublicStat(@Param("updateData") MvhcmaDTO updateData, @Param("userData") UserVO userData);

	// 차량번호 개수 조회
	int selectVhcfnamCnt(@Param("param") MvhcmaDTO param, @Param("userData") UserVO userData);

	// update check
	MvhcmaDTO selectOneMvhcma(@Param("param") MvhcmaDTO param, @Param("userData") UserVO userData);

	/*************** MREGMA : 목적지(권역) 마스터 ***************/
	// 목적지 리스트 조회
	List<MregmaDTO> selectMregmaList(@Param("params") MregmaDTO params, @Param("userData") UserVO userData);

	// 목적지 등록
	int insertMregma(@Param("addData") MregmaDTO addData, @Param("userData") UserVO userData);

	// 목적지 수정
	int updateMregma(@Param("updateData") MregmaDTO updateData, @Param("userData") UserVO userData);

	// 목적지 셀렉트박스 조회
	List<ComboDataDTO> selectMregmaSelectBox(@Param("params") MregmaDTO params, @Param("userData") UserVO userData);

	// 목적지 사용여부 조회
	String selectMregmaReuseyn(@Param("regikey") String regikey, @Param("userData") UserVO userInfo);

	/*************** MCUSMA : 고객 마스터 ***************/
	// 고객 리스트 조회
	List<McusmaDTO> selectMcusmaList(@Param("params") McusmaDTO params, @Param("userData") UserVO userData);

	// 고객 등록
	int insertMcusma(@Param("addData") McusmaDTO addData, @Param("userData") UserVO userData);

	// 고객 수정
	int updateMcusma(@Param("updateData") McusmaDTO updateData, @Param("userData") UserVO userData);

	// 고객 셀렉트박스 조회
	List<ComboDataDTO> selectMcusmaSelectBox(@Param("params") McusmaDTO params, @Param("userData") UserVO userData);

	// 고객 - 목적지 관계리스트 조회
	List<MdesmaDTO> selectRegistrationRelationList(@Param("param") MdesmaDTO params, @Param("userData") UserVO userInfo);

	/*************** MDESMA : 상세착지 마스터 ***************/
	// 상세착지 리스트 조회
	List<MdesmaDTO> selectMdesmaList(@Param("params") MdesmaDTO params, @Param("userData") UserVO userData);

	// 상세착지 등록
	int insertMdesma(@Param("addData") MdesmaDTO addData, @Param("userData") UserVO userData);

	// 상세착지 수정
	int updateMdesma(@Param("updateData") MdesmaDTO updateData, @Param("userData") UserVO userData);

	// 상세착지 셀렉트박스 조회
	List<ComboDataDTO> selectMdesmaSelectBox(@Param("params") MdesmaDTO params, @Param("userData") UserVO userData);

	// 고객 - 목적지 - 상세착지 관계리스트 조회
	public List<MdesmaDTO> selectDestinationRelationList(@Param("param") MdesmaDTO params, @Param("userData") UserVO userData);

	/*************** MOWRMA : 판매계약사 마스터 (보류) ***************/
	// 판매계약사 리스트 조회
	List<MowrmaDTO> selectMowrmaList(@Param("params") MowrmaDTO params, @Param("userData") UserVO userData);

	// 판매계약사 등록
	int insertMowrma(@Param("addData") MowrmaDTO addData, @Param("userData") UserVO userData);

	// 판매계약사 수정
	int updateMowrma(@Param("updateData") MowrmaDTO updateData, @Param("userData") UserVO userData);

	// 판매계약사 셀렉트박스 조회
	List<ComboDataDTO> selectMowrmaSelectBox(@Param("params") MowrmaDTO params, @Param("userData") UserVO userData);
}