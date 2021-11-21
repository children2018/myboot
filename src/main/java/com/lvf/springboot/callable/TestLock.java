package com.lvf.springboot.callable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.Test;

public class TestLock implements Runnable {
	
	private volatile boolean status = true;
	
	private List<Integer> list = new ArrayList<Integer>();
	
	private Lock lock = new ReentrantLock(true);  //参数为true，公平锁
	
	private Condition condition = lock.newCondition();
	
	public void add() {
		try {
			lock.lockInterruptibly();
			list.add(1);
			list.add(2);
			list.add(3);
			list.add(4);
			list.add(5);
			list.add(6);
			list.add(7);
			list.add(8);
			list.add(9);
			list.add(10);
			condition.signalAll();
			lock.unlock();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void addOne(int i) {
		try {
			lock.lockInterruptibly();
			list.add(i);
			condition.signalAll();
			lock.unlock();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void destroy() {
		try {
			lock.lockInterruptibly();
			this.status = false;
			condition.signalAll();
			lock.unlock();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		while (status) {
			try {
				System.out.println(Thread.currentThread().getName() + "我进来了");
				lock.lockInterruptibly();
				System.out.println(Thread.currentThread().getName() + "我获得了锁");
				while (status && list.size() == 0) {
					System.out.println(Thread.currentThread().getName() + "我在等待");
					condition.await(1, TimeUnit.HOURS);
					System.out.println(Thread.currentThread().getName() + "我在执行，不在等待");
				}
				if (status) {
					System.out.println(Thread.currentThread().getName() + " 移除前的值list:" + list);
					list.remove(0);
					//Thread.sleep(1000*1);
					System.out.println(Thread.currentThread().getName() + " 移除后的值list:" + list);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				System.out.println(Thread.currentThread().getName() + "我释放了锁");
				lock.unlock();
			}
			
		}
		
	}
	
	@Test
	public void test1() {
		TestLock test = new TestLock();
		test.add();
		
		try {
			Thread.sleep(1000 * 1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Thread t1 = new Thread(test);
		t1.start();
		Thread t2 = new Thread(test);
		t2.start();
		Thread t3 = new Thread(test);
		t3.start();
		
		
		try {
			Thread.sleep(1000 * 10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		test.add();
		
		
		try {
			Thread.sleep(1000 * 15);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		test.destroy();
	}
	
	@Test
	public void test2() {
		TestLock test = new TestLock();
		
		
		final TestLock testFinal = test;
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				int i = 30;
				while (true) {
					testFinal.addOne(++i);
					System.out.println("set " + i);
					try {
						Thread.sleep(1000*1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
		
		
		try {
			Thread.sleep(1000 * 10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Thread t1 = new Thread(test);
		t1.start();
		Thread t2 = new Thread(test);
		t2.start();
		Thread t3 = new Thread(test);
		t3.start();
		
		try {
			Thread.sleep(1000 * 60);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		test.destroy();
	}
	
}
