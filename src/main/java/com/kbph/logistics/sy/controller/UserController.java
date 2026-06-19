package com.kbph.logistics.sy.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kbph.logistics.common.domain.GridDTO;
import com.kbph.logistics.common.domain.InitDataDTO;
import com.kbph.logistics.sy.domain.UserDTO;
import com.kbph.logistics.sy.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;
	private final MessageSource msg;

	/*
	 * 사용자 Grid Setting & Column Layout 조회
	 */
	@GetMapping("/sy/syu10/select")
	public Map<String, Object> syu10Select(@RequestParam Map<String, Object> param) {
		Map<String, Object> result = new HashMap<>();
		result.put("gridSettingLayout", userService.gridSettingLayout(param));
		result.put("gridColumnLayout", userService.gridColumnLayout(param));
		result.put("layoutSettingName", this.layoutSettingName());

		return result;
	}

	/*
	 * 사용자 Grid Setting & Column Layout 저장
	 */
	@PostMapping("/sy/syu10/save")
	public int syu10Save(@RequestBody Map<String, Object> param) {
		return userService.setSYU10Save(param);
	}

	/*
	 * 사용자 Grid Setting & Column Layout Reset
	 */
	@DeleteMapping("/sy/syu10/reset")
	public int syu10Reset(@RequestBody Map<String, Object> param) {
		return userService.setSYU10Reset(param);
	}

	/*
	 * layoutSettingName 다국어
	 */
	public List<HashMap<String, Object>> layoutSettingName() {
		ArrayList<HashMap<String, Object>> list = new ArrayList<>();
		HashMap<String, Object> map = new HashMap<>();

		map.put("layoutSave", msg.getMessage("sy.layout.layoutSave", null, LocaleContextHolder.getLocale()));
		map.put("layoutSetting", msg.getMessage("sy.layout.layoutSetting", null, LocaleContextHolder.getLocale()));
		map.put("layoutReset", msg.getMessage("sy.layout.layoutReset", null, LocaleContextHolder.getLocale()));

		map.put("export", msg.getMessage("sy.layout.export", null, LocaleContextHolder.getLocale()));
		map.put("exportCsv", msg.getMessage("sy.layout.exportCsv", null, LocaleContextHolder.getLocale()));
		map.put("exportHtml", msg.getMessage("sy.layout.exportHtml", null, LocaleContextHolder.getLocale()));
		map.put("exportJson", msg.getMessage("sy.layout.exportJson", null, LocaleContextHolder.getLocale()));
		map.put("exportXlsx", msg.getMessage("sy.layout.exportXlsx", null, LocaleContextHolder.getLocale()));

		map.put("numberCell", msg.getMessage("sy.layout.numberCell", null, LocaleContextHolder.getLocale()));

		map.put("selectionModel", msg.getMessage("sy.layout.selectionModel", null, LocaleContextHolder.getLocale()));
		map.put("selectionModelCell",
				msg.getMessage("sy.layout.selectionModelCell", null, LocaleContextHolder.getLocale()));
		map.put("selectionModelRow",
				msg.getMessage("sy.layout.selectionModelRow", null, LocaleContextHolder.getLocale()));

		map.put("freeze", msg.getMessage("sy.layout.freeze", null, LocaleContextHolder.getLocale()));
		map.put("colsCancel", msg.getMessage("sy.layout.colsCancel", null, LocaleContextHolder.getLocale()));
		map.put("rowsCancel", msg.getMessage("sy.layout.rowsCancel", null, LocaleContextHolder.getLocale()));
		map.put("freezeCol", msg.getMessage("sy.layout.freezeCol", null, LocaleContextHolder.getLocale()));
		map.put("freezeRow", msg.getMessage("sy.layout.freezeRow", null, LocaleContextHolder.getLocale()));

		map.put("columnBorders", msg.getMessage("sy.layout.columnBorders", null, LocaleContextHolder.getLocale()));
		map.put("rowBorders", msg.getMessage("sy.layout.rowBorders", null, LocaleContextHolder.getLocale()));
		map.put("stripeRows", msg.getMessage("sy.layout.stripeRows", null, LocaleContextHolder.getLocale()));

		map.put("addNodes", msg.getMessage("sy.layout.addNodes", null, LocaleContextHolder.getLocale()));
		map.put("undo", msg.getMessage("sy.layout.undo", null, LocaleContextHolder.getLocale()));
		map.put("redo", msg.getMessage("sy.layout.redo", null, LocaleContextHolder.getLocale()));
		map.put("groupModel", msg.getMessage("sy.layout.groupModel", null, LocaleContextHolder.getLocale()));
		map.put("copy", msg.getMessage("sy.layout.copy", null, LocaleContextHolder.getLocale()));
		map.put("paste", msg.getMessage("sy.layout.paste", null, LocaleContextHolder.getLocale()));

		map.put("allFields", msg.getMessage("pq.toolbar.allFields", null, LocaleContextHolder.getLocale()));
		map.put("enterYourKeyword",
				msg.getMessage("pq.toolbar.enterYourKeyword", null, LocaleContextHolder.getLocale()));
		map.put("find", msg.getMessage("bt.find", null, LocaleContextHolder.getLocale()));
		map.put("add", msg.getMessage("bt.add", null, LocaleContextHolder.getLocale()));
		map.put("reset", msg.getMessage("bt.reset", null, LocaleContextHolder.getLocale()));
		map.put("delete", msg.getMessage("bt.delete", null, LocaleContextHolder.getLocale()));
		map.put("refresh", msg.getMessage("bt.refresh", null, LocaleContextHolder.getLocale()));
		map.put("save", msg.getMessage("bt.save", null, LocaleContextHolder.getLocale()));

		list.add(map);

		return list;
	}

	// 아이디 중복 체크
	@GetMapping("/signup/check")
	public int getUseractCnt(@RequestParam("useract") String useract) {
		return userService.getUseractCnt(useract);
	}

	// 차주 회원가입
	@PostMapping("/signup")
	public int signUpUser(@RequestBody UserDTO userDTO) {
		return userService.signUpUser(userDTO);
	}

	// syu04 사용자 관리 (관리자용)
	// syu04 승인 모달 초기화
	@GetMapping("/sy/syu04/init")
	public InitDataDTO getSyu04InitData(@ModelAttribute UserDTO param) {
		return userService.getSyu04InitData(param);
	}

	// 사용자 관리 정보 조회
	@GetMapping("/sy/syu04/grid")
	public List<UserDTO> getUserList(@ModelAttribute UserDTO param) {
		return userService.getUserList(param);
	}

	// 사용자 관리 그리드 저장 (그리드 수정)
	@PostMapping("/sy/syu04/grid")
	public int saveUser(@RequestBody GridDTO<UserDTO> saveData) {
		return userService.saveUser(saveData);
	}

	// 가입승인요청 모달 조회
	@GetMapping("/sy/syu04/modal/1")
	public List<UserDTO> getUserApproveList(@ModelAttribute UserDTO param) {
		return userService.getUserApproveList(param);
	}

	// 앱사용자 가입 승인
	@PostMapping("/sy/syu04/modal/1")
	public int approveAppUser(@RequestBody GridDTO<UserDTO> saveList) {
		return userService.approveAppUser(saveList);
	}

	// 신규사용자 추가
	@PostMapping("/sy/syu04/modal/2")
	public int addNewUser(@RequestBody GridDTO<UserDTO> saveList) {
		return userService.addNewUser(saveList);
	}

	// 사용자 비밀번호 초기화
	@PostMapping("/sy/syu04/init/passwrd")
	public int initUserPasswrd(@RequestBody GridDTO<UserDTO> saveList) {
		return userService.initUserPasswrd(saveList);
	}
}
