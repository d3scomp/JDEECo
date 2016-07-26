package cz.cuni.mff.d3s.jdeeco.adaptation.correlation.metadata;

import java.io.Serializable;


/**
 * A simple holder of a pair of knowledge labels.
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 */
public class LabelPair implements Serializable {

	/**
	 * Generated serial version UID.
	 */
	private static final long serialVersionUID = 544506009052039428L;
	
	/**
	 * The first label.
	 */
	private String firstLabel;
	/**
	 * The second label.
	 */
	private String secondLabel;
	
	/**
	 * Creates a new pair of knowledge labels.
	 * @param label1 The first label in the pair.
	 * @param label2 The second label in the pair.
	 */
	public LabelPair(String label1, String label2){
		firstLabel = label1;
		secondLabel = label2;
	}
	
	/**
	 * Returns the first label in the pair.
	 * @return The first label in the pair.
	 */
	public String getFirstLabel(){
		return firstLabel;
	}
	
	/**
	 * Returns the second label in the pair.
	 * @return The second label in the pair.
	 */
	public String getSecondLabel(){
		return secondLabel;
	}
	
	/**
	 * Returns true if the argument is LabelPair and
	 * both the labels from the other LabelPair equals the labels
	 * from this LabelPair. The ordering of the labels matter.
	 * @param other the LabelPair to compare with.
	 * @return True if the other is an LabelPair and both its
	 * labels equals to the labels in this instance. The ordering
	 * of the labels matter in the comparison. False otherwise.
	 */
	@Override
	public boolean equals(Object other){
		if(!(other instanceof LabelPair)){
			return false;
		}
		
		LabelPair otherLabelPair = (LabelPair) other;
		
		return firstLabel.equals(otherLabelPair.firstLabel)
				&& secondLabel.equals(otherLabelPair.secondLabel);
	}
	
	@Override
	public int hashCode() {
		return String.format("%s;%s", firstLabel, secondLabel).hashCode();
	}
}
