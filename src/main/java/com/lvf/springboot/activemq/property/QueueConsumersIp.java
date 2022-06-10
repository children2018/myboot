package com.lvf.springboot.activemq.property;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.activemq.broker.jmx.ConnectionViewMBean;
import org.apache.activemq.broker.jmx.QueueViewMBean;
import org.apache.activemq.broker.jmx.SubscriptionViewMBean;
import org.apache.commons.compress.utils.Lists;

/**
 * 获取某个队列下面所有的消费者IP
 * <br/>
 * 1、activemq-5.16.2\conf\activemq.xml 
 * 2、<broker xmlns="http://activemq.apache.org/schema/core" brokerName="localhost" dataDirectory="${activemq.data}" useJmx="true">
 * 3、<managementContext>
 *        <managementContext createConnector="true" connectorPort="61618" />
 *    </managementContext>
 * @author Administrator
 *
 */
public class QueueConsumersIp {

	private static final String queueObjectName = "org.apache.activemq:type=Broker,brokerName=localhost,destinationType=Queue,destinationName=%s";

	private static final String brokerAddress = "service:jmx:rmi:///jndi/rmi://%s/jmxrmi";
	
	public static void main(String[] args) {
		try {
			List<String> list = getQueueConsumers("yaya", "yaya002", "192.168.1.2:61618", "sms.up.queue");
			for (String str : list) {
				System.out.println("str:" + str);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static List getQueueConsumers(String user, String password, String ip, String queueName) throws Exception {

		List result = Lists.newArrayList();

		String url = String.format(brokerAddress, ip);

		Map credentials = new HashMap<>();

		credentials.put(JMXConnector.CREDENTIALS, new String[] { user, password });

		JMXServiceURL urls = new JMXServiceURL(url);

		JMXConnector connector = JMXConnectorFactory.connect(urls, credentials);

		connector.connect();

		MBeanServerConnection conn = connector.getMBeanServerConnection();

		ObjectName name = new ObjectName(String.format(queueObjectName, queueName));

		QueueViewMBean queueViewMBean = MBeanServerInvocationHandler.newProxyInstance(conn, name, QueueViewMBean.class,
				true);

		for (ObjectName mbeanNameConsumerObjectName : queueViewMBean.getSubscriptions()) {
			
			System.out.println("mbeanNameConsumerObjectName:" + mbeanNameConsumerObjectName);

			SubscriptionViewMBean subscriptionViewMBean = MBeanServerInvocationHandler.newProxyInstance(conn,
					mbeanNameConsumerObjectName, SubscriptionViewMBean.class, true);

			ObjectName connectionObjectName = new ObjectName(subscriptionViewMBean.getConnection().getCanonicalName());

			ConnectionViewMBean connectionViewMBean = MBeanServerInvocationHandler.newProxyInstance(conn,
					connectionObjectName, ConnectionViewMBean.class, true);

			if (connectionViewMBean != null) {
				result.add(connectionViewMBean.getRemoteAddress().replace("//", "").split("\\:")[1]);
				// System.out.println(connectionViewMBean.getRemoteAddress()+"|"+connectionViewMBean.getClientId());
			}

		}

		return result;

	}

}
