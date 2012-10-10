package cz.cuni.mff.d3s.deeco.processor;

import java.lang.reflect.Method;

public class SchedulableProcessUtils {

	public static String getProcessId(Class<?> c, Method m) {
		if (c != null && m != null) {
			return m.getName() + "@" + c.getName();
		}
		return null;
	}
	
}
