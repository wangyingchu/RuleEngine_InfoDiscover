package com.info.discover.ruleengine.plugins.propertymapping;

import java.util.List;

import com.info.discover.ruleengine.base.RuleEngine;
import com.infoDiscover.infoDiscoverEngine.dataMart.Dimension;
import com.infoDiscover.infoDiscoverEngine.dataMart.Fact;

public interface PropertyMappingRuleEngine extends RuleEngine {
	public String getRuleContent(String ruleName);
	
    public List<Dimension> executeRule(String spaceName, Fact fact);
}
