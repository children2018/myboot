package com.lvf.springboot.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Semaphore;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lvf.springboot.mapper.UserMapper;
import com.lvf.springboot.model.Location;
import com.lvf.springboot.model.User;
import com.lvf.springboot.opentsdb.OpentsdbTest;

/**
 * Created by zl on 2015/8/27.
 */

@Service
public class UserService {

	private Logger logger = Logger.getLogger(UserService.class);

	@Autowired
	private UserMapper userMapper;

	public List<User> getUserInfo() {
		List<User> user = userMapper.findUserInfo();
		// User user=null;
		return user;
	}

	public void insertinfo() {
		double s = Math.random();
		User user = new User();
		user.setId(UUID.randomUUID().toString());
		user.setAge((int) (s % 100000));
		user.setName("jack" + s);
		user.setPassword("password" + s);
		logger.info("insert.user:" + JSON.toJSONString(user));
		userMapper.insert(user);
	}

	public void inserts() {
		for (int i = 1; i <= 100000; i++) {
			User user = new User();
			user.setId(UUID.randomUUID().toString());
			user.setAge(i);
			user.setName("jack" + i);
			user.setPassword("password" + i);
			userMapper.insert(user);
		}
	}

	/**
	 * lambda一些灵活代码的运用
	 */
	public void locationPre() {
		List<Location> list = new ArrayList<Location>();
		List<String> areaList = list.stream().map(o -> o.getProvince()).distinct().collect(Collectors.toList());
		areaList.forEach(area -> {
			Function<Location, String> areaFunc = func1 -> {
				return func1.getCity();
			};
			Predicate<Location> areaPredicate = o -> o.getArea().equals(area);
			this.searchList(list, areaPredicate, areaFunc);
		});

		List<String> cityList = list.stream().map(o -> o.getCity()).distinct().collect(Collectors.toList());
		cityList.forEach(city -> {
			Function<Location, String> cityFunc = func1 -> {
				return func1.getCity();
			};
			Predicate<Location> cityPredicate = o -> o.getProvince().equals(city);
			this.searchList(list, cityPredicate, cityFunc);
		});

		List<String> provinceList = list.stream().map(o -> o.getProvince()).distinct().collect(Collectors.toList());
		provinceList.forEach(province -> {
			Function<Location, String> provinceFunc = func1 -> {
				return func1.getCity();
			};
			Predicate<Location> provincePredicate = o -> o.getProvince().equals(province);
			this.searchList(list, provincePredicate, provinceFunc);
		});
	}

	private List<Location> searchList(List<Location> list, Predicate<Location> predicate,
			Function<Location, String> func) {
		list.stream().map(func).collect(Collectors.toList());
		return list.stream().filter(predicate).collect(Collectors.toList());
	}

	public void insertsList() {
		List<User> userList = new ArrayList<User>();
		for (int i = 1; i <= 1000000; i++) {
			User user = new User();
			user.setId(UUID.randomUUID().toString());
			user.setAge(i);
			user.setName("jack" + i);
			user.setPassword("password" + i);
			userList.add(user);
			if (userList.size() % 5000 == 0) {
				userMapper.insertList(userList);
				userList.clear();
			}
		}

		if (userList.size() > 0) {
			userMapper.insertList(userList);
			userList.clear();
		}

	}

	public void process() {
		while (true) {
		}
	}
	
	public void insertInfoWithForkOnlyOne() {
		int i = new Random().nextInt(20);
		User user = new User();
		user.setId(UUID.randomUUID().toString());
		user.setAge(i);
		user.setName("jack" + i);
		user.setPassword("password" + i);
		userMapper.insert(user);
	}

	public void insertInfoWithFork() {
		long start = System.currentTimeMillis();
		List<User> userList = new ArrayList<User>();
		for (int i = 1; i <= 1000000; i++) {
			User user = new User();
			user.setId(UUID.randomUUID().toString());
			user.setAge(i);
			user.setName("jack" + i);
			user.setPassword("password" + i);
			userList.add(user);
		}

		HandlerTask ht = new HandlerTask(userMapper, userList, 0, userList.size(), 5000);
		ForkJoinPool fork = new ForkJoinPool(Runtime.getRuntime().availableProcessors() * 2);
		fork.invoke(ht);
		// fork.shutdown();
		while (true) {
			if (fork.getActiveThreadCount() == 0) {
				try {
					Thread.sleep(200);
					break;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		long end = System.currentTimeMillis();
		System.out.println("cost:" + (end - start));
	}

	public void insertsListWithFork() {
		List<User> userList = new ArrayList<User>();
		int s = new Random().nextInt() % 4;
		for (int i = 1; i <= 100000000; i++) {
			User user = new User();
			user.setId(UUID.randomUUID().toString());
			user.setAge(i);
			user.setName("jack" + i);
			user.setPassword("password" + i);
			user.setArea("1");
			userList.add(user);
		}

		HandlerTask ht = new HandlerTask(userMapper, userList, 0, userList.size(), 3000);
		ForkJoinPool fork = new ForkJoinPool(Runtime.getRuntime().availableProcessors() * 2);
		fork.invoke(ht);
		// fork.shutdown();
	}
	
	public void insertsListWithSemaphore() {
		
		long start = System.currentTimeMillis();
		
		Semaphore spe = new Semaphore(100);
		int max = 100000;
		
		List<User> userList = new ArrayList<User>();
		for (int i = 1; i <= max; i++) {
			User user = new User();
			user.setId(UUID.randomUUID().toString());
			user.setAge(i);
			user.setName("jack" + i);
			user.setPassword("password" + i);
			userList.add(user);
			if (userList.size() % 3000 == 0 || i == max) {
				final List<User> saveList = userList; 
				new Thread(new Runnable() {
					public void run() {
						try {
							spe.acquire();
							userMapper.insertList(saveList);
							spe.release();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}).start();
				userList = new ArrayList<User>();
			}
		}
		while (true) {
			System.out.println("spe.getQueueLength():" + spe.getQueueLength());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (spe.getQueueLength() == 0) {
				long end = System.currentTimeMillis();
				System.out.println("cost.sss:" + (end - start));
				break;
			}
		}

	}

	/**
	 * 为分区作准备创建数据 花费时间：cost:6894791
	 * 
	 * select count(1) from user; 到 127.0.0.1 的连接于 2019-12-14 13:50:09 关闭 Affected
	 * rows: 0 已找到记录: 1 警告: 0 持续时间 1 query: 00:23:47
	 */
	public void insertsListWithForkWithSubregion() {
		Random r = new Random();
		List<User> userList = new ArrayList<User>();
		for (int i = 1; i <= 100000000; i++) {
			User user = new User();
			user.setId("" + i + UUID.randomUUID().toString());
			user.setAge(i);
			user.setName("jack" + i);
			user.setPassword("password" + i);
			user.setRevision("1");
			user.setArea("" + Math.abs(r.nextInt() % 4));
			userList.add(user);
			if (userList.size() % 30000 == 0) {
				List<User> saveList = new ArrayList<User>();
				saveList.addAll(userList);
				HandlerTask ht = new HandlerTask(userMapper, saveList, 0, saveList.size() - 1, 3000);
				ForkJoinPool fork = new ForkJoinPool(Runtime.getRuntime().availableProcessors() * 2);
				fork.invoke(ht);
				userList.clear();
			}
		}
	}

	public void insertsListWithForkWithSubregion2() {

		class MyRun implements Runnable {
			
		    Date startDate = null;

			UserMapper userMapper;

			Semaphore sema = new Semaphore(Runtime.getRuntime().availableProcessors() * 2);

			List<List<User>> saveList;

			volatile int i = 0;

			public MyRun(UserMapper userMapper, List<List<User>> saveList, Date startDate) {
				this.userMapper = userMapper;
				this.saveList = saveList;
				this.startDate = startDate;
			}

			public synchronized int getIndex() {
				return i++;
			}

			@Override
			public void run() {
				try {
					System.out.println(Thread.currentThread().getName() + " come in ...");
					sema.acquire();
					int index = getIndex();
					System.out.println(Thread.currentThread().getName() + " 拿到的index值：" + index);
					this.userMapper.insertList(saveList.get(index));
					if ((index + 1) == saveList.size()) {
						System.out.println("===========================Runtime.getRuntime().availableProcessors() * 2:" + (Runtime.getRuntime().availableProcessors() * 2));
						System.out.println("===========================cost:" + (new Date().getTime() - startDate.getTime()));
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					sema.release();
				}
				
			}

		}
		;

		
		Date startDate = new Date();
		Random r = new Random();
		List<User> userList = new ArrayList<User>();
		List<List<User>> saveList = new ArrayList<List<User>>();
		for (int i = 1; i <= 1000000; i++) {
			User user = new User();
			user.setId("" + i + UUID.randomUUID().toString());
			user.setAge(i);
			user.setName("jack" + i);
			user.setPassword("password" + i);
			user.setRevision("1");
			user.setArea("" + Math.abs(r.nextInt() % 4));
			userList.add(user);
			if (userList.size() % 3000 == 0 || i == 1000000) {
				saveList.add(userList);
				userList = new ArrayList<User>();
			}
		}

		Date midDate = new Date();
		System.out.println("---------------mid------cost:" + (midDate.getTime() - startDate.getTime()));
		MyRun myRun = new MyRun(userMapper, saveList, startDate);
		System.out.println("saveList.size():" + saveList.size());
		for (int j = 0; j < saveList.size(); j ++) {
			new Thread(myRun).start();
		}
	}
	

	public void hello() {
		
		RestTemplate restTemplate = new RestTemplate();

		long start = System.currentTimeMillis();
		CountDownLatch cdl = new CountDownLatch(100000);
		for (int index = 1 ; index <= 100000 ; index ++) {
			
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					OpentsdbTest openTest = new OpentsdbTest();
					openTest.test();
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
		System.out.println("it's done");
		System.out.println("cost.mis:" + (end - start));
		System.out.println("cost.sec:" + (end - start)/1000);
	}

}
