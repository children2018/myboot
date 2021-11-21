package com.lvf.springboot.disruptor;

import java.util.Date;
import java.util.concurrent.Executors;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

public class Main {

    public static void main(String[] args) throws InterruptedException {
    	Date startDate = new Date();

        //创建订单工厂
        OrderFactory orderFactory = new OrderFactory();

        //ringbuffer的大小
        int RINGBUFFER_SIZE = 1024;

        //创建disruptor
        Disruptor<Order> disruptor = new Disruptor<Order>(orderFactory,RINGBUFFER_SIZE,Executors.defaultThreadFactory());

        //设置事件处理器 即消费者
        disruptor.handleEventsWith(new OrderHandler());

        disruptor.start();

        RingBuffer<Order> ringBuffer = disruptor.getRingBuffer();

        //-------------生产数据
        for(long i = 0 ; i < 10000000 ; i++){

        	final long j = i;
        	new Thread(new Runnable() {
        		@Override
        		public void run() {
		            long sequence = ringBuffer.next();
		
		            Order order = ringBuffer.get(sequence);
		            order.setId(j);
            
					ringBuffer.publish(sequence);
				}
			}).start();

           // System.out.println(Thread.currentThread().getName() + " 生产者发布一条数据:" + sequence + " 订单ID：" + i);
        }
        Date endDate = new Date();
        System.out.println("cost:" + (endDate.getTime() - startDate.getTime()));
        Thread.sleep(1000);

        disruptor.shutdown();
    }

}