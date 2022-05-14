package com.lvf.springboot.activemq;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lvf.springboot.websocket.WebSocketService;

import javax.jms.*;

@Component
public class TrueReceiver {
	
    private final String BROKER_URL = "tcp://192.168.1.3:61616";
    //相当于一个数据库（其实是一个队列）
    private final String DESTINATION = "myQueue";
    private ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(BROKER_URL);
    private Connection connection = null;
    private Session session = null;
    private MessageConsumer messageConsumer = null;
    
    @Autowired
    private WebSocketService wss;
    
    public TrueReceiver() {
    	try {
            //2. 获取一个连接
            connection = connectionFactory.createConnection();
            //接收消息，需要将连接启动一下，才可以接收到消息
            connection.start();
            //3. 创建一个Session 第一个参数：是否是事务消息 第二个参数：消息确认机制（自动确认还是手动确认）
            session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
            //4. 有了session之后，就可以创建消息，目的地，生产者和消费者
            //目的地
            Destination destination = session.createQueue(DESTINATION);
            //消费者
            messageConsumer = session.createConsumer(destination);
            //循环接收消息
            while (true){
                //接收消息 有返回值，是阻塞的
                Message message = messageConsumer.receive();
                //判断消息类型
                if(message instanceof TextMessage){
                    String text = ((TextMessage) message).getText();
                    System.out.println("从activemq接收到的消息：" + text);
                    wss.toAllWebSocket(text);
                    System.out.println("向客户端发送消息：" + text);
                }
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }finally{
            try {
                if(messageConsumer != null){
                    messageConsumer.close();
                }
                if(session != null){
                    session.close();
                }
                if(connection != null){
                    connection.close();
                }
            }catch (JMSException e) {
                e.printStackTrace();
            }
        }
	}
    
}