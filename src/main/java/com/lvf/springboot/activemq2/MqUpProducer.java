package com.lvf.springboot.activemq2;

import javax.jms.Queue;
import javax.jms.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class MqUpProducer {
	
	@Autowired
	private JmsMessagingTemplate jmsAbcTemplate;
	
	@Autowired
	private JmsMessagingTemplate jmsAccTemplate;
	
	@Autowired
	private Topic upTopic;
	
	@Autowired
	private Queue upQueue;
	
	public void sendTopic(String msg) {
		System.out.println("生产者发送（主题）：" + msg);
		jmsAbcTemplate.convertAndSend(this.upTopic, msg);
	}
	
	public void sendQueue(String msg) {
		System.out.println("生产者发送（点对点）：" + msg);
		jmsAccTemplate.convertAndSend(this.upQueue, msg);
	}


}
