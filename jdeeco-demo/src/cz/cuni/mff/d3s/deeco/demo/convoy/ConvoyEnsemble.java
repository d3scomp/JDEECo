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

import cz.cuni.mff.d3s.deeco.annotations.KnowledgeExchange;
import cz.cuni.mff.d3s.deeco.annotations.Membership;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.TriggerOnChange;
import cz.cuni.mff.d3s.deeco.ensemble.Ensemble;
import cz.cuni.mff.d3s.deeco.knowledge.Knowledge;

/**
 * Sample ensemble class.
 * 
 * @author Michal Kit
 *
 */

@PeriodicScheduling(1000)
public class ConvoyEnsemble extends Ensemble {

	// must be public, static and extend Knowledge
	public static class ConvoyOutInterface extends Knowledge {
		public String convoyRobot;
	}

	public static class EnsemblePath extends Knowledge {
		public Integer currentPosition;
		public List<Integer> remainingPath;
	}

	@Membership
	public static boolean membership(@In("member.id") String mId,
			@In("member.path.remainingPath") List<Integer> mRemainingPath,
			@In("coord.id") String cId,
			@In("coord.path") @TriggerOnChange EnsemblePath cPath) {
		//System.out.println("[ConvoyEnsemble.membership] mId = " + mId + ", mRemainingPath = " + mRemainingPath + ", cId = " + cId + ", cRemainingPath = " + cPath.remainingPath + ", cCurrentPosition = " + cPath.currentPosition);
		if (!mId.equals(cId)) {
			if (mRemainingPath.size() > 0
					&& cPath.remainingPath.size() > 0
					&& cPath.currentPosition
							.equals(getNextPosition(mRemainingPath)))
			{
				//System.out.println("[ConvoyEnsemble.membership] result = 0.5");
				return false;
			}
		}
		//System.out.println("[ConvoyEnsemble.membership] result = 0.7");
		return true;
	}

	@KnowledgeExchange
	public static void map(@Out("member") ConvoyOutInterface mOutCR,
			@In("coord.path.remainingPath") List<Integer> cRemainingPath) {
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
