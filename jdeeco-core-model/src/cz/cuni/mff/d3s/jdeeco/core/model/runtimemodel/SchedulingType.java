/**
 */
package cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Scheduling Type</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.RuntimemodelPackage#getSchedulingType()
 * @model
 * @generated
 */
public enum SchedulingType implements Enumerator {
	/**
	 * The '<em><b>PERIODIC</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #PERIODIC_VALUE
	 * @generated
	 * @ordered
	 */
	PERIODIC(0, "PERIODIC", "PERIODIC"),

	/**
	 * The '<em><b>TRIGGERED</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #TRIGGERED_VALUE
	 * @generated
	 * @ordered
	 */
	TRIGGERED(1, "TRIGGERED", "TRIGGERED");

	/**
	 * The '<em><b>PERIODIC</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>PERIODIC</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #PERIODIC
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int PERIODIC_VALUE = 0;

	/**
	 * The '<em><b>TRIGGERED</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>TRIGGERED</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #TRIGGERED
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int TRIGGERED_VALUE = 1;

	/**
	 * An array of all the '<em><b>Scheduling Type</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final SchedulingType[] VALUES_ARRAY =
		new SchedulingType[] {
			PERIODIC,
			TRIGGERED,
		};

	/**
	 * A public read-only list of all the '<em><b>Scheduling Type</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List<SchedulingType> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Scheduling Type</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static SchedulingType get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			SchedulingType result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Scheduling Type</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static SchedulingType getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			SchedulingType result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Scheduling Type</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static SchedulingType get(int value) {
		switch (value) {
			case PERIODIC_VALUE: return PERIODIC;
			case TRIGGERED_VALUE: return TRIGGERED;
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
	private SchedulingType(int value, String name, String literal) {
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
	
} //SchedulingType
