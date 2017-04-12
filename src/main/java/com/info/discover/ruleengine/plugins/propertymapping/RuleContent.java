package com.info.discover.ruleengine.plugins.propertymapping;

import com.info.discover.drools.rules.propertyMapping.SourceInput;
import com.info.discover.drools.rules.propertyMapping.TargetInput;
import com.infoDiscover.common.util.JsonUtil;

public class RuleContent {
	private String ruleContent;
	private SourceInput source;
	private TargetInput target;

	public RuleContent(String ruleContent) {
		this.ruleContent = ruleContent;
	}

	public void setSourceInput() {

		String source = JsonUtil.getPropertyValues(PropertyMappingConstants.JSON_SOURCE, ruleContent);
		String sourceType = JsonUtil.getPropertyValues(PropertyMappingConstants.JSON_SOURCE_TYPE, ruleContent);
		String sourceProperties = JsonUtil.getPropertyValues(PropertyMappingConstants.JSON_SOURCE_PROPERTIES,
				ruleContent);

		SourceInput input = new SourceInput();
		input.setSource(source);
		input.setSourceType(sourceType);
		if (sourceProperties != null) {
			input.setSourceProperties(sourceProperties.split(","));
		}
		this.source = input;

	}

	public void setTargetInput() {
		String target = JsonUtil.getPropertyValues(PropertyMappingConstants.JSON_TARGET, ruleContent);
		String targetType = JsonUtil.getPropertyValues(PropertyMappingConstants.JSON_TARGET_TYPE, ruleContent);
		String targetProperty = JsonUtil.getPropertyValues(PropertyMappingConstants.JSON_TARGET_PROPERTY, ruleContent);

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
