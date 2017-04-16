package com.info.discover.ruleengine.plugins.propertymapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.info.discover.drools.DroolsExecutor;
import com.info.discover.drools.rules.propertyMapping.PropertyMappingOutput;
import com.info.discover.drools.rules.propertyMapping.PropertyMappingRuleConstants;
import com.info.discover.ruleengine.base.RuleEngineImpl;
import com.info.discover.ruleengine.base.vo.RuleVO;
import com.info.discover.ruleengine.manager.database.DataSpaceManager;
import com.info.discover.ruleengine.manager.database.FactManager;
import com.info.discover.ruleengine.manager.database.RelationManager;
import com.info.discover.ruleengine.util.RuleHelper;
import com.infoDiscover.infoDiscoverEngine.dataMart.Dimension;
import com.infoDiscover.infoDiscoverEngine.dataMart.Fact;
import com.infoDiscover.infoDiscoverEngine.dataWarehouse.ExploreParameters;
import com.infoDiscover.infoDiscoverEngine.dataWarehouse.InformationExplorer;
import com.infoDiscover.infoDiscoverEngine.dataWarehouse.InformationFiltering.InValueFilteringItem;
import com.infoDiscover.infoDiscoverEngine.infoDiscoverBureau.InfoDiscoverSpace;
import com.infoDiscover.infoDiscoverEngine.util.exception.InfoDiscoveryEngineDataMartException;
import com.infoDiscover.infoDiscoverEngine.util.exception.InfoDiscoveryEngineInfoExploreException;
import com.infoDiscover.infoDiscoverEngine.util.exception.InfoDiscoveryEngineRuntimeException;
import com.infoDiscover.infoDiscoverEngine.util.factory.DiscoverEngineComponentFactory;

public class PropertyMappingRuleEngineImpl extends RuleEngineImpl implements PropertyMappingRuleEngine {
	private final static Logger logger = LoggerFactory.getLogger(PropertyMappingRuleEngineImpl.class);

	public String getRuleId(String spaceName, String factType) {
		return spaceName + "_" + factType;
	}

	public Map<String, List<Dimension>> executeRule(String ruleId) {
		logger.info("Start to executeRule: {}", ruleId);
		RuleVO rule = getRule(ruleId);
		if (rule != null) {
			return executeRule(rule);
		}
		return null;
	}

	public Map<String, List<Dimension>> executeRule(RuleVO rule) {
		logger.info("Start to executeRule with ruleId: {}", rule.getRuleId());

		Map<String, List<Dimension>> mappingDimensions = new HashMap<String, List<Dimension>>();

		if (rule != null) {
			RuleContentVO ruleContent = new RuleContentVO(rule.getContent());
			ruleContent.setSourceInput();
			ruleContent.setTargetInput();
			ruleContent.setSpaceName();

			PropertyMappingOutput output = DroolsExecutor.executeRule(
					PropertyMappingRuleConstants.PROPERTY_MAPPING_RULE_NAME, ruleContent.getSourceInput(),
					ruleContent.getTargetInput());

			// 1. get all the dimensions' names
			String[] dimensionNames = RuleHelper.getAllDimensionNames(ruleContent.getSpaceName(),
					output.getTargetType());

			// 2. loop through the names to find the ones that included in
			// Fact's name or Fact's
			// description
			

			String factTypeName = ruleContent.getSourceInput().getSourceType();
			InfoDiscoverSpace ids = DataSpaceManager.getInfoDiscoverSpace(ruleContent.getSpaceName());
			InformationExplorer ie = ids.getInformationExplorer();
			ExploreParameters ep = new ExploreParameters();
			ep.setType(factTypeName);

			List<Fact> facts = new ArrayList<Fact>();
			try {
				facts = FactManager.getFacts(ie, ep);
				if (facts != null && facts.size() > 0) {
					for (Fact fact : facts) {
						List<Object> matchedDimensionNamesList = new ArrayList<Object>();
						for (String name : dimensionNames) {
							String matchedName = findMatchedDimensionName(fact, output.getSourceProperties(), name);
							if (matchedName != null) {
								matchedDimensionNamesList.add(matchedName);
							}
						}

						// 3. get the matched dimension list via the dimension
						// name list
						List<Dimension> dimensionList = getMatchedDimensionList(ruleContent.getSpaceName(),
								output.getTargetType(), output.getTargetProperty(), matchedDimensionNamesList);

						mappingDimensions.put(fact.getId(), dimensionList);
					}
				}
			} catch (InfoDiscoveryEngineRuntimeException e) {
				logger.error("Error occurs during rule execution: " + e.getMessage());
			} catch (InfoDiscoveryEngineInfoExploreException e) {
				logger.error("Error occurs during rule execution: " + e.getMessage());
			} finally {
				ids.closeSpace();
			}

		}

		return mappingDimensions;
	}

	public List<Dimension> executeRule(String spaceName, Fact fact) {
		logger.info("Start to executeRule with spaceName: {} and fact: {}", spaceName, fact);

		String ruleId = getRuleId(spaceName, fact.getType());

		RuleContentVO ruleContent = new RuleContentVO(getRuleContent(ruleId));
		ruleContent.setSourceInput();
		ruleContent.setTargetInput();
		ruleContent.setSpaceName();

		PropertyMappingOutput output = DroolsExecutor.executeRule(
				PropertyMappingRuleConstants.PROPERTY_MAPPING_RULE_NAME, ruleContent.getSourceInput(),
				ruleContent.getTargetInput());

		// 1. get all the dimensions' names
		String[] dimensionNames = RuleHelper.getAllDimensionNames(spaceName, output.getTargetType());

		// 2. loop through the names to find the ones that included in
		// Fact's name or Fact's
		// description
		List<Object> matchedDimensionNamesList = new ArrayList<Object>();

		for (String name : dimensionNames) {
			String matchedName = findMatchedDimensionName(fact, output.getSourceProperties(), name);
			if (matchedName != null) {
				matchedDimensionNamesList.add(matchedName);
			}
		}

		// 3. get the matched dimension list via the dimension name list
		List<Dimension> dimensionList = getMatchedDimensionList(spaceName, output.getTargetType(),
				output.getTargetProperty(), matchedDimensionNamesList);

		logger.info("End to execute rule");
		return dimensionList;
	}

	private List<Dimension> getMatchedDimensionList(String spaceName, String dimensionType, String propertyName,
			List<Object> names) {
		logger.info(
				"Start to getMatchedDimensionList with spaceName: {} and dimensionType: {} and propertyName: {} and names: {}",
				spaceName, dimensionType, propertyName, names);

		InfoDiscoverSpace ids = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
		InformationExplorer ie = ids.getInformationExplorer();
		ExploreParameters ep = new ExploreParameters();
		ep.setType(dimensionType);

		ep.setDefaultFilteringItem(new InValueFilteringItem(propertyName, names));

		try {
			List<Dimension> dimensionList = ie.discoverDimensions(ep);
			return dimensionList;
		} catch (InfoDiscoveryEngineRuntimeException e) {
			logger.error("Failed to get matched dimensions: " + e.getMessage());
		} catch (InfoDiscoveryEngineInfoExploreException e) {
			logger.error("Failed to get matched dimensions: " + e.getMessage());
		} finally {
			ids.closeSpace();
		}

		logger.info("End to getMatchedDimensionList()...");
		return null;
	}

	private String findMatchedDimensionName(Fact fact, String[] sourceProperties, String dimensionName) {

		boolean matched = false;
		for (String propertyName : sourceProperties) {
			if (fact.getProperty(propertyName.trim()) != null) {
				Object propertyValue = fact.getProperty(propertyName.trim()).getPropertyValue();
				if (propertyValue != null) {
					if (!matched && propertyValue.toString().indexOf(dimensionName) != -1) {
						matched = true;
						break;
					}
				}
			}
		}

		return matched == true ? dimensionName : null;
	}

	private List<Dimension> removeParentDimensions(List<Dimension> dimensionList) {
		return null;
	}

	public void linkFactToDimensionsByRelationType(InfoDiscoverSpace ids, Fact fact,
			List<Dimension> dimensionList, String relationType)
			throws InfoDiscoveryEngineDataMartException, InfoDiscoveryEngineRuntimeException {
		logger.info(
				"Start to link fact to dimension with relation, fact: {}, dimension: {} and relationType: {}",
				 fact.getType(), dimensionList, relationType);

		if (ids != null) {
			RelationManager.linkFactToDimensionsByRelationType(ids, fact, dimensionList, relationType);
		}

		logger.info("End to linkFactToDimensionByRelationType()...");
	}

	public void linkFactToDimensionsByRelationType(InfoDiscoverSpace ids, String factId,
			List<Dimension> dimensionList, String relationType)
			throws InfoDiscoveryEngineDataMartException, InfoDiscoveryEngineRuntimeException {
		logger.info(
				"Start to link fact to dimension with relation, fact: {}, dimension: {} and relationType: {}",
				factId, dimensionList, relationType);
		if (ids != null) {
			RelationManager.linkFactToDimensionsByRelationType(ids, factId, dimensionList, relationType);
		}

		logger.info("End to linkFactToDimensionByRelationType()...");
	}

	public String getRuleContent(String ruleId) {
		logger.info("Start to getRuleContent with ruleId: {}", ruleId);
		RuleVO rule = getRule(ruleId);
		logger.info("End to getRuleContent");
		return rule == null ? null : rule.getContent();
	}

}
