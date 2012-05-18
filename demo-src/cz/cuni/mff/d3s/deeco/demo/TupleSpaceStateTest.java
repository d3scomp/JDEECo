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

import java.rmi.RMISecurityManager;
import java.util.Arrays;

import net.jini.core.lease.Lease;
import net.jini.space.JavaSpace05;
import net.jini.space.MatchSet;
import cz.cuni.mff.d3s.deeco.knowledge.TSUtils;
import cz.cuni.mff.d3s.deeco.knowledge.Tuple;

public class TupleSpaceStateTest {
	
	public static void main(String[] args) {
		if (System.getSecurityManager() == null)
			System.setSecurityManager(new RMISecurityManager());
		try {
			JavaSpace05 space = TSUtils.getSpace();
			MatchSet result = space.contents(Arrays
					.asList(new Object[] { TSUtils.createTemplate(null) }),
					null, Lease.FOREVER, Long.MAX_VALUE);
			Tuple t = (Tuple) result.next();
			System.out.println("Printing Tuple Space state:");
			while (t != null) {
				System.out.println("   " + t.key + ": " + t.value);
				t = (Tuple) result.next();
			}
		} catch (Exception e) {
			System.out.println("sdfasdfasdf");
		}
	}

}
