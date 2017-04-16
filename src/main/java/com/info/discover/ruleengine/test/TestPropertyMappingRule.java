package com.info.discover.ruleengine.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.info.discover.ruleengine.base.vo.RuleVO;
import com.info.discover.ruleengine.manager.database.RuleEngineDatabaseConstants;
import com.info.discover.ruleengine.plugins.propertymapping.PropertyMappingConstants;
import com.info.discover.ruleengine.plugins.propertymapping.PropertyMappingRuleEngineImpl;
import com.infoDiscover.common.util.JsonUtil;
import com.infoDiscover.infoDiscoverEngine.dataMart.Dimension;
import com.infoDiscover.infoDiscoverEngine.dataMart.Fact;
import com.infoDiscover.infoDiscoverEngine.dataWarehouse.ExploreParameters;
import com.infoDiscover.infoDiscoverEngine.dataWarehouse.InformationExplorer;
import com.infoDiscover.infoDiscoverEngine.dataWarehouse.InformationFiltering.EqualFilteringItem;
import com.infoDiscover.infoDiscoverEngine.infoDiscoverBureau.InfoDiscoverSpace;
import com.infoDiscover.infoDiscoverEngine.util.exception.InfoDiscoveryEngineInfoExploreException;
import com.infoDiscover.infoDiscoverEngine.util.exception.InfoDiscoveryEngineRuntimeException;
import com.infoDiscover.infoDiscoverEngine.util.factory.DiscoverEngineComponentFactory;

public class TestPropertyMappingRule {
    // TODO: should remove to unit tests
    public static void main(String[] args) {
        PropertyMappingRuleEngineImpl engine = new PropertyMappingRuleEngineImpl();
        String ruleName = "ID_FACT_factTest";
        String description = "test rule description";

        //addRule(ruleName,description);
        
        String ruleContent = engine.getRuleContent(ruleName);
//        System.out.println(ruleContent);

//        verifyRuleContentIsJson(ruleContent);

        // add a fact test data
        TestPropertyMappingRule test = new TestPropertyMappingRule();
        test.addFactTestData("factTest", "animal", "this is a image");

        // add a dimension test data
        //engine.addDimensionTestData("dimensionTest", "image", null);

        Fact fact = test.getTestFact("factTest","animal");
        System.out.println(fact.getProperty("description").getPropertyValue().toString());

        List<Dimension> dimensionList = engine.executeRule("TestData", fact);
        for(Dimension dimension: dimensionList) {
        	System.out.println("Dimension rid: " + dimension.getId());
        }
    }

    private Fact getTestFact(String factType, String name) {
        InfoDiscoverSpace ids = DiscoverEngineComponentFactory.connectInfoDiscoverSpace
                ("TestData");
        InformationExplorer ie = ids.getInformationExplorer();
        ExploreParameters ep = new ExploreParameters();
        ep.setType(factType);

        ep.setDefaultFilteringItem(new EqualFilteringItem("name", name));

        try {
            List<Fact> factList = ie.discoverFacts(ep);
            return factList.get(0);
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

    private static void verifyRuleContentIsJson(String ruleContent) {
        String sourceProperties = JsonUtil.getPropertyValues(PropertyMappingConstants
                .JSON_SOURCE_PROPERTIES,ruleContent);
        String targetType = JsonUtil.getPropertyValues(PropertyMappingConstants.JSON_TARGET_TYPE, ruleContent);
        String targetProperty = JsonUtil.getPropertyValues(PropertyMappingConstants.JSON_TARGET_PROPERTY,
                ruleContent);

        System.out.println("sourceProperties: " + sourceProperties);
        System.out.println("targetType: " + targetType);
        System.out.println("targetProperty: " + targetProperty);
    }

    private static void addRule(String ruleName, String description) throws InfoDiscoveryEngineRuntimeException {
        PropertyMappingRuleEngineImpl engine = new PropertyMappingRuleEngineImpl();

        HashMap<String, String> ruleContentMap = new HashMap<String, String>();
        ruleContentMap.put("source", "Fact");
        ruleContentMap.put("sourceType", "factTest");
        ruleContentMap.put("sourceProperties", "name,description");
        ruleContentMap.put("target", "Dimension");
        ruleContentMap.put("targetType", "dimensionTest");
        ruleContentMap.put("targetProperty", "name");
        ruleContentMap.put("spaceName", "TestData");

        String ruleContent = JsonUtil.mapToJsonStr(ruleContentMap);
        System.out.println("ruleContent: " + ruleContent);

        RuleVO rule = new RuleVO("ID_FACT_factTest", "ruleForFactTest","PropertyMapping", "desc", ruleContent, false);
        engine.createRule(rule);
    }

    private void addFactTestData(String factType, String name, String description) {
        InfoDiscoverSpace ids = DiscoverEngineComponentFactory.connectInfoDiscoverSpace
                ("TestData");
        if (ids != null) {
            Fact fact = DiscoverEngineComponentFactory.createFact(factType);
            try {
                fact = ids.addFact(fact);

                Map<String, Object> props = new HashMap<String, Object>();
                props.put("name", name);
                props.put("description", description);

                fact.addProperties(props);

            } catch (InfoDiscoveryEngineRuntimeException e) {
                System.out.println(e.getMessage());
                System.out.println("Failed to insert values to fact: " + RuleEngineDatabaseConstants.RuleFact);
            }
        } else {
            System.out.println("Failed to connect to database: " + RuleEngineDatabaseConstants.RuleEngineSpace);
        }

        ids.closeSpace();
    }

    private void addDimensionTestData(String dimensionType, String name, String description) {
        InfoDiscoverSpace ids = DiscoverEngineComponentFactory.connectInfoDiscoverSpace
                ("TestData");
        if (ids != null) {
            Dimension dimension = DiscoverEngineComponentFactory.createDimension(dimensionType);
            try {
                dimension = ids.addDimension(dimension);

                Map<String, Object> props = new HashMap<String, Object>();
                props.put("name", name);
                props.put("description", description);

                dimension.addProperties(props);

            } catch (InfoDiscoveryEngineRuntimeException e) {
                System.out.println(e.getMessage());
                System.out.println("Failed to insert values to fact: " + RuleEngineDatabaseConstants.RuleFact);
            }
        } else {
            System.out.println("Failed to connect to database: " + RuleEngineDatabaseConstants.RuleEngineSpace);
        }

        ids.closeSpace();
    }
}
