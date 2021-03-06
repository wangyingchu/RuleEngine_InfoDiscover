package com.info.discover.ruleengine.plugins.propertymapping.sample;

public class SampleRuleConstants {

	// fact type name
	public final static String FACT_TYPE = "SampleFact";
	public final static String FACT_PROPERTY_NAME = "name";
	public final static String FACT_PROPERTY_DESCRIPTION = "description";
	public final static String FACT_PROPERTY_KEYWORDS = "keywords";
	
	
	// dimension type name
	public final static String DIMENSION_TYPE = "SampleDimension";
	public final static String DIMENSION_PROPERTY_NAME = "name";
	public final static String DIMENSION_PROPERTY_DESCRIPTION = "description";
	public final static String DIMENSION_PROPERTY_KEYWORDS = "keywords";
	
	// relation type name
	public final static String RELATION_TYPE = "HASSAMPLEDIMENSION";
	
	// file template
	public final static String SAMPLE_RULE_FACT_TEMPLATE = "SampleRuleFactTemplate.json";
	public final static String SAMPLE_RULE_DIMENSION_TEMPLATE = "SampleRuleDimensionTemplate.json";
}
