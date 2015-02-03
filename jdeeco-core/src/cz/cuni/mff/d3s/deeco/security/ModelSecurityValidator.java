package cz.cuni.mff.d3s.deeco.security;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeNotFoundException;
import cz.cuni.mff.d3s.deeco.knowledge.ReadOnlyKnowledgeManager;
import cz.cuni.mff.d3s.deeco.model.runtime.api.AbsoluteSecurityRoleArgument;
import cz.cuni.mff.d3s.deeco.model.runtime.api.AccessRights;
import cz.cuni.mff.d3s.deeco.model.runtime.api.BlankSecurityRoleArgument;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Exchange;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Invocable;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeSecurityTag;
import cz.cuni.mff.d3s.deeco.model.runtime.api.LocalKnowledgeTag;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ParameterKind;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNode;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeCoordinator;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeMapKey;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeMember;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathSecurityRoleArgument;
import cz.cuni.mff.d3s.deeco.model.runtime.api.SecurityRole;
import cz.cuni.mff.d3s.deeco.model.runtime.api.SecurityRoleArgument;
import cz.cuni.mff.d3s.deeco.model.runtime.api.SecurityTag;
import cz.cuni.mff.d3s.deeco.task.KnowledgePathHelper;
import cz.cuni.mff.d3s.deeco.task.KnowledgePathHelper.KnowledgePathAndRoot;
import cz.cuni.mff.d3s.deeco.task.KnowledgePathHelper.PathRoot;

/**
 * Helper class for validating security compromise. 
 *
 * @author Ondřej Štumpf
 */
public class ModelSecurityValidator {
	
	private Map<Integer, Set<String>> validationResultsCache = new HashMap<>();	
	
	/**
	 * Validates all processes in the given component.
	 *
	 * @param component
	 *            the component
	 * @return the set of potential errors
	 */
	public Set<String> validate(ComponentInstance component) {
		Set<String> errorList = new HashSet<>();
		
		// no need to check ratings process since it can't change any knowledge
		List<Invocable> invocables = component.getComponentProcesses().stream()
				.filter(process -> !process.isIgnoreKnowledgeCompromise())
				.map(process -> (Invocable)process)
				.collect(Collectors.toList());
		
		// check security integrity in processes
		for (ComponentProcess process : component.getComponentProcesses()) {
			// get all output parameters		
			Set<KnowledgePath> outputParameters = process.getParameters().stream()
					.filter(param -> param.getKind() == ParameterKind.OUT || param.getKind() == ParameterKind.INOUT)
					.map(param -> param.getKnowledgePath())
					.collect(Collectors.toSet());
			
			for (KnowledgePath outputParameterPath : outputParameters) {
				KnowledgeManager knowledgeManager = process.getComponentInstance().getKnowledgeManager();
				SecurityTagCollection outputParamSecurity = SecurityTagCollection.getSecurityTags(outputParameterPath, knowledgeManager);
				
				Map<KnowledgePath, Invocable> transitiveInputParameters = new HashMap<>(); 
				getAllTransitiveInputParameters(outputParameterPath, process, invocables, path -> path, transitiveInputParameters); 
				
				for (KnowledgePath inputParameterPath : transitiveInputParameters.keySet()) {				
					SecurityTagCollection inputParamSecurity = SecurityTagCollection.getSecurityTags(inputParameterPath, knowledgeManager);
					
					if (!isMoreRestrictive(outputParamSecurity, inputParamSecurity)) {
						errorList.add(String.format("Parameter %s is not appropriately secured.", outputParameterPath.toString()));
					}
				}	
			}			
		}
		
		return errorList;
	}

	
	/**
	 * Validates the knowledge exchange method.
	 *
	 * @param pathRoot
	 *            the path root
	 * @param exchange
	 *            the exchange method
	 * @param component
	 *            the local component
	 * @param shadowKnowledgeManager
	 *            the shadow knowledge manager
	 * @return the set of potential errors
	 */
	public Set<String> validate(PathRoot pathRoot, Exchange exchange, ComponentInstance component, ReadOnlyKnowledgeManager shadowKnowledgeManager) {
		Set<String> cacheResult = getCacheResult(pathRoot, exchange, component, shadowKnowledgeManager);
		if (cacheResult != null) {
			return cacheResult;
		}
		
		Set<String> errorList = new HashSet<>();
		
		// add exchange process to the list of component processes
		List<Invocable> invocables = component.getComponentProcesses().stream()
				.filter(process -> !process.isIgnoreKnowledgeCompromise())
				.map(process -> (Invocable)process)
				.collect(Collectors.toList());
		
		if (!exchange.isIgnoreKnowledgeCompromise()) {
			invocables.add(exchange);
		}
		
		// verify no process leads to a knowledge compromise
		for (Invocable invocable : invocables) {
			// get all output parameters		
			Set<KnowledgePath> outputParameters = invocable.getParameters().stream()
					.filter(param -> param.getKind() == ParameterKind.OUT || param.getKind() == ParameterKind.INOUT)
					.map(param -> param.getKnowledgePath())
					.collect(Collectors.toSet());
			
			for (KnowledgePath outputParameterPath : outputParameters) {
				SecurityTagCollection outputParamSecurity;
				KnowledgeManager localKnowledgeManager;
				
				// get output parameter security
				if (invocable instanceof ComponentProcess) {
					localKnowledgeManager = ((ComponentProcess)invocable).getComponentInstance().getKnowledgeManager();
					outputParamSecurity = SecurityTagCollection.getSecurityTags(outputParameterPath, localKnowledgeManager);
				} else {
					localKnowledgeManager = component.getKnowledgeManager();
					outputParamSecurity = SecurityTagCollection.getSecurityTags(pathRoot, outputParameterPath, localKnowledgeManager, shadowKnowledgeManager, null);
				}
						
				KnowledgePath localizedOutputPath = localizeKnowledgePath(pathRoot, outputParameterPath, localKnowledgeManager, shadowKnowledgeManager);
				
				// get transitive input dependencies				
				Map<KnowledgePath, Invocable> transitiveInputParameters = new HashMap<>();
				getAllTransitiveInputParameters(localizedOutputPath, invocable, invocables, path -> localizeKnowledgePath(pathRoot, path, localKnowledgeManager, shadowKnowledgeManager), transitiveInputParameters); 
										
				for (KnowledgePath inputParameterPath : transitiveInputParameters.keySet()) {
					// get input parameter security
					SecurityTagCollection inputParamSecurity;
					Invocable inputSourceInvocable = transitiveInputParameters.get(inputParameterPath);
					if (inputSourceInvocable instanceof ComponentProcess) {
						inputParamSecurity = SecurityTagCollection.getSecurityTags(inputParameterPath, localKnowledgeManager);
					} else {
						inputParamSecurity = SecurityTagCollection.getSecurityTags(pathRoot, inputParameterPath, localKnowledgeManager, shadowKnowledgeManager, null);
					}
					
					if (!isMoreRestrictive(outputParamSecurity, inputParamSecurity)) {
						if (invocable.getMethod() != null) {
							errorList.add(String.format("Parameter %s is not appropriately secured (compromises %s in %s).", 
								outputParameterPath.toString(), inputParameterPath.toString(), invocable.getMethod().getName()));
						} else {
							errorList.add(String.format("Parameter %s is not appropriately secured (compromises %s).", 
									outputParameterPath.toString(), inputParameterPath.toString()));
						}
					}
				}	
			}		
		}
		
		createCacheResult(pathRoot, exchange, component, shadowKnowledgeManager, errorList);
		return errorList;
	}
	
	/**
	 * Stores the validation result in the cache.
	 */
	protected void createCacheResult(PathRoot pathRoot, Exchange exchange, ComponentInstance component, ReadOnlyKnowledgeManager shadowKnowledgeManager, Set<String> errorList) {
		int hash = getHash(pathRoot, exchange, component, shadowKnowledgeManager);
		validationResultsCache.put(hash, errorList);
	}

	/**
	 * Obtains the validation result from the cache - returns null if result is not present.
	 */
	protected Set<String> getCacheResult(PathRoot pathRoot, Exchange exchange, ComponentInstance component, ReadOnlyKnowledgeManager shadowKnowledgeManager) {
		int hash = getHash(pathRoot, exchange, component, shadowKnowledgeManager);
		if (validationResultsCache.containsKey(hash)) {
			return validationResultsCache.get(hash);
		} else {
			return null;
		}		
	}

	/**
	 * Computes hash for given set of parameters. 
	 */
	protected int getHash(PathRoot pathRoot, Exchange exchange, ComponentInstance component, ReadOnlyKnowledgeManager shadowKnowledgeManager) {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pathRoot == null) ? 0 : pathRoot.hashCode());
		result = prime * result + ((exchange == null) ? 0 : exchange.hashCode());
		result = prime * result + ((component == null) ? 0 : component.hashCode());
		result = prime * result + ((shadowKnowledgeManager == null) ? 0 : shadowKnowledgeManager.hashCode());
		return result;
	}

	/**
	 * If possible and necessary, converts this knowledge exchange path to its local form.
	 * @param pathRoot
	 * @param path
	 * @param localKnowledgeManager
	 * @param shadowKnowledgeManager
	 * @return
	 */
	private KnowledgePath localizeKnowledgePath(PathRoot pathRoot, KnowledgePath path, ReadOnlyKnowledgeManager localKnowledgeManager, ReadOnlyKnowledgeManager shadowKnowledgeManager)  {		
		if (path.getNodes().isEmpty()) {
			return path;
		}
				
		PathNode firstNode = path.getNodes().get(0);
		
		if (((firstNode instanceof PathNodeCoordinator) && pathRoot == PathRoot.COORDINATOR) || ((firstNode instanceof PathNodeMember) && pathRoot == PathRoot.MEMBER)) {	
			KnowledgePath modifiablePath = KnowledgePathHelper.cloneKnowledgePath(path);
			modifiablePath.getNodes().remove(0);
			
			for (PathNode node : modifiablePath.getNodes()) {
				if (node instanceof PathNodeMapKey) {
					PathNodeMapKey mapNode = (PathNodeMapKey)node;
					mapNode.setKeyPath(localizeKnowledgePath(pathRoot, mapNode.getKeyPath(), localKnowledgeManager, shadowKnowledgeManager));
				}
			}		
			return modifiablePath;
		} else if ((firstNode instanceof PathNodeMember) && pathRoot == PathRoot.COORDINATOR) {
			try {
				KnowledgePathAndRoot pathAndRoot = KnowledgePathHelper.getAbsoluteStrippedPath(path, localKnowledgeManager, shadowKnowledgeManager);				
				return pathAndRoot.knowledgePath;
			} catch (KnowledgeNotFoundException e) {
				return path;
			}
		} else if ((firstNode instanceof PathNodeCoordinator) && pathRoot == PathRoot.MEMBER) {
			try {
				KnowledgePathAndRoot pathAndRoot = KnowledgePathHelper.getAbsoluteStrippedPath(path, shadowKnowledgeManager, localKnowledgeManager);
				return pathAndRoot.knowledgePath;
			} catch (KnowledgeNotFoundException e) {
				return path;
			}
		} else {
			return path;
		}		
	}


	/**
	 * Checks if the security of output is more or equally restrictive than the security of the input.
	 *
	 * @param out
	 *            the output security (DNF)
	 * @param in
	 *            the input security (DNF)
	 * @return true, if is more restrictive
	 */
	public boolean isMoreRestrictive(SecurityTagCollection out, SecurityTagCollection in) {		
		// if output has no security tags and input does
		if (out.isEmpty() && conjunctionWithoutLocalExists(in)) {
			return false;
		}
		
		// if output has some security tags and input does not
		if (!conjunctionWithoutLocalExists(in) && !out.isEmpty()) {
			return true;
		}
		
		// each conjunction from the output must override some conjunction from the input
		return out.stream().allMatch(conjunction -> satisfiesConjunction(conjunction, in));
	}
	
	private boolean conjunctionWithoutLocalExists(SecurityTagCollection formula) {
		return formula.stream().anyMatch(conjunction -> !conjunction.stream().anyMatch(tag -> tag instanceof LocalKnowledgeTag ) );
	}
	
	private boolean satisfiesConjunction(List<SecurityTag> outConjunction, SecurityTagCollection in) {
		// any of the conjunctions in the input must be overriden by the output conjunction
		return in.stream().anyMatch(inConjunction -> satisfiesConjunction(outConjunction, inConjunction));
	}

	private boolean satisfiesConjunction(List<SecurityTag> outConjunction, List<SecurityTag> inConjunction) {
		// either an output conjunction contains local knowledge
		// or each tag in the input conjunction is overriden in the output
		return  outConjunction.stream().anyMatch(outTag -> outTag instanceof LocalKnowledgeTag) ||
				inConjunction.stream().allMatch(inTag -> tagPresentOrOverriden(inTag, outConjunction));
	}

	private boolean tagPresentOrOverriden(SecurityTag inTag, List<SecurityTag> outConjunction) {
		// any of the output tags must override the input tag
		return outConjunction.stream().anyMatch(outTag -> satisfies(inTag, outTag));
	}

	private boolean satisfies(SecurityTag inTag, SecurityTag outTag) {		
		if (inTag instanceof KnowledgeSecurityTag && outTag instanceof KnowledgeSecurityTag) {
			KnowledgeSecurityTag inKnowledgeTag = (KnowledgeSecurityTag) inTag;
			KnowledgeSecurityTag outKnowledgeTag = (KnowledgeSecurityTag) outTag;
			
			List<SecurityRole> transitiveRoles = RoleHelper.getTransitiveRoles(Arrays.asList(outKnowledgeTag.getRequiredRole()));
			boolean roleMatched = false;
			
			// the role of the output role must be more restrictive than the role of the input
			for (SecurityRole outRole : transitiveRoles) {
				boolean namesMatch = inKnowledgeTag.getRequiredRole().getRoleName().equals(outRole.getRoleName()); 
				boolean argumentsMatch = inKnowledgeTag.getRequiredRole().getArguments().stream().allMatch(inArgument -> 
					outRole.getArguments().stream().anyMatch(outArgument -> satisfies(inArgument, outArgument))
				);	
				roleMatched = roleMatched || (namesMatch && argumentsMatch);
			}
			
			return roleMatched && accessRightsMatched(inKnowledgeTag, outKnowledgeTag);
		}
		return false;
	}

	/** 
	 * Checks if the parameter kind does not violate security access rights 
	 */
	private boolean accessRightsMatched(KnowledgeSecurityTag inTag, KnowledgeSecurityTag outTag) {
		if (inTag.getAccessRights() == outTag.getAccessRights()) {
			return true;
		} else if ((outTag.getAccessRights() == AccessRights.READ || outTag.getAccessRights() == AccessRights.WRITE) && (inTag.getAccessRights() == AccessRights.READ_WRITE)) {
			return true;
		}
		return false;
	}
	
	private boolean satisfies(SecurityRoleArgument inArgument, SecurityRoleArgument outArgument) {
		boolean namesMatch = inArgument.getName().equals(outArgument.getName());
		
		// blank argument overrides anything
		if (inArgument instanceof BlankSecurityRoleArgument) {
			return namesMatch;
		}
		
		// path arguments must share both names and paths
		if (inArgument instanceof PathSecurityRoleArgument && outArgument instanceof PathSecurityRoleArgument) {
			return namesMatch && ((PathSecurityRoleArgument)inArgument).getKnowledgePath().equals(((PathSecurityRoleArgument)outArgument).getKnowledgePath());
		}
		
		// absolute arguments must share names and values
		if (inArgument instanceof AbsoluteSecurityRoleArgument && outArgument instanceof AbsoluteSecurityRoleArgument) {
			Object inValue = ((AbsoluteSecurityRoleArgument)inArgument).getValue();
			Object outValue = ((AbsoluteSecurityRoleArgument)outArgument).getValue();
			return namesMatch && ((inValue == null && outValue == null) || (inValue != null && inValue.equals(outValue)));
		}
		
		return false;
	}

	/**
	 * Returns all knowledge paths on which the given output parameter knowledge path depends
	 * @param outputParameterPath
	 * 			the knowledge path of the output parameter of the process
	 * @param process
	 * 			the process in which the knowledge path belongs
	 * @param result
	 * 			the set of knowledge paths
	 */
	protected void getAllTransitiveInputParameters(KnowledgePath outputParameterPath, Invocable process, Collection<Invocable> allProcesses, 
			UnaryOperator<KnowledgePath> mappingFunction, Map<KnowledgePath, Invocable> result) {
		Map<KnowledgePath, KnowledgePath> inputParameters = process.getParameters().stream()
				.filter(param -> param.getKind() == ParameterKind.IN || param.getKind() == ParameterKind.INOUT)
				.map(param -> param.getKnowledgePath())
				.collect(Collectors.toMap(key -> (KnowledgePath)key, mappingFunction));
		
		if (result.keySet().containsAll(inputParameters.keySet())) return;
		inputParameters.keySet().stream().forEach(inputPath -> result.put(inputPath, process));
		
		for (KnowledgePath inputParameterPath : inputParameters.keySet()) {
			for (Invocable anotherProcess : allProcesses) {
				Set<KnowledgePath> outputParameters = anotherProcess.getParameters().stream()
						.filter(param -> param.getKind() == ParameterKind.OUT || param.getKind() == ParameterKind.INOUT)
						.map(param -> param.getKnowledgePath())
						.map(mappingFunction)
						.collect(Collectors.toSet());
				
				if (outputParameters.contains(inputParameters.get(inputParameterPath))) {
					getAllTransitiveInputParameters(inputParameterPath, anotherProcess, allProcesses, mappingFunction, result);
				}
			}
		}
	
	}

}
