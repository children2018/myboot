package com.lvf.springboot.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lvf.springboot.model.PageBean;
import com.lvf.springboot.model.RespUtil;
import com.lvf.springboot.model.User;
import com.lvf.springboot.service.RedisCache;
import com.lvf.springboot.service.UserService;

/**
 * Created by zl on 2015/8/27.
 */
@RequestMapping("/user")
@Controller
public class UserController {

	private Logger logger = Logger.getLogger(UserController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private RedisCache jedisUtils;
    
    @RequestMapping("/getUserInfo")
    @ResponseBody
    public List<User> getUserInfo() {
        List<User> user = userService.getUserInfo();
        logger.info("yayaya1:");
        //logger.info("yayaya1:" + JSON.toJSONString(user));
        if(user!=null){
        }
        return user;
    }
    
    @RequestMapping("/getUserPage")
    @ResponseBody
    public RespUtil getUserPage(PageBean pageBean) {
    	PageHelper.startPage(pageBean.getPageNum(), pageBean.getPageSize());
        List<User> userList = userService.getUserInfo();
        PageInfo pageInfo = new PageInfo(userList);
        return RespUtil.success(pageInfo);
    }
    
    @RequestMapping("/insertinfo")
    @ResponseBody
    public User insertinfo() {
    	long begin = System.currentTimeMillis();
    	userService.insertinfo();
    	long end = System.currentTimeMillis();
    	User u = new User();
    	u.setName("cost:" + (end - begin));
    	return u;
    }
    
    @RequestMapping("/insertlist")
    @ResponseBody
    public User insertlist() {
    	long begin = System.currentTimeMillis();
    	userService.insertsList();
    	long end = System.currentTimeMillis();
    	User u = new User();
    	u.setName("cost:" + (end - begin));
    	return u;
    }
    
    @RequestMapping("/insertsListWithForkWithSubregion2")
    @ResponseBody
    public User insertsListWithForkWithSubregion2() {
    	long begin = System.currentTimeMillis();
    	userService.insertsListWithForkWithSubregion2();
    	long end = System.currentTimeMillis();
    	User u = new User();
    	u.setName("cost:" + (end - begin));
    	return u;
    }
    
    @RequestMapping("/insertsListWithForkWithSubregion")
    @ResponseBody
    public User insertsListWithForkWithSubregion() {
    	long begin = System.currentTimeMillis();
    	userService.insertsListWithForkWithSubregion();
    	long end = System.currentTimeMillis();
    	User u = new User();
    	u.setName("cost:" + (end - begin));
    	return u;
    }
    
    @RequestMapping("/insertsListWithFork")
    @ResponseBody
    public User insertsListWithFork() {
    	long begin = System.currentTimeMillis();
    	userService.insertsListWithFork();
    	long end = System.currentTimeMillis();
    	User u = new User();
    	u.setName("cost:" + (end - begin));
    	return u;
    }
    @RequestMapping("/insertInfoWithFork")
    @ResponseBody
    public User insertInfoWithFork() {
    	long begin = System.currentTimeMillis();
    	userService.insertInfoWithFork();
    	long end = System.currentTimeMillis();
    	User u = new User();
    	u.setName("cost:" + (end - begin));
    	return u;
    }
    
    
    
    @RequestMapping("/process")
    @ResponseBody
    public List<User> process(String name) {
    	userService.process();
    	return null;
    }
	@RequestMapping("/testjedis")
	@ResponseBody
	public List<User> testjedis(String name) {
		for (int j = 1; j <= 100; j++) {
			String key = "jack" + UUID.randomUUID();
			List<User> userList = new ArrayList<User>();
			for (int i = 1; i <= 10000; i++) {
				User user = new User();
				user.setId(UUID.randomUUID().toString());
				user.setAge(i);
				user.setName("jack" + i);
				user.setPassword("password" + i);
				userList.add(user);
			}
			jedisUtils.getJedis().set(key, JSON.toJSONString(userList));
		}

		return null;
	}
    
    @RequestMapping("/testjedis100")
    @ResponseBody
    public List<User> testjedis100(String name) {
    	String key = "select_random_list_".concat(name);
    	List<User> userList = new ArrayList<User>();
    	for (int i=1; i<= 10000;i++) {
    		User user = new User();
    		user.setId(UUID.randomUUID().toString());
    		user.setAge(i);
    		user.setName("jack" + i);
    		user.setPassword("password" + i);
    		userList.add(user);
    	}
    	jedisUtils.getJedis().set(key, JSON.toJSONString(userList));
    	jedisUtils.getJedis().expire(key, 60 * 10);
    	
    	String uList = jedisUtils.getJedis().get(key);
    	List<User> userList2 = JSONArray.parseArray(uList, User.class);
    	System.out.println(userList2.get(2).getPassword());
    	long start = System.currentTimeMillis();
    	String a = JSON.toJSONString(userList);
    	long end = System.currentTimeMillis();
    	System.out.println("cost:" + (end - start));
    	
    	jedisUtils.getJedis().set(key, JSON.toJSONString(userList));
    	jedisUtils.getJedis().expire(key, 60 * 10);
    	
    	return userList2;
    }
    
}
