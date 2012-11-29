package cz.cuni.mff.d3s.deeco.sde.packager;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.JavaClass;

public class JDEECoOSGiUtils {

	public static void deleteEverything(File file) {
		if (file == null)
			return;
		else if (file.isFile())
			file.delete();
		else if (file.isDirectory()) {
			for (File f : file.listFiles()) {
				deleteEverything(f);
			}
			file.delete();
		}
	}

	public static File getTempDirectory() {
		File tmpDir = new File(System.currentTimeMillis()
				+ JDEECoOSGiPackagerConstants.TMP_DIR);
		if (tmpDir.mkdir())
			return tmpDir;
		else
			return null;
	}

	public static void parseClassFiles(List<File> files, File where,
			List<String> classNames) {
		FileChannel fcSrc = null;
		FileChannel fcDest = null;
		FileInputStream fis = null;
		FileOutputStream fos = null;
		JavaClass clazz;
		String name;
		File tempF;
		for (File f : files) {
			try {
				try {
					clazz = (new ClassParser(f.getAbsolutePath())).parse();
					name = where.getAbsolutePath()
							+ JDEECoOSGiPackagerConstants.PATH_SEPARATOR
							+ clazz.getPackageName()
									.replace(
											JDEECoOSGiPackagerConstants.PACKAGE_SEPARATOR,
											JDEECoOSGiPackagerConstants.PATH_SEPARATOR);
					tempF = new File(name);
					tempF.mkdirs();
					tempF = new File(name
							+ JDEECoOSGiPackagerConstants.PATH_SEPARATOR
							+ f.getName());
					tempF.createNewFile();
					name = clazz.getClassName();
					fis = new FileInputStream(f);
					fos = new FileOutputStream(tempF);
					fcSrc = fis.getChannel();
					fcDest = fos.getChannel();
					fcDest.transferFrom(fcSrc, 0, fcSrc.size());
					// TODO check if it is component or ensemble
					if (!classNames.contains(name))
						classNames.add(name);
					System.out.println("  Class added: " + name);
				} finally {
					if (fcSrc != null)
						fcSrc.close();
					if (fcDest != null)
						fcDest.close();
					if (fis != null)
						fis.close();
					if (fos != null)
						fos.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static List<String> getClassNames(String jarPath) {
		try {
			List<String> result = new LinkedList<String>();
			JarFile jarFile = null;
			try {
				jarFile = new JarFile(jarPath);
				Enumeration<JarEntry> enumeration = jarFile.entries();
				JarEntry je;
				JavaClass jc;
				while (enumeration.hasMoreElements()) {
					je = enumeration.nextElement();
					if (je.getName().endsWith(".class")) {
						try {
							jc = new ClassParser(jarPath, je.getName()).parse();
							result.add(jc.getClassName());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			} finally {
				if (jarFile != null)
					jarFile.close();
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static File updateJarFile(File originalFile, File toInclude,
			File where) {
		try {
			JarFile originalJar = new JarFile(originalFile);
			File destFile = new File(where, originalFile.getName());
			JarOutputStream jos = new JarOutputStream(new FileOutputStream(
					destFile));
			Enumeration<JarEntry> entries = originalJar.entries();
			InputStream is = null;
			byte[] buffer;
			JarEntry entry;
			try {
				while (entries.hasMoreElements()) {
					entry = entries.nextElement();
					is = originalJar.getInputStream(entry);
					jos.putNextEntry(new JarEntry(entry.getName()));
					buffer = new byte[4096];
					int bytesRead = 0;
					while ((bytesRead = is.read(buffer)) != -1) {
						jos.write(buffer, 0, bytesRead);
					}
					is.close();
					jos.flush();
					jos.closeEntry();
				}

				addJarResources(toInclude, null, jos);

			} finally {
				if (is != null)
					is.close();
				originalJar.close();
				jos.close();
			}
			return destFile;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static void addJarResources(File toAdd, String nestTo,
			JarOutputStream outputStream) throws IOException {
		BufferedInputStream bis = null;
		try {
			String name = "";
			if (nestTo != null) {
				if (!nestTo.endsWith("/"))
					nestTo += "/";
				name = nestTo;
			}
			if (toAdd.isDirectory()) {
				name += toAdd.getName().replace("\\", "/");
				if (!name.isEmpty()) {
					if (!name.endsWith("/"))
						name += "/";
					JarEntry entry = new JarEntry(name);
					entry.setTime(toAdd.lastModified());
					outputStream.putNextEntry(entry);
					outputStream.closeEntry();
				}
				for (File nestedFile : toAdd.listFiles())
					addJarResources(nestedFile, name, outputStream);
				return;
			}
			name += toAdd.getName().replace("\\", "/");
			JarEntry entry = new JarEntry(name);
			entry.setTime(toAdd.lastModified());
			outputStream.putNextEntry(entry);
			bis = new BufferedInputStream(new FileInputStream(toAdd));
			byte[] buffer = new byte[1024];
			while (true) {
				int count = bis.read(buffer);
				if (count == -1)
					break;
				outputStream.write(buffer, 0, count);
			}
			outputStream.closeEntry();
		} finally {
			if (bis != null)
				bis.close();
		}
	}

}
