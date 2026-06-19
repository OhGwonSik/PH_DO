package com.kbph.logistics.common.domain;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class HeadItemGridListDTO<T1, T2> {
	private List<T1> headList;
	private List<T2> itemList;
}
