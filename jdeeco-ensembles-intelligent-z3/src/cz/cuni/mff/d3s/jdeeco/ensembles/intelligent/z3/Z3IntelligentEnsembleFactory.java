package cz.cuni.mff.d3s.jdeeco.ensembles.intelligent.z3;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.UnsupportedDataTypeException;

import com.microsoft.z3.ArrayExpr;
import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;
import com.microsoft.z3.FuncInterp.Entry;
import com.microsoft.z3.IntExpr;
import com.microsoft.z3.Model;
import com.microsoft.z3.Optimize;
import com.microsoft.z3.Sort;
import com.microsoft.z3.Status;
import com.microsoft.z3.enumerations.Z3_lbool;

import cz.cuni.mff.d3s.deeco.ensembles.EnsembleFactory;
import cz.cuni.mff.d3s.deeco.ensembles.EnsembleFormationException;
import cz.cuni.mff.d3s.deeco.ensembles.EnsembleInstance;
import cz.cuni.mff.d3s.deeco.knowledge.container.KnowledgeContainer;
import cz.cuni.mff.d3s.deeco.knowledge.container.KnowledgeContainerException;
import cz.cuni.mff.d3s.jdeeco.edl.BaseDataContract;
import cz.cuni.mff.d3s.jdeeco.edl.PrimitiveTypes;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.DataContractDefinition;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.EdlDocument;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.EnsembleDefinition;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.FieldDeclaration;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.RoleDefinition;
import cz.cuni.mff.d3s.jdeeco.edl.utils.ITypeResolutionContext;
import cz.cuni.mff.d3s.jdeeco.edl.utils.SimpleTypeResolutionContext;

class KnowledgeFieldVector {
	private ArrayExpr values; // for individual components
	private Context ctx;
	
	public KnowledgeFieldVector(Context ctx, String dataContractName, String fieldName, String fieldType)
			throws UnsupportedDataTypeException {
		
		this.ctx = ctx;
		
		Sort sort;
		if (PrimitiveTypes.BOOL.equals(fieldType)) {
			sort = ctx.mkBoolSort();
		} else if (PrimitiveTypes.INT.equals(fieldType)) {
			sort = ctx.mkIntSort();
		} else {
			throw new UnsupportedDataTypeException(fieldType);
		}
		
		values = ctx.mkArrayConst(dataContractName + "_" + fieldName + "_vals", ctx.mkIntSort(), sort);
	}
	
	public void set(int componentIndex, Expr value) {
		ctx.mkStore(values, ctx.mkInt(componentIndex), value);
	}
	
	public Expr get(Expr componentIndex) {
		return ctx.mkSelect(values, componentIndex);
	}
	
}

class DataContractInstancesContainer {
	private BaseDataContract[] instances;
	private Map<String, KnowledgeFieldVector> knowledgeFields;
	private String dataContractName;
	
	public DataContractInstancesContainer(Context ctx, String packageName, DataContractDefinition dataContractDefinition,
			KnowledgeContainer knowledgeContainer)
			throws ClassNotFoundException, KnowledgeContainerException, UnsupportedDataTypeException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		
		@SuppressWarnings("unchecked")
		Class<? extends BaseDataContract> roleClass = (Class<? extends BaseDataContract>) Class.forName(packageName + "." + dataContractDefinition.getName());
		Collection<? extends BaseDataContract> instancesUnsorted = knowledgeContainer.getTrackedKnowledgeForRole(roleClass);
				
		this.instances = new BaseDataContract[instancesUnsorted.size()];
		this.dataContractName = dataContractDefinition.getName();
		for (int i = 0; !instancesUnsorted.isEmpty(); i++) {
			BaseDataContract minInstance = instancesUnsorted.iterator().next();
			for (BaseDataContract instance : instancesUnsorted) {
				if (Integer.parseInt(instance.id) < Integer.parseInt(minInstance.id)) {
					minInstance = instance;
				}
			}
			
			instances[i] = minInstance;
			instancesUnsorted.remove(minInstance);
		}
		
		knowledgeFields = new HashMap<>();
		
		for (FieldDeclaration fieldDecl : dataContractDefinition.getFields()) {
			String fieldName = fieldDecl.getName();
			String fieldType = fieldDecl.getType().toString();
			KnowledgeFieldVector field = new KnowledgeFieldVector(ctx, dataContractDefinition.getName(), fieldName, fieldType);
			knowledgeFields.put(fieldName, field);
			for (int i = 0; i < instances.length; i++) {
				Object value = readField(fieldName, instances[i]);
				Expr expr;
				if (PrimitiveTypes.BOOL.equals(fieldType)) {
					if (value != null)
						expr = ctx.mkBool(((Boolean)value).booleanValue());
					else
						expr = ctx.mkBool(false);
				} else if (PrimitiveTypes.INT.equals(fieldType)) {
					if (value != null)
						expr = ctx.mkInt(((Integer)value).intValue());
					else
						expr = ctx.mkInt(0);
				} else {
					throw new UnsupportedDataTypeException(fieldType);
				}
				
				field.set(i, expr);
			}
		}
	}
	
	private Object readField(String fieldName, BaseDataContract instance) 
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field field = instance.getClass().getField(fieldName);
		return field.get(instance);
	}
	
	public int getMaxEnsembleCount(EnsembleDefinition ensembleDefinition) {
        List<RoleDefinition> roles = ensembleDefinition.getRoles();
        int minCardinalitiesSum = 0;
        for (RoleDefinition roleDefinition : roles) {
        	if (roleDefinition.getType().toString().equals(getName())) {
        		minCardinalitiesSum += roleDefinition.getCardinalityMin();
        	}
        }
        
        if (minCardinalitiesSum == 0) {
        	return Integer.MAX_VALUE;
        }
        
        return getNumInstances() / Math.max(1, minCardinalitiesSum);
	}
	
	public String getName() {
		return dataContractName;
	}
	
	public BaseDataContract getInstance(int componentIndex) {
		return instances[componentIndex];
	}
	
	public Expr get(String fieldName, Expr componentIndex) {
		return knowledgeFields.get(fieldName).get(componentIndex);
	}
	
	public int getNumInstances() {
		return instances.length;
	}
	
}

class DataContainer {
	private Map<String, DataContractInstancesContainer> containers;
	
	public DataContainer(Context ctx, String packageName, Collection<DataContractDefinition> dataContractDefinitions, 
			KnowledgeContainer knowledgeContainer) throws ClassNotFoundException, KnowledgeContainerException, UnsupportedDataTypeException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		
		containers = new HashMap<>();
		
		for (DataContractDefinition contract : dataContractDefinitions) {
			containers.put(contract.getName(), new DataContractInstancesContainer(ctx, packageName, contract, knowledgeContainer));
		}
	}
	
	public int getMaxEnsembleCount(EnsembleDefinition ensembleDefinition) {
		int result = Integer.MAX_VALUE;
		for(DataContractInstancesContainer dataContractInstance : containers.values()) {
			int maxEnsembleCount = dataContractInstance.getMaxEnsembleCount(ensembleDefinition);
			if (maxEnsembleCount < result) {
				result = maxEnsembleCount;
			}
		}
		
		return result;
	}
	
	public Collection<DataContractInstancesContainer> getAllDataContracts() {
		return containers.values();
	}
	
	public DataContractInstancesContainer get(String dataContractName) {
		return containers.get(dataContractName);
	}
	
	public Expr get(String dataContractName, String fieldName, Expr componentIndex) {
		return get(dataContractName).get(fieldName, componentIndex);
	}
	
	public int getNumInstances(String dataContractName) {
		return get(dataContractName).getNumInstances();
	}
	
	public Object getInstance(String dataContractName, int componentIndex) {
		return get(dataContractName).getInstance(componentIndex);
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
			System.out.println("train " + e + ":");
			for (int r = 0; r < assignments.get(e).getRoleCount(); r++) {
				RoleDefinition role = roles.get(r);
				System.out.print("  - " + role.getName() + ": [");
				String dataContractName = role.getType().toString();
				//for (int c = 0; c < dataContainer.getNumInstances(dataContractName); c++) {
				//	System.out.print(m.getConstInterp(assignments.get(e, r, c)).getBoolValue() + " ");
				//}
				Entry[] entries = m.getFuncInterp(assignments.get(e, r).getAssignedSet().getFuncDecl()).getEntries();
				for (Entry entry : entries) {
					if (entry.getValue().getBoolValue() == Z3_lbool.Z3_L_TRUE) {
						System.out.print(entry.getArgs()[0] + " ");
					}
				}
				
				System.out.println("]");
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
				
				String dataContractName = role.getType().toString();
				
				Entry[] entries = m.getFuncInterp(assignments.get(e, r).getAssignedSet().getFuncDecl()).getEntries();
				for (int c = 0; c < dataContainer.getNumInstances(dataContractName); c++) {
					//Expr v1 = m.getConstInterp(assignments.get(e, r, c));
					Expr v1 = ctx.mkBool(false);
					for (Entry entry : entries) {
						if (entry.getArgs()[0].toString().equals(Integer.toString(c))) {
							v1 = entry.getValue();
						}
					}
					
					if (v1.getBoolValue() == Z3_lbool.Z3_L_TRUE) {
						Object instance = dataContainer.getInstance(dataContractName, c);
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

	@Override
	public Collection<EnsembleInstance> createInstances(KnowledgeContainer container) throws EnsembleFormationException {
		try {
			long time_milis = System.currentTimeMillis();

			initConfiguration();

			DataContainer dataContainer = new DataContainer(ctx, edlDocument.getPackage().toString(), edlDocument.getDataContracts(), container);
			
	        EnsembleDefinition ensembleDefinition = edlDocument.getEnsembles().get(0);	        
	        List<RoleDefinition> roles = ensembleDefinition.getRoles();
	        int maxEnsembleCount = dataContainer.getMaxEnsembleCount(ensembleDefinition);
						
			EnsembleAssignmentMatrix assignments = EnsembleAssignmentMatrix.create(
					ctx, opt, maxEnsembleCount, roles, dataContainer);
	
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
			
			// assignment to one <ensemble,role> implies nonassignment to the others
			for (DataContractInstancesContainer dataContract : dataContainer.getAllDataContracts()) {
				for (int c = 0; c < dataContract.getNumInstances(); c++) {
					List<BoolExpr> assigned = new ArrayList<BoolExpr>();
					for (int e = 0; e < maxEnsembleCount; e++) {
						for (int r = 0; r < assignments.get(e).getRoleCount(); r++) {
							if (roles.get(r).getType().toString().equals(dataContract.getName()))
								assigned.add(assignments.get(e, r, c));
						}
					}
								
					opt.Add(ctx.mkOr(assigned.toArray(new BoolExpr [assigned.size()])));
					for (int j = 0; j < assigned.size(); j++) {
						BoolExpr[] assignedOthers = new BoolExpr[assigned.size()-1];
						for (int k = 0; k < assigned.size(); k++) {
							if (k < j)
								assignedOthers[k] = assigned.get(k);
							else if (k > j)
								assignedOthers[k-1] = assigned.get(k);
						}
						
						opt.Add(ctx.mkImplies(assigned.get(j), ctx.mkNot(ctx.mkOr(assignedOthers))));
					}
				}
			}
			
			new ConstraintParser(ctx, opt, dataContainer).parseConstraints(ensembleDefinition);
								
			Status status = opt.Check();
			//System.out.println("Solver: " + opt);
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
	public int getSchedulingOffset() {
		return 0;
	}

	@Override
	public int getSchedulingPeriod() {
		return 1000;
	}

}
