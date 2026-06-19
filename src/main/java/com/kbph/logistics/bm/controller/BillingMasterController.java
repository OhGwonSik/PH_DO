package com.kbph.logistics.bm.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kbph.logistics.bm.domain.BaddcoDTO;
import com.kbph.logistics.bm.domain.BsizpaDTO;
import com.kbph.logistics.bm.domain.BtrcatDTO;
import com.kbph.logistics.bm.domain.BtrfPrcDTO;
import com.kbph.logistics.bm.domain.BtrfhdDTO;
import com.kbph.logistics.bm.domain.BtrfitDTO;
import com.kbph.logistics.bm.domain.BtrfmaDTO;
import com.kbph.logistics.bm.domain.BtrfmaSaveDTO;
import com.kbph.logistics.bm.domain.BtrpPrcDTO;
import com.kbph.logistics.bm.domain.BtrphdDTO;
import com.kbph.logistics.bm.domain.BtrpitDTO;
import com.kbph.logistics.bm.domain.BtrpmaDTO;
import com.kbph.logistics.bm.domain.UpdateGridDTO;
import com.kbph.logistics.bm.service.BillingMasterService;
import com.kbph.logistics.common.domain.ComboDataDTO;
import com.kbph.logistics.common.domain.GridDTO;
import com.kbph.logistics.common.domain.HeadItemGridDTO;
import com.kbph.logistics.md.domain.McusmaDTO;
import com.kbph.logistics.md.service.PartnerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BillingMasterController {
	
	private final BillingMasterService billingMasterService;
	private final PartnerService partnerService;
	
	/* common : billing common */
	@GetMapping("/bm/billing/common/size") 
	public List<BaddcoDTO> getAddSizeKeyList(BaddcoDTO baddco) {
		return billingMasterService.getAddCostSizeKey(baddco);
	}
	
	@GetMapping("/bm/billing/common/custkey")
	public List<ComboDataDTO> getSelectBoxCustkey(McusmaDTO param){
		return partnerService.getCustomerSelectBox(param);
	}
	
	/******************** doe078 : 할증기준 **************************/
	@GetMapping("/bm/billing/doe078/baddco")
	public List<BaddcoDTO> getBaddcoList(BaddcoDTO baddcoParams){
		return billingMasterService.getBaddcoList(baddcoParams);
	}
	
	@PostMapping("/bm/billing/doe078/save")
	public int saveDoe078Baddco(@RequestBody GridDTO<BaddcoDTO> baddcoParams){
		return billingMasterService.saveDoe078Baddco(baddcoParams);
	}
	
	/************************ doe079 : 정산유형 *********************/
	@GetMapping("/bm/billing/doe079/btrcat")
	public List<BtrcatDTO> getBtrcatList(BtrcatDTO btrcatParams){
		return billingMasterService.getBtrcatList(btrcatParams);
	}
	
	@GetMapping("/bm/billing/doe079/init")
	public BtrcatDTO getDoe079InitData(BtrcatDTO btrcatParams) {
		return billingMasterService.getDoe079InitData(btrcatParams);
	}
	
	@PostMapping("/bm/billing/doe079/save")
	public int saveDoe079Btrcat(@RequestBody GridDTO<BtrcatDTO> btrcatParams){
		return billingMasterService.saveDoe079Btrcat(btrcatParams);
	}

	/*********************** doe080 : 이송요율 ****************************/
	@GetMapping("/bm/billing/doe080/init")
	public BtrfmaDTO getDoe080InitData(BtrfmaDTO btrfma) {
		return billingMasterService.getDoe080InitData(btrfma);
	}
	
	@GetMapping("/bm/billing/doe080/bsizpa")
	public List<BsizpaDTO> getBsizpaList(BsizpaDTO bsizpaDTO) {
		return billingMasterService.getBsizpaList(bsizpaDTO);
	}
	
	@GetMapping("/bm/billing/doe080/grids")
	public List<BtrfmaDTO> getBtrfmaList(BtrfmaDTO btrfma) {
		return billingMasterService.getBtrfmaList(btrfma);
	}
	
	@PostMapping("/bm/billing/doe080/save")
	public int saveDoe080Data(@RequestBody BtrfmaSaveDTO billingSave) {
		return billingMasterService.saveDoe080Data(billingSave);
	}
	
	/*********************** doe081 : 운송요율 ****************************/
	@GetMapping("/bm/billing/doe081/init")
	public BtrpmaDTO getDoe081InitData(BtrpmaDTO btrpma) {
		return billingMasterService.getDoe081InitData(btrpma);
	}
	
	@GetMapping("/bm/billing/doe081/grids")
	public List<BtrpmaDTO> getBtrpmaList(BtrpmaDTO btrpma){
		return billingMasterService.getBtrpmaList(btrpma);
	}
	
	@GetMapping("/bm/billing/doe081/bsizpa")
	public List<BsizpaDTO> getDoe081BsizpaList(BsizpaDTO bsizpa) {
		return billingMasterService.getDoe081BsizpaList(bsizpa);
	}
	
	@PostMapping("/bm/billing/doe081/save")
	public int saveDoe081Data(@RequestBody HeadItemGridDTO<BtrpmaDTO, BsizpaDTO> requestBtrpma) {
		return billingMasterService.saveDoe081Data(requestBtrpma);
	}
	
	/*********************** doe082 : 이송정산비처리 ****************************/
	@GetMapping("/bm/billing/doe082/grid")
	public List<BtrfPrcDTO> getDoe082List(BtrfPrcDTO param){
		return billingMasterService.getDoe082List(param);
	}
	
	@PostMapping("/bm/billing/doe082/save")
	public int saveDoe082BtrfList(@RequestBody GridDTO<BtrfPrcDTO> param){
		return billingMasterService.saveDoe082BtrfProcessList(param);
	}
	
	/*********************** doe083 : 이송정산비검증 ****************************/
	//헤더그리드 조회
	@GetMapping("/bm/billing/doe083/grid/head")
	public List<BtrfhdDTO> getDoe083AfterHeadList(BtrfhdDTO param){
		return billingMasterService.getDoe083BtrfHeadList(param);
	}
	
	//마감 후 tab,청구처일괄저장 tab 사용 url
	@GetMapping("/bm/billing/doe083/grid/item")
	public List<BtrfitDTO> getDoe083ItemList(BtrfitDTO param){
		return billingMasterService.getBtrfItemList(param);
	}
	
	//검증완료
	@PatchMapping("/bm/billing/doe083/verified")
	public int saveBtrfList(@RequestBody UpdateGridDTO<BtrfhdDTO,BtrfitDTO> param){
		return billingMasterService.updateVerifiedBtrfList(param);
	}
	
	//마감취소
	@PatchMapping("/bm/billing/doe083/cancel")
	public int deleteBtrfhdAndItem(@RequestBody GridDTO<BtrfhdDTO> param){
		return billingMasterService.deleteBtrfhdAndItem(param);
	}
	
	//청구처 item 수정
	@PatchMapping("/bm/billing/doe083/btrfcbt-save")
	public int updateBtrfcbtList(@RequestBody GridDTO<BtrfitDTO> param){
		return billingMasterService.updateDoe083BtrfcbtList(param);
	}

	// 운송이송 마감 + 검증 (화면에서 사용X , 구현만)
	@PostMapping("/bm/billing/doe083/close-verified")
	public int saveAndVefiedBtrfhd(@RequestBody GridDTO<BtrfPrcDTO> param){
		return billingMasterService.closeWrcvitAndVefiedBtrfhd(param);
	}
	
	/* 
	 * doe084: 이송정산확정
	 */
	@GetMapping("/bm/billing/doe084/grid/head")
	public List<BtrfhdDTO> getDoe084HeadList(BtrfhdDTO param){
		return billingMasterService.getDoe084BtrfHeadList(param);
	}
	
	@GetMapping("/bm/billing/doe084/grid/item")
	public List<BtrfitDTO> getDoe084ItemList(BtrfitDTO param){
		return billingMasterService.getBtrfItemList(param);
	}
	
	@PatchMapping("/bm/billing/doe084/cancel")
	public int updateDoe084VerifiedCancel(@RequestBody List<BtrfhdDTO> btrfhdList){
		return billingMasterService.updateBtrfhdCancel(btrfhdList);
	}
	
	@PatchMapping("/bm/billing/doe084/confirm")
	public int updateDoe084Confirm(@RequestBody List<BtrfhdDTO> btrfhdList) {
		return billingMasterService.updateBtrfhdConfirm(btrfhdList);
	}
	
	@GetMapping("/bm/billing/doe084/verify")
	public List<BtrfhdDTO> getDoe084VerifyList(BtrfhdDTO btrfhdParam) {
		return billingMasterService.getDoe084VerifyList(btrfhdParam);			
	}
	
	@PatchMapping("/bm/billing/doe084/confirm-cancel")
	public int deleteDoe084ConfirmCancel(@RequestBody GridDTO<BtrfhdDTO> param){
		return billingMasterService.deleteBtrfhdAndItem(param);
	}
	
	@PatchMapping("/bm/billing/doe084/grid/save")
	public int updateDoe084Payadyn(@RequestBody GridDTO<BtrfhdDTO> param){
		return billingMasterService.updateDoe084Payadyn(param);
	}
	/* 
	 * doe085: 운송정산처리
	 */
	@GetMapping("/bm/billing/doe085/grid")
	public List<BtrpPrcDTO> getDoe082List(BtrpPrcDTO param){
		return billingMasterService.getDoe085List(param);
	}
	
	@PostMapping("/bm/billing/doe085/save")
	public int saveDoe085BtrpList(@RequestBody GridDTO<BtrpPrcDTO> param){
		return billingMasterService.saveDoe085BtrpProcessList(param);
	}
	
	/* 
	 * doe086: 운송정산검증
	 */
	
	@GetMapping("/bm/billing/doe086/grid/head")
	public List<BtrphdDTO> getDoe086AfterGridList(BtrphdDTO param){
		return billingMasterService.getBtrpHeadList(param);
	}
	
	@GetMapping("/bm/billing/doe086/grid/before")
	public List<BtrpPrcDTO> getDoe086BeforeGridList(BtrpPrcDTO param){
		return billingMasterService.getDoe085List(param);
	}
	
	@GetMapping("/bm/billing/doe086/grid/item")
	public List<BtrpitDTO> getDoe086BtrpitList(BtrpitDTO param){
		return billingMasterService.getBtrpItemList(param);
	}
	
	@PatchMapping("/bm/billing/doe086/cancel")
	public int deleteBtrphdAndItem(@RequestBody GridDTO<BtrphdDTO> param){
		return billingMasterService.deleteBtrphdAndItem(param);
	}
	
	@PatchMapping("/bm/billing/doe086/validate")
	public int updateDoe086Validate(@RequestBody UpdateGridDTO<BtrphdDTO, BtrpitDTO> param){
		return billingMasterService.updateDoe086BtrpList(param);
	} 
	
	@PatchMapping("/bm/billing/doe086/payad-cancel")
	public int updatePayadynCancel(@RequestBody List<BtrphdDTO> param){
		return billingMasterService.updatePayadynCancel(param);
	}
	
	//청구처 item 수정
	@PatchMapping("/bm/billing/doe086/btrpcbt-save")
	public int updateBtrpcbtList(@RequestBody GridDTO<BtrpitDTO> param){
		return billingMasterService.updateDoe086BtrpcbtList(param);
	}
	
	// 운송정산 마감 + 검증 (화면에서 사용X , 구현만)
	@PostMapping("/bm/billing/doe086/close-verified")
	public int saveAndVerifiedBtrphd(@RequestBody GridDTO<BtrpPrcDTO> param){
		return billingMasterService.closeTplnhdAndVerifiedBtrphd(param);
	}
	
	/* 
	 * doe087: 운송정산확정
	 */
	
	@GetMapping("/bm/billing/doe087/grid/head")
	public List<BtrphdDTO> getDoe087HeadList(BtrphdDTO param){
		return billingMasterService.getDoe087BtrpHeadList(param);
	}
	
	@GetMapping("/bm/billing/doe087/grid/item")
	public List<BtrpitDTO> getDoe087ItemList(BtrpitDTO param){
		return billingMasterService.getBtrpItemList(param);
	}
	
	@PatchMapping("/bm/billing/doe087/cancel")
	public int updateDoe087VerifiedCancel(@RequestBody List<BtrphdDTO> btrphdList){
		return billingMasterService.updateBtrphdCancel(btrphdList);
	}
	
	@PatchMapping("/bm/billing/doe087/confirm")
	public int updateDoe087Confirm(@RequestBody List<BtrphdDTO> btrphdList) {
		return billingMasterService.updateBtrphdConfirm(btrphdList);
	}
	
	@PatchMapping("/bm/billing/doe087/confirm-cancel")
	public int deleteDoe087ConfirmCancel(@RequestBody GridDTO<BtrphdDTO> param){
		return billingMasterService.deleteBtrphdAndItem(param);
	}
	
	@GetMapping("/bm/billing/doe087/verify")
	public List<BtrphdDTO> getDoe087VerifyList(BtrphdDTO btrphdParam) {
		return billingMasterService.getDoe087VerifyList(btrphdParam);			
	}
	
	/* 
	 * doe088: 정산집계내역
	 */
	@GetMapping("/bm/billing/doe088/grid/before")
	public List<BtrfitDTO> getDoe088BeforeGridList(BtrfitDTO btrfit){
		return billingMasterService.getDoe088BeforeGridList(btrfit);
	}
	
	@GetMapping("/bm/billing/doe088/grid/after")
	public List<BtrpitDTO> getDoe088BeforeGridList(BtrpitDTO btrpit){
		return billingMasterService.getDoe088AfterGridList(btrpit);
	}
	@GetMapping("/bm/billing/doe088/grid/modal")
	public List<BtrfPrcDTO> getDoe088BillSummaryModal(BtrfPrcDTO btrfprc){
		return billingMasterService.getDoe088BillSummaryList(btrfprc);
	}
}
