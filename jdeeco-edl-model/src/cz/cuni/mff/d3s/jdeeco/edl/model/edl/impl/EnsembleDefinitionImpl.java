/**
 */
package cz.cuni.mff.d3s.jdeeco.edl.model.edl.impl;

import cz.cuni.mff.d3s.jdeeco.edl.model.edl.AliasDefinition;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.ChildDefinition;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.EdlPackage;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.EnsembleDefinition;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.EquitableQuery;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.ExchangeRule;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.IdDefinition;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.Query;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.RoleDefinition;
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
 * An implementation of the model object '<em><b>Ensemble Definition</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.edl.model.edl.impl.EnsembleDefinitionImpl#getName <em>Name</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.edl.model.edl.impl.EnsembleDefinitionImpl#getRoles <em>Roles</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.edl.model.edl.impl.EnsembleDefinitionImpl#getAliases <em>Aliases</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.edl.model.edl.impl.EnsembleDefinitionImpl#getConstraints <em>Constraints</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.edl.model.edl.impl.EnsembleDefinitionImpl#getId <em>Id</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.edl.model.edl.impl.EnsembleDefinitionImpl#getParentEnsemble <em>Parent Ensemble</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.edl.model.edl.impl.EnsembleDefinitionImpl#getChildEnsembles <em>Child Ensembles</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.edl.model.edl.impl.EnsembleDefinitionImpl#getExchangeRules <em>Exchange Rules</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.edl.model.edl.impl.EnsembleDefinitionImpl#getFitness <em>Fitness</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.edl.model.edl.impl.EnsembleDefinitionImpl#isExternalKnowledgeExchange <em>External Knowledge Exchange</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class EnsembleDefinitionImpl extends MinimalEObjectImpl.Container implements EnsembleDefinition {
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
	 * The cached value of the '{@link #getRoles() <em>Roles</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRoles()
	 * @generated
	 * @ordered
	 */
	protected EList<RoleDefinition> roles;

	/**
	 * The cached value of the '{@link #getAliases() <em>Aliases</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAliases()
	 * @generated
	 * @ordered
	 */
	protected EList<AliasDefinition> aliases;

	/**
	 * The cached value of the '{@link #getConstraints() <em>Constraints</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConstraints()
	 * @generated
	 * @ordered
	 */
	protected EList<EquitableQuery> constraints;

	/**
	 * The cached value of the '{@link #getId() <em>Id</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected IdDefinition id;

	/**
	 * The default value of the '{@link #getParentEnsemble() <em>Parent Ensemble</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParentEnsemble()
	 * @generated
	 * @ordered
	 */
	protected static final String PARENT_ENSEMBLE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getParentEnsemble() <em>Parent Ensemble</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParentEnsemble()
	 * @generated
	 * @ordered
	 */
	protected String parentEnsemble = PARENT_ENSEMBLE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getChildEnsembles() <em>Child Ensembles</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getChildEnsembles()
	 * @generated
	 * @ordered
	 */
	protected EList<ChildDefinition> childEnsembles;

	/**
	 * The cached value of the '{@link #getExchangeRules() <em>Exchange Rules</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExchangeRules()
	 * @generated
	 * @ordered
	 */
	protected EList<ExchangeRule> exchangeRules;

	/**
	 * The cached value of the '{@link #getFitness() <em>Fitness</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFitness()
	 * @generated
	 * @ordered
	 */
	protected Query fitness;

	/**
	 * The default value of the '{@link #isExternalKnowledgeExchange() <em>External Knowledge Exchange</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isExternalKnowledgeExchange()
	 * @generated
	 * @ordered
	 */
	protected static final boolean EXTERNAL_KNOWLEDGE_EXCHANGE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isExternalKnowledgeExchange() <em>External Knowledge Exchange</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isExternalKnowledgeExchange()
	 * @generated
	 * @ordered
	 */
	protected boolean externalKnowledgeExchange = EXTERNAL_KNOWLEDGE_EXCHANGE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EnsembleDefinitionImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return EdlPackage.Literals.ENSEMBLE_DEFINITION;
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
			eNotify(new ENotificationImpl(this, Notification.SET, EdlPackage.ENSEMBLE_DEFINITION__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<RoleDefinition> getRoles() {
		if (roles == null) {
			roles = new EObjectContainmentEList<RoleDefinition>(RoleDefinition.class, this, EdlPackage.ENSEMBLE_DEFINITION__ROLES);
		}
		return roles;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<AliasDefinition> getAliases() {
		if (aliases == null) {
			aliases = new EObjectContainmentEList<AliasDefinition>(AliasDefinition.class, this, EdlPackage.ENSEMBLE_DEFINITION__ALIASES);
		}
		return aliases;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<EquitableQuery> getConstraints() {
		if (constraints == null) {
			constraints = new EObjectContainmentEList<EquitableQuery>(EquitableQuery.class, this, EdlPackage.ENSEMBLE_DEFINITION__CONSTRAINTS);
		}
		return constraints;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IdDefinition getId() {
		return id;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetId(IdDefinition newId, NotificationChain msgs) {
		IdDefinition oldId = id;
		id = newId;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, EdlPackage.ENSEMBLE_DEFINITION__ID, oldId, newId);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setId(IdDefinition newId) {
		if (newId != id) {
			NotificationChain msgs = null;
			if (id != null)
				msgs = ((InternalEObject)id).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - EdlPackage.ENSEMBLE_DEFINITION__ID, null, msgs);
			if (newId != null)
				msgs = ((InternalEObject)newId).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - EdlPackage.ENSEMBLE_DEFINITION__ID, null, msgs);
			msgs = basicSetId(newId, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EdlPackage.ENSEMBLE_DEFINITION__ID, newId, newId));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getParentEnsemble() {
		return parentEnsemble;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setParentEnsemble(String newParentEnsemble) {
		String oldParentEnsemble = parentEnsemble;
		parentEnsemble = newParentEnsemble;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EdlPackage.ENSEMBLE_DEFINITION__PARENT_ENSEMBLE, oldParentEnsemble, parentEnsemble));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ChildDefinition> getChildEnsembles() {
		if (childEnsembles == null) {
			childEnsembles = new EObjectContainmentEList<ChildDefinition>(ChildDefinition.class, this, EdlPackage.ENSEMBLE_DEFINITION__CHILD_ENSEMBLES);
		}
		return childEnsembles;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ExchangeRule> getExchangeRules() {
		if (exchangeRules == null) {
			exchangeRules = new EObjectContainmentEList<ExchangeRule>(ExchangeRule.class, this, EdlPackage.ENSEMBLE_DEFINITION__EXCHANGE_RULES);
		}
		return exchangeRules;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Query getFitness() {
		return fitness;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetFitness(Query newFitness, NotificationChain msgs) {
		Query oldFitness = fitness;
		fitness = newFitness;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, EdlPackage.ENSEMBLE_DEFINITION__FITNESS, oldFitness, newFitness);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFitness(Query newFitness) {
		if (newFitness != fitness) {
			NotificationChain msgs = null;
			if (fitness != null)
				msgs = ((InternalEObject)fitness).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - EdlPackage.ENSEMBLE_DEFINITION__FITNESS, null, msgs);
			if (newFitness != null)
				msgs = ((InternalEObject)newFitness).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - EdlPackage.ENSEMBLE_DEFINITION__FITNESS, null, msgs);
			msgs = basicSetFitness(newFitness, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EdlPackage.ENSEMBLE_DEFINITION__FITNESS, newFitness, newFitness));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isExternalKnowledgeExchange() {
		return externalKnowledgeExchange;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setExternalKnowledgeExchange(boolean newExternalKnowledgeExchange) {
		boolean oldExternalKnowledgeExchange = externalKnowledgeExchange;
		externalKnowledgeExchange = newExternalKnowledgeExchange;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EdlPackage.ENSEMBLE_DEFINITION__EXTERNAL_KNOWLEDGE_EXCHANGE, oldExternalKnowledgeExchange, externalKnowledgeExchange));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case EdlPackage.ENSEMBLE_DEFINITION__ROLES:
				return ((InternalEList<?>)getRoles()).basicRemove(otherEnd, msgs);
			case EdlPackage.ENSEMBLE_DEFINITION__ALIASES:
				return ((InternalEList<?>)getAliases()).basicRemove(otherEnd, msgs);
			case EdlPackage.ENSEMBLE_DEFINITION__CONSTRAINTS:
				return ((InternalEList<?>)getConstraints()).basicRemove(otherEnd, msgs);
			case EdlPackage.ENSEMBLE_DEFINITION__ID:
				return basicSetId(null, msgs);
			case EdlPackage.ENSEMBLE_DEFINITION__CHILD_ENSEMBLES:
				return ((InternalEList<?>)getChildEnsembles()).basicRemove(otherEnd, msgs);
			case EdlPackage.ENSEMBLE_DEFINITION__EXCHANGE_RULES:
				return ((InternalEList<?>)getExchangeRules()).basicRemove(otherEnd, msgs);
			case EdlPackage.ENSEMBLE_DEFINITION__FITNESS:
				return basicSetFitness(null, msgs);
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
			case EdlPackage.ENSEMBLE_DEFINITION__NAME:
				return getName();
			case EdlPackage.ENSEMBLE_DEFINITION__ROLES:
				return getRoles();
			case EdlPackage.ENSEMBLE_DEFINITION__ALIASES:
				return getAliases();
			case EdlPackage.ENSEMBLE_DEFINITION__CONSTRAINTS:
				return getConstraints();
			case EdlPackage.ENSEMBLE_DEFINITION__ID:
				return getId();
			case EdlPackage.ENSEMBLE_DEFINITION__PARENT_ENSEMBLE:
				return getParentEnsemble();
			case EdlPackage.ENSEMBLE_DEFINITION__CHILD_ENSEMBLES:
				return getChildEnsembles();
			case EdlPackage.ENSEMBLE_DEFINITION__EXCHANGE_RULES:
				return getExchangeRules();
			case EdlPackage.ENSEMBLE_DEFINITION__FITNESS:
				return getFitness();
			case EdlPackage.ENSEMBLE_DEFINITION__EXTERNAL_KNOWLEDGE_EXCHANGE:
				return isExternalKnowledgeExchange();
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
			case EdlPackage.ENSEMBLE_DEFINITION__NAME:
				setName((String)newValue);
				return;
			case EdlPackage.ENSEMBLE_DEFINITION__ROLES:
				getRoles().clear();
				getRoles().addAll((Collection<? extends RoleDefinition>)newValue);
				return;
			case EdlPackage.ENSEMBLE_DEFINITION__ALIASES:
				getAliases().clear();
				getAliases().addAll((Collection<? extends AliasDefinition>)newValue);
				return;
			case EdlPackage.ENSEMBLE_DEFINITION__CONSTRAINTS:
				getConstraints().clear();
				getConstraints().addAll((Collection<? extends EquitableQuery>)newValue);
				return;
			case EdlPackage.ENSEMBLE_DEFINITION__ID:
				setId((IdDefinition)newValue);
				return;
			case EdlPackage.ENSEMBLE_DEFINITION__PARENT_ENSEMBLE:
				setParentEnsemble((String)newValue);
				return;
			case EdlPackage.ENSEMBLE_DEFINITION__CHILD_ENSEMBLES:
				getChildEnsembles().clear();
				getChildEnsembles().addAll((Collection<? extends ChildDefinition>)newValue);
				return;
			case EdlPackage.ENSEMBLE_DEFINITION__EXCHANGE_RULES:
				getExchangeRules().clear();
				getExchangeRules().addAll((Collection<? extends ExchangeRule>)newValue);
				return;
			case EdlPackage.ENSEMBLE_DEFINITION__FITNESS:
				setFitness((Query)newValue);
				return;
			case EdlPackage.ENSEMBLE_DEFINITION__EXTERNAL_KNOWLEDGE_EXCHANGE:
				setExternalKnowledgeExchange((Boolean)newValue);
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
			case EdlPackage.ENSEMBLE_DEFINITION__NAME:
				setName(NAME_EDEFAULT);
				return;
			case EdlPackage.ENSEMBLE_DEFINITION__ROLES:
				getRoles().clear();
				return;
			case EdlPackage.ENSEMBLE_DEFINITION__ALIASES:
				getAliases().clear();
				return;
			case EdlPackage.ENSEMBLE_DEFINITION__CONSTRAINTS:
				getConstraints().clear();
				return;
			case EdlPackage.ENSEMBLE_DEFINITION__ID:
				setId((IdDefinition)null);
				return;
			case EdlPackage.ENSEMBLE_DEFINITION__PARENT_ENSEMBLE:
				setParentEnsemble(PARENT_ENSEMBLE_EDEFAULT);
				return;
			case EdlPackage.ENSEMBLE_DEFINITION__CHILD_ENSEMBLES:
				getChildEnsembles().clear();
				return;
			case EdlPackage.ENSEMBLE_DEFINITION__EXCHANGE_RULES:
				getExchangeRules().clear();
				return;
			case EdlPackage.ENSEMBLE_DEFINITION__FITNESS:
				setFitness((Query)null);
				return;
			case EdlPackage.ENSEMBLE_DEFINITION__EXTERNAL_KNOWLEDGE_EXCHANGE:
				setExternalKnowledgeExchange(EXTERNAL_KNOWLEDGE_EXCHANGE_EDEFAULT);
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
			case EdlPackage.ENSEMBLE_DEFINITION__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case EdlPackage.ENSEMBLE_DEFINITION__ROLES:
				return roles != null && !roles.isEmpty();
			case EdlPackage.ENSEMBLE_DEFINITION__ALIASES:
				return aliases != null && !aliases.isEmpty();
			case EdlPackage.ENSEMBLE_DEFINITION__CONSTRAINTS:
				return constraints != null && !constraints.isEmpty();
			case EdlPackage.ENSEMBLE_DEFINITION__ID:
				return id != null;
			case EdlPackage.ENSEMBLE_DEFINITION__PARENT_ENSEMBLE:
				return PARENT_ENSEMBLE_EDEFAULT == null ? parentEnsemble != null : !PARENT_ENSEMBLE_EDEFAULT.equals(parentEnsemble);
			case EdlPackage.ENSEMBLE_DEFINITION__CHILD_ENSEMBLES:
				return childEnsembles != null && !childEnsembles.isEmpty();
			case EdlPackage.ENSEMBLE_DEFINITION__EXCHANGE_RULES:
				return exchangeRules != null && !exchangeRules.isEmpty();
			case EdlPackage.ENSEMBLE_DEFINITION__FITNESS:
				return fitness != null;
			case EdlPackage.ENSEMBLE_DEFINITION__EXTERNAL_KNOWLEDGE_EXCHANGE:
				return externalKnowledgeExchange != EXTERNAL_KNOWLEDGE_EXCHANGE_EDEFAULT;
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
		result.append(", parentEnsemble: ");
		result.append(parentEnsemble);
		result.append(", externalKnowledgeExchange: ");
		result.append(externalKnowledgeExchange);
		result.append(')');
		return result.toString();
	}

} //EnsembleDefinitionImpl
