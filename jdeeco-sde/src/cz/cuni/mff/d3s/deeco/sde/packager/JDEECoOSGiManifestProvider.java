package cz.cuni.mff.d3s.deeco.sde.packager;

import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.ui.jarpackager.IManifestProvider;
import org.eclipse.jdt.ui.jarpackager.JarPackageData;

public class JDEECoOSGiManifestProvider implements IManifestProvider {

	private String bundleSymbolicName;
	
	public JDEECoOSGiManifestProvider(String bundleSymbolicName) {
		this.bundleSymbolicName = bundleSymbolicName;
	}
	
	@Override
	public Manifest create(JarPackageData jarData) throws CoreException {
		return createDefault("1.0");
	}

	public Manifest createDefault() {
		return createDefault("1.0");
	}
	
	@Override
	public Manifest createDefault(String manifestVersion) {
		Manifest m = new Manifest();
		Attributes attributes = m.getMainAttributes();
		attributes.putValue("Manifest-Version", manifestVersion);
		attributes.putValue("Bundle-ManifestVersion", "2");
		attributes.putValue("Bundle-Version", "0.0.1.SNAPSHOT");
		attributes.putValue("Bundle-ClassPath", ".");
		attributes.putValue("Bundle-SymbolicName", bundleSymbolicName);
		attributes
				.putValue("Bundle-RequiredExecutionEnvironment", "JavaSE-1.7");
		attributes.putValue("Service-Component", "OSGI-INF/ds.xml");
		attributes
				.putValue(
						"Import-Package",
						"Import-Package: cz.cuni.mff.d3s.deeco.annotations, cz.cuni.mff.d3s.deeco.knowledge, cz.cuni.mff.d3s.deeco.provider, cz.cuni.mff.d3s.deeco.sde.provider");
		attributes.putValue("Bundle-ActivationPolicy", "lazy");
		return m;
	}

}
