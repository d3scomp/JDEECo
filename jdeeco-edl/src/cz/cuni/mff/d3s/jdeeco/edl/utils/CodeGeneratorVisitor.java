package cz.cuni.mff.d3s.jdeeco.edl.utils;

import cz.cuni.mff.d3s.jdeeco.edl.model.edl.EnsembleDefinition;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.KnowledgeVariable;

public class CodeGeneratorVisitor extends ToStringVisitor {
	
	private ITypeResolutionContext typeContext;
	private EnsembleDefinition parentEnsemble;

	public CodeGeneratorVisitor(ITypeResolutionContext typeContext, EnsembleDefinition parentEnsemble) {
		this.typeContext = typeContext;
		this.parentEnsemble = parentEnsemble;
	}
	
	@Override
	public String visit(KnowledgeVariable query) {		
		return EDLUtils.getAccessPath(typeContext, query.getPath(), parentEnsemble);
	}
}
