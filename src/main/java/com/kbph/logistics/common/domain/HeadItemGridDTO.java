package com.kbph.logistics.common.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class HeadItemGridDTO<T, T2> {
	private GridDTO<T> headGrid;
	private GridDTO<T2> itemGrid;
}
