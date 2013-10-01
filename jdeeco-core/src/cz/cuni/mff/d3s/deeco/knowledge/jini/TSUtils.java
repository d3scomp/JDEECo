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
package cz.cuni.mff.d3s.deeco.knowledge.jini;

import net.jini.space.JavaSpace05;
import cz.cuni.mff.d3s.deeco.logging.Log;

/**
 * Utility class providing tuple space related functionalities.
 * 
 * @author Michal Kit
 * 
 */
public class TSUtils {

	private static JavaSpace05 space = null;

	/**
	 * Retrieves a java space object.
	 * 
	 * @return java space instance
	 */
	public synchronized static JavaSpace05 getSpace() {
		try {
			if (space == null) {
				Lookup lookup = new Lookup(JavaSpace05.class);
				space = (JavaSpace05) lookup.getService();
			}
			return space;
		} catch (Exception e) {
			Log.e("Space retrieval error", e);
			return null;
		}
	}

	/**
	 * Creates a <code>Tuple</code> instance, with specified <code>key</code>
	 * and <code>value</code>.
	 * 
	 * @param key
	 *            tuple key
	 * @param value
	 *            tuple value
	 * @return tuple instance
	 */
	public static Tuple createTuple(String key, Object value, long timestamp) {
		Tuple result = new Tuple();
		result.key = key;
		result.value = value;
		result.timestamp = timestamp;
		return result;
	}

	/**
	 * Creates a tuple, which is used as a template when searching through a
	 * knowledge repository.
	 * 
	 * @param key
	 *            template key (null is treated as a wildcard)
	 * @return tamplate
	 */
	public static Tuple createTemplate(String key) {
		Tuple result = new Tuple();
		result.key = key;
		return result;
	}
}
