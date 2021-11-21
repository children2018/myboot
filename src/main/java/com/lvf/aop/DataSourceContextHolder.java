package com.lvf.aop;

import org.apache.log4j.Logger;

public class DataSourceContextHolder {
	
	private static final ThreadLocal<String> local = new ThreadLocal<String>();
	
	private static final Logger logger = Logger.getLogger(DataSourceContextHolder.class);

	public static ThreadLocal<String> getLocal() {
		return local;
	}

	/**
	 * 从库可能有多个
	 */
	public static void read() {
		local.set(DataSourceType.READ.getType());
	}

	/**
	 * 主库只有一个
	 */
	public static void write() {
		local.set(DataSourceType.WRITE.getType());
	}

	public static String getJdbcType() {
		return local.get();
	}
	
	public static void clear() {
		//logger.info("正在清除标识：" + local.get());
		local.remove();
	}
}
