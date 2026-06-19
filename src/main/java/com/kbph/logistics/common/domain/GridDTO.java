package com.kbph.logistics.common.domain;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GridDTO<T> {
	private List<T> addList;
	private List<T> updateList;
	private List<T> deleteList;
	private List<T> oldList;
}
