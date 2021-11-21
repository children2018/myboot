package com.lvf.springboot.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lvf.springboot.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zl on 2015/8/27.
 */
@Api(tags = "用户相关模块")
@RestController
@RequestMapping(value = "/users")
public class SwaggerController {

	/*
	 * http://localhost:8080/swagger-ui.html
	 */
	@ApiOperation(value = "获取所有的用户", notes = "requires noting")
	@PostMapping(value = "/getUsers")
	public List<User> getUsers() {
		List<User> list = new ArrayList<User>();

		User user = new User();
		user.setName("hello");
		list.add(user);

		User user2 = new User();
		user.setName("world");
		list.add(user2);
		return list;
	}

	@ApiOperation(value = "通过用户ID获取信息", notes = "返回用户的信息")
	@RequestMapping(value = "/{name}", method = RequestMethod.GET)
	public User getUserById(@PathVariable String name) {
		User user = new User();
		user.setName("hello world");
		return user;
	}

}
