package com.lvf.springboot.controller;

import java.util.concurrent.CountDownLatch;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lvf.springboot.callable.Kabc;
import com.lvf.springboot.opentsdb.OpentsdbTest;


@RequestMapping("/hello")
@Controller
public class HelloController {

    @RequestMapping("/hello")
    public String hello(@RequestParam(value="username", required=false, defaultValue="helloworld") String username, Model model) {
        model.addAttribute("username", username);
        return "hello";
    }
    
	@Autowired
	Kabc kabc;
	
	@GetMapping("abc")
	public void abc(HttpServletRequest req) {
		System.out.println("fds");
		System.out.println("kabc.getUrl():" + kabc.getUrl());
	}
	
	
	@ResponseBody
	@GetMapping("/test")
	public Kabc test() {
		System.out.println("dd");
		Kabc kabc = new Kabc();
		kabc.setUrl("OK");
		return kabc;
	}
	//http://192.168.1.4:8082/hello/test2
	@ResponseBody
	@GetMapping("/test2")
	public void test2() {
		int max = 10000;
		long start = System.currentTimeMillis();
		CountDownLatch cdl = new CountDownLatch(max);
		CountDownLatch cd2 = new CountDownLatch(max);
//		CountDownLatch cd3 = new CountDownLatch(max);
		OpentsdbTest openTest = new OpentsdbTest();
		for (int index = 1 ; index <= max ; index ++) {
			
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					openTest.testLocalhostSpringBoot2();
					cdl.countDown();
				}
			}).start();
			
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					openTest.testLocalhostSpringBoot3();
					cd2.countDown();
				}
			}).start();
			
			/*new Thread(new Runnable() {
				
				@Override
				public void run() {
					openTest.testLocalhostSpringBoot5();
					cd3.countDown();
				}
			}).start();*/
		}
		try {
			cdl.await();
			cd2.await();
//			cd3.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		System.out.println("it's done");
		System.out.println("cost.mis:" + (end - start));
		System.out.println("cost.sec:" + (end - start)/1000);
		System.out.println("1yes:" + openTest.yes.intValue());
		System.out.println("1no:" + openTest.no.intValue());
		System.out.println("2yes:" + openTest.yes2.intValue());
		System.out.println("2no:" + openTest.no2.intValue());
//		System.out.println("3yes:" + openTest.yes3.intValue());
//		System.out.println("3no:" + openTest.no3.intValue());
	}
	//http://192.168.1.4:8082/hello/test3
	@ResponseBody
	@GetMapping("/test3")
	public void test3() {
		int max = 10000;
		long start = System.currentTimeMillis();
		CountDownLatch cdl = new CountDownLatch(max);
		OpentsdbTest openTest = new OpentsdbTest();
		for (int index = 1 ; index <= max ; index ++) {
			
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					openTest.testLocalhostSpringBoot2();
					cdl.countDown();
				}
			}).start();
			
//			new Thread(new Runnable() {
//				
//				@Override
//				public void run() {
//					openTest.testLocalhostSpringBoot3();
//					cd2.countDown();
//				}
//			}).start();
			
			/*new Thread(new Runnable() {
				
				@Override
				public void run() {
					openTest.testLocalhostSpringBoot5();
					cd3.countDown();
				}
			}).start();*/
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
		System.out.println("1yes:" + openTest.yes.intValue());
		System.out.println("1no:" + openTest.no.intValue());
	}
	
	//http://192.168.1.4:8082/hello/test5
	@ResponseBody
	@GetMapping("/test5")
	public void test5() {
		int max = 10000;
		long start = System.currentTimeMillis();
		CountDownLatch cdl = new CountDownLatch(max);
		CountDownLatch cd2 = new CountDownLatch(max);
		CountDownLatch cd3 = new CountDownLatch(max);
		OpentsdbTest openTest = new OpentsdbTest();
		for (int index = 1 ; index <= max ; index ++) {
			
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					openTest.testLocalhostSpringBoot2();
					cdl.countDown();
				}
			}).start();
			
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					openTest.testLocalhostSpringBoot3();
					cd2.countDown();
				}
			}).start();
			
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					openTest.testLocalhostSpringBoot5();
					cd3.countDown();
				}
			}).start();
		}
		try {
			cdl.await();
			cd2.await();
			cd3.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		System.out.println("it's done");
		System.out.println("cost.mis:" + (end - start));
		System.out.println("cost.sec:" + (end - start)/1000);
		System.out.println("1yes:" + openTest.yes.intValue());
		System.out.println("1no:" + openTest.no.intValue());
		System.out.println("2yes:" + openTest.yes2.intValue());
		System.out.println("2no:" + openTest.no2.intValue());
		System.out.println("3yes:" + openTest.yes3.intValue());
		System.out.println("3no:" + openTest.no3.intValue());
	}
    
}
