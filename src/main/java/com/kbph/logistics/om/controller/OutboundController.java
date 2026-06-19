package com.kbph.logistics.om.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kbph.logistics.common.domain.GridDTO;
import com.kbph.logistics.common.domain.HeadItemGridListDTO;
import com.kbph.logistics.om.domain.OoubhdDTO;
import com.kbph.logistics.om.domain.OoubitDTO;
import com.kbph.logistics.om.domain.OutboundOrderForTreeGridDTO;
import com.kbph.logistics.om.domain.WplnhdDTO;
import com.kbph.logistics.om.domain.WplnitDTO;
import com.kbph.logistics.om.domain.WshphdDTO;
import com.kbph.logistics.om.domain.WshpitDTO;
import com.kbph.logistics.om.service.OutboundService;
import com.kbph.logistics.rm.component.AllocationStrategyComponent;
import com.kbph.logistics.sm.domain.WstkkyDTO;
import com.kbph.logistics.sm.domain.WtakitDTO;
import com.kbph.logistics.tm.domain.TmDispatchDTO;
import com.kbph.logistics.tm.domain.TplnhdDTO;
import com.kbph.logistics.tm.domain.TplnitDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class OutboundController {
	private final OutboundService outboundService;
	private final AllocationStrategyComponent allocationStrategyComponent;

	// 출고계획 재고 조회
	@GetMapping(value = {"/om/outbound/doe044/stock", "/om/outbound/doe045/stock"})
	public List<WstkkyDTO> getStockListForShipPlan(@ModelAttribute WstkkyDTO wstkkyDTO){
		return outboundService.getStockListForShipPlan(wstkkyDTO);
	}

	// 출고대상 추가 재고 조회
	@GetMapping(value = {"/om/outbound/doe053/modal/stock"})
	public List<WstkkyDTO> getTargetStockListForAddStock(@ModelAttribute WstkkyDTO wstkkyDTO){
		return outboundService.getTargetStockListForAddStock(wstkkyDTO);
	}

	// 송장수정 재고 조회
	@GetMapping(value = {"/om/outbound/doe057/stock"})
	public List<WstkkyDTO> getTargetStockListForChangeInvoice(@ModelAttribute WstkkyDTO wstkkyDTO){
		return outboundService.getTargetStockListForChangeInvoice(wstkkyDTO);
	}

	// 출고예정저장
	@PostMapping("/om/outbound/doe044/grids/1")
	public int saveStandardOutboundPlanList(@RequestBody List<OutboundOrderForTreeGridDTO> outboundOrderList) {
		return outboundService.saveStandardOutboundPlanList(outboundOrderList);
	}

	// 지정출고저장
	@PostMapping("/om/outbound/doe045/save")
	public int saveDoe045(@RequestBody List<OutboundOrderForTreeGridDTO> outboundOrderList) {
		return outboundService.saveDoe045(outboundOrderList);
	}

	// 출고예정 헤더 리스트 조회(리스트가 너무 많아서 post로)
	@PostMapping("/om/outbound/doe046/grids/1")
	public List<WplnhdDTO> getOutboundPlanHeaderList(@RequestBody WplnhdDTO wplnhdDTO){
		return outboundService.getOutboundPlanHeaderList(wplnhdDTO);
	}

	// 출고예정 아이템 리스트 조회
	@GetMapping("/om/outbound/doe046/grids/2")
	public List<WplnitDTO> getOutboundPlanItemList(@ModelAttribute WplnitDTO wplnitDTO){
		return outboundService.getOutboundPlanItemList(wplnitDTO);
	}

	// 출고예정 헤더 저장(배차오더)
	@PutMapping("/om/outbound/doe046/grids/1")
	public int saveOutboundPlanForDispatch(@RequestBody List<WplnhdDTO> saveList) {
		return outboundService.saveOutboundPlanForDispatch(saveList);
	}

	// 출고마감처리
	@PostMapping("/om/outbound/doe046/close")
	public int saveOutboundPlanClose(@RequestBody List<WplnhdDTO> saveList) {
		return outboundService.saveOutboundPlanClose(saveList);
	}

	// 배차된 출고계획 헤더 리스트 조회
	@GetMapping("/om/outbound/doe047/grids/1")
	public List<WplnhdDTO> getDispatchedOutboundPlanHeaderList(@ModelAttribute WplnhdDTO wplnhdDTO){
		return outboundService.getDispatchedOutboundPlanHeaderList(wplnhdDTO);
	}

	// 배차된 출고계획 아이템 리스트 조회
	@GetMapping("/om/outbound/doe047/grids/2")
	public List<TplnitDTO> getDispatchedItemList(@ModelAttribute TplnhdDTO tplnhdDTO){
		return outboundService.getDispatchedOutboundPlanItemList(tplnhdDTO);
	}

	// 출고예정확정(플래그 업데이트)
	@PostMapping("/om/outbound/doe047/grids/1")
	public int setDispatchedOutboundPlanConfirm(@RequestBody List<WplnhdDTO> saveList){
		return outboundService.setDispatchedOutboundPlanConfirm(saveList);
	}

	// 배차완료된 출고계획 헤더 리스트 조회
	@GetMapping("/om/outbound/doe048/grids/1")
	public List<WplnhdDTO> getFullDispatchedOutboundPlanHeaderList(@ModelAttribute WplnhdDTO wplnhdDTO){
		return outboundService.getFullDispatchedOutboundPlanHeaderList(wplnhdDTO);
	}

	// 배차완료된 출고계획 아이템 리스트 조회
	@GetMapping("/om/outbound/doe048/grids/2")
	public List<TplnitDTO> getFullDispatchedOutboundPlanItemList(@ModelAttribute WplnhdDTO wplnhdDTO){
		return outboundService.getFullDispatchedOutboundPlanItemList(wplnhdDTO);
	}

	// 배차완료된 출고계획 취소(배차 포함)
	@PostMapping("/om/outbound/doe048/grids/1")
	public int cancelDispatchedOutboundPlan(@RequestBody List<WplnhdDTO> saveList){
		return outboundService.cancelDispatchedOutboundPlan(saveList);
	}

	// 출고지시대상 조회
	@GetMapping("/om/outbound/doe049/grids/head")
	public List<OoubhdDTO> getDoe049HeadList(@ModelAttribute OoubhdDTO ooubhdDTO){
		return outboundService.getDoe049HeadList(ooubhdDTO);
	}

	// 출고지시대상 아이템 조회
	@GetMapping("/om/outbound/doe049/grids/item")
	public List<OoubitDTO> getDoe049ItemList(@ModelAttribute OoubitDTO ooubitDTO){
		return outboundService.getDoe049ItemList(ooubitDTO);
	}

	// 출고지시등록
	@PostMapping("/om/outbound/doe049/save")
	public int saveDoe049(@RequestBody List<OoubhdDTO> ooubhdDTOList){
		return outboundService.saveDoe049(ooubhdDTOList);
	}

	// 출고지시 취소
	@PostMapping("/om/outbound/doe050/save")
	public int saveDoe050(@RequestBody List<OoubhdDTO> requestList){
		return outboundService.saveDoe050(requestList);
	}

	// 출고지시취소 대상 조회
	@GetMapping("/om/outbound/doe050/grid/head")
	public List<OoubhdDTO> getDoe050HeadList(@ModelAttribute OoubhdDTO ooubhdDTO){
		return outboundService.getDoe050HeadList(ooubhdDTO);
	}

	// 출고지시취소 대상 아이템 조회
	@GetMapping("/om/outbound/doe050/grid/item")
	public List<OoubitDTO> getDoe050ItemList(@ModelAttribute OoubitDTO ooubitDTO){
		return outboundService.getDoe050ItemList(ooubitDTO);
	}

	// 출고지시 조회
	@GetMapping("/om/outbound/doe051/grid/head")
	public List<OoubhdDTO> getDoe051HeadList(@ModelAttribute OoubhdDTO ooubhdDTO){
		return outboundService.getDoe051HeadList(ooubhdDTO);
	}

	// 출고지시 아이템 조회
	@GetMapping("/om/outbound/doe051/grid/item")
	public List<OoubitDTO> getDoe051ItemList(@ModelAttribute OoubitDTO ooubitDTO){
		return outboundService.getDoe051ItemList(ooubitDTO);
	}

	// 출고대상확정 조회
	@GetMapping("/om/outbound/doe053/grid/head")
	public List<OoubhdDTO> getDoe053HeadList(@ModelAttribute OoubhdDTO ooubhdDTO){
		return outboundService.getDoe053HeadList(ooubhdDTO);
	}

	// 출고대상확정 아이템 조회
	@GetMapping("/om/outbound/doe053/grid/item")
	public List<OoubitDTO> getDoe053ItemList(@ModelAttribute OoubitDTO ooubitDTO){
		return outboundService.getDoe053ItemList(ooubitDTO);
	}

	// 출고대상확정 모달 헤더 조회
	@GetMapping("/om/outbound/doe053/modal/grids/1")
	public List<OoubhdDTO> getDoe053ModalHeadList(@ModelAttribute OoubhdDTO ooubhdDTO){
		return outboundService.getDoe053ModalHeadList(ooubhdDTO);
	}

	// 출고대상확정 모달 아이템 조회
	@GetMapping("/om/outbound/doe053/modal/grids/2")
	public List<TplnitDTO> getDoe053ModalItemList(@ModelAttribute OoubhdDTO ooubhdDTO){
		return outboundService.getDoe053ModalItemList(ooubhdDTO);
	}

	// 출고대상확정 전략조회
	@GetMapping("/om/outbound/doe053/modal/allocationStrategy")
	public List<WstkkyDTO> getDoe053AllocationStrategyStockList(@ModelAttribute OoubhdDTO ooubhdDTO){
		return allocationStrategyComponent.setOutboundSkuWithAllocationStrategy(ooubhdDTO);
	}

	// 출고대상확정
	@PostMapping(value = {"/om/outbound/doe053/save"} )
	public int saveDoe053(@RequestBody List<OoubhdDTO> requestList) {
		return outboundService.saveDoe053(requestList);
	}

	// 입차정보저장
	@PostMapping(value = {"/om/outbound/doe054/save"} )
	public int saveDoe054(@RequestBody List<OoubhdDTO> requestList) {
		return outboundService.saveDoe054(requestList);
	}

	// 출고대상확정 대상(아이템) 추가
	@PostMapping(value = {"/om/outbound/doe053/addstock"} )
	public int saveDoe053AddStock(@RequestBody HeadItemGridListDTO<OoubhdDTO, WstkkyDTO> headItemGridList) {
		return outboundService.saveDoe053AddStock(headItemGridList);
	}

	// 입차대상 조회
	@GetMapping("/om/outbound/doe054/grid/head")
	public List<OoubhdDTO> getDoe054HeadList(@ModelAttribute OoubhdDTO ooubhdDTO){
		return outboundService.getDoe054HeadList(ooubhdDTO);
	}

	// 입차대상 아이템 조회
	@GetMapping("/om/outbound/doe054/grid/item")
	public List<OoubitDTO> getDoe054ItemList(@ModelAttribute OoubitDTO ooubitDTO){
		return outboundService.getDoe054ItemList(ooubitDTO);
	}

//	@PostMapping("/om/outbound/doe054/save")
//	public int saveDoe054(@RequestBody List<OoubhdDTO> requestList) {
//		return outboundService.saveDoe054(requestList);
//	}

	@GetMapping("/om/outbound/doe056/grid/head")
	public List<WtakitDTO> getDoe056HeadList(@ModelAttribute WtakitDTO wtakitDTO){
		return outboundService.getDoe056HeadList(wtakitDTO);
	}

	@GetMapping("/om/outbound/doe056/grid/item")
	public List<WtakitDTO> getDoe056ItemList(@ModelAttribute WtakitDTO wtakitDTO){
		return outboundService.getDoe056ItemList(wtakitDTO);
	}

	//조업시작-벨리데이션
	@PostMapping("/om/outbound/doe056/oper-start/validation")
	public int saveDoe056StartOperValidation(@RequestBody WtakitDTO requestDTO) {
		return outboundService.saveDoe056StartOperValidation(requestDTO);
	}

	//조업시작
	@PostMapping("/om/outbound/doe056/oper-start")
	public int saveDoe056StartOper(@RequestBody WtakitDTO requestDTO) {
		return outboundService.saveDoe056StartOper(requestDTO);
	}

	//작업완료-벨리데이션
	@PostMapping("/om/outbound/doe056/task-cmp/validation")
	public int saveDoe056CmpTaskValidation(@RequestBody WtakitDTO requestDTO){
		return outboundService.saveDoe056CmpTaskValidation(requestDTO);
	}

	//작업완료
	@PostMapping("/om/outbound/doe056/task-cmp")
	public int saveDoe036CmpTask(@RequestBody GridDTO<WtakitDTO> requestDTO){
		return outboundService.saveDoe056CmpTask(requestDTO);
	}

	//조업완료-벨리데이션
	@PostMapping("/om/outbound/doe056/oper-cmp/validation")
	public int saveDoe056CmpOperValidation(@RequestBody WtakitDTO requestDTO) {
		return outboundService.saveDoe056CmpOperValidation(requestDTO);
	}

	//조업완료
	@PostMapping("/om/outbound/doe056/oper-cmp")
	public int saveDoe056CmpOper(@RequestBody WtakitDTO requestDTO) {
		return outboundService.saveDoe056CmpOper(requestDTO);
	}

	@GetMapping("/om/outbound/doe057/grid/head")
	public List<WshphdDTO> getDoe057HeadList(@ModelAttribute WshphdDTO wshphdDTO){
		return outboundService.getDoe057HeadList(wshphdDTO);
	}

	@GetMapping("/om/outbound/doe057/grid/item")
	public List<WshpitDTO> getDoe057ItemList(@ModelAttribute WshpitDTO wshpitDTO){
		return outboundService.getDoe057ItemList(wshpitDTO);
	}

	@GetMapping("/om/outbound/doe057/grid/total")
	public List<WshpitDTO> getDoe057TotalList(@ModelAttribute WshphdDTO wshphdDTO){
		return outboundService.getDoe057TotalList(wshphdDTO);
	}

	@PostMapping("/om/outbound/doe057/save")
	public int saveDoe057(@RequestBody List<WshphdDTO> requestDTO) {
		return outboundService.saveDoe057(requestDTO);
	}

	// 송장수정 저장
	@PostMapping("/om/outbound/doe057/changeInvoice")
	public int changeInvoice(@RequestBody GridDTO<WshpitDTO> saveList) {
		return outboundService.changeInvoice(saveList);
	}

	// 로케이션 리스트 조회
	@GetMapping("/om/outbound/common/location-list")
	public List<TmDispatchDTO> getFilterLocationList(TmDispatchDTO dispatch) {
		return outboundService.getFilterLocationList(dispatch);
	}

	// 운송여부 확인 조회
	@GetMapping("/om/outbound/doe057/transport/stat")
	public boolean checkTransportPlanStat(WshphdDTO wshphdDTO) {
		return outboundService.checkTransportPlanStat(wshphdDTO);
	}
}
