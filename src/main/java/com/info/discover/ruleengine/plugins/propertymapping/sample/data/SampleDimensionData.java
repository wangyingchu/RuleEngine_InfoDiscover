package com.info.discover.ruleengine.plugins.propertymapping.sample.data;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.info.discover.ruleengine.manager.database.DataSpaceManager;
import com.info.discover.ruleengine.manager.database.DimensionManager;
import com.info.discover.ruleengine.plugins.propertymapping.sample.SampleRuleConstants;
import com.infoDiscover.infoDiscoverEngine.dataWarehouse.ExploreParameters;
import com.infoDiscover.infoDiscoverEngine.dataWarehouse.InformationExplorer;
import com.infoDiscover.infoDiscoverEngine.infoDiscoverBureau.InfoDiscoverSpace;
import com.infoDiscover.infoDiscoverEngine.util.exception.InfoDiscoveryEngineRuntimeException;

public class SampleDimensionData {
	private final static Logger logger = LoggerFactory.getLogger(SampleDimensionData.class);

	// to move to a file
	private static String[] names = { "animal", "image", "house", "music" };

	public static void generateSampleRuleDimension() {
		InfoDiscoverSpace ids = DataSpaceManager.getInfoDiscoverSpace();
		for (int i = 0; i < names.length; i++) {
			Map<String, Object> properties = generateSamplePropertiesData(names[i], "", "");
			try {
				DimensionManager.createDimension(ids, SampleRuleConstants.DIMENSION_TYPE, properties);
			} catch (InfoDiscoveryEngineRuntimeException e) {
				logger.error("Failed to create dimension with properties: " + properties);
				logger.error(e.getMessage());
			}
		}

		ids.closeSpace();
	}

	public static Map<String, Object> generateSamplePropertiesData(String name, String description, String keywords) {
		logger.info("Enter method generateSamplePropertiesData()");

		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(SampleRuleConstants.FACT_PROPERTY_NAME, name);
		properties.put(SampleRuleConstants.FACT_PROPERTY_DESCRIPTION, description);
		properties.put(SampleRuleConstants.FACT_PROPERTY_KEYWORDS, keywords);

		logger.info("Exit method generateProgressRandomData()...");
		return properties;
	}

	public static boolean checkDimensionIsCreate(String dimensionType) {
		logger.info("Start to check sample dimension is created: " + dimensionType);
		InfoDiscoverSpace ids = DataSpaceManager.getInfoDiscoverSpace();
		InformationExplorer ie = ids.getInformationExplorer();
		ExploreParameters ep = new ExploreParameters();
		ep.setType(dimensionType);

		boolean result = DimensionManager.checkDimesnionIsCreated(ie, ep);
		ids.closeSpace();
		logger.info("End to check sampel dimension is created or not");
		return result;
	}
}
