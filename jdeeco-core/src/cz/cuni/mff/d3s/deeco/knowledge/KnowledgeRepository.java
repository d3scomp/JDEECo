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

import cz.cuni.mff.d3s.deeco.exceptions.KRExceptionAccessError;
import cz.cuni.mff.d3s.deeco.exceptions.KRExceptionUnavailableEntry;

/**
 * An abstract class specifing and defining basic operations on the knowledge
 * repository.
 * 
 * @author Michal Kit
 * 
 */
public abstract class KnowledgeRepository implements IKnowledgeRepository {

	@Override
	public Object [] get(String entryKey) throws KRExceptionUnavailableEntry,
			KRExceptionAccessError {
		return get(entryKey, null);
	}

	@Override
	public void put(String entryKey, Object value)
			throws KRExceptionAccessError {
		put(entryKey, value, null);
	}

	@Override
	public Object [] take(String entryKey) throws KRExceptionUnavailableEntry, KRExceptionAccessError {
		return take(entryKey, null);
	}
}
