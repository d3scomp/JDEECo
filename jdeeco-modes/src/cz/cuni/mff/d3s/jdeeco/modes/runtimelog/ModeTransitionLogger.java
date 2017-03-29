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
package cz.cuni.mff.d3s.jdeeco.modes.runtimelog;

import java.io.IOException;

import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.modes.DEECoMode;
import cz.cuni.mff.d3s.deeco.modes.ModeTransitionListener;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;
import cz.cuni.mff.d3s.deeco.task.ProcessContext;

/**
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 *
 */
public class ModeTransitionLogger implements ModeTransitionListener {

	public final DEECoMode oldMode;
	public final DEECoMode newMode;
	
	public ModeTransitionLogger(DEECoMode oldMode, DEECoMode newMode) {
		this.oldMode = oldMode;
		this.newMode = newMode;
	}
	
	@Override
	public String[] getKnowledgeNames() {
		return new String[]{"id"};
	}

	@Override
	public void transitionTaken(ParamHolder<?>[] knowledgeValues) {

		String robotId = (String) knowledgeValues[0].value;
		
		// Log the modes being switched
		ModeRecord record = new ModeRecord(robotId);
		record.setOldMode(oldMode);
		record.setNewMode(newMode);
		
		try {
			ProcessContext.getRuntimeLogger().log(record);
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
		}

		Log.i(String.format("%s switching from %s to %s", robotId, oldMode, newMode));
	}

}
