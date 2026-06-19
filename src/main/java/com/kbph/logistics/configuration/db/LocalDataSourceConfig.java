package com.kbph.logistics.configuration.db;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@ConfigurationProperties
@Profile("local-prod")
public class LocalDataSourceConfig {
	public static final String OPERATION_DATASOURCE = "operationDs";
	public static final String COMMON_DATASOURCE = "commonDs";

    @Value("${ssh.host}")
    private String sshHost;

    @Value("${ssh.port}")
    private int sshPort;
    
    @Value("${ssh.username}")
    private String sshUsername;

    @Value("${ssh.password}")
    private String sshPassword;

    @Value("${database.host}")
    private String databaseHost;

    @Value("${database.port}")
    private int databasePort;
    
	@Primary
	@Bean(name = OPERATION_DATASOURCE)
	@ConfigurationProperties(prefix = "spring.datasource.hikari.operation")
	public DataSource operationDs() throws JSchException {
        JSch jsch = new JSch();
        Session session = jsch.getSession(sshUsername, sshHost, sshPort);
        session.setPassword(sshPassword);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();
        session.setPortForwardingL(4307, databaseHost, databasePort);
		return new HikariDataSource();
	}

	@Bean(name = COMMON_DATASOURCE)
	@ConfigurationProperties(prefix = "spring.datasource.hikari.common")
	public DataSource commonDs() throws JSchException {
        JSch jsch = new JSch();
        Session session = jsch.getSession(sshUsername, sshHost, sshPort);
        session.setPassword(sshPassword);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();
        session.setPortForwardingL(4307, databaseHost, databasePort);
		return new HikariDataSource();
	}
}