package com.kbph.logistics.configuration.type;

public enum UserRole {
	ADMIN("관리자"),
	USER("사용자"),
	DRIVER("기사"),
	OWNER("판매계약사"),
	TABLET("현장_태블릿")
	;

    private final String name;

    private UserRole(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
