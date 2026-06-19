package com.kbph.logistics.bm.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

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
import com.kbph.logistics.bm.mapper.BillingMasterMapper;
import com.kbph.logistics.bm.type.Btrfitm;
import com.kbph.logistics.bm.type.Btrpitm;
import com.kbph.logistics.bm.type.Btrtype;
import com.kbph.logistics.bm.type.Crud;
import com.kbph.logistics.bm.type.Vowaffi;
import com.kbph.logistics.common.domain.GridDTO;
import com.kbph.logistics.common.domain.HeadItemGridDTO;
import com.kbph.logistics.common.enums.Useyn;
import com.kbph.logistics.common.util.SecurityUtils;
import com.kbph.logistics.configuration.error.DeleteCheckedException;
import com.kbph.logistics.configuration.error.DuplicateKeyAutowiredException;
import com.kbph.logistics.configuration.error.InsertCheckedException;
import com.kbph.logistics.configuration.error.RequiredNotValueException;
import com.kbph.logistics.configuration.error.UpdateCheckedException;
import com.kbph.logistics.md.domain.McodemDTO;
import com.kbph.logistics.md.domain.McusmaDTO;
import com.kbph.logistics.md.domain.MregmaDTO;
import com.kbph.logistics.md.mapper.CodeMapper;
import com.kbph.logistics.md.mapper.PartnerMapper;
import com.kbph.logistics.sy.domain.UserVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BillingMasterService {

	private final BillingMasterMapper billingMasterMapper;
	private final CodeMapper codeMapper;
	private final PartnerMapper partnerMapper;

	public List<BaddcoDTO> getAddCostSizeKey(BaddcoDTO baddco) {
		return billingMasterMapper.selectAddCostSizeKey(baddco, SecurityUtils.getSchema());
	}

	public List<BaddcoDTO> getBaddcoList(BaddcoDTO baddcoParams) {
		return billingMasterMapper.selectBaddcoList(baddcoParams, SecurityUtils.getSchema());
	}

	public int saveDoe078Baddco(GridDTO<BaddcoDTO> requestParam) {
		int insertResult=0;
		int updateResult=0;
		int deleteResult=0;

		UserVO userInfo = SecurityUtils.getCustomUserDetails().getUserInfo();

		//1. addList 일 경우 할증코드 채번
		for(BaddcoDTO baddco : requestParam.getAddList()) {
			String addcoky = billingMasterMapper.getAddcoky(userInfo);
			baddco.setAddcoky(addcoky);		//채번된 할증코드
			insertResult = billingMasterMapper.insertBaddco(baddco,userInfo);

			if (insertResult == 0) {
				throw new InsertCheckedException();
			}
		}

		//2. updateList일 경우 길이, 폭 중복값 확인
		for(BaddcoDTO baddco : requestParam.getUpdateList()) {
			updateResult = billingMasterMapper.updateBaddco(baddco,userInfo);
			if (updateResult == 0) {
				throw new UpdateCheckedException();
			}
		}

		//3. deleteList일 경우 삭제
		for(BaddcoDTO baddco : requestParam.getDeleteList()) {
			deleteResult = billingMasterMapper.deleteBaddco(baddco,userInfo);
			if (deleteResult == 0) {
				throw new DeleteCheckedException();
			}
		}

		return insertResult + updateResult + deleteResult;
	}

	public List<BtrcatDTO> getBtrcatList(BtrcatDTO btrcatParams){
		return billingMasterMapper.selectBtrcatList(btrcatParams, SecurityUtils.getSchema());
	}

	public BtrcatDTO getDoe079InitData(BtrcatDTO btrcatParams) {
		UserVO userInfo = SecurityUtils.getCustomUserDetails().getUserInfo();
		McodemDTO mcodemParam = new McodemDTO();
		mcodemParam.setComcdky(Btrtype.BTRF_CODE.getString());
		btrcatParams.setBtrfbtpList(codeMapper.selectMcodemList(mcodemParam,userInfo));	//이송

		mcodemParam.setComcdky(Btrtype.BTRP_CODE.getString());
		btrcatParams.setBtrpbtpList(codeMapper.selectMcodemList(mcodemParam,userInfo));	//운송
		return btrcatParams;
	}

	public int saveDoe079Btrcat(GridDTO<BtrcatDTO> requestParam) {
		UserVO userInfo = SecurityUtils.getCustomUserDetails().getUserInfo();

		int insertResult=0;
		int updateResult=0;
		int deleteResult=0;

		for(BtrcatDTO btrcat : requestParam.getAddList()) {
			//1. addList 일 경우  정산유형코드 채번
			String btrcate = billingMasterMapper.getBtrcate(userInfo);
			
			btrcat.setBtrcate(btrcate);
			insertResult = billingMasterMapper.insertBtrcat(btrcat,userInfo);
			if (insertResult == 0) {
				throw new InsertCheckedException();
			}
		}

		//2. updateList일 경우
		for(BtrcatDTO btrcat : requestParam.getUpdateList()) {
			updateResult = billingMasterMapper.updateBtrcat(btrcat,userInfo);
			if (updateResult == 0) {
				throw new UpdateCheckedException();
			}
		}

		//3. deleteList일 경우 삭제
		for(BtrcatDTO btrcat : requestParam.getDeleteList()) {
			deleteResult = billingMasterMapper.deleteBtrcat(btrcat,userInfo);
			if (deleteResult == 0) {
				throw new DeleteCheckedException();
			}
		}

		return insertResult + updateResult + deleteResult;
	}

	public BtrfmaDTO getDoe080InitData(BtrfmaDTO btrfma) {
		BtrcatDTO btrcat = new BtrcatDTO();
		btrfma.setBtrcatList(billingMasterMapper.selectBtrcatList(btrcat, SecurityUtils.getSchema()));

		UserVO userInfo = SecurityUtils.getCustomUserDetails().getUserInfo();
		McodemDTO mcodem = new McodemDTO();
		mcodem.setComcdky(Btrfitm.CODE.getString());
		btrfma.setCodeList(codeMapper.selectMcodemList(mcodem,userInfo));
		mcodem.setComcdky(Btrfitm.STNWEIG.getString());
		btrfma.setStnweigList(codeMapper.selectMcodemList(mcodem,userInfo));

		return btrfma;
	}

	public List<BsizpaDTO> getBsizpaList(BsizpaDTO bsizpa) {
		return billingMasterMapper.selectBsizpaList(bsizpa, SecurityUtils.getSchema());
	}

	public List<BtrfmaDTO> getBtrfmaList(BtrfmaDTO btrfma) {
		return billingMasterMapper.selectBtrfmaList(btrfma, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	public int saveDoe080Data(BtrfmaSaveDTO billingSave) {
		int insertResult = 0;
		int updateResult = 0;
		int deleteResult = 0;
		GridDTO<BtrfmaDTO> btrfmaParam = billingSave.getMainGridParam();
		GridDTO<BsizpaDTO> bsizpaParam = billingSave.getItemGridParam();
		List<BsizpaDTO> bsizpaAddList = bsizpaParam.getAddList();
		UserVO userInfo = SecurityUtils.getCustomUserDetails().getUserInfo();

		String btrflsq = billingMasterMapper.getBtrflsq(userInfo);
		for(BtrfmaDTO btrfma : btrfmaParam.getAddList()) {
			if(!billingMasterMapper.selectBtrfmaList(btrfma, userInfo).isEmpty()) {
				throw new DuplicateKeyAutowiredException();
			}
			String btrfkey = billingMasterMapper.getBtrfkey(userInfo);
			btrfma.setBtrfkey(btrfkey);
			insertResult = billingMasterMapper.insertBtrfma(btrfma, userInfo);
			btrfma.setBtrflsq(btrflsq);
			btrfma.setCrudmod(Crud.CREATE.getString());
			int result = billingMasterMapper.insertBtrfmaLog(btrfma, userInfo);
			if (insertResult == 0 || result == 0) {
				throw new InsertCheckedException();
			}
			for(BsizpaDTO bsizpa : bsizpaAddList) {
				if(btrfma.getPqRi().equals(bsizpa.getBtrnkey())) {
					bsizpa.setBtrnkey(btrfkey);
				}
			}
		}

		for(BtrfmaDTO btrfma : btrfmaParam.getUpdateList()) {
			updateResult = billingMasterMapper.updateBtrfma(btrfma, userInfo);
			btrfma.setBtrflsq(btrflsq);
			btrfma.setCrudmod(Crud.UPDATE.getString());
			int result = billingMasterMapper.insertBtrfmaLog(btrfma, userInfo);

			if (updateResult == 0 || result == 0) {
				throw new UpdateCheckedException();
			}
		}

		for(BtrfmaDTO btrfma : btrfmaParam.getDeleteList()) {
			deleteResult = billingMasterMapper.deleteBtrfma(btrfma,userInfo);
			btrfma.setBtrflsq(btrflsq);
			btrfma.setCrudmod(Crud.DELETE.getString());
			int result = billingMasterMapper.insertBtrfmaLog(btrfma, userInfo);

			if(deleteResult == 0 || result == 0) {
				throw new DeleteCheckedException();
			}
		}
		//BSIZPA TABLE CUD
		saveBsizpaData(bsizpaParam);

		return insertResult + updateResult + deleteResult;
	}

	public int saveBsizpaData(GridDTO<BsizpaDTO> requestParam) {
		int insertResult=0;
		int updateResult=0;
		int deleteResult=0;

		UserVO userInfo = SecurityUtils.getCustomUserDetails().getUserInfo();

		for(BsizpaDTO bsizpa : requestParam.getAddList()) {
			insertResult = billingMasterMapper.insertBsizpa(bsizpa, userInfo);
			if (insertResult == 0) {
				throw new InsertCheckedException();
			}
		}

		for(BsizpaDTO btrfma : requestParam.getUpdateList()) {
			updateResult = billingMasterMapper.updateBsizpa(btrfma, userInfo);
			if (updateResult == 0) {
				throw new UpdateCheckedException();
			}
		}

		for(BsizpaDTO bsizpa : requestParam.getDeleteList()) {
			deleteResult = billingMasterMapper.deleteBsizpa(bsizpa, userInfo);
			if(deleteResult == 0) {
				throw new DeleteCheckedException();
			}
		}

		return insertResult + updateResult + deleteResult;
	}

	/* doe081 : 운송요율 */
	public List<BtrpmaDTO> getBtrpmaList(BtrpmaDTO btrpma) {
		return billingMasterMapper.selectBtrpmaList(btrpma,SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	public BtrpmaDTO getDoe081InitData(BtrpmaDTO btrpma) {
		BtrcatDTO btrcat = new BtrcatDTO();
		btrpma.setBtrcatList(billingMasterMapper.selectBtrcatList(btrcat, SecurityUtils.getSchema()));	//정산유형

		UserVO userInfo = SecurityUtils.getCustomUserDetails().getUserInfo();
		McodemDTO mcodem = new McodemDTO();
		mcodem.setComcdky(Btrpitm.CODE.getCode());
		btrpma.setBtrpItemList(codeMapper.selectMcodemList(mcodem,userInfo));	//운송정산비 항목

		mcodem.setComcdky(Vowaffi.CODE.getString());
		btrpma.setVowaffiList(codeMapper.selectMcodemList(mcodem,userInfo));		//차량소속
		
		mcodem.setComcdky(Btrfitm.STNWEIG.getString());
		btrpma.setStnweigList(codeMapper.selectMcodemList(mcodem,userInfo));		//기준중량

		MregmaDTO mregma = new MregmaDTO();
		btrpma.setRegionList(partnerMapper.selectMregmaSelectBox(mregma,userInfo));

		return btrpma;
	}
	//운송요율 사이즈 지급단가 GET
	public List<BsizpaDTO> getDoe081BsizpaList(BsizpaDTO bsizpa) {
		return billingMasterMapper.selectDoe081BsizpaList(bsizpa, SecurityUtils.getCustomUserDetails().getUserInfo());
	}
	
	//운송요율 저장 및 로그 저장
	public int saveDoe081Data(HeadItemGridDTO<BtrpmaDTO, BsizpaDTO> requestParam) {
		int insertResult=0;
		int updateResult=0;
		int deleteResult=0;

		GridDTO<BtrpmaDTO> headGrid = requestParam.getHeadGrid();
		GridDTO<BsizpaDTO> itemGrid = requestParam.getItemGrid();

		List<BsizpaDTO> bsizpaAddList = itemGrid.getAddList();

		UserVO userInfo = SecurityUtils.getCustomUserDetails().getUserInfo();

		for(BtrpmaDTO btrpma : headGrid.getAddList()) {
			if(!billingMasterMapper.selectBtrpmaList(btrpma, userInfo).isEmpty()) {
				throw new DuplicateKeyAutowiredException();
			}
			//운송요율코드 채번
			String btrpkey = billingMasterMapper.getBtrpkey(userInfo);
			btrpma.setBtrpkey(btrpkey);
			insertResult = billingMasterMapper.insertBtrpma(btrpma, userInfo);

			//운송요율 로그번호 채번
			String btrplsq = billingMasterMapper.getBtrplsq(userInfo);
			btrpma.setBtrplsq(btrplsq);
			btrpma.setCrudmod(Crud.CREATE.getString());
			int logResult = billingMasterMapper.insertBtrpmaLog(btrpma, userInfo);

			if (insertResult == 0 || logResult == 0) {
				throw new InsertCheckedException();
			}

			// rowkey 없이 인서트 되는 경우 따로 요율코드 넣은 후 아래에서 save
			for(BsizpaDTO bsizpa : bsizpaAddList) {
				if(btrpma.getPqRi().equals(bsizpa.getBtrnkey())) {
					bsizpa.setBtrnkey(btrpkey);
				}
			}
		}

		for(BtrpmaDTO btrpma : headGrid.getUpdateList()) {
			updateResult = billingMasterMapper.updateBtrpma(btrpma, userInfo);

			//운송요율 로그번호 채번
			String btrplsq = billingMasterMapper.getBtrplsq(userInfo);
			btrpma.setBtrplsq(btrplsq);
			btrpma.setCrudmod(Crud.UPDATE.getString());
			int logResult = billingMasterMapper.insertBtrpmaLog(btrpma, userInfo);

			if (updateResult == 0 || logResult == 0) {
				throw new UpdateCheckedException();
			}
		}

		for(BtrpmaDTO btrpma : headGrid.getDeleteList()) {
			deleteResult = billingMasterMapper.deleteBtrpma(btrpma, userInfo);

			//운송요율 로그번호 채번
			String btrplsq = billingMasterMapper.getBtrplsq(userInfo);
			btrpma.setBtrplsq(btrplsq);
			btrpma.setCrudmod(Crud.DELETE.getString());
			int logResult = billingMasterMapper.insertBtrpmaLog(btrpma, userInfo);

			//요율 삭제시 BSIZPA에 요율코드에 해당하는 데이터가 있다면 같이 delete
			if(btrpma.getSizgbyn() != null && btrpma.getSizgbyn().equals(Useyn.USE.getString())) {
				BsizpaDTO bsizpa = new BsizpaDTO();
				bsizpa.setBtrnkey(btrpma.getBtrpkey());
				List<BsizpaDTO> bsizpaList = billingMasterMapper.selectDoe081BsizpaList(bsizpa, userInfo);
				for(BsizpaDTO bsizpaData : bsizpaList) {
					int sizeDeleteResult = billingMasterMapper.deleteBsizpa(bsizpaData, userInfo);
					if(sizeDeleteResult == 0) {
						throw new DeleteCheckedException();
					}
				}
			}

			if (deleteResult == 0 || logResult == 0) {
				throw new DeleteCheckedException();
			}
		}
		saveBsizpaData(itemGrid);	//사이즈 지급단가 SAVE
		return insertResult + updateResult + deleteResult;
	}

	/************************
	 * 이송정산
	 ***********************/
	public List<BtrfPrcDTO> getDoe082List(BtrfPrcDTO btrfParam){
		UserVO userInfo = SecurityUtils.getCustomUserDetails().getUserInfo();
		//이송요율 select시 사용하기 위한 dto value setting
		btrfParam.setTransfer(Btrfitm.TRANSFER.getString());
		btrfParam.setLoading(Btrfitm.LOADING.getString());
		btrfParam.setSelection(Btrfitm.SELECTION.getString());
		btrfParam.setStorage(Btrfitm.STORAGE.getString());
		btrfParam.setStnweig(Btrfitm.GROSS.getString());
		btrfParam.setGrossWgName(Btrfitm.GROSSWG.getString());
		btrfParam.setSkuWeigName(Btrfitm.SKUWEIG.getString());
		return billingMasterMapper.selectDoe082List(btrfParam, userInfo);
	}
	
	public int saveDoe082BtrfProcessList(GridDTO<BtrfPrcDTO> btrfParam) {
	    UserVO userInfo = SecurityUtils.getCustomUserDetails().getUserInfo(); 
	    int btrfhdInsertCnt = 0; 
	    int btrfitInsertCnt = 0; 
	    int wrcvitUpdateCnt = 0;
	    int itemCnt = 0;
	    Map<String, BtrfhdDTO> eoasnkyMap = new HashMap<>();
	    
	    for(BtrfPrcDTO btrfData : btrfParam.getAddList()) {
	    	if(!eoasnkyMap.containsKey(btrfData.getEoasnky())){					//eoasnky가 Map안에 있는지 확인
	    		itemCnt = 1;
	    		String bfhdkey = billingMasterMapper.getBfhdkey(userInfo);				//bfhdkey 채번
	    		BtrfhdDTO btrfhd = new BtrfhdDTO();
	    		btrfhd.setRcvdcky(btrfData.getRcvdcky());
	    		btrfhd.setWarekey(btrfData.getWarekey());
	    		btrfhd.setBfhdkey(bfhdkey);
	    		btrfhd.setCalweig(btrfData.getCalweig());
	    		btrfhd.setTrfdfsd(24000); 										//부적기준중량 (임시)
	    		btrfhd.setFcvrfyn(Useyn.UNUSE.getString());
	    		btrfhd.setFccfmyn(Useyn.UNUSE.getString());
	    		btrfhd.setPoshpdt(btrfData.getPoshpdt());
	    		eoasnkyMap.put(btrfData.getEoasnky(), btrfhd);					//map에 put
	    	}
	    	btrfData.setBfhdkey(eoasnkyMap.get(btrfData.getEoasnky()).getBfhdkey());
	    	btrfData.setDoccate(Btrfitm.DOCCATE.getString());	//900
	    	btrfData.setDoctype(Btrfitm.DOCTYPE.getString());	//910
	    	btrfData.setBfhdcit(itemCnt);
	    	btrfitInsertCnt = billingMasterMapper.insertBtrfit(btrfData, userInfo);		//BTRFIT 테이블 먼저 INSERT
	    	itemCnt ++;
	    	if(btrfitInsertCnt == 0 ) {
	    		throw new InsertCheckedException();
	    	}
	    }

	    for (BtrfhdDTO btrfhd : eoasnkyMap.values()) {
	        btrfhdInsertCnt = billingMasterMapper.insertBtrfhdFromBtrfit(btrfhd, userInfo);	//BTRFHD 테이블 인서트
	        if (btrfhdInsertCnt == 0) {
	            throw new InsertCheckedException(); 
	        }
	        btrfhd.setIclosyn(Useyn.USE.getString());			//WRCVIT 마감여부 컬럼 'Y'
	        wrcvitUpdateCnt = billingMasterMapper.updateWrcvhdCloseyn(btrfhd, userInfo);	//WRCVHD 마감여부 'Y' UPDATE
	    	if(wrcvitUpdateCnt == 0) {
	    		throw new UpdateCheckedException();
	    	}
	    }
	   
	    return btrfhdInsertCnt + btrfitInsertCnt + wrcvitUpdateCnt ;
	}

	//이송정산비검증 : doe083
	public BtrfitDTO getDoe083InitData(BtrfitDTO param) {
		McusmaDTO params = new McusmaDTO();
		param.setCustkeyList(partnerMapper.selectMcusmaSelectBox(params, SecurityUtils.getCustomUserDetails().getUserInfo()));
		return param;
	}
	
	public List<BtrfhdDTO> getDoe083BtrfHeadList(BtrfhdDTO btrfParam){
		UserVO userInfo = SecurityUtils.getCustomUserDetails().getUserInfo();
		btrfParam.setGrossWgName(Btrfitm.GROSSWG.getString());
		btrfParam.setFcvrfyn(Useyn.UNUSE.getString());			//검증 'N'
		return billingMasterMapper.selectBtrfhdList(btrfParam, userInfo);
	}
	
	public List<BtrfitDTO> getBtrfItemList(BtrfitDTO btrfParam){
		UserVO userInfo = SecurityUtils.getCustomUserDetails().getUserInfo();
		return billingMasterMapper.selectBtrfitList(btrfParam, userInfo);
	}
	
	//이송정산 검증완료 UPDATE
	public int updateVerifiedBtrfList(UpdateGridDTO<BtrfhdDTO,BtrfitDTO> btrfParam){
		UserVO userInfo = SecurityUtils.getCustomUserDetails().getUserInfo();
		int headResultCnt = 0;
		int itemResultCnt = 0;
		
		// 헤드 그리드의 검증여부 Y -> 아이템 그리드의 updateList(청구처 , 추가요금 등) 있을 경우 update 처리 필요
		for(BtrfhdDTO btrfhd : btrfParam.getHeadUpdateList()) {
			btrfhd.setFcvrfyn(Useyn.USE.getString());	//검증여부 Y
			headResultCnt = billingMasterMapper.updateBtrfhdList(btrfhd,userInfo);
			
			if(headResultCnt == 0) {
				throw new UpdateCheckedException();
			}
		}
		
		//아이템그리드 update 및 청구처 필수값 확인
		for(BtrfitDTO btrfit : btrfParam.getItemUpdateList()) {
			//청구처 값이 없을경우 exception
			if(btrfit.getBtrfcbt() == null || btrfit.getBtrfcbt().trim().isEmpty()){
				throw new RequiredNotValueException();		//필수값 Exception
			}
			itemResultCnt = billingMasterMapper.updateBtrfitList(btrfit,userInfo);
			if(itemResultCnt == 0) {
				throw new UpdateCheckedException();
			}
		}
		
		return headResultCnt + itemResultCnt;
	}
	//이송정산 마감취소 - (확정페이지에서 확정취소시에도 사용)
	public int deleteBtrfhdAndItem(GridDTO<BtrfhdDTO> btrfhd) {
		UserVO userInfo = SecurityUtils.getCustomUserDetails().getUserInfo();
		int deleteBtrfhdCnt = 0;
		int deleteBtrfitCnt = 0;
		int updateWrcvhdCnt = 0;
		for(BtrfhdDTO btrfhdData : btrfhd.getUpdateList()) {
			BtrfPrcDTO btrfit = new BtrfPrcDTO();
			btrfit.setBfhdkey(btrfhdData.getBfhdkey());
			btrfit.setRcvdcky(btrfhdData.getRcvdcky());
			btrfit.setWarekey(btrfhdData.getWarekey());
			deleteBtrfitCnt =  billingMasterMapper.deleteBtrfit(btrfit, userInfo);
			deleteBtrfhdCnt =  billingMasterMapper.deleteBtrfhd(btrfhdData, userInfo);
			if(deleteBtrfitCnt == 0 || deleteBtrfhdCnt == 0) {
				throw new DeleteCheckedException();
			}
			btrfhdData.setIclosyn(Useyn.UNUSE.getString());
			btrfhdData.setBfhdkey("");
			updateWrcvhdCnt = billingMasterMapper.updateWrcvhdCloseyn(btrfhdData, userInfo);
			if(updateWrcvhdCnt == 0) {
				throw new UpdateCheckedException();
			}
		}
		return deleteBtrfhdCnt + deleteBtrfitCnt + updateWrcvhdCnt;
	}
	
	//이송정산 청구처 수정
	public int updateDoe083BtrfcbtList(GridDTO<BtrfitDTO> btrfit) {
		UserVO userInfo = SecurityUtils.getCustomUserDetails().getUserInfo();
		int result = 0;
		for(BtrfitDTO btrfitData : btrfit.getUpdateList()) {
			if(btrfitData.getBtrfcbt() == null || btrfitData.getBtrfcbt().trim().isEmpty()){
				throw new RequiredNotValueException();		//필수값 Exception
			}
			result += billingMasterMapper.updateDoe083BtrfcbtList(btrfitData,userInfo);
		}
		if(result != btrfit.getUpdateList().size()) {
			throw new UpdateCheckedException();
		}
		return result;
	}
	
	// 이송정산 선지급 (선지급은 운송에만 존재하나 이송에서 필요할 시 구현 가능 서비스 로직)
	public int updateDoe084Payadyn(GridDTO<BtrfhdDTO> btrfhdList) {
		UserVO userInfo = SecurityUtils.getCustomUserDetails().getUserInfo();
		int btrfhdResult = 0;
		int wrcvhdResult = 0;
		for(BtrfhdDTO btrfhd : btrfhdList.getUpdateList()) {
			btrfhdResult = billingMasterMapper.updateDoe084BtrfhdPayadyn(btrfhd,userInfo);
			wrcvhdResult = billingMasterMapper.updateDoe084WrcvhdPayadyn(btrfhd,userInfo);
			if(btrfhdResult == 0 || wrcvhdResult == 0) {
				throw new UpdateCheckedException();
			}
		}
		return btrfhdResult + wrcvhdResult;
	}
	//이송정산 확정완료
	public int updateBtrfhdConfirm(List<BtrfhdDTO> btrfhdList) {
		UserVO userInfo = SecurityUtils.getCustomUserDetails().getUserInfo();
		int result = 0;
		int wrcvhdResult = 0;
		for(BtrfhdDTO btrfhd : btrfhdList) {
			btrfhd.setFccfmyn(Useyn.USE.getString());
			result += billingMasterMapper.updateBtrfhdConfirm(btrfhd, userInfo);
			if(btrfhd.getPayadyn() != null) {
				wrcvhdResult = billingMasterMapper.updateDoe084WrcvhdPayadyn(btrfhd,userInfo);
				if(wrcvhdResult == 0) {
					throw new UpdateCheckedException();
				}
			}
		}
		if(result != btrfhdList.size()) {
			throw new UpdateCheckedException();
		}
		
		return result;		
	}
	//이송정산 검증취소
	public int updateBtrfhdCancel(List<BtrfhdDTO> btrfhdList) {
		int result = 0;
		for(BtrfhdDTO btrfhd : btrfhdList) {
			btrfhd.setFcvrfyn(Useyn.UNUSE.getString());
			result += billingMasterMapper.updateBtrfhdList(btrfhd, SecurityUtils.getCustomUserDetails().getUserInfo());
		}
		if(result != btrfhdList.size()) {
			throw new UpdateCheckedException();
		}
		
		return result;		
	}
	
	// 이송정산 마감 + 검증 (화면에서 사용X , 구현만)
	public int closeWrcvitAndVefiedBtrfhd(GridDTO<BtrfPrcDTO> btrfParam){
		UserVO userInfo = SecurityUtils.getCustomUserDetails().getUserInfo(); 
	    int btrfhdInsertCnt = 0; 
	    int btrfitInsertCnt = 0; 
	    int wrcvitUpdateCnt = 0;
	    int btrfhdUpdateCnt = 0;
	    Map<String, BtrfhdDTO> eoasnkyMap = new HashMap<>();
	    
	    for(BtrfPrcDTO btrfData : btrfParam.getAddList()) {
	    	if(!eoasnkyMap.containsKey(btrfData.getEoasnky())){					//eoasnky가 Map안에 있는지 확인
	    		String bfhdkey = billingMasterMapper.getBfhdkey(userInfo);				//bfhdkey 채번
	    		
	    		BtrfhdDTO btrfhd = new BtrfhdDTO();
	    		btrfhd.setWarekey(btrfData.getWarekey());
	    		btrfhd.setRcvdcky(btrfData.getRcvdcky());
	    		btrfhd.setBfhdkey(bfhdkey);
	    		btrfhd.setCalweig(btrfData.getCalweig());    		
	    		btrfhd.setFcvrfyn(Useyn.UNUSE.getString());
	    		btrfhd.setFccfmyn(Useyn.UNUSE.getString());
	    		btrfhd.setTrfdfsd(24000); 			//부적기준중량 (임시)
	    		eoasnkyMap.put(btrfData.getEoasnky(), btrfhd);					//map에 put
	    	}
	    	btrfData.setBfhdkey(eoasnkyMap.get(btrfData.getEoasnky()).getBfhdkey());
	    	btrfData.setDoccate(Btrfitm.DOCCATE.getString());	//900
	    	btrfData.setDoctype(Btrfitm.DOCTYPE.getString());	//910
	    	btrfitInsertCnt = billingMasterMapper.insertBtrfit(btrfData, userInfo);
	    	
	    	if(btrfitInsertCnt == 0 ) {
	    		throw new InsertCheckedException();
	    	}
	    }

	    for (BtrfhdDTO btrfhd : eoasnkyMap.values()) {
	        btrfhdInsertCnt = billingMasterMapper.insertBtrfhdFromBtrfit(btrfhd, userInfo);	//헤더 인서트
	        if (btrfhdInsertCnt == 0) {
	            throw new InsertCheckedException(); 
	        }
	        btrfhd.setIclosyn(Useyn.USE.getString());			//WRCVIT 마감여부 컬럼 'Y'
	    	wrcvitUpdateCnt = billingMasterMapper.updateWrcvhdCloseyn(btrfhd, userInfo);	//마감여부 'Y' UPDATE
	    	if(wrcvitUpdateCnt == 0) {
	    		throw new UpdateCheckedException();
	    	}
	    	
	        btrfhd.setFcvrfyn(Useyn.USE.getString());
	        btrfhdUpdateCnt = billingMasterMapper.updateBtrfhdList(btrfhd,userInfo);		//검증여부 'Y'업데이트
	        if(btrfhdUpdateCnt == 0) {
	    		throw new UpdateCheckedException();
	    	}
	    }
	    return btrfhdInsertCnt + btrfitInsertCnt + wrcvitUpdateCnt + btrfhdUpdateCnt ;
	}
	
	public BtrfitDTO getDoe084InitData(BtrfitDTO param) {
		McusmaDTO params = new McusmaDTO();
		param.setCustkeyList(partnerMapper.selectMcusmaSelectBox(params, SecurityUtils.getCustomUserDetails().getUserInfo()));
		return param;
	}
	
	public List<BtrfhdDTO> getDoe084BtrfHeadList(BtrfhdDTO btrfParam){
		UserVO userInfo = SecurityUtils.getCustomUserDetails().getUserInfo();
		btrfParam.setGrossWgName(Btrfitm.GROSSWG.getString());
		btrfParam.setBtrstat(btrfParam.getBtrstat().toUpperCase());
		return billingMasterMapper.selectBtrfhdList(btrfParam, userInfo);
	}
	//이송정산 검증요약
	public List<BtrfhdDTO> getDoe084VerifyList(BtrfhdDTO btrfhdParam) {
		return billingMasterMapper.selectDoe084VerifySummary(btrfhdParam, SecurityUtils.getCustomUserDetails().getUserInfo());
	}
	
	/************************
	 * 운송정산
	 ***********************/
	public BtrpitDTO getDoe086InitData(BtrpitDTO param) {
		McusmaDTO params = new McusmaDTO();
		param.setCustkeyList(partnerMapper.selectMcusmaSelectBox(params, SecurityUtils.getCustomUserDetails().getUserInfo()));
		return param;
	}
	public List<BtrpPrcDTO> getDoe085List(BtrpPrcDTO btrpParam){
		UserVO userInfo = SecurityUtils.getCustomUserDetails().getUserInfo();
		btrpParam.setStnweig(Btrfitm.GROSS.getString());
		btrpParam.setGrossWgName(Btrfitm.GROSSWG.getString());
		btrpParam.setSkuWeigName(Btrfitm.SKUWEIG.getString());
		btrpParam.setBtrpitm(Btrpitm.CODE.getString());
		return billingMasterMapper.selectDoe085List(btrpParam, userInfo);
	}
	
	//운송정산 마감처리
	public int saveDoe085BtrpProcessList(GridDTO<BtrpPrcDTO> btrpParam) {
		UserVO userInfo = SecurityUtils.getCustomUserDetails().getUserInfo();
		int btrphdInsertCnt = 0;
		int btrpitInsertCnt = 0;
		int tplnhdUpdateCnt = 0;
		int itemCnt = 0;
		Map<String, BtrphdDTO> vhplnkyMap = new HashMap<>();
	    
	    for(BtrpPrcDTO btrpData : btrpParam.getAddList()) {
	    	if(!vhplnkyMap.containsKey(btrpData.getVhplnky())){					//vhplnky가 Map안에 있는지 확인
	    		itemCnt = 1;
	    		String bphdkey = billingMasterMapper.getBphdkey(userInfo);				//bphdkey 채번
	    		BtrphdDTO btrphd = new BtrphdDTO();
	    		btrphd.setVhplnky(btrpData.getVhplnky());
	    		btrphd.setWarekey(btrpData.getWarekey());
	    		btrphd.setBphdkey(bphdkey);
	    		btrphd.setCalweig(btrpData.getCalweig());
	    		btrphd.setTrpdfsd(24000); 										//부적운임기준중량 (임시) 
	    		btrphd.setPcvrfyn(Useyn.UNUSE.getString());
	    		btrphd.setPccfmyn(Useyn.UNUSE.getString());
	    		vhplnkyMap.put(btrpData.getVhplnky(), btrphd);					//map에 put
	    	}
	    	btrpData.setBphdkey(vhplnkyMap.get(btrpData.getVhplnky()).getBphdkey());
	    	btrpData.setDoccate(Btrpitm.DOCCATE.getCode());	//900
	    	btrpData.setDoctype(Btrpitm.DOCCATE.getString());	//920
	    	btrpData.setBphdcit(itemCnt);
	    	btrpitInsertCnt = billingMasterMapper.insertBtrpitList(btrpData, userInfo);
	    	itemCnt ++;
	    	if(btrpitInsertCnt == 0 ) {
	    		throw new InsertCheckedException();
	    	}
	    }

	    for (BtrphdDTO btrphd : vhplnkyMap.values()) {
	        btrphdInsertCnt = billingMasterMapper.insertBtrphdFromBtrpit(btrphd, userInfo);	//헤더 인서트
	        if (btrphdInsertCnt == 0) {
	            throw new InsertCheckedException(); 
	        }
	        btrphd.setTclosyn(Useyn.USE.getString());			//TPLNHD 마감여부 컬럼 'Y'
	        tplnhdUpdateCnt = billingMasterMapper.updateTplnhdCloseyn(btrphd, userInfo);	//마감여부 'Y' UPDATE
	    	if(tplnhdUpdateCnt == 0) {
	    		throw new UpdateCheckedException();
	    	}
	    }
		return btrphdInsertCnt + btrpitInsertCnt + tplnhdUpdateCnt;
	}

	public List<BtrphdDTO> getBtrpHeadList(BtrphdDTO btrpParam){
		UserVO userInfo = SecurityUtils.getCustomUserDetails().getUserInfo();
		btrpParam.setGrossWgName(Btrfitm.GROSSWG.getString());
		btrpParam.setPcvrfyn(Useyn.UNUSE.getString());
		return billingMasterMapper.selectBtrphdList(btrpParam, userInfo);
	}
	
	public List<BtrphdDTO> getDoe087BtrpHeadList(BtrphdDTO btrpParam){
		UserVO userInfo = SecurityUtils.getCustomUserDetails().getUserInfo();
		btrpParam.setGrossWgName(Btrfitm.GROSSWG.getString());
		btrpParam.setPcvrfyn(Useyn.USE.getString());
		return billingMasterMapper.selectBtrphdList(btrpParam, userInfo);
	}
	
	public List<BtrpitDTO> getBtrpItemList(BtrpitDTO btrpParam){
		UserVO userInfo = SecurityUtils.getCustomUserDetails().getUserInfo();
		return billingMasterMapper.selectBtrpitList(btrpParam, userInfo);
	}
	
	public int deleteBtrphdAndItem(GridDTO<BtrphdDTO> btrphd) {
		UserVO userInfo = SecurityUtils.getCustomUserDetails().getUserInfo();
		int deleteBtrphdCnt = 0;
		int deleteBtrpitCnt = 0;
		int updateTplnhdCnt = 0;
		for(BtrphdDTO btrphdData : btrphd.getUpdateList()) {
			BtrpitDTO btrpit = new BtrpitDTO();
			btrpit.setBphdkey(btrphdData.getBphdkey());
			btrpit.setVhplnky(btrphdData.getVhplnky());
			btrpit.setWarekey(btrphdData.getWarekey());
			deleteBtrpitCnt =  billingMasterMapper.deleteBtrpit(btrpit, userInfo);
			deleteBtrphdCnt =  billingMasterMapper.deleteBtrphd(btrphdData, userInfo);
			if(deleteBtrpitCnt == 0 || deleteBtrphdCnt == 0) {
				throw new DeleteCheckedException();
			}
			
			btrphdData.setTclosyn(Useyn.UNUSE.getString());
			updateTplnhdCnt = billingMasterMapper.updateTplnhdCloseyn(btrphdData, userInfo);
			if(updateTplnhdCnt == 0) {
				throw new UpdateCheckedException();
			}
		}
		return deleteBtrphdCnt + deleteBtrpitCnt + updateTplnhdCnt;
	}
	
	public int updateDoe086BtrpList(UpdateGridDTO<BtrphdDTO, BtrpitDTO> param){
		UserVO userInfo = SecurityUtils.getCustomUserDetails().getUserInfo();
		int headResultCnt = 0;
		int itemResultCnt = 0;
		
		for(BtrphdDTO btrphd : param.getHeadUpdateList()) {
			btrphd.setPcvrfyn(Useyn.USE.getString());	//검증여부 Y
			headResultCnt += billingMasterMapper.updateDoe086Validate(btrphd, userInfo);
		}

		if(headResultCnt != param.getHeadUpdateList().size()) {
			throw new UpdateCheckedException();
		}
		
		for(BtrpitDTO btrpit : param.getItemUpdateList()) {
			if(btrpit.getBtrpcbt() == null || btrpit.getBtrpcbt().trim().isEmpty()) {
				throw new RequiredNotValueException();		//필수값 미입력시 exception
			}
			itemResultCnt += billingMasterMapper.updateDoe086Item(btrpit, userInfo);
		}

		if(itemResultCnt != param.getItemUpdateList().size()) {
			throw new UpdateCheckedException();
		}
		
		return headResultCnt + itemResultCnt;
	}
	
	// 운송정산 마감 + 검증까지 완료 로직	(화면구현 X)
	public int closeTplnhdAndVerifiedBtrphd(GridDTO<BtrpPrcDTO> btrpParam) {
		UserVO userInfo = SecurityUtils.getCustomUserDetails().getUserInfo();
		int btrphdInsertCnt = 0;
		int btrpitInsertCnt = 0;
		int btrphdUpdateCnt = 0;
		int tplnhdUpdateCnt = 0;
		
		Map<String, BtrphdDTO> vhplnkyMap = new HashMap<>();
	    
	    for(BtrpPrcDTO btrpData : btrpParam.getAddList()) {
	    	if(!vhplnkyMap.containsKey(btrpData.getVhplnky())){					//vhplnky가 Map안에 있는지 확인
	    		String bphdkey = billingMasterMapper.getBphdkey(userInfo);				//bphdkey 채번
	    		
	    		BtrphdDTO btrphd = new BtrphdDTO();
	    		btrphd.setVhplnky(btrpData.getVhplnky());
	    		btrphd.setWarekey(btrpData.getWarekey());
	    		btrphd.setBphdkey(bphdkey);
	    		btrphd.setCalweig(btrpData.getCalweig());	    	
	    		btrphd.setTrpdfsd(24000); 										//부적운임기준중량 (임시) 
	    		btrphd.setPcvrfyn(Useyn.UNUSE.getString());
	    		btrphd.setPccfmyn(Useyn.UNUSE.getString());
	    		vhplnkyMap.put(btrpData.getVhplnky(), btrphd);					//map에 put
	    	}
	    	btrpData.setBphdkey(vhplnkyMap.get(btrpData.getVhplnky()).getBphdkey());
	    	btrpData.setDoccate(Btrpitm.DOCCATE.getCode());	//900
	    	btrpData.setDoctype(Btrpitm.DOCCATE.getString());	//920
	    	btrpitInsertCnt = billingMasterMapper.insertBtrpitList(btrpData, userInfo);
	    	
	    	if(btrpitInsertCnt == 0 ) {
	    		throw new InsertCheckedException();
	    	}
	    }

	    for (BtrphdDTO btrphd : vhplnkyMap.values()) {
	        btrphdInsertCnt = billingMasterMapper.insertBtrphdFromBtrpit(btrphd, userInfo);	//헤더 인서트
	        if (btrphdInsertCnt == 0) {
	            throw new InsertCheckedException(); 
	        }
	        btrphd.setPcvrfyn(Useyn.USE.getString());			//BTRPHD 검증여부 컬럼 'Y'
	        btrphdUpdateCnt = billingMasterMapper.updateBtrphdYnList(btrphd, userInfo);
	        
	        btrphd.setTclosyn(Useyn.USE.getString());			//TPLNHD 마감여부 컬럼 'Y'
	        tplnhdUpdateCnt = billingMasterMapper.updateTplnhdCloseyn(btrphd, userInfo);	//마감여부 'Y' UPDATE
	    	if(tplnhdUpdateCnt == 0 || btrphdUpdateCnt == 0) {
	    		throw new UpdateCheckedException();
	    	}
	    }
		return btrphdInsertCnt + btrpitInsertCnt + tplnhdUpdateCnt;
	}
	
	//선지급 취소
	public int updatePayadynCancel(List<BtrphdDTO> btrpParam) {
		UserVO userInfo = SecurityUtils.getCustomUserDetails().getUserInfo();
		int tplnhdResult = 0;
		int btrphdResult = 0;
		int bppahdResult = 0;
		int bppaitResult = 0;
		for(BtrphdDTO btrphd : btrpParam) {
			btrphd.setPayadyn(Useyn.UNUSE.getString());
			tplnhdResult = billingMasterMapper.updateTplnhdPayadyn(btrphd,userInfo);	//운송계획헤더 update
			btrphdResult = billingMasterMapper.updateBtrphdPayadyn(btrphd,userInfo);	//운송정산헤더 update
			if(tplnhdResult == 0 || btrphdResult == 0) {
				throw new UpdateCheckedException();
			}
			bppahdResult = billingMasterMapper.deleteBppahd(btrphd, userInfo);	//선지급 이력 헤더 삭제
			bppaitResult = billingMasterMapper.deleteBppait(btrphd, userInfo);	//선지급 이력 아이쳄 삭제
			if(bppahdResult == 0 || bppaitResult == 0) {
				throw new DeleteCheckedException();
			}
			
		}
		return tplnhdResult  + btrphdResult + bppahdResult + bppaitResult;
	}
	
	//이송정산 청구처 수정
	public int updateDoe086BtrpcbtList(GridDTO<BtrpitDTO> btrpit) {
		UserVO userInfo = SecurityUtils.getCustomUserDetails().getUserInfo();
		int result = 0;
		for(BtrpitDTO btrpitData : btrpit.getUpdateList()) {
			if(btrpitData.getBtrpcbt() == null || btrpitData.getBtrpcbt().trim().isEmpty()){
				throw new RequiredNotValueException();		//필수값 Exception
			}
			result += billingMasterMapper.updateDoe086BtrpcbtList(btrpitData,userInfo);
		}
		if(result != btrpit.getUpdateList().size()) {
			throw new UpdateCheckedException();
		}
		return result;
	}
	
	//운송정산 검증요약
	public List<BtrphdDTO> getDoe087VerifyList(BtrphdDTO btrpParam) {
		UserVO userInfo = SecurityUtils.getCustomUserDetails().getUserInfo();
		return billingMasterMapper.selectDoe087VerifySummary(btrpParam, userInfo);
	}
	
	//운송정산 확정완료
	public int updateBtrphdConfirm(List<BtrphdDTO> btrphdList) {
		UserVO userInfo = SecurityUtils.getCustomUserDetails().getUserInfo();
		int result = 0;
		int tplnhdResult = 0;
		int bppahdResult = 0;
		int bppaitResult = 0;
		
		//확정 완료 -> 선지급 여부가 'Y'일 경우 이력 테이블 insert
		for(BtrphdDTO btrphd : btrphdList) {
			btrphd.setPccfmyn(Useyn.USE.getString());	//확정완료 'Y'
			result += billingMasterMapper.updateBtrphdConfirm(btrphd, userInfo);		//운송정산헤더 update
			tplnhdResult += billingMasterMapper.updateTplnhdPayadyn(btrphd,userInfo);	//운송계획헤더 update
			
			//기존 선지급 여부의 값이 N이고 , payadmd 의 값이 Y일 경우 선지급 이력 insert
			if("N".equals(btrphd.getPayadog()) && "Y".equals(btrphd.getPayadmd())) {
				bppahdResult = billingMasterMapper.insertBppahdList(btrphd,userInfo);
				bppaitResult = billingMasterMapper.insertBppaitList(btrphd,userInfo);
				if(bppahdResult == 0 || bppaitResult == 0) {
					throw new InsertCheckedException();
				}
			}
		}
		
		if(result != btrphdList.size() || tplnhdResult != btrphdList.size()) {
			throw new UpdateCheckedException();
		}
		
		return result;		
	}
	
	//운송정산 검증 취소
	public int updateBtrphdCancel(List<BtrphdDTO> btrphdList) {
		int result = 0;
		for(BtrphdDTO btrphd : btrphdList) {
			btrphd.setPcvrfyn(Useyn.UNUSE.getString());	//검증여부 'N'
			result += billingMasterMapper.updateBtrphdYnList(btrphd, SecurityUtils.getCustomUserDetails().getUserInfo());
		}
		if(result != btrphdList.size()) {
			throw new UpdateCheckedException();
		}
		
		return result;		
	}
	//이송집계 내역 리스트
	public List<BtrfitDTO> getDoe088BeforeGridList(BtrfitDTO btrfit){
		btrfit.setFccfmyn(Useyn.USE.getString());		
		return billingMasterMapper.selectBtrfitList(btrfit, SecurityUtils.getCustomUserDetails().getUserInfo());
	}
	//운송집계내역 리스트
	public List<BtrpitDTO> getDoe088AfterGridList(BtrpitDTO btrpit){
		btrpit.setPccfmyn(Useyn.USE.getString());		
		return billingMasterMapper.selectBtrpitList(btrpit, SecurityUtils.getCustomUserDetails().getUserInfo());
	}
	
	/* 실적집계 리스트 :일일누적정산(DAY), 월별 누적정산집계(MONTH) */ 
	public List<BtrfPrcDTO> getDoe088BillSummaryList(BtrfPrcDTO btrfParam){
		UserVO userInfo = SecurityUtils.getCustomUserDetails().getUserInfo();
		//이송요율 select시 사용하기 위한 dto value setting
		btrfParam.setTransfer(Btrfitm.TRANSFER.getString());
		btrfParam.setLoading(Btrfitm.LOADING.getString());
		btrfParam.setSelection(Btrfitm.SELECTION.getString());
		btrfParam.setStorage(Btrfitm.STORAGE.getString());
		btrfParam.setStnweig(Btrfitm.GROSS.getString());
		btrfParam.setGrossWgName(Btrfitm.GROSSWG.getString());
		btrfParam.setSkuWeigName(Btrfitm.SKUWEIG.getString());
		return billingMasterMapper.selectDoe088Md8SummaryList(btrfParam, userInfo);
	}
}
