package com.kbph.logistics.api.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class CommonInfoDTO {
	private String schema;
	private String commonSchema;
	private String useract;
}
