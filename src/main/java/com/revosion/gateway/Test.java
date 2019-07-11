package com.revosion.gateway;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class Test {

	public static void main(String[] args) {
		
		MqttConfig aMqttConfig = new MqttConfig();
		
		// 构建MQTT连接
		MqttServices mqttService = new MqttServices(aMqttConfig.StarwsnHost(), aMqttConfig.StarwsnUserName(), aMqttConfig.StarwsnPassWord());
		boolean cb = mqttService.connect(true);
		if (cb) {
			// byte[] a = Starwsn.getCollectionIntervalCmd(10);
			// 订阅设备
			mqttService.subScription(aMqttConfig.StarwsnTopics(), aMqttConfig.StarwsnQos());
		}

		MqttServices ms = new MqttServices(aMqttConfig.CnPumpHost(), aMqttConfig.CnPumpThingId(), aMqttConfig.CnPumpThingKey());
		boolean cb1 = ms.connect(true);

		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);

		while (cb1) {
			try {
				List<SenML> data = mqttService.getQueue();
				byte[] message = mapper.writeValueAsBytes(data);
				System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data));
				ms.publish("channels/" + aMqttConfig.CnPumpChannelId() + "/messages", message);
			} catch (JsonProcessingException e) {

			} catch (InterruptedException e) {

			}

			// 设备采集周期指令
			// byte[] a = Starwsn.getCollectionIntervalCmd(10);
			// 设备上传周期指令
			// byte[] b = Starwsn.getUploadIntervalCmd(100);
			// 设备压力上下限
			// byte[] c = Starwsn.getPressureLimitCmd(0.7, 0.3);
			// mqttService.publish("ND/BDA393/set_para", a);
			// mqttService.publish("ND/BDA393/set_para", b);
			// mqttService.publish("ND/BDA393/set_para", c);
		}
	}

}
