package com.kbph.logistics.configuration.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.kbph.logistics.configuration.BeanConfig;

import lombok.RequiredArgsConstructor;

// 인증 완료 후 객체 제공
@Component
@RequiredArgsConstructor
public class AuthProvider implements AuthenticationProvider {
	private final CustomUserDetailService customUserDetailService;
	private final BeanConfig beanConfig;

	@Override
	public Authentication authenticate(Authentication authentication) {
		String useract = ((CustomUserDetails) authentication.getPrincipal()).getUsername();
		String passwrd = authentication.getCredentials().toString();
		CustomUserDetails userDetails = customUserDetailService.loadUserByUsername(useract);

		if (userDetails == null) {
            throw new UsernameNotFoundException("User not found"); // to-do 메세지로 변경
        }
		if(!beanConfig.passwordEncoder().matches(passwrd, userDetails.getPassword())) {
			throw new BadCredentialsException("Invalid password");
		}
		if(!userDetails.isAccountNonExpired()) {
            throw new DisabledException("계정 만료"); // to-do 메세지로 변경
		}
		if(!userDetails.isAccountNonLocked()) {
            throw new LockedException("계정 잠금"); // to-do 메세지로 변경
		}
		if(!userDetails.isEnabled()) {
            throw new LockedException("계정 비활성화 됨"); // to-do 메세지로 변경
		}

		return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
}
