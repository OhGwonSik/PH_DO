package com.kbph.logistics.configuration.error;

public class TaskProcessingException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public TaskProcessingException(String message) {
		super(message);
	}
}
