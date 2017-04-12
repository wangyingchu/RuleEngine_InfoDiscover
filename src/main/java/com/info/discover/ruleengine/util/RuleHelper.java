package com.info.discover.ruleengine.util;

import com.infoDiscover.infoDiscoverEngine.dataMart.Dimension;
import com.infoDiscover.infoDiscoverEngine.dataWarehouse.ExploreParameters;
import com.infoDiscover.infoDiscoverEngine.dataWarehouse.InformationExplorer;
import com.infoDiscover.infoDiscoverEngine.infoDiscoverBureau.InfoDiscoverSpace;
import com.infoDiscover.infoDiscoverEngine.util.exception.InfoDiscoveryEngineInfoExploreException;
import com.infoDiscover.infoDiscoverEngine.util.exception.InfoDiscoveryEngineRuntimeException;
import com.infoDiscover.infoDiscoverEngine.util.factory.DiscoverEngineComponentFactory;

import java.util.HashMap;
import java.util.List;

public class RuleHelper {

    private static HashMap<String, String[]> dimensionNamesMap = new HashMap<String, String[]>();
    private final static String KEY = "AllDimensionNames";

    public static String[] getAllDimensionNames(String spaceName, String dimensionType) {

        if (dimensionNamesMap.get(KEY) == null) {

            InfoDiscoverSpace ids = DiscoverEngineComponentFactory.connectInfoDiscoverSpace
                    (spaceName);

            InformationExplorer ie = ids.getInformationExplorer();
            ExploreParameters ep = new ExploreParameters();
            ep.setType(dimensionType);

            try {
                List<Dimension> dimensionList = ie.discoverDimensions(ep);
                if (dimensionList != null && dimensionList.size() > 0) {
                    String[] names = new String[dimensionList.size()];
                    int i = 0;
                    for (Dimension dimension : dimensionList) {
                        String name = dimension.getProperty("name").getPropertyValue().toString();
                        names[i] = name;
                        i++;
                    }
                    dimensionNamesMap.put(KEY, names);
                }
            } catch (InfoDiscoveryEngineRuntimeException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            } catch (InfoDiscoveryEngineInfoExploreException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }

            ids.closeSpace();
        }

        return dimensionNamesMap.get(KEY);
    }
}
