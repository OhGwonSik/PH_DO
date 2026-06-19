package com.kbph.logistics.im.domain;

import java.util.List;

import com.kbph.logistics.common.domain.CommonColumnDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class InbInspectionDTO extends CommonColumnDTO{

	private WasnhdDTO headParam;
	private List<WasnitDTO> itemParam;
}
