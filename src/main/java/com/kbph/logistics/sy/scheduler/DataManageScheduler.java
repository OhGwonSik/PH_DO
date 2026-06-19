package com.kbph.logistics.sy.scheduler;

import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.kbph.logistics.common.util.DateUtil;
import com.kbph.logistics.sy.service.DataManageService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Profile("prod")
public class DataManageScheduler {

	private final DataManageService dataManageService;

	/*
	 * ALRMHI(앱 알람이력) clean 스케쥴러
	 * 주기 : 매일 아침 06시
	 * 삭제 대상 : 한 달 이상 이력
	*/
	@Scheduled(cron = "0 0 06 * * * ")
	public void alarmMonthlyCleanup() {
		dataManageService.alarmMonthlyCleanup();
	}

	/*
	 * WSTKKY(재고) => WSTKDY(일재고백업) 데이터 이관 스케쥴러
	 * 주기 : 매일 / 창고 마스터 내 일재고백업시간(STKDYTM)
	 * 이관 대상 : 주기 시점의 재고를 이관함.
	 * 삭제 대상 : 스케쥴러 실행 일자 3개월 이전의 데이터(01일, 15일, 마지막일은 제외) 삭제.
	*/

	@Scheduled(cron = "0 0 * * * * ")
	public void dailyStockBackup() {
		dataManageService.dailyStockBackup(DateUtil.getTime("HHmmss"));
	}
}
