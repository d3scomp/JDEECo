package cz.cuni.mff.d3s.deeco.model.runtime.stateflow;

import java.util.ArrayList;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;

import cz.cuni.mff.d3s.deeco.annotations.ModeTransactions;
import cz.cuni.mff.d3s.deeco.annotations.ModeTransition;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage;



public class ComponentModeTransitionRegistry {

	public ComponentModeInfoRegistry cReg = null;
	public KnowledgePath knowledgePath = null;
	public StateSpaceModel statespace = null;
	public ArrayList<ComponentModeTransition> transitions = new ArrayList<ComponentModeTransition>();
	protected EList<Trigger> triggers = null;
	
	public ComponentModeTransitionRegistry(KnowledgePath kp, ComponentModeInfoRegistry cReg) {
		this.knowledgePath = kp;
		this.cReg = cReg;
	}
	
	
	public void registerModeTransition(ModeTransactions modeTrans){
		
		for (ModeTransition modeTran : modeTrans.transitions()) {
			ComponentModeTransition newTran = new ComponentModeTransition();
			for (ComponentMode cm : cReg.modes) {
				if(cm.name.equals(modeTran.fromMode().getSimpleName())){
					newTran.fromMode = cm;
				}
				if(cm.name.equals(modeTran.toMode().getSimpleName())){
					newTran.fromMode = cm;
				}
			}
			newTran.transitionCondition = modeTran.transitionCondition();
			transitions.add(newTran);
//			statespace = modeTrans.statespace();
		}
	}
	
	
	public void setTriggers(EList<Trigger> triggers) {
		this.triggers = triggers;
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
//	public EList<Trigger> getTriggers() {
//		if (triggers == null) {
//			triggers = new EObjectContainmentEList<Trigger>(Trigger.class, this, RuntimeMetadataPackage.COMPONENT_PROCESS__TRIGGERS);
//		}
//		return triggers;
//	}
}
