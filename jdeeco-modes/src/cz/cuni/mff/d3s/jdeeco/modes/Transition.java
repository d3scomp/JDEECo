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
package cz.cuni.mff.d3s.jdeeco.modes;

import cz.cuni.mff.d3s.deeco.modes.DEECoMode;
import cz.cuni.mff.d3s.deeco.modes.DEECoTransition;
import cz.cuni.mff.d3s.deeco.modes.ModeGuard;

/**
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 *
 */
public class Transition implements DEECoTransition {

	private final DEECoMode from;
	private final DEECoMode to;
	private int priority;
	private final ModeGuard guard;
	
	public Transition(DEECoMode from, DEECoMode to, ModeGuard guard){
		if (from == null) throw new IllegalArgumentException(
				String.format("The \"%s\" argument is null.", "from"));
		if (to == null) throw new IllegalArgumentException(
				String.format("The \"%s\" argument is null.", "to"));
		if (guard == null) throw new IllegalArgumentException(
				String.format("The \"%s\" argument is null.", "guard"));
		
		this.from = from;
		this.to= to;
		this.guard = guard;
		priority = 0;
	}
	
	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.modes.DEECoTransition#getFrom()
	 */
	@Override
	public DEECoMode getFrom() {
		return from;
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.modes.DEECoTransition#getTo()
	 */
	@Override
	public DEECoMode getTo() {
		return to;
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.modes.DEECoTransition#getPriority()
	 */
	@Override
	public int getPriority() {
		return priority;
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.modes.DEECoTransition#setPriority(int)
	 */
	@Override
	public void setPriority(int priority) {
		this.priority = priority;
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.modes.DEECoTransition#getGuard()
	 */
	@Override
	public ModeGuard getGuard() {
		return guard;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof DEECoTransition){
			DEECoTransition other = (DEECoTransition) obj;
			return this.from.equals(other.getFrom())
					&& this.to.equals(other.getTo());
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("%s -> %s", from, to);
	}
}
