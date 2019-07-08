package com.revosion.gateway;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revosion.gateway.JsonToSenML;

import org.testng.annotations.Test;

public class JsonToSenMLTest {

    @Test
    public void testToSenML() throws JsonProcessingException, IOException {
        ClassLoader classLoader = getClass().getClassLoader();

        ObjectMapper mapper = new ObjectMapper();
        InputStream inputStream = classLoader.getResourceAsStream("data.json");
        JsonNode jsonNode = mapper.readTree(inputStream);
        List<SenML> data = JsonToSenML.toSenML(jsonNode.get("msg"));

        mapper.setSerializationInclusion(Include.NON_NULL);
        String jsonWriter = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
        System.out.println(jsonWriter);
    }
}