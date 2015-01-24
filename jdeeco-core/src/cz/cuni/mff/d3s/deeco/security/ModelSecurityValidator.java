package cz.cuni.mff.d3s.deeco.security;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.ReadOnlyKnowledgeManager;
import cz.cuni.mff.d3s.deeco.model.runtime.api.AbsoluteSecurityRoleArgument;
import cz.cuni.mff.d3s.deeco.model.runtime.api.BlankSecurityRoleArgument;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Exchange;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Invocable;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeSecurityTag;
import cz.cuni.mff.d3s.deeco.model.runtime.api.LocalKnowledgeTag;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ParameterKind;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathSecurityRoleArgument;
import cz.cuni.mff.d3s.deeco.model.runtime.api.SecurityRole;
import cz.cuni.mff.d3s.deeco.model.runtime.api.SecurityRoleArgument;
import cz.cuni.mff.d3s.deeco.model.runtime.api.SecurityTag;
import cz.cuni.mff.d3s.deeco.task.KnowledgePathHelper.PathRoot;

/**
 * Helper class for validating security compromise. 
 *
 * @author Ondřej Štumpf
 */
public class ModelSecurityValidator {
	
	/**
	 * Validates all processes in the given component.
	 *
	 * @param component
	 *            the component
	 * @return the set of potential errors
	 */
	public static Set<String> validate(ComponentInstance component) {
		Set<String> errorList = new HashSet<>();
		
		// no need to check ratings process since it can't change any knowledge
		List<Invocable> invocables = component.getComponentProcesses().stream()
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
				
				Set<KnowledgePath> transitiveInputParameters = new HashSet<>(); 
				getAllTransitiveInputParameters(outputParameterPath, process, invocables, transitiveInputParameters); 
				
				for (KnowledgePath inputParameterPath : transitiveInputParameters) {				
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
	public static Set<String> validate(PathRoot pathRoot, Exchange exchange, ComponentInstance component, ReadOnlyKnowledgeManager shadowKnowledgeManager) {
		Set<String> errorList = new HashSet<>();

		// add exchange process to the list of component processes
		List<Invocable> invocables = component.getComponentProcesses().stream()
				.map(process -> (Invocable)process)
				.collect(Collectors.toList());
		invocables.add(exchange);
		
		// verify no process leads to a knowledge compromise
		for (Invocable invocable : invocables) {
			// get all output parameters		
			Set<KnowledgePath> outputParameters = invocable.getParameters().stream()
					.filter(param -> param.getKind() == ParameterKind.OUT || param.getKind() == ParameterKind.INOUT)
					.map(param -> param.getKnowledgePath())
					.collect(Collectors.toSet());
			
			for (KnowledgePath outputParameterPath : outputParameters) {
				SecurityTagCollection outputParamSecurity;
				// get output parameter security
				if (invocable instanceof ComponentProcess) {
					KnowledgeManager knowledgeManager = ((ComponentProcess)invocable).getComponentInstance().getKnowledgeManager();
					outputParamSecurity = SecurityTagCollection.getSecurityTags(outputParameterPath, knowledgeManager);
				} else {
					KnowledgeManager localKnowledgeManager = component.getKnowledgeManager();
					outputParamSecurity = SecurityTagCollection.getSecurityTags(pathRoot, outputParameterPath, localKnowledgeManager, shadowKnowledgeManager, null);
				}
				
				// get transitive input dependencies
				Set<KnowledgePath> transitiveInputParameters = new HashSet<>(); 
				getAllTransitiveInputParameters(outputParameterPath, invocable, invocables, transitiveInputParameters); 
				
				for (KnowledgePath inputParameterPath : transitiveInputParameters) {
					// get input parameter security
					SecurityTagCollection inputParamSecurity;
					if (invocable instanceof ComponentProcess) {
						KnowledgeManager knowledgeManager = ((ComponentProcess)invocable).getComponentInstance().getKnowledgeManager();
						inputParamSecurity = SecurityTagCollection.getSecurityTags(inputParameterPath, knowledgeManager);
					} else {
						KnowledgeManager localKnowledgeManager = component.getKnowledgeManager();
						inputParamSecurity = SecurityTagCollection.getSecurityTags(pathRoot, inputParameterPath, localKnowledgeManager, shadowKnowledgeManager, null);
					}
					
					if (!isMoreRestrictive(outputParamSecurity, inputParamSecurity)) {
						errorList.add(String.format("Parameter %s is not appropriately secured.", outputParameterPath.toString()));
					}
				}	
			}		
		}
		
				
		
		return errorList;
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
	public static boolean isMoreRestrictive(SecurityTagCollection out, SecurityTagCollection in) {		
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
	
	private static boolean conjunctionWithoutLocalExists(SecurityTagCollection formula) {
		return formula.stream().anyMatch(conjunction -> !conjunction.stream().anyMatch(tag -> tag instanceof LocalKnowledgeTag ) );
	}
	
	private static boolean satisfiesConjunction(List<SecurityTag> outConjunction, SecurityTagCollection in) {
		// any of the conjunctions in the input must be overriden by the output conjunction
		return in.stream().anyMatch(inConjunction -> satisfiesConjunction(outConjunction, inConjunction));
	}

	private static boolean satisfiesConjunction(List<SecurityTag> outConjunction, List<SecurityTag> inConjunction) {
		// either an output conjunction contains local knowledge
		// or each tag in the input conjunction is overriden in the output
		return  outConjunction.stream().anyMatch(outTag -> outTag instanceof LocalKnowledgeTag) ||
				inConjunction.stream().allMatch(inTag -> tagPresentOrOverriden(inTag, outConjunction));
	}

	private static boolean tagPresentOrOverriden(SecurityTag inTag, List<SecurityTag> outConjunction) {
		// any of the output tags must override the input tag
		return outConjunction.stream().anyMatch(outTag -> satisfies(inTag, outTag));
	}

	private static boolean satisfies(SecurityTag inTag, SecurityTag outTag) {		
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
			
			return roleMatched;
		}
		return false;
	}

	private static boolean satisfies(SecurityRoleArgument inArgument, SecurityRoleArgument outArgument) {
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
	protected static void getAllTransitiveInputParameters(KnowledgePath outputParameterPath, Invocable process, Collection<Invocable> allProcesses, Set<KnowledgePath> result) {
		Set<KnowledgePath> inputParameters = process.getParameters().stream()
				.filter(param -> param.getKind() == ParameterKind.IN || param.getKind() == ParameterKind.INOUT)
				.map(param -> param.getKnowledgePath())
				.collect(Collectors.toSet());
		
		if (result.containsAll(inputParameters)) return;
		result.addAll(inputParameters);
		
		for (KnowledgePath inputParameterPath : inputParameters) {
			for (Invocable anotherProcess : allProcesses) {
				Set<KnowledgePath> outputParameters = anotherProcess.getParameters().stream()
						.filter(param -> param.getKind() == ParameterKind.OUT || param.getKind() == ParameterKind.INOUT)
						.map(param -> param.getKnowledgePath())
						.collect(Collectors.toSet());
				
				if (outputParameters.contains(inputParameterPath)) {
					getAllTransitiveInputParameters(inputParameterPath, anotherProcess, allProcesses, result);
				}
			}
		}
	
	}

}
