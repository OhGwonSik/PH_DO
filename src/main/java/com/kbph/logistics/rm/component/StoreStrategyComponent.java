package com.kbph.logistics.rm.component;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.kbph.logistics.common.enums.Useyn;
import com.kbph.logistics.common.util.SecurityUtils;
import com.kbph.logistics.configuration.error.TaskProcessingException;
import com.kbph.logistics.im.domain.WasnitDTO;
import com.kbph.logistics.md.type.DoctypeInbound;
import com.kbph.logistics.rm.domain.LocationForStrategyDTO;
import com.kbph.logistics.rm.domain.WpaordDTO;
import com.kbph.logistics.rm.domain.WpasitDTO;
import com.kbph.logistics.rm.mapper.StoreStrategyMapper;

import io.jsonwebtoken.lang.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
/**
 * @author : t.s.park
 * @version : 1.0.0
 * @since : 2024-08-01
 * @note : StoreStrategyComponent
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
public class StoreStrategyComponent {
	private final StoreStrategyMapper storeStrategyMapper;

	// 해당 문서의 적치 전략 조회
	public List<WpasitDTO> getStoreStrategeForOrder(WpaordDTO wpaordDTO){
		return (wpaordDTO == null) ? Collections.emptyList() : storeStrategyMapper.selectWpaordWithWpasit(wpaordDTO, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	// 적치 전략에 따른 베드 리스트 조회
	// 가중치 및 필수조건필드 계산
	// 가중치는 말 그대로 적치전략 가중치(1~10으로 지정? / 지금은 제한 x)
	// 필수조건yn은 조건이 맞지 않으면 아예 리스트 제외
	//	INBOPRI // 입고 우선 베드 여부(가중치)
	// 최대한 비어있는 배드(양이 적은 배드 / 우선순위 / 재고카운트) // 현재 작업중이지 않은 배드(우선순위 / 작업 중인 리스트 개수 조회))
	public WasnitDTO getTargetLocationListWithStoreStrategy(WasnitDTO asn) {
		WpaordDTO wpaordParam = new WpaordDTO();
		wpaordParam.setWarekey(asn.getWarekey());
		wpaordParam.setDoccate(asn.getDoccate());
		wpaordParam.setDoctype(asn.getDoctype()); // asn의 오더번호 세팅
		List<WpasitDTO> storeStrategy = getStoreStrategeForOrder(wpaordParam); // 오더의 적치전략 리스트 저장용
		if(!CollectionUtils.isEmpty(storeStrategy)) {
			// 로그용
			WpasitDTO storeStrategyInfo = storeStrategy.get(0);
			String strategyDoctype = storeStrategyInfo.getDoctype();
			log.info("storeStrategy \n 대상창고={} 문서타입={} 전략키={}", storeStrategyInfo.getWarekey(), strategyDoctype, storeStrategyInfo.getPastrky());

			// 전략별로 다르게 처리해야 한다면 분기 처리(큰수레 엑셀업로드 전략 추가)
			if(DoctypeInbound.POSCO_STRATEGY.getCode().equals(strategyDoctype) || DoctypeInbound.TOLL_STRATEGY.getCode().equals(strategyDoctype) || DoctypeInbound.OUTSIDE_STRATEGY.getCode().equals(strategyDoctype) || DoctypeInbound.EXCEL_STRATEGY.getCode().equals(strategyDoctype)) {
				List<LocationForStrategyDTO> locationListWithStoreStrategy = storeStrategyMapper.selectLocationListForStoreStrategy(asn, storeStrategy, SecurityUtils.getCustomUserDetails().getUserInfo());
				if(!locationListWithStoreStrategy.isEmpty()) {
					LocationForStrategyDTO highScoreLocation = locationListWithStoreStrategy.get(0);
					asn.setToareky(highScoreLocation.getAreakey());
					asn.setToarenm(highScoreLocation.getAreanam());
					asn.setTolocky(highScoreLocation.getLocakey());
					asn.setTolocnm(highScoreLocation.getLocanam());
					asn.setEquipky(highScoreLocation.getEquipky());
					asn.setIsStrategy(Useyn.USE.getString());
					asn.setUllocky(highScoreLocation.getUllocky());
					asn.setUllocnm(highScoreLocation.getUllocnm());

					return asn;
				}

				throw new TaskProcessingException("전략에 맞는 결과를 찾을 수 없습니다.");
			}
		}

		throw new TaskProcessingException("해당 입고예정정보에 맞는 전략을 찾을 수 없습니다.");
	}

	// 적치 전략에 따른 입고예정정보의 To정보 세팅
	public List<WasnitDTO> setAsnLocationWithStoreStrategy(List<WasnitDTO> asnItemList) {
		if(asnItemList == null) {
			throw new TaskProcessingException("ASN 데이터를 찾을 수 없습니다.");
		}

		for(int idx=0; idx<asnItemList.size(); idx++) {
			asnItemList.set(idx, getTargetLocationListWithStoreStrategy(asnItemList.get(idx)));
		}

		List<WasnitDTO> sortedAsnList = asnItemList.stream()
																		.sorted(
																				Comparator.comparing(WasnitDTO::getWarekey)
																								.thenComparing(WasnitDTO::getToareky)
																								.thenComparing(WasnitDTO::getTolocky)
																								.thenComparing(WasnitDTO::getStlayer)
																								.reversed()
																		)
																		.toList();

		// 단수 계산
		int layerCnt = 0;
		int startIdx=0;
		while(startIdx<sortedAsnList.size()) {
			WasnitDTO startAsn = sortedAsnList.get(startIdx);

			for(int endIdx=startIdx; endIdx<sortedAsnList.size(); endIdx++) {
				startIdx++;
				WasnitDTO endAsn = sortedAsnList.get(endIdx);
				boolean isSameLocation = startAsn.getWarekey().equals(endAsn.getWarekey())
													&& startAsn.getToareky().equals(endAsn.getToareky())
													&& startAsn.getTolocky().equals(endAsn.getTolocky());

				if(isSameLocation) {
					sortedAsnList.get(endIdx).setTolayer(++layerCnt);
				} else {
					layerCnt = 1;
					sortedAsnList.get(endIdx).setTolayer(layerCnt);

					break;
				}
			}
		}
		return sortedAsnList.stream()
									.sorted(Comparator.comparing(WasnitDTO::getStlayer).reversed())
									.toList();
	}
}
