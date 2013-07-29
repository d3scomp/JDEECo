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
package cz.cuni.mff.d3s.deeco.invokable.parameters;

import java.lang.reflect.Type;

import cz.cuni.mff.d3s.deeco.exceptions.ComponentEnsembleParseException;
import cz.cuni.mff.d3s.deeco.path.grammar.KnowledgePath;


/**
 * Class used to represent a method parameter.
 * This standard parameter has an additional field kPath to define the absolute knowledge path
 * describing where the knowledge is to be retrieved from the knowledge repository. 
 * 
 * @author Michal Kit
 * @author Julien Malvot
 *
 */
public class Parameter extends GenericParameter {
	
	private static final long serialVersionUID = -5306187392194189194L;

	public final KnowledgePath kPath;

	public Parameter(KnowledgePath kPath, Type type, Integer index) throws ComponentEnsembleParseException {
		super(type, index);
		this.kPath = kPath;
	}

}
