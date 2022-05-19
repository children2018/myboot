package com.lvf.springboot.activemq2;

import javax.jms.Queue;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsMessagingTemplate;

/**
 * 适用于多个activemq的情况
 * @author Administrator
 *
 */
@Configuration
public class MqUpConfig {
	
	@Value("${spring.activemq.user}")
	private String user;
	
	@Value("${spring.activemq.password}")
	private String password;
	
	@Value("${spring.activemq.brokerUrl}")
	private String brokerUrl;
	
	@Bean
	public ActiveMQConnectionFactory connectionFactory () {
		return new ActiveMQConnectionFactory(user, password, brokerUrl);
	}
	
	@Bean 
	public JmsListenerContainerFactory<?> jmsListenerContainerQueue(ActiveMQConnectionFactory connectionFactory) {
		DefaultJmsListenerContainerFactory bean = new DefaultJmsListenerContainerFactory();
		bean.setConnectionFactory(connectionFactory);
		return bean;
	}
	
	@Bean 
	public JmsListenerContainerFactory<?> jmsListenerContainerTopic(ActiveMQConnectionFactory connectionFactory) {
		DefaultJmsListenerContainerFactory bean = new DefaultJmsListenerContainerFactory();
		bean.setPubSubDomain(true);
		bean.setConnectionFactory(connectionFactory);
		return bean;
	}
	
	@Bean
	public JmsMessagingTemplate jmsAbcTemplate() {
	  JmsMessagingTemplate jmsMessagingTemplate = new JmsMessagingTemplate();
	  jmsMessagingTemplate.setConnectionFactory(connectionFactory());
	  //jmsMessagingTemplate.setDefaultDestinationName("localMq");
      return jmsMessagingTemplate;
	}
	
	@Bean
	public JmsMessagingTemplate jmsAccTemplate() {
	  JmsMessagingTemplate jmsMessagingTemplate = new JmsMessagingTemplate();
	  jmsMessagingTemplate.setConnectionFactory(connectionFactory());
	  //jmsMessagingTemplate.setDefaultDestinationName("localMq");
      return jmsMessagingTemplate;
	}
	
	@Bean
	public Topic upTopic() {
		return new ActiveMQTopic("sms.up.topic");
	}
	
	@Bean
	public Queue upQueue() {
		return new ActiveMQQueue("sms.up.queue");
	}

}
