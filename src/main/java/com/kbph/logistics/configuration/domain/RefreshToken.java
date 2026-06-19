package com.kbph.logistics.configuration.domain;

import groovy.transform.ToString;
import lombok.Setter;

// 미사용
@Setter
@ToString
public class RefreshToken {
	private Long id; // 토큰 id
	private String useract; // 유저 id
	private String refreshJwt; // refresh token
	private Boolean expired; // 만료
	private Boolean revoked; // 폐기

	public Long getId() {
		return this.id;
	}
	public String getUseract() {
		return this.useract;
	}
	public String getRefreshJwt() {
		return this.refreshJwt;
	}
	public Boolean isExpired() {
		return this.expired;
	}
	public Boolean isRevoked() {
		return this.revoked;
	}
}
