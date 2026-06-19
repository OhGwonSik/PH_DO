package com.kbph.logistics.rm.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.kbph.logistics.common.domain.GridDTO;
import com.kbph.logistics.common.util.SecurityUtils;
import com.kbph.logistics.configuration.error.InsertCheckedException;
import com.kbph.logistics.configuration.error.NoSaveDataException;
import com.kbph.logistics.configuration.error.UpdateCheckedException;
import com.kbph.logistics.rm.domain.WpaordDTO;
import com.kbph.logistics.rm.domain.WpashdDTO;
import com.kbph.logistics.rm.domain.WpasitDTO;
import com.kbph.logistics.rm.domain.WpasmaDTO;
import com.kbph.logistics.rm.mapper.StoreStrategyMapper;
import com.kbph.logistics.sy.domain.UserVO;

import lombok.RequiredArgsConstructor;
/**
 * @author : t.s.park
 * @version : 1.0.0
 * @since : 2024-07-09
 * @note : Service Class for store strategy
 * ==================================================
 * DATE							AUTHOR            					NOTE
 * --------------------------------------------------------------------------------------
 * 2024-07-09					t.s.park        					create Service class
 * --------------------------------------------------------------------------------------
 * ==================================================
 */
@Service
@RequiredArgsConstructor
public class StoreStrategyService {
	private final StoreStrategyMapper storeStrategyMapper;

	// 적치전략마스터(WPASMA) 조회
	public List<WpasmaDTO> getStoreStrategyMasterList(WpasmaDTO wpasmaDTO){
		return storeStrategyMapper.selectWpasmaList(wpasmaDTO, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	// 적치전략마스터(WPASMA) 저장
	public int saveStorestrategyMasterList(GridDTO<WpasmaDTO> saveList) {
		if(saveList == null) {
			throw new NoSaveDataException();
		}

		UserVO userInfo = SecurityUtils.getCustomUserDetails().getUserInfo();
		List<WpasmaDTO> addList = saveList.getAddList();
		List<WpasmaDTO> updateList = saveList.getUpdateList();
		int insertResult = 0;
		int updateResult = 0;

		if(!CollectionUtils.isEmpty(addList)) {
			insertResult = storeStrategyMapper.insertWpasmaList(saveList.getAddList(), userInfo);

			if(insertResult == 0) {
				throw new InsertCheckedException();
			}
		}

		if(!CollectionUtils.isEmpty(updateList)) {
			updateResult = storeStrategyMapper.updateWpasmaList(saveList.getUpdateList(), userInfo);

			if(updateResult == 0) {
				throw new UpdateCheckedException();
			}
		}

		return insertResult + updateResult;
	}

	// 적치전략헤더(WPASHD) 조회
	public List<WpashdDTO> getStoreStrategyHeaderList(WpashdDTO wpashdDTO){
		return storeStrategyMapper.selectWpashdList(wpashdDTO, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	// 적치전략헤더(WPASHD) 저장
	public int saveStoreStrategyHeaderList(GridDTO<WpashdDTO> saveList) {
		if(saveList == null) {
			throw new NoSaveDataException();
		}

		UserVO userInfo = SecurityUtils.getCustomUserDetails().getUserInfo();
		List<WpashdDTO> addList = saveList.getAddList();
		List<WpashdDTO> updateList = saveList.getUpdateList();
		int insertResult = 0;
		int updateResult = 0;

		if(!CollectionUtils.isEmpty(addList)) {
			insertResult = storeStrategyMapper.insertWpashdList(saveList.getAddList(), userInfo);

			if(insertResult == 0) {
				throw new InsertCheckedException();
			}
		}

		if(!CollectionUtils.isEmpty(updateList)) {
			updateResult = storeStrategyMapper.updateWpashdList(saveList.getUpdateList(), userInfo);

			if(updateResult == 0) {
				throw new UpdateCheckedException();
			}
		}

		return insertResult + updateResult;
	}

	// 적치전략아이템(WPASIT) 조회
	public List<WpasitDTO> getStoreStrategyItemList(WpasitDTO wpasitDTO){
		return storeStrategyMapper.selectWpasitList(wpasitDTO, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	// 적치전략아이템(WPASIT) 저장
	public int saveStoreStrategyItemList(GridDTO<WpasitDTO> saveList) {
		if(saveList == null) {
			throw new NoSaveDataException();
		}

		UserVO userInfo = SecurityUtils.getCustomUserDetails().getUserInfo();
		List<WpasitDTO> addList = saveList.getAddList();
		List<WpasitDTO> updateList = saveList.getUpdateList();
		int insertResult = 0;
		int updateResult = 0;

		if(!CollectionUtils.isEmpty(addList)) {
			insertResult = storeStrategyMapper.insertWpasitList(saveList.getAddList(), userInfo);

			if(insertResult == 0) {
				throw new InsertCheckedException();
			}
		}

		if(!CollectionUtils.isEmpty(updateList)) {
			updateResult = storeStrategyMapper.updateWpasitList(saveList.getUpdateList(), userInfo);

			if(updateResult == 0) {
				throw new UpdateCheckedException();
			}
		}

		return insertResult + updateResult;
	}

	// 오더별 적치전략(WPAORD) 조회
	public List<WpaordDTO> getStoreStrategyForOrderList(WpaordDTO wpaordDTO){
		return storeStrategyMapper.selectWpaordList(wpaordDTO, SecurityUtils.getCustomUserDetails().getUserInfo());
	}

	// 오더별 적치전략(WPAORD) 저장
	public int saveStoreStrategyForOrderList(GridDTO<WpaordDTO> saveList) {
		if(saveList == null) {
			throw new NoSaveDataException();
		}

		UserVO userInfo = SecurityUtils.getCustomUserDetails().getUserInfo();
		List<WpaordDTO> addList = saveList.getAddList();
		List<WpaordDTO> updateList = saveList.getUpdateList();
		int insertResult = 0;
		int updateResult = 0;

		if(!CollectionUtils.isEmpty(addList)) {
			insertResult = storeStrategyMapper.insertWpaordList(saveList.getAddList(), userInfo);

			if(insertResult == 0) {
				throw new InsertCheckedException();
			}
		}

		if(!CollectionUtils.isEmpty(updateList)) {
			updateResult = storeStrategyMapper.updateWpaordList(saveList.getUpdateList(), userInfo);

			if(updateResult == 0) {
				throw new UpdateCheckedException();
			}
		}

		return insertResult + updateResult;
	}
}
