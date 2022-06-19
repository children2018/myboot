package com.lvf.springboot.mutilthread3;

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
	private boolean over = false;
	private Lock lock = new ReentrantLock();
	private Lock lock2 = new ReentrantLock();
	private Condition condition = lock.newCondition();
	
	public DataService(int size, int threadNum, List<T> list) {
		this.size = size;
		this.list = list;
		this.threadNum = threadNum;
	}
	
	private boolean isOver(int state) {
		if (state == 1) {
			over = true;
		} 
		return over;
	}
	
	@Override
	public void run() {
		System.out.println(System.currentTimeMillis() + " run cd");
		try {
			this.process();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void doneNum(String uuid) {
		lock2.lock();
			System.out.println(System.currentTimeMillis() + " uuid:" + uuid + "获得了资源");
			if (++doneNum >= threadNum){
				lock.lock();
				System.out.println(System.currentTimeMillis() + "game over...doneNum:" + doneNum);
				this.isOver(1);
				condition.signal();
				lock.unlock();
			} 
			System.out.println(System.currentTimeMillis() + " uuid:" + uuid + "释放了资源");
		lock2.unlock();
	}
	
	public void waitThis() {
		try {
			lock.lock();
				System.out.println(System.currentTimeMillis() + " waitThis获得了资源");
				if (!isOver(0)) {
					System.out.println(System.currentTimeMillis() + " waitThis我等待。。。");
					condition.await();
				}
				System.out.println(System.currentTimeMillis() + " waitThis结束。。。");
			lock.unlock();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void process(){
		Data<T> data = new Data<T>(size, list);
		for (int i = 0 ;i < threadNum ; i ++) {
			DataThread<T> dataThread = new DataThread<T>(data, this);
			dataThread.start();
		}
		try {
			Thread.sleep(1000 * 10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		List<String> list = new ArrayList<String>();
		for ( int i = 0 ;i < 30; i ++) {
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
		list.add("30");
		*/
		
		DataService<String> ds = new DataService<String>(1, 26, list);
		ds.start();
//		try {
//			Thread.sleep(1000 * 6);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		ds.waitThis();
		System.out.println(System.currentTimeMillis() + "end............");
	}

}
