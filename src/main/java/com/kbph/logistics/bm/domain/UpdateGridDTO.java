package com.kbph.logistics.bm.domain;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class UpdateGridDTO<T1,T2> {
	private List<T1> headUpdateList;
	private List<T2> itemUpdateList;
}
