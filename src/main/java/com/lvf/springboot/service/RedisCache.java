package com.lvf.springboot.service;

import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;

@Component
public class RedisCache {
	
	private Jedis jedis;
	
	public RedisCache() {
		jedis = new Jedis("127.0.0.1", 6379);
	}
	
	public Jedis getJedis() {
		return jedis;
	}

}
