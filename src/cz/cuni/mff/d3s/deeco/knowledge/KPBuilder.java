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

/**
 * Class providing utility functions for dealing with knowledge paths.
 * 
 * @author Michal Kit
 * 
 */
public class KPBuilder {
	public static final String PATH_DELIMITER = ".";

	/**
	 * Static function appending a new (tail) part to the existing one (root).
	 * New instance of String object is returned.
	 * 
	 * @param root
	 *            head part of the path
	 * @param tail
	 *            tail path of the path
	 * @return full path
	 */
	public static String appendToRoot(String root, String tail) {
		if (root == null || root.equals(""))
			return tail;
		if (tail == null || tail.equals(""))
			return root;
		return root + PATH_DELIMITER + tail;
	}
}
