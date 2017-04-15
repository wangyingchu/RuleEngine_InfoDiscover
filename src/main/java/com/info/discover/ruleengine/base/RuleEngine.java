package com.info.discover.ruleengine.base;

import com.info.discover.ruleengine.base.vo.RuleVO;
import com.infoDiscover.infoDiscoverEngine.util.exception.InfoDiscoveryEngineRuntimeException;

public interface RuleEngine {
    public void createRule(RuleVO rule) throws InfoDiscoveryEngineRuntimeException;
    
    public void updateRule(RuleVO rule);
    
    public boolean checkRuleExistence(String ruleId);
    
    public boolean deleteRule(String ruleId);
}
