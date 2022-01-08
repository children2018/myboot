package com.lvf.springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.lvf.springboot.model.User;
import com.lvf.springboot.singlelist.SingleToList;
 
@Controller
@RequestMapping("/singleList")
public class SingleListController {
	
	@Autowired
	private SingleToList singleToList;
	
	@PostMapping("/ok")
	@ResponseBody
	public String ok(@RequestBody User user) {
		System.out.println("singleToList.user:" + JSON.toJSONString(user));
		System.out.println("singleToList.getState():" + singleToList.getState());
		singleToList.add(user);
		return "ok";
	}
	
}
