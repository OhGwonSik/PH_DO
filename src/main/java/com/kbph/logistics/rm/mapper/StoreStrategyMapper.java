package com.kbph.logistics.rm.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kbph.logistics.im.domain.WasnitDTO;
import com.kbph.logistics.rm.domain.LocationForStrategyDTO;
import com.kbph.logistics.rm.domain.WpaordDTO;
import com.kbph.logistics.rm.domain.WpashdDTO;
import com.kbph.logistics.rm.domain.WpasitDTO;
import com.kbph.logistics.rm.domain.WpasmaDTO;
import com.kbph.logistics.sy.domain.UserVO;
/**
 * @author : t.s.park
 * @version : 1.0.0
 * @since : 2024-07-09
 * @note : Mapper Class for store strategy
 * ==================================================
 * DATE							AUTHOR            					NOTE
 * --------------------------------------------------------------------------------------
 * 2024-07-09					t.s.park        					create Mapper class
 * --------------------------------------------------------------------------------------
 * ==================================================
 */
@Mapper
public interface StoreStrategyMapper {
	// 적치전략마스터 조회
	List<WpasmaDTO> selectWpasmaList(@Param("param") WpasmaDTO param, @Param("userInfo") UserVO userInfo);
	// 적치전략마스터 생성
	int insertWpasmaList(@Param("addList") List<WpasmaDTO> addList, @Param("userInfo") UserVO userInfo);
	// 적치전략마스터 업데이트
	int updateWpasmaList(@Param("updateList") List<WpasmaDTO> updateList, @Param("userInfo") UserVO userInfo);

	// 적치전략헤더 조회
	List<WpashdDTO> selectWpashdList(@Param("param") WpashdDTO param, @Param("userInfo") UserVO userInfo);
	// 적치전략헤더 생성
	int insertWpashdList(@Param("addList") List<WpashdDTO> addList, @Param("userInfo") UserVO userInfo);
	// 적치전략헤더 업데이트
	int updateWpashdList(@Param("updateList") List<WpashdDTO> addList, @Param("userInfo") UserVO userInfo);

	// 적치전략아이템 조회
	List<WpasitDTO> selectWpasitList(@Param("param") WpasitDTO param, @Param("userInfo") UserVO userInfo);
	// 적치전략아이템 생성
	int insertWpasitList(@Param("addList") List<WpasitDTO> addList, @Param("userInfo") UserVO userInfo);
	// 적치전략아이템 업데이트
	int updateWpasitList(@Param("updateList") List<WpasitDTO> updateList, @Param("userInfo") UserVO userInfo);

	// 오더별적치전략 조회
	WpaordDTO selectWpaord(@Param("param") WpaordDTO param, @Param("userInfo") UserVO userInfo);
	// 오더별적치전략 리스트 조회
	List<WpaordDTO> selectWpaordList(@Param("param") WpaordDTO param, @Param("userInfo") UserVO userInfo);
	// 오더별적치전략 생성
	int insertWpaordList(@Param("addList") List<WpaordDTO> addList, @Param("userInfo") UserVO userInfo);
	// 오더별적치전략 업데이트
	int updateWpaordList(@Param("updateList") List<WpaordDTO> updateList, @Param("userInfo") UserVO userInfo);

	// 오더별 적치전략 아이템 포함 리스트 조회
	List<WpasitDTO> selectWpaordWithWpasit(@Param("param") WpaordDTO param, @Param("userInfo") UserVO userInfo);

	// 적치전략 적용된 베드 리스트 조회
	List<LocationForStrategyDTO> selectLocationListForStoreStrategy(@Param("param") WasnitDTO param, @Param("storeStrategy") List<WpasitDTO> storeStrategy, @Param("userInfo") UserVO userInfo);
}
