package cz.cuni.mff.d3s.jdeeco.ensembles.intelligent.z3;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.microsoft.z3.ArithExpr;
import com.microsoft.z3.ArrayExpr;
import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;
import com.microsoft.z3.IntExpr;
import com.microsoft.z3.Model;
import com.microsoft.z3.Optimize;
import com.microsoft.z3.Optimize.Handle;
import com.microsoft.z3.Status;
import com.microsoft.z3.enumerations.Z3_lbool;

import cz.cuni.mff.d3s.deeco.ensembles.EnsembleFactory;
import cz.cuni.mff.d3s.deeco.ensembles.EnsembleFormationException;
import cz.cuni.mff.d3s.deeco.ensembles.EnsembleInstance;
import cz.cuni.mff.d3s.deeco.knowledge.container.KnowledgeContainer;
import cz.cuni.mff.d3s.jdeeco.edl.BaseDataContract;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.EdlDocument;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.EnsembleDefinition;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.RoleDefinition;
import cz.cuni.mff.d3s.jdeeco.edl.utils.ITypeResolutionContext;
import cz.cuni.mff.d3s.jdeeco.edl.utils.SimpleTypeResolutionContext;

class GlobalComponentAssignment {
	private Context ctx;
	public IntExpr ensembleIndexExpr;
	public IntExpr roleIndexExpr;
	
	public GlobalComponentAssignment(Context ctx, String componentId) {
		this.ctx = ctx;
		ensembleIndexExpr = ctx.mkIntConst("all_component_ensembles_" + componentId);
		roleIndexExpr = ctx.mkIntConst("all_component_roleIndex_" + componentId);
	}
	
	public BoolExpr createCondition(EnsembleAssignmentMatrix assignments, int ensembleIndex, int roleIndex, int componentIndex) {
		return ctx.mkEq(ctx.mkAnd(assignments.ensembleExists(ensembleIndex), assignments.get(ensembleIndex, roleIndex, componentIndex)),
				ctx.mkAnd(
						ctx.mkEq(ensembleIndexExpr, ctx.mkInt(ensembleIndex)),
						ctx.mkEq(roleIndexExpr, ctx.mkInt(roleIndex))
						));
	}
}

public class Z3IntelligentEnsembleFactory implements EnsembleFactory {

	private EdlDocument edlDocument;
	private ITypeResolutionContext typeResolution;
	private Context ctx;
	private Optimize opt;
	
	public Z3IntelligentEnsembleFactory(EdlDocument edlDocument) {
		this.edlDocument = edlDocument;
		this.typeResolution = new SimpleTypeResolutionContext(edlDocument);
	}
	
	private void initConfiguration() {
		HashMap<String, String> cfg = new HashMap<String, String>();
        cfg.put("model", "true");
        ctx = new Context(cfg);
        opt = ctx.mkOptimize();
	}

	private void printModel(Model m, EnsembleAssignmentMatrix assignments, List<RoleDefinition> roles,
			DataContainer dataContainer) {
		for (int e = 0; e < assignments.getMaxEnsembleCount(); e++) {
			if (m.getConstInterp(assignments.get(e).ensembleExists()).getBoolValue() == Z3_lbool.Z3_L_TRUE) {			
				System.out.println("ensemble " + e + ":");
				for (int r = 0; r < assignments.get(e).getRoleCount(); r++) {
					RoleDefinition role = roles.get(r);
					System.out.print("  - " + role.getName() + ": [");
					String dataContractName = role.getType().toString();
					//for (int c = 0; c < dataContainer.getNumInstances(dataContractName); c++) {
					//	System.out.print(m.getConstInterp(assignments.get(e, r, c)).getBoolValue() + " ");
					//}
					ComponentAssignmentResults assignmentResults = assignments.get(e, r).getResults(m);
					for (int componentIndex : assignmentResults.getAssignedIndices()) {
						System.out.print(componentIndex + " ");
					}
				
					System.out.println("]");
				}
			} else {
				System.out.println("ensemble " + e + " not instantiated.");
			}
		}
	}
	
	private List<EnsembleInstance> createEnsembles(Model m, EnsembleAssignmentMatrix assignments, EnsembleDefinition ensembleDefinition,
			DataContainer dataContainer, String packageName) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, InstantiationException, ClassNotFoundException {
		int maxEnsembleCount = assignments.getMaxEnsembleCount();
		
		// create ensembles
		List<EnsembleInstance> result = new ArrayList<>();
		List<RoleDefinition> roles = ensembleDefinition.getRoles();
		Class<?> ensembleClass = Class.forName(packageName + "." + ensembleDefinition.getName());
		for (int e = 0; e < maxEnsembleCount; e++) {
			Expr exists = m.getConstInterp(assignments.ensembleExists(e));
			if (exists.getBoolValue() == Z3_lbool.Z3_L_FALSE)
				continue;
			
			// TODO
			//EnsembleInstance ie = (EnsembleInstance) ensembleClass.getConstructor(int.class).
			EnsembleInstance ie = new PendolinoEnsemble(e + 1);

			for (int r = 0; r < assignments.get(e).getRoleCount(); r++) {
				RoleDefinition role = roles.get(r);
				Field roleField = ensembleClass.getField(role.getName());
				List<Object> list = new ArrayList<>();
				if (role.getCardinalityMax() > 1) {
					roleField.set(ie, list);
				}
				
				ComponentAssignmentResults assignmentResults = assignments.get(e, r).getResults(m);
				boolean[] indicators = assignmentResults.getAssignedIndicators();
				for (int c = 0; c < dataContainer.getNumInstances(role.getName(), e); c++) {
					if (indicators[c]) {
						Object instance = dataContainer.getInstance(role.getName(), c, e);
						if (role.getCardinalityMax() == 1) {
							roleField.set(ie, instance);
						} else {
							list.add(instance);
						}
					}
				}
				
				result.add(ie);
			}
		}
		
		return result;
	}

	private Collection<EnsembleInstance> createInstancesOfEnsemble(KnowledgeContainer container, 
			EnsembleDefinition ensembleDefinition) throws EnsembleFormationException {
		
		System.out.println("\nEnsemble: " + ensembleDefinition.getName());
		
		try {
    		long time_milis = System.currentTimeMillis();

    		initConfiguration();

    		List<RoleDefinition> roles = ensembleDefinition.getRoles();
			DataContainer dataContainer = new DataContainer(ctx, opt, edlDocument.getPackage().toString(), edlDocument, container, 
					typeResolution, ensembleDefinition);			
	        int maxEnsembleCount = dataContainer.getMaxEnsembleCount();
	        
	        if (maxEnsembleCount == 0) {
	        	System.out.println("Not enough components of suitable roles to form any ensemble instance of the type " + ensembleDefinition.getName() + ".");
	        	return Collections.emptyList();
	        }
						
			EnsembleAssignmentMatrix assignments = EnsembleAssignmentMatrix.create(
					ctx, opt, maxEnsembleCount, ensembleDefinition, dataContainer);
	
			assignments.createCounters();
			
			// number of rescuers is within cardinality conditions
			for (int i = 0; i < maxEnsembleCount; i++) {
				for (int j = 0; j < assignments.get(i).getRoleCount(); j++) {
					RoleDefinition roleDefinition = roles.get(j);
					BoolExpr le = ctx.mkLe(assignments.getAssignedCount(i, j), ctx.mkInt(roleDefinition.getCardinalityMax()));
					BoolExpr ge = ctx.mkGe(assignments.getAssignedCount(i, j), ctx.mkInt(roleDefinition.getCardinalityMin()));
					BoolExpr cardinalityOk = ctx.mkAnd(le, ge);
					opt.Add(ctx.mkImplies(assignments.ensembleExists(i), cardinalityOk));
					BoolExpr ensembleEmpty = ctx.mkEq(assignments.getAssignedCount(i, j), ctx.mkInt(0));
					opt.Add(ctx.mkImplies(ctx.mkNot(assignments.ensembleExists(i)), ensembleEmpty));
				}
			}

			// nonexistence of an ensemble implies nonexistence of the following ensembles (use consecutive number set from 1)
			for (int i = 1; i < maxEnsembleCount; i++) {
				opt.Add(ctx.mkImplies(ctx.mkNot(assignments.ensembleExists(i-1)), ctx.mkNot(assignments.ensembleExists(i))));
			}
			
			
			Map<String, GlobalComponentAssignment> allComponentEnsembles = new HashMap<>();
			for (int e = 0; e < maxEnsembleCount; e++) {
				for (int r = 0; r < assignments.get(e).getRoleCount(); r++) {
					String roleName = assignments.get(e, r).getRoleName();
					DataContractInstancesContainer dataContract = dataContainer.get(roleName);
					for (int c = 0; c < dataContract.getNumInstances(e); c++) {
						BaseDataContract instance = dataContainer.getInstance(roleName, c, e);
						String id = instance.id;
						GlobalComponentAssignment globalAssignment;
						if (allComponentEnsembles.containsKey(id)) {
							globalAssignment = allComponentEnsembles.get(id);
						} else {
							globalAssignment = new GlobalComponentAssignment(ctx, id);
							allComponentEnsembles.put(id, globalAssignment);
						}
						
						opt.Add(globalAssignment.createCondition(assignments, e, r, c));
					}
				}
			}
			
			ArithExpr[] fitnesses = new ArithExpr[maxEnsembleCount];
			for (int e = 0; e < assignments.getMaxEnsembleCount(); e++) {
				ConstraintParser cp = new ConstraintParser(ctx, opt, dataContainer, assignments.get(e), e, typeResolution);
				cp.parseConstraints();
				fitnesses[e] = cp.parseFitness();
			}
			
			Handle h = opt.MkMaximize(ctx.mkAdd(fitnesses));
			
			opt.Add(assignments.ensembleExists(0));
								
			//System.out.println("Solver: " + opt);
			Status status = opt.Check();
			System.out.println("Maximize: " + h);
			if (status == Status.SATISFIABLE) {
				//System.out.println("Model = " + opt.getModel());
				printModel(opt.getModel(), assignments, roles, dataContainer);
				
				List<EnsembleInstance> result = createEnsembles(opt.getModel(), assignments, ensembleDefinition, 
						dataContainer, edlDocument.getPackage().toString());
				System.out.println("Time taken (ms): " + (System.currentTimeMillis() - time_milis));
				return result;
				
			} else {
				System.out.println("Unsat :-(");
				System.out.println("Time taken (ms): " + (System.currentTimeMillis() - time_milis));
				return Collections.emptyList();
			}
			
		} catch (Exception e) {
			throw new EnsembleFormationException(e);
		}
	}	

	@Override
	public Collection<EnsembleInstance> createInstances(KnowledgeContainer container) throws EnsembleFormationException {

		List<EnsembleInstance> result = new ArrayList<>();
		
        for (EnsembleDefinition ensembleDefinition : edlDocument.getEnsembles()) {	        
        	Collection<EnsembleInstance> instances = createInstancesOfEnsemble(container, ensembleDefinition);
        	result.addAll(instances);
        }
        
        return result;
	}

	@Override
	public int getSchedulingOffset() {
		return 0;
	}

	@Override
	public int getSchedulingPeriod() {
		return 1000;
	}

}
