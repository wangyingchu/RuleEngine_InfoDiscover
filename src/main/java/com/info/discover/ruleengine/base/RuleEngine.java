package com.info.discover.ruleengine.base;

import com.infoDiscover.infoDiscoverEngine.util.exception.InfoDiscoveryEngineRuntimeException;

public interface RuleEngine {
    public boolean createRule(String ruleName, String description, String ruleType, String
            ruleContent) throws InfoDiscoveryEngineRuntimeException;
    
    public boolean updateRule(String ruleName, String description, String ruleType, String
            ruleContent);
    
    public boolean checkRuleIsExist(String ruleName);
    
}
