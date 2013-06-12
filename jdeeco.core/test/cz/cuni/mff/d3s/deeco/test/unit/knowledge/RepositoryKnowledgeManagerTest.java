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

import cz.cuni.mff.d3s.deeco.exceptions.KMException;
import cz.cuni.mff.d3s.deeco.exceptions.KMNotExistentException;
import cz.cuni.mff.d3s.deeco.knowledge.ISession;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeRepository;
import cz.cuni.mff.d3s.deeco.knowledge.RepositoryKnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.local.LocalKnowledgeRepository;

@Test
public class RepositoryKnowledgeManagerTest {

//	private KnowledgeRepository kr = new LocalKnowledgeRepository();
//	private KnowledgeManager km = new RepositoryKnowledgeManager(kr);
//	private ISession session = kr.createSession();
//
//	@BeforeClass
//	public void putKnowledge() throws KMException {
//		Object obj = "simple test value";
//		km.alterKnowledge("some.key", obj, null);
//
//		km.alterKnowledge("another.key", "old", null);
//		km.alterKnowledge("another.key", "new", null);
//
//		session.begin();
//		while (session.repeat()) {
//			km.alterKnowledge("do.not.replace.path", "first", session);
//			km.alterKnowledge("do.not.replace.path", "second", session);
//			km.alterKnowledge("do.not.replace.path", "third", session);
//			session.end();
//		}
//	}
//
//	public void getKnowledge() throws KMException {
//		Object val = km.getKnowledge("some.key");
//		if (val instanceof Object []) {
//			if (((Object []) val).length > 0)
//				val = ((Object []) val)[0];
//		}
//		assertEquals(val.toString(), "simple test value");
//	}
//
//	public void getKnowledgeFromKeyWithUniqueValue() throws KMException {
//		Object val = km.getKnowledge("another.key");
//		assertEquals(val.toString(), "new");
//	}
//	
//	@Test(expectedExceptions = KMNotExistentException.class)
//	public void takeKnowledge() throws KMException {
//		Object val = km.takeKnowledge("some.key");
//		assertEquals(val.toString(), "simple test value");
//		val = km.takeKnowledge("some.key");
//	}
}
