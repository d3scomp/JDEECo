package cz.cuni.mff.d3s.jdeeco.adaptation.correlation.metadata;

import java.io.Serializable;

/**
 * The {@link KnowledgeQuadruple} object holds four knowledge values.
 * It is designed to hold values for two components and two of theirs
 * knowledge field values.
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 */
public class KnowledgeQuadruple implements Serializable {
	/**
	 * Generated serial version UID.
	 */
	private static final long serialVersionUID = 6780029457467263020L;
	
	/**
	 * The time slot into which the contained knowledge values belong.
	 */
	public final long timeSlot;
	/**
	 * The value of the one of the knowledge fields in component 1.
	 */
	public final CorrelationMetadataWrapper<? extends Object> c1Value1;
	/**
	 * The value of the other of the knowledge field in component 1.
	 */
	public final CorrelationMetadataWrapper<? extends Object> c1Value2;
	/**
	 * The value of the one of the knowledge fields in component 2.
	 */
	public final CorrelationMetadataWrapper<? extends Object> c2Value1;
	/**
	 * The value of the other of the knowledge field in component 2.
	 */
	public final CorrelationMetadataWrapper<? extends Object> c2Value2;
	
	/**
	 * Creates a new instance of {@link KnowledgeQuadruple} with the provided
	 * values.
	 * @param c1Value1 The value of the one of the knowledge fields in component 1.
	 * @param c1Value2 The value of the other of the knowledge field in component 1.
	 * @param c2Value1 The value of the one of the knowledge fields in component 2.
	 * @param c2Value2 The value of the other of the knowledge field in component 2.
	 * @param timeSlot The time slot into which the contained knowledge values belong.
	 */
	public KnowledgeQuadruple(CorrelationMetadataWrapper<? extends Object> c1Value1,
							  CorrelationMetadataWrapper<? extends Object> c1Value2,
							  CorrelationMetadataWrapper<? extends Object> c2Value1,
							  CorrelationMetadataWrapper<? extends Object> c2Value2,
							  long timeSlot){
		this.timeSlot = timeSlot;
		this.c1Value1 = c1Value1;
		this.c1Value2 = c1Value2;
		this.c2Value1 = c2Value1;
		this.c2Value2 = c2Value2;
	}
}
