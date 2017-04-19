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
import cz.cuni.mff.d3s.deeco.modes.DEECoModeChart;
import cz.cuni.mff.d3s.deeco.modes.DEECoTransition;
import cz.cuni.mff.d3s.deeco.modes.DEECoTransitionListener;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;


public class ModeChartImpl extends DEECoModeChart {
	
	public ModeChartImpl(ComponentInstance component) {
		super(component);
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
	}
	
	private Set<DEECoTransition> getTransitionsFrom(DEECoMode mode){
		Set<DEECoTransition> outgoing = new HashSet<>();
		for(DEECoTransition transition : transitions){
			if(transition.getFrom().equals(mode)){
				outgoing.add(transition);
			}
		}
		
		return outgoing;
	}
	
	private List<DEECoTransition> sortByPriority(Set<DEECoTransition> transitions){
		List<DEECoTransition> sorted = new ArrayList<>(transitions);
		sorted.sort(Comparator.comparing(t -> -t.getPriority()));
		
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
	
}
