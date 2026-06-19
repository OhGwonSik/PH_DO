package com.kbph.logistics.md.mapper;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kbph.logistics.md.domain.McodemDTO;
import com.kbph.logistics.md.domain.MrscmaDTO;
import com.kbph.logistics.sy.domain.UserVO;
@Mapper
public interface CodeMapper {
	/*************** MCODEM : 공통코드 마스터 ***************/
	// 공통코드 리스트 조회
	List<McodemDTO> selectMcodemList(@Param("param") McodemDTO params, @Param("userData") UserVO userData);

	// 공통코드 조회
	List<McodemDTO> selectMcodem(@Param("params") McodemDTO params, @Param("userData") UserVO userData);

	// 공통코드 단건 조회
	McodemDTO selectCode(@Param("param") McodemDTO params, @Param("userData") UserVO userData);

	// 공통코드 등록
	int insertMcodem(@Param("addData") McodemDTO addData, @Param("userData") UserVO userData);

	// 공통코드 수정
	int updateMcodem(@Param("updateData") McodemDTO updateData, @Param("userData") UserVO userData);

	// 공통코드 셀렉트박스 조회
	List<McodemDTO> selectMcodemSelectBox(@Param("params") McodemDTO params, @Param("userData") UserVO userData);

	/*************** MRSCMA : 사유코드 마스터 ***************/
	// 사유코드 리스트 조회
	List<MrscmaDTO> selectMrscmaList(@Param("params") MrscmaDTO params, @Param("userData") UserVO userData);

	// 사유코드 등록
	int insertMrscma(@Param("addData") MrscmaDTO addData, @Param("userData") UserVO userData);

	// 사유코드 수정
	int updateMrscma(@Param("updateData") MrscmaDTO updateData, @Param("userData") UserVO userData);

	// 사유코드 셀렉트박스 조회
	List<MrscmaDTO> selectMrscmaSelectBox(@Param("params") MrscmaDTO params, @Param("userData") UserVO userData);

	// 사유코드 관계데이터 조회
	public List<MrscmaDTO> selectMrscmaRelationList(@Param("param") MrscmaDTO params, @Param("userData") UserVO userData);
}