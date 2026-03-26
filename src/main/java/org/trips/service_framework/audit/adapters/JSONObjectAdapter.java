package org.trips.service_framework.audit.adapters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSerializationContext;
import org.javers.core.json.JsonTypeAdapter;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author anomitra on 16/08/24
 */

@Component
public class JSONObjectAdapter implements JsonTypeAdapter<JSONObject> {

    private final ObjectMapper objectMapper;

    public JSONObjectAdapter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public JSONObject fromJson(JsonElement json,
                             JsonDeserializationContext jsonDeserializationContext) {
        try {
            JsonNode jsonNode = objectMapper.readTree(json.toString());
            return objectMapper.treeToValue(jsonNode, JSONObject.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize JSON to POJO", e);
        }
    }

    @Override
    public JsonElement toJson(JSONObject sourceValue,
                              JsonSerializationContext jsonSerializationContext) {
        JsonNode jsonNode = objectMapper.valueToTree(sourceValue);
        return JsonParser.parseString(jsonNode.toString());
    }

    @Override
    public List<Class> getValueTypes() {
        return List.of(JSONObject.class);
    }
}
