package com.kbph.logistics.im.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kbph.logistics.common.domain.GridDTO;
import com.kbph.logistics.common.enums.Useyn;
import com.kbph.logistics.im.domain.InbInspectionDTO;
import com.kbph.logistics.im.domain.InboundCheckDTO;
import com.kbph.logistics.im.domain.InboundOrderForTreeGridDTO;
import com.kbph.logistics.im.domain.WasnhdDTO;
import com.kbph.logistics.im.domain.WasnitDTO;
import com.kbph.logistics.im.domain.WrcvhdDTO;
import com.kbph.logistics.im.domain.WrcvitDTO;
import com.kbph.logistics.im.service.InboundService;
import com.kbph.logistics.md.type.Asnstat;

import lombok.RequiredArgsConstructor;

/**
 * @author : OP
 * @version : 1.0.0
 * @since : 2024-07-09
 * @note : Contorller Class for Inbound
 * ==================================================
 * DATE 							AUTHOR 								NOTE
 * --------------------------------------------------------------------------------------
 * 2024-07-09 					OP 						create Controller class
 * --------------------------------------------------------------------------------------
 * ==================================================
 */
@RestController
@RequiredArgsConstructor
public class InboundController {
	private final InboundService inboundService; // 입고

	// 입고오더 저장
	@PutMapping("/im/inbound/doe023/grids/1")
	public int createInboundOrder(@RequestBody List<InboundOrderForTreeGridDTO> inboundOrderList) {
		return inboundService.createInboundOrder(inboundOrderList);
	}

	// 직송처리
	@PostMapping("/im/inbound/doe024/direct-shipping")
	public int saveDirectShipping(@RequestBody List<WasnhdDTO> saveList) {
		return inboundService.saveDirectShipping(saveList);
	}

	@PostMapping("/im/inbound/doe024/cancel-asn")
	public int cancelAsnItem(@RequestBody List<WasnitDTO> requestDTO) {
		return inboundService.cancelAsnItem(requestDTO);
	}

	// asn header 조회
	@GetMapping(path = { "/im/inbound/doe021/grids/1", "/im/inbound/doe024/grids/1", "/im/inbound/doe025/grids/1", "/im/inbound/doe026/grids/1", "/im/inbound/doe029/grids/1" })
	public List<WasnhdDTO> getWasnhdList(@ModelAttribute WasnhdDTO wasnhdDTO) {
		return inboundService.getWasnhdList(wasnhdDTO);
	}

	// asn item 조회
	@GetMapping(path = { "/im/inbound/doe021/grids/2", "/im/inbound/doe025/grids/2", "/im/inbound/doe026/grids/2", "/im/inbound/doe029/grids/2" })
	public List<WasnitDTO> getWasnitList(@ModelAttribute WasnitDTO wasnitDTO) {
		return inboundService.getWasnitList(wasnitDTO);
	}

	// asn item 조회 확정 X
	@GetMapping("/im/inbound/doe024/grids/2")
	public List<WasnitDTO> getDoe024WasnitList(@ModelAttribute WasnitDTO wasnitDTO) {
		return inboundService.getDoe024WasnitList(wasnitDTO);
	}
	// asn item 조회(확정시 전략 태움)
	@PostMapping("/im/inbound/doe024/grids/2/strategy")
	public List<WasnitDTO> getWasnitListForConfirm(@RequestBody List<WasnitDTO> wasnitList) {
		return inboundService.getWasnitListForConfirm(wasnitList);
	}

	// 최상단 체크 저장
	@PostMapping("/im/inbound/doe021/grids/2")
	public int saveAsnTopLayer(@RequestBody List<WasnitDTO> saveList) {
		return inboundService.saveAsnTopLayer(saveList);
	}

	// wasnhd를 통한 all 확정
	@PostMapping("/im/inbound/doe024/grids/1")
	public int saveAsnStatusForAllConfirm(@RequestBody List<WasnhdDTO> saveList) {
		return inboundService.saveAsnStatusForAllConfirm(saveList);
	}

	// wasnit를 통한 부분 확정
	@PostMapping("/im/inbound/doe024/grids/2")
	public int saveAsnStatusForPartConfirm(@RequestBody List<WasnitDTO> saveList) {
		return inboundService.saveAsnStatusForPartConfirm(saveList);
	}

	// wasnhd를 통한 all 취소
	@PostMapping("/im/inbound/doe025/grids/1")
	public int saveAsnStatusForAllCancel(@RequestBody List<WasnhdDTO> saveList) {
		return inboundService.saveAsnStatusForAllCancel(saveList);
	}

	// wasnit를 통한 부분 취소
	@PostMapping("/im/inbound/doe025/grids/2")
	public int saveAsnStatusForPartCancel(@RequestBody List<WasnitDTO> saveList) {
		return inboundService.saveAsnStatusForPartCancel(saveList);
	}

	// 입고검수대상 확정
	@PostMapping("/im/inbound/doe026/grids/1")
	public int setWasnhdFlagForInspectionTarget(@RequestBody List<WasnhdDTO> saveList) {
		return inboundService.setWasnhdFlagForInspectionTarget(saveList);
	}

	@GetMapping("/im/inbound/doe027/grid/head")
	public List<WasnhdDTO> getDoe027HeadList(@ModelAttribute WasnhdDTO wasnhdDTO) {
		return inboundService.getDoe027HeadList(wasnhdDTO);
	}

	@GetMapping("/im/inbound/doe027/grid/item")
	public List<WasnitDTO> getDoe027ItemList(@ModelAttribute WasnitDTO wasnitDTO) {
		return inboundService.getDoe027ItemList(wasnitDTO);
	}

	//입고검수 헤더 저장
	@PostMapping("/im/inbound/doe027/head/pass")
	public int saveHeadPassDoe027(@RequestBody InbInspectionDTO requestList) {
		return inboundService.saveHeadPassDoe027(requestList);
	}
	@PostMapping("/im/inbound/doe027/head/fail")
	public int saveHeadFailDoe027(@RequestBody GridDTO<WasnhdDTO> requestList) {
		return inboundService.saveHeadFailDoe027(requestList);
	}

	//입고검수 아이템 저장 -> 삭제예정
	@PostMapping("/im/inbound/doe027/pass")
	public int savePassDoe027(@RequestBody InboundCheckDTO<WasnhdDTO, WasnitDTO> requestList) {
		return inboundService.savePassDoe027(requestList);
	}

	@PostMapping("/im/inbound/doe027/fail")
	public int saveFailDoe027(@RequestBody InboundCheckDTO<WasnhdDTO, WasnitDTO> requestList) {
		return inboundService.saveFailDoe027(requestList);
	}

	// 입차정보 입력(업데이트)
	@PostMapping("/im/inbound/doe029/grids/1")
	public int saveInboundVehicleEntranceInfo(@RequestBody List<WasnhdDTO> saveList) {
		return inboundService.saveInboundVehicleEntranceInfo(saveList);
	}

	// 입고지시 대상 헤더 조회
	@GetMapping(path = { "/im/inbound/doe031/grids/1" })
	public List<WasnhdDTO> getWasnhdListForReceiveDocument(@ModelAttribute WasnhdDTO wasnhdDTO) {
		wasnhdDTO.setAsnityn(Useyn.USE.getString());
		if(CollectionUtils.isEmpty(wasnhdDTO.getAsnstatList())) {
			List<String> asnstatList = new ArrayList<>();

			asnstatList.add(Asnstat.PART_PASS_INSPECTION.getCode());
			asnstatList.add(Asnstat.PASS_INSPECTION.getCode());
			asnstatList.add(Asnstat.PROCEED.getCode());
			asnstatList.add(Asnstat.PART_CANCEL.getCode());

			wasnhdDTO.setAsnstatList(asnstatList);
		}

		return inboundService.getWasnhdList(wasnhdDTO);
	}

	// 입고지시 대상 리스트 조회
	@GetMapping("/im/inbound/doe031/grids/2")
	public List<WasnitDTO> getWasnitListWithoutTask(@ModelAttribute WasnitDTO wasnitDTO) {
		return inboundService.getWasnitListWithoutTask(wasnitDTO);
	}

	// 입고문서 생성 & 지시(작업) 등록
	@PostMapping("/im/inbound/doe031/grids/2")
	public int createInboundDocumentItemList(@RequestBody List<WasnitDTO> saveList) {
		return inboundService.createInboundDocument(saveList);
	}

	// 지시한 입고문서 헤더 조회(신규만)
	@GetMapping("/im/inbound/doe032/grids/1")
	public List<WrcvhdDTO> getOrderedReceiveDocumentHeaderList(@ModelAttribute WrcvhdDTO wrcvhdDTO) {
		return inboundService.getOrderedReceiveDocumentHeaderList(wrcvhdDTO);
	}

	// 지시한 입고문서 아이템 조회
	@GetMapping("/im/inbound/doe032/grids/2")
	public List<WrcvitDTO> getOrderedReceiveDocumentItemList(@ModelAttribute WrcvitDTO wrcvitDTO) {
		return inboundService.getOrderedReceiveDocumentItemList(wrcvitDTO);
	}

	// 지시한 입고문서 취소(헤더)
	@PostMapping("/im/inbound/doe032/grids/1")
	public int cancelOrderedReceiveDocumentHeaderList(@RequestBody List<WrcvhdDTO> saveList) {
		return inboundService.cancelOrderedReceiveDocumentHeaderList(saveList);
	}

	// 지시한 입고문서 취소(아이템)
	@PostMapping("/im/inbound/doe032/grids/2")
	public int cancelOrderedReceiveDocumentItemList(@RequestBody List<WrcvitDTO> saveList) {
		return inboundService.cancelOrderedReceiveDocumentItemList(saveList);
	}

	// 입고정보등록-태블릿-헤더
	@GetMapping("/im/inbound/doe033/grid/head")
	public List<WasnhdDTO> getDoe033HeadList(@ModelAttribute WasnhdDTO wasnhdDTO) {
		return inboundService.getDoe033HeadList(wasnhdDTO);
	}

	// 입고정보등록-태블릿-아이템
	@GetMapping("/im/inbound/doe033/grid/item")
	public List<WasnitDTO> getDoe033ItemList(@ModelAttribute WasnitDTO wasnitDTO) {
		return inboundService.getDoe033ItemList(wasnitDTO);
	}

	// 조업시작-벨리데이션
	@PostMapping("/im/inbound/doe033/oper-start/validation")
	public int saveDoe033StartOperValidation(@RequestBody WasnhdDTO requestDTO) {
		return inboundService.saveDoe033StartOperValidation(requestDTO);
	}

	// 조업시작
	@PostMapping("/im/inbound/doe033/oper-start")
	public int saveDoe033StartOper(@RequestBody WasnhdDTO requestDTO) {
		return inboundService.saveDoe033StartOper(requestDTO);
	}

	// 작업완료-벨리데이션
	@PostMapping("/im/inbound/doe033/task-cmp/validation")
	public int saveDoe033CmpTaskValidation(@RequestBody WasnhdDTO requestDTO) {
		return inboundService.saveDoe033CmpTaskValidation(requestDTO);
	}

	// 작업완료
	@PostMapping("/im/inbound/doe033/task-cmp")
	public int saveDoe033CmpTask(@RequestBody GridDTO<WasnitDTO> requestDTO) {
		return inboundService.saveDoe033CmpTask(requestDTO);
	}

	// 조업완료-벨리데이션
	@PostMapping("/im/inbound/doe033/oper-cmp/validation")
	public int saveDoe033CmpOperValidation(@RequestBody WasnhdDTO requestDTO) {
		return inboundService.saveDoe033CmpOperValidation(requestDTO);
	}

	// 조업완료
	@PostMapping("/im/inbound/doe033/oper-cmp")
	public int saveDoe033CmpOper(@RequestBody WasnhdDTO requestDTO) {
		return inboundService.saveDoe033CmpOper(requestDTO);
	}

	// 입고완료 대상 조회
	@GetMapping("/im/inbound/doe034/grids/1")
	public List<WrcvhdDTO> getCompleteReceiveHeaderList(@ModelAttribute WrcvhdDTO wrcvhdDTO) {
		return inboundService.getCompleteReceiveDocumentHeaderList(wrcvhdDTO);
	}

	// 입고완료 대상 아이템 조회
	@GetMapping("/im/inbound/doe034/grids/2")
	public List<WrcvitDTO> getCompleteReceiveDocumentItemList(@ModelAttribute WrcvitDTO wrcvitDTO) {
		return inboundService.getCompleteReceiveDocumentItemList(wrcvitDTO);
	}

	// 입고완료처리
	@PostMapping("/im/inbound/doe034/grids/1")
	public int saveInboundComplete(@RequestBody List<WrcvhdDTO> saveList) {
		return inboundService.saveInboundComplete(saveList);
	}
}
