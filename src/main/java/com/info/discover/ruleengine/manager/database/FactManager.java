package com.info.discover.ruleengine.manager.database;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.infoDiscover.infoDiscoverEngine.dataMart.Fact;
import com.infoDiscover.infoDiscoverEngine.dataMart.FactType;
import com.infoDiscover.infoDiscoverEngine.dataMart.PropertyType;
import com.infoDiscover.infoDiscoverEngine.dataWarehouse.ExploreParameters;
import com.infoDiscover.infoDiscoverEngine.dataWarehouse.InformationExplorer;
import com.infoDiscover.infoDiscoverEngine.infoDiscoverBureau.InfoDiscoverSpace;
import com.infoDiscover.infoDiscoverEngine.util.exception.InfoDiscoveryEngineDataMartException;
import com.infoDiscover.infoDiscoverEngine.util.exception.InfoDiscoveryEngineInfoExploreException;
import com.infoDiscover.infoDiscoverEngine.util.exception.InfoDiscoveryEngineRuntimeException;
import com.infoDiscover.infoDiscoverEngine.util.factory.DiscoverEngineComponentFactory;

public class FactManager {
	private final static Logger logger = LoggerFactory.getLogger(FactManager.class);

	public static void createFactType(InfoDiscoverSpace ids, String factTypeName, Map<String, PropertyType> properties)
			throws InfoDiscoveryEngineDataMartException, InfoDiscoveryEngineRuntimeException {
		logger.info("Enter method createFactType() with factType: " + factTypeName + " and properties: " + properties);

		if (ids != null && !ids.hasFactType(factTypeName)) {
			FactType factType = ids.addFactType(factTypeName);
			logger.info("Created factType: " + factType.getTypeName());

			if (properties != null) {
				logger.info("Start to add properties to factType: " + factType.getTypeName());
				Set<String> keySet = properties.keySet();
				Iterator<String> it = keySet.iterator();
				while (it.hasNext()) {
					String key = it.next();
					PropertyType value = properties.get(key);
					factType.addTypeProperty(key, value);
				}
				logger.info("End to add properties to factType: " + factType.getTypeName());
			}
		}

		logger.info("Exit method createFactType...");
	}

	public static boolean createFact(InfoDiscoverSpace ids, String factType, Map<String, Object> properties)
			throws InfoDiscoveryEngineRuntimeException {
		logger.info("Start to createFact() with factType: " + factType + " and properties: " + properties);
		if (ids != null) {
			Fact fact = DiscoverEngineComponentFactory.createFact(factType);
			fact = ids.addFact(fact);

			if (properties != null) {
				logger.info("Start to add properties to the new created type");
				fact.addProperties(properties);
				logger.info("End to add properties");
			}

			return true;
		}

		return false;
	}

	public static List<Fact> getFacts(InformationExplorer ie, ExploreParameters ep)
			throws InfoDiscoveryEngineRuntimeException, InfoDiscoveryEngineInfoExploreException {
		return ie.discoverFacts(ep);
	}

	public static Fact getFact(InformationExplorer ie, ExploreParameters ep)
			throws InfoDiscoveryEngineRuntimeException, InfoDiscoveryEngineInfoExploreException {
		List<Fact> factList = ie.discoverFacts(ep);
		if (factList != null && factList.size() > 0) {
			return factList.get(0);
		}

		return null;
	}

	public static boolean deleteFact(InfoDiscoverSpace ids, String rid) throws InfoDiscoveryEngineRuntimeException {
		return ids.removeFact(rid);
	}

	public static Fact updateFact(InfoDiscoverSpace ids, Fact fact, Map<String, Object> properties) {
		fact.addNewOrUpdateProperties(properties);
		return fact;
	}

}
