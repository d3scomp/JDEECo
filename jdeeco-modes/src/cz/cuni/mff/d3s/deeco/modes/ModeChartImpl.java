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
package cz.cuni.mff.d3s.deeco.modes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import cz.cuni.mff.d3s.deeco.knowledge.ChangeSet;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeNotFoundException;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeUpdateException;
import cz.cuni.mff.d3s.deeco.knowledge.ValueSet;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeField;
import cz.cuni.mff.d3s.deeco.model.runtime.custom.RuntimeMetadataFactoryExt;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;

// TODO: make tests
class ModeChartImpl extends ModeChart{
	
	public static final double MODE_NOT_FOUND = -1;
	
	public static final double TRANSITION_NOT_FOUND = -2;
	
	public static final Random rand = new Random(78631);
	
	Map<Class<? extends DEECoMode>, Set<ModeSuccessor>> modes;
	
	/**
	 * Internal constructor enables {@link ModeChartFactory} to be the
	 * Privileged creator of {link {@link ModeChartImpl} instances.
	 */
	ModeChartImpl() {
		currentMode = null;
		modes = new HashMap<>();
		transitionListeners = new HashMap<>();
	}
	
	void setInitialNode(Class<? extends DEECoMode> mode){
		currentMode = mode;
	}
	
	Class<? extends DEECoMode> getInitialMode(){
		return currentMode;
	}

	@Override
	public Set<Class<? extends DEECoMode>> getModes() {
		Set<Class<? extends DEECoMode>> allModes = new HashSet<>();
		// Put all the modes from keySet
		allModes.addAll(modes.keySet());
		// Put the rest of the modes that are not in the key set
		for(Class<? extends DEECoMode> mode : modes.keySet()){
			allModes.addAll(getSuccessors(mode));
		}

		// Prevent modification attempts
		return Collections.unmodifiableSet(allModes);
	}
	
	@Override
	public Class<? extends DEECoMode> switchMode(){
		
		// Switch mode only if there is a transition from it
		if(modes.containsKey(currentMode)){
			// Get successor modes
			List<ModeSuccessor> successors = new ArrayList<>(modes.get(currentMode));
			// Filter out inapplicable transitions
			for(ModeSuccessor succ : successors.toArray(new ModeSuccessor[]{})){
				String[] knowledge = succ.guard.getKnowledgeNames();
				Object[] values = getValues(knowledge);
				//System.out.format("Knowledge: %s Value %s%n", knowledge, String.valueOf(value));
				if(!succ.guard.isSatisfied(values)){
					successors.remove(succ);
				}
			}
			// Sort according to the probabilities
			Collections.sort(successors, new Comparator<ModeSuccessor>(){
				@Override
				public int compare(ModeSuccessor s1, ModeSuccessor s2) {
					return Double.compare(s1.probability, s2.probability);
				}
			});
			// Check probability consistency
			double probabilitySum = 0;
			for(ModeSuccessor s : successors){
				probabilitySum += s.probability;
			}
			if(probabilitySum > 1){
				StringBuilder builder = new StringBuilder();
				for(ModeSuccessor succ : successors){
					builder.append("\n").append(succ.getTypeName())
						.append(" probability = ").append(succ.probability);
				}
				Log.e("The probabilities of these satisfied mode successors"
						+ " of " + currentMode.getTypeName()  
						+ " is greater than 1 and will lead to unconsistent behavior:"
						+ builder.toString());
			}
			// switch nondeterministically
			double random = rand.nextDouble();
			double successorTreshold = 0;
			for(ModeSuccessor s : successors){
				successorTreshold += s.probability;
				if(random < successorTreshold){
					// Call the transition listeners before the mode is switched
					invokeTransitionListeners(currentMode, s.successor);
					// Switch the mode
					currentMode = s.successor;
					break;
				}
			}
		}

		return currentMode;
	}
	
	private void invokeTransitionListeners(Class<? extends DEECoMode> from, Class<? extends DEECoMode> to){
		if(transitionListeners.containsKey(from)){
			Map<Class<? extends DEECoMode>, List<ModeTransitionListener>> fromListeners = transitionListeners.get(from); 
			if(fromListeners.containsKey(to)){
				for(ModeTransitionListener transitionListener : fromListeners.get(to)){
					// Get the knowledge values
					String[] knowledge = transitionListener.getKnowledgeNames();
					ParamHolder<?>[] values = getValuesForUpdate(knowledge);
					// Call the listener with proper values
					transitionListener.transitionTaken(values);
					updateValues(knowledge, values);	
				}
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
	
	public Set<Class<? extends DEECoMode>> getSuccessors(Class<? extends DEECoMode> mode){
		Set<Class<? extends DEECoMode>> successors = new HashSet<>();
		if(modes.containsKey(mode)){
			for(ModeSuccessor succ : modes.get(mode)){
				successors.add(succ.successor);
			}
		}
		
		return successors;
	}
	
	public double getProbability(Class<? extends DEECoMode> from, Class<? extends DEECoMode> to){
		if(modes.containsKey(from)){
			for(ModeSuccessor succ : modes.get(from)){
				if(succ.successor.equals(to)){
					return succ.probability;
				}
			}
			return TRANSITION_NOT_FOUND;
		}
		return MODE_NOT_FOUND;
	}
}
