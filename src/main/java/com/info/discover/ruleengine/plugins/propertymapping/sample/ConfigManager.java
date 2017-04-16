package com.info.discover.ruleengine.plugins.propertymapping.sample;

public class ConfigManager {
	public static String getRoot() {
		return System.getenv("INFODISCOVER_HOME");
	}

	public static String getRuleFileTemplatePath() {
		return getRoot() + "/rule/sample/data";
	}
}
