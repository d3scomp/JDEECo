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

import java.io.Serializable;

/**
 * Base class used to wrap output parameterTypes, making them mutable from the level
 * of a function.
 * 
 * @author Michal Kit
 * 
 * @param <T>
 *            wrapped item type
 */
public class OutWrapper<T> implements Serializable {
	/**
	 * Wrapped item.
	 */
	public T item;

	public OutWrapper() {
		item = null;
	}

	public OutWrapper(T item) {
		this.item = item;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof OutWrapper<?>))
			return false;
		OutWrapper<?> other = (OutWrapper<?>) obj;
		return item == null ? other.item == null : item.equals(other.item);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return item == null ? 0 : item.hashCode();
	}
}
