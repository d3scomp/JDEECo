/**
 */
package cz.cuni.mff.d3s.deeco.model.runtime.impl;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.ShadowKnowledgeManagerRegistry;

import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentMode;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleController;

import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.EcoreEMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Component Instance</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.ComponentInstanceImpl#getComponentProcesses <em>Component Processes</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.ComponentInstanceImpl#getName <em>Name</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.ComponentInstanceImpl#getKnowledgeManager <em>Knowledge Manager</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.ComponentInstanceImpl#getShadowKnowledgeManagerRegistry <em>Shadow Knowledge Manager Registry</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.ComponentInstanceImpl#getEnsembleControllers <em>Ensemble Controllers</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.ComponentInstanceImpl#getInternalData <em>Internal Data</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.ComponentInstanceImpl#getComponentModes <em>Component Modes</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ComponentInstanceImpl extends MinimalEObjectImpl.Container implements ComponentInstance {
	/**
	 * The cached value of the '{@link #getComponentProcesses() <em>Component Processes</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getComponentProcesses()
	 * @generated
	 * @ordered
	 */
	protected EList<ComponentProcess> componentProcesses;

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
	 * The default value of the '{@link #getShadowKnowledgeManagerRegistry() <em>Shadow Knowledge Manager Registry</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getShadowKnowledgeManagerRegistry()
	 * @generated
	 * @ordered
	 */
	protected static final ShadowKnowledgeManagerRegistry SHADOW_KNOWLEDGE_MANAGER_REGISTRY_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getShadowKnowledgeManagerRegistry() <em>Shadow Knowledge Manager Registry</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getShadowKnowledgeManagerRegistry()
	 * @generated
	 * @ordered
	 */
	protected ShadowKnowledgeManagerRegistry shadowKnowledgeManagerRegistry = SHADOW_KNOWLEDGE_MANAGER_REGISTRY_EDEFAULT;

	/**
	 * The cached value of the '{@link #getEnsembleControllers() <em>Ensemble Controllers</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEnsembleControllers()
	 * @generated
	 * @ordered
	 */
	protected EList<EnsembleController> ensembleControllers;

	/**
	 * The cached value of the '{@link #getInternalData() <em>Internal Data</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInternalData()
	 * @generated
	 * @ordered
	 */
	protected EMap<String, Object> internalData;

	/**
	 * The cached value of the '{@link #getComponentModes() <em>Component Modes</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getComponentModes()
	 * @generated
	 * @ordered
	 */
	protected EList<ComponentMode> componentModes;

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
	public EList<ComponentProcess> getComponentProcesses() {
		if (componentProcesses == null) {
			componentProcesses = new EObjectContainmentWithInverseEList<ComponentProcess>(ComponentProcess.class, this, RuntimeMetadataPackage.COMPONENT_INSTANCE__COMPONENT_PROCESSES, RuntimeMetadataPackage.COMPONENT_PROCESS__COMPONENT_INSTANCE);
		}
		return componentProcesses;
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
	public ShadowKnowledgeManagerRegistry getShadowKnowledgeManagerRegistry() {
		return shadowKnowledgeManagerRegistry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setShadowKnowledgeManagerRegistry(ShadowKnowledgeManagerRegistry newShadowKnowledgeManagerRegistry) {
		ShadowKnowledgeManagerRegistry oldShadowKnowledgeManagerRegistry = shadowKnowledgeManagerRegistry;
		shadowKnowledgeManagerRegistry = newShadowKnowledgeManagerRegistry;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RuntimeMetadataPackage.COMPONENT_INSTANCE__SHADOW_KNOWLEDGE_MANAGER_REGISTRY, oldShadowKnowledgeManagerRegistry, shadowKnowledgeManagerRegistry));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<EnsembleController> getEnsembleControllers() {
		if (ensembleControllers == null) {
			ensembleControllers = new EObjectContainmentWithInverseEList<EnsembleController>(EnsembleController.class, this, RuntimeMetadataPackage.COMPONENT_INSTANCE__ENSEMBLE_CONTROLLERS, RuntimeMetadataPackage.ENSEMBLE_CONTROLLER__COMPONENT_INSTANCE);
		}
		return ensembleControllers;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EMap<String, Object> getInternalData() {
		if (internalData == null) {
			internalData = new EcoreEMap<String,Object>(RuntimeMetadataPackage.Literals.STRING_TO_OBJECT_MAP, StringToObjectMapImpl.class, this, RuntimeMetadataPackage.COMPONENT_INSTANCE__INTERNAL_DATA);
		}
		return internalData;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ComponentMode> getComponentModes() {
		if (componentModes == null) {
			componentModes = new EObjectContainmentEList<ComponentMode>(ComponentMode.class, this, RuntimeMetadataPackage.COMPONENT_INSTANCE__COMPONENT_MODES);
		}
		return componentModes;
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
			case RuntimeMetadataPackage.COMPONENT_INSTANCE__COMPONENT_PROCESSES:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getComponentProcesses()).basicAdd(otherEnd, msgs);
			case RuntimeMetadataPackage.COMPONENT_INSTANCE__ENSEMBLE_CONTROLLERS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getEnsembleControllers()).basicAdd(otherEnd, msgs);
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
			case RuntimeMetadataPackage.COMPONENT_INSTANCE__COMPONENT_PROCESSES:
				return ((InternalEList<?>)getComponentProcesses()).basicRemove(otherEnd, msgs);
			case RuntimeMetadataPackage.COMPONENT_INSTANCE__ENSEMBLE_CONTROLLERS:
				return ((InternalEList<?>)getEnsembleControllers()).basicRemove(otherEnd, msgs);
			case RuntimeMetadataPackage.COMPONENT_INSTANCE__INTERNAL_DATA:
				return ((InternalEList<?>)getInternalData()).basicRemove(otherEnd, msgs);
			case RuntimeMetadataPackage.COMPONENT_INSTANCE__COMPONENT_MODES:
				return ((InternalEList<?>)getComponentModes()).basicRemove(otherEnd, msgs);
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
			case RuntimeMetadataPackage.COMPONENT_INSTANCE__COMPONENT_PROCESSES:
				return getComponentProcesses();
			case RuntimeMetadataPackage.COMPONENT_INSTANCE__NAME:
				return getName();
			case RuntimeMetadataPackage.COMPONENT_INSTANCE__KNOWLEDGE_MANAGER:
				return getKnowledgeManager();
			case RuntimeMetadataPackage.COMPONENT_INSTANCE__SHADOW_KNOWLEDGE_MANAGER_REGISTRY:
				return getShadowKnowledgeManagerRegistry();
			case RuntimeMetadataPackage.COMPONENT_INSTANCE__ENSEMBLE_CONTROLLERS:
				return getEnsembleControllers();
			case RuntimeMetadataPackage.COMPONENT_INSTANCE__INTERNAL_DATA:
				if (coreType) return getInternalData();
				else return getInternalData().map();
			case RuntimeMetadataPackage.COMPONENT_INSTANCE__COMPONENT_MODES:
				return getComponentModes();
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
			case RuntimeMetadataPackage.COMPONENT_INSTANCE__COMPONENT_PROCESSES:
				getComponentProcesses().clear();
				getComponentProcesses().addAll((Collection<? extends ComponentProcess>)newValue);
				return;
			case RuntimeMetadataPackage.COMPONENT_INSTANCE__NAME:
				setName((String)newValue);
				return;
			case RuntimeMetadataPackage.COMPONENT_INSTANCE__KNOWLEDGE_MANAGER:
				setKnowledgeManager((KnowledgeManager)newValue);
				return;
			case RuntimeMetadataPackage.COMPONENT_INSTANCE__SHADOW_KNOWLEDGE_MANAGER_REGISTRY:
				setShadowKnowledgeManagerRegistry((ShadowKnowledgeManagerRegistry)newValue);
				return;
			case RuntimeMetadataPackage.COMPONENT_INSTANCE__ENSEMBLE_CONTROLLERS:
				getEnsembleControllers().clear();
				getEnsembleControllers().addAll((Collection<? extends EnsembleController>)newValue);
				return;
			case RuntimeMetadataPackage.COMPONENT_INSTANCE__INTERNAL_DATA:
				((EStructuralFeature.Setting)getInternalData()).set(newValue);
				return;
			case RuntimeMetadataPackage.COMPONENT_INSTANCE__COMPONENT_MODES:
				getComponentModes().clear();
				getComponentModes().addAll((Collection<? extends ComponentMode>)newValue);
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
			case RuntimeMetadataPackage.COMPONENT_INSTANCE__COMPONENT_PROCESSES:
				getComponentProcesses().clear();
				return;
			case RuntimeMetadataPackage.COMPONENT_INSTANCE__NAME:
				setName(NAME_EDEFAULT);
				return;
			case RuntimeMetadataPackage.COMPONENT_INSTANCE__KNOWLEDGE_MANAGER:
				setKnowledgeManager(KNOWLEDGE_MANAGER_EDEFAULT);
				return;
			case RuntimeMetadataPackage.COMPONENT_INSTANCE__SHADOW_KNOWLEDGE_MANAGER_REGISTRY:
				setShadowKnowledgeManagerRegistry(SHADOW_KNOWLEDGE_MANAGER_REGISTRY_EDEFAULT);
				return;
			case RuntimeMetadataPackage.COMPONENT_INSTANCE__ENSEMBLE_CONTROLLERS:
				getEnsembleControllers().clear();
				return;
			case RuntimeMetadataPackage.COMPONENT_INSTANCE__INTERNAL_DATA:
				getInternalData().clear();
				return;
			case RuntimeMetadataPackage.COMPONENT_INSTANCE__COMPONENT_MODES:
				getComponentModes().clear();
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
			case RuntimeMetadataPackage.COMPONENT_INSTANCE__COMPONENT_PROCESSES:
				return componentProcesses != null && !componentProcesses.isEmpty();
			case RuntimeMetadataPackage.COMPONENT_INSTANCE__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case RuntimeMetadataPackage.COMPONENT_INSTANCE__KNOWLEDGE_MANAGER:
				return KNOWLEDGE_MANAGER_EDEFAULT == null ? knowledgeManager != null : !KNOWLEDGE_MANAGER_EDEFAULT.equals(knowledgeManager);
			case RuntimeMetadataPackage.COMPONENT_INSTANCE__SHADOW_KNOWLEDGE_MANAGER_REGISTRY:
				return SHADOW_KNOWLEDGE_MANAGER_REGISTRY_EDEFAULT == null ? shadowKnowledgeManagerRegistry != null : !SHADOW_KNOWLEDGE_MANAGER_REGISTRY_EDEFAULT.equals(shadowKnowledgeManagerRegistry);
			case RuntimeMetadataPackage.COMPONENT_INSTANCE__ENSEMBLE_CONTROLLERS:
				return ensembleControllers != null && !ensembleControllers.isEmpty();
			case RuntimeMetadataPackage.COMPONENT_INSTANCE__INTERNAL_DATA:
				return internalData != null && !internalData.isEmpty();
			case RuntimeMetadataPackage.COMPONENT_INSTANCE__COMPONENT_MODES:
				return componentModes != null && !componentModes.isEmpty();
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
		result.append(", knowledgeManager: ");
		result.append(knowledgeManager);
		result.append(", shadowKnowledgeManagerRegistry: ");
		result.append(shadowKnowledgeManagerRegistry);
		result.append(')');
		return result.toString();
	}

} //ComponentInstanceImpl
