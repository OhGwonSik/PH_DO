package com.kbph.logistics.sy.domain;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RoleMenuWhDTO<T> {

	private String rolgkey; //권한그룹키

	private List<T> roleMenuWhList;
}
