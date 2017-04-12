package com.info.discover.drools.rules.propertyMapping;

public class SourceInput {
	private String source = "Fact";
	private String sourceType;
	private String[] sourceProperties;
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
	
	
}
