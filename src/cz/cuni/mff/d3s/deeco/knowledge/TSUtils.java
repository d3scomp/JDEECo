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
package cz.cuni.mff.d3s.deeco.knowledge;

import net.jini.space.JavaSpace05;

public class TSUtils {
	
	private static JavaSpace05 space = null;
	
	private static Object spaceLock = new Object();

	public static JavaSpace05 getSpace() {
		synchronized (spaceLock) {
			try {
				if (space == null) {
					Lookup lookup = new Lookup(JavaSpace05.class);
					space = (JavaSpace05) lookup.getService();
				}
				return space;
			} catch (Exception e) {
				System.out.println("ERROR - Space retrieval error: "
						+ e.getMessage());
				return null;
			}
		}
	}

	public static Tuple createTuple(String key, Object value) {
		Tuple result = new Tuple();
		result.key = key;
		result.value = value;
		return result;
	}

	public static Tuple createTemplate(String key) {
		Tuple result = new Tuple();
		result.key = key;
		return result;
	}
}
