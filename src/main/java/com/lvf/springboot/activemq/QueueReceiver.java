package com.lvf.springboot.activemq;
import org.apache.activemq.ActiveMQConnectionFactory;
import javax.jms.*;
public class QueueReceiver {
    public static final String BROKER_URL = "tcp://192.168.1.3:61616";
    //相当于一个数据库（其实是一个队列）
    public static final String DESTINATION = "myQueue";

    public static void main(String[] args) {
        receiveMessage();
    }
    public static void receiveMessage(){
        //1 .创建一个连接工厂
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(BROKER_URL);
        Connection connection = null;
        Session session = null;
        MessageConsumer messageConsumer = null;
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
                    System.out.println(text);
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