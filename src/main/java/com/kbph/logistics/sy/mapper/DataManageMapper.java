package com.kbph.logistics.sy.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kbph.logistics.md.domain.MwarmaDTO;
import com.kbph.logistics.sy.domain.SpllogVO;

@Mapper
public interface DataManageMapper {

	int alarmMonthlyCleanup(@Param("schema") String schema);
	List<MwarmaDTO> selectStockDailyTime(@Param("schema") String schema);
	int stockQuarterlyCleanup(@Param("param") MwarmaDTO mwarma, @Param("schema") String schema);
	int insertStockDailyBackup(@Param("param") MwarmaDTO mwarma, @Param("schema") String schema);
	int insertSpllog(@Param("param") SpllogVO spllog, @Param("schema") String schema);
}
