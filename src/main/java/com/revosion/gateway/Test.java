package com.revosion.gateway;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class Test {

	public static void main(String[] args) {
		// 构建MQTT连接
		String host = "tcp://120.77.155.222:1883"; // 服务地址
		String userName = "admin"; // 用户名
		String passWord = "password"; // 密码
		MqttServices mqttService = new MqttServices(host, userName, passWord);
		boolean cb = mqttService.connect(true);
		String[] topics = { "ND/1CD34D/#", "ND/1DG3G4/#", "ND/64BW9M/#", "ND/JSU127/#", "ND/E69UH4/#", "ND/T9G9L6/#",
				"ND/W871YD/#", "ND/FP735S/#", "ND/11N1WS/#", "ND/U40UT5/#", "ND/V2C49C/#", "ND/51Q1YX/#", "ND/B8M8X3/#",
				"ND/5F98PW/#" };
		int[] qos = { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };
		if (cb) {
			// byte[] a = Starwsn.getCollectionIntervalCmd(10);
			// 订阅设备
			boolean sb = mqttService.subScription(topics, qos);
		}

		String cloud_url = "tcp://localhost:1883";
		String channel_id = "f8a79e69-75f8-4b6d-bc0c-52c3fa0f587a";
		String thing_id = "c69f6155-aa20-4021-bdbf-b428b2b478b1";
		String thing_key = "a3f3342c-7575-447d-bfa9-884fa44d3859";

		MqttServices ms = new MqttServices(cloud_url, thing_id, thing_key);
		boolean cb1 = ms.connect(true);

		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);

		while (cb1) {
			try {
				List<SenML> data = mqttService.getQueue();
				byte[] message = mapper.writeValueAsBytes(data);
				System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data));
				ms.publish("channels/" + channel_id + "/messages", message);
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
