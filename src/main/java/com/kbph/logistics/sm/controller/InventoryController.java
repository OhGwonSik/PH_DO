package com.kbph.logistics.sm.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kbph.logistics.common.domain.GridDTO;
import com.kbph.logistics.common.domain.InitDataDTO;
import com.kbph.logistics.md.domain.MaremaDTO;
import com.kbph.logistics.md.domain.MdesmaDTO;
import com.kbph.logistics.md.domain.MlocmaDTO;
import com.kbph.logistics.md.domain.MrscmaDTO;
import com.kbph.logistics.sm.domain.WphyitDTO;
import com.kbph.logistics.sm.domain.WstkkyDTO;
import com.kbph.logistics.sm.domain.WtakitDTO;
import com.kbph.logistics.sm.service.InventoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class InventoryController {

	private final InventoryService inventoryService;

	@GetMapping("/sm/inventory/init")
	public InitDataDTO inventoryInit(@ModelAttribute MaremaDTO maremaDTO, MlocmaDTO mlocmaDTO) {
		return inventoryService.inventoryInit(maremaDTO, mlocmaDTO);
	}

	@GetMapping("/sm/inventory/init/mrscma")
	public InitDataDTO doe035Init(@ModelAttribute MrscmaDTO mrscmaDTO) {
		return inventoryService.doe035Init(mrscmaDTO);
	}

	@GetMapping("/sm/inventory/doe035/layer")
	public WstkkyDTO getDoe035Layer(@ModelAttribute WstkkyDTO wstkkyDTO) {
		return inventoryService.getDoe035Layer(wstkkyDTO);
	}

	@GetMapping("/sm/inventory/stock/grid")
	public List<WstkkyDTO> getWstkkyList(@ModelAttribute WstkkyDTO wstkkyDTO){
		return inventoryService.getWstkkyList(wstkkyDTO);
	}

	//선별지시 벨리데이션
	@PostMapping("/sm/inventory/doe035/validate")
	public int saveDoe035Validation(@RequestBody List<WstkkyDTO> requestDTO) {
		return inventoryService.saveDoe035Validation(requestDTO);
	}

	//선별지시
	@PostMapping("/sm/inventory/doe035/grid")
	public List<WstkkyDTO> getDoe035WstkkyList(@RequestBody WstkkyDTO wstkkyDTO){
		return inventoryService.getDoe035WstkkyList(wstkkyDTO);
	}

	@PostMapping("/sm/inventory/doe035/save")
	public int saveDoe035(@RequestBody GridDTO<WstkkyDTO> requestDTO) {
		return inventoryService.saveDoe035(requestDTO);
	}

	// doedy035 선별지시
	@PostMapping("/sm/inventory/doedy035/grid")
	public List<WstkkyDTO> getDoeDy035WstkkyList(@RequestBody WstkkyDTO wstkkyDTO){
		return inventoryService.getDoeDy035WstkkyList(wstkkyDTO);
	}

	//선별정보등록
	@GetMapping("/sm/inventory/doe036/grid/head")
	public List<WtakitDTO> getDoe036HeadList(@ModelAttribute WtakitDTO wtakitDTO){
		return inventoryService.getDoe036HeadList(wtakitDTO);
	}

	@GetMapping("/sm/inventory/doe036/grid/item")
	public List<WtakitDTO> getDoe036ItemList(@ModelAttribute WtakitDTO wtakitDTO){
		return inventoryService.getDoe036ItemList(wtakitDTO);
	}

	@GetMapping("/sm/inventory/doe036/grid/modal")
	public List<WtakitDTO> getDoe036ModalList(@ModelAttribute WtakitDTO wtakitDTO){
		return inventoryService.getDoe036ModalList(wtakitDTO);
	}

	//조업시작-벨리데이션
	@PostMapping("/sm/inventory/doe036/oper-start/validation")
	public int saveDoe036StartOperValidation(@RequestBody WtakitDTO requestDTO) {
		return inventoryService.saveDoe036StartOperValidation(requestDTO);
	}

	//조업시작
	@PostMapping("/sm/inventory/doe036/oper-start")
	public int saveDoe036StartOper(@RequestBody WtakitDTO requestDTO) {
		return inventoryService.saveDoe036StartOper(requestDTO);
	}

	//작업완료-벨리데이션
	@PostMapping("/sm/inventory/doe036/task-cmp/validation")
	public int saveDoe036CmpTaskValidation(@RequestBody WtakitDTO requestDTO){
		return inventoryService.saveDoe036CmpTaskValidation(requestDTO);
	}

	//작업완료
	@PostMapping("/sm/inventory/doe036/task-cmp")
	public int saveDoe036CmpTask(@RequestBody GridDTO<WtakitDTO> requestDTO){
		return inventoryService.saveDoe036CmpTask(requestDTO);
	}

	//조업완료-벨리데이션
	@PostMapping("/sm/inventory/doe036/oper-cmp/validation")
	public int saveDoe036CmpOperValidation(@RequestBody WtakitDTO requestDTO) {
		return inventoryService.saveDoe036CmpOperValidation(requestDTO);
	}

	//조업완료
	@PostMapping("/sm/inventory/doe036/oper-cmp")
	public int saveDoe036CmpOper(@RequestBody WtakitDTO requestDTO) {
		return inventoryService.saveDoe036CmpOper(requestDTO);
	}

	//선별취소
	@PostMapping("/sm/inventory/doe037/cancel")
	public int saveDoe037Cancel(@RequestBody GridDTO<WtakitDTO> requestDTO) {
		return inventoryService.saveDoe037Cancel(requestDTO);
	}

	//선별확정
	@GetMapping("/sm/inventory/doe037/grid")
	public List<WtakitDTO> getDoe037WtakitList(@ModelAttribute WtakitDTO wtakitDTO){
		return inventoryService.getDoe037WtakitList(wtakitDTO);
	}

	//선별완료확정
	@PostMapping("/sm/inventory/doe037/save")
	public int saveDoe037(@RequestBody GridDTO<WtakitDTO> requestDTO) {
		return inventoryService.saveDoe037(requestDTO);
	}

	//재고실사지시
	@PostMapping("/sm/inventory/doe038/save")
	public int saveDoe038(@RequestBody GridDTO<WstkkyDTO> requestDTO) {
		return inventoryService.saveDoe038(requestDTO);
	}

	//재고실사등록
	@GetMapping("/sm/inventory/doe039/grid/head")
	public List<WphyitDTO> getDoe039WphyitList(@ModelAttribute WphyitDTO wphyitDTO){
		return inventoryService.getDoe039WphyitList(wphyitDTO);
	}

	@GetMapping("/sm/inventory/doe039/grid/item")
	public List<WphyitDTO> getDoe039ItemList(@ModelAttribute WphyitDTO wphyitDTO){
		return inventoryService.getDoe039ItemList(wphyitDTO);
	}

	@PostMapping("/sm/inventory/doe039/save")
	public int saveDoe039(@RequestBody GridDTO<WphyitDTO> requestDTO) {
		return inventoryService.saveDoe039(requestDTO);
	}

	//재고실사확정
	@GetMapping("/sm/inventory/doe040/grid/head")
	public List<WphyitDTO> getDoe040List(@ModelAttribute WphyitDTO wphyitDTO){
		return inventoryService.getDoe040HeadList(wphyitDTO);
	}

	@GetMapping("/sm/inventory/doe040/grid/item")
	public List<WphyitDTO> getDoe040ItemList(@ModelAttribute WphyitDTO wphyitDTO){
		return inventoryService.getDoe040ItemList(wphyitDTO);
	}

	@PostMapping("/sm/inventory/doe040/check/same-place")
	public List<WstkkyDTO> samePlaceCheck(@RequestBody GridDTO<WphyitDTO> requestDTO) {
		return inventoryService.samePlaceCheck(requestDTO);
	}

	@PostMapping("/sm/inventory/doe040/save")
	public int saveDoe040(@RequestBody GridDTO<WphyitDTO> requestDTO) {
		return inventoryService.saveDoe040(requestDTO);
	}

	//재고조정
	@PostMapping("/sm/inventory/doe041/save")
	public int saveDoe041(@RequestBody GridDTO<WstkkyDTO> requestDTO) {
		return inventoryService.saveDoe041(requestDTO);
	}

	//재고블락
	@GetMapping("/sm/inventory/doe042/grid")
	public List<WstkkyDTO> getDoe042List(@ModelAttribute WstkkyDTO wstkkyDTO){
		return inventoryService.getDoe042List(wstkkyDTO);
	}

	@PostMapping("/sm/inventory/doe042/save")
	public int saveDoe042(@RequestBody GridDTO<WstkkyDTO> requestDTO) {
		return inventoryService.saveDoe042(requestDTO);
	}

	//재고 블락해제
	@GetMapping("/sm/inventory/doe043/grid")
	public List<WstkkyDTO> getDoe043List(@ModelAttribute WstkkyDTO wstkkyDTO) {
		return inventoryService.getDoe043List(wstkkyDTO);
	}

	@PostMapping("/sm/inventory/doe043/save")
	public int saveDoe043(@RequestBody GridDTO<WstkkyDTO> requestDTO) {
		return inventoryService.saveDoe043(requestDTO);
	}

	@PostMapping("/sm/inventory/doe089/grid")
	public List<WstkkyDTO> getDoe089List(@RequestBody WstkkyDTO wstkkyDTO){
		return inventoryService.getDoe089List(wstkkyDTO);
	}

	//doe041: 재고조정 (모달) 상세착지 selectbox용
	@GetMapping("/sm/inventory/doe041/denamlc")
	public List<MdesmaDTO> getDoe041Denamlc(@ModelAttribute MdesmaDTO mdesmaDTO){
		return inventoryService.getDoe041Denamlc(mdesmaDTO);
	}

	//doe041: 재고조정 (변경모달) 업데이트
	@PatchMapping("/sm/inventory/doe041/modal/update")
	public int updateDoe041ModalUpdate(@RequestBody WstkkyDTO wstkky) {
		return inventoryService.updateDoe041ModalUpdate(wstkky);
	}
}
