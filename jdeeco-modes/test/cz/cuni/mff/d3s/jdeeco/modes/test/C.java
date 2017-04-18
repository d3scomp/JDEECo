/*******************************************************************************
 * Copyright 2017 Charles University in Prague
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
package cz.cuni.mff.d3s.jdeeco.modes.test;

import static org.junit.Assert.*;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;
import cz.cuni.mff.d3s.jdeeco.annotations.ComponentModeChart;
import cz.cuni.mff.d3s.jdeeco.annotations.ExcludeMode;
import cz.cuni.mff.d3s.jdeeco.annotations.ExcludeModes;
import cz.cuni.mff.d3s.jdeeco.annotations.Mode;
import cz.cuni.mff.d3s.jdeeco.annotations.Modes;


/**
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 *
 */
@Component
@ComponentModeChart(TestModeChartHolder.class)
public class C {

	///////////////////////////////////////////////////////////////////////////
	//     KNOWLEDGE                                                         //
	///////////////////////////////////////////////////////////////////////////
	
	public String id;
	public Boolean canTransit;
	public Integer inMode;
	public Integer oldMode;

	///////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////
	
	public C(){
		id = "C";
		canTransit = false;
		inMode = 1;
		oldMode = 0;
	}
		
	///////////////////////////////////////////////////////////////////////////
	//     PROCESSES                                                         //
	///////////////////////////////////////////////////////////////////////////
	
	@Process
	@ExcludeModes({})
	@PeriodicScheduling(period = 1000)
	public static void checkTransit(@In("inMode") Integer inMode,
			@InOut("oldMode") ParamHolder<Integer> oldMode,
			@InOut("canTransit") ParamHolder<Boolean> canTransit){
		if(canTransit.value){
			// Wait until a transition is taken
			return;
		}
		
		int oldModeInt = oldMode.value;
		switch(inMode){
		case 1:
			assertEquals("Unexpected transition taken.", 0, oldModeInt);
			oldMode.value = 1;
			canTransit.value = true;
			break;
		case 2:
			assertEquals("Unexpected transition taken.", 1, oldModeInt);
			oldMode.value = 2;
			canTransit.value = true;
			break;
		case 3:
			assertEquals("Unexpected transition taken.", 2, oldModeInt);
			oldMode.value = 3;
			canTransit.value = true;
			break;
		case 4:
			assertEquals("Unexpected transition taken.", 3, oldModeInt);
			oldMode.value = 4;
			canTransit.value = true;
			break;
		default:
			fail("C is in unknown mode.");
			break;
		}
	}
	
	@Process
	@Modes({M1.class, M2.class})
	@PeriodicScheduling(period = 500)
	public static void m1m2(@In("inMode") Integer inMode){
		assertTrue("Process m1m2 invoked in forbiden mode " + inMode,
				inMode == 1 || inMode == 2);
	}
	
	@Process
	@Mode(M3.class)
	@PeriodicScheduling(period = 500)
	public static void m3(@In("inMode") Integer inMode){
		assertTrue("Process m3 invoked in forbiden mode " + inMode,
				inMode == 3);
	}
	
	@Process
	@ExcludeMode(M1.class)
	@PeriodicScheduling(period = 500)
	public static void em1(@In("inMode") Integer inMode){
		assertTrue("Process em1 invoked in forbiden mode " + inMode,
				inMode != 1);
	}

	@Process
	@ExcludeModes({M3.class, M4.class})
	@PeriodicScheduling(period = 500)
	public static void em3_em4(@In("inMode") Integer inMode){
		assertTrue("Process em3_em4 invoked in forbiden mode " + inMode,
				inMode != 3 && inMode != 4);
	}
}
