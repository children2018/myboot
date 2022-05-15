package com.lvf.springboot.utils;

import org.springframework.context.ApplicationContext;

public class ApplicationContextUtil {
	
	private static ApplicationContext applicationContext;
	
	public static <T> T getBean(Class<T> requiredType) {
		return applicationContext.getBean(requiredType);
	}

	public static void setApplicationContext(ApplicationContext a) {
		System.out.println("setApplicationContext.a:" + a);
		applicationContext = a;
	}
	
}