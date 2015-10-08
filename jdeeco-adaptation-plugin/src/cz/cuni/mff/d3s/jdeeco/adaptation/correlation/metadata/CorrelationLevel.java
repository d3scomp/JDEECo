package cz.cuni.mff.d3s.jdeeco.adaptation.correlation.metadata;

import java.io.Serializable;


/**
 * This class encapsulates the correlation of knowledge identified by two labels.
 * The correlation captures the the dependency of the knowledge identified by the
 * secong label on the knowledge identified by the first label. The dependency is
 * following:
 * 	knowledge(firstLabel) -> knowledge(secondLabel)
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 */
public class CorrelationLevel implements Serializable {

	/**
	 * Generated serial version UID.
	 */
	private static final long serialVersionUID = 451221475569509379L;

	/**
	 * Enumerates the classes of distances.
	 * 
	 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
	 */
	public enum DistanceClass {
		/**
		 * The distance is far.
		 */
		Far,
		/**
		 * The distance is close.
		 */
		Close,
		/**
		 * The distance is undefined.
		 */
		Undefined;
	}

	/**
	 * The pair of labels for which the correlation is held in this instance.
	 * The ordering of the labels matter and gives the direction of dependency.
	 * knowledge(firstLabel) -> knowledge(secondLabel)
	 */
	private LabelPair labels;
	/**
	 * The count of close knowledge values (comparing only knowledge values taken in the same time frame)
	 * identified by the first label.
	 */
	private int data1CloseCount;
	/**
	 * The count of close knowledge values (comparing only knowledge values taken in the same time frame)
	 * identified by the second label. The comparison of these knowledge values is made only when the
	 * knowledge values identified by the first label are close. The distance classes of the knowledge
	 * values identified by the first label serves as a filter of the knowledge values identified by
	 * the second label that will be compared.
	 */
	private int data2CloseCount;
	
	/**
	 * Create new instance of CorrelationLevel for given pair of labels.
	 * @param labels the pair of label for which the correlation will be held.
	 */
	public CorrelationLevel(LabelPair labels){
		this.labels = labels;
		data1CloseCount = 0;
		data2CloseCount = 0;
	}
	
	/**
	 * The passed values will contribute to the computed correlation.
	 * @param component1Value1 The value from one component identified by the first label. 
	 * @param component2Value1 The value from the other component identified by the first label.
	 * @param component1Value2 The value from one component identified by the second label.
	 * @param component2Value2 The value from the other component identified by the second label.
	 */
	public void addValues(Object component1Value1, Object component2Value1,
			Object component1Value2, Object component2Value2){
		if(DistanceClass.Close == KnowledgeMetadataHolder.classifyDistance(
				labels.getFirstLabel(), component1Value1, component2Value1)){
			data1CloseCount++;
			if(DistanceClass.Close == KnowledgeMetadataHolder.classifyDistance(
					labels.getSecondLabel(), component1Value2, component2Value2)){
				data2CloseCount++;
			}
		}
	}
	
	/**
	 * Returns the pair of labels for which the correlation is held in this instance.
	 * @return The pair of labels for which the correlation is held in this instance.
	 */
	public LabelPair getLabelPair(){
		return labels;
	}
	
	/**
	 * Returns the correlation level for the knowledge identified by the second label dependent
	 * on the knowledge identified by the first label.
	 * @return The correlation level for the knowledge identified by the second label dependent
	 * on the knowledge identified by the first label.
	 */
	public double getCorrelationLevel(){
		return (double) data2CloseCount / (double) data1CloseCount;
	}
	
	
}