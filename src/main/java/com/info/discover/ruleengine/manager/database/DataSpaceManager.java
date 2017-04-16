package com.info.discover.ruleengine.manager.database;

import com.infoDiscover.infoDiscoverEngine.infoDiscoverBureau.InfoDiscoverSpace;
import com.infoDiscover.infoDiscoverEngine.util.factory.DiscoverEngineComponentFactory;

public class DataSpaceManager {
	
	public static InfoDiscoverSpace getInfoDiscoverSpace(String spaceName) {
        return DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
    }
	
	public static InfoDiscoverSpace getRuleEngineInfoDiscoverSpace() {
        return getInfoDiscoverSpace(RuleEngineDatabaseConstants.RuleEngineSpace);
    }
	
	public static boolean checkDataSapceExistence(String spaceName) {
		return DiscoverEngineComponentFactory.checkDiscoverSpaceExistence(spaceName);
	}
	
	public static boolean createDataSpace(String spaceName) {
		return DiscoverEngineComponentFactory.createInfoDiscoverSpace(spaceName);
	}
	
	public static boolean dropDataSpace(String spaceName) {
		return DiscoverEngineComponentFactory.deleteInfoDiscoverSpace(spaceName);
	}
	
}
