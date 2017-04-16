package com.info.discover.ruleengine.plugins.propertymapping.sample.data;

import java.util.HashMap;
import java.util.Map;

import com.info.discover.drools.rules.propertyMapping.PropertyMappingRuleConstants;
import com.info.discover.ruleengine.base.vo.RuleVO;
import com.info.discover.ruleengine.manager.database.RuleEngineDatabaseConstants;
import com.info.discover.ruleengine.plugins.propertymapping.PropertyMappingConstants;
import com.info.discover.ruleengine.plugins.propertymapping.PropertyMappingRuleEngineImpl;
import com.info.discover.ruleengine.plugins.propertymapping.sample.SampleRuleConstants;
import com.infoDiscover.common.util.JsonUtil;
import com.infoDiscover.infoDiscoverEngine.util.exception.InfoDiscoveryEngineRuntimeException;

public class SampleRuleData {
	private static String ruleId = SampleRuleConstants.FACT_TYPE;
	private static String name = "属性匹配规则样例";
	private static String type = PropertyMappingRuleConstants.PROPERTY_MAPPING_RULE_NAME;
	private static String description = "这是一个用来演示属性匹配规则的样例，执行此规则后，根据定义的规则，可以从多个维度来观察和分析样例的事实表数据。";
	private static boolean deleted = false;
	
	public static boolean generateSampleRule() throws InfoDiscoveryEngineRuntimeException {
		RuleVO rule = new RuleVO(ruleId, name, type, description, generateSampleRuleConstent(), deleted);
		return new PropertyMappingRuleEngineImpl().createRule(rule);
	}

	public static boolean checkSampleRuleExistenct() {
		return new PropertyMappingRuleEngineImpl().checkRuleExistence(ruleId);
	}
	
	public static String generateSampleRuleConstent() {
		Map<String, Object> properties = generateSampleRuleContentProperties();
		return JsonUtil.mapToJsonStr(properties);
	}

	public static Map<String, Object> generateSampleRuleContentProperties() {
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(PropertyMappingConstants.JSON_SPACE, RuleEngineDatabaseConstants.RuleEngineSpace);
//		properties.put(PropertyMappingConstants.JSON_SOURCE, "Fact");
		properties.put(PropertyMappingConstants.JSON_SOURCE_TYPE, SampleRuleConstants.FACT_TYPE);
		properties.put(PropertyMappingConstants.JSON_SOURCE_PROPERTIES, "[name, description]");
//		properties.put(PropertyMappingConstants.JSON_TARGET, "Dimension");
		properties.put(PropertyMappingConstants.JSON_TARGET_TYPE, SampleRuleConstants.DIMENSION_TYPE);
		properties.put(PropertyMappingConstants.JSON_TARGET_PROPERTY, "name");
		return properties;
	}
}
