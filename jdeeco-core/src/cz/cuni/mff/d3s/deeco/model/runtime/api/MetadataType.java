/**
 */
package cz.cuni.mff.d3s.deeco.model.runtime.api;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Metadata Type</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getMetadataType()
 * @model
 * @generated
 */
public enum MetadataType implements Enumerator {
	/**
	 * The '<em><b>EMPTY</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #EMPTY_VALUE
	 * @generated
	 * @ordered
	 */
	EMPTY(0, "EMPTY", "EMPTY"),

	/**
	 * The '<em><b>TS</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #TS_VALUE
	 * @generated
	 * @ordered
	 */
	TS(1, "TS", "TS"),

	/**
	 * The '<em><b>MIN BOUNDARY</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #MIN_BOUNDARY_VALUE
	 * @generated
	 * @ordered
	 */
	MIN_BOUNDARY(2, "MIN_BOUNDARY", "MIN_BOUNDARY"),

	/**
	 * The '<em><b>MAX BOUNDARY</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #MAX_BOUNDARY_VALUE
	 * @generated
	 * @ordered
	 */
	MAX_BOUNDARY(3, "MAX_BOUNDARY", "MAX_BOUNDARY"),

	/**
	 * The '<em><b>INACCURACY</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #INACCURACY_VALUE
	 * @generated
	 * @ordered
	 */
	INACCURACY(4, "INACCURACY", "INACCURACY");

	/**
	 * The '<em><b>EMPTY</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>EMPTY</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #EMPTY
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int EMPTY_VALUE = 0;

	/**
	 * The '<em><b>TS</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>TS</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #TS
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int TS_VALUE = 1;

	/**
	 * The '<em><b>MIN BOUNDARY</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>MIN BOUNDARY</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #MIN_BOUNDARY
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int MIN_BOUNDARY_VALUE = 2;

	/**
	 * The '<em><b>MAX BOUNDARY</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>MAX BOUNDARY</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #MAX_BOUNDARY
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int MAX_BOUNDARY_VALUE = 3;

	/**
	 * The '<em><b>INACCURACY</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>INACCURACY</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #INACCURACY
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int INACCURACY_VALUE = 4;

	/**
	 * An array of all the '<em><b>Metadata Type</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final MetadataType[] VALUES_ARRAY =
		new MetadataType[] {
			EMPTY,
			TS,
			MIN_BOUNDARY,
			MAX_BOUNDARY,
			INACCURACY,
		};

	/**
	 * A public read-only list of all the '<em><b>Metadata Type</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List<MetadataType> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Metadata Type</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static MetadataType get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			MetadataType result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Metadata Type</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static MetadataType getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			MetadataType result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Metadata Type</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static MetadataType get(int value) {
		switch (value) {
			case EMPTY_VALUE: return EMPTY;
			case TS_VALUE: return TS;
			case MIN_BOUNDARY_VALUE: return MIN_BOUNDARY;
			case MAX_BOUNDARY_VALUE: return MAX_BOUNDARY;
			case INACCURACY_VALUE: return INACCURACY;
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private final int value;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private final String name;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private final String literal;

	/**
	 * Only this class can construct instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private MetadataType(int value, String name, String literal) {
		this.value = value;
		this.name = name;
		this.literal = literal;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getValue() {
	  return value;
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
	public String getLiteral() {
	  return literal;
	}

	/**
	 * Returns the literal value of the enumerator, which is its string representation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		return literal;
	}
	
} //MetadataType
