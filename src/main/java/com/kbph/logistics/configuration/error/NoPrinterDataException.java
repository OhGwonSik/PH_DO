package com.kbph.logistics.configuration.error;

public class NoPrinterDataException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private static final String MESSAGE = "ms.nodataForPrinter";

    public NoPrinterDataException () {
        super(MESSAGE);
    }

}
