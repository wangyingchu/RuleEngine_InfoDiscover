package com.info.discover.ruleengine.plugins.propertymapping.sample.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.info.discover.ruleengine.manager.database.DataSpaceManager;
import com.info.discover.ruleengine.manager.database.FactManager;
import com.info.discover.ruleengine.plugins.propertymapping.sample.SampleRuleConstants;
import com.infoDiscover.infoDiscoverEngine.infoDiscoverBureau.InfoDiscoverSpace;
import com.infoDiscover.infoDiscoverEngine.util.exception.InfoDiscoveryEngineRuntimeException;

public class SampleFactData {

	private final static Logger logger = LoggerFactory.getLogger(SampleFactData.class);
	
	// to move to a file
	private static String[] names = {"animal", "image", "house"};
	private static String[] descriptions = {"this is an animal", "this is an image", "this is a house"};
	
	// to move to configuration file
	private static int toGenerateCount = 10;
	
	public static void generateSampleRuleFact() {
		if (toGenerateCount > 0) {
			InfoDiscoverSpace ids = DataSpaceManager.getInfoDiscoverSpace();
			for (int i = 0; i < toGenerateCount; i++){
				Map<String, Object> properties = generateSamplePropertiesData();
				try {
					FactManager.createFact(ids, SampleRuleConstants.FACT_TYPE, properties);
				} catch (InfoDiscoveryEngineRuntimeException e) {
					logger.error("Failed to create fact with properties: " + properties);
					logger.error(e.getMessage());
				}
			}
			
			ids.closeSpace();
		}
	}

	
	public static Map<String, Object> generateSamplePropertiesData() {
		logger.info("Enter method generateSamplePropertiesData()");
		
		Map<String, Object> properties = new HashMap<String, Object>();
		int randomIndex = getRandomIndex(names);
		properties.put(SampleRuleConstants.FACT_PROPERTY_NAME, names[randomIndex]);
		properties.put(SampleRuleConstants.FACT_PROPERTY_DESCRIPTION, descriptions[randomIndex]);
		properties.put(SampleRuleConstants.FACT_PROPERTY_KEYWORDS, "");

		logger.info("Exit method generateProgressRandomData()...");
		return properties;
	}
	
	public static String getRandomName() {
		return getRandomValue(names);
	}
	
	public static String getRandomDescription() {
		return getRandomValue(descriptions);
	}
	
	public static int getRandomIndex(String[] values) {
		return generateRandomInRange(0, values.length);
	}
	
	public static String getRandomValue(String[] values) {
		int randomIndex = generateRandomInRange(0, values.length -1);
		return values[randomIndex];
	}
	
	public static int generateRandomInRange(int min, int max) {
        Random random = new Random();
        int value = random.nextInt(max) % (max - min + 1) + min;
        return value;
    }
	
}
