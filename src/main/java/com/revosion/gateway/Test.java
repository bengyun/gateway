package com.revosion.gateway;

import com.starwsn.protocol.core.Starwsn;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class Test {

	public static void main(String[] args) {
		// 构建MQTT连接
		String host = "tcp://120.77.155.222:1883"; // 服务地址
		String userName = "admin"; // 用户名
		String passWord = "password"; // 密码
		MqttServices mqttService = new MqttServices(host, userName, passWord);
		boolean cb = mqttService.connect(true);

		String cloud_url = "tcp://localhost:1883";
		String channel_id = "f8a79e69-75f8-4b6d-bc0c-52c3fa0f587a";
		String thing_id = "c69f6155-aa20-4021-bdbf-b428b2b478b1";
		String thing_key = "a3f3342c-7575-447d-bfa9-884fa44d3859";

		// MqttServices ms = new MqttServices(cloud_url, thing_id, thing_key);
		// boolean cb = ms.connect(true);

		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);

		SenML data = new SenML();
		data.setBaseName("pump1");
		data.setBaseTime(1222);
		data.setBaseUnit("v");
		data.setBaseValue(1.23);
		try {
			String jsonWriter = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
			System.out.println(jsonWriter);
			System.out.println(data.toString());
		} catch (JsonProcessingException e) {

		}

		String[] topics = { "ND/2TGH41/#", "ND/1CD34D/#", "ND/229JXK/#", "ND/1DG3G4/#" };
		int[] qos = { 1, 1, 1, 1 };
		if (cb) {
			// byte[] a = Starwsn.getCollectionIntervalCmd(10);
			// ms.publish("channels/" + channel_id + "/messages", a);
			// 订阅设备
			boolean sb = mqttService.subScription(topics, qos);

			if (sb) {
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
}
