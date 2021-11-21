package com.lvf.springboot.callable;

import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class TestCyclicBarrier {

	private static final int THREAD_NUM = 21;

	public static class WorkerThread implements Runnable {
		CountDownLatch cout;
		CyclicBarrier barrier;
		CyclicBarrier barrier2;

		public WorkerThread(CyclicBarrier b, CyclicBarrier c ,CountDownLatch cout) {
			this.barrier = b;
			this.barrier2 = c;
			this.cout = cout;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				cout.await();
				System.out.println(Thread.currentThread().getName() +  "Worker's come here");
				//barrier2.await();
				barrier.await();
				//Thread.sleep(1000 * 5);
				//System.out.println(Thread.currentThread().getName() + " Working" + new Date().getTime());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CyclicBarrier cb = new CyclicBarrier(2, new Runnable() {
			// 当所有线程到达barrier时执行
			@Override
			public void run() {
				// TODO Auto-generated method stub
				System.out.println("Inside Barrier");

			}
		});
		
		CountDownLatch cout = new CountDownLatch(THREAD_NUM);
		CyclicBarrier b2 = new CyclicBarrier(THREAD_NUM);
		for (int i = 0; i < THREAD_NUM; i++) {
			new Thread( new WorkerThread(cb, b2, cout), "" + (i+10)).start();
			cout.countDown();
			try {
				Thread.sleep(1000*1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			Thread.sleep(1000* 20);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("it's done");
	}

}
/*
 * 以下是输出： Worker's waiting Worker's waiting Worker's waiting Worker's waiting
 * Worker's waiting Inside Barrier ID:12 Working ID:8 Working ID:11 Working ID:9
 * Working ID:10 Working
 */