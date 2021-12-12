package com.lvf.springboot.opentsdb;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;

public class OpentsdbTest {
	
	public volatile AtomicInteger yes = new AtomicInteger();
	public volatile AtomicInteger no = new AtomicInteger();
	public volatile AtomicInteger yes2 = new AtomicInteger();
	public volatile AtomicInteger no2 = new AtomicInteger();
	public volatile AtomicInteger yes3 = new AtomicInteger();
	public volatile AtomicInteger no3 = new AtomicInteger();
	
	public void test() {
		RestTemplate restTemplate = new RestTemplate();
		String url = "http://47.240.169.234:4242/api/put?summary";
		JSONObject data = new JSONObject();
		data.put("metric", "sys.cpu.nice");
		data.put("timestamp", new Long(1546272000000L + new Long(new Random().nextInt())));
		data.put("value", new Random().nextInt(19) + 1);
		
		JSONObject tags = new JSONObject();
		tags.put("host", "web01");
		tags.put("dc", "lga");
		data.put("tags", tags);
		
		String resultStr = null;
		try {
			resultStr = restTemplate.postForObject(url, data, String.class);
		} catch (Exception e) {
			System.out.println("hello:response:error:resultStr:" + resultStr);
			System.out.println("hello:response:error:" + e.getMessage());
		}
	}
	
	public void testLocalhostSpringBoot2() {
		RestTemplate restTemplate = new RestTemplate();
		String url = "http://192.168.1.5:8080/hello/test";
		String resultStr = null;
		JSONObject data = new JSONObject();
		try {
			resultStr = restTemplate.getForObject(url, String.class, data);
			yes.incrementAndGet();
		} catch (Exception e) {
			no.incrementAndGet();
			System.out.println("hello:response:error:resultStr:" + resultStr);
			System.out.println("hello:response:error:" + e.getMessage());
		}
	}
	
	public void testLocalhostSpringBoot3() {
		RestTemplate restTemplate = new RestTemplate();
		String url = "http://192.168.1.5:8081/hello/test";
		String resultStr = null;
		JSONObject data = new JSONObject();
		try {
			resultStr = restTemplate.getForObject(url, String.class, data);
			yes2.incrementAndGet();
		} catch (Exception e) {
			no2.incrementAndGet();
			System.out.println("hello:response:error:resultStr:" + resultStr);
			System.out.println("hello:response:error:" + e.getMessage());
		}
	}
	
	public void testLocalhostSpringBoot5() {
		RestTemplate restTemplate = new RestTemplate();
		String url = "http://192.168.1.5:8082/hello/test";
		String resultStr = null;
		JSONObject data = new JSONObject();
		try {
			resultStr = restTemplate.getForObject(url, String.class, data);
			yes3.incrementAndGet();
		} catch (Exception e) {
			no3.incrementAndGet();
			System.out.println("hello:response:error:resultStr:" + resultStr);
			System.out.println("hello:response:error:" + e.getMessage());
		}
	}

}
