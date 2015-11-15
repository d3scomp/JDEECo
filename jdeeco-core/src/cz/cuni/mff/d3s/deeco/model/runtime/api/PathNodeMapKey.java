/**
 */
package cz.cuni.mff.d3s.deeco.model.runtime.api;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Path Node Map Key</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeMapKey#getKeyPath <em>Key Path</em>}</li>
 * </ul>
 *
 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getPathNodeMapKey()
 * @model
 * @generated
 */
public interface PathNodeMapKey extends PathNode {
	/**
	 * Returns the value of the '<em><b>Key Path</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Key Path</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Key Path</em>' containment reference.
	 * @see #setKeyPath(KnowledgePath)
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getPathNodeMapKey_KeyPath()
	 * @model containment="true" required="true"
	 * @generated
	 */
	KnowledgePath getKeyPath();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeMapKey#getKeyPath <em>Key Path</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Key Path</em>' containment reference.
	 * @see #getKeyPath()
	 * @generated
	 */
	void setKeyPath(KnowledgePath value);

} // PathNodeMapKey
