package com.lvf.springboot.websocket.client;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

@ClientEndpoint
public class SocketClient {
	
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