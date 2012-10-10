package cz.cuni.mff.d3s.deeco.sde.packager.simplepackager;

import java.io.File;
import java.io.FileOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.Manifest;

import cz.cuni.mff.d3s.deeco.sde.packager.JDEECoOSGiDSFileBuilder;
import cz.cuni.mff.d3s.deeco.sde.packager.JDEECoOSGiManifestProvider;
import cz.cuni.mff.d3s.deeco.sde.packager.JDEECoOSGiPackagerConstants;
import cz.cuni.mff.d3s.deeco.sde.packager.JDEECoOSGiUtils;

public class JDEECoOSGiSimplePackager {

	public boolean pack(List<File> inputs, String target,
			String bundleSymbolicName) {
//		For tests
//		inputs = new LinkedList<File>();
//		inputs.add(new File("D:/JDEECo/bin/demo/cz/cuni/mff/d3s/deeco/demo/cloud/NodeA.class"));
//		target = "d:\\x.jar";
//		bundleSymbolicName = "x";
		if (target == null || target.equals("") || inputs == null
				|| inputs.size() == 0 || bundleSymbolicName == null
				|| bundleSymbolicName.equals(""))
			return false;
		try {
			System.out.println("Packaging started:");
			List<String> classNames = new LinkedList<String>();
			File tmpDir = null;
			File manifest = null;
			try {
				tmpDir = JDEECoOSGiUtils.getTempDirectory();
				manifest = buildManifest(bundleSymbolicName);
				JDEECoOSGiUtils.parseClassFiles(inputs, tmpDir, classNames);
				JDEECoOSGiDSFileBuilder.buildDeclarativeServiceXML(tmpDir, classNames,
						bundleSymbolicName);
				String command = "jar cfvm " + target + " "
						+ manifest.getAbsolutePath() + " -C "
						+ tmpDir.getAbsolutePath() + " .";
				Runtime runtime = Runtime.getRuntime();
				Process process = runtime.exec(command);
				process.waitFor();
			} finally {
				if (tmpDir != null)
					JDEECoOSGiUtils.deleteEverything(tmpDir);
				if (manifest != null)
					manifest.delete();
			}
			System.out.println("Packaging ended. See " + target);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private File buildManifest(String bundleSymbolicName) {
		Manifest m = new JDEECoOSGiManifestProvider(bundleSymbolicName).createDefault();
		File f = new File(JDEECoOSGiPackagerConstants.MANIFEST);
		try {
			FileOutputStream fos = null;
			try {
				f.createNewFile();
				fos = new FileOutputStream(f);
				m.write(fos);
			} finally {
				if (fos != null)
					fos.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return f;
	}
}
