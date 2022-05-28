package com.lvf.springboot.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.websocket.ContainerProvider;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lvf.springboot.activemq.MqProducer;
import com.lvf.springboot.activemq2.MqUpProducer;
import com.lvf.springboot.callable.Kabc;
import com.lvf.springboot.websocket.client.SocketClient;

@Controller
public class ProducerController {
	
	@Autowired
	private MqProducer mqProducer;
	
	private List<Session> sessionList = new ArrayList<Session>();
	
	/*@ResponseBody
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
	}*/
	
	@Autowired
	private MqUpProducer mqUpProducer;
	
	@ResponseBody
	@GetMapping("/senduptopic")
	public Kabc senduptopic(String msg) {
		mqUpProducer.sendTopic(msg);
		Kabc kabc = new Kabc();
		kabc.setUrl("OK");
		return kabc;
	}
	
	@ResponseBody
	@GetMapping("/sendupqueue")
	public Kabc sendupqueue(String msg) {
		mqUpProducer.sendQueue(msg);
		Kabc kabc = new Kabc();
		kabc.setUrl("OK");
		return kabc;
	}
	
	@ResponseBody
	@GetMapping("/testWebsocketCluster")
	public Kabc testWebsocketCluster() {
		
		System.out.println("cd testWebsocketCluster");
		
		for (int i = 0;i < 10000; i++) {
			String str = "ws://192.168.1.2:8083/websocket?idx=sparta" + i;
			
			URI path = URI.create(str);
	        WebSocketContainer webSocketContainer = ContainerProvider.getWebSocketContainer();
	
	        Session session = null;
	        SocketClient socketClient = new SocketClient();
	        try {
	            session = webSocketContainer.connectToServer(socketClient, path);
	            sessionList.add(session);
	        }catch (Exception e) {
	            System.out.println("sendMsg error:{}" + e.getMessage());
	        }
	        
        
		}
		
		System.out.println("sessionList.size:" + sessionList.size());
		
		new Thread(new Runnable() {
			public void run() {
				while (true) {
					for (int j = 0;j < 10000; j++) {
						sessionList.get(j).getAsyncRemote().sendText("msg&*#&$*#*$testWebsocketCluster:" + UUID.randomUUID().toString());
					}
					try {
						Thread.sleep(1000 * 6);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
		
		Kabc kabc = new Kabc();
		kabc.setUrl("OK");
		return kabc;
	}

}
