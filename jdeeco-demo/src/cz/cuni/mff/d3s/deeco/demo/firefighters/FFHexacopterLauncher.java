package cz.cuni.mff.d3s.deeco.demo.firefighters;

import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessor;
import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata;
import cz.cuni.mff.d3s.deeco.model.runtime.custom.RuntimeMetadataFactoryExt;
import cz.cuni.mff.d3s.deeco.runtime.RuntimeConfiguration;
import cz.cuni.mff.d3s.deeco.runtime.RuntimeConfiguration.Distribution;
import cz.cuni.mff.d3s.deeco.runtime.RuntimeConfiguration.Execution;
import cz.cuni.mff.d3s.deeco.runtime.RuntimeConfiguration.Scheduling;
import cz.cuni.mff.d3s.deeco.runtime.RuntimeFramework;
import cz.cuni.mff.d3s.deeco.runtime.RuntimeFrameworkBuilder;

/**
 * Main class for launching the FF scenario with hexacopter.
 * 
 * @author Ilias Gerostathopoulos
 * 
 */
public class FFHexacopterLauncher {

	public static void main(String[] args) throws AnnotationProcessorException {
		
		AnnotationProcessor processor = new AnnotationProcessor(RuntimeMetadataFactoryExt.eINSTANCE);
		RuntimeMetadata model = RuntimeMetadataFactoryExt.eINSTANCE.createRuntimeMetadata();
		
		processor.process(model, 
						new GroupMember("FF1", "T1"), new GroupMember("FF2", "T1"),
						new GroupMember("FF3", "T1"), new GroupMember("FF4", "T2"),
						new GroupMember("FF5", "T2"), new GroupMember("FF6", "T2"),
						new GroupLeader("GL1", "T1", false, new Position(1000, 1000)),
						new GroupLeader("GL2", "T2", true, new Position(0, 0)), // Components 
						SensorDataAggregation.class, CriticalDataAggregationOnHexacopter.class, CriticalDataCopyFromHexacopterToSL.class // Ensembles
							);
		
		RuntimeFrameworkBuilder builder = new RuntimeFrameworkBuilder(
				new RuntimeConfiguration(
						Scheduling.WALL_TIME, 
						Distribution.LOCAL, 
						Execution.SINGLE_THREADED));
		RuntimeFramework runtime = builder.build(model); 
		runtime.start();
	}
}
