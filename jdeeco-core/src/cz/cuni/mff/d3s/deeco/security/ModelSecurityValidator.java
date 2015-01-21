package cz.cuni.mff.d3s.deeco.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.ReadOnlyKnowledgeManager;
import cz.cuni.mff.d3s.deeco.model.runtime.api.AbsoluteSecurityRoleArgument;
import cz.cuni.mff.d3s.deeco.model.runtime.api.BlankSecurityRoleArgument;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Exchange;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeSecurityTag;
import cz.cuni.mff.d3s.deeco.model.runtime.api.LocalKnowledgeTag;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Parameter;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ParameterKind;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathSecurityRoleArgument;
import cz.cuni.mff.d3s.deeco.model.runtime.api.SecurityRole;
import cz.cuni.mff.d3s.deeco.model.runtime.api.SecurityRoleArgument;
import cz.cuni.mff.d3s.deeco.model.runtime.api.SecurityTag;
import cz.cuni.mff.d3s.deeco.task.KnowledgePathHelper.PathRoot;

/**
 * 
 * @author Ondřej Štumpf
 *
 */
public class ModelSecurityValidator {
	
	public static List<String> validate(ComponentInstance component) {
		List<String> errorList = new ArrayList<>();
		
		// no need to check ratings process since it can't change any knowledge
		
		// check security integrity in processes
		for (ComponentProcess process : component.getComponentProcesses()) {
			errorList.addAll(validate(process));
		}
		
		return errorList;
	}

	public static List<String> validate(ComponentProcess process) {
		List<String> errorList = new ArrayList<>();
		
		List<Parameter> inputParameters = process.getParameters().stream()
				.filter(param -> param.getKind() == ParameterKind.IN || param.getKind() == ParameterKind.INOUT)
				.collect(Collectors.toList());
		List<Parameter> outputParameters = process.getParameters().stream()
				.filter(param -> param.getKind() == ParameterKind.OUT || param.getKind() == ParameterKind.INOUT)
				.collect(Collectors.toList());
		
		// each output parameter needs to be local or have higher security than all the input parameters
		for (Parameter outputParameter : outputParameters) {
			KnowledgeManager knowledgeManager = process.getComponentInstance().getKnowledgeManager();
			SecurityTagCollection outputParamSecurity = SecurityTagCollection.getSecurityTags(outputParameter.getKnowledgePath(), knowledgeManager);
			
			for (Parameter inputParameter : inputParameters) {				
				SecurityTagCollection inputParamSecurity = SecurityTagCollection.getSecurityTags(inputParameter.getKnowledgePath(), knowledgeManager);
				
				if (!isMoreRestrictive(outputParamSecurity, inputParamSecurity)) {
					errorList.add(String.format("Parameter %s is not appropriately secured.", outputParameter.getKnowledgePath().toString()));
				}
			}			
		}		
		
		return errorList;
	}

	public static List<String> validate(PathRoot pathRoot, Exchange exchange, ComponentInstance component, ReadOnlyKnowledgeManager shadowKnowledgeManager) {
		List<String> errorList = new ArrayList<>();
		
		// get all input parameters
		List<Parameter> inputParameters = exchange.getParameters().stream()
				.filter(param -> param.getKind() == ParameterKind.IN || param.getKind() == ParameterKind.INOUT)
				.collect(Collectors.toList());

		// get all output parameters		
		List<Parameter> outputParameters = exchange.getParameters().stream()
				.filter(param -> param.getKind() == ParameterKind.OUT || param.getKind() == ParameterKind.INOUT)				
				.collect(Collectors.toList());
				
		// each output parameter needs to be local or have higher security than all the input parameters
		for (Parameter outputParameter : outputParameters) {
			KnowledgeManager localKnowledgeManager = component.getKnowledgeManager();
			SecurityTagCollection outputParamSecurity = SecurityTagCollection.getSecurityTags(pathRoot, outputParameter.getKnowledgePath(), localKnowledgeManager, shadowKnowledgeManager, null);
			
			for (Parameter inputParameter : inputParameters) {				
				SecurityTagCollection inputParamSecurity = SecurityTagCollection.getSecurityTags(pathRoot, inputParameter.getKnowledgePath(), localKnowledgeManager, shadowKnowledgeManager, null);
				
				if (!isMoreRestrictive(outputParamSecurity, inputParamSecurity)) {
					errorList.add(String.format("Parameter %s is not appropriately secured.", outputParameter.getKnowledgePath().toString()));
				}
			}			
		}		
		
		return errorList;
	}
	
	
	public static boolean isMoreRestrictive(SecurityTagCollection out, SecurityTagCollection in) {		
		if (out.isEmpty() && !in.isEmpty()) {
			return false;
		}
		if (in.isEmpty() && !out.isEmpty()) {
			return true;
		}
		return out.stream().allMatch(conjunction -> satisfiesConjunction(conjunction, in));
	}
	
	private static boolean satisfiesConjunction(List<SecurityTag> outConjunction, SecurityTagCollection in) {
		return in.stream().anyMatch(inConjunction -> satisfiesConjunction(outConjunction, inConjunction));
	}

	private static boolean satisfiesConjunction(List<SecurityTag> outConjunction, List<SecurityTag> inConjunction) {
		return  outConjunction.stream().anyMatch(outTag -> outTag instanceof LocalKnowledgeTag) ||
				inConjunction.stream().allMatch(inTag -> tagPresentOrOverriden(inTag, outConjunction));
	}

	private static boolean tagPresentOrOverriden(SecurityTag inTag, List<SecurityTag> outConjunction) {
		return outConjunction.stream().anyMatch(outTag -> satisfies(inTag, outTag));
	}

	private static boolean satisfies(SecurityTag inTag, SecurityTag outTag) {		
		if (inTag instanceof KnowledgeSecurityTag && outTag instanceof KnowledgeSecurityTag) {
			KnowledgeSecurityTag inKnowledgeTag = (KnowledgeSecurityTag) inTag;
			KnowledgeSecurityTag outKnowledgeTag = (KnowledgeSecurityTag) outTag;
			
			List<SecurityRole> transitiveRoles = RoleHelper.getTransitiveRoles(Arrays.asList(outKnowledgeTag.getRequiredRole()));
			boolean roleMatched = false;
			
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
		
		if (inArgument instanceof BlankSecurityRoleArgument) {
			return namesMatch;
		}
		if (inArgument instanceof PathSecurityRoleArgument && outArgument instanceof PathSecurityRoleArgument) {
			return namesMatch && ((PathSecurityRoleArgument)inArgument).getKnowledgePath().equals(((PathSecurityRoleArgument)outArgument).getKnowledgePath());
		}
		if (inArgument instanceof AbsoluteSecurityRoleArgument && outArgument instanceof AbsoluteSecurityRoleArgument) {
			Object inValue = ((AbsoluteSecurityRoleArgument)inArgument).getValue();
			Object outValue = ((AbsoluteSecurityRoleArgument)outArgument).getValue();
			return namesMatch && ((inValue == null && outValue == null) || (inValue != null && inValue.equals(outValue)));
		}
		
		return false;
	}

	
}
