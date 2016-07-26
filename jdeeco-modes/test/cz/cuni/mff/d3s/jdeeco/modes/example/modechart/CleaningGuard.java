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
package cz.cuni.mff.d3s.jdeeco.modes.example.modechart;

import java.util.List;

import cz.cuni.mff.d3s.deeco.modes.ModeGuard;
import cz.cuni.mff.d3s.deeco.task.ProcessContext;
import cz.cuni.mff.d3s.jdeeco.modes.example.DirtySpot;

public class CleaningGuard implements ModeGuard {

	@Override
	public String[] getKnowledgeNames() {
		return new String[] {"dirtySpots"};
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean isSatisfied(Object[] knowledgeValues) {
		if (!((List<DirtySpot>)knowledgeValues[0]).isEmpty()) {
			long currentTime = ProcessContext.getTimeProvider().getCurrentMilliseconds();
			System.out.println("##switching to: Cleaning at time " + currentTime);
			return true;
		}
		return false;
	}

}
