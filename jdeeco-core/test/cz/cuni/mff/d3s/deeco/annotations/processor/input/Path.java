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
package cz.cuni.mff.d3s.deeco.annotations.processor.input;

import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.Knowledge;


/**
 * Class describing a robot path.
 * 
 * @author Michal Kit
 *
 */
@Knowledge
public class Path {
	
	public Integer currentPosition;
	public List<Integer> remainingPath;

	public Integer getNextPosition() {
		if (remainingPath.size() > 0) {
			return remainingPath.get(0);
		}
		return -1;
	}
}
