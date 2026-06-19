package com.kbph.logistics.im.domain;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
/**
 * @author : t.s.park
 * @version : 1.0.0
 * @since : 2024-08-01
 * @note : WasnitSeacrhParamDTO
 * ==================================================
 * DATE							AUTHOR            					NOTE
 * --------------------------------------------------------------------------------------
 * 2024-08-01					t.s.park        					create DTO class
 * --------------------------------------------------------------------------------------
 * ==================================================
 */
@Getter
@Setter
@ToString
@Builder
public class WasnitSeacrhParamDTO {
	private String eoasnky;
	private String warekey;
	private Integer eoasnit;
	private List<String> asnitstList;
}
