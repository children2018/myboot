package com.lvf.springboot.disruptor;

import java.util.concurrent.atomic.AtomicLong;

import com.lmax.disruptor.EventHandler;

/**
 * 对指定事件的处理过程
 *
 */
public class DataEventHandler implements EventHandler<DataEvent> {
	public AtomicLong count = new AtomicLong(0);

	@Override
	public void onEvent(DataEvent event, long sequence, boolean endOfBatch) throws Exception {
		/**
		 * 消费者线程由初始化Disruptor时指定的threadFactory创建的
		 */
		if (count.incrementAndGet() == CompareTest.TOTAL_COUNT) {
			CompareTest.println("处理的sequence：" + sequence + " count：" + count.get() + "  Disruptor 总耗时："
					+ (System.currentTimeMillis() - event.getStartTime()));
		}
	}

}