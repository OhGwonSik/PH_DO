package com.kbph.logistics.bm.domain;

import com.kbph.logistics.common.domain.GridDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BtrfmaSaveDTO {

	GridDTO<BtrfmaDTO> mainGridParam;
	GridDTO<BsizpaDTO> itemGridParam;
}
