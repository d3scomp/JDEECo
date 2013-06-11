/**
 */
package cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl;

import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Component;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.KnowledgeDefinition;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.RuntimemodelPackage;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.TopLevelKnowledgeDefinition;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Component</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.ComponentImpl#getKnowledge <em>Knowledge</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.ComponentImpl#getProcesses <em>Processes</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.ComponentImpl#getName <em>Name</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ComponentImpl extends EObjectImpl implements Component {
	/**
	 * The cached value of the '{@link #getKnowledge() <em>Knowledge</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKnowledge()
	 * @generated
	 * @ordered
	 */
	protected EList<TopLevelKnowledgeDefinition> knowledge;

	/**
	 * The cached value of the '{@link #getProcesses() <em>Processes</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProcesses()
	 * @generated
	 * @ordered
	 */
	protected EList<cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Process> processes;

	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ComponentImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return RuntimemodelPackage.Literals.COMPONENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<TopLevelKnowledgeDefinition> getKnowledge() {
		if (knowledge == null) {
			knowledge = new EObjectContainmentWithInverseEList<TopLevelKnowledgeDefinition>(TopLevelKnowledgeDefinition.class, this, RuntimemodelPackage.COMPONENT__KNOWLEDGE, RuntimemodelPackage.TOP_LEVEL_KNOWLEDGE_DEFINITION__COMPONENT);
		}
		return knowledge;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Process> getProcesses() {
		if (processes == null) {
			processes = new EObjectContainmentWithInverseEList<cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Process>(cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Process.class, this, RuntimemodelPackage.COMPONENT__PROCESSES, RuntimemodelPackage.PROCESS__COMPONENT);
		}
		return processes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RuntimemodelPackage.COMPONENT__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public KnowledgeDefinition findDefinition(String path) {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case RuntimemodelPackage.COMPONENT__KNOWLEDGE:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getKnowledge()).basicAdd(otherEnd, msgs);
			case RuntimemodelPackage.COMPONENT__PROCESSES:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getProcesses()).basicAdd(otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case RuntimemodelPackage.COMPONENT__KNOWLEDGE:
				return ((InternalEList<?>)getKnowledge()).basicRemove(otherEnd, msgs);
			case RuntimemodelPackage.COMPONENT__PROCESSES:
				return ((InternalEList<?>)getProcesses()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case RuntimemodelPackage.COMPONENT__KNOWLEDGE:
				return getKnowledge();
			case RuntimemodelPackage.COMPONENT__PROCESSES:
				return getProcesses();
			case RuntimemodelPackage.COMPONENT__NAME:
				return getName();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case RuntimemodelPackage.COMPONENT__KNOWLEDGE:
				getKnowledge().clear();
				getKnowledge().addAll((Collection<? extends TopLevelKnowledgeDefinition>)newValue);
				return;
			case RuntimemodelPackage.COMPONENT__PROCESSES:
				getProcesses().clear();
				getProcesses().addAll((Collection<? extends cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Process>)newValue);
				return;
			case RuntimemodelPackage.COMPONENT__NAME:
				setName((String)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case RuntimemodelPackage.COMPONENT__KNOWLEDGE:
				getKnowledge().clear();
				return;
			case RuntimemodelPackage.COMPONENT__PROCESSES:
				getProcesses().clear();
				return;
			case RuntimemodelPackage.COMPONENT__NAME:
				setName(NAME_EDEFAULT);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case RuntimemodelPackage.COMPONENT__KNOWLEDGE:
				return knowledge != null && !knowledge.isEmpty();
			case RuntimemodelPackage.COMPONENT__PROCESSES:
				return processes != null && !processes.isEmpty();
			case RuntimemodelPackage.COMPONENT__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (name: ");
		result.append(name);
		result.append(')');
		return result.toString();
	}

} //ComponentImpl
