package com.lvf.aop;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * 数据库配置
 * @author Administrator
 *
 */
@Configuration
@AutoConfigureAfter({ DataSourceConfiguration.class })
@Slf4j
public class MybatisConfiguration extends MybatisAutoConfiguration {
	public MybatisConfiguration(MybatisProperties properties, 
			ObjectProvider<Interceptor[]> interceptorsProvider,
			ResourceLoader resourceLoader, 
			ObjectProvider<DatabaseIdProvider> databaseIdProvider,
			ObjectProvider<List<ConfigurationCustomizer>> configurationCustomizersProvider) {
		super(properties, interceptorsProvider, resourceLoader, databaseIdProvider, configurationCustomizersProvider);
	}
	
	@Autowired
	private SpringContextHolder springContextHolder;

	@Value("${jdbc.database.config.slavenum}")
	private String dataSourceSize;

	/**
	 * 重载父类 sqlSessionFactory
	 * @return
	 * @throws Exception
	 */
	@Bean
	public SqlSessionFactory sqlSessionFactorys() throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(this.roundRobinDataSouceProxy());
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:/mapper/*.xml"));
		return sqlSessionFactoryBean.getObject();
	}

	/**
	 * 加载数据库配置
	 * @return
	 */
	@Bean(name = "roundRobinDataSouceProxy")
	public AbstractRoutingDataSource roundRobinDataSouceProxy() {
		System.out.println("dataSourceSize:" + dataSourceSize);
		int size = Integer.parseInt(dataSourceSize);
		MyAbstractRoutingDataSource proxy = new MyAbstractRoutingDataSource(size);
		Map<Object, Object> targetDataSources = new HashMap<Object, Object>();
		DataSource writeDataSource = springContextHolder.getBean("writeDataSource");
		// 加载主库配置
		targetDataSources.put(DataSourceType.WRITE.getType(), springContextHolder.getBean("writeDataSource"));

		// 加载从库配置
		for (int i = 0; i < size; i++) {
			targetDataSources.put("" + i, springContextHolder.getBean("readDataSource" + (i + 1)));
		}
		// 指定默认的数据源
		proxy.setDefaultTargetDataSource(writeDataSource);
		proxy.setTargetDataSources(targetDataSources);
		// 返回所有数据源配置
		return proxy;
	}

}
