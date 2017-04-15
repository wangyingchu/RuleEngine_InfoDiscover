package com.info.discover.ruleengine.plugins.propertymapping;

import com.info.discover.drools.rules.propertyMapping.SourceInput;
import com.info.discover.drools.rules.propertyMapping.TargetInput;
import com.infoDiscover.common.util.JsonUtil;

public class RuleContentVO {
	private String ruleContentJson;
	private SourceInput source;
	private TargetInput target;

	public RuleContentVO(String ruleContentJson) {
		this.ruleContentJson = ruleContentJson;
	}

	public void setSourceInput() {

		String source = JsonUtil.getPropertyValues(PropertyMappingConstants.JSON_SOURCE, ruleContentJson);
		String sourceType = JsonUtil.getPropertyValues(PropertyMappingConstants.JSON_SOURCE_TYPE, ruleContentJson);
		String sourceProperties = JsonUtil.getPropertyValues(PropertyMappingConstants.JSON_SOURCE_PROPERTIES,
				ruleContentJson);

		SourceInput input = new SourceInput();
		input.setSource(source);
		input.setSourceType(sourceType);
		if (sourceProperties != null) {
			input.setSourceProperties(sourceProperties.split(","));
		}
		this.source = input;

	}

	public void setTargetInput() {
		String target = JsonUtil.getPropertyValues(PropertyMappingConstants.JSON_TARGET, ruleContentJson);
		String targetType = JsonUtil.getPropertyValues(PropertyMappingConstants.JSON_TARGET_TYPE, ruleContentJson);
		String targetProperty = JsonUtil.getPropertyValues(PropertyMappingConstants.JSON_TARGET_PROPERTY, ruleContentJson);

		TargetInput input = new TargetInput();
		input.setTarget(target);
		input.setTargetType(targetType);
		input.setTargetProperty(targetProperty);
		this.target = input;
	}

	public SourceInput getSourceInput() {
		return source;
	}

	public TargetInput getTargetInput() {
		return target;
	}
}
