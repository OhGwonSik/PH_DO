package com.kbph.logistics.im.domain;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
/**
 * @author : t.s.park
 * @version : 1.0.0
 * @since : 2024-08-22
 * @note : OstritSeacrhParamDTO
 * ==================================================
 * DATE							AUTHOR            					NOTE
 * --------------------------------------------------------------------------------------
 * 2024-08-22					t.s.park        					create DTO class
 * --------------------------------------------------------------------------------------
 * ==================================================
 */
@Getter
@Setter
@ToString
@Builder
public class OstritSeacrhParamDTO {
	private String costrky;
	private String warekey;
	private Integer costrit;
	private List<String> stritstList;
}
