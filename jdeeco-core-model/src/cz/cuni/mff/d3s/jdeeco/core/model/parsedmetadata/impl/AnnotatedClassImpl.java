/**
 */
package cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.impl;

import cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.AnnotatedClass;
import cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.AnnotatedEntity;
import cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.AnnotatedField;
import cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.AnnotatedMethod;
import cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.Annotation;
import cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.ParsedmodelPackage;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Annotated Class</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.impl.AnnotatedClassImpl#getAnnotations <em>Annotations</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.impl.AnnotatedClassImpl#getMethods <em>Methods</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.impl.AnnotatedClassImpl#getClass_ <em>Class</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.impl.AnnotatedClassImpl#getFields <em>Fields</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class AnnotatedClassImpl extends NamedEntityImpl implements AnnotatedClass {
	/**
	 * The cached value of the '{@link #getAnnotations() <em>Annotations</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAnnotations()
	 * @generated
	 * @ordered
	 */
	protected EList<Annotation> annotations;

	/**
	 * The cached value of the '{@link #getMethods() <em>Methods</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMethods()
	 * @generated
	 * @ordered
	 */
	protected EList<AnnotatedMethod> methods;

	/**
	 * The cached value of the '{@link #getClass_() <em>Class</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getClass_()
	 * @generated
	 * @ordered
	 */
	protected Class class_;

	/**
	 * The cached value of the '{@link #getFields() <em>Fields</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFields()
	 * @generated
	 * @ordered
	 */
	protected EList<AnnotatedField> fields;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AnnotatedClassImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ParsedmodelPackage.Literals.ANNOTATED_CLASS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Annotation> getAnnotations() {
		if (annotations == null) {
			annotations = new EObjectContainmentEList<Annotation>(Annotation.class, this, ParsedmodelPackage.ANNOTATED_CLASS__ANNOTATIONS);
		}
		return annotations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<AnnotatedMethod> getMethods() {
		if (methods == null) {
			methods = new EObjectContainmentEList<AnnotatedMethod>(AnnotatedMethod.class, this, ParsedmodelPackage.ANNOTATED_CLASS__METHODS);
		}
		return methods;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Class getClass_() {
		return class_;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setClass(Class newClass) {
		Class oldClass = class_;
		class_ = newClass;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ParsedmodelPackage.ANNOTATED_CLASS__CLASS, oldClass, class_));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<AnnotatedField> getFields() {
		if (fields == null) {
			fields = new EObjectContainmentEList<AnnotatedField>(AnnotatedField.class, this, ParsedmodelPackage.ANNOTATED_CLASS__FIELDS);
		}
		return fields;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Annotation findAnnotationByJavaClass(Class class_) {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ParsedmodelPackage.ANNOTATED_CLASS__ANNOTATIONS:
				return ((InternalEList<?>)getAnnotations()).basicRemove(otherEnd, msgs);
			case ParsedmodelPackage.ANNOTATED_CLASS__METHODS:
				return ((InternalEList<?>)getMethods()).basicRemove(otherEnd, msgs);
			case ParsedmodelPackage.ANNOTATED_CLASS__FIELDS:
				return ((InternalEList<?>)getFields()).basicRemove(otherEnd, msgs);
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
			case ParsedmodelPackage.ANNOTATED_CLASS__ANNOTATIONS:
				return getAnnotations();
			case ParsedmodelPackage.ANNOTATED_CLASS__METHODS:
				return getMethods();
			case ParsedmodelPackage.ANNOTATED_CLASS__CLASS:
				return getClass_();
			case ParsedmodelPackage.ANNOTATED_CLASS__FIELDS:
				return getFields();
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
			case ParsedmodelPackage.ANNOTATED_CLASS__ANNOTATIONS:
				getAnnotations().clear();
				getAnnotations().addAll((Collection<? extends Annotation>)newValue);
				return;
			case ParsedmodelPackage.ANNOTATED_CLASS__METHODS:
				getMethods().clear();
				getMethods().addAll((Collection<? extends AnnotatedMethod>)newValue);
				return;
			case ParsedmodelPackage.ANNOTATED_CLASS__CLASS:
				setClass((Class)newValue);
				return;
			case ParsedmodelPackage.ANNOTATED_CLASS__FIELDS:
				getFields().clear();
				getFields().addAll((Collection<? extends AnnotatedField>)newValue);
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
			case ParsedmodelPackage.ANNOTATED_CLASS__ANNOTATIONS:
				getAnnotations().clear();
				return;
			case ParsedmodelPackage.ANNOTATED_CLASS__METHODS:
				getMethods().clear();
				return;
			case ParsedmodelPackage.ANNOTATED_CLASS__CLASS:
				setClass((Class)null);
				return;
			case ParsedmodelPackage.ANNOTATED_CLASS__FIELDS:
				getFields().clear();
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
			case ParsedmodelPackage.ANNOTATED_CLASS__ANNOTATIONS:
				return annotations != null && !annotations.isEmpty();
			case ParsedmodelPackage.ANNOTATED_CLASS__METHODS:
				return methods != null && !methods.isEmpty();
			case ParsedmodelPackage.ANNOTATED_CLASS__CLASS:
				return class_ != null;
			case ParsedmodelPackage.ANNOTATED_CLASS__FIELDS:
				return fields != null && !fields.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
		if (baseClass == AnnotatedEntity.class) {
			switch (derivedFeatureID) {
				case ParsedmodelPackage.ANNOTATED_CLASS__ANNOTATIONS: return ParsedmodelPackage.ANNOTATED_ENTITY__ANNOTATIONS;
				default: return -1;
			}
		}
		return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
		if (baseClass == AnnotatedEntity.class) {
			switch (baseFeatureID) {
				case ParsedmodelPackage.ANNOTATED_ENTITY__ANNOTATIONS: return ParsedmodelPackage.ANNOTATED_CLASS__ANNOTATIONS;
				default: return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
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
		result.append(" (class: ");
		result.append(class_);
		result.append(')');
		return result.toString();
	}

} //AnnotatedClassImpl
