/**
 */
package cz.cuni.mff.d3s.deeco.model.runtime.api;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Comparison Type</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getComparisonType()
 * @model
 * @generated
 */
public enum ComparisonType implements Enumerator {
	/**
	 * The '<em><b>EQUAL</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #EQUAL_VALUE
	 * @generated
	 * @ordered
	 */
	EQUAL(0, "EQUAL", "EQUAL"),

	/**
	 * The '<em><b>EQUAL LESS THAN</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #EQUAL_LESS_THAN_VALUE
	 * @generated
	 * @ordered
	 */
	EQUAL_LESS_THAN(1, "EQUAL_LESS_THAN", "EQUAL_LESS_THAN"),

	/**
	 * The '<em><b>EQUAL MORE THAN</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #EQUAL_MORE_THAN_VALUE
	 * @generated
	 * @ordered
	 */
	EQUAL_MORE_THAN(2, "EQUAL_MORE_THAN", "EQUAL_MORE_THAN"),

	/**
	 * The '<em><b>LESS THAN</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #LESS_THAN_VALUE
	 * @generated
	 * @ordered
	 */
	LESS_THAN(3, "LESS_THAN", "LESS_THAN"),

	/**
	 * The '<em><b>MORE THAN</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #MORE_THAN_VALUE
	 * @generated
	 * @ordered
	 */
	MORE_THAN(4, "MORE_THAN", "MORE_THAN");

	/**
	 * The '<em><b>EQUAL</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>EQUAL</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #EQUAL
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int EQUAL_VALUE = 0;

	/**
	 * The '<em><b>EQUAL LESS THAN</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>EQUAL LESS THAN</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #EQUAL_LESS_THAN
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int EQUAL_LESS_THAN_VALUE = 1;

	/**
	 * The '<em><b>EQUAL MORE THAN</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>EQUAL MORE THAN</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #EQUAL_MORE_THAN
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int EQUAL_MORE_THAN_VALUE = 2;

	/**
	 * The '<em><b>LESS THAN</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>LESS THAN</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #LESS_THAN
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int LESS_THAN_VALUE = 3;

	/**
	 * The '<em><b>MORE THAN</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>MORE THAN</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #MORE_THAN
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int MORE_THAN_VALUE = 4;

	/**
	 * An array of all the '<em><b>Comparison Type</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final ComparisonType[] VALUES_ARRAY =
		new ComparisonType[] {
			EQUAL,
			EQUAL_LESS_THAN,
			EQUAL_MORE_THAN,
			LESS_THAN,
			MORE_THAN,
		};

	/**
	 * A public read-only list of all the '<em><b>Comparison Type</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List<ComparisonType> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Comparison Type</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static ComparisonType get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			ComparisonType result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Comparison Type</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static ComparisonType getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			ComparisonType result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Comparison Type</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static ComparisonType get(int value) {
		switch (value) {
			case EQUAL_VALUE: return EQUAL;
			case EQUAL_LESS_THAN_VALUE: return EQUAL_LESS_THAN;
			case EQUAL_MORE_THAN_VALUE: return EQUAL_MORE_THAN;
			case LESS_THAN_VALUE: return LESS_THAN;
			case MORE_THAN_VALUE: return MORE_THAN;
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
	private ComparisonType(int value, String name, String literal) {
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
	
} //ComparisonType
