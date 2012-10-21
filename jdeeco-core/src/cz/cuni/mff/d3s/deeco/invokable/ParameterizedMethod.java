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

import java.lang.reflect.Method;
import java.util.List;

import cz.cuni.mff.d3s.deeco.invokable.creators.ParametrizedMethodCreator;

/**
 * Base class representing a parameterized method.
 * 
 * @author Michal Kit
 * 
 */
public class ParameterizedMethod {
  
  public final transient Method method;

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

  public ParameterizedMethod(ParametrizedMethodCreator creator) {
    this.in = creator.in;
    this.inOut = creator.inOut;
    this.out = creator.out;
    this.method = creator.methodDesc.getMethod();
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
      return method.invoke(null, parameters);
    } catch (Exception e) {
      System.out.println("Method invocation error: " + e.getMessage());
      return null;
    }
  }

}
