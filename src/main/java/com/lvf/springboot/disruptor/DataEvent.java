package com.lvf.springboot.disruptor;

/**
 * 事件实例封装 业务数据传递对象
 * 
 * @author admin
 *
 */
public class DataEvent {
	private long startTime;

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

}