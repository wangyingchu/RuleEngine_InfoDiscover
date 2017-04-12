package com.info.discover.ruleengine.plugins.propertymapping;

import java.util.ArrayList;
import java.util.List;

import com.info.discover.drools.DroolsExecutor;
import com.info.discover.drools.rules.propertyMapping.PropertyMappingOutput;
import com.info.discover.drools.rules.propertyMapping.PropertyMappingRuleConstants;
import com.info.discover.ruleengine.base.RuleConstants;
import com.info.discover.ruleengine.base.RuleEngineImpl;
import com.info.discover.ruleengine.util.RuleHelper;
import com.infoDiscover.infoDiscoverEngine.dataMart.Dimension;
import com.infoDiscover.infoDiscoverEngine.dataMart.Fact;
import com.infoDiscover.infoDiscoverEngine.dataMart.Relation;
import com.infoDiscover.infoDiscoverEngine.dataWarehouse.ExploreParameters;
import com.infoDiscover.infoDiscoverEngine.dataWarehouse.InformationExplorer;
import com.infoDiscover.infoDiscoverEngine.dataWarehouse.InformationFiltering.EqualFilteringItem;
import com.infoDiscover.infoDiscoverEngine.dataWarehouse.InformationFiltering.InValueFilteringItem;
import com.infoDiscover.infoDiscoverEngine.infoDiscoverBureau.InfoDiscoverSpace;
import com.infoDiscover.infoDiscoverEngine.util.InfoDiscoverEngineConstant;
import com.infoDiscover.infoDiscoverEngine.util.exception.InfoDiscoveryEngineInfoExploreException;
import com.infoDiscover.infoDiscoverEngine.util.exception.InfoDiscoveryEngineRuntimeException;
import com.infoDiscover.infoDiscoverEngine.util.factory.DiscoverEngineComponentFactory;

public class PropertyMappingRuleEngineImpl extends RuleEngineImpl implements PropertyMappingRuleEngine {
	
	public List<Dimension> executeRule(String spaceName, Fact fact) {
		String ruleName = InfoDiscoverEngineConstant.CLASSPERFIX_FACT + fact.getType();

		RuleContent ruleContent = new RuleContent(getRuleContent(ruleName));
		ruleContent.setSourceInput();
		ruleContent.setTargetInput();
		
		PropertyMappingOutput output = DroolsExecutor.executeRule(PropertyMappingRuleConstants.PROPERTY_MAPPING_RULE_NAME,
				ruleContent.getSourceInput(), ruleContent.getTargetInput());

		// 1. get all the dimensions' names
		String[] dimensionNames = RuleHelper.getAllDimensionNames(spaceName,output.getTargetType());

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
		List<Dimension> dimensionList = getMatchedDimensionList(spaceName, output.getTargetType(), output.getTargetProperty(),
				matchedDimensionNamesList);

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
			String properValue = fact.getProperty(propertyName).getPropertyValue().toString();
			if (!matched && properValue.indexOf(dimensionName) != -1) {
				matched = true;
				break;
			}
		}

		return matched == true ? dimensionName : null;
	}

	private List<Dimension> removeParentDimensions(List<Dimension> dimensionList) {
		return null;
	}

	public void connectFactDimensionWithRelation(Fact fact, Dimension dimension, Relation relation) {

	}

	public String getRuleContent(String ruleName) {
		String content = null;

		InfoDiscoverSpace ids = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(RuleConstants.RuleEngineSpace);

		InformationExplorer ie = ids.getInformationExplorer();
		ExploreParameters ep = new ExploreParameters();
		ep.setType(RuleConstants.RuleFact);

		ep.setDefaultFilteringItem(new EqualFilteringItem(RuleConstants.FACT_NAME, ruleName));
		try {
			List<Fact> factList = ie.discoverFacts(ep);
			content = factList.get(0).getProperty(RuleConstants.FACT_CONTENT).getPropertyValue().toString();
		} catch (InfoDiscoveryEngineRuntimeException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (InfoDiscoveryEngineInfoExploreException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		ids.closeSpace();

		return content;
	}

}
