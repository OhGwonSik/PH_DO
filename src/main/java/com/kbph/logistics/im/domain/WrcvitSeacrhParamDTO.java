package com.kbph.logistics.im.domain;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
/**
 * @author : t.s.park
 * @version : 1.0.0
 * @since : 2024-09-06
 * @note : WrcvitSeacrhParamDTO
 * ==================================================
 * DATE							AUTHOR            					NOTE
 * --------------------------------------------------------------------------------------
 * 2024-09-06					t.s.park        					create DTO class
 * --------------------------------------------------------------------------------------
 * ==================================================
 */
@Getter
@Setter
@ToString
@Builder
public class WrcvitSeacrhParamDTO {
	private String rcvdcky;
	private String warekey;
	private Integer rcvdcit;
	private String eoasnky;
	private List<String> rcvitstList;
}
