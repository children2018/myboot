package com.lvf.springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lvf.springboot.activemq.MqProducer;
import com.lvf.springboot.callable.Kabc;

@Controller
public class ProducerController {
	
	@Autowired
	private MqProducer mqProducer;
	
	@ResponseBody
	@GetMapping("/send")
	public Kabc send(String msg) {
		mqProducer.send(msg);
		Kabc kabc = new Kabc();
		kabc.setUrl("OK");
		return kabc;
	}
	
	@ResponseBody
	@GetMapping("/send2")
	public Kabc send2(String msg) {
		mqProducer.send2(msg);
		Kabc kabc = new Kabc();
		kabc.setUrl("OK");
		return kabc;
	}

}
