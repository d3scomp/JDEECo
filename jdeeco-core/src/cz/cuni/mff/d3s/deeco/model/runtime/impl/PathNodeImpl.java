/**
 */
package cz.cuni.mff.d3s.deeco.model.runtime.impl;

import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNode;

import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Path Node</b></em>'.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public abstract class PathNodeImpl extends MinimalEObjectImpl.Container implements PathNode {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PathNodeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return RuntimeMetadataPackage.Literals.PATH_NODE;
	}

} //PathNodeImpl
