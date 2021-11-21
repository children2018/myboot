package com.lvf.aop;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSON;
import com.lvf.springboot.callable.Kabc;

@Configuration
public class DataSourceConfiguration {

	private Logger logger = Logger.getLogger(DataSourceConfiguration.class);

	/*
	 * @Value("${jdbc.type}") private Class<? extends DataSource>
	 * dataSourceType;
	 */

	@Bean(name = "writeDataSource")
	@Primary
	@ConfigurationProperties(prefix = "jdbc.database.master")
	public DataSource writeDataSource() {
		logger.info("-------------------- writeDataSource init ---------------------");
		DruidDataSource druidDataSource = new DruidDataSource();
		// 设置自定义filter
		// druidDataSource.setProxyFilters(proxyFilters());
		return druidDataSource;
	}

	@Bean(name = "readDataSource1")
	@ConfigurationProperties(prefix = "jdbc.database.slave1")
	public DataSource readDataSourceOne() {
		logger.info("-------------------- readDataSourceOne init ---------------------");
		DruidDataSource druidDataSource = new DruidDataSource();
		// 设置自定义filter
		// druidDataSource.setProxyFilters(proxyFilters());
		return druidDataSource;
	}

	@Bean(name = "readDataSource2")
	@ConfigurationProperties(prefix = "jdbc.database.slave2")
	public DataSource readDataSourceTwo() {
		logger.info("-------------------- readDataSourceTwo init ---------------------");
		DruidDataSource druidDataSource = new DruidDataSource();
		// 设置自定义filter
		// druidDataSource.setProxyFilters(proxyFilters());
		return druidDataSource;
	}
}
