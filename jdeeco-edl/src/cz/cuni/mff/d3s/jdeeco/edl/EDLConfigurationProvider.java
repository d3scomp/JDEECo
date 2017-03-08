package cz.cuni.mff.d3s.jdeeco.edl;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.generator.IOutputConfigurationProvider;
import org.eclipse.xtext.generator.OutputConfiguration;

public class EDLConfigurationProvider implements IOutputConfigurationProvider {

	@Override
	public Set<OutputConfiguration> getOutputConfigurations() {
		OutputConfiguration defaultOutput = new OutputConfiguration(IFileSystemAccess.DEFAULT_OUTPUT);
	    defaultOutput.setDescription("Output Folder");
	    defaultOutput.setOutputDirectory("./src-gen");
	    defaultOutput.setOverrideExistingResources(true);
	    defaultOutput.setCreateOutputDirectory(true);
	    defaultOutput.setCleanUpDerivedResources(true);
	    defaultOutput.setSetDerivedProperty(true);

	    OutputConfiguration onceOutput = new OutputConfiguration(ContextSymbols.GENERATE_ONCE);
	    onceOutput.setDescription("Output Folder (once)");
	    onceOutput.setOutputDirectory("./src");
	    onceOutput.setOverrideExistingResources(false);
	    onceOutput.setCreateOutputDirectory(true);
	    onceOutput.setCleanUpDerivedResources(false);
	    onceOutput.setSetDerivedProperty(true);
	    
	    Set<OutputConfiguration> result = new HashSet<OutputConfiguration>();
	    result.add(defaultOutput);
	    result.add(onceOutput);
	    return result;
	}

}
