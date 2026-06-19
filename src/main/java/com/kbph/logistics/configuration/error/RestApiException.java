package com.kbph.logistics.configuration.error;

public class RestApiException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private static final String MESSAGE = "ms.restApiErr";

    public RestApiException () {
        super(MESSAGE);
    }

    public RestApiException (String msg) {
        super(msg);
    }

}
