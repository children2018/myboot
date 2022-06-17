package com.lvf.springboot.mutilthread;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DataService<T> extends Thread {
	
	private int size;
	private int doneNum = 0;
	private int threadNum;
	private List<T> list;
	private Lock lock = new ReentrantLock();
	private Lock lock2 = new ReentrantLock();
	private Condition condition = lock.newCondition();
	private Condition condition2 = lock.newCondition();
	private boolean run = false;
	
	public DataService(int size, int threadNum, List<T> list) {
		this.size = size;
		this.list = list;
		this.threadNum = threadNum;
	}
	
	private boolean isRun(int state) {
		if (state == 1) {
			run = true;
		} 
		return run;
	}
	
	@Override
	public void run() {
		lock.lock();
		System.out.println("run获得了资源");
		try {
			if (!isRun(0)) {
				System.out.println("我睡着了。。。");
				condition2.await();
				System.out.println("我醒了。。。");
			} else {
				System.out.println("我没睡 不用叫醒我。。。");
			}
			this.process();
		} catch (Exception e) {
			e.printStackTrace();
		}
		lock.unlock();
	}
	
	public void doneNum(String uuid) {
		lock2.lock();
		System.out.println(uuid + "获得了资源");
		if (++doneNum >= threadNum){
			lock.lock();
			System.out.println("game over...");
			condition.signal();
			lock.unlock();
		} 
		System.out.println(uuid + "释放了资源");
		lock2.unlock();
	}
	
	public void waitThis() {
		lock.lock();
		System.out.println("waitThis获得了资源");
		try {
			if (isRun(1)) {
				System.out.println("我叫醒它。。。");
				condition2.signal();
			}
			System.out.println("我等待。。。");
			condition.await();
		} catch (Exception e) {
			e.printStackTrace();
		}
		lock.unlock();
	}
	
	public void process(){
		Data<T> data = new Data<T>(size, list);
		for (int i = 0 ;i < threadNum ; i ++) {
			DataThread<T> dataThread = new DataThread<T>(data, this);
			dataThread.start();
		}
	}
	
	public static void main(String[] args) {
		List<String> list = new ArrayList<String>();
		for ( int i = 0 ;i < 10000; i ++) {
			list.add("" + new Random().nextInt());
		}
		/*list.add("1");
		list.add("2");
		list.add("6");
		list.add("3");
		list.add("4");
		list.add("5");
		list.add("555");
		list.add("12");
		list.add("31");
		list.add("60");
		list.add("36");
		list.add("30");*/
		
		DataService<String> ds = new DataService<String>(200, 26, list);
		ds.start();
//		try {
//			Thread.sleep(1000 * 1);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		ds.waitThis();
		System.out.println("end............");
	}

}
