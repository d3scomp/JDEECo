/*******************************************************************************
 * Copyright 2014 Charles University in Prague
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
package cz.cuni.mff.d3s.jdeeco.simulation.demo;

import java.util.Random;

import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;

/**
 * A component that represents another kind of component; will only relay
 * knowledge of potential members and leaders.
 * 
 * @author Jaroslav Keznikl <keznikl@d3s.mff.cuni.cz>
 * 
 */
@Component 
public class OtherComponent {

	private static final long serialVersionUID = 5243757300436836781L;
	
	public String id;
	public Position position;

	public OtherComponent(String id, Position position) {
		this.id = id;		
		this.position = position;
	}
}
