package com.lvf.springboot.opentsdb;

import java.util.Random;

import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;

public class OpentsdbTest {
	
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

}
