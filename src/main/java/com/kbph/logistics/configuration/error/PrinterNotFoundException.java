package com.kbph.logistics.configuration.error;

public class PrinterNotFoundException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	private static final String MESSAGE = "ms.printerNotFound";

    public PrinterNotFoundException () {
    	super(MESSAGE);
    }

}
