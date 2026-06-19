package com.kbph.logistics.sy.domain;


import java.io.Serializable;
import java.util.List;

import com.kbph.logistics.common.domain.CommonColumnDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class HeaderAndMenuDTO extends CommonColumnDTO implements Serializable {

	private static final long serialVersionUID = 6772093399940321141L;

	private List<SmnghdDTO> mainHeaderList; //헤더리스트
	private List<SmnumaDTO> userMenuList; //메뉴리스트

}
