package com.kbph.logistics.md.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kbph.logistics.md.domain.MdocmaDTO;
import com.kbph.logistics.sy.domain.UserVO;

@Mapper
public interface DocumentMapper {
	/*************** MDOCMA : 문서타입 마스터 ***************/
	// 문서 유형 리스트 조회
	List<MdocmaDTO> selectMdocmaDoccateList(@Param("params") MdocmaDTO params, @Param("userData") UserVO userData);

	// 문서 관계 리스트 조회 (창고, 문서유형, 문서타입)
	List<MdocmaDTO> selectWareCateTypeRelations(@Param("params") MdocmaDTO params, @Param("userData") UserVO userData);

	// 문서 유형 셀렉트박스 조회
	List<MdocmaDTO> selectDoccateSelectBox(@Param("params") MdocmaDTO params, @Param("userData") UserVO userData);

	// 문서 타입 셀렉트박스 조회
	List<MdocmaDTO> selectDoctypeSelectBox(@Param("params") MdocmaDTO params, @Param("userData") UserVO userData);
}