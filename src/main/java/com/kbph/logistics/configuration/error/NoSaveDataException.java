package com.kbph.logistics.configuration.error;

public class NoSaveDataException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private static final String MESSAGE = "ms.nodata";

	public NoSaveDataException() {
		super(MESSAGE);
	}

	public NoSaveDataException(String msg) {
		super(msg);
	}
}
