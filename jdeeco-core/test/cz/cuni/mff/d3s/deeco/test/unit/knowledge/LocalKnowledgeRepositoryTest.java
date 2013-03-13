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
package cz.cuni.mff.d3s.deeco.test.unit.knowledge;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cz.cuni.mff.d3s.deeco.exceptions.KRExceptionAccessError;
import cz.cuni.mff.d3s.deeco.exceptions.KRExceptionUnavailableEntry;
import cz.cuni.mff.d3s.deeco.knowledge.ISession;
import cz.cuni.mff.d3s.deeco.knowledge.local.LocalKnowledgeRepository;

@Test
public class LocalKnowledgeRepositoryTest {

	private LocalKnowledgeRepository kr = new LocalKnowledgeRepository();
	private ISession session = kr.createSession();

	@BeforeClass
	public void put() throws KRExceptionAccessError {
		session = null;
		kr.put("first.of.all", "test value", session);
		kr.put("34", new Integer(42), session);
		kr.put("3", "aaaa");
		kr.put("3", "bbbb");
		// session in LocalKnowledgeRepository should be revised.
	}

	public void get() throws KRExceptionUnavailableEntry,
			KRExceptionAccessError {
		Object val = kr.get("34", session);
		if (val instanceof Object []) {
			if (((Object []) val).length > 0)
				val = ((Object []) val)[0];
		}
		Object val2 = kr.get("first.of.all");
		if (val2 instanceof Object []) {
			if (((Object []) val2).length > 0)
				val2 = ((Object []) val2)[0];
		}
		assertEquals(val, 42);
		assertEquals(val2, "test value");
	}

	@Test(expectedExceptions = KRExceptionUnavailableEntry.class)
	public void take() throws KRExceptionUnavailableEntry,
			KRExceptionAccessError {
		Object val = kr.take("34");
		val = kr.take("34", session);
	}

	@Test(expectedExceptions = KRExceptionUnavailableEntry.class)
	public void getWithException() throws KRExceptionAccessError,
			KRExceptionUnavailableEntry {
		Object val = kr.get("does.not.exist", session);
	}

}
