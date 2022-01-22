package com.lvf.springboot.callable;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

public class CurrentMapTest {

	@Test
	public void test1() {
		long start = System.currentTimeMillis();
		ConcurrentMap<String, Integer> map = new ConcurrentHashMap<>();
		CountDownLatch cdl = new CountDownLatch(200000);
		for (int i = 1; i <= 200000; i++) {
			final int j = i;
			new Thread(new Runnable() {
				public void run() {
					map.put("sss" + j, j);
					cdl.countDown();
				}
			}).start();
		}
		
		try {
			cdl.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		System.out.println("cost:" + (end - start));
	}
	
	@Test
	public void test2() {
		long start = System.currentTimeMillis();
		Hashtable<String, Integer> map = new Hashtable<>();
		CountDownLatch cdl = new CountDownLatch(200000);
		for (int i = 1; i <= 200000; i++) {
			final int j = i;
			new Thread(new Runnable() {
				public void run() {
					map.put("sss" + j, j);
					cdl.countDown();
				}
			}).start();
		}
		
		try {
			cdl.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		System.out.println("cost:" + (end - start));
	}
	
	@Test
	public void test3() {
		long start = System.currentTimeMillis();
		HashMap<String, Integer> map = new HashMap<>();
		CountDownLatch cdl = new CountDownLatch(200000);
		for (int i = 1; i <= 200000; i++) {
			final int j = i;
			new Thread(new Runnable() {
				public void run() {
					map.put("sss" + j, j);
					cdl.countDown();
				}
			}).start();
		}
		
		try {
			cdl.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		System.out.println("cost:" + (end - start));
	}

}
