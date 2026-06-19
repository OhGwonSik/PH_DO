package com.kbph.logistics.configuration.filter;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.kbph.logistics.common.constant.Constants;
import com.kbph.logistics.configuration.security.CustomUserDetailService;
import com.kbph.logistics.configuration.security.CustomUserDetails;
import com.kbph.logistics.configuration.security.JwtProvider;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * @author : t.s.park
 * @version : 1.0.0
 * @since : 2024-08-05
 * @note : AuthorizationFilter
 * ==================================================
 * DATE							AUTHOR            					NOTE
 * --------------------------------------------------------------------------------------
 * 2024-08-05                 t.s.park                     create AuthorizationFilter
 * --------------------------------------------------------------------------------------
 * ==================================================
 */
@RequiredArgsConstructor
public class AuthorizationFilter extends OncePerRequestFilter {
	private final CustomUserDetailService customUserDetailService;
	private final JwtProvider jwtProvider;
	private final List<String> excludePathList;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		try {
			String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);
			if(StringUtils.isEmpty(accessToken)) {
				for (String excludePath : excludePathList) {
					if (request.getRequestURI().trim().contains(excludePath)) {
						if(request.getSession().getAttribute(Constants.Auth.ACCESS_TOKEN_NAME) != null) {
							accessToken = request.getSession().getAttribute(Constants.Auth.ACCESS_TOKEN_NAME).toString().trim();
							request.getSession().setAttribute(Constants.Auth.ACCESS_TOKEN_NAME, accessToken);
							break;
						}
					}
				}
			}

			if (!StringUtils.isEmpty(accessToken) && jwtProvider.validateAccessToken(accessToken)) {
				CustomUserDetails custonUserDetails = customUserDetailService.loadUserByUsername(jwtProvider.getAccessTokenUseract(accessToken));

				if (custonUserDetails == null) {
					throw new UsernameNotFoundException("User not found");
				}
				if (!custonUserDetails.isAccountNonExpired()) {
					throw new DisabledException("계정 만료");
				}
				if (!custonUserDetails.isAccountNonLocked()) {
					throw new LockedException("계정 잠금");
				}
				if (!custonUserDetails.isEnabled()) {
					throw new LockedException("계정 비활성화");
				}

				UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(custonUserDetails, custonUserDetails.getPassword(), custonUserDetails.getAuthorities());
				authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (e instanceof io.jsonwebtoken.security.SecurityException || e instanceof MalformedJwtException) {
				throw new MalformedJwtException("잘못된 JWT 서명입니다.");
			} else if (e instanceof ExpiredJwtException) {
				throw new ExpiredJwtException(null, null, "만료된 JWT 토큰입니다.");
			} else if (e instanceof UnsupportedJwtException) {
				throw new UnsupportedJwtException("지원되지 않는 JWT 토큰입니다.");
			} else if (e instanceof IllegalArgumentException) {
				throw new IllegalArgumentException("JWT 토큰이 잘못되었습니다.");
			} else if (e instanceof UsernameNotFoundException) {
				throw new UsernameNotFoundException("User not found");
			} else if (e instanceof DisabledException) {
				throw new DisabledException("계정 만료");
			} else if (e instanceof LockedException) {
				throw new LockedException("계정 잠금/비활성화"); // to-do 메세지로 변경
			} else {
				throw new RuntimeException("기타 에러");
			}
		}

		filterChain.doFilter(request, response);
	}
}