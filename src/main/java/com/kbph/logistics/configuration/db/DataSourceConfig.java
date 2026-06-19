package com.kbph.logistics.configuration.db;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Configuration
@ConfigurationProperties
@Profile("!local-prod")
public class DataSourceConfig {
	public static final String OPERATION_DATASOURCE = "operationDs";
	public static final String COMMON_DATASOURCE = "commonDs";

	@Primary
	@Bean(name = OPERATION_DATASOURCE)
	@ConfigurationProperties(prefix = "spring.datasource.hikari.operation")
	public DataSource operationDs() {
		return new HikariDataSource();
	}

	@Bean(name = COMMON_DATASOURCE)
	@ConfigurationProperties(prefix = "spring.datasource.hikari.common")
	public DataSource commonDs() {
		return new HikariDataSource();
	}
}