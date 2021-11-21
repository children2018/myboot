package com.lvf.springboot.controller;

import java.util.concurrent.CountDownLatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lvf.springboot.opentsdb.OpentsdbTest;

@RequestMapping("opentsdb")
@Controller
public class OpentsdbController {

	RestTemplate restTemplate = new RestTemplate();

	@RequestMapping("/hello")
	public void hello() {
		
		String url = "http://47.240.169.234:4242/api/put?summary";
		JSONObject data = new JSONObject();
		data.put("metric", "sys.cpu.nice");
		data.put("timestamp", 1640880000001L);
		data.put("value", 13.3);
		
		JSONObject tags = new JSONObject();
		tags.put("host", "web01");
		tags.put("dc", "lga");
		data.put("tags", tags);
		
		System.out.println("hello:request:data:" + data.toJSONString());
		String resultStr = null;
		try {
			resultStr = restTemplate.postForObject(url, data, String.class);
		} catch (Exception e) {
			System.out.println("hello:response:error:resultStr:" + resultStr);
			System.out.println("hello:response:error:" + e.getMessage());
		}
		System.out.println("hello:request:resultStr:" + resultStr);
	}
	
	@RequestMapping("/testMultiRequest")
	public void testMultiRequest() {
		
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
