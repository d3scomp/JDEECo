/**
 */
package cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Knowledge Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.KnowledgeType#isIsStructured <em>Is Structured</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.KnowledgeType#isIsWrapper <em>Is Wrapper</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.KnowledgeType#getClazz <em>Clazz</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.KnowledgeType#getOwner <em>Owner</em>}</li>
 * </ul>
 * </p>
 *
 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.RuntimemodelPackage#getKnowledgeType()
 * @model abstract="true"
 * @generated
 */
public interface KnowledgeType extends EObject {
	/**
	 * Returns the value of the '<em><b>Is Structured</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Is Structured</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Is Structured</em>' attribute.
	 * @see #setIsStructured(boolean)
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.RuntimemodelPackage#getKnowledgeType_IsStructured()
	 * @model required="true"
	 * @generated
	 */
	boolean isIsStructured();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.KnowledgeType#isIsStructured <em>Is Structured</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Is Structured</em>' attribute.
	 * @see #isIsStructured()
	 * @generated
	 */
	void setIsStructured(boolean value);

	/**
	 * Returns the value of the '<em><b>Is Wrapper</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Is Wrapper</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Is Wrapper</em>' attribute.
	 * @see #setIsWrapper(boolean)
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.RuntimemodelPackage#getKnowledgeType_IsWrapper()
	 * @model required="true"
	 * @generated
	 */
	boolean isIsWrapper();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.KnowledgeType#isIsWrapper <em>Is Wrapper</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Is Wrapper</em>' attribute.
	 * @see #isIsWrapper()
	 * @generated
	 */
	void setIsWrapper(boolean value);

	/**
	 * Returns the value of the '<em><b>Clazz</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Clazz</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Clazz</em>' attribute.
	 * @see #setClazz(Class)
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.RuntimemodelPackage#getKnowledgeType_Clazz()
	 * @model required="true"
	 * @generated
	 */
	Class getClazz();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.KnowledgeType#getClazz <em>Clazz</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Clazz</em>' attribute.
	 * @see #getClazz()
	 * @generated
	 */
	void setClazz(Class value);

	/**
	 * Returns the value of the '<em><b>Owner</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.KnowledgeTypeOwner#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Owner</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Owner</em>' container reference.
	 * @see #setOwner(KnowledgeTypeOwner)
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.RuntimemodelPackage#getKnowledgeType_Owner()
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.KnowledgeTypeOwner#getType
	 * @model opposite="type" required="true" transient="false"
	 * @generated
	 */
	KnowledgeTypeOwner getOwner();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.KnowledgeType#getOwner <em>Owner</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Owner</em>' container reference.
	 * @see #getOwner()
	 * @generated
	 */
	void setOwner(KnowledgeTypeOwner value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model required="true"
	 * @generated
	 */
	Object createFromRaw(Object rawValue);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model required="true"
	 * @generated
	 */
	Object storeToRaw(Object value);

} // KnowledgeType
