package com.kbph.logistics.api.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ApiCommonDTO<T> {

	@JsonProperty("api_code")
	private String apiCode;

	@JsonProperty("send_date")
	private String sendDate;

	@JsonProperty("send_time")
	private String sendTime;

	@JsonProperty("api_status")
	private int apiStatus;

	@JsonProperty("api_data")
	private List<T> apiData;

	private String schema;
	private String commonSchema;
	private String useract;
}
