package com.kbph.logistics.rm.component;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.kbph.logistics.common.util.SecurityUtils;
import com.kbph.logistics.om.domain.OoubhdDTO;
import com.kbph.logistics.rm.domain.WalordDTO;
import com.kbph.logistics.rm.domain.WalsitDTO;
import com.kbph.logistics.rm.mapper.AllocationStrategyMapper;
import com.kbph.logistics.sm.domain.WstkkyDTO;

import io.jsonwebtoken.lang.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
/**
 * @author : t.s.park
 * @version : 1.0.0
 * @since : 2024-08-01
 * @note : AllocationStrategyComponent
 * ==================================================
 * DATE							AUTHOR            					NOTE
 * --------------------------------------------------------------------------------------
 * 2024-08-01					t.s.park        					create Component class
 * --------------------------------------------------------------------------------------
 * ==================================================
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AllocationStrategyComponent {
	private final AllocationStrategyMapper allocationStrategyMapper;

	// 해당 문서의 할당 전략 조회
	public List<WalsitDTO> getAllocationStrategeForOrder(WalordDTO walordDTO){
		return (walordDTO == null) ? Collections.emptyList() : allocationStrategyMapper.selectWalordWithWalsit(walordDTO, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	// 최대한 비어있는 배드(양이 적은 배드 / 우선순위 / 재고카운트) // 현재 작업중이지 않은 배드(우선순위 / 작업 중인 리스트 개수 조회(입고예정시간 이전시간))
	// 가중치 및 필수조건필드 계산
	// 가중치는 말 그대로 적치전략 가중치(범위 미정)
	// 필수조건yn은 조건이 맞지 않으면 아예 배제하겠다는 걸로
	// 무게 계산해서 할당 해주기 // 재고가 모자라면 거기서 끝
	//	OUBOPRI // 출고 우선 베드 여부(가중치)
	// 할당 전략에 따른 재고 리스트 조회
	// 인터페이스 관련 이슈로 전략 대폭 축소
	public List<WstkkyDTO> getStockListWithAllocationStrategy(OoubhdDTO ooubhdDTO) {
		WalordDTO walordParam = new WalordDTO();
		walordParam.setWarekey(ooubhdDTO.getWarekey());
		walordParam.setDoccate(ooubhdDTO.getDoccate());
		walordParam.setDoctype(ooubhdDTO.getDoctype());
		List<WalsitDTO> allocationStrategy = getAllocationStrategeForOrder(walordParam); // 오더의 적치전략 리스트 저장용
		List<WstkkyDTO> stockListWithAllocationStrategy = new ArrayList<>();
		if(!CollectionUtils.isEmpty(allocationStrategy)) {
			WalsitDTO allocationStrategyInfo = allocationStrategy.get(0);
			String strategyDoctype = allocationStrategyInfo.getDoctype();
			// 전략별로 다르게 처리해야 한다면 분기 처리
			stockListWithAllocationStrategy = allocationStrategyMapper.selectStockListForAllocationStrategy(ooubhdDTO, allocationStrategy, SecurityUtils.getCustomUserDetails().getUserInfo());
		}

		return stockListWithAllocationStrategy;
	}

	// 할당 전략에 따른 출고오더 제품 세팅
	public List<WstkkyDTO> setOutboundSkuWithAllocationStrategy(OoubhdDTO ooubhdDTO) {
		if(ooubhdDTO == null) {
			return Collections.emptyList();
		}

		return getStockListWithAllocationStrategy(ooubhdDTO);
	}
}
