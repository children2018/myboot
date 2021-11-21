package com.lvf.springboot.callable;

import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import org.junit.Test;

public class CallableFutureTest {
	
	@Test
	public void test() throws Exception {


		// FutureTask 是实现了Runnable和Future接口的实现类
		FutureTask[] randomNumberTasks = new FutureTask[5];

		Thread[] threads = new Thread[5];

		for (int i = 0; i < 5; i++) {
			Callable callable = new CallableExample(i);

			// 通过Callable创建FutureTask
			randomNumberTasks[i] = new FutureTask(callable);

			// 因为FutureTask实现了Runnable接口, 因此可以通过 FutureTask创建一个线程对象。
			Thread t = new Thread(randomNumberTasks[i]);
			threads[i] = t;
			threads[i].start();
			// System.out.println(randomNumberTasks[i].get());
		}
		
		Thread.sleep(1000 * 10);

		Long startTime = new Date().getTime();
		for (int i = 0; i < 5; i++) {
			// 当线程结束可以通过get()方法获取线程的运行结果。
			final int j = i;
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						System.out.println(randomNumberTasks[j].get());
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (ExecutionException e) {
						e.printStackTrace();
					}
				}
			}).start();

			// get方法在没有获得结果的时候是阻塞状态的。
		}
		
		System.out.println("endTime:" + (new Date().getTime() - startTime));
	}
	
	/**
	 * 说明futureTask的get方法是同步的，不是异步
	 * 而线程是先执行完
	 * @throws Exception
	 */
	@Test
	public void test2() throws Exception {

		// FutureTask 是实现了Runnable和Future接口的实现类
		FutureTask[] randomNumberTasks = new FutureTask[5];

		Thread[] threads = new Thread[5];

		for (int i = 0; i < 5; i++) {
			Callable callable = new CallableExample(i);

			// 通过Callable创建FutureTask
			randomNumberTasks[i] = new FutureTask(callable);

			// 因为FutureTask实现了Runnable接口, 因此可以通过 FutureTask创建一个线程对象。
			Thread t = new Thread(randomNumberTasks[i]);
			threads[i] = t;
			threads[i].start();
			// System.out.println(randomNumberTasks[i].get());
		}
		
		Thread.sleep(1000 * 10);

		Long startTime = new Date().getTime();
		for (int i = 0; i < 5; i++) {
			// 当线程结束可以通过get()方法获取线程的运行结果。
			System.out.println(randomNumberTasks[i].get());
			// get方法在没有获得结果的时候是阻塞状态的。
		}
		
		System.out.println("endTime:" + (new Date().getTime() - startTime));
	}
	
	
	@Test
	public void test3() throws Exception {

		// FutureTask 是实现了Runnable和Future接口的实现类
		FutureTask[] randomNumberTasks = new FutureTask[5];

		Thread[] threads = new Thread[5];

		for (int i = 0; i < 5; i++) {
			Callable callable = new CallableExample2(i);

			// 通过Callable创建FutureTask
			randomNumberTasks[i] = new FutureTask(callable);

			// 因为FutureTask实现了Runnable接口, 因此可以通过 FutureTask创建一个线程对象。
			Thread t = new Thread(randomNumberTasks[i]);
			threads[i] = t;
			threads[i].start();
			// System.out.println(randomNumberTasks[i].get());
		}
		
		Long startTime = new Date().getTime();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				int i = 0;
				while(true) {
					try {
						Thread.sleep(1000*1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					i++;
					System.out.println("time cost -------------" + i);
				}
				
			}
		}).start();
		
		for (int i = 0; i < 5; i++) {
			// 当线程结束可以通过get()方法获取线程的运行结果。
			System.out.println("[xx]".replace("xx", "" + i) + randomNumberTasks[i].get());
			// get方法在没有获得结果的时候是阻塞状态的。
		}
		
		System.out.println("endTime:" + (new Date().getTime() - startTime));
	}
	
	@Test
	public void test4() throws Exception {

		// FutureTask 是实现了Runnable和Future接口的实现类
		FutureTask[] randomNumberTasks = new FutureTask[5];

		Thread[] threads = new Thread[5];

		for (int i = 0; i < 5; i++) {
			Callable callable = new CallableExample3(i);

			// 通过Callable创建FutureTask
			randomNumberTasks[i] = new FutureTask(callable);

			// 因为FutureTask实现了Runnable接口, 因此可以通过 FutureTask创建一个线程对象。
			Thread t = new Thread(randomNumberTasks[i]);
			threads[i] = t;
			threads[i].start();
			// System.out.println(randomNumberTasks[i].get());
		}
		
		Long startTime = new Date().getTime();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				int i = 0;
				while(true) {
					try {
						Thread.sleep(1000*1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					i++;
					System.out.println("time cost -------------" + i);
				}
				
			}
		}).start();
		
//		for (int i = 0; i < 5; i++) {
//			// 当线程结束可以通过get()方法获取线程的运行结果。
//			System.out.println("[xx]".replace("xx", "" + i) + randomNumberTasks[i].get());
//			// get方法在没有获得结果的时候是阻塞状态的。
//		}
		//等效于循环
		System.out.println("[xx]".replace("xx", "" + 0) + randomNumberTasks[0].get());
		System.out.println("[xx]".replace("xx", "" + 1) + randomNumberTasks[1].get());
		System.out.println("[xx]".replace("xx", "" + 2) + randomNumberTasks[2].get());
		System.out.println("[xx]".replace("xx", "" + 3) + randomNumberTasks[3].get());
		System.out.println("[xx]".replace("xx", "" + 4) + randomNumberTasks[4].get());
		
		System.out.println("endTime:" + (new Date().getTime() - startTime));
	}
	
}