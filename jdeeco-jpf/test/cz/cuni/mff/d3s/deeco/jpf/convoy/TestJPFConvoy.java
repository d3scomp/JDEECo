package cz.cuni.mff.d3s.deeco.jpf.convoy;

import static org.junit.Assert.*;
import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessor;
import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.jpf.AtomicProposition;
import cz.cuni.mff.d3s.deeco.jpf.KnowledgeJPF;
import cz.cuni.mff.d3s.deeco.knowledge.BaseKnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManagerContainer;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeNotFoundException;
import cz.cuni.mff.d3s.deeco.knowledge.LocalListener;
import cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata;
import cz.cuni.mff.d3s.deeco.model.runtime.custom.RuntimeMetadataFactoryExt;
import cz.cuni.mff.d3s.deeco.runtime.RuntimeConfiguration;
import cz.cuni.mff.d3s.deeco.runtime.RuntimeFramework;
import cz.cuni.mff.d3s.deeco.runtime.RuntimeFrameworkBuilder;
/*
import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.util.test.TestJPF;
import org.junit.Test;
 */

import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class TestJPFConvoy { //extends TestJPF {
                        
    @Test
    public void testJPFConvoy() throws FileNotFoundException {
    	
        // this initializes the JPF configuration from default.properties, site.properties
        // configured extensions (jpf.properties), current directory (jpf.properies) and
        // command line args ("+<key>=<value>" options and *.jpf)    	
        Config conf = JPF.createConfig(new String[]{        		
        		"+site=./site.properties",
        		"+classpath=target/classes,target/test-classes,target/objenesis.jar,target/cloning.jar,target/cz.cuni.mff.d3s.jdeeco.core.jar,target/org.eclipse.emf.common.jar,target/org.eclipse.emf.ecore.jar",
        		"+jpf-core.native_classpath+=;target/classes;target/test-classes;target/cz.cuni.mff.d3s.jdeeco.core.jar",
        		"jpfProperties/TestJPFConvoy.jpf"});
      
        System.setProperty("org.eclipse.emf.common.util.ReferenceClearingQueue", "false");
        
        
        JPF jpf = new JPF(conf);

        jpf.run();
        if (jpf.foundErrors()){
            // ... process property violations discovered by JPF
        	fail();
        } 
    }

                      


    ///////////////////////////////////////////////////////////////////////////
    // Scenario
    ///////////////////////////////////////////////////////////////////////////

    List<AtomicProposition> propositions = Arrays.asList(new AtomicProposition[]{
            /**
             * An atomic proposition expressing the 'is follower at destination' condition
             */
            new AtomicProposition() {
                @Override
                public String getName() {
                    return "isFollowerAtDestination";
                }

                @Override
                public boolean evaluate(KnowledgeJPF knowledge) {
                    try {
                        return knowledge.get("follower.position.x").equals(knowledge.get("follower.destination.x"))
                                && knowledge.get("follower.position.y").equals(knowledge.get("follower.destination.y"));
                    } catch (KnowledgeNotFoundException e) {
                        return false;
                    }
                }
            }
    });


    static Leader l1 = new Leader("leader1", new LinkedList<>(Arrays.asList(
            new Waypoint(8, 8),
            new Waypoint(8, 7), new Waypoint(8, 6), new Waypoint(8, 5),
            new Waypoint(7, 5), new Waypoint(6, 5), new Waypoint(5, 5),
            new Waypoint(4, 5), new Waypoint(3, 5), new Waypoint(2, 5),
            new Waypoint(1, 5), new Waypoint(0, 5), new Waypoint(0, 4),
            new Waypoint(0, 3), new Waypoint(0, 2), new Waypoint(1, 2),
            new Waypoint(2, 2), new Waypoint(3, 2), new Waypoint(4, 2),
            new Waypoint(5, 2), new Waypoint(6, 2), new Waypoint(7, 2),
            new Waypoint(8, 2), new Waypoint(9, 2), new Waypoint(9, 1),
            new Waypoint(9, 0))
    ));

    static Leader l2 = new Leader("leader2", new LinkedList<>(Arrays.asList(
            new Waypoint(1, 0),
            new Waypoint(1, 1), new Waypoint(1, 2), new Waypoint(1, 3),
            new Waypoint(2, 3), new Waypoint(3, 3), new Waypoint(4, 3),
            new Waypoint(5, 3), new Waypoint(6, 3), new Waypoint(6, 4),
            new Waypoint(6, 5), new Waypoint(6, 6), new Waypoint(6, 7),
            new Waypoint(6, 8), new Waypoint(7, 8), new Waypoint(8, 8))
    ));

    static Follower f = new Follower("follower", new Waypoint(1, 4), new Waypoint(7, 2));

    public static void main (String[] args) throws AnnotationProcessorException {
    	
    	System.out.println(String.format("org.eclipse.emf.common.util.ReferenceClearingQueue = %s",
    			System.getProperty("org.eclipse.emf.common.util.ReferenceClearingQueue")));
    	
    	
        AnnotationProcessor processor = new AnnotationProcessor(RuntimeMetadataFactoryExt.eINSTANCE);
        RuntimeMetadata model = RuntimeMetadataFactoryExt.eINSTANCE.createRuntimeMetadata();

        processor.process(model,
                l1, l2, f, // Components
                ConvoyEnsemble.class // Ensembles
        );

        // create an ad-hoc builder subclass that returns a JPF-aware knowledge manager container
        RuntimeFrameworkBuilder builder = new RuntimeFrameworkBuilder(new RuntimeConfiguration(
                RuntimeConfiguration.Scheduling.WALL_TIME,
                RuntimeConfiguration.Distribution.LOCAL,
                RuntimeConfiguration.Execution.SINGLE_THREADED)) {
        	
        	@Override
        	protected void buildKnowledgeManagerContainer() {
        		kmContainer = new KnowledgeManagerContainer() {
        			@Override
        			public KnowledgeManager createLocal(String id) {
        				if (locals.containsKey(id))
        					return locals.get(id);
        				
        				// return BaseKnowledgeManager instead of CloningKnowledgeManager
        				// TODO: refactor
        				KnowledgeManager result = new BaseKnowledgeManager(id);
        				locals.put(id, result);
        				
        				for (LocalListener listener : localListeners) {
        					listener.localCreated(result, this);
        				}		
        				return result;
        			}
        		};
        	}                
    	};
        RuntimeFramework runtime = builder.build(model);
        runtime.start();
    }


}
