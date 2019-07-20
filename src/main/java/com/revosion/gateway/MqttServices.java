package com.revosion.gateway;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.fasterxml.jackson.databind.JsonNode;

import org.apache.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * mqtt服务
 * @author 
 */
public class MqttServices {
	private static Logger logger = Logger.getLogger(MqttServices.class);
	private String host; // 服务地址
	private String userName; // 用户名
	private String passWord; // 密码
	private static int keepAlive = 10; // 超时时间
	private static int keepAliveInterval = 20; // 会话心跳时间
	private static String clientId = "starwsn-mqtt-test-295224018"; // 客户端ID
	private static MqttClient client; // 客户端对象
	private BlockingQueue<JsonNode> queue = new LinkedBlockingQueue<JsonNode>();
	@SuppressWarnings("rawtypes")
	private List<LinkedHashMap> topics_to_sub;

	public MqttServices(String host, String userName, String passWord, List<LinkedHashMap> topics_to_sub) {
		this.host = host;
		this.userName = userName;
		this.passWord = passWord;
		this.topics_to_sub = topics_to_sub;
	}

	/**
	 * 连接服务器
	 * @param cleanSession 是否清除session
	 */
	@SuppressWarnings("rawtypes")
	public boolean connect(boolean cleanSession) {
		try {
			client = new MqttClient(host, clientId, new MemoryPersistence());
			MqttConnectOptions options = new MqttConnectOptions();
			options.setCleanSession(cleanSession);
			options.setUserName(userName);
			options.setPassword(passWord.toCharArray());
			// 设置超时时间
			options.setConnectionTimeout(keepAlive);
			// 设置会话心跳时间
			options.setKeepAliveInterval(keepAliveInterval);
			client.setCallback(new MessageCallback(this));
			client.connect(options);
			logger.info("mqtt服务连接成功！！！");
			// 订阅设备
			for (LinkedHashMap topic : this.topics_to_sub) {
				this.subScription((String) topic.get("topic"), (int) topic.get("qos"));
			}
			return true;
		} catch (Exception e) {
			logger.info("mqtt服务连接失败！！！");
			return false;
		}
	}
	
	/**
	 * 断开客户端连接
	 * @param clientId
	 */
	public boolean disConnect() {
		try {
			client.disconnect();
			client.close();
			logger.info("mqtt服务断开成功！！！");
			return true;
		} catch (MqttException e) {
			logger.info("mqtt服务断开成功！！！");
			return false;
		}
	}

	/**
	 * 主题订阅
	 * @param topics 主题名称数组
	 * @param Qos 服务质量数组（与主题名称一一匹配）
	 * @return
	 */
	public boolean subScription(String[] topics,int[] Qos) {
		try {
			client.subscribe(topics, Qos);
			String loginfo = "mqtt主题{";
			for(int i=0;i<topics.length;i++){
				loginfo += topics[i];
				if(i != topics.length-1)
					loginfo += ",";
			}
			loginfo += "}订阅成功！！！";
			logger.info(loginfo);
			return true;
		} catch (Exception e) {
			String loginfo = "mqtt主题{";
			for(int i=0;i<topics.length;i++){
				loginfo += topics[i];
				if(i != topics.length-2)
					loginfo += ",";
			}
			loginfo += "}订阅失败！！！";
			logger.info(loginfo);
			return false;
		}
	}
	
	/**
	 * 主题订阅
	 * @param topic 主题名称
	 * @param Qos  服务质量
	 * @return
	 */
	public boolean subScription(String topic,int Qos) {
		try {
			int[] _Qos = { Qos };
			String[] _topic = { topic };
			client.subscribe(_topic, _Qos);
			logger.info("mqtt主题{"+topic+"}订阅成功！！！");
			return true;
		} catch (Exception e) {
			logger.info("mqtt主题{"+topic+"}订阅失败！！！");
			return false;
		}
	}
	
	/**
	 * 主题订阅，默认服务质量为0
	 * @param topic 主题名称
	 * @return
	 */
	public boolean subScription(String topic) {
		try {
			int[] _Qos = { 0 };
			String[] _topic = { topic };
			client.subscribe(_topic, _Qos);
			logger.info("mqtt主题{"+topic+"}订阅成功！！！");
			return true;
		} catch (Exception e) {
			logger.info("mqtt主题{"+topic+"}订阅失败！！！");
			return false;
		}
	}

	/**
	 * 取消主题订阅
	 * @param topic 主题名称
	 * @return
	 */
	public boolean unSubScription(String[] topics) {
		String topic = "";
		try {
			for(int i=0;i<topics.length;i++){
				topic = topics[i];
				client.unsubscribe(topic);
				logger.info("mqtt主题{" + topic + "}取消订阅成功！！！");
			}
			return true;
		} catch (Exception e) {
			logger.info("mqtt主题{" + topic + "}取消订阅失败！！！");
			return false;
		}
	}
	
	/**
	 * 取消主题订阅
	 * @param topic 主题名称
	 * @return
	 */
	public boolean unSubScription(String topic) {
		try {
			client.unsubscribe(topic);
			logger.info("mqtt主题{" + topic + "}取消订阅成功！！！");
			return true;
		} catch (Exception e) {
			logger.info("mqtt主题{" + topic + "}取消订阅失败！！！");
			return false;
		}
	}
	
	/**
	 * 推送消息
	 * @param topicName  主题名称
	 * @param payload  消息内容
	 * @return
	 */
	public boolean publish(String topicName, byte[] payload) {
		try {
			MqttTopic topic = client.getTopic(topicName);
			MqttMessage message = new MqttMessage();
			message.setQos(1);
			message.setRetained(false);
			message.setPayload(payload);
			MqttDeliveryToken token = topic.publish(message);
			logger.info("mqtt消息发送结果等待。。。。。");
			token.waitForCompletion();
			logger.info("mqtt消息发送结果：" + token.isComplete());
			return token.isComplete();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public void addQueue(JsonNode jsonNode) {
		this.queue.add(jsonNode);
	}

	public JsonNode getQueue() throws InterruptedException {
		return this.queue.take();
	}
}
