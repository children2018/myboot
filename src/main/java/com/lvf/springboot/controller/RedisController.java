package com.lvf.springboot.controller;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhangfanghao
 * @version 1.0
 * @since 2020/10/26 16:53
 */
@RestController
@RequestMapping("redis")
public class RedisController {

    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping("msg")
    public String publishMsg(String msg, String channel) {
        if (StringUtils.isEmpty(msg) || StringUtils.isEmpty(channel)) {
            return "false";
        }
        System.out.println("发布消息【" + msg + "】至 channel: " + channel);
        redisTemplate.convertAndSend(channel, msg);
        return "true";
    }
    
    @GetMapping("msg2")
    public String publishMsg2() {
        redisTemplate.opsForValue().set("testtoday", "success2");
        return "true";
    }    
    
    @GetMapping("msg3")
    public String publishMsg3() {
    	String key = "msg3";
        Long res = redisTemplate.getConnectionFactory().getConnection().incr(key.getBytes());
        if (res == 1) {
        	redisTemplate.expire(key, 1, TimeUnit.DAYS);
        }
        System.out.println("i get: " + res);
        return "true";
    }
    
    @GetMapping("msg4")
    public String getMsg4() {
        String result = (String) redisTemplate.opsForValue().get("testtoday");
        return result;
    }   
    
    
}
