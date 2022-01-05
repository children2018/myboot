package com.lvf.springboot.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.lvf.springboot.model.User;


@RequestMapping("/mongodb")
@Controller
public class MongodbController {
	
	@Autowired
	private MongoTemplate mongoTemplate;

    @RequestMapping("/hello")
    public void hello() {
        System.out.println("mongoTemplate:" + mongoTemplate);
    }
    
    
    @RequestMapping("/ins")
    public void ins() {
    	User user = new User();
    	user.setId("1");
    	user.setName("tom01");
    	mongoTemplate.insert(user);
        System.out.println("it's done!" );
        System.out.println("findAll:" + JSON.toJSONString(mongoTemplate.findAll(User.class)));
    }
    
    @RequestMapping("/test1")
    public void test1() {
    	
    	long start = System.currentTimeMillis();
    	for (int index = 1; index <= 10000 ;index++) {
    		User user = new User();
    		user.setId("" + index);
    		user.setName("tom" + index);
    		mongoTemplate.insert(user);
    	}
        System.out.println("it's done!" );
        long end = System.currentTimeMillis();
        System.out.println("cost:" + (end - start) );
        //cost:2725  3700
    }
    
    @RequestMapping("/test2")
    public void test2() {
    	
    	long start = System.currentTimeMillis();
    	for (int index = 1; index <= 100000 ;index++) {
    		User user = new User();
    		user.setId("" + index);
    		user.setName("tom" + index);
    		mongoTemplate.insert(user);
    	}
        System.out.println("it's done!" );
        long end = System.currentTimeMillis();
        System.out.println("cost:" + (end - start) );
        //cost:18172  5000
    }
    
    @RequestMapping("/test3")
    public void test3() {
    	
    	long start = System.currentTimeMillis();
    	for (int index = 1; index <= 1000000 ;index++) {
    		User user = new User();
    		user.setId("" + index);
    		user.setName("tom" + index);
    		mongoTemplate.insert(user);
    	}
        System.out.println("it's done!" );
        long end = System.currentTimeMillis();
        System.out.println("cost:" + (end - start) );
        //cost:169705  5880
    }
    
    @RequestMapping("/test6")
    public void test6() {
    	long start = System.currentTimeMillis();
    	int max = 1000000;
    	List<User> userList = new ArrayList<User>();
    	for (int index = 0; index < max ;index++) {
    		User user = new User();
    		user.setId("" + index);
    		user.setName("tom" + index);
    		userList.add(user);
    	}
		mongoTemplate.insertAll(userList);
        System.out.println("it's done!" );
        long end = System.currentTimeMillis();
        System.out.println("cost:" + (end - start) );
        //cost:27267  1850
    }
    
    @RequestMapping("/test5")
    public void test5() {
    	int max = 50000;
    	Semaphore sem = new Semaphore(499);
    	CountDownLatch cdl = new CountDownLatch(max);
    	long start = System.currentTimeMillis();
    	for (int index = 1; index <= max ;index++) {
    		final int i = index;
    		new Thread(new Runnable() {
				public void run() {
					User user = new User();
					user.setId("" + i);
					user.setName("tom" + i);
					try {
						sem.acquire();
						mongoTemplate.insert(user);
					} catch (InterruptedException e) {
						e.printStackTrace();
					} finally {
						sem.release();
					}
					cdl.countDown();
				}
			}).start();
    	}
    	try {
			cdl.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        System.out.println("it's done!" );
        long end = System.currentTimeMillis();
        System.out.println("cost:" + (end - start) );
        //cost:27267  1850
    }
    
}
