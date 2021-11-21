package com.lvf.springboot.controller;
import java.util.Date;
import java.util.Hashtable;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lvf.springboot.mapper.UserMapper;
import com.lvf.springboot.model.User;
 
@Controller
public class TestController {
	
	@Autowired
	private UserMapper userMapper;
 
	@RequestMapping("/demo")
	@ResponseBody
	public String demo() {
		return "Hello World!";
	}
	
	@GetMapping("/ok")
	@ResponseBody
	public User ok() {
		User user = new User();
		user.setId(UUID.randomUUID().toString());
		user.setName("testjack");
		user.setPassword("DSKLFKLDSJFLDSFDSFD");
		user.setAge(18);
		userMapper.insert(user);
		return user;
	}
	
	@GetMapping("/ok2")
	@ResponseBody
	public User ok2() {
		return userMapper.queryUserInfoById("3001e2573971-1535-44e8-a221-a4f12b40568e");
	}
	
	@GetMapping("/supervene")
	@ResponseBody
	public User supervene() {
		AtomicInteger zero = new AtomicInteger();
		AtomicInteger one = new AtomicInteger();
		User user = new User();
		user.setId("1d8bf18d-9dcf-4d6d-b62e-ec740d4b2297");
		user.setName("testjack");
		user.setPassword("DSKLFKLDSJFLDSFDSFD");
		user.setAge(19);
		user.setRevision("1");
		CountDownLatch cdl = new CountDownLatch(1000);
		CountDownLatch ready = new CountDownLatch(1);
		Long start = new Date(). getTime();
		ExecutorService execute = Executors.newFixedThreadPool(1000);
		for (int index =1 ;index <= 1000;index++) {
			execute.submit(new Thread() {
				@Override
				public void run() {
					try {
						ready.await();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					Long startssss = new Date().getTime();
					int result = userMapper.updateUser(user);
					System.out.println("" + startssss +"\t" + new Date().getTime() + "\t-----------------------result:" + result);
					if (result == 0) {
						zero.incrementAndGet();
					} else if (result == 1) {
						one.incrementAndGet();
					}
					cdl.countDown();
				}
			});
		}
		try {
			System.out.println("countDown.....");
			ready.countDown();
			cdl.await();
			execute.shutdown();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Long end = new Date(). getTime();
		System.out.println("0.value:" + zero.get());
		System.out.println("1.value:" + one.get());
		System.out.println("time cost:" + (end-start));
		return user;
	}
	
}
