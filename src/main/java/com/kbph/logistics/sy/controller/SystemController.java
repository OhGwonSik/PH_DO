package com.kbph.logistics.sy.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kbph.logistics.common.domain.GridDTO;
import com.kbph.logistics.common.util.RequestUtil;
import com.kbph.logistics.configuration.security.CustomUserDetails;
import com.kbph.logistics.sy.domain.DashBoardDTO;
import com.kbph.logistics.sy.domain.HeaderAndMenuDTO;
import com.kbph.logistics.sy.domain.RoleMenuWhDTO;
import com.kbph.logistics.sy.domain.SmnghdDTO;
import com.kbph.logistics.sy.domain.SmnubmDTO;
import com.kbph.logistics.sy.domain.SmnumaDTO;
import com.kbph.logistics.sy.domain.SrolgrDTO;
import com.kbph.logistics.sy.domain.SrolmnDTO;
import com.kbph.logistics.sy.domain.SrolwhDTO;
import com.kbph.logistics.sy.domain.SusrvhDTO;
import com.kbph.logistics.sy.domain.TotalDashBoardDTO;
import com.kbph.logistics.sy.domain.UserDTO;
import com.kbph.logistics.sy.domain.UserVO;
import com.kbph.logistics.sy.service.SystemService;
import com.kbph.logistics.sy.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class SystemController {
	private final SystemService systemService;
	private final UserService userService;

	public List<SmnghdDTO> getSmnghdList(SmnghdDTO smnghd) {
		return systemService.getSmnghdList(smnghd);
	}

	@GetMapping("/sy/menu/grid/header")
	public List<SmnghdDTO> selectSmnghdList(@ModelAttribute SmnghdDTO smnghd){
		return systemService.getSmnghdList(smnghd);
	}

	//doe16 메뉴등록
	@GetMapping("/sy/doe016/grid")
	public List<SmnumaDTO> getMenuList(@ModelAttribute SmnumaDTO smnumaDTO){
		return systemService.getMenuList(smnumaDTO);
	}

	@PostMapping("/sy/menu/save")
	public int saveSmnuma(@RequestBody GridDTO<SmnumaDTO> requestDTO) {
		return systemService.saveSmnuma(requestDTO);
	}

	// syu03 권한 등록
	@GetMapping("/sy/syu03/grid/role")
	public List<SrolgrDTO> getRoleList (@ModelAttribute SrolgrDTO srolgrDTO){
		return systemService.getRoleList(srolgrDTO);
	}

	@GetMapping("/sy/syu03/grid/menu-role")
	public List<SmnumaDTO> getMenuByRoleList(@ModelAttribute SmnumaDTO smnumaDTO){
		return systemService.getMenuByRoleList(smnumaDTO);
	}

	@GetMapping("/sy/syu03/grid/war-role")
	public List<SrolwhDTO> getWarehouseByRoleList(@ModelAttribute SrolwhDTO srolwhDTO){
		return systemService.getWarehouseByRoleList(srolwhDTO);
	}

	@PostMapping("/sy/role/save")
	public int saveSrolgr(@RequestBody GridDTO<SrolgrDTO> requestDTO) {
		return systemService.saveSrolgr(requestDTO);
	}

	@PostMapping("/sy/role-menu/save")
	public int saveMenuByRole(@RequestBody RoleMenuWhDTO<SrolmnDTO> roleMenuWhDTO) {
		return systemService.saveMenuByRole(roleMenuWhDTO);
	}

	@PostMapping("/sy/role-wh/save")
	public int saveSrolwh(@RequestBody RoleMenuWhDTO<SrolwhDTO> roleMenuWhDTO) {
		return systemService.saveSrolwh(roleMenuWhDTO);
	}

	@GetMapping("/session/role-menu")
	private HeaderAndMenuDTO getSessionRoleMenu(HttpServletRequest request , @AuthenticationPrincipal CustomUserDetails customUserDetails) {
		HttpSession session = request.getSession((request.getSession(false) == null)); // true = create session

		UserVO userVO = (UserVO) customUserDetails.getUserInfo();

		HeaderAndMenuDTO headerAndMenuDTO = new HeaderAndMenuDTO();

		// 유저의 Header/Menu/bookmark 셋팅
		headerAndMenuDTO.setMainHeaderList(userService.getMainHeaderList(userVO));
		headerAndMenuDTO.setUserMenuList(userService.getUserMenu(userVO));

		session.setAttribute("mainHeader", headerAndMenuDTO.getMainHeaderList());
		session.setAttribute("userMenu", headerAndMenuDTO.getUserMenuList());

		return headerAndMenuDTO;
	}

	@PostMapping("/sy/bookmark/post")
	public int saveBookMark(@RequestBody SmnubmDTO smnubm) {
		return systemService.saveBookMark(smnubm);
	}

	@DeleteMapping("/sy/bookmark/delete")
	public int deleteBookMark(@RequestBody SmnubmDTO smnubm) {
		return systemService.deleteBookMark(smnubm);
	}

	@GetMapping("/sy/syu05/init")
	public List<SusrvhDTO> syu05VehicleInitList(SusrvhDTO susrvh){
		susrvh.setSubDomain(RequestUtil.extractSubDomain());
		return systemService.syu05VehicleInitList(susrvh);
	}

	@GetMapping("/sy/syu05/susrma")
	public List<UserDTO> getAppUserList(UserDTO user) {
		return systemService.getAppUserList(user);
	}

	@GetMapping("/sy/syu05/susrvh")
	public List<SusrvhDTO> getVehicleByUser(SusrvhDTO susrvh) {
		susrvh.setSubDomain(RequestUtil.extractSubDomain());
		return systemService.getVehicleByUser(susrvh);
	}

	@PostMapping("/sy/syu05/save")
	public int syu05SaveDriver(@RequestBody GridDTO<SusrvhDTO> requestDTO) {
		return systemService.syu05SaveDriver(requestDTO);
	}

	@GetMapping("/sy/main/dashboard")
	public TotalDashBoardDTO getDashBoardInfo(DashBoardDTO dashboard) {
		return systemService.getDashBoardInfo(dashboard);
	}
}