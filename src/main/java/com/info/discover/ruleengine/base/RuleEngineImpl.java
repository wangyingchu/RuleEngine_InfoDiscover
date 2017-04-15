package com.info.discover.ruleengine.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.info.discover.ruleengine.base.vo.RuleVO;
import com.info.discover.ruleengine.manager.database.DataSpaceManager;
import com.info.discover.ruleengine.manager.database.DatabaseConstants;
import com.info.discover.ruleengine.manager.database.RuleEngineFactManager;
import com.infoDiscover.infoDiscoverEngine.infoDiscoverBureau.InfoDiscoverSpace;
import com.infoDiscover.infoDiscoverEngine.util.exception.InfoDiscoveryEngineInfoExploreException;
import com.infoDiscover.infoDiscoverEngine.util.exception.InfoDiscoveryEngineRuntimeException;

public class RuleEngineImpl implements RuleEngine {
	private final static Logger logger = LoggerFactory.getLogger(RuleEngineImpl.class);

	public void createRule(RuleVO rule) throws InfoDiscoveryEngineRuntimeException {

		if (rule == null) {
			logger.error("New Rule is null");
			throw new InfoDiscoveryEngineRuntimeException();
		}

		logger.info("Start to createRule() with ruleName: " + rule.getName());

		InfoDiscoverSpace ids = DataSpaceManager.getInfoDiscoverSpace();
		if (ids != null) {
			RuleEngineFactManager.createRule(ids, rule);
		} else {
			logger.error("Failed to connect to database: " + DatabaseConstants.RuleEngineSpace);
		}

		ids.closeSpace();

	}

	public boolean checkRuleExistence(String ruleId) {
		InfoDiscoverSpace ids = DataSpaceManager.getInfoDiscoverSpace();
		try {
			return RuleEngineFactManager.checkRuleExistence(ids, DatabaseConstants.RuleFact, ruleId);
		} catch (InfoDiscoveryEngineRuntimeException e) {
			logger.error("Failed to check rule existence: " + e.getMessage());
		} catch (InfoDiscoveryEngineInfoExploreException e) {
			logger.error("Failed to check rule existence: " + e.getMessage());
		} finally {
			ids.closeSpace();
		}

		return false;
	}

	@Override
	public boolean deleteRule(String ruleId) {
		InfoDiscoverSpace ids = DataSpaceManager.getInfoDiscoverSpace();
		boolean result = false;
		try {
			result = RuleEngineFactManager.deleteRule(ids, DatabaseConstants.RuleFact, ruleId);
		} catch (InfoDiscoveryEngineRuntimeException | InfoDiscoveryEngineInfoExploreException e) {
			logger.error("Failed to delete rule: " + e.getMessage());
		} finally {
			ids.closeSpace();
		}
		return result;
	}

	@Override
	public void updateRule(RuleVO rule) {
		InfoDiscoverSpace ids = DataSpaceManager.getInfoDiscoverSpace();
		try {
			RuleEngineFactManager.updateRule(ids, DatabaseConstants.RuleFact, rule);
		} catch (InfoDiscoveryEngineRuntimeException | InfoDiscoveryEngineInfoExploreException e) {
			logger.error("Failed to update rule: " + e.getMessage());
		}
	}

}
