package com.kbph.logistics.sy.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kbph.logistics.common.constant.Constants.ApplicationInfo;
import com.kbph.logistics.md.domain.MwarmaDTO;
import com.kbph.logistics.sy.domain.SchemaDTO;
import com.kbph.logistics.sy.domain.SpllogVO;
import com.kbph.logistics.sy.mapper.DataManageMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataManageService {

	private final DataManageMapper dataManageMapper;
	private final UserService userService;

	public void alarmMonthlyCleanup() {
		List<SchemaDTO> schemaList = userService.getSchemaList(ApplicationInfo.APPLICATION_NAME);
		for(SchemaDTO schema : schemaList) {
			int result = dataManageMapper.alarmMonthlyCleanup(schema.getSchemaName());
			log.info("alarm history cleansing => schema: {}, datacnt: {}", schema.getSchemaName(), result);
			
			SpllogVO spllog = new SpllogVO();
			spllog.setPllogmg("알람 내역 테이블 clean 데이터 건 수 = " + result + "건");
			dataManageMapper.insertSpllog(spllog, schema.getSchemaName());
		}
	}
	
	public void dailyStockBackup(String scheduleTime) {
		List<SchemaDTO> schemaList = userService.getSchemaList(ApplicationInfo.APPLICATION_NAME);
		for(SchemaDTO schema : schemaList) {
			String schemaName = schema.getSchemaName();
			List<MwarmaDTO> mwarmaList = dataManageMapper.selectStockDailyTime(schemaName);
			for(MwarmaDTO mwarma : mwarmaList) {
				if(!scheduleTime.equals(mwarma.getStkdytm())) {
					continue;
				}
				// 3개월 이상 데이터 clean. 해당 삭제 대상 월간 데이터 내의 01일, 15일, 마지막일자는 제외. 
				int cleanupCnt = dataManageMapper.stockQuarterlyCleanup(mwarma, schemaName);
				int backupCnt = dataManageMapper.insertStockDailyBackup(mwarma, schemaName);

				log.info("dailyStock backup => schema: {}, warekey: {}, cleanupCnt: {}, backupCnt: {}", schema.getSchemaName(), mwarma.getWarekey(), cleanupCnt, backupCnt);
				SpllogVO spllog = new SpllogVO();
				spllog.setPllogmg("일 재고 백업 실행 창고키 = " + mwarma.getWarekey() + " clean 데이터 = " + cleanupCnt + "건 // 삽입 데이터 = " + backupCnt + "건");
				dataManageMapper.insertSpllog(spllog, schemaName);
			}
		}
	}
}
