/*******************************************************************************
 * Copyright 2015 Charles University in Prague
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cz.cuni.mff.d3s.deeco.knowledge.ChangeSet;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeNotFoundException;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeUpdateException;
import cz.cuni.mff.d3s.deeco.knowledge.ValueSet;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeField;
import cz.cuni.mff.d3s.deeco.model.runtime.custom.RuntimeMetadataFactoryExt;
import cz.cuni.mff.d3s.deeco.modes.DEECoMode;
import cz.cuni.mff.d3s.deeco.modes.DEECoTransition;
import cz.cuni.mff.d3s.deeco.modes.DEECoModeChart;
import cz.cuni.mff.d3s.deeco.modes.DEECoTransitionListener;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;

// TODO: make tests
public class ModeChartImpl extends DEECoModeChart{
	
//	public static final double MODE_NOT_FOUND = -1;
//	
//	public static final double TRANSITION_NOT_FOUND = -2;
	
//	public static final Random rand = new Random(78631);
//	
//	public Map<Class<? extends DEECoMode>, Set<ModeSuccessor>> modes;
	
	/**
	 * Indicates whether the mode chart was modified at runtime
	 */
//	private boolean modified;
	
	/**
	 * Internal constructor enables {@link ModeChartFactory} to be the
	 * Privileged creator of {link {@link ModeChartImpl} instances.
	 */
	public ModeChartImpl(ComponentInstance component) {
		super(component);
		
//		currentMode = null;
//		modes = new HashMap<>();
//		transitionListeners = new HashMap<>();
//		modified = false;
	}
	
	public void setInitialMode(DEECoMode mode){
		if(mode == null){
			throw new IllegalArgumentException(String.format(
					"The %s argument is null.", "mode"));
		}
		
		if(!modes.contains(mode)){
			throw new IllegalArgumentException(String.format(
					"The given mode %s doesn't appear in the mode chart.", mode));
		}
		
		currentMode = mode;
	}
	
	public void setModes(Set<DEECoMode> modes){
		if(modes == null){
			throw new IllegalArgumentException(String.format(
					"The %s argument is null.", "modes"));
		}
		if(!this.modes.isEmpty()){
			throw new IllegalStateException("Multiple assignment of modes to a mode chart.");
		}
		
		this.modes.addAll(modes);
	}

	public void setTransitions(Set<DEECoTransition> transitions){
		if(transitions == null){
			throw new IllegalArgumentException(String.format(
					"The %s argument is null.", "transitions"));
		}
		if(!this.transitions.isEmpty()){
			throw new IllegalStateException("Multiple assignment of transitions to a mode chart.");
		}
		
		this.transitions.addAll(transitions);	
	}
	
	public void setTransitionListeners(Map<DEECoTransition, List<DEECoTransitionListener>> listeners){
		if(listeners == null){
			throw new IllegalArgumentException(String.format(
					"The %s argument is null.", "listeners"));
		}
		
		// Check whether all transition for which listeners are added are present
		for(DEECoTransition transition : listeners.keySet()){
			if(!transitions.contains(transition)){
				throw new IllegalStateException(String.format(
						"The transition %s required by listener %s not found.",
						transition, listeners.get(transition)));
			}	
		}
		
		transitionListeners.putAll(listeners);
	}
		
	
//	public void wasModified(){
//		modified = true;
//	}
	
//	public boolean isModified(){
//		return modified;
//	}
	
//	void setInitialNode(Class<? extends DEECoMode> mode){
//		currentMode = mode;
//	}
	
//	Class<? extends DEECoMode> getInitialMode(){
//		return currentMode;
//	}

//	@Override
//	public Set<Class<? extends DEECoMode>> getModes() {
//		Set<Class<? extends DEECoMode>> allModes = new HashSet<>();
//		// Put all the modes from keySet
//		allModes.addAll(modes.keySet());
//		// Put the rest of the modes that are not in the key set
//		for(Class<? extends DEECoMode> mode : modes.keySet()){
//			allModes.addAll(getSuccessors(mode));
//		}
//
//		// Prevent modification attempts
//		return Collections.unmodifiableSet(allModes);
//	}
	
	@Override
	public DEECoMode switchMode(){
		
		// Switch mode only if there is a transition from it
		Set<DEECoTransition> out = getTransitionsFrom(currentMode);
		if(out.isEmpty()){
			return currentMode;
		}
		
		// Sort transitions by priority
		List<DEECoTransition> sortedOut = sortByPriority(out);
		
		// Switch first satisfied transition
		for(DEECoTransition transition : sortedOut){
			String[] knowledge = transition.getGuard().getKnowledgeNames();
			Object[] values = getValues(knowledge);
			if(transition.getGuard().isSatisfied(values)){
				// Call the transition listeners before the mode is switched
				invokeTransitionListeners(transition);
				currentMode = transition.getTo();
				break;
			}
		}
		
		return currentMode;
		
//		if(modes.containsKey(currentMode)){
//			// Get successor modes
//			List<ModeSuccessor> successors = new ArrayList<>(modes.get(currentMode));
//			// Filter out inapplicable transitions
//			for(ModeSuccessor succ : successors.toArray(new ModeSuccessor[]{})){
//				String[] knowledge = succ.guard.getKnowledgeNames();
//				Object[] values = getValues(knowledge);
//				//System.out.format("Knowledge: %s Value %s%n", knowledge, String.valueOf(value));
//				if(!succ.guard.isSatisfied(values)){
//					successors.remove(succ);
//				}
//			}
//			// Sort according to the probabilities
//			Collections.sort(successors, new Comparator<ModeSuccessor>(){
//				@Override
//				public int compare(ModeSuccessor s1, ModeSuccessor s2) {
//					return Double.compare(s1.probability, s2.probability);
//				}
//			});
//			// Check probability consistency
//			double probabilitySum = 0;
//			for(ModeSuccessor s : successors){
//				probabilitySum += s.probability;
//			}
//			if(probabilitySum > 1){
//				StringBuilder builder = new StringBuilder();
//				for(ModeSuccessor succ : successors){
//					builder.append("\n").append(succ.getTypeName())
//						.append(" probability = ").append(succ.probability);
//				}
//				Log.e("The probabilities of these satisfied mode successors"
//						+ " of " + currentMode.getTypeName()  
//						+ " is greater than 1 and will lead to unconsistent behavior:"
//						+ builder.toString());
//			}
//			// switch nondeterministically
//			double random = rand.nextDouble();
//			double successorTreshold = 0;
//			for(ModeSuccessor s : successors){
//				successorTreshold += s.probability;
//				if(random < successorTreshold){
//					// Call the transition listeners before the mode is switched
//					invokeTransitionListeners(currentMode, s.successor);
//					// Switch the mode
//					currentMode = s.successor;
//					break;
//				}
//			}
//		}
//
//		return currentMode;
	}
	
	private Set<DEECoTransition> getTransitionsFrom(DEECoMode mode){
		Set<DEECoTransition> outgoing = new HashSet<>();
		for(DEECoTransition transition : transitions){
			if(transition.getFrom() == mode){
				outgoing.add(transition);
			}
		}
		
		return outgoing;
	}
	
	private List<DEECoTransition> sortByPriority(Set<DEECoTransition> transitions){
		List<DEECoTransition> sorted = new ArrayList<>(transitions);
		sorted.sort(Comparator.comparing(t -> -t.getPriority())); // TODO: debug
		
		return sorted;
	}
	
	private void invokeTransitionListeners(DEECoTransition transition){
		if(transitionListeners.containsKey(transition)){
			for(DEECoTransitionListener transitionListener : transitionListeners.get(transition)){
				// Get the knowledge values
				String[] knowledge = transitionListener.getKnowledgeNames();
				ParamHolder<?>[] values = getValuesForUpdate(knowledge);
				// Call the listener with proper values
				transitionListener.transitionTaken(values);
				updateValues(knowledge, values);	
			}
		}
	}

	private Object[] getValues(String[] knowledgeNames) {
		List<KnowledgePath> paths = new ArrayList<>();
		for(String knowledgeName : knowledgeNames){
			KnowledgePath path = RuntimeMetadataFactoryExt.eINSTANCE.createKnowledgePath();
			PathNodeField pNode = RuntimeMetadataFactoryExt.eINSTANCE.createPathNodeField();
			pNode.setName(knowledgeName);
			path.getNodes().add(pNode);
			paths.add(path);
		}
		try {
			ValueSet vSet = component.getKnowledgeManager().get(paths);
			Object[] values = new Object[knowledgeNames.length];
			for (int i = 0; i < knowledgeNames.length; i++) {
				values[i] = vSet.getValue(paths.get(i));
			}
			return values;
		} catch (KnowledgeNotFoundException e) {
			Log.e("Couldn't find knowledge " + knowledgeNames + " in component " + component);
			return null;
		}
	}
	
	private ParamHolder<?>[] getValuesForUpdate(String[] knowledgeNames) {
		List<KnowledgePath> paths = new ArrayList<>();
		for(String knowledgeName : knowledgeNames){
			KnowledgePath path = RuntimeMetadataFactoryExt.eINSTANCE.createKnowledgePath();
			PathNodeField pNode = RuntimeMetadataFactoryExt.eINSTANCE.createPathNodeField();
			pNode.setName(knowledgeName);
			path.getNodes().add(pNode);
			paths.add(path);
		}
		try {
			ValueSet vSet = component.getKnowledgeManager().get(paths);
			ParamHolder<?>[] values = new ParamHolder<?>[knowledgeNames.length];
			for (int i = 0; i < knowledgeNames.length; i++) {
				values[i] = new ParamHolder<>(vSet.getValue(paths.get(i)));
			}
			return values;
		} catch (KnowledgeNotFoundException e) {
			Log.e("Couldn't find knowledge " + knowledgeNames + " in component " + component);
			return null;
		}
	}
	
	private void updateValues(String[] knowledgeNames, ParamHolder<?>[] values){
		ChangeSet valuesToUpdate = new ChangeSet();
		List<KnowledgePath> paths = new ArrayList<>();
		for(String knowledgeName : knowledgeNames){
			KnowledgePath path = RuntimeMetadataFactoryExt.eINSTANCE.createKnowledgePath();
			PathNodeField pNode = RuntimeMetadataFactoryExt.eINSTANCE.createPathNodeField();
			pNode.setName(knowledgeName);
			path.getNodes().add(pNode);
			paths.add(path);
		}
		for (int i = 0; i < paths.size(); i++) {
			valuesToUpdate.setValue(paths.get(i), values[i].value);
		}
		try {
			component.getKnowledgeManager().update(valuesToUpdate);
		} catch (KnowledgeUpdateException e) {
			Log.e("Couldn't update knowledge " + knowledgeNames + " in component " + component);
		}
	}
	
//	public Set<Class<? extends DEECoMode>> getSuccessors(Class<? extends DEECoMode> mode){
//		Set<Class<? extends DEECoMode>> successors = new HashSet<>();
//		if(modes.containsKey(mode)){
//			for(ModeSuccessor succ : modes.get(mode)){
//				successors.add(succ.successor);
//			}
//		}
//		
//		return successors;
//	}
//	
//	public double getProbability(Class<? extends DEECoMode> from, Class<? extends DEECoMode> to){
//		if(modes.containsKey(from)){
//			for(ModeSuccessor succ : modes.get(from)){
//				if(succ.successor.equals(to)){
//					return succ.probability;
//				}
//			}
//			return TRANSITION_NOT_FOUND;
//		}
//		return MODE_NOT_FOUND;
//	}
}
