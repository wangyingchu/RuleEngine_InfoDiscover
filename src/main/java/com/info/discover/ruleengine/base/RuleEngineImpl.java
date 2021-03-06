package com.info.discover.ruleengine.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.info.discover.ruleengine.base.vo.RuleVO;
import com.info.discover.ruleengine.manager.database.DataSpaceManager;
import com.info.discover.ruleengine.manager.database.RuleEngineDatabaseConstants;
import com.info.discover.ruleengine.manager.database.RuleEngineFactManager;
import com.infoDiscover.infoDiscoverEngine.infoDiscoverBureau.InfoDiscoverSpace;
import com.infoDiscover.infoDiscoverEngine.util.exception.InfoDiscoveryEngineInfoExploreException;
import com.infoDiscover.infoDiscoverEngine.util.exception.InfoDiscoveryEngineRuntimeException;

public class RuleEngineImpl implements RuleEngine {
	private final static Logger logger = LoggerFactory.getLogger(RuleEngineImpl.class);

	public boolean createRule(RuleVO rule) throws InfoDiscoveryEngineRuntimeException {

		if (rule == null) {
			logger.error("New Rule is null");
			throw new InfoDiscoveryEngineRuntimeException();
		}

		logger.info("Start to createRule() with ruleName: " + rule.getName());
		boolean result = false;
		InfoDiscoverSpace ids = DataSpaceManager.getRuleEngineInfoDiscoverSpace();
		if (ids != null) {
			result = RuleEngineFactManager.createRule(ids, rule);
		} else {
			logger.error("Failed to connect to database: " + RuleEngineDatabaseConstants.RuleEngineSpace);
		}

		ids.closeSpace();

		return result;
	}

	public boolean checkRuleExistence(String ruleId) {
		InfoDiscoverSpace ids = DataSpaceManager.getRuleEngineInfoDiscoverSpace();
		try {
			return RuleEngineFactManager.checkRuleExistence(ids, RuleEngineDatabaseConstants.RuleFact, ruleId);
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
		InfoDiscoverSpace ids = DataSpaceManager.getRuleEngineInfoDiscoverSpace();
		boolean result = false;
		try {
			result = RuleEngineFactManager.deleteRule(ids, RuleEngineDatabaseConstants.RuleFact, ruleId);
		} catch (InfoDiscoveryEngineRuntimeException e) {
			logger.error("Failed to delete rule: " + e.getMessage());
		} catch (InfoDiscoveryEngineInfoExploreException e) {
			logger.error("Failed to delete rule: " + e.getMessage());
		} finally {
			ids.closeSpace();
		}
		return result;
	}

	@Override
	public boolean updateRule(RuleVO rule) {
		InfoDiscoverSpace ids = DataSpaceManager.getRuleEngineInfoDiscoverSpace();
		try {
			return RuleEngineFactManager.updateRule(ids, RuleEngineDatabaseConstants.RuleFact, rule);
		} catch (InfoDiscoveryEngineRuntimeException e) {
			logger.error("Failed to update rule: " + e.getMessage());
		} catch (InfoDiscoveryEngineInfoExploreException e) {
			logger.error("Failed to update rule: " + e.getMessage());
		} finally {
			ids.closeSpace();
		}

		return false;
	}

	@Override
	public RuleVO getRule(String ruleId) {
		InfoDiscoverSpace ids = DataSpaceManager.getRuleEngineInfoDiscoverSpace();
		try {
			return RuleEngineFactManager.getActiveRule(ids, RuleEngineDatabaseConstants.RuleFact, ruleId);
		} catch (InfoDiscoveryEngineRuntimeException e) {
			logger.error("Failed to get rule: " + e.getMessage());
		} catch (InfoDiscoveryEngineInfoExploreException e) {
			logger.error("Failed to get rule: " + e.getMessage());
		} finally {
			ids.closeSpace();
		}
		return null;
	}

	@Override
	public boolean checkRuleEngineDataspaceExistence() {
		return DataSpaceManager.checkDataSapceExistence(RuleEngineDatabaseConstants.RuleEngineSpace);
	}

	@Override
	public boolean deleteRule(String ruleId, boolean softDelete) {
		InfoDiscoverSpace ids = DataSpaceManager.getRuleEngineInfoDiscoverSpace();
		try {
			RuleVO rule = RuleEngineFactManager.getActiveRule(ids, RuleEngineDatabaseConstants.RuleFact, ruleId);
			rule.setDeleted(true);
			return RuleEngineFactManager.updateRule(ids, RuleEngineDatabaseConstants.RuleFact, rule);
		} catch (InfoDiscoveryEngineRuntimeException e) {
			logger.error("Failed to get rule: " + e.getMessage());
		} catch (InfoDiscoveryEngineInfoExploreException e) {
			logger.error("Failed to get rule: " + e.getMessage());
		}
		return false;
	}

}
