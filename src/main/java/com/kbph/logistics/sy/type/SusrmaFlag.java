package com.kbph.logistics.sy.type;

public enum SusrmaFlag {
	APPROVE("Y") // APPROYN - 사용자승인여부
	, UNAPPROVE("N")
	, SIGNED("Y") // SIGUPYN - 회원가입여부
	, UNSIGNED("N")
	, APP_USER("Y") // APUSEYN - 앱사용자여부
	, APP_UNUSER("N")
	, ACTIVE("Y") // ACTIVYN - 사용자 활성화여부
	, DEACTIVE("N");

	private final String string;

	SusrmaFlag(String string) {
		this.string = string;
	}

	public String getString() {
		return string;
	}
}