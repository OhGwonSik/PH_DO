package com.kbph.logistics.rm.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kbph.logistics.om.domain.OoubhdDTO;
import com.kbph.logistics.rm.domain.WalordDTO;
import com.kbph.logistics.rm.domain.WalshdDTO;
import com.kbph.logistics.rm.domain.WalsitDTO;
import com.kbph.logistics.rm.domain.WalsmaDTO;
import com.kbph.logistics.sm.domain.WstkkyDTO;
import com.kbph.logistics.sy.domain.UserVO;
/**
 * @author : t.s.park
 * @version : 1.0.0
 * @since : 2024-07-09
 * @note : Mapper Class for allocation strategy
 * ==================================================
 * DATE							AUTHOR            					NOTE
 * --------------------------------------------------------------------------------------
 * 2024-07-09					t.s.park        					create Mapper class
 * 2024-07-15					t.s.park        					create Mapper method
 * --------------------------------------------------------------------------------------
 * ==================================================
 */
@Mapper
public interface AllocationStrategyMapper {
	// 할당전략마스터 조회
	List<WalsmaDTO> selectWalsmaList(@Param("param") WalsmaDTO param, @Param("userInfo") UserVO userInfo);
	// 할당전략마스터 생성
	int insertWalsmaList(@Param("addList") List<WalsmaDTO> addList, @Param("userInfo") UserVO userInfo);
	// 할당전략마스터 업데이트
	int updateWalsmaList(@Param("updateList") List<WalsmaDTO> updateList, @Param("userInfo") UserVO userInfo);

	// 할당전략헤더 조회
	List<WalshdDTO> selectWalshdList(@Param("param") WalshdDTO param, @Param("userInfo") UserVO userInfo);
	// 할당전략헤더 생성
	int insertWalshdList(@Param("addList") List<WalshdDTO> addList, @Param("userInfo") UserVO userInfo);
	// 할당전략헤더 업데이트
	int updateWalshdList(@Param("updateList") List<WalshdDTO> addList, @Param("userInfo") UserVO userInfo);

	// 할당전략아이템 조회
	List<WalsitDTO> selectWalsitList(@Param("param") WalsitDTO param, @Param("userInfo") UserVO userInfo);
	// 할당전략아이템 생성
	int insertWalsitList(@Param("addList") List<WalsitDTO> addList, @Param("userInfo") UserVO userInfo);
	// 할당전략아이템 업데이트
	int updateWalsitList(@Param("updateList") List<WalsitDTO> updateList, @Param("userInfo") UserVO userInfo);

	// 오더별할당전략 조회
	List<WalordDTO> selectWalordList(@Param("param") WalordDTO param, @Param("userInfo") UserVO userInfo);
	// 오더별할당전략 생성
	int insertWalordList(@Param("addList") List<WalordDTO> addList, @Param("userInfo") UserVO userInfo);
	// 오더별할당전략 업데이트
	int updateWalordList(@Param("updateList") List<WalordDTO> updateList, @Param("userInfo") UserVO userInfo);

	// 오더별 할당전략 아이템 포함 리스트 조회
	List<WalsitDTO> selectWalordWithWalsit(@Param("param") WalordDTO param, @Param("userInfo") UserVO userInfo);

	// 할당전략 적용된 베드 리스트와 할당전략 리스트를 바탕으로 재고 리스트업
	List<WstkkyDTO> selectStockListForAllocationStrategy(@Param("order") OoubhdDTO ooubhdDTO, @Param("allocationStrategy") List<WalsitDTO> allocationStrategy, @Param("userInfo") UserVO userInfo);
}
