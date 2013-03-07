/*******************************************************************************
 * Copyright 2012 Charles University in Prague
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package cz.cuni.mff.d3s.deeco.invokable;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;

import cz.cuni.mff.d3s.deeco.processor.MethodDescription;

/**
 * Base class representing a parameterized method.
 * 
 * @author Michal Kit
 * 
 */
public class ParameterizedMethod {
	
	/**
	 * The java method that represents the executable code.
	 * Cannot be serialized. 
	 */
	 private transient Method method = null;
	 
	
	/**
	 * Class  capturing serializable information that allows recreating 
	 * the method reference after serialization. 
	 * 
	 * @author Keznikl
	 *
	 */
	private class MethodDescription implements Serializable {
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
  
  /**
   *  Method class is not serializable.
   *  Thus the necessary information is serialized within this object.
   */
  private final MethodDescription methodDesc;

  /**
   * Input parameterTypes
   */
  public final List<Parameter> in;
  /**
   * Input/Output parameterTypes
   */
  public final List<Parameter> inOut;
  /**
   * Output parameterTypes
   */
  public final List<Parameter> out;

  public ParameterizedMethod(List<Parameter> in, List<Parameter> inOut, List<Parameter> out, Method method) {
    this.in = in;
    this.inOut = inOut;
    this.out = out;
    this.methodDesc = new MethodDescription(method);
  }
  
  public synchronized Method getMethod() {
	  if (method == null) {
		  method = methodDesc.getMethod();
	  }
	  return method;
  }
  
  /**
   * Invokes process method execution.
   * 
   * @param parameterTypes
   *          list of method parameterTypes
   * @return invocation result or null in case of an exception.
   */
  public Object invoke(Object[] parameters) {
    try {
      return getMethod().invoke(null, parameters);
    } catch (Exception e) {
      System.out.println("Method invocation error: " + e.getMessage());
      return null;
    }
  }

}
