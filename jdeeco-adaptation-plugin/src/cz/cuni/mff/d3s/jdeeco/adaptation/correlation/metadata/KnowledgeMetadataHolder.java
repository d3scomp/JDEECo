package cz.cuni.mff.d3s.jdeeco.adaptation.correlation.metadata;

import java.util.HashMap;
import java.util.Map;

import cz.cuni.mff.d3s.jdeeco.adaptation.correlation.metadata.CorrelationLevel.DistanceClass;
import cz.cuni.mff.d3s.jdeeco.adaptation.correlation.metric.Metric;

/**
 * A class that maps additional metadata to individual knowledge fields.
 * The metadata are hard coded to predefined knowledge fields identified by
 * labels. The metadata are a distance bound and metric.
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 */
public class KnowledgeMetadataHolder {

	/**
	 * Encapsulates the metadata for a single knowledge field.
	 * 
	 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
	 */
	private static class KnowledgeMetadata{
		/**
		 * The distance bound delimiting close and far distance.
		 */
		private double bound;
		/**
		 * The metric to compute the distance of values of the knowledge field.
		 */
		private Metric metric;
		/**
		 * The boundary that delimits when the correlation is accurate enough.
		 * It is a number from the interval [0,1], that represent percentage.
		 * The correlation itself reflects the percentage of "close" values when
		 * the filtering values are "close".
		 */
		private double confidenceLevel;
		
		/**
		 * Create a new instance of KnowledgeMetadata.
		 * @param bound The distance bound delimiting close and far distance.
		 * @param metric The metric to compute the distance of values of the knowledge field.
		 * @Param confidence The boundary that delimits when the correlation is accurate enough.
		 * 		It is a number from the interval [0,1], that represent percentage.
		 * 		The correlation itself reflects the percentage of "close" values when
		 * 		the filtering values are "close".
		 */
		public KnowledgeMetadata(double bound, Metric metric, double confidence){
			this.bound = bound;
			this.metric = metric;
			confidenceLevel = confidence;
		}
		
		/**
		 * The distance bound delimiting close and far distance.
		 * @return The distance bound delimiting close and far distance.
		 */
		public double getBound(){
			return bound;
		}
		
		/**
		 * The metric to compute the distance of values of the knowledge field.
		 * @return The metric to compute the distance of values of the knowledge field.
		 */
		public Metric getMetric(){
			return metric;
		}
		
		/**
		 * The boundary that delimits when the correlation is accurate enough.
		 * 		It is a number from the interval [0,1], that represent percentage.
		 * 		The correlation itself reflects the percentage of "close" values when
		 * 		the filtering values are "close".
		 * @return The boundary that delimits when the correlation is accurate enough.
		 */
		public double getConfidenceLevel(){
			return confidenceLevel;
		}
	}
	
	/**
	 * Mapping of the metadata to knowledge fields identified by theirs labels.
	 */
	private static final Map<String, KnowledgeMetadata> knowledgeMetadata = new HashMap<String, KnowledgeMetadata>();

	/**
	 * Set the given metadata for the knowledge field identified by the given label. 
	 * @param label Identifies the metadata field.
	 * @param bound The distance bound delimiting close and far distance.
	 * @param metric The metric to compute the distance of values of the knowledge field.
	 * @Param confidence The boundary that delimits when the correlation is accurate enough.
	 * 		It is a number from the interval [0,1], that represent percentage.
	 * 		The correlation itself reflects the percentage of "close" values when
	 * 		the filtering values are "close".
	 */
	public static void setBoundAndMetric(String label, double bound, Metric metric, double confidence){
		knowledgeMetadata.put(label, new KnowledgeMetadata(bound, metric, confidence));
	}
	
	/**
	 * The distance bound delimiting close and far distance for the knowledge field
	 * identified by the given label.
	 * @param label Identifies the knowledge field.
	 * @return The distance bound delimiting close and far distance for the knowledge
	 * field identified by the given label.
	 */
	public static double getBound(String label){
		if(knowledgeMetadata.containsKey(label)){
			return knowledgeMetadata.get(label).getBound();
		}
		else {
			return -1;
		}
	}
	
	/**
	 * The metric to compute the distance of values of the knowledge field
	 * identified by the given label.
	 * @param label Identifies the knowledge field.
	 * @return The metric to compute the distance of values of the knowledge
	 * field identified by the given label.
	 */
	public static Metric getMetric(String label){
		if(knowledgeMetadata.containsKey(label)){
			return knowledgeMetadata.get(label).getMetric();
		}
		else {
			return null;
		}
	}

	/**
	 * The confidence level is a boundary that delimits when the correlation is accurate enough.
	 * It is a number from the interval [0,1], that represent percentage.
	 * The correlation itself reflects the percentage of "close" values when
	 * the filtering values are "close".
	 * @param label Identifies the knowledge field.
	 * @return The boundary that delimits when the correlation is accurate enough.
	 */
	public static double getConfidenceLevel(String label){
		if(knowledgeMetadata.containsKey(label)){
			return knowledgeMetadata.get(label).getConfidenceLevel();
		}
		else {
			return Double.NaN;
		}
	}
	
	/**
	 * Indicates whether the KnowledgeMetadataHolder contains the specified label.
	 * @param label Identifies the knowledge field.
	 * @return True if the MetadataKnowledgeHolder contains metadata for the
	 * knowledge field identified by the given label.
	 */
	public static boolean containsLabel(String label){
		return knowledgeMetadata.containsKey(label);
	}
	
	/**
	 * Classifies the distance between the given values based on the distance bound
	 * using the metric specific to the knowledge field identified by the given label.
	 * @param label Identifies the knowledge field.
	 * @param value1 The value to classify the distance from.
	 * @param value2 The value to classify the distance to.
	 * @return The class of the measured distance for the specified knowledge field.
	 */
	public static DistanceClass classifyDistance(String label, Object value1, Object value2){
		if(containsLabel(label)){
			Metric metric = getMetric(label);
			double bound = getBound(label);
			
			if(metric.distance(value1, value2) <= bound){
				return DistanceClass.Close;
			}
			else{
				return DistanceClass.Far;
			}	
		}
		
		return DistanceClass.Undefined;
	}

	/**
	 * Computes the distance between the given values
	 * using the metric specific to the knowledge field identified by the given label.
	 * @param label Identifies the knowledge field.
	 * @param value1 The value to classify the distance from.
	 * @param value2 The value to classify the distance to.
	 * @return The value of the measured distance for the specified knowledge field.
	 */
	public static double distance(String label, Object value1, Object value2){
		if(containsLabel(label)){
			Metric metric = getMetric(label);	
			return metric.distance(value1, value2);
		}
		
		return Double.NaN;
	}
}
