/**
 */
package cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl;

import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.MethodParameter;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.ParameterizedMethod;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.RuntimemodelPackage;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Parameterized Method</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.ParameterizedMethodImpl#getDeclaringClass <em>Declaring Class</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.ParameterizedMethodImpl#getMethodName <em>Method Name</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.ParameterizedMethodImpl#getFormalParameters <em>Formal Parameters</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.ParameterizedMethodImpl#getInParameters <em>In Parameters</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.ParameterizedMethodImpl#getOutParameters <em>Out Parameters</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.ParameterizedMethodImpl#getInOutParameters <em>In Out Parameters</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class ParameterizedMethodImpl extends EObjectImpl implements ParameterizedMethod {
	/**
	 * The cached value of the '{@link #getDeclaringClass() <em>Declaring Class</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDeclaringClass()
	 * @generated
	 * @ordered
	 */
	protected Class declaringClass;

	/**
	 * The default value of the '{@link #getMethodName() <em>Method Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMethodName()
	 * @generated
	 * @ordered
	 */
	protected static final String METHOD_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getMethodName() <em>Method Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMethodName()
	 * @generated
	 * @ordered
	 */
	protected String methodName = METHOD_NAME_EDEFAULT;

	/**
	 * The cached value of the '{@link #getFormalParameters() <em>Formal Parameters</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFormalParameters()
	 * @generated
	 * @ordered
	 */
	protected EList<MethodParameter> formalParameters;

	/**
	 * The cached value of the '{@link #getInParameters() <em>In Parameters</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInParameters()
	 * @generated
	 * @ordered
	 */
	protected EList<MethodParameter> inParameters;

	/**
	 * The cached value of the '{@link #getOutParameters() <em>Out Parameters</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOutParameters()
	 * @generated
	 * @ordered
	 */
	protected EList<MethodParameter> outParameters;

	/**
	 * The cached value of the '{@link #getInOutParameters() <em>In Out Parameters</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInOutParameters()
	 * @generated
	 * @ordered
	 */
	protected EList<MethodParameter> inOutParameters;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ParameterizedMethodImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return RuntimemodelPackage.Literals.PARAMETERIZED_METHOD;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Class getDeclaringClass() {
		return declaringClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDeclaringClass(Class newDeclaringClass) {
		Class oldDeclaringClass = declaringClass;
		declaringClass = newDeclaringClass;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RuntimemodelPackage.PARAMETERIZED_METHOD__DECLARING_CLASS, oldDeclaringClass, declaringClass));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getMethodName() {
		return methodName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMethodName(String newMethodName) {
		String oldMethodName = methodName;
		methodName = newMethodName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RuntimemodelPackage.PARAMETERIZED_METHOD__METHOD_NAME, oldMethodName, methodName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<MethodParameter> getFormalParameters() {
		if (formalParameters == null) {
			formalParameters = new EObjectContainmentWithInverseEList<MethodParameter>(MethodParameter.class, this, RuntimemodelPackage.PARAMETERIZED_METHOD__FORMAL_PARAMETERS, RuntimemodelPackage.METHOD_PARAMETER__OWNER);
		}
		return formalParameters;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<MethodParameter> getInParameters() {
		if (inParameters == null) {
			inParameters = new EObjectResolvingEList<MethodParameter>(MethodParameter.class, this, RuntimemodelPackage.PARAMETERIZED_METHOD__IN_PARAMETERS);
		}
		return inParameters;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<MethodParameter> getOutParameters() {
		if (outParameters == null) {
			outParameters = new EObjectResolvingEList<MethodParameter>(MethodParameter.class, this, RuntimemodelPackage.PARAMETERIZED_METHOD__OUT_PARAMETERS);
		}
		return outParameters;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<MethodParameter> getInOutParameters() {
		if (inOutParameters == null) {
			inOutParameters = new EObjectResolvingEList<MethodParameter>(MethodParameter.class, this, RuntimemodelPackage.PARAMETERIZED_METHOD__IN_OUT_PARAMETERS);
		}
		return inOutParameters;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object invoke() {
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
			case RuntimemodelPackage.PARAMETERIZED_METHOD__FORMAL_PARAMETERS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getFormalParameters()).basicAdd(otherEnd, msgs);
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
			case RuntimemodelPackage.PARAMETERIZED_METHOD__FORMAL_PARAMETERS:
				return ((InternalEList<?>)getFormalParameters()).basicRemove(otherEnd, msgs);
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
			case RuntimemodelPackage.PARAMETERIZED_METHOD__DECLARING_CLASS:
				return getDeclaringClass();
			case RuntimemodelPackage.PARAMETERIZED_METHOD__METHOD_NAME:
				return getMethodName();
			case RuntimemodelPackage.PARAMETERIZED_METHOD__FORMAL_PARAMETERS:
				return getFormalParameters();
			case RuntimemodelPackage.PARAMETERIZED_METHOD__IN_PARAMETERS:
				return getInParameters();
			case RuntimemodelPackage.PARAMETERIZED_METHOD__OUT_PARAMETERS:
				return getOutParameters();
			case RuntimemodelPackage.PARAMETERIZED_METHOD__IN_OUT_PARAMETERS:
				return getInOutParameters();
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
			case RuntimemodelPackage.PARAMETERIZED_METHOD__DECLARING_CLASS:
				setDeclaringClass((Class)newValue);
				return;
			case RuntimemodelPackage.PARAMETERIZED_METHOD__METHOD_NAME:
				setMethodName((String)newValue);
				return;
			case RuntimemodelPackage.PARAMETERIZED_METHOD__FORMAL_PARAMETERS:
				getFormalParameters().clear();
				getFormalParameters().addAll((Collection<? extends MethodParameter>)newValue);
				return;
			case RuntimemodelPackage.PARAMETERIZED_METHOD__IN_PARAMETERS:
				getInParameters().clear();
				getInParameters().addAll((Collection<? extends MethodParameter>)newValue);
				return;
			case RuntimemodelPackage.PARAMETERIZED_METHOD__OUT_PARAMETERS:
				getOutParameters().clear();
				getOutParameters().addAll((Collection<? extends MethodParameter>)newValue);
				return;
			case RuntimemodelPackage.PARAMETERIZED_METHOD__IN_OUT_PARAMETERS:
				getInOutParameters().clear();
				getInOutParameters().addAll((Collection<? extends MethodParameter>)newValue);
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
			case RuntimemodelPackage.PARAMETERIZED_METHOD__DECLARING_CLASS:
				setDeclaringClass((Class)null);
				return;
			case RuntimemodelPackage.PARAMETERIZED_METHOD__METHOD_NAME:
				setMethodName(METHOD_NAME_EDEFAULT);
				return;
			case RuntimemodelPackage.PARAMETERIZED_METHOD__FORMAL_PARAMETERS:
				getFormalParameters().clear();
				return;
			case RuntimemodelPackage.PARAMETERIZED_METHOD__IN_PARAMETERS:
				getInParameters().clear();
				return;
			case RuntimemodelPackage.PARAMETERIZED_METHOD__OUT_PARAMETERS:
				getOutParameters().clear();
				return;
			case RuntimemodelPackage.PARAMETERIZED_METHOD__IN_OUT_PARAMETERS:
				getInOutParameters().clear();
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
			case RuntimemodelPackage.PARAMETERIZED_METHOD__DECLARING_CLASS:
				return declaringClass != null;
			case RuntimemodelPackage.PARAMETERIZED_METHOD__METHOD_NAME:
				return METHOD_NAME_EDEFAULT == null ? methodName != null : !METHOD_NAME_EDEFAULT.equals(methodName);
			case RuntimemodelPackage.PARAMETERIZED_METHOD__FORMAL_PARAMETERS:
				return formalParameters != null && !formalParameters.isEmpty();
			case RuntimemodelPackage.PARAMETERIZED_METHOD__IN_PARAMETERS:
				return inParameters != null && !inParameters.isEmpty();
			case RuntimemodelPackage.PARAMETERIZED_METHOD__OUT_PARAMETERS:
				return outParameters != null && !outParameters.isEmpty();
			case RuntimemodelPackage.PARAMETERIZED_METHOD__IN_OUT_PARAMETERS:
				return inOutParameters != null && !inOutParameters.isEmpty();
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
		result.append(" (declaringClass: ");
		result.append(declaringClass);
		result.append(", methodName: ");
		result.append(methodName);
		result.append(')');
		return result.toString();
	}

} //ParameterizedMethodImpl
