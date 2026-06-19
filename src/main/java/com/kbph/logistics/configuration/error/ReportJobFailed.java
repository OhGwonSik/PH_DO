package com.kbph.logistics.configuration.error;

public class ReportJobFailed extends RuntimeException{
	private static final long serialVersionUID = 1L;
	private static final String MESSAGE = "ms.reportJobFailed";

    public ReportJobFailed () {
    	super(MESSAGE);
    }

    public ReportJobFailed (String msg) {
    	super(msg);
    }

}
