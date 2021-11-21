package com.lvf.springboot.callable;

import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

import org.junit.Test;

public class TestLockArrayBlockingQueue2 implements Runnable {
	
	private volatile boolean status = true;
	
	private Queue<Integer> list = new ArrayBlockingQueue<Integer>(100);
	
	public void add() {
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
			list.add(11);
			list.add(12);
			list.add(13);
			list.add(14);
			list.add(15);
			list.add(16);
			list.add(17);
			list.add(18);
			list.add(19);
			list.add(20);
			list.add(21);
			list.add(22);
			list.add(23);
	}
	
	@Override
	public void run() {
		int sum = 0;
		Integer value = null;
		while (status) {
			try {
				value = list.poll();
				if (value == null) {
					if (++sum ==3) {
						System.exit(0);
					}
				}
				if (status) {
					System.out.println(Thread.currentThread().getName() + " 获取到的值value:" + value);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
			}
			
		}
		
	}
	
	public static void main(String[] args) {
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				int i =0;
				while(true) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println(++i);
				}
			}
			
		});

		TestLockArrayBlockingQueue2 test = new TestLockArrayBlockingQueue2();
		test.add();
		
		try {
			Thread.sleep(1000 * 1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Thread t1 = new Thread(test);
		Thread t2 = new Thread(test);
		Thread t3 = new Thread(test);
		t1.start();
		t2.start();
		t3.start();
	
	}
	
}
