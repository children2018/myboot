package com.lvf;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
@MapperScan("com.lvf.springboot.mapper")
public class MyBoot {
	
	public static void main(String[] args) {
		SpringApplication.run(MyBoot.class, args);
	}
	
}
