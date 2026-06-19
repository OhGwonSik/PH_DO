package com.kbph.logistics.jasper;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Configuration
@PropertySource("classpath:/jasper.properties")
@ConfigurationProperties(prefix = "jasper")
@Getter
@Setter
@ToString
public class JasperConfig {
	private List<String> headerNames;
	private List<String> headerValues;
	private String reportPath;
	private String extName;
}
