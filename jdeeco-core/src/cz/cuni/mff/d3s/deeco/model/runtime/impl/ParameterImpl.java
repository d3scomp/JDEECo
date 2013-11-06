/**
 */
package cz.cuni.mff.d3s.deeco.model.runtime.impl;

import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Parameter;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ParameterDirection;

import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Parameter</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.ParameterImpl#getDirection <em>Direction</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.ParameterImpl#getKnowledgePath <em>Knowledge Path</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ParameterImpl extends MinimalEObjectImpl.Container implements Parameter {
        /**
         * The default value of the '{@link #getDirection() <em>Direction</em>}' attribute.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see #getDirection()
         * @generated
         * @ordered
         */
        protected static final ParameterDirection DIRECTION_EDEFAULT = ParameterDirection.IN;

        /**
         * The cached value of the '{@link #getDirection() <em>Direction</em>}' attribute.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see #getDirection()
         * @generated
         * @ordered
         */
        protected ParameterDirection direction = DIRECTION_EDEFAULT;

        /**
         * This is true if the Direction attribute has been set.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         * @ordered
         */
        protected boolean directionESet;

        /**
         * The cached value of the '{@link #getKnowledgePath() <em>Knowledge Path</em>}' containment reference.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see #getKnowledgePath()
         * @generated
         * @ordered
         */
        protected KnowledgePath knowledgePath;

        /**
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        protected ParameterImpl() {
                super();
        }

        /**
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        @Override
        protected EClass eStaticClass() {
                return RuntimeMetadataPackage.Literals.PARAMETER;
        }

        /**
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        public ParameterDirection getDirection() {
                return direction;
        }

        /**
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        public void setDirection(ParameterDirection newDirection) {
                ParameterDirection oldDirection = direction;
                direction = newDirection == null ? DIRECTION_EDEFAULT : newDirection;
                boolean oldDirectionESet = directionESet;
                directionESet = true;
                if (eNotificationRequired())
                        eNotify(new ENotificationImpl(this, Notification.SET, RuntimeMetadataPackage.PARAMETER__DIRECTION, oldDirection, direction, !oldDirectionESet));
        }

        /**
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        public void unsetDirection() {
                ParameterDirection oldDirection = direction;
                boolean oldDirectionESet = directionESet;
                direction = DIRECTION_EDEFAULT;
                directionESet = false;
                if (eNotificationRequired())
                        eNotify(new ENotificationImpl(this, Notification.UNSET, RuntimeMetadataPackage.PARAMETER__DIRECTION, oldDirection, DIRECTION_EDEFAULT, oldDirectionESet));
        }

        /**
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        public boolean isSetDirection() {
                return directionESet;
        }

        /**
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        public KnowledgePath getKnowledgePath() {
                return knowledgePath;
        }

        /**
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        public NotificationChain basicSetKnowledgePath(KnowledgePath newKnowledgePath, NotificationChain msgs) {
                KnowledgePath oldKnowledgePath = knowledgePath;
                knowledgePath = newKnowledgePath;
                if (eNotificationRequired()) {
                        ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, RuntimeMetadataPackage.PARAMETER__KNOWLEDGE_PATH, oldKnowledgePath, newKnowledgePath);
                        if (msgs == null) msgs = notification; else msgs.add(notification);
                }
                return msgs;
        }

        /**
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        public void setKnowledgePath(KnowledgePath newKnowledgePath) {
                if (newKnowledgePath != knowledgePath) {
                        NotificationChain msgs = null;
                        if (knowledgePath != null)
                                msgs = ((InternalEObject)knowledgePath).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - RuntimeMetadataPackage.PARAMETER__KNOWLEDGE_PATH, null, msgs);
                        if (newKnowledgePath != null)
                                msgs = ((InternalEObject)newKnowledgePath).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - RuntimeMetadataPackage.PARAMETER__KNOWLEDGE_PATH, null, msgs);
                        msgs = basicSetKnowledgePath(newKnowledgePath, msgs);
                        if (msgs != null) msgs.dispatch();
                }
                else if (eNotificationRequired())
                        eNotify(new ENotificationImpl(this, Notification.SET, RuntimeMetadataPackage.PARAMETER__KNOWLEDGE_PATH, newKnowledgePath, newKnowledgePath));
        }

        /**
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        @Override
        public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
                switch (featureID) {
                        case RuntimeMetadataPackage.PARAMETER__KNOWLEDGE_PATH:
                                return basicSetKnowledgePath(null, msgs);
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
                        case RuntimeMetadataPackage.PARAMETER__DIRECTION:
                                return getDirection();
                        case RuntimeMetadataPackage.PARAMETER__KNOWLEDGE_PATH:
                                return getKnowledgePath();
                }
                return super.eGet(featureID, resolve, coreType);
        }

        /**
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        @Override
        public void eSet(int featureID, Object newValue) {
                switch (featureID) {
                        case RuntimeMetadataPackage.PARAMETER__DIRECTION:
                                setDirection((ParameterDirection)newValue);
                                return;
                        case RuntimeMetadataPackage.PARAMETER__KNOWLEDGE_PATH:
                                setKnowledgePath((KnowledgePath)newValue);
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
                        case RuntimeMetadataPackage.PARAMETER__DIRECTION:
                                unsetDirection();
                                return;
                        case RuntimeMetadataPackage.PARAMETER__KNOWLEDGE_PATH:
                                setKnowledgePath((KnowledgePath)null);
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
                        case RuntimeMetadataPackage.PARAMETER__DIRECTION:
                                return isSetDirection();
                        case RuntimeMetadataPackage.PARAMETER__KNOWLEDGE_PATH:
                                return knowledgePath != null;
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
                result.append(" (direction: ");
                if (directionESet) result.append(direction); else result.append("<unset>");
                result.append(')');
                return result.toString();
        }

} //ParameterImpl