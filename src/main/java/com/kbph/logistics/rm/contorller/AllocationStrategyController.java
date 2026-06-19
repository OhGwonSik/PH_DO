package com.kbph.logistics.rm.contorller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kbph.logistics.common.domain.GridDTO;
import com.kbph.logistics.rm.domain.WalordDTO;
import com.kbph.logistics.rm.domain.WalshdDTO;
import com.kbph.logistics.rm.domain.WalsitDTO;
import com.kbph.logistics.rm.domain.WalsmaDTO;
import com.kbph.logistics.rm.service.AllocationStrategyService;

import lombok.RequiredArgsConstructor;
/**
 * @author : t.s.park
 * @version : 1.0.0
 * @since : 2024-07-09
 * @note : Controller Class for allocation strategy
 * ==================================================
 * DATE							AUTHOR            					NOTE
 * --------------------------------------------------------------------------------------
 * 2024-07-09					t.s.park        					create Controller class
 * 2024-07-15					t.s.park        					create Controller method
 * --------------------------------------------------------------------------------------
 * ==================================================
 */
@RestController
@RequiredArgsConstructor
public class AllocationStrategyController {
	private final AllocationStrategyService allocationStrategyService;

	@GetMapping(path = {"/rm/allocationStrategy/doe019/grids/1", "/rm/allocationStrategy/doe020/grids/1"}) // 같은 기능이라 2개 매핑
	public List<WalsmaDTO> getAllocationStrategyMasterList(@ModelAttribute WalsmaDTO walsmaDTO) {
		return allocationStrategyService.getAllocationStrategyMasterList(walsmaDTO);
	}

	@GetMapping(path = {"/rm/allocationStrategy/doe019/grids/2", "/rm/allocationStrategy/doe020/grids/2"}) // 같은 기능이라 2개 매핑
	public List<WalshdDTO> getAllocationStrategyHeaderList(@ModelAttribute WalshdDTO walshdDTO) {
		return allocationStrategyService.getAllocationStrategyHeaderList(walshdDTO);
	}

	@GetMapping(path = {"/rm/allocationStrategy/doe019/grids/3", "/rm/allocationStrategy/doe020/grids/3"}) // 같은 기능이라 2개 매핑
	public List<WalsitDTO> getAllocationStrategyItemList(@ModelAttribute WalsitDTO walsitDTO) {
		return allocationStrategyService.getAllocationStrategyItemList(walsitDTO);
	}

	@GetMapping(path = {"/rm/allocationStrategy/doe019/grids/4", "/rm/allocationStrategy/doe020/grids/4"})
	public List<WalordDTO> getAllocationStrategyForOrderList(@ModelAttribute WalordDTO walordDTO) {
		return allocationStrategyService.getAllocationStrategyForOrderList(walordDTO);
	}

	@PostMapping("/rm/allocationStrategy/doe020/grids/1")
	public int saveAllocationstrategyMasterList(@RequestBody GridDTO<WalsmaDTO> saveList) {
		return allocationStrategyService.saveAllocationstrategyMasterList(saveList);
	}

	@PostMapping("/rm/allocationStrategy/doe020/grids/2")
	public int saveAllocationstrategyHeaderList(@RequestBody GridDTO<WalshdDTO> saveList) {
		return allocationStrategyService.saveAllocationStrategyHeaderList(saveList);
	}

	@PostMapping("/rm/allocationStrategy/doe020/grids/3")
	public int saveAllocationstrategyItemList(@RequestBody GridDTO<WalsitDTO> saveList) {
		return allocationStrategyService.saveAllocationStrategyItemList(saveList);
	}

	@PostMapping("/rm/allocationStrategy/doe020/grids/4")
	public int saveAllocationstrategyForOrderList(@RequestBody GridDTO<WalordDTO> saveList) {
		return allocationStrategyService.saveAllocationStrategyForOrderList(saveList);
	}
}
