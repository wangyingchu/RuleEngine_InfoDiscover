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
		properties.put(DatabaseConstants.FACT_RULENAME, PropertyType.STRING);
		properties.put(DatabaseConstants.FACT_ID, PropertyType.STRING);
		properties.put(DatabaseConstants.FACT_TYPE, PropertyType.STRING);
		properties.put(DatabaseConstants.FACT_DESCRIPTION, PropertyType.STRING);
		properties.put(DatabaseConstants.FACT_CONTENT, PropertyType.STRING);

		FactManager.createFactType(ids, DatabaseConstants.RuleFact, properties);
		logger.info("End to createRuleFactType()...");
	}

	public static void createRule(InfoDiscoverSpace ids, RuleVO rule) throws InfoDiscoveryEngineRuntimeException {
		logger.info("Start to createRuleFact with properties");
		Map<String, Object> properties = convertRuleVOToMap(rule);
		FactManager.createFact(ids, rule.getId(), properties);
		logger.info("End to createRuleFact()...");
	}

	private static Map<String, Object> convertRuleVOToMap(RuleVO rule) {
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(DatabaseConstants.FACT_RULENAME, rule.getName());
		properties.put(DatabaseConstants.FACT_ID, rule.getId());
		properties.put(DatabaseConstants.FACT_TYPE, rule.getType());
		properties.put(DatabaseConstants.FACT_DESCRIPTION, rule.getDescription());
		properties.put(DatabaseConstants.FACT_CONTENT, rule.getContent());
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
		ep.setDefaultFilteringItem(new EqualFilteringItem(DatabaseConstants.FACT_ID, ruleId));

		logger.info("End to getRuleFact()...");
		return FactManager.getFact(ie, ep);
	}

	private static RuleVO getRuleVOFromFact(Fact ruleFact) {
		String ruleName = ruleFact.getProperty(DatabaseConstants.FACT_RULENAME).toString();
		String id = ruleFact.getProperty(DatabaseConstants.FACT_ID).toString();
		String ruleType = ruleFact.getProperty(DatabaseConstants.FACT_TYPE).toString();
		String description = ruleFact.getProperty(DatabaseConstants.FACT_DESCRIPTION).toString();
		String ruleContent = ruleFact.getProperty(DatabaseConstants.FACT_CONTENT).toString();

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

	public static void updateRule(InfoDiscoverSpace ids, String factType, RuleVO rule)
			throws InfoDiscoveryEngineRuntimeException, InfoDiscoveryEngineInfoExploreException {
		logger.info("Start to updateRule with ruleId: " + rule.getId());
		Fact ruleFact = getRuleFact(ids, factType, rule.getId());
		FactManager.updateFact(ids, ruleFact, convertRuleVOToMap(rule));
		logger.info("End to updateRule()...");
	}
}
