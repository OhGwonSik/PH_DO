package com.kbph.logistics.configuration.error;

public class DuplicateLayerException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private static final String MESSAGE = "ms.duplicateLayer";

    public DuplicateLayerException () {
        super(MESSAGE);
    }

}
