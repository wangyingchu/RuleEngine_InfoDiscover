package com.info.discover.ruleengine.plugins.propertymapping;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.info.discover.drools.DroolsExecutor;
import com.info.discover.drools.rules.propertyMapping.PropertyMappingOutput;
import com.info.discover.drools.rules.propertyMapping.PropertyMappingRuleConstants;
import com.info.discover.ruleengine.base.RuleEngineImpl;
import com.info.discover.ruleengine.base.vo.RuleVO;
import com.info.discover.ruleengine.util.RuleHelper;
import com.infoDiscover.infoDiscoverEngine.dataMart.Dimension;
import com.infoDiscover.infoDiscoverEngine.dataMart.Fact;
import com.infoDiscover.infoDiscoverEngine.dataMart.Relation;
import com.infoDiscover.infoDiscoverEngine.dataWarehouse.ExploreParameters;
import com.infoDiscover.infoDiscoverEngine.dataWarehouse.InformationExplorer;
import com.infoDiscover.infoDiscoverEngine.dataWarehouse.InformationFiltering.InValueFilteringItem;
import com.infoDiscover.infoDiscoverEngine.infoDiscoverBureau.InfoDiscoverSpace;
import com.infoDiscover.infoDiscoverEngine.util.exception.InfoDiscoveryEngineInfoExploreException;
import com.infoDiscover.infoDiscoverEngine.util.exception.InfoDiscoveryEngineRuntimeException;
import com.infoDiscover.infoDiscoverEngine.util.factory.DiscoverEngineComponentFactory;

public class PropertyMappingRuleEngineImpl extends RuleEngineImpl implements PropertyMappingRuleEngine {
	private final static Logger logger = LoggerFactory.getLogger(PropertyMappingRuleEngineImpl.class);

	public String getRuleId(String spaceName, String factType) {
		return spaceName + "_" + factType;
	}
	
	public List<Dimension> executeRule(String spaceName, Fact fact) {
		logger.info("Start to executeRule with spaceName: {} and fact: {}", spaceName, fact);

		String ruleId = getRuleId(spaceName, fact.getType());

		RuleContentVO ruleContent = new RuleContentVO(getRuleContent(ruleId));
		ruleContent.setSourceInput();
		ruleContent.setTargetInput();

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
		InfoDiscoverSpace ids = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
		InformationExplorer ie = ids.getInformationExplorer();
		ExploreParameters ep = new ExploreParameters();
		ep.setType(dimensionType);

		ep.setDefaultFilteringItem(new InValueFilteringItem(propertyName, names));

		try {
			List<Dimension> dimensionList = ie.discoverDimensions(ep);
			return dimensionList;
		} catch (InfoDiscoveryEngineRuntimeException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (InfoDiscoveryEngineInfoExploreException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		ids.closeSpace();

		return null;
	}

	private String findMatchedDimensionName(Fact fact, String[] sourceProperties, String dimensionName) {

		boolean matched = false;
		for (String propertyName : sourceProperties) {
			Object propertyValue = fact.getProperty(propertyName.trim()).getPropertyValue();
			if (propertyValue != null) {
				if (!matched && propertyValue.toString().indexOf(dimensionName) != -1) {
					matched = true;
					break;
				}
			}
		}

		return matched == true ? dimensionName : null;
	}

	private List<Dimension> removeParentDimensions(List<Dimension> dimensionList) {
		return null;
	}

	public void connectFactDimensionWithRelation(Fact fact, Dimension dimension, Relation relation) {

	}

	public String getRuleContent(String ruleId) {
		logger.info("Start to getRuleContent with ruleId: {}", ruleId);
		RuleVO rule = new PropertyMappingRuleEngineImpl().getRule(ruleId);
		logger.info("End to getRuleContent");
		return rule == null ? null : rule.getContent();
	}

}
