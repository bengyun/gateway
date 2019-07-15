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
    private LinkedHashMap things = null;
    @SuppressWarnings("rawtypes")
    private List<LinkedHashMap> starwsn_topics = null;
    private boolean loadResult = false;
    
    /* mqtt.properties */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public Config() {
        try {
        	InputStream input = this.getClass().getClassLoader().getResourceAsStream("config.yml");
            Yaml yaml = new Yaml();
            linkedHashMap = yaml.load(input);            
            things = (LinkedHashMap<String, Object>)(linkedHashMap.get("things"));
            starwsn_topics = (List<LinkedHashMap>) (linkedHashMap.get("mqtt_starwsn_topics"));
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
        log.info("things=" + Things());
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
    public String BengyunChannelId() {
        return loadResult == true ? (String) (linkedHashMap.get("mqtt_bengyun_channel_id")) : null;
    }
    /* things list */
    @SuppressWarnings("rawtypes")
    public LinkedHashMap Things() {
        return loadResult == true ? things : null;
    }
    /* starwsn topic list */
    @SuppressWarnings("rawtypes")
    public List<LinkedHashMap> StarwsnTopics() {
        return loadResult == true ? starwsn_topics : null;
    }
}
