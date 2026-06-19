package com.kbph.logistics.configuration.error;

import org.springframework.dao.DuplicateKeyException;

public class DuplicateKeyAutowiredException extends DuplicateKeyException{
	private static final long serialVersionUID = 1L;
	private static final String MESSAGE = "ms.DuplicateKeyErr";

	public DuplicateKeyAutowiredException() {
		super(MESSAGE);
	}
}
