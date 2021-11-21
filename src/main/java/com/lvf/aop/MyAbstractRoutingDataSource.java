package com.lvf.aop;

import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.Logger;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.util.StringUtils;

public class MyAbstractRoutingDataSource extends AbstractRoutingDataSource {

	private Logger logger = Logger.getLogger(MyAbstractRoutingDataSource.class);

	private final int dataSourceNumber;
	private AtomicLong count = new AtomicLong(0);

	public MyAbstractRoutingDataSource(int dataSourceNumber) {
		this.dataSourceNumber = dataSourceNumber;
	}

	@Override
	protected Object determineCurrentLookupKey() {
		Object resultObject = null;
		String typeKey = DataSourceContextHolder.getJdbcType();
		// 只对主库开启事务，如果typeKey为空表示获取主库的datasource
		if (StringUtils.isEmpty(typeKey) || DataSourceType.WRITE.getType().equals(typeKey)) {
			resultObject = DataSourceType.WRITE.getType();
		} else {
			// 读简单负载均衡
			Long number = count.getAndAdd(1);
			Long lookupKey = number % dataSourceNumber;
			resultObject = "" + lookupKey;
		}
		//logger.info("determineCurrentLookupKey:" + resultObject);
		return resultObject;
	}
}
