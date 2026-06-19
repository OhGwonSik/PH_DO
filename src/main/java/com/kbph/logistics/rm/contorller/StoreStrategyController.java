package com.kbph.logistics.rm.contorller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kbph.logistics.common.domain.GridDTO;
import com.kbph.logistics.rm.domain.WpaordDTO;
import com.kbph.logistics.rm.domain.WpashdDTO;
import com.kbph.logistics.rm.domain.WpasitDTO;
import com.kbph.logistics.rm.domain.WpasmaDTO;
import com.kbph.logistics.rm.service.StoreStrategyService;

import lombok.RequiredArgsConstructor;
/**
 * @author : t.s.park
 * @version : 1.0.0
 * @since : 2024-07-09
 * @note : Controller Class for store strategy
 * ==================================================
 * DATE							AUTHOR            					NOTE
 * --------------------------------------------------------------------------------------
 * 2024-07-09					t.s.park        					create Controller class
 * --------------------------------------------------------------------------------------
 * ==================================================
 */
@RestController
@RequiredArgsConstructor
public class StoreStrategyController {
	private final StoreStrategyService storeStrategyService;

	@GetMapping(path = {"/rm/storeStrategy/doe017/grids/1", "/rm/storeStrategy/doe018/grids/1"}) // 같은 기능이라 2개 매핑
	public List<WpasmaDTO> getStoreStrategyMasterList(@ModelAttribute WpasmaDTO wpasmaDTO) {
		return storeStrategyService.getStoreStrategyMasterList(wpasmaDTO);
	}

	@GetMapping(path = {"/rm/storeStrategy/doe017/grids/2", "/rm/storeStrategy/doe018/grids/2"}) // 같은 기능이라 2개 매핑
	public List<WpashdDTO> getStoreStrategyHeaderList(@ModelAttribute WpashdDTO wpashdDTO) {
		return storeStrategyService.getStoreStrategyHeaderList(wpashdDTO);
	}

	@GetMapping(path = {"/rm/storeStrategy/doe017/grids/3", "/rm/storeStrategy/doe018/grids/3"}) // 같은 기능이라 2개 매핑
	public List<WpasitDTO> getStoreStrategyItemList(@ModelAttribute WpasitDTO wpasitDTO) {
		return storeStrategyService.getStoreStrategyItemList(wpasitDTO);
	}

	@GetMapping(path = {"/rm/storeStrategy/doe017/grids/4", "/rm/storeStrategy/doe018/grids/4"})
	public List<WpaordDTO> getStoreStrategyForOrderList(@ModelAttribute WpaordDTO wpaordDTO) {
		return storeStrategyService.getStoreStrategyForOrderList(wpaordDTO);
	}

	@PostMapping("/rm/storeStrategy/doe018/grids/1")
	public int saveStorestrategyMasterList(@RequestBody GridDTO<WpasmaDTO> saveList) {
		return storeStrategyService.saveStorestrategyMasterList(saveList);
	}

	@PostMapping("/rm/storeStrategy/doe018/grids/2")
	public int saveStorestrategyHeaderList(@RequestBody GridDTO<WpashdDTO> saveList) {
		return storeStrategyService.saveStoreStrategyHeaderList(saveList);
	}

	@PostMapping("/rm/storeStrategy/doe018/grids/3")
	public int saveStorestrategyItemList(@RequestBody GridDTO<WpasitDTO> saveList) {
		return storeStrategyService.saveStoreStrategyItemList(saveList);
	}

	@PostMapping("/rm/storeStrategy/doe018/grids/4")
	public int saveStorestrategyForOrderList(@RequestBody GridDTO<WpaordDTO> saveList) {
		return storeStrategyService.saveStoreStrategyForOrderList(saveList);
	}
}
