package com.revosion.gateway;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;


public class GatewayService {

	@SuppressWarnings("rawtypes")
	public static void main(String[] args) {
		final Logger log = Logger.getLogger(GatewayService.class);
		final Config aMqttConfig = new Config();

		// 构建MQTT连接
		MqttServices mqttService = new MqttServices(aMqttConfig.StarwsnHost(), aMqttConfig.StarwsnUserName(),
				aMqttConfig.StarwsnPassWord(), aMqttConfig.StarwsnTopics());
		mqttService.connect(true);

		//MqttServices ms = new MqttServices(aMqttConfig.BengyunHost(), "d", "d");
		//boolean cb1 = ms.connect(true);
		CloseableHttpClient client = HttpClients.createDefault();

		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);

		while (true) {
			try {
				JsonNode jsonNode = mqttService.getQueue();
				List<SenML> data = JsonToSenML.toSenML(jsonNode.get("msg"));
				String deviceName = jsonNode.get("snCode").asText();
				String message = mapper.writeValueAsString(data);
				log.info("message in senml format:" + message);
				if (aMqttConfig.Things().containsKey(deviceName)) {
					StringEntity entity = new StringEntity(message);
					HttpPost httpPost = new HttpPost(
							aMqttConfig.BengyunHost() + aMqttConfig.BengyunChannelId() + "/messages");
					String thing_id = ((LinkedHashMap<String, String>) (aMqttConfig.Things().get(deviceName)))
							.get("thingid");
					String thing_key = ((LinkedHashMap<String, String>) (aMqttConfig.Things().get(deviceName)))
							.get("thingkey");
					log.info(thing_id + " - " + thing_key);
					httpPost.setEntity(entity);
					httpPost.setHeader("Content-type", "application/senml+json");
					httpPost.setHeader("Authorization", thing_key);
					CloseableHttpResponse response = client.execute(httpPost);
					log.info(response.getStatusLine());
					response.close();
					
				}
			} catch (JsonProcessingException e) {
				log.error(e);
			} catch (InterruptedException e) {
				log.error(e);
			} catch (IOException e) {
				log.error(e);
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
