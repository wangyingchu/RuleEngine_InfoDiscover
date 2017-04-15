package com.info.discover.ruleengine.base.vo;

public class RuleVO {
	private String ruleId;
	private String name;
	private String type;
    private String description;
    private String content;
    
    public RuleVO(String ruleId, String name, String type, String description, String content) {
    	this.ruleId = ruleId;
    	this.name = name;
    	this.type = type;
    	this.description = description;
    	this.content = content;
    }
    
	public String getRuleId() {
		return ruleId;
	}

	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

}
