/*******************************************************************************
 * Copyright 2014 Charles University in Prague
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
package cz.cuni.mff.d3s.deeco.jpf;

import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;


import cz.cuni.mff.d3s.deeco.annotations.pathparser.ParseException;
import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessor;
import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.knowledge.*;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;
import cz.cuni.mff.d3s.deeco.model.runtime.custom.RuntimeMetadataFactoryExt;
import cz.cuni.mff.d3s.ltl.CommlinkSuTJPF;



/**
 * Implementation of the knowledge repository using a hashmap optimized for JPF
 * 
 * This implementation allows only for local execution.
 * It is based on the {@link cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager}.
 * It allows for evaluating atomic propositions over the component knowledge after each change.
 * 
 * @author Jaroslav Keznikl
 * 
 */
public class KnowledgeManagerContainerJPF extends KnowledgeManagerContainer implements KnowledgeJPF {

    /**
     * Lock necessary to ensure atomic update of atomic propositions for LTL checking
     */
    protected final ReentrantLock lock = new ReentrantLock();

    public HashMap<String, Boolean> propositionValues = new HashMap<>();
	public HashMap<String, Boolean> propositionToEvaluate = new HashMap<>();
	
	boolean evaluatePropositions = false;
	
	List<AtomicProposition> propositions = new ArrayList<>();

    AnnotationProcessor ap;


    public KnowledgeManagerContainerJPF(List<AtomicProposition> propositions) {
        ap = new AnnotationProcessor(RuntimeMetadataFactoryExt.eINSTANCE);

		this.propositions = propositions;
		for (AtomicProposition ap : propositions) {
			propositionToEvaluate.put(ap.getName(), true);
			propositionValues.put(ap.getName(), false);
		}
		
		// JPF optimization
		// class initialization if the lock is used causes state space explosion
		// here we intentionally use the lock -> it will hopefully execute all class 
		// initializers in single threaded code and reduce number of program states
		// problematic class: java.util.concurrent.locks.AbstractQueuedSynchronizer$Node
		// problematic class java.util.concurrent.locks.LockSupport

		Condition c = lock.newCondition();
		try {
			lock.lock();
			c.awaitNanos(-1); // Not sleep at all
			lock.unlock();
		} catch (InterruptedException e) {
		}
		LockSupport.unpark(null);
	}


    /**
     * TODO
     * @param knowledgePath
     *         the knowledge path to return
     * @return
     * @throws KnowledgeNotFoundException
     * @throws IllegalArgumentException
     */
	@Override
	public Object get(String knowledgePath) throws KnowledgeNotFoundException, IllegalArgumentException {
        try {
            // the first part of the path is the component id
            String[] pathParts = knowledgePath.split("\\.");
            if (pathParts.length < 2) {
                throw new ParseException();
            }

            String componentId = pathParts[0];
            KnowledgeManager km =  getLocal(componentId);

            KnowledgePath kp = ap.createSimplePath(knowledgePath);
            ValueSet vs = km.get(Arrays.asList(kp));
            return vs.getValue(kp);

        } catch (KnowledgeNotFoundException e) {
            throw e;
        } catch (ParseException | AnnotationProcessorException e) {
            Log.e("Invalid knowledge path: " + knowledgePath);
            throw new IllegalArgumentException(e);
        }
    }

	// TODO: manage via runtime event listener mechanism (to be done) instead
	public void onStart() {
		evaluatePropositions = true;
		tryEvaluatePropositions();
	}
	
	// TODO: manage via runtime event listener mechanism (to be done) instead
	public void onStop() {
		evaluatePropositions = false;
	}
	
	private void tryEvaluatePropositions() {
		if (evaluatePropositions) {
			for (AtomicProposition ap : propositions) {
				// propositionToEvaluate.get(...) might return null 
				if (propositionToEvaluate.get(ap.getName()) == true) {
					boolean value = propositionValues.get(ap.getName());
					try {
						value = ap.evaluate(this);
					} catch (Exception e) {
						Log.e("Atomic proposition evaluation failed (" + ap.getName() + ").");
					}					
					propositionValues.put(ap.getName(), value);
				}
			}
	
			// send names of atomic propositions into JPF
			// we consider only propositions that evaluate to "true" in the current state
			CommlinkSuTJPF.notifyEventProcessingStart();
			for (AtomicProposition ap : propositions) 
			{
				Boolean apVal = propositionValues.get(ap.getName());
				if ((apVal != null) && apVal.booleanValue()) CommlinkSuTJPF.addTrueAtomicProposition(ap.getName());
			}
			CommlinkSuTJPF.notifyAtomicPropositionsComplete();		
		}
	}


    /**
     * Creates a new instance of local knowledge manager that is an adaptor over the original implementation,
     * the adaptor takes care of evaluating atomic propositions on update.
     *
     * @param id
     *            the identifier of the knowledge manager
     * @return {@link KnowledgeManager} the newly created KnowledgeManager.
     */
    @Override
    public KnowledgeManager createLocal(String id) {
        KnowledgeManager res = super.createLocal(id);
        if (res == null)
            return null;
        else
            return new KnowledgeManagerJPFAdaptor(res);
    }

    /**
     * An adaptor that globally synchronizes calls on {@link KnowledgeManager#update(cz.cuni.mff.d3s.deeco.knowledge.ChangeSet)} and calls
     * {@link KnowledgeManagerContainerJPF#tryEvaluatePropositions()} after every update.
     *
     * @author Keznikl
     */
    private class KnowledgeManagerJPFAdaptor implements KnowledgeManager {
        KnowledgeManager km;

        public KnowledgeManagerJPFAdaptor(KnowledgeManager km) {
            this.km = km;
        }


        @Override
        public void update(ChangeSet changeSet) throws KnowledgeUpdateException {
            // make sure that when outside of a session, put + evaluation of propositions is done atomically
            lock.lock();

            km.update(changeSet);

            tryEvaluatePropositions();

            lock.unlock();
        }

        @Override
        public ValueSet get(Collection<KnowledgePath> knowledgeReferenceList) throws KnowledgeNotFoundException {
            return km.get(knowledgeReferenceList);
        }

        @Override
        public void register(Trigger trigger, TriggerListener triggerListener) {
            km.register(trigger, triggerListener);
        }

        @Override
        public void unregister(Trigger trigger, TriggerListener triggerListener) {
            km.unregister(trigger, triggerListener);
        }

        @Override
        public String getId() {
            return km.getId();
        }

    }

}
