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

import cz.cuni.mff.d3s.deeco.exceptions.SessionException;

/**
 * Interface representing session, which is used for performing bulk of
 * operations on the knowledge repository. It is used to guarantee data
 * consistency in the knowledge repository.
 * 
 * @author Michal Kit
 * 
 */
public interface ISession {

	/**
	 * Starts this session.
	 */
	public void begin();

	/**
	 * Ends this session.
	 * 
	 * @throws SessionException
	 *             thrown when the session is required to be stopped before its
	 *             start
	 */
	public void end() throws SessionException;

	/**
	 * Cancels this session.
	 * 
	 * @throws SessionException
	 *             thrown when the session is canceled after it was ended
	 */
	public void cancel() throws SessionException;

	/**
	 * Checks if this session should be repeated due to either it has
	 * not been started yet or an error which occurred during the session
	 * ending.
	 * 
	 * @return true or false depending on the current session state
	 */
	public boolean repeat();

	/**
	 * Checks if this session has been successfully ended.
	 * 
	 * @return true or false depending on the current session state
	 */
	public boolean hasSucceeded();
}
