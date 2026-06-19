package com.kbph.logistics.im.domain;

import com.kbph.logistics.common.domain.GridDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class InboundCheckDTO <H, I> {
	private H headGrid;
	private GridDTO<I> itemGrid;
}
