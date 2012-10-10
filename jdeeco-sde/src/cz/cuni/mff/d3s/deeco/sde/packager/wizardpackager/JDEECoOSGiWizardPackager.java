package cz.cuni.mff.d3s.deeco.sde.packager.wizardpackager;

import java.io.File;
import java.nio.file.Files;

import org.eclipse.jdt.ui.jarpackager.JarPackageData;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import cz.cuni.mff.d3s.deeco.sde.packager.JDEECoOSGiDSFileBuilder;
import cz.cuni.mff.d3s.deeco.sde.packager.JDEECoOSGiManifestProvider;
import cz.cuni.mff.d3s.deeco.sde.packager.JDEECoOSGiUtils;

public class JDEECoOSGiWizardPackager {

	private JDEECoOSGiWizard wizard;
	private JarPackageData jarData;

	public void pack(final String bundleSymbolicName) {
		wizard = new JDEECoOSGiWizard();
		jarData = new JarPackageData();
		jarData.setManifestProvider(new JDEECoOSGiManifestProvider(
				bundleSymbolicName));
		wizard.init(PlatformUI.getWorkbench(), jarData);
		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run() {
				Shell shell = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getShell();
				WizardDialog wizardDialog = new WizardDialog(shell, wizard);
				if (wizardDialog.open() == Window.OK) {
					finalizePackaging(jarData, bundleSymbolicName);
				} else
					JDEECoOSGiUtils.deleteEverything(new File(jarData
							.getAbsoluteJarLocation().toString()));
			}

		});

	}

	private void finalizePackaging(JarPackageData jarData,
			String bundleSymbolicName) {
		File tmpDir = null;
		String jarPath = jarData.getAbsoluteJarLocation().toString();
		try {
			try {
				tmpDir = JDEECoOSGiUtils.getTempDirectory();
				File osgiInf = JDEECoOSGiDSFileBuilder
						.buildDeclarativeServiceXML(tmpDir,
								JDEECoOSGiUtils.getClassNames(jarPath),
								bundleSymbolicName);
				File originalFile = new File(jarPath);
				File newJarFile = JDEECoOSGiUtils.updateJarFile(originalFile,
						osgiInf, tmpDir);
				File dir = originalFile.getParentFile();
				String name = originalFile.getName();
				Files.delete(originalFile.toPath());
				if (newJarFile.renameTo(new File(dir, name)))
					System.out.println("Packaging successful");
				else
					System.out.println("Packaging failed");
			} finally {
				if (tmpDir != null)
					JDEECoOSGiUtils.deleteEverything(tmpDir);
			}
		} catch (Exception e) {
			e.printStackTrace();
			JDEECoOSGiUtils.deleteEverything(new File(jarPath));
		}
	}
}