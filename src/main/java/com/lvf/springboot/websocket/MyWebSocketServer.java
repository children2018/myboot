package com.lvf.springboot.websocket;

import java.io.IOException;
import java.util.List;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSONObject;
import com.lvf.aop.SpringContextHolder;
import com.lvf.springboot.activemq.MqProducer;
import com.lvf.springboot.utils.ApplicationContextUtil;

@Component
@ServerEndpoint(value = "/websocket")
public class MyWebSocketServer {

	private Logger logger = Logger.getLogger(MyWebSocketServer.class);
	private Session session;
	
	/**
	 * 连接建立后触发的方法
	 * @throws IOException 
	 */
	@OnOpen
	public void onOpen(Session session) throws IOException {
		this.session = session;
		String idx = this.getIdx(session);
		System.out.println("onOpen idx:" + idx);
		if (idx == null) {
			System.out.println("非法连接");
			return ;
		}
		WebSocketMapUtil.put(idx, this);
	}

	/**
	 * 连接关闭后触发的方法
	 * @throws IOException 
	 */
	@OnClose
	public void onClose() throws IOException {
		String idx = this.getIdx(session);
		// 从map中删除
		WebSocketMapUtil.remove(idx);
		System.out.println("====== onClose:" + idx + " ======");
	}

	/**
	 * 接收到客户端消息时触发的方法
	 */
	@OnMessage
	public void onMessage(String params, Session session) throws Exception {
		String idx = this.getIdx(session);
		// 获取服务端到客户端的通道
		MyWebSocketServer myWebSocket = WebSocketMapUtil.get(idx);
		System.out.println("收到来自" + idx + "的消息" + params);
		String result = "收到来自" + idx + "的消息" + params;
		
		//推送给activemq
		MqProducer mqProducer = ApplicationContextUtil.getBean(MqProducer.class);
		System.out.println("onMessage.mqProducer:" + mqProducer);
		mqProducer.send(params);
		
		// 返回消息给Web Socket客户端（浏览器）
		//这一条暂时注释掉，因为多个线程操作数据发送接口会出现异常
		//myWebSocket.sendMessage(1, "成功！", result);
	}

	/**
	 * 发生错误时触发的方法
	 * @throws IOException 
	 */
	@OnError
	public void onError(Session session, Throwable error) throws IOException {
		String idx = this.getIdx(session);
		System.out.println(idx + "连接发生错误" + error.getMessage());
		error.printStackTrace();
	}

	public void sendMessage(int status, String message, Object datas) throws IOException {
		JSONObject result = new JSONObject();
		result.put("status", status);
		result.put("message", message);
		result.put("datas", datas);
		this.session.getBasicRemote().sendText(result.toString());
	}
	
	public String getIdx(Session session) throws IOException {
		List<String> list = session.getRequestParameterMap().get("idx");
		for (String idx : list) {
			return idx;
		}
		return null;
	}

}