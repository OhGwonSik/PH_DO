package com.kbph.logistics.configuration.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

import com.kbph.logistics.common.util.SecurityUtils;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class AuthenticationSetter {
	private final AuthenticationManager authenticationManager;
	private final CustomUserDetailService customUserDetailService;

	@Bean
	public boolean updateAuthentication() {
		try {
			CustomUserDetails details = SecurityUtils.getCustomUserDetails();
			CustomUserDetails newDetails = customUserDetailService.loadUserByUsername(details.getUsername());
			SecurityContextHolder.getContext().setAuthentication(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(newDetails, newDetails.getPassword())));
		} catch (AuthenticationException e) {
			e.getStackTrace();
		}

		return true;
	}

}
