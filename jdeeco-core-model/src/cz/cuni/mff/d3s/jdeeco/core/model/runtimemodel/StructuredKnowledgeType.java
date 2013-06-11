/**
 */
package cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Structured Knowledge Type</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.RuntimemodelPackage#getStructuredKnowledgeType()
 * @model
 * @generated
 */
public enum StructuredKnowledgeType implements Enumerator {
	/**
	 * The '<em><b>KNOWLEDGE</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #KNOWLEDGE_VALUE
	 * @generated
	 * @ordered
	 */
	KNOWLEDGE(0, "KNOWLEDGE", "KNOWLEDGE"),

	/**
	 * The '<em><b>LIST</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #LIST_VALUE
	 * @generated
	 * @ordered
	 */
	LIST(1, "LIST", "LIST"),

	/**
	 * The '<em><b>MAP</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #MAP_VALUE
	 * @generated
	 * @ordered
	 */
	MAP(2, "MAP", "MAP"),

	/**
	 * The '<em><b>UNSTRUCTURED</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #UNSTRUCTURED_VALUE
	 * @generated
	 * @ordered
	 */
	UNSTRUCTURED(3, "UNSTRUCTURED", "UNSTRUCTURED");

	/**
	 * The '<em><b>KNOWLEDGE</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>KNOWLEDGE</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #KNOWLEDGE
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int KNOWLEDGE_VALUE = 0;

	/**
	 * The '<em><b>LIST</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>LIST</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #LIST
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int LIST_VALUE = 1;

	/**
	 * The '<em><b>MAP</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>MAP</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #MAP
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int MAP_VALUE = 2;

	/**
	 * The '<em><b>UNSTRUCTURED</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>UNSTRUCTURED</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #UNSTRUCTURED
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int UNSTRUCTURED_VALUE = 3;

	/**
	 * An array of all the '<em><b>Structured Knowledge Type</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final StructuredKnowledgeType[] VALUES_ARRAY =
		new StructuredKnowledgeType[] {
			KNOWLEDGE,
			LIST,
			MAP,
			UNSTRUCTURED,
		};

	/**
	 * A public read-only list of all the '<em><b>Structured Knowledge Type</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List<StructuredKnowledgeType> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Structured Knowledge Type</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static StructuredKnowledgeType get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			StructuredKnowledgeType result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Structured Knowledge Type</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static StructuredKnowledgeType getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			StructuredKnowledgeType result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Structured Knowledge Type</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static StructuredKnowledgeType get(int value) {
		switch (value) {
			case KNOWLEDGE_VALUE: return KNOWLEDGE;
			case LIST_VALUE: return LIST;
			case MAP_VALUE: return MAP;
			case UNSTRUCTURED_VALUE: return UNSTRUCTURED;
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
	private StructuredKnowledgeType(int value, String name, String literal) {
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
	
} //StructuredKnowledgeType
