package com.lvf.springboot.activemq;

import javax.jms.Topic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class MqProducer {
	
	@Autowired
	private JmsMessagingTemplate jmsMessagingTemplate;
	
	@Autowired
	private Topic topic;
	
	public void send(String msg) {
		System.out.println("生产者发送：" + msg);
		jmsMessagingTemplate.convertAndSend(this.topic, msg);
	}

}
