package com.lvf.springboot.disruptor;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

public class DisruptorTest {
	public static void execute() {

		Disruptor<DataEvent> disruptor = new Disruptor<DataEvent>(new DataEventFactory(), CompareTest.SIZE,
				new ThreadFactory() {
					@Override
					public Thread newThread(Runnable eventProcessor) {
						CompareTest.println("EventProcessor wrapper");// 对事件处理总线的封装
						Thread thread = new Thread(eventProcessor);
						thread.setName("EventProcessorWrapper");
						return thread;
					}
				});
		/**
		 * 创建EventProcessors<Runnable>.
		 * 子过程Disruptor.checkNotStarted()事件处理handler必须在启动之前绑定.
		 */
		disruptor.handleEventsWith(new DataEventHandler());

		disruptor.start();
		CompareTest.println("disruptor start success！");

		RingBuffer<DataEvent> ringBuffer = disruptor.getRingBuffer();
		DataProducer producer = new DataProducer(ringBuffer);
		DataEventProducerWithTranslator translator = new DataEventProducerWithTranslator(ringBuffer);
		long start = System.currentTimeMillis();

		for (int l = 0; l < CompareTest.THREAD; l++) {
			new Thread(() -> {
				for (int m = 0; m < CompareTest.PER; m++) {
					producer.onData(start);
					// translator.onData(start);
				}
			}).start();
		}
		/**
		 * 关闭 disruptor，方法会堵塞，直至所有的事件都得到处理；并不会自动关闭外部指定的executor,需要主动关闭
		 */
		//disruptor.shutdown();
        // 	CompareTest.println("disruptor shutdown success！");
	}
}