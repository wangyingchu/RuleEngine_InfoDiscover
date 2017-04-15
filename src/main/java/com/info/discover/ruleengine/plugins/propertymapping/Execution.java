package com.info.discover.ruleengine.plugins.propertymapping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.info.discover.ruleengine.manager.database.DataSpaceManager;
import com.info.discover.ruleengine.manager.database.RuleEngineDatabaseConstants;
import com.info.discover.ruleengine.manager.database.RuleEngineFactManager;
import com.infoDiscover.infoDiscoverEngine.infoDiscoverBureau.InfoDiscoverSpace;
import com.infoDiscover.infoDiscoverEngine.util.exception.InfoDiscoveryEngineDataMartException;
import com.infoDiscover.infoDiscoverEngine.util.exception.InfoDiscoveryEngineRuntimeException;

public class Execution {

	private final static Logger logger = LoggerFactory.getLogger(Execution.class);

	public static void initRuleEngine() {
		logger.info("Start to initialize RuleEngine...");
		// 1. create RuleEngine space
		logger.info("Start to create RuleEngine space: " + RuleEngineDatabaseConstants.RuleEngineSpace);
		if (DataSpaceManager.checkDataSapceExistence(RuleEngineDatabaseConstants.RuleEngineSpace)) {
			logger.info("RuleEngine space: " + RuleEngineDatabaseConstants.RuleEngineSpace + " is already existed");
		} else {
			boolean created = DataSpaceManager.createDataSpace(RuleEngineDatabaseConstants.RuleEngineSpace);
			if (created) {
				logger.info("End to create RuleEngine space...");
			}
		}
		
		// 2. create ID_FACT_RULE fact type with properties
		logger.info("Start to create ID_FACT_RULE table: " + RuleEngineDatabaseConstants.RuleFact);
		InfoDiscoverSpace ids = DataSpaceManager.getInfoDiscoverSpace();
		try {
			RuleEngineFactManager.createRuleFactType(ids);
		} catch (InfoDiscoveryEngineDataMartException e) {
			logger.error("Failed to create Rule Fact: " + RuleEngineDatabaseConstants.RuleFact);
			logger.equals("Error: " + e.getMessage());
		} catch (InfoDiscoveryEngineRuntimeException e) {
			logger.error("Failed to create Rule Fact: " + RuleEngineDatabaseConstants.RuleFact);
			logger.equals("Error: " + e.getMessage());
		} finally {
			ids.closeSpace();
		}
		
		logger.info("End to create ID_FACT_RULE table...");
	}

	public static void main(String[] args) {
		initRuleEngine();
	}

}
