package com.lvf.springboot.activemq;

import javax.jms.Topic;

import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class MqConfig {
	
	@Bean
	public Topic topic () {
		return new ActiveMQTopic("sms.test.topic");
	}
	
	@Bean
	public Topic topic2 () {
		return new ActiveMQTopic("sms.test.topic2");
	}

}
