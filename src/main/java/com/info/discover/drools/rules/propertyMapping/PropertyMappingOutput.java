package com.info.discover.drools.rules.propertyMapping;

public class PropertyMappingOutput {
	private String source = "Fact";
	private String sourceType;
	private String[] sourceProperties;
	private String target = "Dimension";
	private String targetType;
	private String targetProperty;

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public String[] getSourceProperties() {
		return sourceProperties;
	}

	public void setSourceProperties(String[] sourceProperties) {
		this.sourceProperties = sourceProperties;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	public String getTargetProperty() {
		return targetProperty;
	}

	public void setTargetProperty(String targetProperty) {
		this.targetProperty = targetProperty;
	}

}
