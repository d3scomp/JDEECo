package cz.cuni.mff.d3s.jdeeco.edl.utils;

import cz.cuni.mff.d3s.jdeeco.edl.model.edl.EnsembleDefinition;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.FunctionCall;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.KnowledgeVariable;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.Query;

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
	
	@Override
	public String visit(FunctionCall query) {
		StringBuilder builder = new StringBuilder();		
		
		builder.append("(" + 
		EDLUtils.getJavaTypeName(typeContext.getFunctionRegistry().getFunctionReturnType(typeContext, parentEnsemble, query.getName(), query.getParameters().toArray(new Query[]{}))) 
		+") new " + query.getName() + "().evaluate(");
		
		for (Query p : query.getParameters()) {
			builder.append(p.accept(this));
		}
		
		builder.append(")");
		return builder.toString();
	}
}
