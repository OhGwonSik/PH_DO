package com.kbph.logistics.configuration.error;

public class PrintLoadFailed extends RuntimeException{
	private static final long serialVersionUID = 1L;
	private static final String MESSAGE = "ms.printLoadFailed";

    public PrintLoadFailed () {
    	super(MESSAGE);
    }

}
