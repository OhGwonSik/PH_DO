package com.kbph.logistics.sm.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kbph.logistics.md.domain.MdesmaDTO;
import com.kbph.logistics.md.domain.MskuwcDTO;
import com.kbph.logistics.om.domain.WshpitDTO;
import com.kbph.logistics.sm.domain.WadjitDTO;
import com.kbph.logistics.sm.domain.WphyitDTO;
import com.kbph.logistics.sm.domain.WstkkyDTO;
import com.kbph.logistics.sm.domain.WtakitDTO;
import com.kbph.logistics.sy.domain.UserVO;

@Mapper
public interface InventoryMapper {
	//로케이션 재고
	public List<WstkkyDTO> getFromLocationStockInfo(@Param("param") WstkkyDTO wstkkyDTO, @Param("userData") UserVO userData);
	public List<WstkkyDTO> getToLocationStockInfo(@Param("param") WstkkyDTO wstkkyDTO, @Param("userData") UserVO userData);
	public List<WstkkyDTO> getLocationStockInfo(@Param("param") WstkkyDTO wstkkyDTO, @Param("userData") UserVO userData);
	public int updateStockLayerSort(@Param("param") WstkkyDTO wstkkyDTO, @Param("userData") UserVO userData);
	//설비키
	public String getEquipky(@Param("param") WstkkyDTO wstkkyDTO, @Param("userData") UserVO userData);

	//단 정보 가져오기
	public WstkkyDTO getDoe035Layer(@Param("param") WstkkyDTO wstkkyDTO, @Param("userData") UserVO userData);
	//선별지시할 재고리스트 조회
	public int checkDoe035StockVail(@Param("param") WstkkyDTO wstkkyDTO, @Param("userData") UserVO userData);
	public int checkDoe035AboveStockExist(@Param("param") WstkkyDTO wstkkyDTO, @Param("userData") UserVO userData);
	public int checkDoe035AboveTaskExist(@Param("param") WstkkyDTO wstkkyDTO, @Param("userData") UserVO userData);
	public int checkDoe035StockAboveOutExist(@Param("param") WstkkyDTO wstkkyDTO, @Param("userData") UserVO userData);
	public List<WstkkyDTO> getDoe035WstkkyList(@Param("param") WstkkyDTO wstkkyDTO, @Param("userData") UserVO userData);
	public List<WstkkyDTO> getWstkkyList(@Param("param") WstkkyDTO wstkkyDTO, @Param("userData") UserVO userData);
	// doedy035 선별지시
	public List<WstkkyDTO> getDoeDy035WstkkyList(@Param("param") WstkkyDTO wstkkyDTO, @Param("userData") UserVO userData);

	//TASKOKY 채번
	public String selectTaskoky(@Param("userData") UserVO userData);
	//TASKOIT 채번
	public Integer selectTaskoit(@Param("taskoky") String taskoky, @Param("userData") UserVO userData);

	//선별지시
	public int saveDoe035List(@Param("param") WstkkyDTO wstkkyDTO, @Param("userData") UserVO userData);
	public int updateTaskToSallqty(@Param("param") WstkkyDTO wstkkyDTO, @Param("userData") UserVO userData);
	public int updateTasksts(@Param("param") WstkkyDTO wstkkyDTO, @Param("userData") UserVO userData);
	public List<WstkkyDTO> checkLayerExists(@Param("param") WstkkyDTO wstkkyDTO, @Param("userData") UserVO userData);

	//선별정보등록
	public List<WtakitDTO> getDoe036HeadList(@Param("param") WtakitDTO wtakitDTO, @Param("userData") UserVO userData);
	public List<WtakitDTO> getDoe036ItemList(@Param("param") WtakitDTO wtakitDTO, @Param("userData") UserVO userData);
	public List<WtakitDTO> getSelectionBeforeLocaList(@Param("param") WtakitDTO wtakitDTO, @Param("userData") UserVO userData);
	public int updateSelectionBeforeLoca(@Param("param") WtakitDTO wtakitDTO, @Param("userData") UserVO userData);
	public int updateDoe036WtakitOperStart(@Param("param") WtakitDTO wtakitDTO, @Param("userData") UserVO userData);
	public int updateDoe036Wtakit(@Param("param") WtakitDTO wtakitDTO, @Param("userData") UserVO userData);
	public int updateDoe036WtakitOperCmp(@Param("param") WtakitDTO wtakitDTO, @Param("userData") UserVO userData);
	public List<WtakitDTO> getDoe036WtakitList(@Param("param") WtakitDTO wtakitDTO, @Param("userData") UserVO userData);
	public List<WtakitDTO> getDoe036WtakitTaskstsList(@Param("param") WtakitDTO wtakitDTO, @Param("userData") UserVO userData);
	public List<WtakitDTO> getDoe036StockInfos(@Param("param") WtakitDTO wtakitDTO, @Param("userData") UserVO userData);

	//선별 확정
	public int getDoe037StatusCnt(@Param("param") WtakitDTO wtakitDTO, @Param("userData") UserVO userData);
	public int updateDoe037CancelWstkky(@Param("param") WtakitDTO wtakitDTO, @Param("userData") UserVO userData);
	public int saveDoe037Cancel(@Param("param") WtakitDTO wtakitDTO, @Param("userData") UserVO userData);
	public <T> WstkkyDTO hasStockToLoc(@Param("param") T paramDTO, @Param("userData") UserVO userData);
	public List<WtakitDTO> getDoe037WtakitList(@Param("param") WtakitDTO wtakitDTO, @Param("userData") UserVO userData);
	public <T> WstkkyDTO hasStock(@Param("param") T paramDTO, @Param("userData") UserVO userData);
	public int updateDoe037Wstkky(@Param("param") WtakitDTO wtakitDTO, @Param("userData") UserVO userData);
	public int updateDoe037Wtakit(@Param("param") WtakitDTO wtakitDTO, @Param("userData") UserVO userData);

	//재고실사지시
	public String selectPhysoky(@Param("param") WstkkyDTO wstkkyDTO, @Param("userData") UserVO userData);
	public <T> Integer selectPhysoit(@Param("param") T paramDTO, @Param("userData") UserVO userData);
	public String selectPhygrky(@Param("param") WstkkyDTO wstkkyDTO, @Param("userData") UserVO userData);
	public int saveDoe038List(@Param("param") WstkkyDTO wstkkyDTO, @Param("userData") UserVO userData);
	public int updatePhyToSallqty(@Param("param") WstkkyDTO wstkkyDTO, @Param("userData") UserVO userData);

	//재고실사등록
	public List<WphyitDTO> getDoe039HeadList(@Param("param") WphyitDTO wphyitDTO, @Param("userData") UserVO userData);
	public List<WphyitDTO> getDoe039ItemStatusList(@Param("param") WphyitDTO wphyitDTO, @Param("userData") UserVO userData);
	public List<WphyitDTO> getDoe039ItemList(@Param("param") WphyitDTO wphyitDTO, @Param("userData") UserVO userData);
	public WphyitDTO getStkLocationAndLayer(@Param("param") WphyitDTO wphyitDTO, @Param("userData") UserVO userData);
	public int saveDoe039List(@Param("param") WphyitDTO wphyitDTO, @Param("userData") UserVO userData);
	public List<WphyitDTO> getBeforeAfterData(@Param("param") WphyitDTO wphyitDTO, @Param("userData") UserVO userData);
	public int updatePhystat(@Param("param") WphyitDTO wphyitDTO, @Param("userData") UserVO userData);

	//재고실사확정
	public WstkkyDTO doe040HasStock(@Param("param") WphyitDTO wphyitDTO, @Param("userData") UserVO userData);
	public WstkkyDTO hasPlaceStock(@Param("param") WphyitDTO wphyitDTO, @Param("userData") UserVO userData);
	public List<WstkkyDTO> getSamePlaceStock(@Param("param") WphyitDTO wphyitDTO, @Param("userData") UserVO userData);
	public int updateLayerSort(@Param("param") WstkkyDTO wstkkyDTO, @Param("userData") UserVO userData);
	public int updateSamePlaceStock(@Param("param") WstkkyDTO wstkkyDTO, @Param("userData") UserVO userData);
	public int updateDoe040Wtakit(@Param("param") WstkkyDTO wstkkyDTO, @Param("userData") UserVO userData);
	public int updateDoe040Wstkky(@Param("param") WphyitDTO wphyitDTO, @Param("userData") UserVO userData);
	public int updateDoe040Wphyit(@Param("param") WphyitDTO wphyitDTO, @Param("userData") UserVO userData);
	public <T> MskuwcDTO getSkuInfo(@Param("param") T paramDTO, @Param("userData") UserVO userData);
	public <T> int updateDoe040Mskuwc(@Param("param") T paramDTO, @Param("userData") UserVO userData);
	public List<WphyitDTO> getWpyitApi(@Param("param") WphyitDTO wphyitDTO, @Param("userData") UserVO userData);
	//재고조정
	public String selectAdjsoky(@Param("userData") UserVO userData);
	public Integer selectAdjsoit(@Param("param") WstkkyDTO wstkkyDTO, @Param("userData") UserVO userData);
	public String selectAdjgrky(@Param("userData") UserVO userData);
	public int insertWadjit(@Param("param") WstkkyDTO wstkkyDTO, @Param("userData") UserVO userData);
	public int updateDoe041Wstkky(@Param("param") WstkkyDTO wstkkyDTO, @Param("userData") UserVO userData);
	public int updateDoe041Adjistat(@Param("param") WstkkyDTO wstkkyDTO, @Param("userData") UserVO userData);
	public List<WadjitDTO> getWadjitApi(@Param("param")WadjitDTO wadjitDTO, @Param("userData") UserVO userVO);
	//재고블락
	public List<WstkkyDTO> getDoe042List(@Param("param") WstkkyDTO wstkkyDTO, @Param("userData") UserVO userData);
	public int updateDoe042Wstkky(@Param("param") WstkkyDTO wstkkyDTO, @Param("userData") UserVO userData);

	//재고블락해제
	public List<WstkkyDTO> getDoe043List(@Param("param") WstkkyDTO wstkkyDTO, @Param("userData") UserVO userData);

	// 작업리스트 조회(입고)
	public List<WtakitDTO> selectWtakitListForInbound(@Param("param") WtakitDTO wtakitDTO, @Param("userData") UserVO userData);

	// 작업리스트 조회(입고)
	public List<WtakitDTO> selectWtakitListForOutbound(@Param("param") WtakitDTO wtakitDTO, @Param("userData") UserVO userData);

	// 작업리스트 상태 업데이트(입고문서 기준)
	public int updateWtakitStatusToRcvdckyAndRcvdcit(@Param("param") WtakitDTO wtakitDTO, @Param("status") String status, @Param("userData") UserVO userData);
	// 작업리스트 상태 업데이트(작업문서 기준)

	// 작업리스트 상태 업데이트(출고문서 기준)
	public int updateWtakitStatusToShpdckyAndShpdcit(@Param("param") WtakitDTO wtakitDTO, @Param("status") String status, @Param("userData") UserVO userData);

	//재고조회
	public List<WstkkyDTO> getDoe089List(@Param("param") WstkkyDTO wstkkyDTO, @Param("userData") UserVO userData);

	public List<MdesmaDTO> getDoe041Denamlc(@Param("param") MdesmaDTO mdesmaDTO, @Param("userData") UserVO userData);
	public int updateDoe041ModalUpdate(@Param("param") WstkkyDTO wstkky, @Param("userInfo") UserVO user);

	// 재고 할당
	public int updateStockAllocate(@Param("param") WstkkyDTO wstkkyDTO, @Param("userData") UserVO userData);

	// 재고생성(송장수정 / 출고문서 기반)
	public int insertWstkkyForChangeInvoice(@Param("param") WshpitDTO wshpitDTO, @Param("userData") UserVO userData);
	// 재고 삭제(송장수정용)
	public int deleteWstkkyForChangeInvoice(@Param("param") WstkkyDTO wstkkyDTO, @Param("userData") UserVO userData);

	// 재고 삭제 (실사용)
	public int deleteWstkkyForPhysical(@Param("param") WphyitDTO wphyitDTO, @Param("userData") UserVO userData);
	public int deleteMskuwcForPhysical(@Param("param") WphyitDTO wphyitDTO, @Param("userData") UserVO userData);
	public int updateWrcvitForPhysical(@Param("param") WphyitDTO wphyitDTO, @Param("userData") UserVO userData);

	// 실사 후 제품 마스터 생성
	public int insertMskuwcForPhysical(@Param("param") WphyitDTO wphyitDTO, @Param("userData") UserVO userData);

	// 실사문서 생성(송장수정)
	public int insertWphyitForInvoiceChange(@Param("param") WphyitDTO wphyitDTO, @Param("userData") UserVO userData);

	// 작업문서리스트 조회(검증용)
	public List<WtakitDTO> selectWtakitListForValidation(@Param("param") WtakitDTO wtakitDTO, @Param("userData") UserVO userData);
}