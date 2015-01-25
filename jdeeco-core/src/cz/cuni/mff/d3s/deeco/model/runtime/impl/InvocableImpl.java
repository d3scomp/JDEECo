/**
 */
package cz.cuni.mff.d3s.deeco.model.runtime.impl;

import cz.cuni.mff.d3s.deeco.model.runtime.api.Invocable;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Parameter;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage;
import java.lang.reflect.Method;
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
 * An implementation of the model object '<em><b>Invocable</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.InvocableImpl#getParameters <em>Parameters</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.InvocableImpl#getMethod <em>Method</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.InvocableImpl#isIgnoreKnowledgeCompromise <em>Ignore Knowledge Compromise</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class InvocableImpl extends MinimalEObjectImpl.Container implements Invocable {
	/**
	 * The cached value of the '{@link #getParameters() <em>Parameters</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParameters()
	 * @generated
	 * @ordered
	 */
	protected EList<Parameter> parameters;

	/**
	 * The default value of the '{@link #getMethod() <em>Method</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMethod()
	 * @generated
	 * @ordered
	 */
	protected static final Method METHOD_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getMethod() <em>Method</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMethod()
	 * @generated
	 * @ordered
	 */
	protected Method method = METHOD_EDEFAULT;

	/**
	 * The default value of the '{@link #isIgnoreKnowledgeCompromise() <em>Ignore Knowledge Compromise</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIgnoreKnowledgeCompromise()
	 * @generated
	 * @ordered
	 */
	protected static final boolean IGNORE_KNOWLEDGE_COMPROMISE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isIgnoreKnowledgeCompromise() <em>Ignore Knowledge Compromise</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIgnoreKnowledgeCompromise()
	 * @generated
	 * @ordered
	 */
	protected boolean ignoreKnowledgeCompromise = IGNORE_KNOWLEDGE_COMPROMISE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected InvocableImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return RuntimeMetadataPackage.Literals.INVOCABLE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Parameter> getParameters() {
		if (parameters == null) {
			parameters = new EObjectContainmentEList<Parameter>(Parameter.class, this, RuntimeMetadataPackage.INVOCABLE__PARAMETERS);
		}
		return parameters;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Method getMethod() {
		return method;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMethod(Method newMethod) {
		Method oldMethod = method;
		method = newMethod;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RuntimeMetadataPackage.INVOCABLE__METHOD, oldMethod, method));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isIgnoreKnowledgeCompromise() {
		return ignoreKnowledgeCompromise;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIgnoreKnowledgeCompromise(boolean newIgnoreKnowledgeCompromise) {
		boolean oldIgnoreKnowledgeCompromise = ignoreKnowledgeCompromise;
		ignoreKnowledgeCompromise = newIgnoreKnowledgeCompromise;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RuntimeMetadataPackage.INVOCABLE__IGNORE_KNOWLEDGE_COMPROMISE, oldIgnoreKnowledgeCompromise, ignoreKnowledgeCompromise));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case RuntimeMetadataPackage.INVOCABLE__PARAMETERS:
				return ((InternalEList<?>)getParameters()).basicRemove(otherEnd, msgs);
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
			case RuntimeMetadataPackage.INVOCABLE__PARAMETERS:
				return getParameters();
			case RuntimeMetadataPackage.INVOCABLE__METHOD:
				return getMethod();
			case RuntimeMetadataPackage.INVOCABLE__IGNORE_KNOWLEDGE_COMPROMISE:
				return isIgnoreKnowledgeCompromise();
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
			case RuntimeMetadataPackage.INVOCABLE__PARAMETERS:
				getParameters().clear();
				getParameters().addAll((Collection<? extends Parameter>)newValue);
				return;
			case RuntimeMetadataPackage.INVOCABLE__METHOD:
				setMethod((Method)newValue);
				return;
			case RuntimeMetadataPackage.INVOCABLE__IGNORE_KNOWLEDGE_COMPROMISE:
				setIgnoreKnowledgeCompromise((Boolean)newValue);
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
			case RuntimeMetadataPackage.INVOCABLE__PARAMETERS:
				getParameters().clear();
				return;
			case RuntimeMetadataPackage.INVOCABLE__METHOD:
				setMethod(METHOD_EDEFAULT);
				return;
			case RuntimeMetadataPackage.INVOCABLE__IGNORE_KNOWLEDGE_COMPROMISE:
				setIgnoreKnowledgeCompromise(IGNORE_KNOWLEDGE_COMPROMISE_EDEFAULT);
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
			case RuntimeMetadataPackage.INVOCABLE__PARAMETERS:
				return parameters != null && !parameters.isEmpty();
			case RuntimeMetadataPackage.INVOCABLE__METHOD:
				return METHOD_EDEFAULT == null ? method != null : !METHOD_EDEFAULT.equals(method);
			case RuntimeMetadataPackage.INVOCABLE__IGNORE_KNOWLEDGE_COMPROMISE:
				return ignoreKnowledgeCompromise != IGNORE_KNOWLEDGE_COMPROMISE_EDEFAULT;
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
		result.append(" (method: ");
		result.append(method);
		result.append(", ignoreKnowledgeCompromise: ");
		result.append(ignoreKnowledgeCompromise);
		result.append(')');
		return result.toString();
	}

} //InvocableImpl
