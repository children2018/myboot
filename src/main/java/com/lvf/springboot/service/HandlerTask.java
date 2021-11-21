package com.lvf.springboot.service;

import java.util.List;
import java.util.concurrent.RecursiveTask;

import com.lvf.springboot.mapper.UserMapper;
import com.lvf.springboot.model.User;

public class HandlerTask extends RecursiveTask<String> {

	private static final long serialVersionUID = 5471983847321729125L;
	
	private int HANDLER_NUMB;
	
	List<User> userList;
	
	private UserMapper userMapper;
	
	private int startIndex;
	
	private int endIndex;
	
	public HandlerTask(UserMapper userMapper, List<User> userList, int startIndex, int endIndex, int HANDLER_NUMB) {
		this.userList = userList;
		this.userMapper = userMapper;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		this.HANDLER_NUMB = HANDLER_NUMB;
	}

	@Override
	protected String compute() {
		
		if (endIndex - startIndex + 1 <= this.HANDLER_NUMB) {
			System.out.println("startIndex:" + startIndex + "\t endIndex:" + endIndex);
			userMapper.insertList(userList.subList(startIndex, endIndex));
		} else {
			int middle = (startIndex + endIndex) / 2;
			HandlerTask left = new HandlerTask(this.userMapper, userList, startIndex, middle, this.HANDLER_NUMB);
			HandlerTask right = new HandlerTask(this.userMapper, userList, middle + 1, endIndex, this.HANDLER_NUMB);
			left.fork();
			right.fork();
			
			left.join();
			right.join();
		}
		
		return "ok";
	}

}
