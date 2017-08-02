package com.info.discover.ruleengine.solution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.info.discover.ruleengine.solution.contants.*;
import com.info.discover.ruleengine.solution.pojo.*;
import com.infoDiscover.infoDiscoverEngine.dataMart.Fact;
import com.infoDiscover.infoDiscoverEngine.dataWarehouse.ExploreParameters;
import com.infoDiscover.infoDiscoverEngine.dataWarehouse.InformationFiltering.EqualFilteringItem;
import com.infoDiscover.infoDiscoverEngine.infoDiscoverBureau.InfoDiscoverSpace;
import com.infoDiscover.infoDiscoverEngine.util.config.PropertyHandler;
import com.infoDiscover.infoDiscoverEngine.util.factory.DiscoverEngineComponentFactory;

/**
 * Created by sun on 7/17/17.
 */
public class SolutionRelationMapping {
	public static final Logger logger = LoggerFactory.getLogger(SolutionRelationMapping.class);

	private static String META_CONFIG_DISCOVERSPACE = "META_CONFIG_DISCOVERSPACE";
	private static String spaceName = PropertyHandler.getPropertyValue(META_CONFIG_DISCOVERSPACE);
	
	private static Map<String, List<RelationMappingVO>> factToDimensionMap = new HashMap<>();
	private static Map<String, List<RelationMappingVO>> dimensionToFactMap = new HashMap<>();
	private static Map<String, List<RelationMappingVO>> factToFactMap = new HashMap<>();
	private static Map<String, List<RelationMappingVO>> dimensionToDimensionMap = new HashMap<>();
	private static Map<String, List<DataDateMappingVO>> factToDateMap = new HashMap<>();
	private static Map<String, List<DataDateMappingVO>> dimensionToDateMap = new HashMap<>();
	private static Map<String, List<DataDuplicateCopyMappingVO>> factDuplicatedCopyMap = new HashMap<>();
	private static Map<String, List<DataDuplicateCopyMappingVO>> dimensionDuplicatedCopyMap = new HashMap<>();

	public void refresh(String relationMappingType) {
		logger.info("Refresh relation mapping cache: {}", relationMappingType);
		InfoDiscoverSpace ids = null;

		try {
			ids = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
			switch (relationMappingType) {
			case RelationMappingType.DATA_RELATION_MAPPING:
				setFactToFactMappings(ids);
				setFactToDimensionMappings(ids);
				setDimensionToFactMappings(ids);
				setDimensionToDimensionMappings(ids);
				break;
			case RelationMappingType.DATA_DATE_DIMENSION_MAPPING:
				setFactToDateMappings(ids);
				setDimensionToDateMappings(ids);
				break;
			case RelationMappingType.DATA_PROPERTIES_DUPLICATE_MAPPING:
				setFactDuplicateCopyMappings(ids);
				setDimensiontDuplicateCopyMappings(ids);
				break;
			default:
				break;
			}
		} catch (Exception e) {
			logger.error("Failed to connect to {}", spaceName);
		} finally {
			ids.closeSpace();
		}

		logger.info("Exit method refresh relation mapping cache()...");
	}

	public void getSolutionRelationMappings() {
		logger.info("Enter getRelationMappings()");
		InfoDiscoverSpace ids = null;
		try {
			ids = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
			if (ids != null) {
				if(factToFactMap.isEmpty()) {
					setFactToFactMappings(ids);
				}
				if(factToDimensionMap.isEmpty()) {
					setFactToDimensionMappings(ids);
				}
				if(dimensionToFactMap.isEmpty()) {
					setDimensionToFactMappings(ids);
				}
				if(dimensionToDimensionMap.isEmpty()) {
					setDimensionToDimensionMappings(ids);
				}

				if(factToDateMap.isEmpty()) {
					setFactToDateMappings(ids);
				}
				if(dimensionToDateMap.isEmpty()) {
					setDimensionToDateMappings(ids);
				}

				if(factDuplicatedCopyMap.isEmpty()) {
					setFactDuplicateCopyMappings(ids);
				}
				if(dimensionDuplicatedCopyMap.isEmpty()) {
					setDimensiontDuplicateCopyMappings(ids);
				}
			}
		} catch (Exception e) {
			logger.error("Failed to connect to {}", spaceName);
		} finally {
			ids.closeSpace();
		}

		logger.info("Exit getRelationMappings()");
	}

	private void setFactToFactMappings(InfoDiscoverSpace ids) {
		
		if(!factToFactMap.isEmpty()) {
			factToFactMap.clear();
		}
		
		ExploreParameters ep = new ExploreParameters();
		ep.setType(SolutionTemplateConstants.DataMapping_SpaceDataRelationMappingDefinition);
		ep.addFilteringItem(new EqualFilteringItem("sourceDataTypeKind", "FACT"), ExploreParameters.FilteringLogic.AND);
		ep.addFilteringItem(new EqualFilteringItem("targetDataTypeKind", "FACT"), ExploreParameters.FilteringLogic.AND);

		List<Fact> factToFactMappingList = QueryExecutor.executeFactsQuery(ids.getInformationExplorer(), ep);
		if (CollectionUtils.isEmpty(factToFactMappingList)) {
			this.setFactToFactMap(null);
		}

		List<RelationMappingVO> list = convertFactToRelationMappingPOJO(factToFactMappingList,
				SolutionTemplateConstants.JSON_FACT_TO_FACT_MAPPING);
		Map<String, List<RelationMappingVO>> map = new HashMap<>();
		map.put(SolutionTemplateConstants.JSON_FACT_TO_FACT_MAPPING, list);
		this.setFactToFactMap(map);
	}

	private void setFactToDimensionMappings(InfoDiscoverSpace ids) {
		
		if (!factToDimensionMap.isEmpty()) {
			factToDimensionMap.clear();
		}
		
		ExploreParameters ep = new ExploreParameters();
		ep.setType(SolutionTemplateConstants.DataMapping_SpaceDataRelationMappingDefinition);
		ep.addFilteringItem(new EqualFilteringItem("sourceDataTypeKind", "FACT"), ExploreParameters.FilteringLogic.AND);
		ep.addFilteringItem(new EqualFilteringItem("targetDataTypeKind", "DIMENSION"),
				ExploreParameters.FilteringLogic.AND);

		List<Fact> factToDimensionMappingList = QueryExecutor.executeFactsQuery(ids.getInformationExplorer(), ep);
		if (CollectionUtils.isEmpty(factToDimensionMappingList)) {
			this.setFactToDimensionMap(null);
		}

		List<RelationMappingVO> list = convertFactToRelationMappingPOJO(factToDimensionMappingList,
				SolutionTemplateConstants.JSON_FACT_TO_DIMENSION_MAPPING);
		Map<String, List<RelationMappingVO>> map = new HashMap<>();
		map.put(SolutionTemplateConstants.JSON_FACT_TO_DIMENSION_MAPPING, list);
		this.setFactToDimensionMap(map);
	}

	private void setDimensionToDimensionMappings(InfoDiscoverSpace ids) {
		if (!dimensionToDimensionMap.isEmpty()) {
			dimensionToDimensionMap.clear();
		}
		
		ExploreParameters ep = new ExploreParameters();
		ep.setType(SolutionTemplateConstants.DataMapping_SpaceDataRelationMappingDefinition);
		ep.addFilteringItem(new EqualFilteringItem("sourceDataTypeKind", "DIMENSION"),
				ExploreParameters.FilteringLogic.AND);
		ep.addFilteringItem(new EqualFilteringItem("targetDataTypeKind", "DIMENSION"),
				ExploreParameters.FilteringLogic.AND);

		List<Fact> factToDimensionMappingList = QueryExecutor.executeFactsQuery(ids.getInformationExplorer(), ep);
		if (CollectionUtils.isEmpty(factToDimensionMappingList)) {
			this.setDimensionToDimensionMap(null);
		}

		List<RelationMappingVO> list = convertFactToRelationMappingPOJO(factToDimensionMappingList,
				SolutionTemplateConstants.JSON_DIMENSION_TO_DIMENSION_MAPPING);
		Map<String, List<RelationMappingVO>> map = new HashMap<>();
		map.put(SolutionTemplateConstants.JSON_DIMENSION_TO_DIMENSION_MAPPING, list);
		this.setDimensionToDimensionMap(map);
	}

	private void setDimensionToFactMappings(InfoDiscoverSpace ids) {
		
		if (!dimensionToFactMap.isEmpty()) {
			dimensionToFactMap.clear();
		}
		
		ExploreParameters ep = new ExploreParameters();
		ep.setType(SolutionTemplateConstants.DataMapping_SpaceDataRelationMappingDefinition);
		ep.addFilteringItem(new EqualFilteringItem("sourceDataTypeKind", "DIMENSION"),
				ExploreParameters.FilteringLogic.AND);
		ep.addFilteringItem(new EqualFilteringItem("targetDataTypeKind", "FACT"), ExploreParameters.FilteringLogic.AND);

		List<Fact> factToDimensionMappingList = QueryExecutor.executeFactsQuery(ids.getInformationExplorer(), ep);
		if (CollectionUtils.isEmpty(factToDimensionMappingList)) {
			this.setDimensionToFactMap(null);
		}

		List<RelationMappingVO> list = convertFactToRelationMappingPOJO(factToDimensionMappingList,
				SolutionTemplateConstants.JSON_DIMENSION_TO_FACT_MAPPING);
		Map<String, List<RelationMappingVO>> map = new HashMap<>();
		map.put(SolutionTemplateConstants.JSON_DIMENSION_TO_FACT_MAPPING, list);
		this.setDimensionToFactMap(map);
	}

	private void setFactToDateMappings(InfoDiscoverSpace ids) {
		if(!factToDateMap.isEmpty()) {
			factToDateMap.clear();
		}
		
		ExploreParameters ep = new ExploreParameters();
		ep.setType(SolutionTemplateConstants.DataMapping_SpaceDataDateDimensionMappingDefinition);
		ep.addFilteringItem(new EqualFilteringItem("sourceDataTypeKind", "FACT"), ExploreParameters.FilteringLogic.AND);

		List<Fact> factToDimensionMappingList = QueryExecutor.executeFactsQuery(ids.getInformationExplorer(), ep);
		if (CollectionUtils.isEmpty(factToDimensionMappingList)) {
			this.setFactToDateMap(null);
		}

		List<DataDateMappingVO> list = convertToDataDateMappingPOJO(factToDimensionMappingList,
				SolutionTemplateConstants.JSON_FACT_TO_DATE_DIMENSION_MAPPING);
		Map<String, List<DataDateMappingVO>> map = new HashMap<>();
		map.put(SolutionTemplateConstants.JSON_FACT_TO_DATE_DIMENSION_MAPPING, list);
		this.setFactToDateMap(map);
	}

	private void setDimensionToDateMappings(InfoDiscoverSpace ids) {
		if(!dimensionToDateMap.isEmpty()) {
			dimensionToDateMap.clear();
		}
		
		ExploreParameters ep = new ExploreParameters();
		ep.setType(SolutionTemplateConstants.DataMapping_SpaceDataDateDimensionMappingDefinition);
		ep.addFilteringItem(new EqualFilteringItem("sourceDataTypeKind", "DIMENSION"),
				ExploreParameters.FilteringLogic.AND);

		List<Fact> factToDimensionMappingList = QueryExecutor.executeFactsQuery(ids.getInformationExplorer(), ep);
		if (CollectionUtils.isEmpty(factToDimensionMappingList)) {
			this.setDimensionToDateMap(null);
		}

		List<DataDateMappingVO> list = convertToDataDateMappingPOJO(factToDimensionMappingList,
				SolutionTemplateConstants.JSON_DIMENSION_TO_DATE_DIMENSION_MAPPING);
		Map<String, List<DataDateMappingVO>> map = new HashMap<>();
		map.put(SolutionTemplateConstants.JSON_DIMENSION_TO_DATE_DIMENSION_MAPPING, list);
		this.setDimensionToDateMap(map);
	}

	private void setFactDuplicateCopyMappings(InfoDiscoverSpace ids) {
		if(!factDuplicatedCopyMap.isEmpty()) {
			factDuplicatedCopyMap.clear();
		}
		
		ExploreParameters ep = new ExploreParameters();
		ep.setType(SolutionTemplateConstants.DataMapping_SpaceDataPropertiesDuplicateMappingDefinition);
		ep.addFilteringItem(new EqualFilteringItem("sourceDataTypeKind", "FACT"), ExploreParameters.FilteringLogic.AND);

		List<Fact> factToDimensionMappingList = QueryExecutor.executeFactsQuery(ids.getInformationExplorer(), ep);
		if (CollectionUtils.isEmpty(factToDimensionMappingList)) {
			this.setFactDuplicatedCopyMap(null);
		}

		List<DataDuplicateCopyMappingVO> list = convertToDataDuplicateCopyMappingPOJO(factToDimensionMappingList,
				SolutionTemplateConstants.JSON_FACT_DUPLICATE_COPY_MAPPING);
		Map<String, List<DataDuplicateCopyMappingVO>> map = new HashMap<>();
		map.put(SolutionTemplateConstants.JSON_FACT_DUPLICATE_COPY_MAPPING, list);
		this.setFactDuplicatedCopyMap(map);
	}

	private void setDimensiontDuplicateCopyMappings(InfoDiscoverSpace ids) {
		if(!dimensionDuplicatedCopyMap.isEmpty()) {
			dimensionDuplicatedCopyMap.clear();
		}
		
		ExploreParameters ep = new ExploreParameters();
		ep.setType(SolutionTemplateConstants.DataMapping_SpaceDataPropertiesDuplicateMappingDefinition);
		ep.addFilteringItem(new EqualFilteringItem("sourceDataTypeKind", "DIMENSION"),
				ExploreParameters.FilteringLogic.AND);

		List<Fact> factToDimensionMappingList = QueryExecutor.executeFactsQuery(ids.getInformationExplorer(), ep);
		if (CollectionUtils.isEmpty(factToDimensionMappingList)) {
			this.setDimensionDuplicatedCopyMap(null);
		}

		List<DataDuplicateCopyMappingVO> list = convertToDataDuplicateCopyMappingPOJO(factToDimensionMappingList,
				SolutionTemplateConstants.JSON_DIMENSION_DUPLICATE_COPY_MAPPING);
		Map<String, List<DataDuplicateCopyMappingVO>> map = new HashMap<>();
		map.put(SolutionTemplateConstants.JSON_DIMENSION_DUPLICATE_COPY_MAPPING, list);
		this.setDimensionDuplicatedCopyMap(map);
	}

	private List<RelationMappingVO> convertFactToRelationMappingPOJO(List<Fact> facts, String relationMappingType) {
		List<RelationMappingVO> list = new ArrayList<>();
		for (Fact fact : facts) {
			Object sourceDataTypeKind = fact.getProperty("sourceDataTypeKind").getPropertyValue();
			Object sourceDataTypeName = fact.getProperty("sourceDataTypeName").getPropertyValue();
			Object sourceDataPropertyName = fact.getProperty("sourceDataPropertyName").getPropertyValue();
			Object sourceDataPropertyType = fact.getProperty("sourceDataPropertyType").getPropertyValue();
			Object targetDataTypeKind = fact.getProperty("targetDataTypeKind").getPropertyValue();
			Object targetDataTypeName = fact.getProperty("targetDataTypeName").getPropertyValue();
			Object targetDataPropertyName = fact.getProperty("targetDataPropertyName").getPropertyValue();
			Object targetDataPropertyType = fact.getProperty("targetDataPropertyType").getPropertyValue();
			Object targetDataPropertyValue = fact.getProperty("targetDataPropertyValue") == null ? null :
				fact.getProperty("targetDataPropertyValue").getPropertyValue();
			Object relationTypeName = fact.getProperty("relationTypeName").getPropertyValue();
			Object relationDirection = fact.getProperty("relationDirection").getPropertyValue();
			Object minValue = fact.getProperty("minValue") == null ? null
					: fact.getProperty("minValue").getPropertyValue();
			Object maxValue = fact.getProperty("maxValue") == null ? null
					: fact.getProperty("maxValue").getPropertyValue();
			Object mappingNotExistHandleMethod = fact.getProperty("mappingNotExistHandleMethod").getPropertyValue();

			RelationMappingVO pojo = new RelationMappingVO();
			pojo.setRelationMappingType(relationMappingType);
			pojo.setSourceDataTypeKind(sourceDataTypeKind == null ? null : sourceDataTypeKind.toString());
			pojo.setSourceDataTypeName(sourceDataTypeName == null ? null : sourceDataTypeName.toString());
			pojo.setSourceDataPropertyName(sourceDataPropertyName == null ? null : sourceDataPropertyName.toString());
			pojo.setSourceDataPropertyType(sourceDataPropertyType == null ? null : sourceDataPropertyType.toString());
			pojo.setTargetDataTypeKind(targetDataTypeKind == null ? null : targetDataTypeKind.toString());
			pojo.setTargetDataTypeName(targetDataTypeName == null ? null : targetDataTypeName.toString());
			pojo.setTargetDataPropertyName(targetDataPropertyName == null ? null : targetDataPropertyName.toString());
			pojo.setTargetDataPropertyType(targetDataPropertyType == null ? null : targetDataPropertyType.toString());
			pojo.setTargetDataPropertyValue(targetDataPropertyValue == null ? null : targetDataPropertyValue.toString());
			pojo.setRelationTypeName(relationTypeName == null ? null : relationTypeName.toString());
			pojo.setRelationDirection(relationDirection.toString());
			pojo.setMinValue(minValue == null ? null : minValue.toString());
			pojo.setMaxValue(maxValue == null ? null : maxValue.toString());
			pojo.setMappingNotExistHandleMethod(
					mappingNotExistHandleMethod == null ? null : mappingNotExistHandleMethod.toString());

			list.add(pojo);
		}
		return list;
	}

	private List<DataDateMappingVO> convertToDataDateMappingPOJO(List<Fact> facts, String relationMappingType) {
		List<DataDateMappingVO> list = new ArrayList<>();
		for (Fact fact : facts) {

			Object sourceDataTypeKind = fact.getProperty("sourceDataTypeKind").getPropertyValue();
			Object sourceDataTypeName = fact.getProperty("sourceDataTypeName").getPropertyValue();
			Object sourceDataPropertyName = fact.getProperty("sourceDataPropertyName").getPropertyValue();
			Object relationTypeName = fact.getProperty("relationTypeName").getPropertyValue();
			Object relationDirection = fact.getProperty("relationDirection").getPropertyValue();
			Object dateDimensionTypePrefix = fact.getProperty("dateDimensionTypePrefix").getPropertyValue();

			DataDateMappingVO pojo = new DataDateMappingVO();
			pojo.setRelationMappingType(relationMappingType);
			pojo.setSourceDataTypeKind(sourceDataTypeKind == null ? null : sourceDataTypeKind.toString());
			pojo.setSourceDataTypeName(sourceDataTypeName == null ? null : sourceDataTypeName.toString());
			pojo.setSourceDataPropertyName(sourceDataPropertyName == null ? null : sourceDataPropertyName.toString());
			pojo.setRelationTypeName(relationTypeName == null ? null : relationTypeName.toString());
			pojo.setRelationDirection(relationDirection.toString());
			pojo.setDateDimensionTypePrefix(
					dateDimensionTypePrefix == null ? null : dateDimensionTypePrefix.toString());

			list.add(pojo);
		}
		return list;
	}

	private List<DataDuplicateCopyMappingVO> convertToDataDuplicateCopyMappingPOJO(List<Fact> facts,
			String relationMappingType) {
		List<DataDuplicateCopyMappingVO> list = new ArrayList<>();
		for (Fact fact : facts) {

			Object sourceDataTypeKind = fact.getProperty("sourceDataTypeKind").getPropertyValue();
			Object sourceDataTypeName = fact.getProperty("sourceDataTypeName").getPropertyValue();
			Object sourceDataPropertyName = fact.getProperty("sourceDataPropertyName").getPropertyValue();
			Object sourceDataPropertyType = fact.getProperty("sourceDataPropertyType").getPropertyValue();

			Object targetDataTypeName = fact.getProperty("targetDataTypeName").getPropertyValue();
			Object targetDataPropertyName = fact.getProperty("targetDataPropertyName").getPropertyValue();
			Object targetDataPropertyType = fact.getProperty("targetDataPropertyType").getPropertyValue();
			Object existingPropertyHandleMethod = fact.getProperty("existingPropertyHandleMethod").getPropertyValue();

			DataDuplicateCopyMappingVO pojo = new DataDuplicateCopyMappingVO();
			pojo.setRelationMappingType(relationMappingType);
			pojo.setSourceDataTypeKind(sourceDataTypeKind == null ? null : sourceDataTypeKind.toString());
			pojo.setSourceDataTypeName(sourceDataTypeName == null ? null : sourceDataTypeName.toString());
			pojo.setSourceDataPropertyName(sourceDataPropertyName == null ? null : sourceDataPropertyName.toString());
			pojo.setSourceDataPropertyType(sourceDataPropertyType == null ? null : sourceDataPropertyType.toString());
			pojo.setTargetDataTypeName(targetDataTypeName == null ? null : targetDataTypeName.toString());
			pojo.setTargetDataPropertyName(targetDataPropertyName == null ? null : targetDataPropertyName.toString());
			pojo.setTargetDataPropertyType(targetDataPropertyType == null ? null : targetDataPropertyType.toString());
			pojo.setExistingPropertyHandleMethod(
					existingPropertyHandleMethod == null ? null : existingPropertyHandleMethod.toString());

			list.add(pojo);
		}
		return list;
	}

	public Map<String, List<RelationMappingVO>> getFactToDimensionMap() {
		return factToDimensionMap;
	}

	public void setFactToDimensionMap(Map<String, List<RelationMappingVO>> factToDimensionMap) {
		SolutionRelationMapping.factToDimensionMap = factToDimensionMap;
	}

	public Map<String, List<RelationMappingVO>> getDimensionToFactMap() {
		return dimensionToFactMap;
	}

	public void setDimensionToFactMap(Map<String, List<RelationMappingVO>> dimensionToFactMap) {
		SolutionRelationMapping.dimensionToFactMap = dimensionToFactMap;
	}

	public Map<String, List<RelationMappingVO>> getFactToFactMap() {
		return factToFactMap;
	}

	public void setFactToFactMap(Map<String, List<RelationMappingVO>> factToFactMap) {
		SolutionRelationMapping.factToFactMap = factToFactMap;
	}

	public Map<String, List<RelationMappingVO>> getDimensionToDimensionMap() {
		return dimensionToDimensionMap;
	}

	public void setDimensionToDimensionMap(Map<String, List<RelationMappingVO>> dimensionToDimensionMap) {
		SolutionRelationMapping.dimensionToDimensionMap = dimensionToDimensionMap;
	}

	public Map<String, List<DataDateMappingVO>> getFactToDataMap() {
		return factToDateMap;
	}

	public void setFactToDateMap(Map<String, List<DataDateMappingVO>> factToDataMap) {
		SolutionRelationMapping.factToDateMap = factToDataMap;
	}

	public Map<String, List<DataDateMappingVO>> getDimensionToDateMap() {
		return dimensionToDateMap;
	}

	public void setDimensionToDateMap(Map<String, List<DataDateMappingVO>> dimensionToDateMap) {
		SolutionRelationMapping.dimensionToDateMap = dimensionToDateMap;
	}

	public Map<String, List<DataDuplicateCopyMappingVO>> getFactDuplicatedCopyMap() {
		return factDuplicatedCopyMap;
	}

	public void setFactDuplicatedCopyMap(Map<String, List<DataDuplicateCopyMappingVO>> factDuplicatedCopyMap) {
		SolutionRelationMapping.factDuplicatedCopyMap = factDuplicatedCopyMap;
	}

	public Map<String, List<DataDuplicateCopyMappingVO>> getDimensionDuplicatedCopyMap() {
		return dimensionDuplicatedCopyMap;
	}

	public void setDimensionDuplicatedCopyMap(
			Map<String, List<DataDuplicateCopyMappingVO>> dimensionDuplicatedCopyMap) {
		SolutionRelationMapping.dimensionDuplicatedCopyMap = dimensionDuplicatedCopyMap;
	}
}
