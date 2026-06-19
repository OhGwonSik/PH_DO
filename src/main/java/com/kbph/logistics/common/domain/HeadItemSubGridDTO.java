package com.kbph.logistics.common.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class HeadItemSubGridDTO<T3> extends HeadItemGridDTO<Object, Object> {
	private GridDTO<T3> subItemGrid;
}
