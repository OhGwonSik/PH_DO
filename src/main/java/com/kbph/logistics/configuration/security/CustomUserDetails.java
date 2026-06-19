package com.kbph.logistics.configuration.security;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.kbph.logistics.common.constant.Constants;
import com.kbph.logistics.common.enums.Useyn;
import com.kbph.logistics.configuration.type.UserRole;
import com.kbph.logistics.sy.domain.UserVO;

import lombok.Getter;

public class CustomUserDetails implements UserDetails, Serializable {
	private static final long serialVersionUID = -965292052058204506L;

	@Getter
	private final UserVO userInfo;

	public CustomUserDetails(UserVO userInfo) {
		this.userInfo = userInfo;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singletonList(new SimpleGrantedAuthority((this.userInfo.getRolgkey() != null) ? (Constants.Auth.ROLE_PREFIX + this.userInfo.getRolgkey().toUpperCase()) : (Constants.Auth.ROLE_PREFIX + UserRole.USER.name())));
	}

	public String getUserkey() {
		return this.getUserInfo().getUserkey();
	}

	@Override
	public String getPassword() {
		return this.getUserInfo().getPasswrd();
	}

	@Override
	public String getUsername() {
		return this.getUserInfo().getUseract();
	}

	@Override
	public boolean isAccountNonExpired() {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

		String toDate = dateFormat.format(date);
		String expDate;

		if (this.getUserInfo().getIdexpdt() == null) {
			expDate = toDate;
		} else {
			expDate = this.getUserInfo().getIdexpdt();
		}

		return (toDate.compareTo(expDate) < 0);
	}

	@Override
	public boolean isAccountNonLocked() {
		return this.getUserInfo().getPwercnt() < Constants.Auth.ACCOUNT_LOCK_LIMIT;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return !Useyn.USE.getString().equals(this.getUserInfo().getAlocked());

	}

	// 계정승인여부
	public boolean isApproved() {
		return !Useyn.UNUSE.getString().equals(this.getUserInfo().getApproyn());
	}
}
