package com.info.discover.ruleengine.manager.database;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infoDiscover.infoDiscoverEngine.dataMart.Dimension;
import com.infoDiscover.infoDiscoverEngine.dataMart.Fact;
import com.infoDiscover.infoDiscoverEngine.dataMart.PropertyType;
import com.infoDiscover.infoDiscoverEngine.dataMart.Relation;
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

	public static Relation linkFactToDimensionByRelationType(InfoDiscoverSpace ids, Fact fromFact,
			Dimension toDimension, String relationType)
			throws InfoDiscoveryEngineDataMartException, InfoDiscoveryEngineRuntimeException {
		logger.info("Enter method linkFactToDimensionByRelationType() with fromFactId: " + fromFact.getId() + " "
				+ "and " + "toDimensionId: " + toDimension.getId() + " and relationType: " + relationType);

		if (fromFact != null && toDimension != null) {

			if (!ids.hasRelationType(relationType)) {
				ids.addRelationType(relationType);
			}

			Relation r = ids.attachFactToDimension(fromFact.getId(), toDimension.getId(), relationType);
			return r;
		}

		logger.info("Exit method linkFactToDimensionByRelationType()...");
		return null;
	}

	public static void linkFactToDimensionsByRelationType(InfoDiscoverSpace ids, Fact fromFact,
			List<Dimension> toDimensionList, String relationType)
			throws InfoDiscoveryEngineDataMartException, InfoDiscoveryEngineRuntimeException {
		logger.info(
				"Enter method linkFactToDimensionsByRelationType() with fromFact: {} and dimensionList: {} and relationType: {}",
				fromFact, toDimensionList, relationType);

		if (fromFact != null && toDimensionList != null && toDimensionList.size() > 0) {

			if (!ids.hasRelationType(relationType)) {
				ids.addRelationType(relationType);
			}
			for (Dimension dimension : toDimensionList) {
				ids.attachFactToDimension(fromFact.getId(), dimension.getId(), relationType);
			}
		}

		logger.info("Exit method linkFactToDimensionsByRelationType()...");
	}
	
	public static void linkFactToDimensionsByRelationType(InfoDiscoverSpace ids, String factId,
			List<Dimension> toDimensionList, String relationType)
			throws InfoDiscoveryEngineDataMartException, InfoDiscoveryEngineRuntimeException {
		logger.info(
				"Enter method linkFactToDimensionsByRelationType() with factId: {} and dimensionList: {} and relationType: {}",
				factId, toDimensionList, relationType);

		if (factId != null && toDimensionList != null && toDimensionList.size() > 0) {

			if (!ids.hasRelationType(relationType)) {
				ids.addRelationType(relationType);
			}
			for (Dimension dimension : toDimensionList) {
				ids.attachFactToDimension(factId, dimension.getId(), relationType);
			}
		}

		logger.info("Exit method linkFactToDimensionsByRelationType()...");
	}
}
