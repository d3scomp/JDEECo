package cz.cuni.mff.d3s.deeco.processor;

import java.io.Serializable;
import java.lang.reflect.Method;

public class MethodDescription implements Serializable {
	public String methodName;
	public Class<?> [] parameterTypes;
	
	public MethodDescription(String methodName, Class<?> [] parameterTypes) {
		this.methodName = methodName;
		this.parameterTypes = parameterTypes;
	}
	
	public MethodDescription(Method method) {
		this.methodName = method.getName();
		this.parameterTypes = method.getParameterTypes();
	}
}
