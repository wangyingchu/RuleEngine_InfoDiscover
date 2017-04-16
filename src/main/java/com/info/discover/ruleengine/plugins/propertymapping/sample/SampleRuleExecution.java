package com.info.discover.ruleengine.plugins.propertymapping.sample;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.info.discover.ruleengine.manager.database.DataSpaceManager;
import com.info.discover.ruleengine.manager.database.DimensionManager;
import com.info.discover.ruleengine.manager.database.FactManager;
import com.info.discover.ruleengine.manager.database.RelationManager;
import com.info.discover.ruleengine.plugins.propertymapping.sample.data.SampleDimensionData;
import com.info.discover.ruleengine.plugins.propertymapping.sample.data.SampleFactData;
import com.info.discover.ruleengine.plugins.propertymapping.sample.data.SampleRuleData;
import com.infoDiscover.infoDiscoverEngine.dataMart.PropertyType;
import com.infoDiscover.infoDiscoverEngine.infoDiscoverBureau.InfoDiscoverSpace;
import com.infoDiscover.infoDiscoverEngine.util.exception.InfoDiscoveryEngineDataMartException;
import com.infoDiscover.infoDiscoverEngine.util.exception.InfoDiscoveryEngineRuntimeException;

public class SampleRuleExecution {
	private final static Logger logger = LoggerFactory.getLogger(SampleRuleExecution.class);

	public static void main(String[] args) {
		// 1. prepare sample Fact type and dimension type
		prepareSampleType();

		// 2. prepare sample data
		prepareSampleData();

		// 3. create a sample rule
		prepareSampleRule();
		// 4. to reset the sample data
	}

	public static void prepareSampleRule() {
		if (!SampleRuleData.checkSampleRuleExistenct()) {
			try {
				SampleRuleData.generateSampleRule();
			} catch (InfoDiscoveryEngineRuntimeException e) {
				e.printStackTrace();
			}
		}
	}

	public static void prepareSampleType() {
		prepareFactType();
		prepareDimensionType();
		prepareRelationType();
	}

	public static void prepareSampleData() {
		prepareFactData();
		prepareDimensionData();
	}

	public static void prepareFactData() {
		SampleFactData.generateSampleRuleFact();
	}

	public static void prepareDimensionData() {
		if (!SampleDimensionData.checkDimensionIsCreate(SampleRuleConstants.DIMENSION_TYPE)) {
			SampleDimensionData.generateSampleRuleDimension();
		}
	}

	public static void prepareRelationType() {
		logger.info("Start to create sample relation type: " + SampleRuleConstants.RELATION_TYPE);
		InfoDiscoverSpace ids = DataSpaceManager.getRuleEngineInfoDiscoverSpace();
		try {
			RelationManager.createRelationType(ids, SampleRuleConstants.RELATION_TYPE, null);
		} catch (InfoDiscoveryEngineDataMartException e) {
			logger.error("Failed to create sampel relation type: " + e.getMessage());
		} catch (InfoDiscoveryEngineRuntimeException e) {
			logger.error("Failed to create sampel relation type: " + e.getMessage());
		} finally {
			ids.closeSpace();
		}

		logger.info("End to create sample relation type");
	}

	public static void prepareFactType() {
		logger.info("Start to create sample fact type: " + SampleRuleConstants.FACT_TYPE);

		InfoDiscoverSpace ids = DataSpaceManager.getRuleEngineInfoDiscoverSpace();
		try {
			FactManager.createFactType(ids, SampleRuleConstants.FACT_TYPE, getSampleFactProperties());
		} catch (InfoDiscoveryEngineDataMartException e) {
			logger.error("Failed to create sampel fact type: " + e.getMessage());
		} catch (InfoDiscoveryEngineRuntimeException e) {
			logger.error("Failed to create sampel fact type: " + e.getMessage());
		} finally {
			ids.closeSpace();
		}

		logger.info("End to create sample fact type");
	}

	public static void prepareDimensionType() {
		logger.info("Start to create sample dimension type: " + SampleRuleConstants.DIMENSION_TYPE);
		InfoDiscoverSpace ids = DataSpaceManager.getRuleEngineInfoDiscoverSpace();
		try {
			DimensionManager.createDimensionType(ids, SampleRuleConstants.DIMENSION_TYPE,
					getSampleDimensionProperties());
		} catch (InfoDiscoveryEngineDataMartException e) {
			logger.error("Failed to create sampel dimension type: " + e.getMessage());
		} catch (InfoDiscoveryEngineRuntimeException e) {
			logger.error("Failed to create sampel dimension type: " + e.getMessage());
		} finally {
			ids.closeSpace();
		}
		logger.info("End to create sampel dimension type");
	}

	public static Map<String, PropertyType> getSampleFactProperties() {
		Map<String, PropertyType> properties = new HashMap<String, PropertyType>();
		properties.put(SampleRuleConstants.FACT_PROPERTY_NAME, PropertyType.STRING);
		properties.put(SampleRuleConstants.FACT_PROPERTY_DESCRIPTION, PropertyType.STRING);
		properties.put(SampleRuleConstants.FACT_PROPERTY_KEYWORDS, PropertyType.STRING);
		return properties;
	}

	public static Map<String, PropertyType> getSampleDimensionProperties() {
		Map<String, PropertyType> properties = new HashMap<String, PropertyType>();
		properties.put(SampleRuleConstants.DIMENSION_PROPERTY_NAME, PropertyType.STRING);
		properties.put(SampleRuleConstants.DIMENSION_PROPERTY_DESCRIPTION, PropertyType.STRING);
		properties.put(SampleRuleConstants.DIMENSION_PROPERTY_KEYWORDS, PropertyType.STRING);
		return properties;
	}

}
