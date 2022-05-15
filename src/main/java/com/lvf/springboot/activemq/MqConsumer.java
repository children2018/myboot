package com.lvf.springboot.activemq;

import java.util.Collection;

import javax.jms.JMSException;
import javax.jms.TextMessage;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.lvf.springboot.websocket.MyWebSocketServer;
import com.lvf.springboot.websocket.WebSocketMapUtil;

@Component
public class MqConsumer {
	
	@JmsListener(destination="sms.test.topic")
	public void receive(TextMessage msg) throws JMSException {
		System.out.println("消费者接收：" + msg.getText());
		
		Collection<MyWebSocketServer> col = WebSocketMapUtil.getValues();
		for (MyWebSocketServer wss : col) {
			try {
				wss.sendMessage(1, "success", msg.getText());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
