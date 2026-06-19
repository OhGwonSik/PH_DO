package com.kbph.logistics.md.type;

public enum Tasksts {
	NEW("NEW","신규"),
	OPERATION_START("OPERSTR", "조업시작"),
	TASK_COMPLETE("TASKCMP", "작업완료"),
	OPERATION_COMPLETE("OPERCMP", "조업완료"),
	COMPLETE("CMP", "완료"),
	SHPOUT("SHPOUT", "출고완료"),
	CANCEL("CANCEL", "취소");

	private final String code;
	private final String description;

	Tasksts (String code, String description){
		this.code = code;
		this.description = description;
	}
	public String getCode() {
		return code;
	}
	public String getDescription() {
		return description;
	}
}
