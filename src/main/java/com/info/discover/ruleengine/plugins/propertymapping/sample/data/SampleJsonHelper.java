package com.info.discover.ruleengine.plugins.propertymapping.sample.data;

import java.io.IOException;

import org.codehaus.jackson.JsonNode;

import com.infoDiscover.common.util.JsonUtil;

public class SampleJsonHelper {
	public static JsonNode getDataNode(String json) {
		if (json == null) {
			return null;
		}

		try {
			JsonNode jsonNode = JsonUtil.string2JsonNode(json);
			return jsonNode.get(JsonConstants.JSON_DATA);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static JsonNode getJsonNodes(String type, String json) {
		JsonNode dataNode = getDataNode(json);
		if (dataNode == null) {
			return null;
		}
		return dataNode.get(type);
	}
	
	public static JsonNode getPropertiesJsonNode(JsonNode jsonNode) {
        if (jsonNode == null) {
            return null;
        }
        return jsonNode.get(JsonConstants.JSON_PROPERTIES);
    }
}
