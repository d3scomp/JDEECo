package cz.cuni.mff.d3s.deeco.task;

import static cz.cuni.mff.d3s.deeco.task.KnowledgePathHelper.getAbsoluteStrippedPath;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessor;
import cz.cuni.mff.d3s.deeco.integrity.RatingsManager;
import cz.cuni.mff.d3s.deeco.knowledge.ChangeSet;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeNotFoundException;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeUpdateException;
import cz.cuni.mff.d3s.deeco.knowledge.ReadOnlyKnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.ValueSet;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleController;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Parameter;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ParameterKind;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;
import cz.cuni.mff.d3s.deeco.task.KnowledgePathHelper.KnowledgePathAndRoot;
import cz.cuni.mff.d3s.deeco.task.KnowledgePathHelper.PathRoot;

/**
 * Handles membership and knowledge exchange within an ensemble.
 * 
 * @author Zbyněk Jiráček
 *
 */
public class EnsembleDataExchange {

	/**
	 * Reference to the corresponding {@link EnsembleController} in the runtime metadata 
	 */
	EnsembleDefinition ensembleDefinition;
	
	/**
	 * Reference to the ratings manager
	 */	
	RatingsManager ratingsManager;
			
	public EnsembleDataExchange(EnsembleDefinition ensembleDefinition,
			RatingsManager ratingsManager) {
		super();
		this.ensembleDefinition = ensembleDefinition;
		this.ratingsManager = ratingsManager;
	}

	/**
	 * Checks membership condition on parameters of the local knowledge manager and parameters from a particular shadow knowledge manager.
	 * It fixes the coordinator/member roles based on the <code>localRole</code> parameter. If <code>localRole</code> is equal to {@link KnowledgePathHelper.PathRoot#COORDINATOR} it
	 * takes the coordinators parameters from the local knowledge manager and the member parameters from the shadow knowledge manager.
	 * If <code>localRole</code> is equal to {@link KnowledgePathHelper.PathRoot#MEMBER}, it does it vice-versa.
	 * 
	 * It essentially works in the following steps:
	 * <ol>
	 *   <li>It resolves the IN paths of the membership condition and retrieves the knowledge from the local knowledge manager and 
	 *   the shadow knowledge manager with the member/coordinator roles fixed according to the <code>localRole</code> parameter.
	 *   (Note that INOUT and OUT paths are not allowed. If they are present a {@link TaskInvocationException} is thrown.)</li>
	 *   <li>If the IN parameters were not found in the knowledge, <code>false</code> is returned.</li>
	 *   <li>Otherwise, it invokes the membership condition with the values obtained in the previous step and returns its return value.</li>
	 * </ol>
	 * 
	 * @param localRole specifies whether the membership condition is to be evaluated with assuming the local component as a coordinator or member.
	 * @param shadowKnowledgeManager specifies the shadow knowledge manager which contains data of the opposite component instance. 
	 * @return <code>true</code> if the membership condition was satisfied. <code>false</code> if it was not satisfied or if the
	 * IN parameters were not found in the knowledge.
	 * @throws TaskInvocationException thrown when the membership condition can't be executed (e.g. it does not exist, has another parameters, has INOUT/OUT parameters, etc.)
	 */
	public boolean checkMembership(PathRoot localRole, KnowledgeManager localKnowledgeManager,
			ReadOnlyKnowledgeManager shadowKnowledgeManager) throws TaskInvocationException {
		if (!checkMembershipRoles(localRole, localKnowledgeManager.getRoles(), shadowKnowledgeManager.getRoles())) {
			return false;
		}
		
		List<Parameter> formalParams = ensembleDefinition.getMembership().getParameters();
		Object[] actualParams;
		try {
			ParameterKnowledgePaths parameters = getAllPathsWithRoots(localRole, localKnowledgeManager,
					shadowKnowledgeManager, formalParams, false);
			actualParams = loadActualParams(localRole, localKnowledgeManager, shadowKnowledgeManager, parameters);
		} catch (KnowledgeNotFoundException e) {
			Log.d(String.format("Input knowledge (%s) of a membership function in %s not found.", 
					e.getNotFoundPath(), ensembleDefinition.getName()));
			return false;
		}
		
		try {
			// Call the membership condition
			return (Boolean)ensembleDefinition.getMembership().getMethod().invoke(null, actualParams);
			
		} catch (IllegalAccessException | IllegalArgumentException e) {
			throw new TaskInvocationException("Error when invoking a membership condition.", e);
		} catch (ClassCastException e) {
			throw new TaskInvocationException("Membership condition does not return a boolean.", e);			
		} catch (InvocationTargetException e) {
			Log.d("Membership condition returned an exception.", e.getTargetException());
			return false;
		}
	}
	
	/**
	 * Performs the knowledge exchange between the local knowledge manager and a particular shadow knowledge manager. Afterwards, it writes new values to 
	 * the local knowledge manager. Similarly to {@link #checkMembership(PathRoot, ReadOnlyKnowledgeManager)}, it uses the <code>localRole</code>
	 * parameter to specify, whether the local component instance is assumed to be in member or coordinator role.
	 * 
	 * The method essentially works in the following steps:
	 * <ol>
	 *   <li>It resolves the IN/INOUT paths of the membership condition and retrieves the knowledge from the local knowledge manager and 
	 *   the shadow knowledge manager with the member/coordinator roles fixed according to the <code>localRole</code> parameter.
	 *   <li>It invokes the membership condition with the values obtained in the previous step.</li>
	 *   <li>It updates the local knowledge manager with the INOUT/OUT data that pertain to the local component.</li>
	 * </ol>
	 * 
	 * Similar to the {@link ProcessTask#invoke(Trigger)}, the INOUT/OUT parameters are wrapped by {@link ParamHolder}. 
	 * 
	 * @param localRole specifies whether the knowledge exchange is to be evaluated with assuming the local component as a coordinator or member.
	 * @param shadowKnowledgeManager specifies the shadow knowledge manager which contains data of the opposite component instance. 
	 * @return true if the exchange was performed successfully
	 * @throws TaskInvocationException thrown when the knowledge exchange can't be executed (e.g. it does not exist, has another parameters, has INOUT/OUT parameters, etc.).
	 * The exception is thrown also in the case when parameters cannot be retrieved from the local/shadow knowledge manager or when the output
	 * parameters can't be updated in the knowledge manager.
	 */
	public void performExchange(PathRoot localRole, KnowledgeManager localKnowledgeManager,
			ReadOnlyKnowledgeManager shadowKnowledgeManager) throws TaskInvocationException {
		List<Parameter> formalParams = ensembleDefinition.getKnowledgeExchange().getParameters();
		ParameterKnowledgePaths parameters;
		Object[] actualParams;
		try {
			parameters = getAllPathsWithRoots(localRole, localKnowledgeManager, shadowKnowledgeManager, formalParams, true);
			actualParams = loadActualParams(localRole, localKnowledgeManager, shadowKnowledgeManager, parameters);	
		} catch (KnowledgeNotFoundException e) {
			throw new TaskInvocationException(
					String.format("Input knowledge (%s) of a knowledge exchange in %s not found.", 
					e.getNotFoundPath(), ensembleDefinition.getName()),
					e);
		}
		
		try {			
			// Call the process method
			ensembleDefinition.getKnowledgeExchange().getMethod().invoke(null, actualParams);
		} catch (IllegalAccessException | IllegalArgumentException e) {
			throw new TaskInvocationException(
					String.format("Error when invoking a knowledge exchange for ensemble: %s", 
					ensembleDefinition.getName()),
					e);			
		} catch (InvocationTargetException e) {
			throw new TaskInvocationException(
					String.format("Knowledge exchange of the ensemble %s returned an exception.",
					ensembleDefinition.getName()),
					e.getTargetException());
		}	
		
		try {
			updateOutParameters(localRole, localKnowledgeManager, shadowKnowledgeManager, parameters, actualParams);
		} catch (KnowledgeUpdateException e) {
			throw new TaskInvocationException(
					String.format("Error when updating the knowledge after a knowledge exchange for ensemble: %s", 
					ensembleDefinition.getName()),
					e);	
		}
	}
	
	private boolean checkMembershipRoles(PathRoot localRole, Class<?>[] localRoles, Class<?>[] shadowRoles) {
		Class<?>[] coordinatorRoles, memberRoles;
		if (localRole == PathRoot.COORDINATOR) {
			coordinatorRoles = localRoles;
			memberRoles = shadowRoles;
		} else {
			coordinatorRoles = shadowRoles;
			memberRoles = localRoles;
		}
		
		return checkRoleImplementation(ensembleDefinition.getCoordinatorRole(), coordinatorRoles)
				&& checkRoleImplementation(ensembleDefinition.getMemberRole(), memberRoles);
	}
	
	private boolean checkRoleImplementation(Class<?> desiredRole, Class<?>[] implementedRoles) {
		if (desiredRole == null) {
			return true;
		}
		
		for (Class<?> implementedRole : implementedRoles) {
			if (implementedRole.equals(desiredRole)) {
				return true;
			}
		}
		
		return false;
	}
	
	private static class ParameterKnowledgePaths {
		List<Parameter> formalParams;
		List<KnowledgePathAndRoot> allPathsWithRoots;
		// formalParams correspond with allPathsWithRoots 1:1
		
		Collection<KnowledgePath> localInPaths;
		Collection<KnowledgePath> shadowInPaths;
		// localInPaths and shadowInPaths do not contain out parameters
		
		public ParameterKnowledgePaths(List<Parameter> formalParams) {
			this.formalParams = formalParams;
			allPathsWithRoots = new LinkedList<KnowledgePathAndRoot>();
			localInPaths = new LinkedList<KnowledgePath>();
			shadowInPaths = new LinkedList<KnowledgePath>();
		}
	}
	
	private ParameterKnowledgePaths getAllPathsWithRoots(PathRoot localRole, KnowledgeManager localKnowledgeManager,
			ReadOnlyKnowledgeManager shadowKnowledgeManager, List<Parameter> formalParams, boolean allowOutParams)
					throws TaskInvocationException, KnowledgeNotFoundException {

		ParameterKnowledgePaths result = new ParameterKnowledgePaths(formalParams);
		
		for (Parameter formalParam : formalParams) {
			ParameterKind paramDir = formalParam.getKind();

			if (!allowOutParams && paramDir != ParameterKind.IN && paramDir != ParameterKind.RATING) {
				throw new TaskInvocationException("Only IN and RATING params allowed in membership condition.");
			}
			
			KnowledgePathAndRoot absoluteKnowledgePathAndRoot;
			// FIXME: The call to getAbsoluteStrippedPath is in theory wrong, because this way we are not obtaining the
			// knowledge within one transaction. But fortunately this is not a problem with the single 
			// threaded scheduler we have at the moment, because once the invoke method starts there is no other
			// activity whatsoever in the system.
			if (localRole == PathRoot.COORDINATOR) {
				absoluteKnowledgePathAndRoot = getAbsoluteStrippedPath(formalParam.getKnowledgePath(), 
						localKnowledgeManager, shadowKnowledgeManager);
			} else {
				absoluteKnowledgePathAndRoot = getAbsoluteStrippedPath(formalParam.getKnowledgePath(),
						shadowKnowledgeManager, localKnowledgeManager);				
			}
			
			if (paramDir == ParameterKind.OUT || paramDir == ParameterKind.INOUT) {
				// no paths are locked in shadow knowledge manager
				if (absoluteKnowledgePathAndRoot.root == localRole 
						&& localKnowledgeManager.isLocked(absoluteKnowledgePathAndRoot.knowledgePath)) {
					throw new TaskInvocationException(String.format(
							"Path %s is used as a parameter of a security role and therefore cannot be modified.", 
							absoluteKnowledgePathAndRoot.knowledgePath));
				}
			}

			if (paramDir == ParameterKind.IN || paramDir == ParameterKind.INOUT || paramDir == ParameterKind.OUT) {
				if (absoluteKnowledgePathAndRoot == null) {
					throw new TaskInvocationException("Member/Coordinator prefix required for membership paths.");
				} 
				
				if (paramDir != ParameterKind.OUT) {
					if (absoluteKnowledgePathAndRoot.root == localRole) {
						result.localInPaths.add(absoluteKnowledgePathAndRoot.knowledgePath);
					} else {
						result.shadowInPaths.add(absoluteKnowledgePathAndRoot.knowledgePath);
					}
				}
			}
			
			result.allPathsWithRoots.add(absoluteKnowledgePathAndRoot);
		}
		
		return result;
	} 
	
	private Object[] loadActualParams(PathRoot localRole, KnowledgeManager localKnowledgeManager,
			ReadOnlyKnowledgeManager shadowKnowledgeManager, ParameterKnowledgePaths parameters) 
					throws KnowledgeNotFoundException, TaskInvocationException {
		
		ValueSet localKnowledge = getKnowledge(localKnowledgeManager, parameters.localInPaths);
		ValueSet shadowKnowledge = getKnowledge(shadowKnowledgeManager, parameters.shadowInPaths);

		// Construct the parameters for the process method invocation
		Object[] actualParams = new Object[parameters.formalParams.size()];
		
		int paramIdx = 0;
		Iterator<KnowledgePathAndRoot> allPathsWithRootsIter = parameters.allPathsWithRoots.iterator(); 
		for (Parameter formalParam : parameters.formalParams) {
			ParameterKind paramDir = formalParam.getKind();			
			KnowledgePathAndRoot absoluteKnowledgePathAndRoot = allPathsWithRootsIter.next();
			String knowledgeAuthor = null;
			Object paramValue = null;
			
			if (paramDir == ParameterKind.IN || paramDir == ParameterKind.INOUT || paramDir == ParameterKind.RATING) {				
				if (absoluteKnowledgePathAndRoot.root == localRole) {
					paramValue = getParamValue(localKnowledge, localKnowledgeManager, absoluteKnowledgePathAndRoot.knowledgePath);
					knowledgeAuthor = localKnowledgeManager.getAuthor(absoluteKnowledgePathAndRoot.knowledgePath);
				} else {
					paramValue = getParamValue(shadowKnowledge, shadowKnowledgeManager, absoluteKnowledgePathAndRoot.knowledgePath);
					knowledgeAuthor = shadowKnowledgeManager.getAuthor(absoluteKnowledgePathAndRoot.knowledgePath);
				}
			}
			
			if (paramDir == ParameterKind.IN) {
				actualParams[paramIdx] = paramValue;				
			} else if (paramDir == ParameterKind.OUT) {
				actualParams[paramIdx] = new ParamHolder<Object>();
			} else if (paramDir == ParameterKind.INOUT) {
				actualParams[paramIdx] = new ParamHolder<Object>(paramValue);
			} else if (paramDir == ParameterKind.RATING) {				
				actualParams[paramIdx] = ratingsManager.createReadonlyRatingsHolder(knowledgeAuthor, 
						absoluteKnowledgePathAndRoot.knowledgePath); 			
			}
			// TODO: We could have an option of not creating the wrapper. That would make it easier to work with mutable out types.
			// TODO: We need some way of handling insertions/deletions in a hashmap.
			
			paramIdx++;
		}
		
		return actualParams;
	}

	/**
	 * @param knowledgeManager
	 * @param paths
	 * @return
	 * @throws KnowledgeNotFoundException 
	 */
	private ValueSet getKnowledge(ReadOnlyKnowledgeManager knowledgeManager,
			Collection<KnowledgePath> paths) throws KnowledgeNotFoundException {
		Collection<KnowledgePath> allPaths = new ArrayList<>(); 
		for (KnowledgePath p : paths) {
			if (p.toString().equals(AnnotationProcessor.ALL_FIELDS_TOKEN)) {
				allPaths.addAll(knowledgeManager.getAllPaths());
			} else {
				allPaths.add(p);
			}
		}
		return knowledgeManager.get(allPaths);
	}

	/**
	 * @param knowledge
	 * @param knowledgeManager 
	 * @param knowledgePath
	 * @return
	 */
	private Object getParamValue(ValueSet knowledge, ReadOnlyKnowledgeManager knowledgeManager, KnowledgePath knowledgePath) {
		Object ret = null;
		if (knowledgePath.toString().equals(AnnotationProcessor.ALL_FIELDS_TOKEN)) {
		
			List<Object> correlationDataValues = new ArrayList<>();
			for (KnowledgePath path: knowledgeManager.getAllPaths()) {
				correlationDataValues.add(knowledge.getValue(path));

			}
			ret = correlationDataValues.toArray();
		} else {
			ret = knowledge.getValue(knowledgePath);
		}
		return ret;
	}

	/**
	 * @param localRole
	 * @param shadowKnowledgeManager
	 * @param formalParams
	 * @param parameters
	 * @param actualParams
	 * @throws TaskInvocationException
	 */
	private void updateOutParameters(PathRoot localRole, KnowledgeManager localKnowledgeManager,
			ReadOnlyKnowledgeManager shadowKnowledgeManager, ParameterKnowledgePaths parameters, 
			Object[] actualParams) throws KnowledgeUpdateException {
		// Create a changeset
		Map<String, ChangeSet> localChangeSets = new HashMap<>();

		int paramIdx = 0;
		Iterator<KnowledgePathAndRoot> allPathsWithRootsIter = parameters.allPathsWithRoots.iterator(); 
		for (Parameter formalParam : parameters.formalParams) {
			ParameterKind paramDir = formalParam.getKind();
			KnowledgePathAndRoot absoluteKnowledgePathAndRoot = allPathsWithRootsIter.next();

			if (absoluteKnowledgePathAndRoot.root == localRole) {
				if (paramDir == ParameterKind.OUT || paramDir == ParameterKind.INOUT) {
					
					String author = shadowKnowledgeManager.getAuthor(absoluteKnowledgePathAndRoot.knowledgePath);
					if (author == null) {
						author = shadowKnowledgeManager.getId();
					}
					if (!localChangeSets.containsKey(author)) {
						localChangeSets.put(author, new ChangeSet());
					}
					
					localChangeSets.get(author).setValue(absoluteKnowledgePathAndRoot.knowledgePath, 
							((ParamHolder<Object>)actualParams[paramIdx]).value);
				}
			}
			
			paramIdx++;
		}
		
		// Write the changeset back to the knowledge
		for (Entry<String, ChangeSet> entry : localChangeSets.entrySet()) {
			localKnowledgeManager.update(entry.getValue(), entry.getKey());
		}
	}

}
