package cz.cuni.mff.d3s.deeco.sde.packager;

import java.util.UUID;

public class JDEECoOSGiBundleNameGenerator {

	public static String generateBundleName() {
		return JDEECoOSGiPackagerConstants.BUNDLE + "-" + UUID.randomUUID().toString();
	}
}
