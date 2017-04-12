package com.info.discover.ruleengine.base;

import java.util.HashMap;
import java.util.Map;

import com.infoDiscover.infoDiscoverEngine.dataMart.Fact;
import com.infoDiscover.infoDiscoverEngine.infoDiscoverBureau.InfoDiscoverSpace;
import com.infoDiscover.infoDiscoverEngine.util.exception.InfoDiscoveryEngineRuntimeException;
import com.infoDiscover.infoDiscoverEngine.util.factory.DiscoverEngineComponentFactory;

public class RuleEngineImpl implements RuleEngine {

	public boolean createRule(String ruleName, String description, String ruleType, String ruleContent)
			throws InfoDiscoveryEngineRuntimeException {
		boolean result = false;

		InfoDiscoverSpace ids = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(RuleConstants.RuleEngineSpace);
		if (ids != null) {
			Fact fact = DiscoverEngineComponentFactory.createFact(RuleConstants.RuleFact);

			fact = ids.addFact(fact);

			Map<String, Object> props = new HashMap<String, Object>();
			props.put(RuleConstants.FACT_NAME, ruleName);
			props.put(RuleConstants.FACT_DESCRIPTION, description);
			props.put(RuleConstants.FACT_TYPE, ruleType);
			props.put(RuleConstants.FACT_CONTENT, ruleContent);

			fact.addProperties(props);

			result = true;

		} else {
			System.out.println("Failed to connect to database: " + RuleConstants.RuleEngineSpace);
		}

		ids.closeSpace();

		return result;
	}

	public boolean updateRule(String ruleName, String description, String ruleType, String ruleContent) {
		return false;
	}

	public boolean checkRuleIsExist(String ruleName) {
		// TODO Auto-generated method stub
		return false;
	}

}
