package com.kbph.logistics.sy.domain;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserLayoutDTO {
	private List<SmnghdDTO> mainHeaderList;
	private List<SmnumaDTO> userMenuList;
}
