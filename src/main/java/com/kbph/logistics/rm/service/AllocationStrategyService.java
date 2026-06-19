package com.kbph.logistics.rm.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.kbph.logistics.common.domain.GridDTO;
import com.kbph.logistics.common.util.SecurityUtils;
import com.kbph.logistics.configuration.error.InsertCheckedException;
import com.kbph.logistics.configuration.error.NoSaveDataException;
import com.kbph.logistics.configuration.error.UpdateCheckedException;
import com.kbph.logistics.rm.domain.WalordDTO;
import com.kbph.logistics.rm.domain.WalshdDTO;
import com.kbph.logistics.rm.domain.WalsitDTO;
import com.kbph.logistics.rm.domain.WalsmaDTO;
import com.kbph.logistics.rm.mapper.AllocationStrategyMapper;
import com.kbph.logistics.sy.domain.UserVO;

import lombok.RequiredArgsConstructor;
/**
 * @author : OP
 * @version : 1.0.0
 * @since : 2024-07-09
 * @note : Service Class for allocation strategy
 * ==================================================
 * DATE							AUTHOR            					NOTE
 * --------------------------------------------------------------------------------------
 * 2024-07-09				     	OP        				     	create Service class
 * 2024-07-15					t.s.park        					create Service method
 * --------------------------------------------------------------------------------------
 * ==================================================
 */
@Service
@RequiredArgsConstructor
public class AllocationStrategyService {
	private final AllocationStrategyMapper allocationStrategyMapper;

	// 할당전략마스터(WPASMA) 조회
	public List<WalsmaDTO> getAllocationStrategyMasterList(WalsmaDTO walsmaDTO){
		return allocationStrategyMapper.selectWalsmaList(walsmaDTO, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	// 할당전략마스터(WPASMA) 저장
	public int saveAllocationstrategyMasterList(GridDTO<WalsmaDTO> saveList) {
		if(saveList == null) {
			throw new NoSaveDataException();
		}

		UserVO userInfo = SecurityUtils.getCustomUserDetails().getUserInfo();
		List<WalsmaDTO> addList = saveList.getAddList();
		List<WalsmaDTO> updateList = saveList.getUpdateList();
		int insertResult = 0;
		int updateResult = 0;

		if(!CollectionUtils.isEmpty(addList)) {
			insertResult = allocationStrategyMapper.insertWalsmaList(saveList.getAddList(), userInfo);

			if(insertResult == 0) {
				throw new InsertCheckedException();
			}
		}

		if(!CollectionUtils.isEmpty(updateList)) {
			updateResult = allocationStrategyMapper.updateWalsmaList(saveList.getUpdateList(), userInfo);

			if(updateResult == 0) {
				throw new UpdateCheckedException();
			}
		}

		return insertResult + updateResult;
	}

	// 할당전략헤더(WPASHD) 조회
	public List<WalshdDTO> getAllocationStrategyHeaderList(WalshdDTO walshdDTO){
		return allocationStrategyMapper.selectWalshdList(walshdDTO, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	// 할당전략헤더(WALSHD) 저장
	public int saveAllocationStrategyHeaderList(GridDTO<WalshdDTO> saveList) {
		if(saveList == null) {
			throw new NoSaveDataException();
		}

		UserVO userInfo = SecurityUtils.getCustomUserDetails().getUserInfo();
		List<WalshdDTO> addList = saveList.getAddList();
		List<WalshdDTO> updateList = saveList.getUpdateList();
		int insertResult = 0;
		int updateResult = 0;

		if(!CollectionUtils.isEmpty(addList)) {
			insertResult = allocationStrategyMapper.insertWalshdList(saveList.getAddList(), userInfo);

			if(insertResult == 0) {
				throw new InsertCheckedException();
			}
		}

		if(!CollectionUtils.isEmpty(updateList)) {
			updateResult = allocationStrategyMapper.updateWalshdList(saveList.getUpdateList(), userInfo);

			if(updateResult == 0) {
				throw new UpdateCheckedException();
			}
		}

		return insertResult + updateResult;
	}

	// 할당전략아이템(WALSIT) 조회
	public List<WalsitDTO> getAllocationStrategyItemList(WalsitDTO walsitDTO){
		return allocationStrategyMapper.selectWalsitList(walsitDTO, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	// 할당전략아이템(WALSIT) 저장
	public int saveAllocationStrategyItemList(GridDTO<WalsitDTO> saveList) {
		if(saveList == null) {
			throw new NoSaveDataException();
		}

		UserVO userInfo = SecurityUtils.getCustomUserDetails().getUserInfo();
		List<WalsitDTO> addList = saveList.getAddList();
		List<WalsitDTO> updateList = saveList.getUpdateList();
		int insertResult = 0;
		int updateResult = 0;

		if(!CollectionUtils.isEmpty(addList)) {
			insertResult = allocationStrategyMapper.insertWalsitList(saveList.getAddList(), userInfo);

			if(insertResult == 0) {
				throw new InsertCheckedException();
			}
		}

		if(!CollectionUtils.isEmpty(updateList)) {
			updateResult = allocationStrategyMapper.updateWalsitList(saveList.getUpdateList(), userInfo);

			if(updateResult == 0) {
				throw new UpdateCheckedException();
			}
		}

		return insertResult + updateResult;
	}

	// 오더별 할당전략(WALORD) 조회
	public List<WalordDTO> getAllocationStrategyForOrderList(WalordDTO walordDTO){
		return allocationStrategyMapper.selectWalordList(walordDTO, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	// 오더별 할당전략(WALORD) 저장
	public int saveAllocationStrategyForOrderList(GridDTO<WalordDTO> saveList) {
		if(saveList == null) {
			throw new NoSaveDataException();
		}

		UserVO userInfo = SecurityUtils.getCustomUserDetails().getUserInfo();
		List<WalordDTO> addList = saveList.getAddList();
		List<WalordDTO> updateList = saveList.getUpdateList();
		int insertResult = 0;
		int updateResult = 0;

		if(!CollectionUtils.isEmpty(addList)) {
			insertResult = allocationStrategyMapper.insertWalordList(saveList.getAddList(), userInfo);

			if(insertResult == 0) {
				throw new InsertCheckedException();
			}
		}

		if(!CollectionUtils.isEmpty(updateList)) {
			updateResult = allocationStrategyMapper.updateWalordList(saveList.getUpdateList(), userInfo);

			if(updateResult == 0) {
				throw new UpdateCheckedException();
			}
		}

		return insertResult + updateResult;
	}
}
