/*******************************************************************************
 * Copyright 2015 Charles University in Prague
 *  
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *******************************************************************************/
package cz.cuni.mff.d3s.deeco.modes;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class ModeGuard {
	
	protected final Map<String, Double> parameters;
	
	public ModeGuard(){
		parameters = new HashMap<>();
		specifyParameters();
	}
	
	public Map<String, Double> getParameters(){
		return Collections.unmodifiableMap(parameters);
	}
	
	public void setParameter(String name, double value){
		if(parameters.containsKey(name)){
			parameters.put(name, value);
		} else {
			throw new IllegalArgumentException(String.format(
					"The %s parameter doesn't exists in %s",
					name, this.getClass().getName()));
		}
	}
	
	abstract protected void specifyParameters();
	
	public abstract String[] getKnowledgeNames();
	
	public abstract boolean isSatisfied(Object [] knowledgeValues);

}
