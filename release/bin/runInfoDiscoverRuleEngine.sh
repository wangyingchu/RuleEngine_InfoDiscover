#!/usr/bin/env bash

echo "Start InfoDiscover RuleEngine"
java -cp ../lib/DiscoverEngine_InfoDiscover-1.0.0.jar:../lib/RuleEngine_InfoDiscover-1.0.0-SNAPSHOT-jar-with-dependencies.jar com.info.discover.ruleengine.plugins.propertymapping.PropertyMappingInitializer

echo "Start InfoDiscover RuleEngine successful!"
