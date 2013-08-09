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
package cz.cuni.mff.d3s.deeco.knowledge;

import java.io.Serializable;

/**
 * Base class for representing knowledge.
 * 
 * It is extended by component knowledge and knowledge interfaces. Any class
 * extending <code>Knowledge</code> is decomposed when stored to the knowledge
 * repository.
 * 
 * @author Michal Kit
 * 
 */
public class Knowledge implements Serializable {
}
