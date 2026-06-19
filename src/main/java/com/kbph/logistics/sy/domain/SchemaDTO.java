package com.kbph.logistics.sy.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SchemaDTO {

	private String catalogName;
	private String schemaName;
	private String defaultCharacterSetName;
	private String sqlPath;
	private String schemaComment;
}
