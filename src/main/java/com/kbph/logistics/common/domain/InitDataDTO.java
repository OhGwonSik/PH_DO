package com.kbph.logistics.common.domain;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class InitDataDTO {
	private Map<String, Object> item;
}
