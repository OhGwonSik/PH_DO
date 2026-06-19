package com.kbph.logistics.configuration.error;

public class NoReportDataException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private static final String MESSAGE = "ms.nodataForPrintOut";

    public NoReportDataException () {
        super(MESSAGE);
    }

    public NoReportDataException (String msg) {
        super(msg);
    }

}
