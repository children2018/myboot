package com.lvf.springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.lvf.springboot.activemq.MqProducer;

@Controller
public class ProducerController {
	
	@Autowired
	private MqProducer mqProducer;
	
	@GetMapping("/send")
	public void send(String msg) {
		mqProducer.send(msg);
	}

}
