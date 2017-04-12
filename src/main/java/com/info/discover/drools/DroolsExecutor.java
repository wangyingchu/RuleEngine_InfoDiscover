package com.info.discover.drools;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.AgendaFilter;
import org.kie.api.runtime.rule.Match;

import com.info.discover.drools.rules.propertyMapping.PropertyMappingOutput;
import com.info.discover.drools.rules.propertyMapping.SourceInput;
import com.info.discover.drools.rules.propertyMapping.TargetInput;

public class DroolsExecutor {
	
//	private static DroolsExecutor instance = null;
//	
//	public static synchronized DroolsExecutor getInstance() {
//		return instance == null ? new DroolsExecutor() : instance;
//	}
	
	public static void main(String[] args) {
		SourceInput source = new SourceInput();
    	source.setSource("Fact");
    	source.setSourceType("testFact");
    	String[] props = {"p1", "p2"};
    	source.setSourceProperties(props);
    	
    	TargetInput target = new TargetInput();
    	target.setTarget("Dimension");
    	target.setTargetType("testDimension");
    	target.setTargetProperty("name");
    	
    	PropertyMappingOutput mapping = executeRule("PropertyMapping", source, target);
    	
    	System.out.println("source: " + mapping.getSource());
    	System.out.println("sourceType: " + mapping.getSourceType());
    	System.out.println("sourceProperties: " + mapping.getSourceProperties());
    	System.out.println("target: " + mapping.getTarget());
    	System.out.println("targetType: " + mapping.getTargetType());
    	System.out.println("targetProperty: " + mapping.getTargetProperty());
	}
	
	public static PropertyMappingOutput executeRule(String ruleName, SourceInput source, TargetInput target) {
		final PropertyMappingOutput mapping = new PropertyMappingOutput();
		try {
	        // load up the knowledge base
	        KieServices ks = KieServices.Factory.get();
		    KieContainer kContainer = ks.getKieClasspathContainer();
	    	KieSession kSession = kContainer.newKieSession("ksession-rules");
	    	
	    	kSession.insert(source);
	    	kSession.insert(target);
	    	kSession.setGlobal("mapping", mapping);
	        
	    	final String name = ruleName;
	    	kSession.fireAllRules(new AgendaFilter() {
				public boolean accept(Match match) {
					return match.getRule().getName().equalsIgnoreCase(name);
				}
	    	});
	    	
	        kSession.dispose();
	    } catch (Throwable t) {
	        t.printStackTrace();
	    }
		return mapping;
	}
}


