package com.lvf.springboot.singlelist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public abstract class SingleToList<T> extends Thread {

	private BlockingQueue<T> queue = new LinkedBlockingQueue<T>();
	private int maxSize = 3000;
	private int count = 0;
	private T[] array = null;
	private long[] timeoutArray = {1, 10, 100, 1000, 1000 * 60, 1000 * 60 * 60};
	private int timeoutIndex = 0;
	
	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}
	
	@Override
	public void run() {
		array = (T[]) new Object[maxSize];
		try {
			while (true) {
				long start = System.currentTimeMillis();
				T takeStr = queue.poll(timeoutArray[timeoutIndex], TimeUnit.MILLISECONDS);
				long end = System.currentTimeMillis();
				long cost  = end - start;

				if (takeStr != null) {
					array[count++] = takeStr;
					if (count >= maxSize) {
						List<T> list = new ArrayList<T>(Arrays.asList(array));
						list.removeIf(c -> c == null);
						if (list.size() > 0) {
							this.save(list);
							this.clear();
						}
					}
				} else if (takeStr == null ){
					if (count > 0) {
						List<T> list = new ArrayList<T>(Arrays.asList(array));
						list.removeIf(c -> c == null);
						if (list.size() > 0) {
							this.save(list);
							this.clear();
						}
					}
				}
				
				if (takeStr == null && cost >= timeoutArray[timeoutIndex]) {
					if (timeoutIndex + 1 < timeoutArray.length) {
						timeoutIndex += 1;
						System.out.println("..........." + timeoutArray[timeoutIndex]);
					}
				} else if (takeStr != null && cost < timeoutArray[timeoutIndex]) {
					if (timeoutIndex - 1 >= 0) {
						timeoutIndex -= 1;
						System.out.println("..........." + timeoutArray[timeoutIndex]);
					}
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void clear() {
		for (int index = 0; index < array.length; index++) {
			if (array[index] != null) {
				array[index] = null;
			}
		}
		count = 0;
	}
	
	public abstract void save(List<T> list);
	
	public void add(T val) {
		try {
			queue.put(val);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
