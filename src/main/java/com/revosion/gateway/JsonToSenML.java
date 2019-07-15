package com.revosion.gateway;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import com.fasterxml.jackson.databind.JsonNode;

public class JsonToSenML {

    public static List<SenML> toSenML(JsonNode jsonNode) {
        List<SenML> list = new ArrayList<SenML>();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        if (jsonNode.isArray()) {
            for (final JsonNode objNode : jsonNode) {
                SenML senML = new SenML();
                senML.setName(objNode.get("monitorName").textValue().replace("液位高度", "WaterLevel").replace("设备实时电压",
                        "BatteryVoltage"));
                senML.setUnit(objNode.get("dataUnit").textValue());
                senML.setValue(objNode.get("monitorData").asDouble());
                try {
                    senML.setTime(formatter.parse(objNode.get("monitorTime").textValue()).getTime()/1000);
                } catch (ParseException e) {

                }
                list.add(senML);
                list.get(0).setBaseName("test");
                list.get(0).setBaseUnit("cm");
            }
        }
        return list;
    }

}