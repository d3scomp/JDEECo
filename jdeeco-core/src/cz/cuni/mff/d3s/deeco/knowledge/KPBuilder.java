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

import cz.cuni.mff.d3s.deeco.path.grammar.PathGrammar;

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
		if (root == null || root.trim().equals(""))
			return tail;
		if (tail == null || tail.trim().equals(""))
			return root;
		return root + PathGrammar.PATH_SEPARATOR + tail;
	}

	public static String prependToRoot(String root, String head) {
		if (root == null || root.trim().equals(""))
			return head;
		if (head == null || head.trim().equals(""))
			return root;
		return head + PathGrammar.PATH_SEPARATOR + root;
	}

	public static String[] decomposePath(String knowledgePath) {
		if (knowledgePath == null || knowledgePath.trim().equals(""))
			return new String[] {};
		return knowledgePath.split("\\"+PATH_DELIMITER,-1);
	}

	public static String replaceHead(String pathWithHead, String newHead) {
		if (pathWithHead == null || pathWithHead.equals(""))
			return newHead;
		if (pathWithHead == null || pathWithHead.equals(""))
			return newHead;
		int index = pathWithHead
				.indexOf(PathGrammar.PATH_SEPARATOR);
		if (index < 0) 
			return newHead;
		 else
		return newHead
				+ pathWithHead.substring(index);
	}
}
