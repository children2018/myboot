package com.lvf.springboot.singlelist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lvf.springboot.mapper.UserMapper;
import com.lvf.springboot.model.User;

@Component
public class SingleToList extends Thread {

	private BlockingQueue<User> queue = new LinkedBlockingQueue<User>();
	private int maxSize = 2000;
	private int count = 0;
	private User[] array = null;
	private long[] timeoutArray = {1, 10, 100, 1000, 1000 * 60, 1000 * 60 * 60};
	private int timeoutIndex = 0;
	
	private StringBuffer sbr = new StringBuffer();
	
	@Autowired
	private UserMapper userMapper;
	
	public void setUserMapper(UserMapper userMapper) {
		this.userMapper = userMapper;
	}
	
	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}
	
	public SingleToList() {
		this.start();
	}
	
	@Override
	public void run() {
		array = new User[maxSize];
		try {
			while (true) {
				long start = System.currentTimeMillis();
				User takeStr = queue.poll(timeoutArray[timeoutIndex], TimeUnit.MILLISECONDS);
				long end = System.currentTimeMillis();
				long cost  = end - start;

				if (takeStr != null) {
					array[count++] = takeStr;
					if (count >= maxSize) {
						List<User> list = new ArrayList<User>(Arrays.asList(array));
						list.removeIf(c -> c == null);
						if (list.size() > 0) {
							this.save(list);
							this.clear();
						}
					}
				} else if (takeStr == null ){
					if (count > 0) {
						List<User> list = new ArrayList<User>(Arrays.asList(array));
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
					
					if (cost >= 1000) {
						System.out.println("===========================sbr:\n" + sbr.toString() + "===========================");
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
	
	public void save(List<User> list) {
		System.out.println("list.size():" + list.size());
		sbr.append("list.size():").append(list.size()).append("\n");
		userMapper.insertList(list);
	}
	
	public void add(User val) {
		try {
			queue.put(val);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
