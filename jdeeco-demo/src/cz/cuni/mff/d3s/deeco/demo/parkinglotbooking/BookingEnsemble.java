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
package cz.cuni.mff.d3s.deeco.demo.parkinglotbooking;

import cz.cuni.mff.d3s.deeco.annotations.Membership;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;

/**
 * Sample car-park booking ensemble class.
 * 
 * @author Jaroslav Keznikl
 *
 */


public class BookingEnsemble extends RequestResponseEnsemble {
	
	public final static long serialVersionUID = 1L;

	// must be public, static and extend Knowledge
	public static class CarInterface extends RequestResponseEnsemble.RequesterInterface {
		
		public final static long serialVersionUID = 1L;
		
		public Position targetPosition;
	}

	public static class CarParkInterface extends RequestResponseEnsemble.ResponderInterface {
		
		public final static long serialVersionUID = 1L;
		
		public Position position;
	}

	@Membership
	@PeriodicScheduling(2000)
	public static boolean membership(
			@In("member.targetPosition") Position targetPosition,
			@In("coord.position") Position position) {
		return targetPosition.equals(position);
	}

	
}
