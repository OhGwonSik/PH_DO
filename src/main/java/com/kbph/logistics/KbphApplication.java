package com.kbph.logistics;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan
@MapperScan
@EnableScheduling
@PropertySource(value = "classpath:application.yml", encoding = "UTF-8")
public class KbphApplication {

	public static void main(String[] args) {
		SpringApplication.run(KbphApplication.class, args);
	}

}
