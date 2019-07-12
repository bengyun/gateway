package com.revosion.gateway;

import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import org.yaml.snakeyaml.Yaml;

import org.apache.log4j.Logger;

/* MQTT配置文件 */
public class Config {
    private Logger log = Logger.getLogger(Config.class);
    private LinkedHashMap<String, Object> linkedHashMap = null;
    @SuppressWarnings("rawtypes")
	private List<LinkedHashMap> things = null;
    private String[] mqtt_topic_list = null;
    private int[] mqtt_qos_list = null;
    private boolean loadResult = false;
    
    /* mqtt.properties */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public Config() {
        try {
        	InputStream input = this.getClass().getClassLoader().getResourceAsStream("config.yml");
            Yaml yaml = new Yaml();
            linkedHashMap = yaml.load(input);            
            things = (List<LinkedHashMap>)(linkedHashMap.get("things"));
            mqtt_topic_list = new String[things.size()];
            mqtt_qos_list = new int[things.size()];
            for(int idx = 0; idx < things.size(); idx++) {
            	mqtt_topic_list[idx] = (String)(things.get(idx).get("mqtt_topic"));
            	mqtt_qos_list[idx] = (int)(things.get(idx).get("mqtt_qos"));
            }
            log.info("加载配置文件成功");
            loadResult = true;
        } catch (Exception e) {
            log.error("加载配置文件失败或配置文件不存在");
            log.error(e.getMessage());
            loadResult = false;
        }
        
        
        /* Test */
        log.info("Config Information:");
        log.info("mqtt_starwsn_host=" + StarwsnHost());
        log.info("mqtt_starwsn_username=" + StarwsnUserName());
        log.info("mqtt_starwsn_password=" + StarwsnPassWord());
        log.info("mqtt_bengyun_host=" + BengyunHost());
        log.info("mqtt_bengyun_channel_id=" + BengyunChannelId());
        log.info("mqtt_topic_list=" + Arrays.asList(StarwsnTopics()));
        int[] aQos = StarwsnQos();
        String strQoslist = "mqtt_qos_list=[";
        for(int idx = 0; idx < aQos.length - 1; idx++) strQoslist = strQoslist + aQos[idx] + ", ";
        log.info(strQoslist + aQos[aQos.length - 1] + "]");
    }
    
    /* mqtt_starwsn_host */
    public String StarwsnHost(){
        return loadResult == true ? (String)(linkedHashMap.get("mqtt_starwsn_host")) : null;
    }
    /* mqtt_starwsn_username */
    public String StarwsnUserName(){
        return loadResult == true ? (String)(linkedHashMap.get("mqtt_starwsn_username")) : null;
    }
    /* mqtt_starwsn_password */
    public String StarwsnPassWord(){
        return loadResult == true ? (String)(linkedHashMap.get("mqtt_starwsn_password")) : null;
    }
    /* mqtt_bengyun_host */
    public String BengyunHost(){
        return loadResult == true ? (String)(linkedHashMap.get("mqtt_bengyun_host")) : null;
    }
    /* mqtt_bengyun_channel_id */
    public String BengyunChannelId(){
        return loadResult == true ? (String)(linkedHashMap.get("mqtt_bengyun_channel_id")) : null;
    }
    /* mqtt_topic_list */
    public String[] StarwsnTopics(){
        return loadResult == true ? mqtt_topic_list : null;
    }
    /* mqtt_qos_list */
    public int[] StarwsnQos(){
    	return loadResult == true ? mqtt_qos_list : null;
    }
    /* mqtt_qos_list */
    public String ThingId(){
    	return loadResult == true ? (String)(things.get(0).get("thingid")) : null;
    }
    /* mqtt_qos_list */
    public String ThingKey(){
    	return loadResult == true ? (String)(things.get(0).get("thingkey")) : null;
    }
}
