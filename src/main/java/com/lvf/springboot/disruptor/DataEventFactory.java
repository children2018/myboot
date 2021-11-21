package com.lvf.springboot.disruptor;
import java.util.concurrent.atomic.AtomicInteger;

import com.lmax.disruptor.EventFactory;

/*
 * 构建传递的数据封装对象， 在初始化ringBuffer时，直接给entries[]每个地址上初始化DataEvent
 */
public class DataEventFactory implements EventFactory {
	private AtomicInteger count = new AtomicInteger(0);

	@Override
	public Object newInstance() {
		//CompareTest.println("dataEvent newInstance: " + count.incrementAndGet());
		return new DataEvent();
	}

}