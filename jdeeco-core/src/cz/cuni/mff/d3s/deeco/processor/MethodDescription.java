package cz.cuni.mff.d3s.deeco.processor;

import java.io.Serializable;
import java.lang.reflect.Method;

public class MethodDescription implements Serializable {
	private static final long serialVersionUID = -8932122303127301918L;

	public final Class<?> declaringClass; // In which class the method is declared
	public final String methodName;
	public final Class<?> [] parameterTypes;
	
	public MethodDescription(Method method) {
		this.declaringClass = method.getDeclaringClass();
		this.methodName = method.getName();
		this.parameterTypes = method.getParameterTypes();
	}
	
	public Method getMethod() {
		try {
			return declaringClass.getMethod(methodName, parameterTypes);
		} catch (SecurityException e) {
			System.err.println("Extracting method exception.");
			System.err.println(e);
		} catch (NoSuchMethodException e) {
			System.err.println("Extracting method exception.");
			System.err.println(e);
		}
		
		return null;
	}
}
