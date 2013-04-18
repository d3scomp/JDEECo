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
package cz.cuni.mff.d3s.deeco.runtime;

import cz.cuni.mff.d3s.deeco.invokable.SchedulableProcess;

/**
 * Runtime utility methods.
 *
 * @author Petr Hnetynka
 */
public class RuntimeUtil {

  /**
   * <b>Should be called only component processes</b>; it returns a reference to the runtime.
   * If it is not called from a component process, there is no guarantee that it returns a meaningful value.
   * 
   * @return a reference to the runtime
   */
  public static IRuntime getRuntime() {
    return SchedulableProcess.runtime.get();
  }
}
