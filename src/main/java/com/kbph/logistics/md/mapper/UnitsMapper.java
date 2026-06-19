package com.kbph.logistics.md.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kbph.logistics.common.domain.ComboDataDTO;
import com.kbph.logistics.md.domain.MgrpmaDTO;
import com.kbph.logistics.md.domain.MskuwcDTO;
import com.kbph.logistics.md.domain.MuommaDTO;
import com.kbph.logistics.sy.domain.UserVO;

@Mapper
public interface UnitsMapper {
	/*************** MSKUWC : 제품 마스터 ***************/
	// 제품 리스트 조회
	List<MskuwcDTO> selectMskuwcList(@Param("params") MskuwcDTO params, @Param("userData") UserVO userData);

	// 제품 등록
	int insertMskuwc(@Param("addData") MskuwcDTO addData, @Param("userData") UserVO userData);

	// 제품 수정
	int updateMskuwc(@Param("updateData") MskuwcDTO updateData, @Param("userData") UserVO userData);

	// 제품 셀렉트박스 조회
	List<ComboDataDTO> selectMskuwcSelectBox(@Param("params") MskuwcDTO params, @Param("userData") UserVO userData);

	/*************** MGRPMA : 제품그룹 마스터 ***************/
	// 제품그룹 리스트 조회
	List<MgrpmaDTO> selectMgrpmaList(@Param("params") MgrpmaDTO params, @Param("userData") UserVO userData);

	// 제품그룹 등록
	String selectSkugrky(@Param("userData") UserVO userData); // 제품그룹 키 채번
	int insertMgrpma(@Param("addData") MgrpmaDTO addData, @Param("userData") UserVO userData);

	// 제품그룹 수정
	int updateMgrpma(@Param("updateData") MgrpmaDTO updateData, @Param("userData") UserVO userData);

	// 제품그룹 셀렉트박스 조회
	List<ComboDataDTO> selectMgrpmaSelectBox(@Param("params") MgrpmaDTO params, @Param("userData") UserVO userData);

	/*************** MUOMMA : 단위 마스터 ***************/
	// 단위 셀렉트박스 조회
	List<ComboDataDTO> selectMuommaSelectBox(@Param("params") MuommaDTO params, @Param("userData") UserVO userData);
}
