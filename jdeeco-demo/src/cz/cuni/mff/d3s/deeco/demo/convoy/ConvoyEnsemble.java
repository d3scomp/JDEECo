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
package cz.cuni.mff.d3s.deeco.demo.convoy;

import java.util.List;
import java.util.Random;

import cz.cuni.mff.d3s.deeco.annotations.DEECoEnsemble;
import cz.cuni.mff.d3s.deeco.annotations.DEECoEnsembleMapper;
import cz.cuni.mff.d3s.deeco.annotations.DEECoEnsembleMembership;
import cz.cuni.mff.d3s.deeco.annotations.DEECoIn;
import cz.cuni.mff.d3s.deeco.annotations.DEECoOut;
import cz.cuni.mff.d3s.deeco.annotations.DEECoPeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.DEECoTrigger;
import cz.cuni.mff.d3s.deeco.ensemble.Ensemble;
import cz.cuni.mff.d3s.deeco.knowledge.Knowledge;

/**
 * Sample ensemble class.
 * 
 * @author Michal Kit
 *
 */
@DEECoEnsemble
@DEECoPeriodicScheduling(1000)
public class ConvoyEnsemble extends Ensemble {

	// must be public, static and extend Knowledge
	public static class ConvoyOutInterface extends Knowledge {
		public String convoyRobot;
	}

	public static class EnsemblePath extends Knowledge {
		public Integer currentPosition;
		public List<Integer> remainingPath;
	}

	@DEECoEnsembleMembership(.6)
	public static double membership(@DEECoIn("member.id") String mId,
			@DEECoIn("member.path.remainingPath") List<Integer> mRemainingPath,
			@DEECoIn("coord.id") String cId,
			@DEECoIn("coord.path") @DEECoTrigger EnsemblePath cPath) {
		//System.out.println("[ConvoyEnsemble.membership] mId = " + mId + ", mRemainingPath = " + mRemainingPath + ", cId = " + cId + ", cRemainingPath = " + cPath.remainingPath + ", cCurrentPosition = " + cPath.currentPosition);
		if (!mId.equals(cId)) {
			if (mRemainingPath.size() > 0
					&& cPath.remainingPath.size() > 0
					&& cPath.currentPosition
							.equals(getNextPosition(mRemainingPath)))
			{
				//System.out.println("[ConvoyEnsemble.membership] result = 0.5");
				return .5;
			}
		}
		//System.out.println("[ConvoyEnsemble.membership] result = 0.7");
		return .7;
	}

	@DEECoEnsembleMapper
	public static void map(@DEECoOut("member") ConvoyOutInterface mOutCR,
			@DEECoIn("coord.path.remainingPath") List<Integer> cRemainingPath) {
		mOutCR.convoyRobot = Integer.toString(new Random().nextInt());
		//System.out.println("[ConvoyEnsemble.map] convoyRobot = " + mOutCR.convoyRobot);
	}

	public static Integer getNextPosition(List<Integer> remainingPath) {
		if (remainingPath.size() > 0) {
			return remainingPath.get(0);
		}
		return -1;
	}
}
