#!/usr/bin/env bash

echo "Start to create sample rule data"

java -cp ../lib/DiscoverEngine_InfoDiscover-1.0.0.jar:../lib/RuleEngine_InfoDiscover-1.0.0-SNAPSHOT-jar-with-dependencies.jar com.info.discover.ruleengine.plugins.propertymapping.sample.SampleRuleExecution

echo "Sample rule data created successful!"
