package cz.cuni.mff.d3s.deeco.processor;

import java.lang.reflect.Method;

/*
 * TODO: Comment is missing
 * Where is this used from? If it is not used then it should be removed.
 */

public class SchedulableProcessUtils {

	public static String getProcessId(Class<?> c, Method m) {
		if (c != null && m != null) {
			return m.getName() + "@" + c.getName();
		}
		return null;
	}
	
}
