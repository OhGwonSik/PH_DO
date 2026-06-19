package com.kbph.logistics.configuration.error;

public class RequiredNotValueException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private static final String MESSAGE = "ms.requiredValue";

	public RequiredNotValueException() {
		super(MESSAGE);
	}
}
