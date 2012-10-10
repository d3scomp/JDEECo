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
 * ConstantKeys class contains static fields definitions used while managing
 * knowledge.
 * 
 * 
 * @author Michal Kit
 * 
 */
public class ConstantKeys {
	/**
	 * Defines the field name for the root knowledge unique identifier in the
	 * ComponentKnowledge class.
	 */
	public static final String ROOT_KNOWLEDGE_ID = "id";
	/**
	 * Defines identifier used in the knowledge repository to store knowledge
	 * class definition.
	 */
	public static final String STRUCTURE_ID = "#structure";
	/**
	 * Key that is prepended to the knowledge path to point out that should be
	 * listened for.
	 */
	public static final String LISTEN_ID = "#listen";
	
	public static final String UNDEFINED = "undefined";
	
	public static final String BIN_FILE_NAME = "deecoSerializedObjects.bin";

}
