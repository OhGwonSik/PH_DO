package com.kbph.logistics.sy.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.kbph.logistics.common.constant.Constants;
import com.kbph.logistics.common.util.SecurityUtils;
import com.kbph.logistics.configuration.security.JwtProvider;
import com.kbph.logistics.sy.domain.SmnghdDTO;
import com.kbph.logistics.sy.domain.SmnumaDTO;
import com.kbph.logistics.sy.service.UserService;

import io.jsonwebtoken.lang.Collections;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MainController {
	private final UserService userService;
	private final JwtProvider jwtProvider;
	@Value("${server.servlet.page-path}")
	private String pagePath;

	// main page url
	@ModelAttribute("mainPage")
	public String getMainPage(final HttpServletRequest request) {
		return Constants.Uri.MAIN_URI;
	}

	// context path
	@ModelAttribute("contextPath")
	public String getContextPath(final HttpServletRequest request) {
		return request.getContextPath();
	}

	// tab page path
	@ModelAttribute("pagePath")
	public String getPagePath(final HttpServletRequest request) {
		return this.pagePath;
	}

	// user mainHeader(menu header)
	@ModelAttribute("mainHeader")
	public List<SmnghdDTO> getMainHeader(final Authentication auth) {
		return (auth != null && auth.isAuthenticated()) ? userService.getMainHeaderList(SecurityUtils.getCustomUserDetails().getUserInfo()) : Collections.emptyList();
	}

	// userMenu list
	@ModelAttribute("userMenu")
	public List<SmnumaDTO> getUserMenu(final Authentication auth) {
		return (auth != null && auth.isAuthenticated()) ? userService.getUserMenu(SecurityUtils.getCustomUserDetails().getUserInfo()) : Collections.emptyList();
	}

	// Modal 페이지 링크
	@GetMapping(Constants.Uri.MAIN_URI + "/modalLoad")
	public String modalLoadPage(Model model, @RequestParam Map<String, Object> params) {
		model.addAttribute("params", params);

		return (String) params.get("gprogurl");
	}

	// 메인 page
	@GetMapping(Constants.Uri.MAIN_URI)
	public String mainPage(Model model) {
		return Constants.Uri.MAIN_URI + "/htmlMain"; // /main/htmlMain
	}

	// 태블릿 메인 page
	@GetMapping(Constants.Uri.MAIN_URI + "/tablet")
	public String tabletMainPage(Model model) {
		return Constants.Uri.MAIN_URI + "/tabletMain"; // /main/tabletMain
	}

	// 로그인
	@GetMapping(Constants.Uri.LOGIN_URI)
	public String loginPage(final HttpServletRequest request,
									@RequestParam(value = "error", required = false) Boolean error,
									@RequestParam(value = "exception", required = false) String exception,
									Model model) {
		String accessToken = null;
		if(request.getSession(false) != null && request.getSession(false).getAttribute("accessToken") != null) { // when already authorization
			accessToken = request.getSession().getAttribute("accessToken").toString().trim();
		}

		if(accessToken != null && jwtProvider.validateAccessToken(accessToken)) {
			String checkDevice = null;
			if (request.getHeader("User-Agent") != null) {
				checkDevice = request.getHeader("User-Agent").toUpperCase();
			}

			return "redirect:" + Constants.Uri.MAIN_URI + ((checkDevice != null && checkDevice.indexOf("MOBILE") != -1) ? (Constants.Uri.MOBILE_SUFFIX_URI) : (""));
		}

		model.addAttribute("error", error); // boolean
		model.addAttribute("exception", exception); // exception message

		return Constants.Uri.LOGIN_URI; // /login
	}

	// 회원가입
	@GetMapping(Constants.Uri.SIGN_UP_URI)
	public String signupPage(Model model) {
		return Constants.Uri.SIGN_UP_URI;
	}
	
	//다운로드
	@GetMapping(Constants.Uri.DOWNLOAD_URI)
	public String downloadPage(Model model) {
		return Constants.Uri.DOWNLOAD_URI;
	}

	// 이용약관
	@GetMapping(Constants.Uri.SIGN_UP_URI + "/text")
	public String termsOfUse(Model model) {
		return Constants.Uri.SIGN_UP_URI + "_text";
	}

	// 탭 페이지 이동
	@GetMapping("${server.servlet.page-path}/**")
	public String tabPage(final HttpServletRequest request, Model model) {
		String contextPath = request.getContextPath();
		String tabPage = request.getRequestURI().substring(request.getRequestURI().indexOf(contextPath) + contextPath.length() + this.pagePath.length());
		String progrid = tabPage.substring(tabPage.lastIndexOf("/") + 1);

		if (!userService.isValidProgrid(progrid)) {
			model.addAttribute("alertMsg", "ms.invalidAccess");

			return Constants.Uri.ALERT_FAILURE_URI; // /alert
		}
		model.addAttribute("tabPage", progrid);

		return tabPage;
	}

	// 태블릿 페이지이동
	@GetMapping("${server.servlet.page-path}/tablet/**")
	public String tabletMenuPage(final HttpServletRequest request, Model model) {
		String tabletPagePath = request.getRequestURI().substring(request.getRequestURI().indexOf(this.pagePath) + (this.pagePath.length() + Constants.Uri.MOBILE_SUFFIX_URI.length()));
		String progrid = tabletPagePath.substring(tabletPagePath.lastIndexOf("/") + 1);
		String pageName = request.getParameter("pagename");
		model.addAttribute("tabPage", progrid); // 이름 통일
		model.addAttribute("tabletPagePath", tabletPagePath);
		model.addAttribute("pagename", pageName);

		return Constants.Uri.MAIN_URI + "/tabletMain";
	}
}
