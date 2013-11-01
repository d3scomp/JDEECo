/**
 */
package cz.cuni.mff.d3s.deeco.model.runtime.impl;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;

import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleController;

import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Component Instance</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.ComponentInstanceImpl#getProcesses <em>Processes</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.ComponentInstanceImpl#getId <em>Id</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.ComponentInstanceImpl#getName <em>Name</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.ComponentInstanceImpl#getKnowledgeManager <em>Knowledge Manager</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.ComponentInstanceImpl#getOtherKnowledgeManagersAccess <em>Other Knowledge Managers Access</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.ComponentInstanceImpl#getEnsemblingControllers <em>Ensembling Controllers</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ComponentInstanceImpl extends MinimalEObjectImpl.Container implements ComponentInstance {
	/**
	 * The cached value of the '{@link #getProcesses() <em>Processes</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProcesses()
	 * @generated
	 * @ordered
	 */
	protected EList<cz.cuni.mff.d3s.deeco.model.runtime.api.Process> processes;

	/**
	 * The default value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected static final String ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected String id = ID_EDEFAULT;

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
	 * The default value of the '{@link #getKnowledgeManager() <em>Knowledge Manager</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKnowledgeManager()
	 * @generated
	 * @ordered
	 */
	protected static final KnowledgeManager KNOWLEDGE_MANAGER_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getKnowledgeManager() <em>Knowledge Manager</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKnowledgeManager()
	 * @generated
	 * @ordered
	 */
	protected KnowledgeManager knowledgeManager = KNOWLEDGE_MANAGER_EDEFAULT;

	/**
	 * The default value of the '{@link #getOtherKnowledgeManagersAccess() <em>Other Knowledge Managers Access</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOtherKnowledgeManagersAccess()
	 * @generated
	 * @ordered
	 */
	protected static final Object OTHER_KNOWLEDGE_MANAGERS_ACCESS_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getOtherKnowledgeManagersAccess() <em>Other Knowledge Managers Access</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOtherKnowledgeManagersAccess()
	 * @generated
	 * @ordered
	 */
	protected Object otherKnowledgeManagersAccess = OTHER_KNOWLEDGE_MANAGERS_ACCESS_EDEFAULT;

	/**
	 * The cached value of the '{@link #getEnsemblingControllers() <em>Ensembling Controllers</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEnsemblingControllers()
	 * @generated
	 * @ordered
	 */
	protected EList<EnsembleController> ensemblingControllers;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ComponentInstanceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return RuntimeMetadataPackage.Literals.COMPONENT_INSTANCE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<cz.cuni.mff.d3s.deeco.model.runtime.api.Process> getProcesses() {
		if (processes == null) {
			processes = new EObjectContainmentWithInverseEList<cz.cuni.mff.d3s.deeco.model.runtime.api.Process>(cz.cuni.mff.d3s.deeco.model.runtime.api.Process.class, this, RuntimeMetadataPackage.COMPONENT_INSTANCE__PROCESSES, RuntimeMetadataPackage.PROCESS__COMPONENT_INSTANCE);
		}
		return processes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getId() {
		return id;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setId(String newId) {
		String oldId = id;
		id = newId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RuntimeMetadataPackage.COMPONENT_INSTANCE__ID, oldId, id));
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
			eNotify(new ENotificationImpl(this, Notification.SET, RuntimeMetadataPackage.COMPONENT_INSTANCE__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public KnowledgeManager getKnowledgeManager() {
		return knowledgeManager;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setKnowledgeManager(KnowledgeManager newKnowledgeManager) {
		KnowledgeManager oldKnowledgeManager = knowledgeManager;
		knowledgeManager = newKnowledgeManager;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RuntimeMetadataPackage.COMPONENT_INSTANCE__KNOWLEDGE_MANAGER, oldKnowledgeManager, knowledgeManager));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object getOtherKnowledgeManagersAccess() {
		return otherKnowledgeManagersAccess;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOtherKnowledgeManagersAccess(Object newOtherKnowledgeManagersAccess) {
		Object oldOtherKnowledgeManagersAccess = otherKnowledgeManagersAccess;
		otherKnowledgeManagersAccess = newOtherKnowledgeManagersAccess;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RuntimeMetadataPackage.COMPONENT_INSTANCE__OTHER_KNOWLEDGE_MANAGERS_ACCESS, oldOtherKnowledgeManagersAccess, otherKnowledgeManagersAccess));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<EnsembleController> getEnsemblingControllers() {
		if (ensemblingControllers == null) {
			ensemblingControllers = new EObjectContainmentWithInverseEList<EnsembleController>(EnsembleController.class, this, RuntimeMetadataPackage.COMPONENT_INSTANCE__ENSEMBLING_CONTROLLERS, RuntimeMetadataPackage.ENSEMBLE_CONTROLLER__COMPONENT_INSTANCE);
		}
		return ensemblingControllers;
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
			case RuntimeMetadataPackage.COMPONENT_INSTANCE__PROCESSES:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getProcesses()).basicAdd(otherEnd, msgs);
			case RuntimeMetadataPackage.COMPONENT_INSTANCE__ENSEMBLING_CONTROLLERS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getEnsemblingControllers()).basicAdd(otherEnd, msgs);
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
			case RuntimeMetadataPackage.COMPONENT_INSTANCE__PROCESSES:
				return ((InternalEList<?>)getProcesses()).basicRemove(otherEnd, msgs);
			case RuntimeMetadataPackage.COMPONENT_INSTANCE__ENSEMBLING_CONTROLLERS:
				return ((InternalEList<?>)getEnsemblingControllers()).basicRemove(otherEnd, msgs);
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
			case RuntimeMetadataPackage.COMPONENT_INSTANCE__PROCESSES:
				return getProcesses();
			case RuntimeMetadataPackage.COMPONENT_INSTANCE__ID:
				return getId();
			case RuntimeMetadataPackage.COMPONENT_INSTANCE__NAME:
				return getName();
			case RuntimeMetadataPackage.COMPONENT_INSTANCE__KNOWLEDGE_MANAGER:
				return getKnowledgeManager();
			case RuntimeMetadataPackage.COMPONENT_INSTANCE__OTHER_KNOWLEDGE_MANAGERS_ACCESS:
				return getOtherKnowledgeManagersAccess();
			case RuntimeMetadataPackage.COMPONENT_INSTANCE__ENSEMBLING_CONTROLLERS:
				return getEnsemblingControllers();
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
			case RuntimeMetadataPackage.COMPONENT_INSTANCE__PROCESSES:
				getProcesses().clear();
				getProcesses().addAll((Collection<? extends cz.cuni.mff.d3s.deeco.model.runtime.api.Process>)newValue);
				return;
			case RuntimeMetadataPackage.COMPONENT_INSTANCE__ID:
				setId((String)newValue);
				return;
			case RuntimeMetadataPackage.COMPONENT_INSTANCE__NAME:
				setName((String)newValue);
				return;
			case RuntimeMetadataPackage.COMPONENT_INSTANCE__KNOWLEDGE_MANAGER:
				setKnowledgeManager((KnowledgeManager)newValue);
				return;
			case RuntimeMetadataPackage.COMPONENT_INSTANCE__OTHER_KNOWLEDGE_MANAGERS_ACCESS:
				setOtherKnowledgeManagersAccess(newValue);
				return;
			case RuntimeMetadataPackage.COMPONENT_INSTANCE__ENSEMBLING_CONTROLLERS:
				getEnsemblingControllers().clear();
				getEnsemblingControllers().addAll((Collection<? extends EnsembleController>)newValue);
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
			case RuntimeMetadataPackage.COMPONENT_INSTANCE__PROCESSES:
				getProcesses().clear();
				return;
			case RuntimeMetadataPackage.COMPONENT_INSTANCE__ID:
				setId(ID_EDEFAULT);
				return;
			case RuntimeMetadataPackage.COMPONENT_INSTANCE__NAME:
				setName(NAME_EDEFAULT);
				return;
			case RuntimeMetadataPackage.COMPONENT_INSTANCE__KNOWLEDGE_MANAGER:
				setKnowledgeManager(KNOWLEDGE_MANAGER_EDEFAULT);
				return;
			case RuntimeMetadataPackage.COMPONENT_INSTANCE__OTHER_KNOWLEDGE_MANAGERS_ACCESS:
				setOtherKnowledgeManagersAccess(OTHER_KNOWLEDGE_MANAGERS_ACCESS_EDEFAULT);
				return;
			case RuntimeMetadataPackage.COMPONENT_INSTANCE__ENSEMBLING_CONTROLLERS:
				getEnsemblingControllers().clear();
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
			case RuntimeMetadataPackage.COMPONENT_INSTANCE__PROCESSES:
				return processes != null && !processes.isEmpty();
			case RuntimeMetadataPackage.COMPONENT_INSTANCE__ID:
				return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
			case RuntimeMetadataPackage.COMPONENT_INSTANCE__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case RuntimeMetadataPackage.COMPONENT_INSTANCE__KNOWLEDGE_MANAGER:
				return KNOWLEDGE_MANAGER_EDEFAULT == null ? knowledgeManager != null : !KNOWLEDGE_MANAGER_EDEFAULT.equals(knowledgeManager);
			case RuntimeMetadataPackage.COMPONENT_INSTANCE__OTHER_KNOWLEDGE_MANAGERS_ACCESS:
				return OTHER_KNOWLEDGE_MANAGERS_ACCESS_EDEFAULT == null ? otherKnowledgeManagersAccess != null : !OTHER_KNOWLEDGE_MANAGERS_ACCESS_EDEFAULT.equals(otherKnowledgeManagersAccess);
			case RuntimeMetadataPackage.COMPONENT_INSTANCE__ENSEMBLING_CONTROLLERS:
				return ensemblingControllers != null && !ensemblingControllers.isEmpty();
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
		result.append(" (id: ");
		result.append(id);
		result.append(", name: ");
		result.append(name);
		result.append(", knowledgeManager: ");
		result.append(knowledgeManager);
		result.append(", otherKnowledgeManagersAccess: ");
		result.append(otherKnowledgeManagersAccess);
		result.append(')');
		return result.toString();
	}

} //ComponentInstanceImpl
