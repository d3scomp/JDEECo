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
package cz.cuni.mff.d3s.deeco.demo;

import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.DEECoCoordinatorIn;
import cz.cuni.mff.d3s.deeco.annotations.DEECoEnsemble;
import cz.cuni.mff.d3s.deeco.annotations.DEECoEnsembleMapper;
import cz.cuni.mff.d3s.deeco.annotations.DEECoEnsembleMembership;
import cz.cuni.mff.d3s.deeco.annotations.DEECoMemberIn;
import cz.cuni.mff.d3s.deeco.annotations.DEECoMemberOut;
import cz.cuni.mff.d3s.deeco.annotations.DEECoPeriodicScheduling;
import cz.cuni.mff.d3s.deeco.knowledge.Knowledge;

@DEECoEnsemble
@DEECoPeriodicScheduling(2000)
public class ConvoyEnsemble {

	// must be public, static and extend Knowledge
	public static class ConvoyOutInterface extends Knowledge {
		public String convoyRobot;
	}

	public static class EnsemblePath extends Knowledge {
		public Integer currentPosition;
		public List<Integer> remainingPath;
	}

	@DEECoEnsembleMembership
	public static boolean membership(@DEECoMemberIn("id") String mId,
			@DEECoMemberIn("path.remainingPath") List<Integer> mRemainingPath,
			@DEECoCoordinatorIn("id") String cId,
			@DEECoCoordinatorIn("path") EnsemblePath cPath) {
		if (!mId.equals(cId)) {
			return mRemainingPath.size() > 0
					&& cPath.remainingPath.size() > 0
					&& cPath.currentPosition
							.equals(getNextPosition(mRemainingPath));
		}
		return false;
	}

	@DEECoEnsembleMapper
	public static void map(@DEECoMemberOut ConvoyOutInterface mOutCR,
			@DEECoCoordinatorIn("path.remainingPath") List<Integer> cRemainingPath) {
		mOutCR.convoyRobot = "1";
	}

	public static Integer getNextPosition(List<Integer> remainingPath) {
		if (remainingPath.size() > 0) {
			return remainingPath.get(0);
		}
		return -1;
	}
}
