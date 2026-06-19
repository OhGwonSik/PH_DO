package com.kbph.logistics.om.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kbph.logistics.md.domain.MeqlocDTO;
import com.kbph.logistics.md.domain.MlocmaDTO;
import com.kbph.logistics.om.domain.OoubhdDTO;
import com.kbph.logistics.om.domain.OoubitDTO;
import com.kbph.logistics.om.domain.OutboundOrderForTreeGridDTO;
import com.kbph.logistics.om.domain.TpwpwgDTO;
import com.kbph.logistics.om.domain.WplnhdDTO;
import com.kbph.logistics.om.domain.WplnitDTO;
import com.kbph.logistics.om.domain.WshphdDTO;
import com.kbph.logistics.om.domain.WshpitDTO;
import com.kbph.logistics.sm.domain.WstkkyDTO;
import com.kbph.logistics.sm.domain.WtakitDTO;
import com.kbph.logistics.sy.domain.UserVO;
import com.kbph.logistics.tm.domain.TmDispatchDTO;
import com.kbph.logistics.tm.domain.TplnhdDTO;
import com.kbph.logistics.tm.domain.TplnitDTO;

@Mapper
public interface OutboundMapper {
	// 출고예정번호 채번
	public String selectShpplky(@Param("userData") UserVO userVO);
	// 출고예정 아이템 번호 채번
	public Integer selectShpplit(@Param("param") OutboundOrderForTreeGridDTO outboundOrderForTreeGridDTO, @Param("userData") UserVO userVO);
	// 출고오더 번호 채번
	public String selectOutboky(@Param("userData") UserVO userVO);
	// 출고오더 아이템 번호 채번
	public Integer selectOutboit(@Param("param") OoubhdDTO ooubhdDTO, @Param("userData") UserVO userVO);
	// 출고문서 번호 채번
	public String selectShpdcky(@Param("userData") UserVO userVO);
	// 출고문서 아이템 번호 채번
	public Integer selectShpdcit(@Param("shpdcky") String shpdcky, @Param("userData") UserVO userVO);
	// 출고계획대상 재고리스트 조회
	public List<WstkkyDTO> selectStockListForShipPlan(@Param("param") WstkkyDTO wstkkyDTO, @Param("userData") UserVO userVO);
	// 출고대상확정 제품 추가 재고리스트 조회
	public List<WstkkyDTO> selectTargetStockListForAddStock(@Param("param") WstkkyDTO wstkkyDTO, @Param("userData") UserVO userVO);
	// 출고대상확정 제품 추가 재고리스트 조회(검증용)
	public List<WstkkyDTO> selectTargetStockListForValidation(@Param("stockList") List<WstkkyDTO> stockList, @Param("targetValue") OoubhdDTO ooubhdDTO, @Param("userData") UserVO userVO);
	// 조건에 맞는 재고조회(송장수정용)
	public List<WstkkyDTO> selectTargetStockListForChangeInvoice(@Param("param") WstkkyDTO wstkkyDTO, @Param("userData") UserVO userVO);
	// 재고조회
	public List<WstkkyDTO> doe045StockList(@Param("param") WstkkyDTO wstkkyDTO, @Param("userData") UserVO userVO);
	// 재고조회(송장수정용)
	public List<WstkkyDTO> selectStockListForAddStockToInvoice(@Param("param") WstkkyDTO wstkkyDTO, @Param("userData") UserVO userVO);
	// 타겟 위에 존재하는 재고조회(송장수정용)
	public List<WstkkyDTO> selectStockListForUpdate(@Param("param") WstkkyDTO wstkkyDTO, @Param("userData") UserVO userVO);
	public int saveDoe045Wplnhd(@Param("param") OutboundOrderForTreeGridDTO outboundOrderForTreeGridDTO, @Param("userData") UserVO userVO);
	public int saveDoe045Wplnit(@Param("param") OutboundOrderForTreeGridDTO outboundOrderForTreeGridDTO, @Param("stock") WstkkyDTO wstkkyDTO, @Param("userData") UserVO userVO);
	public int updateStockSallqty(@Param("param") OutboundOrderForTreeGridDTO outboundOrderForTreeGridDTO, @Param("userData") UserVO userVO);

	// 출고계획아이템 추가(출고대상 추가)
	public int insertWplnitForAddStock(@Param("param") WplnhdDTO wplnhdDTO, @Param("stockList") List<WstkkyDTO> stockList, @Param("userData") UserVO userVO);
	// 배차계획아이템 추가(출고대상 추가)
	public int insertTplnitForAddStock(@Param("param") TplnhdDTO tplnhdDTO, @Param("stockList") List<WstkkyDTO> stockList, @Param("userData") UserVO userVO);
	// 출고오더아이템 추가(출고대상 추가)
	public int insertOoubitForAddStock(@Param("param") OoubhdDTO ooubhdDTO, @Param("stockList") List<WstkkyDTO> stockList, @Param("userData") UserVO userVO);

	// 출고계획 헤더 조회(백단 검증용)
	public WplnhdDTO selectWplnhdListForVerification(@Param("param") WplnhdDTO wplnhdDTO, @Param("userData") UserVO userData);
	// 출고계획 아이템 조회(백단 검증용)
	public List<WplnitDTO> selectWplnitListForVerification(@Param("param") WplnitDTO wplnitDTO, @Param("userData") UserVO userData);
	// 출고계획 헤더 조회
	public List<WplnhdDTO>selectWplnhdList(@Param("param") WplnhdDTO wplnhdDTO, @Param("userData") UserVO userData);
	// 출고계획 아이템 조회
	public List<WplnitDTO>selectWplnitList(@Param("param") WplnitDTO wplnitDTO, @Param("userData") UserVO userData);
	// 배차된 출고계획 헤더 조회
	public List<WplnhdDTO>selectDispatchedWplnhdList(@Param("param") WplnhdDTO wplnhdDTO, @Param("userData") UserVO userData);
	// 배차된 출고계획 아이템 조회
	public List<TplnitDTO>selectDispatchedWplnitList(@Param("param") TplnhdDTO tplnhdDTO, @Param("userData") UserVO userData);
	// 출고예정변경 업데이트
	public int updateWplnhdForDispatch(@Param("updateList") List<WplnhdDTO> updateList, @Param("userData") UserVO userData);
	// 모든 무게가 배차할당된 출고예정헤더 리스트 조회
	public List<WplnhdDTO> selectFullDispatchedOutboundPlanHeaderList(@Param("param") WplnhdDTO wplnhdDTO, @Param("userData") UserVO userData);
	// 모든 무게가 배차할당된 출고예정아이템 리스트 조회
	public List<TplnitDTO> selectFullDispatchedOutboundPlanItemList(@Param("param") WplnhdDTO wplnhdDTO, @Param("userData") UserVO userData);
	// 출고오더의 배차별 출고계획 리스트 조회
	public List<OoubhdDTO> selectDoe053ModalHeadList(@Param("param") OoubhdDTO ooubhdDTO, @Param("userData") UserVO userVO);
	// 출고오더의 배차별 출고계획아이템 리스트 조회
	public List<TplnitDTO> selectDoe053ModalItemList(@Param("param") OoubhdDTO ooubhdDTO, @Param("userData") UserVO userVO);
	// 출고오더 조회(검증용)
	public OoubhdDTO selectOoubhdForVerification(@Param("param") OoubhdDTO ooubhdDTO, @Param("userData") UserVO userVO);
	// 출고오더 조회(검증용)
	public OoubhdDTO selectOoubhdForReselect(@Param("param") OoubhdDTO ooubhdDTO, @Param("userData") UserVO userVO);

	// 출고문서 헤더 조회(백단 검증용 / 헤더param)
	public WshphdDTO selectWshphdListForVerification(@Param("param") WshphdDTO wshphdDTO, @Param("userData") UserVO userData);
	// 출고문서 아이템 조회(백단 검증용 / 헤더param)
	public List<WshpitDTO> selectWshpitListForVerification(@Param("param") WshphdDTO wshphdDTO, @Param("userData") UserVO userData);
	// 출고계획 아이템 조회(백단 검증용/ item param)
	public WshpitDTO selectWshpitListForVerificationItem(@Param("param") WshpitDTO wshpitDTO, @Param("userData") UserVO userData);

	// 출고예정확정(플래그 업데이트)
	public int updateTpwpwgConfirmFlag(@Param("param") TpwpwgDTO tpwpwg, @Param("status") String status, @Param("userData") UserVO userData);
	// 출고예정헤더 상태 업데이트
	public int updateWplnhdStatus(@Param("param") WplnhdDTO param, @Param("status") String status, @Param("remweigCheck") boolean remweigCheck, @Param("userData") UserVO userVO);
	// 출고예정아이템 상태 업데이트
	public int updateWplnitStatus(@Param("param") WplnitDTO param, @Param("status") String status, @Param("userData") UserVO userVO);

	// 출고예정취소(배차계획 헤더)
	public int updateTransportPlanHeadToCancel(@Param("param") TplnitDTO tplnit, @Param("userData") UserVO userData);
	// 출고예정취소(배차계획 아이템)
	public int updateTransportPlanItemToCancel(@Param("param") TplnitDTO tplnit, @Param("userData") UserVO userData);
	// 출고예정취소 배차 아이템 모두 할당 해제
	public int updateStockAllocation(@Param("updateList") List<WplnitDTO> updateList, @Param("userData") UserVO userData);
	// 출고예정취소 무게 할당 삭제(배차계획 기준)
	public int deleteTpwpwgList(@Param("deleteList") List<WplnhdDTO> deleteList, @Param("userData") UserVO userData);
	// 출고예정취소(출고계획 아이템)
	public int updateShipPlanItemToCancel(@Param("param") WplnitDTO wplnit, @Param("userData") UserVO userData);
	// 출고예정취소(출고계획 헤더)
	public int updateShipPlanHeadToCancel(@Param("param") WplnhdDTO param, @Param("userData") UserVO userData);
	// 할당 업데이트
	public int updateWstkkyAlloc(@Param("param") WstkkyDTO wstkky, @Param("doAlloc") boolean doAlloc, @Param("doShipPlan") boolean doShipPlan, @Param("doShipPlanConf") boolean doShipPlanConf, @Param("userData") UserVO userData);
	// 블락 업데이트
	public int updateWstkkyBlock(@Param("updateList") List<WstkkyDTO> updateList, @Param("doBlock") boolean doBlock, @Param("userData") UserVO userData);

	public List<OoubhdDTO>getDoe049HeadList(@Param("param") OoubhdDTO ooubhdDTO, @Param("userData") UserVO userVO);
	public <T> List<OoubitDTO>getDoe049ItemList(@Param("param") T paramDTO, @Param("userData") UserVO userVO);
	public List<OoubhdDTO> selectOoubhdListForApi(@Param("param") OoubhdDTO ooubhdDTO, @Param("userData") UserVO userVO);
	public List<OoubitDTO> selectOoubitListForApi(@Param("param") OoubhdDTO ooubhdDTO, @Param("userData") UserVO userVO);
	public int saveDoe049Ooubhd(@Param("param") OoubhdDTO ooubhdDTO, @Param("userData") UserVO userVO);
	public int saveDoe049Ooubit(@Param("param") OoubitDTO ooubitDTO, @Param("stock") WstkkyDTO wstkkyDTO, @Param("userData") UserVO userVO);
	public int saveDoe049Wshphd(@Param("param") OoubhdDTO ooubhdDTO, @Param("userData") UserVO userVO);
	public int saveDoe049Wshpit(@Param("param") OoubitDTO ooubitDTO, @Param("stock") WstkkyDTO wstkkyDTO, @Param("userData") UserVO userVO);
	public <T> int saveDoe049Wtakit(@Param("param") T paramDTO, @Param("userData") UserVO userVO);
	public int updateObostat(@Param("param") OoubhdDTO ooubhdDTO, @Param("userData") UserVO userVO);
	public int updateOboitst(@Param("param") OoubitDTO ooubitDTO, @Param("userData") UserVO userVO);

	public int updateTasksts(@Param("param") WtakitDTO wtakitDTO, @Param("userData") UserVO userVO);
	public int updateShpitst(@Param("param") WtakitDTO wtakitDTO, @Param("userData") UserVO userVO);

	public <T> List<WshpitDTO> getDoe050WshpitList(@Param("param") T paramDTO, @Param("userData") UserVO userVO);
	public <T> List<OoubitDTO> getDoe050OoubitList(@Param("param") T paramDTO, @Param("userData") UserVO userVO);
	public int updateOoucfyn(@Param("param") OoubhdDTO ooubhdDTO, @Param("userData") UserVO userVO);
	public int updateShpdcst(@Param("param") WtakitDTO wtakitDTO, @Param("userData") UserVO userVO);

	public List<WtakitDTO> getDoe051HeadList(@Param("param") WtakitDTO wtakitDTO, @Param("userData") UserVO userVO);
	public List<WtakitDTO> getDoe051ItemList(@Param("param") WtakitDTO wtakitDTO, @Param("userData") UserVO userVO);

	public int updateEntdate(@Param("param") OoubhdDTO ooubhdDTO, @Param("userData") UserVO userVO);
	public int updateDoe053Entdate(@Param("param") OoubhdDTO ooubhdDTO, @Param("userData") UserVO userVO);

	public List<OoubhdDTO> getDoe053HeadList(@Param("param") OoubhdDTO ooubhdDTO, @Param("userData") UserVO userVO);

	public List<OoubhdDTO> getDoe054HeadList(@Param("param") OoubhdDTO ooubhdDTO, @Param("userData") UserVO userVO);

	public List<OoubitDTO> getOoubitShlockyGroup(@Param("param") OoubhdDTO ooubhdDTO, @Param("userData") UserVO userVO);
	public List<OoubitDTO> getOoubitShlockyGroupList(@Param("param") OoubitDTO ooubitDTO, @Param("userData") UserVO userVO);

	public List<String> getShpplkyList(@Param("param") OoubitDTO ooubitDTO, @Param("userData") UserVO userVO);
	public <T> List<OoubitDTO> getDoe053ItemList(@Param("param") T paramDTO, @Param("userData") UserVO userVO);

	public List<WtakitDTO> getDoe056HeadList(@Param("param") WtakitDTO wtakitDTO, @Param("userData") UserVO userVO);
	public List<WtakitDTO> getDoe056WtakitList(@Param("param") WtakitDTO wtakitDTO, @Param("userData") UserVO userVO);
	public List<WshpitDTO> getDoe056WshpitStatList(@Param("param") WtakitDTO wtakitDTO, @Param("userData") UserVO userVO);
	public int updateDoe056ShpDate(@Param("param") WtakitDTO wtakitDTO, @Param("userData") UserVO userVO);
	public int deleteDoe056Stock(@Param("param") WtakitDTO wtakitDTO, @Param("userData") UserVO userVO);
	public List<WtakitDTO> getDoe056WtakitShpdckyAnditList(@Param("param") WtakitDTO wtakitDTO, @Param("userData") UserVO userVO);

	//doe056 ullocky stlayer
	public int getDoe056UllockyStlayer(@Param("param") WtakitDTO wtakitDTO, @Param("userData") UserVO userVO);

	public List<WshphdDTO> getDoe057HeadList(@Param("param") WshphdDTO wshphdDTO, @Param("userData") UserVO userVO);
	public List<WshpitDTO> getDoe057ItemList(@Param("param") WshpitDTO wshpitDTO, @Param("userData") UserVO userVO);
	public List<WshpitDTO> getDoe057TotalList(@Param("param") WshphdDTO wshphdDTO, @Param("userData") UserVO userVO);
	public WshphdDTO checkShipmentCompletionStatus(@Param("param") WshphdDTO wshphdDTO, @Param("userData") UserVO userVO);
	public int updateTpwpwgShpcfyn(@Param("param") WshphdDTO wshphdDTO, @Param("userData") UserVO userVO);
	public int updateWplnhdStat(@Param("param") WshphdDTO wshphdDTO, @Param("userData") UserVO userVO);
	public int updateWplnitStat(@Param("param") WshphdDTO wshphdDTO, @Param("userData") UserVO userVO);
	public int updateObostatCMP(@Param("param") OoubhdDTO ooubhdDTO, @Param("userData") UserVO userVO);
	public int updateOboitstCMP(@Param("param") WtakitDTO wtakitDTO, @Param("userData") UserVO userVO);
	public int updateOboitstCmpToWshpit(@Param("updateList") List<WshpitDTO> wshpitDTO, @Param("status") String status, @Param("userData") UserVO userVO);
	public int updateShpdcstCMP(@Param("param") WtakitDTO wtakitDTO, @Param("userData") UserVO userVO);
	public int updateShpitstCMP(@Param("param") WtakitDTO wtakitDTO, @Param("userData") UserVO userVO);

	// 출고오더 아이템 개수 조회
	public Integer selectOoubitCountValidationList(@Param("param") OoubhdDTO ooubhdDTO, @Param("userData") UserVO userVO);
	// 배차아이템 개수 조회
	public Integer selectTplnitCountValidationList(@Param("param") WshphdDTO wshphdDTO, @Param("userData") UserVO userVO);
	// 배차 헤더 상태 업데이트
	public int updateTransportPlanHeaderStatus(@Param("param") WshphdDTO param, @Param("status") String status, @Param("userData") UserVO userVO);
	// 배차 아이템 상태 업데이트
	public int updateTransportPlanItemStatus(@Param("updateList") List<WshpitDTO> updateList, @Param("status") String status, @Param("userData") UserVO userVO);
	// 송장수정(출고문서 추가)
	public int insertWshpitForChangeInvoice(@Param("param") WshpitDTO wshpitDTO, @Param("sku") WstkkyDTO wstkkyDTO, @Param("userData") UserVO userData);
	// 송장수정(출고문서 변경)
	public int updateWshpitForChangeInvoice(@Param("param") WshpitDTO wshpitDTO, @Param("userData") UserVO userData);
	// 송장수정(출고문서 삭제)
	public int deleteWshpitForChangeInvoice(@Param("param") WshpitDTO wshpitDTO, @Param("userData") UserVO userData);
	// 재고검색(출고계획)
	public WstkkyDTO selectStockForShipPlan(@Param("param") WstkkyDTO wstkkyDTO, @Param("userData") UserVO userData);
	// 지시설비조회
	public List<MeqlocDTO> selectEquipListForTask(@Param("param") MlocmaDTO mlocmaDTO, @Param("userData") UserVO userData);
	//배차아이템 (doe049)
	public List<TplnitDTO> selectTplnitListByDoe049(@Param("param") TplnitDTO tplnit, @Param("userData") UserVO userData);
	// 배차계획 헤더 조회(검증용)
	public TplnhdDTO selectTplnhdForVerification(@Param("param") TplnhdDTO tplnhdDTO, @Param("userData") UserVO userData);

	//하차포인트 리스트
	public List<TmDispatchDTO> selectOmFilterLocationList(@Param("param") TmDispatchDTO dispatch, @Param("userInfo") UserVO userData);
	public List<TmDispatchDTO> selectOmLocationList(@Param("param") TmDispatchDTO dispatch, @Param("userInfo") UserVO userData);
	public List<TmDispatchDTO> selectTplnitListValidation(@Param("param") TmDispatchDTO dispatch, @Param("userInfo") UserVO userData);

	// 배차계획 리스트 조회(검증용)
	public List<TpwpwgDTO> selectTpwpwgListForValidation(@Param("param") TpwpwgDTO tpwpwgDTO, @Param("userInfo") UserVO userData);

	// 배차계획 리스트 조회(검증용)
	public List<TplnhdDTO> selectTransportPlanStat(@Param("param") WshphdDTO wshphdDTO, @Param("userInfo") UserVO userData);

	// 배차계획 추가 (송장수정)
	public int insertTplnitForChangeInvoice(@Param("param") WshpitDTO wshpitDTO, @Param("sku") WstkkyDTO wstkkyDTO, @Param("userInfo") UserVO userData);

	// 배차계획 삭제 (송장수정)
	public int deleteTplnitForChangeInvoice(@Param("param") WshpitDTO wshpitDTO, @Param("userInfo") UserVO userData);

	// max단 조회
	public Integer selectMaxLayerOfLocation(@Param("param") MlocmaDTO mlocmaDTO, @Param("userInfo") UserVO userData);
}
