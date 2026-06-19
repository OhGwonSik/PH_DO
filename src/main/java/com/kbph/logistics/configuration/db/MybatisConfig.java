package com.kbph.logistics.configuration.db;

import javax.sql.DataSource;

import org.apache.ibatis.session.LocalCacheScope;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@MapperScan(basePackages={
		"com.kbph.logistics.sy", // 추후 이동
		"com.kbph.logistics.bm",
		"com.kbph.logistics.main",
		"com.kbph.logistics.md",
		"com.kbph.logistics.om",
		"com.kbph.logistics.rp",
		"com.kbph.logistics.tm",
		"com.kbph.logistics.wm"
},sqlSessionFactoryRef=MybatisConfig.OPERATION_SESSION_FACTORY)
//@MapperScan(basePackages={
//		"com.kbph.logistics.sy",
//},sqlSessionFactoryRef=MybatisConfig.COMMON_SESSION_FACTORY)

public class MybatisConfig {
	// OP
	public static final String OPERATION_MYBATIS_SESSION_CONFIG = "operationMybatisSessionConfig";
	public static final String OPERATION_SESSION_FACTORY = "operationSessionFactory";
	public static final String OPERATION_SESSION_TEMPLATE = "operationSessionTemplate";
//	// COMMON
//	public static final String COMMON_MYBATIS_SESSION_CONFIG = "commonMybatisSessionConfig";
//	public static final String COMMON_SESSION_FACTORY = "commonSessionFactory";
//	public static final String COMMON_SESSION_TEMPLATE = "commonSessionTemplate";

	@Primary
	@Bean(name = OPERATION_SESSION_FACTORY, destroyMethod = "")
	public SqlSessionFactory operationSessionFactory(
			@Qualifier(DataSourceConfig.OPERATION_DATASOURCE) final DataSource dataSource,
			final ApplicationContext applicationContext) throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource);
		sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:mappers/operation/**/*.xml"));

		operationMybatisSessionConfig().setLocalCacheScope(LocalCacheScope.STATEMENT);
		operationMybatisSessionConfig().setCacheEnabled(false);
		operationMybatisSessionConfig().setJdbcTypeForNull(JdbcType.VARCHAR);
		sqlSessionFactoryBean.setConfiguration(operationMybatisSessionConfig());
		return sqlSessionFactoryBean.getObject();
	}

	@Primary
	@Bean(name = OPERATION_SESSION_TEMPLATE)
	public SqlSessionTemplate operationSessionTemplate(
			@Qualifier(OPERATION_SESSION_FACTORY) SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}

	//===============================
	// MYBATIS_SESSION_CONFIG
	//===============================
	@Bean(name = OPERATION_MYBATIS_SESSION_CONFIG)
	public org.apache.ibatis.session.Configuration operationMybatisSessionConfig(){
		org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
		configuration.setMapUnderscoreToCamelCase(true);
		return configuration;
	}

//	@Bean(name = COMMON_SESSION_FACTORY, destroyMethod = "")
//	public SqlSessionFactory commonSessionFactory(
//			@Qualifier(DataSourceConfig.COMMON_DATASOURCE) final DataSource dataSource,
//			final ApplicationContext applicationContext) throws Exception {
//		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
//		sqlSessionFactoryBean.setDataSource(dataSource);
//		sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:mappers/common/**/*.xml"));
//
//		operationMybatisSessionConfig().setLocalCacheScope(LocalCacheScope.STATEMENT);
//		operationMybatisSessionConfig().setCacheEnabled(false);
//		sqlSessionFactoryBean.setConfiguration(operationMybatisSessionConfig());
//		return sqlSessionFactoryBean.getObject();
//	}
//
//	@Bean(name = COMMON_SESSION_TEMPLATE)
//	public SqlSessionTemplate commonSessionTemplate(
//			@Qualifier(COMMON_SESSION_FACTORY) SqlSessionFactory sqlSessionFactory) {
//		return new SqlSessionTemplate(sqlSessionFactory);
//	}
//
//	@Bean(name = COMMON_MYBATIS_SESSION_CONFIG)
//	public org.apache.ibatis.session.Configuration commonMybatisSessionConfig(){
//		return new org.apache.ibatis.session.Configuration();
//	}
}
