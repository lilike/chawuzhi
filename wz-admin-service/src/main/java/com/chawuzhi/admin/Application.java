package com.chawuzhi.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages="com.chawuzhi.admin.mapper")
public class Application {

	public static void main(String[] args) {
		 SpringApplication.run(Application.class, args);
	}
	
}
