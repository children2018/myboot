package com.lvf.springboot.websocket.server;

import java.util.Collection;

import org.springframework.stereotype.Component;

@Component
public class WebSocketService {
	
	public void toAllWebSocket(String str) {
		Collection<MyWebSocketServer> col = WebSocketMapUtil.getValues();
		for (MyWebSocketServer wss : col) {
			try {
				wss.sendMessage(1, "success", str);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
