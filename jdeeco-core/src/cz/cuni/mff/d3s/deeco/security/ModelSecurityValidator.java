package cz.cuni.mff.d3s.deeco.security;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.ReadOnlyKnowledgeManager;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Exchange;
import cz.cuni.mff.d3s.deeco.model.runtime.api.LocalKnowledgeTag;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Parameter;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ParameterKind;
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
		
		List<Parameter> inputParameters = process.getParameters().stream().filter(param -> param.getKind() == ParameterKind.IN || param.getKind() == ParameterKind.INOUT).collect(Collectors.toList());
		List<Parameter> outputParameters = process.getParameters().stream().filter(param -> param.getKind() == ParameterKind.OUT || param.getKind() == ParameterKind.INOUT).collect(Collectors.toList());
		
		// each output parameter needs to be local or have higher security than all the input parameters
		for (Parameter outputParameter : outputParameters) {
			for (Parameter inputParameter : inputParameters) {
				KnowledgeManager knowledgeManager = process.getComponentInstance().getKnowledgeManager();
				SecurityTagCollection outputParamSecurity = SecurityTagCollection.getSecurityTags(outputParameter.getKnowledgePath(), knowledgeManager);
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
		
		List<Parameter> inputParameters = exchange.getParameters().stream().filter(param -> param.getKind() == ParameterKind.IN || param.getKind() == ParameterKind.INOUT).collect(Collectors.toList());
		List<Parameter> outputParameters = exchange.getParameters().stream().filter(param -> param.getKind() == ParameterKind.OUT || param.getKind() == ParameterKind.INOUT).collect(Collectors.toList());
				
		// each output parameter needs to be local or have higher security than all the input parameters
		for (Parameter outputParameter : outputParameters) {
			for (Parameter inputParameter : inputParameters) {
				KnowledgeManager localKnowledgeManager = component.getKnowledgeManager();
				SecurityTagCollection outputParamSecurity = SecurityTagCollection.getSecurityTags(pathRoot, outputParameter.getKnowledgePath(), localKnowledgeManager, shadowKnowledgeManager, null);
				SecurityTagCollection inputParamSecurity = SecurityTagCollection.getSecurityTags(pathRoot, inputParameter.getKnowledgePath(), localKnowledgeManager, shadowKnowledgeManager, null);
				
				if (!isMoreRestrictive(outputParamSecurity, inputParamSecurity)) {
					errorList.add(String.format("Parameter %s is not appropriately secured.", outputParameter.getKnowledgePath().toString()));
				}
			}			
		}		
		
		return errorList;
	}
	
	
	public static boolean isMoreRestrictive(SecurityTagCollection out, SecurityTagCollection in) {
		if (in.size() > out.size()) {
			return false;
		} else {
			boolean ok = true;
			for (List<SecurityTag> inConjuction : in) {
				boolean isSatisfied = false;
				for (List<SecurityTag> outConjuction : out) {
					boolean containedInOut = inConjuction.stream().allMatch(inTag -> outConjuction.contains(inTag));
					boolean outContainsLocal = outConjuction.stream().anyMatch(tag -> tag instanceof LocalKnowledgeTag);
					isSatisfied = isSatisfied || (containedInOut || outContainsLocal);
				}
				ok = ok && isSatisfied;
			}
			
			return ok;
		}
	}
}
