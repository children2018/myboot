package com.lvf.springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lvf.springboot.callable.Kabc;


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
	public void abc() {
		System.out.println("fds");
		System.out.println("kabc.getUrl():" + kabc.getUrl());
	}
    
}
