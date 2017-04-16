package com.info.discover.ruleengine.manager.database;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infoDiscover.infoDiscoverEngine.dataMart.PropertyType;
import com.infoDiscover.infoDiscoverEngine.dataMart.RelationType;
import com.infoDiscover.infoDiscoverEngine.infoDiscoverBureau.InfoDiscoverSpace;
import com.infoDiscover.infoDiscoverEngine.util.exception.InfoDiscoveryEngineDataMartException;
import com.infoDiscover.infoDiscoverEngine.util.exception.InfoDiscoveryEngineRuntimeException;

public class RelationManager {
	private final static Logger logger = LoggerFactory.getLogger(RelationManager.class);

	public static void createRelationType(InfoDiscoverSpace ids, String relationTypeName,
			Map<String, PropertyType> properties)
			throws InfoDiscoveryEngineDataMartException, InfoDiscoveryEngineRuntimeException {
		logger.info("Enter method createRelationType() with relationType: " + relationTypeName + " and properties: "
				+ properties);

		if (ids != null && !ids.hasRelationType(relationTypeName)) {
			RelationType relationType = ids.addRelationType(relationTypeName);

			logger.info("Created relationType: " + relationType.getTypeName());

			if (properties != null) {
				logger.info("Start to add properties to relationType: " + relationType.getTypeName());
				Set<String> keySet = properties.keySet();
				Iterator<String> it = keySet.iterator();
				while (it.hasNext()) {
					String key = it.next();
					PropertyType value = properties.get(key);
					relationType.addTypeProperty(key, value);
				}
				logger.info("End to add properties to relationType: " + relationType.getTypeName());
			}
		}

		logger.info("Exit method createRelationType...");
	}
	
}
