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

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;

import cz.cuni.mff.d3s.deeco.exceptions.KMException;
import cz.cuni.mff.d3s.deeco.exceptions.KMNotExistentException;
import cz.cuni.mff.d3s.deeco.exceptions.UnavailableEntryException;
import cz.cuni.mff.d3s.deeco.knowledge.ISession;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeRepository;
import cz.cuni.mff.d3s.deeco.knowledge.RepositoryKnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.local.LocalKnowledgeRepository;

@Test
public class RepositoryKnowledgeManagerTest {

	private KnowledgeRepository kr = new LocalKnowledgeRepository();
	private KnowledgeManager km = new RepositoryKnowledgeManager(kr);
	private ISession session = kr.createSession();

	@BeforeClass
	public void putKnowledge() throws KMException {
		Object obj = "simple test value";
		km.putKnowledge("some.key", obj, null);

		km.putKnowledge("another.key", "old", null);
		km.putKnowledge("another.key", "new", null);

		session.begin();
		while (session.repeat()) {
			km.putKnowledge("do.not.replace.path", "first", null, session, false);
			km.putKnowledge("do.not.replace.path", "second", null, session,
					false);
			km.putKnowledge("do.not.replace.path", "third", null, session,
					false);
			session.end();
		}
	}

	public void getKnowledge() throws KMException {
		Object val = km.getKnowledge("some.key", String.class);
		assertEquals(val.toString(), "simple test value");
	}

	public void getKnowledgeFromKeyWithUniqueValue() throws KMException {
		Object val = km.getKnowledge("another.key", String.class);
		assertEquals(val.toString(), "new");
	}

	public void getKnowledgeFromKeyWithMultipleValues() throws KMException {
		Object[] vals = (Object[]) km.getKnowledge("do.not.replace.path", null);
		assertEquals(vals.length, 3);
		assertEquals(vals[0].toString(), "first");
		assertEquals(vals[1].toString(), "second");
		assertEquals(vals[2].toString(), "third");
	}
	
	@Test(expectedExceptions = KMNotExistentException.class)
	public void takeKnowledge() throws KMException {
		Object val = km.takeKnowledge("some.key", String.class);
		assertEquals(val.toString(), "simple test value");
		val = km.takeKnowledge("some.key", String.class);
	}
}
