package com.lvf.springboot.activemq;

import javax.jms.JMSException;
import javax.jms.TextMessage;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class MqConsumer {
	
	@JmsListener(destination="sms.test.topic")
	public void receive(TextMessage msg) throws JMSException {
		System.out.println("消费者接收：" + msg.getText());
	}

}
