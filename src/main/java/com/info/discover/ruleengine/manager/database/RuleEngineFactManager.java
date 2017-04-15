package com.info.discover.ruleengine.manager.database;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.info.discover.ruleengine.base.vo.RuleVO;
import com.infoDiscover.infoDiscoverEngine.dataMart.Fact;
import com.infoDiscover.infoDiscoverEngine.dataMart.PropertyType;
import com.infoDiscover.infoDiscoverEngine.dataWarehouse.ExploreParameters;
import com.infoDiscover.infoDiscoverEngine.dataWarehouse.InformationExplorer;
import com.infoDiscover.infoDiscoverEngine.dataWarehouse.InformationFiltering.EqualFilteringItem;
import com.infoDiscover.infoDiscoverEngine.infoDiscoverBureau.InfoDiscoverSpace;
import com.infoDiscover.infoDiscoverEngine.util.exception.InfoDiscoveryEngineDataMartException;
import com.infoDiscover.infoDiscoverEngine.util.exception.InfoDiscoveryEngineInfoExploreException;
import com.infoDiscover.infoDiscoverEngine.util.exception.InfoDiscoveryEngineRuntimeException;

public class RuleEngineFactManager {

	private final static Logger logger = LoggerFactory.getLogger(RuleEngineFactManager.class);

	public static void createRuleFactType(InfoDiscoverSpace ids)
			throws InfoDiscoveryEngineDataMartException, InfoDiscoveryEngineRuntimeException {
		logger.info("Start to createRuleFactType with properties");
		Map<String, PropertyType> properties = new HashMap<String, PropertyType>();
		properties.put(RuleEngineDatabaseConstants.FACT_RULENAME, PropertyType.STRING);
		properties.put(RuleEngineDatabaseConstants.FACT_ID, PropertyType.STRING);
		properties.put(RuleEngineDatabaseConstants.FACT_TYPE, PropertyType.STRING);
		properties.put(RuleEngineDatabaseConstants.FACT_DESCRIPTION, PropertyType.STRING);
		properties.put(RuleEngineDatabaseConstants.FACT_CONTENT, PropertyType.STRING);

		FactManager.createFactType(ids, RuleEngineDatabaseConstants.RuleFact, properties);
		logger.info("End to createRuleFactType()...");
	}

	public static boolean createRule(InfoDiscoverSpace ids, RuleVO rule) throws InfoDiscoveryEngineRuntimeException {
		logger.info("Start to createRuleFact with properties");
		Map<String, Object> properties = convertRuleVOToMap(rule);
		logger.info("End to createRuleFact()...");
		return FactManager.createFact(ids, RuleEngineDatabaseConstants.RuleFact, properties);
	}

	private static Map<String, Object> convertRuleVOToMap(RuleVO rule) {
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(RuleEngineDatabaseConstants.FACT_RULENAME, rule.getName());
		properties.put(RuleEngineDatabaseConstants.FACT_ID, rule.getRuleId());
		properties.put(RuleEngineDatabaseConstants.FACT_TYPE, rule.getType());
		properties.put(RuleEngineDatabaseConstants.FACT_DESCRIPTION, rule.getDescription());
		properties.put(RuleEngineDatabaseConstants.FACT_CONTENT, rule.getContent());
		return properties;
	}

	public static RuleVO getRule(InfoDiscoverSpace ids, String factType, String ruleId)
			throws InfoDiscoveryEngineRuntimeException, InfoDiscoveryEngineInfoExploreException {
		logger.info("Start to getRule with factType: " + factType + " and ruleId : " + ruleId);

		Fact ruleFact = getRuleFact(ids, factType, ruleId);
		logger.info("End to getRule()...");

		return ruleFact == null ? null : getRuleVOFromFact(ruleFact);
	}

	private static Fact getRuleFact(InfoDiscoverSpace ids, String factType, String ruleId)
			throws InfoDiscoveryEngineRuntimeException, InfoDiscoveryEngineInfoExploreException {
		logger.info("Start to getRuleFact with factType: " + factType + " and ruleId : " + ruleId);
		InformationExplorer ie = ids.getInformationExplorer();
		ExploreParameters ep = new ExploreParameters();
		ep.setType(factType);
		ep.setDefaultFilteringItem(new EqualFilteringItem(RuleEngineDatabaseConstants.FACT_ID, ruleId));

		logger.info("End to getRuleFact()...");
		return FactManager.getFact(ie, ep);
	}

	public static RuleVO getRuleVOFromFact(Fact ruleFact) {
		String ruleName = ruleFact.getProperty(RuleEngineDatabaseConstants.FACT_RULENAME).getPropertyValue().toString();
		String id = ruleFact.getProperty(RuleEngineDatabaseConstants.FACT_ID).getPropertyValue().toString();
		String ruleType = ruleFact.getProperty(RuleEngineDatabaseConstants.FACT_TYPE).getPropertyValue().toString();
		String description = ruleFact.getProperty(RuleEngineDatabaseConstants.FACT_DESCRIPTION).getPropertyValue().toString();
		String ruleContent = ruleFact.getProperty(RuleEngineDatabaseConstants.FACT_CONTENT).getPropertyValue().toString();

		return new RuleVO(id, ruleName, ruleType, description, ruleContent);
	}

	public static boolean checkRuleExistence(InfoDiscoverSpace ids, String factType, String ruleId)
			throws InfoDiscoveryEngineRuntimeException, InfoDiscoveryEngineInfoExploreException {
		return getRule(ids, factType, ruleId) == null ? false : true;
	}

	public static boolean deleteRule(InfoDiscoverSpace ids, String factType, String ruleId)
			throws InfoDiscoveryEngineRuntimeException, InfoDiscoveryEngineInfoExploreException {
		logger.info("Start to deleteRule with factType: " + factType + " and ruleId: " + ruleId);
		Fact rule = getRuleFact(ids, factType, ruleId);
		boolean result = false;
		if (rule != null) {
			result = FactManager.deleteFact(ids, rule.getId());
		}

		logger.info("End to deleteRule()...");
		return result;
	}

	public static boolean updateRule(InfoDiscoverSpace ids, String factType, RuleVO rule)
			throws InfoDiscoveryEngineRuntimeException, InfoDiscoveryEngineInfoExploreException {
		logger.info("Start to updateRule with ruleId: " + rule.getRuleId());
		Fact ruleFact = getRuleFact(ids, factType, rule.getRuleId());
		Fact fact = FactManager.updateFact(ids, ruleFact, convertRuleVOToMap(rule));
		logger.info("End to updateRule()...");
		return fact != null;
	}
	
}
