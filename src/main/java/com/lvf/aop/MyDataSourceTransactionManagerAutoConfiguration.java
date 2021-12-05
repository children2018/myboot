package com.lvf.aop;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import lombok.extern.slf4j.Slf4j;

/**
 * 重写事务配置
 */
@Configuration
@EnableTransactionManagement
@Slf4j
public class MyDataSourceTransactionManagerAutoConfiguration extends DataSourceTransactionManagerAutoConfiguration {
	
	@Autowired
	private SpringContextHolder springContextHolder;
	
	private Logger logger = Logger.getLogger(MyDataSourceTransactionManagerAutoConfiguration.class);
	/**
	 * 自定义事务
	 * @return
	 */
	@Bean
	public DataSourceTransactionManager transactionManagers() {
		logger.info("-------------------- transactionManager init ---------------------");
		return new DataSourceTransactionManager(springContextHolder.getBean("roundRobinDataSouceProxy"));
	}
}
