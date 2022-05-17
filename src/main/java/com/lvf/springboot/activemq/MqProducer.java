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
	
	@Autowired
	private Topic topic2;
	
	public void send(String msg) {
		System.out.println("生产者发送：" + msg);
		jmsMessagingTemplate.convertAndSend(this.topic, msg);
	}
	
	public void send2(String msg) {
		System.out.println("生产者发送2：" + msg);
		jmsMessagingTemplate.convertAndSend(this.topic2, msg);
	}


}
