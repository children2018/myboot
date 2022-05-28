package com.lvf.springboot.websocket.client;

import java.net.URI;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.springframework.stereotype.Component;

//@Component
@ClientEndpoint
public class SocketClient2 {
	
	private Session session = null;
	
	public SocketClient2() {
		URI path = URI.create("ws://localhost:8080/websocket/");
        WebSocketContainer webSocketContainer = ContainerProvider.getWebSocketContainer();

        try {
            session = webSocketContainer.connectToServer(this, path);
            System.out.println("int SocketClient it's done");
        }catch (Exception e) {
            System.err.println("sendMsg error:{}" + e.getMessage());
            System.out.println("sendMsg out:{}" + e.getMessage());
        }
	}

    @OnOpen
    public void onOpen() {
        System.out.println("webSocket on onOpen------");
    }

    @OnMessage
    public void onMessage(String message) {
    	System.out.println("message:{}" + message);
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
    	System.out.println("断线重连:{}" + reason.toString());

    }

    @OnError
    public void onError(Throwable ex) {
    	System.out.println("ex:{}" + ex);
    }

}