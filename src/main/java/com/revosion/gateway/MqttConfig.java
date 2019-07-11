package com.revosion.gateway;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/* MQTT配置文件 */
public class MqttConfig {
	private Logger log = Logger.getLogger(MqttConfig.class);
    InputStream input = null;
    Properties p = new Properties();
    
    /* mqtt.properties */
    public MqttConfig() {
    	try {
            input = this.getClass().getClassLoader().getResourceAsStream("mqtt.properties");
            p.load(input);
            System.out.println("加载配置文件成功");
            log.info("加载配置文件成功");
        } catch (IOException e) {
            System.out.println("加载配置文件失败或配置文件不存在");
            log.error("加载配置文件失败或配置文件不存在");
            e.printStackTrace();
        }
	}
    
    /* mqtt.starwsn.host */
    public String StarwsnHost(){
        return p.getProperty("mqtt.starwsn.host");
    }
    /* mqtt.starwsn.username */
    public String StarwsnUserName(){
        return p.getProperty("mqtt.starwsn.username");
    }
    /* mqtt.starwsn.password */
    public String StarwsnPassWord(){
        return p.getProperty("mqtt.starwsn.password");
    }
    /* mqtt.starwsn.topics */
    public String[] StarwsnTopics(){
    	String strTopics = p.getProperty("mqtt.starwsn.topics");
        return strTopics.split(",");
    }
    /* mqtt.starwsn.qos */
    public int[] StarwsnQos(){
    	String strQos = p.getProperty("mqtt.starwsn.qos");
    	String[] strArrayQos = strQos.split(",");
    	int[] iArrayQos = new int[strArrayQos.length];
    	for(int idx = 0; idx < strArrayQos.length; idx++) {
    		try {
    			iArrayQos[idx] = Integer.parseInt(strArrayQos[idx]);
    		} catch(NumberFormatException e) {
    			iArrayQos[idx] = 0;
    		}
    	}
        return iArrayQos;
    }
    
    /* mqtt.cnpump.host */
    public String CnPumpHost(){
        return p.getProperty("mqtt.cnpump.host");
    }
    /* mqtt.cnpump.thingid */
    public String CnPumpThingId(){
        return p.getProperty("mqtt.cnpump.thingid");
    }
    /* mqtt.cnpump.thingkey */
    public String CnPumpThingKey(){
        return p.getProperty("mqtt.cnpump.thingkey");
    }
    /* mqtt.cnpump.channelid */
    public String CnPumpChannelId(){
        return p.getProperty("mqtt.cnpump.channelid");
    }
}
