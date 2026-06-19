package com.kbph.logistics.smp.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SmpResponse {
	
    private String httpStatus;
    private String message;
    private int statusCode;
}
