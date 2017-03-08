/**
 */
package cz.cuni.mff.d3s.jdeeco.edl.model.edl.impl;

import cz.cuni.mff.d3s.jdeeco.edl.model.edl.DataContractDefinition;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.EdlDocument;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.EdlPackage;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.EnsembleDefinition;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.QualifiedName;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.TypeDefinition;
import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Document</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.edl.model.edl.impl.EdlDocumentImpl#getEnsembles <em>Ensembles</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.edl.model.edl.impl.EdlDocumentImpl#getDataContracts <em>Data Contracts</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.edl.model.edl.impl.EdlDocumentImpl#getPackage <em>Package</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.edl.model.edl.impl.EdlDocumentImpl#getKnowledgeTypes <em>Knowledge Types</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class EdlDocumentImpl extends MinimalEObjectImpl.Container implements EdlDocument {
	/**
	 * The cached value of the '{@link #getEnsembles() <em>Ensembles</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEnsembles()
	 * @generated
	 * @ordered
	 */
	protected EList<EnsembleDefinition> ensembles;

	/**
	 * The cached value of the '{@link #getDataContracts() <em>Data Contracts</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDataContracts()
	 * @generated
	 * @ordered
	 */
	protected EList<DataContractDefinition> dataContracts;

	/**
	 * The cached value of the '{@link #getPackage() <em>Package</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPackage()
	 * @generated
	 * @ordered
	 */
	protected QualifiedName package_;

	/**
	 * The cached value of the '{@link #getKnowledgeTypes() <em>Knowledge Types</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKnowledgeTypes()
	 * @generated
	 * @ordered
	 */
	protected EList<TypeDefinition> knowledgeTypes;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EdlDocumentImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return EdlPackage.Literals.EDL_DOCUMENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<EnsembleDefinition> getEnsembles() {
		if (ensembles == null) {
			ensembles = new EObjectContainmentEList<EnsembleDefinition>(EnsembleDefinition.class, this, EdlPackage.EDL_DOCUMENT__ENSEMBLES);
		}
		return ensembles;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<DataContractDefinition> getDataContracts() {
		if (dataContracts == null) {
			dataContracts = new EObjectContainmentEList<DataContractDefinition>(DataContractDefinition.class, this, EdlPackage.EDL_DOCUMENT__DATA_CONTRACTS);
		}
		return dataContracts;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public QualifiedName getPackage() {
		return package_;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetPackage(QualifiedName newPackage, NotificationChain msgs) {
		QualifiedName oldPackage = package_;
		package_ = newPackage;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, EdlPackage.EDL_DOCUMENT__PACKAGE, oldPackage, newPackage);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPackage(QualifiedName newPackage) {
		if (newPackage != package_) {
			NotificationChain msgs = null;
			if (package_ != null)
				msgs = ((InternalEObject)package_).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - EdlPackage.EDL_DOCUMENT__PACKAGE, null, msgs);
			if (newPackage != null)
				msgs = ((InternalEObject)newPackage).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - EdlPackage.EDL_DOCUMENT__PACKAGE, null, msgs);
			msgs = basicSetPackage(newPackage, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EdlPackage.EDL_DOCUMENT__PACKAGE, newPackage, newPackage));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<TypeDefinition> getKnowledgeTypes() {
		if (knowledgeTypes == null) {
			knowledgeTypes = new EObjectContainmentEList<TypeDefinition>(TypeDefinition.class, this, EdlPackage.EDL_DOCUMENT__KNOWLEDGE_TYPES);
		}
		return knowledgeTypes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case EdlPackage.EDL_DOCUMENT__ENSEMBLES:
				return ((InternalEList<?>)getEnsembles()).basicRemove(otherEnd, msgs);
			case EdlPackage.EDL_DOCUMENT__DATA_CONTRACTS:
				return ((InternalEList<?>)getDataContracts()).basicRemove(otherEnd, msgs);
			case EdlPackage.EDL_DOCUMENT__PACKAGE:
				return basicSetPackage(null, msgs);
			case EdlPackage.EDL_DOCUMENT__KNOWLEDGE_TYPES:
				return ((InternalEList<?>)getKnowledgeTypes()).basicRemove(otherEnd, msgs);
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
			case EdlPackage.EDL_DOCUMENT__ENSEMBLES:
				return getEnsembles();
			case EdlPackage.EDL_DOCUMENT__DATA_CONTRACTS:
				return getDataContracts();
			case EdlPackage.EDL_DOCUMENT__PACKAGE:
				return getPackage();
			case EdlPackage.EDL_DOCUMENT__KNOWLEDGE_TYPES:
				return getKnowledgeTypes();
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
			case EdlPackage.EDL_DOCUMENT__ENSEMBLES:
				getEnsembles().clear();
				getEnsembles().addAll((Collection<? extends EnsembleDefinition>)newValue);
				return;
			case EdlPackage.EDL_DOCUMENT__DATA_CONTRACTS:
				getDataContracts().clear();
				getDataContracts().addAll((Collection<? extends DataContractDefinition>)newValue);
				return;
			case EdlPackage.EDL_DOCUMENT__PACKAGE:
				setPackage((QualifiedName)newValue);
				return;
			case EdlPackage.EDL_DOCUMENT__KNOWLEDGE_TYPES:
				getKnowledgeTypes().clear();
				getKnowledgeTypes().addAll((Collection<? extends TypeDefinition>)newValue);
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
			case EdlPackage.EDL_DOCUMENT__ENSEMBLES:
				getEnsembles().clear();
				return;
			case EdlPackage.EDL_DOCUMENT__DATA_CONTRACTS:
				getDataContracts().clear();
				return;
			case EdlPackage.EDL_DOCUMENT__PACKAGE:
				setPackage((QualifiedName)null);
				return;
			case EdlPackage.EDL_DOCUMENT__KNOWLEDGE_TYPES:
				getKnowledgeTypes().clear();
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
			case EdlPackage.EDL_DOCUMENT__ENSEMBLES:
				return ensembles != null && !ensembles.isEmpty();
			case EdlPackage.EDL_DOCUMENT__DATA_CONTRACTS:
				return dataContracts != null && !dataContracts.isEmpty();
			case EdlPackage.EDL_DOCUMENT__PACKAGE:
				return package_ != null;
			case EdlPackage.EDL_DOCUMENT__KNOWLEDGE_TYPES:
				return knowledgeTypes != null && !knowledgeTypes.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //EdlDocumentImpl
