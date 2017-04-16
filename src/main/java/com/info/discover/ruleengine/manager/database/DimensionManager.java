package com.info.discover.ruleengine.manager.database;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infoDiscover.infoDiscoverEngine.dataMart.Dimension;
import com.infoDiscover.infoDiscoverEngine.dataMart.DimensionType;
import com.infoDiscover.infoDiscoverEngine.dataMart.PropertyType;
import com.infoDiscover.infoDiscoverEngine.dataWarehouse.ExploreParameters;
import com.infoDiscover.infoDiscoverEngine.dataWarehouse.InformationExplorer;
import com.infoDiscover.infoDiscoverEngine.infoDiscoverBureau.InfoDiscoverSpace;
import com.infoDiscover.infoDiscoverEngine.util.exception.InfoDiscoveryEngineDataMartException;
import com.infoDiscover.infoDiscoverEngine.util.exception.InfoDiscoveryEngineInfoExploreException;
import com.infoDiscover.infoDiscoverEngine.util.exception.InfoDiscoveryEngineRuntimeException;
import com.infoDiscover.infoDiscoverEngine.util.factory.DiscoverEngineComponentFactory;

public class DimensionManager {

	private final static Logger logger = LoggerFactory.getLogger(DimensionManager.class);

	public static void createDimensionType(InfoDiscoverSpace ids, String dimensionTypeName,
			Map<String, PropertyType> properties)
			throws InfoDiscoveryEngineDataMartException, InfoDiscoveryEngineRuntimeException {
		logger.info("Enter method createDimensionType() with factType: " + dimensionTypeName + " and properties: "
				+ properties);

		if (ids != null && !ids.hasDimensionType(dimensionTypeName)) {
			DimensionType dimensionType = ids.addDimensionType(dimensionTypeName);

			logger.info("Created dimensionTYpe: " + dimensionType.getTypeName());

			if (properties != null) {
				logger.info("Start to add properties to dimensionType: " + dimensionType.getTypeName());
				Set<String> keySet = properties.keySet();
				Iterator<String> it = keySet.iterator();
				while (it.hasNext()) {
					String key = it.next();
					PropertyType value = properties.get(key);
					dimensionType.addTypeProperty(key, value);
				}
				logger.info("End to add properties to factType: " + dimensionType.getTypeName());
			}
		}

		logger.info("Exit method createDimensionType...");
	}

	public static boolean createDimension(InfoDiscoverSpace ids, String dimensionType, Map<String, Object> properties)
			throws InfoDiscoveryEngineRuntimeException {
		logger.info(
				"Start to createDimension() with dimensionType: " + dimensionType + " and properties: " + properties);
		if (ids != null) {
			Dimension dimension = DiscoverEngineComponentFactory.createDimension(dimensionType);
			dimension = ids.addDimension(dimension);

			if (properties != null) {
				logger.info("Start to add properties to the new created dimension");
				dimension.addProperties(properties);
				logger.info("End to add properties");
			}

			return true;
		}

		return false;
	}

	public static List<Dimension> getDimensionList(InformationExplorer ie, ExploreParameters ep)
			throws InfoDiscoveryEngineRuntimeException, InfoDiscoveryEngineInfoExploreException {
		return ie.discoverDimensions(ep);
	}
	
	public static boolean checkDimesnionIsCreated(InformationExplorer ie, ExploreParameters ep) {
		List<Dimension> list = new ArrayList<Dimension>();
		try {
			list = getDimensionList(ie, ep);
		} catch (InfoDiscoveryEngineRuntimeException e) {
			e.printStackTrace();
		} catch (InfoDiscoveryEngineInfoExploreException e) {
			e.printStackTrace();
		}
		return list.size() > 0;
	}
}
