package cz.cuni.mff.d3s.deeco.sde.packager.wizardpackager;

import org.eclipse.jdt.internal.ui.jarpackager.JarPackageWizard;
import org.eclipse.jdt.ui.jarpackager.JarPackageData;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.ui.IWorkbench;

@SuppressWarnings("restriction")
public class JDEECoOSGiWizard extends JarPackageWizard {
	
	@Override
	public void init(IWorkbench workbench, JarPackageData jarPackage) {
		super.init(workbench, jarPackage);
		setWindowTitle("JDEECo OSGi Bundle Exporter");
	}
	
	@Override
	public void addPages() {
		super.addPages();
	}
	
	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		if (page != getStartingPage())
			return null;
		return super.getNextPage(page);
	}
}
