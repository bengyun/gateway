package com.revosion.gateway;

import org.apache.log4j.Logger;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.starwsn.protocol.core.Starwsn;

public class MessageCallback  implements MqttCallback{
	private Logger log = Logger.getLogger(MessageCallback.class);
	private MqttServices mqttService;

	public MessageCallback(MqttServices mqttService) {
		this.mqttService = mqttService;
	}
	
	public void connectionLost(Throwable cause) {
		log.info("mqtt服务断开连接！！！");
		for(int i = 0; i < 10000; i++) {
			log.info("第【" + (i + 1) + "】次尝试连接MQTT服务");
			boolean cb = this.mqttService.connect(false);
			if (cb) {
				log.info("mqtt服务重连成功！！！");
				break;
			}
			try {
				if(i<5){
					Thread.sleep(5000);
				}else if(i>=5 && i<10){
					Thread.sleep(10000);
				}else{
					Thread.sleep(60000);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(i==9999){
				log.info("mqtt服务重连失败！！！");
			}
		}
	}

	public void deliveryComplete(IMqttDeliveryToken token) {
		log.debug("==============mqtt消息发布状态："+token.isComplete()+"==============");
	}

	public void messageArrived(String topic, MqttMessage message) {
		try {
			String[] topics = topic.split("/");
			if("set_para".equals(topics[2])){
				return;
			}
			// 消息体
			byte[] payLoadArray = message.getPayload();
			String jsonStr = Starwsn.messageResolve(topic, payLoadArray);
			log.debug(JsonFormatUtil.formatJson(jsonStr));
			ObjectMapper mapper = new ObjectMapper();
			JsonNode jsonNode = mapper.readTree(jsonStr);

			if (jsonNode.get("msgType").asInt() == 2) {
				this.mqttService.addQueue(jsonNode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}
}
