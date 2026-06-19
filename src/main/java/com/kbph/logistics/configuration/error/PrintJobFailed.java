package com.kbph.logistics.configuration.error;

public class PrintJobFailed extends RuntimeException{
	private static final long serialVersionUID = 1L;
	private static final String MESSAGE = "ms.printJobFailed";

    public PrintJobFailed () {
    	super(MESSAGE);
    }

}
