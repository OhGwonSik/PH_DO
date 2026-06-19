package com.kbph.logistics.fcm.type;

public enum FcmEnum {
	API_URL("https://fcm.googleapis.com/v1/projects/kpslp-app-push/messages:send"),
	FIREBASE_KEY("firebase/firebase_service_key.json"),
	GOOGLE_URL("https://www.googleapis.com/auth/cloud-platform"),
	DISPATCH("DISPATCH"),
	COMWARE("COMWARE"),
	TM("tm"),
	CONTROL("control"),
	CANCEL("cancel"),
	UPCANCEL("CANCEL");
	
	private final String string;
	
	FcmEnum(String string) {
		this.string = string;
	}
	
	public String getString() {
		return string;
	}
}
