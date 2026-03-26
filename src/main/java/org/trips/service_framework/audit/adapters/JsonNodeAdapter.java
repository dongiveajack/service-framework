package org.trips.service_framework.audit.adapters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSerializationContext;
import org.javers.core.json.JsonTypeAdapter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author anomitra on 16/08/24
 */

@Component
public class JsonNodeAdapter implements JsonTypeAdapter<ObjectNode> {

    // NOTE: This class is an adapter for JsonNode objects defined in entities - however
    // it is written with ObjectNode as the type parameter because JsonNode is an
    // abstract class, and our JsonNode fields actually contain ObjectNode objects.

    private final ObjectMapper objectMapper;

    public JsonNodeAdapter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public ObjectNode fromJson(JsonElement json,
                             JsonDeserializationContext jsonDeserializationContext) {
        try {
            return (ObjectNode) objectMapper.readTree(json.toString());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize JSON to POJO", e);
        }
    }

    @Override
    public JsonElement toJson(ObjectNode sourceValue, JsonSerializationContext jsonSerializationContext) {
        return JsonParser.parseString(sourceValue.toString());
    }

    @Override
    public List<Class> getValueTypes() {
        return List.of(JsonNode.class, ObjectNode.class);
    }
}
