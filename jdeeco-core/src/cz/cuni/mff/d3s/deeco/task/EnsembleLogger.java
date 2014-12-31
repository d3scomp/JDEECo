package cz.cuni.mff.d3s.deeco.task;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.knowledge.ReadOnlyKnowledgeManager;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleController;
import cz.cuni.mff.d3s.deeco.scheduler.CurrentTimeProvider;

/**
 * Main usage of this class: logging of the changes in ensemble membership
 * @see {@link EnsembleLogger#logEvent(EnsembleController, ReadOnlyKnowledgeManager, CurrentTimeProvider, boolean)}
 * 
 * @author Tomas Filipek <tom.filipek@seznam.cz>
 */
class EnsembleLogger {
	
	/**
	 * Encapsulates three pieces of data: ensemble name, coordinator ID and member ID.
	 * It is used to index the information whether the membership condition holds for the 
	 * given triplet. 
	 * 
	 * @author Tomas Filipek <tom.filipek@seznam.cz>
	 */
	private static class MembershipRecord {
		
		/**
		 * Identification of the ensemble
		 */
		private final String ensembleName;
		
		/**
		 * Identification of the coordinator
		 */
		private final String coordinatorID;
		
		/**
		 * Identification of the member
		 */
		private final String memberID;
		
		/**
		 * @param ensembleName Identification of the ensemble
		 * @param coordinatorID Identification of the coordinator
		 * @param memberID Identification of the member
		 */
		public MembershipRecord(String ensembleName, String coordinatorID, String memberID) {
			this.ensembleName = ensembleName;
			this.coordinatorID = coordinatorID;
			this.memberID = memberID;
		}
		
		/**
		 * The produced hash code depends on precisely the following fields:
		 * {@link MembershipRecord#ensembleName} , 
		 * {@link MembershipRecord#coordinatorID} , 
		 * {@link MembershipRecord#memberID}
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime
					* result
					+ ((coordinatorID == null) ? 0 : coordinatorID
							.hashCode());
			result = prime
					* result
					+ ((ensembleName == null) ? 0 : ensembleName.hashCode());
			result = prime * result
					+ ((memberID == null) ? 0 : memberID.hashCode());
			return result;
		}
		
		/**
		 * The equality holds if and only if the following fields are equal amid the two instances:
		 * {@link MembershipRecord#ensembleName} , 
		 * {@link MembershipRecord#coordinatorID} , 
		 * {@link MembershipRecord#memberID}
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (!(obj instanceof EnsembleLogger.MembershipRecord)) {
				return false;
			}
			EnsembleLogger.MembershipRecord other = (EnsembleLogger.MembershipRecord) obj;
			if (coordinatorID == null) {
				if (other.coordinatorID != null) {
					return false;
				}
			} else if (!coordinatorID.equals(other.coordinatorID)) {
				return false;
			}
			if (ensembleName == null) {
				if (other.ensembleName != null) {
					return false;
				}
			} else if (!ensembleName.equals(other.ensembleName)) {
				return false;
			}
			if (memberID == null) {
				if (other.memberID != null) {
					return false;
				}
			} else if (!memberID.equals(other.memberID)) {
				return false;
			}
			return true;
		}	
	}

	/**
	 * The event log will be saved to this file.
	 */
	private final File ensembleLogFile = new File("logs/ensembles.xml");
	
	/**
	 * An instance of {@link RandomAccessFile} that writes into {@link EnsembleLogger#ensembleLogFile}.
	 * @see {@link EnsembleLogger#ensembleLogFile}
	 */
	private final RandomAccessFile out;
	
	/**
	 * <p> Used to remember the last encountered value of the membership condition, computed on the 
	 * input objects which are given as an instance of {@link MembershipRecord}. </p> 
	 * <p> <b>Example:</b><br/>
	 * key = triplet of (ensemble E, coordinator C, member M)<br/>
	 * value = true (meaning that when last checked, M was in ensemble E with C as the coordinator)
	 * </p>
	 */
	private final Map<EnsembleLogger.MembershipRecord, Boolean> membershipRecords = new HashMap<>();
	
	/**
	 * Initializes the output streams
	 */
	private EnsembleLogger() {
		RandomAccessFile raf;
		try {
			raf = new RandomAccessFile(ensembleLogFile, "rwd");
		} catch (Exception ex) {
			raf = null;
		}
		out = raf;
		try {
			out.setLength(0);
			out.write(("<events>\n" + bottomElement).getBytes());
		} catch (IOException | NullPointerException ex) {
			Log.e("Could not log an ensemble event to " + ensembleLogFile.getAbsolutePath().toString());
		}
	}
	
	/**
	 * The singleton instance of this class
	 */
	private static final EnsembleLogger INSTANCE = new EnsembleLogger();
	
	/**
	 * @return The singleton instance of this class
	 * @see {@link EnsembleLogger#INSTANCE}
	 */
	public static EnsembleLogger getInstance() {
		return INSTANCE;
	}
	
	/**
	 * Local name of the event element, as it appears in the output log file 
	 */
	private final String eventElementName = "event";
	
	/**
	 * Names of the event element attributes that appear in the output log file,
	 * excluding the "type" attribute, which is added to the output separately
	 */
	private final String[] eventAttributes = new String[] {"coordinator", "member", "membership", "ensemble", "time"};
	
	/**
	 * Closing root element
	 */
	private final String bottomElement = "</events>";
	
	/**
	 * Length (in characters) of the closing root element
	 */
	private final int footerLength = bottomElement.getBytes().length;

	/**
	 * Logs the information about ensemble membership to the output file. It remembers the 
	 * previous value, so the output is produced only if it has changed from the last time. 
	 * @param ensembleController Controller of the {@link EnsembleTask} where this method is called.
	 * @param shadowKnowledgeManager Knowledge of the other component
	 * @param timeProvider Used to get the current time
	 * @param membership The current value of the membership condition
	 */
	public void logEvent(EnsembleController ensembleController, ReadOnlyKnowledgeManager shadowKnowledgeManager, 
			CurrentTimeProvider timeProvider, boolean membership){
		if ((out == null) || (ensembleController == null) || (shadowKnowledgeManager == null) || (timeProvider == null)){
			return;
		}		
		long timeSeconds = timeProvider.getCurrentMilliseconds() / 1000L;
		String ensembleName = ensembleController.getEnsembleDefinition().getName();
		String coordinatorID = ensembleController.getComponentInstance().getKnowledgeManager().getId();
		String memberID = shadowKnowledgeManager.getId();
		EnsembleLogger.MembershipRecord mr = new MembershipRecord(ensembleName, coordinatorID, memberID);
		boolean oldMembership = membershipRecords.get(mr) == null ? false : membershipRecords.get(mr);
		if (oldMembership != membership){
			membershipRecords.put(mr, Boolean.valueOf(membership));
			String[] attributeValues = new String[] {coordinatorID, memberID, Boolean.toString(membership), ensembleName, Long.toString(timeSeconds)};
			String elementText = getElementText(eventElementName, eventAttributes, attributeValues);
			if (elementText != null){
				try {
					bufferedWrite(elementText);
				} catch (IOException | NullPointerException ex) {
					Log.e("Could not log an ensemble event to " + ensembleLogFile.getAbsolutePath().toString());
				}
			}
		}
	}
	
	/**
	 * Buffer for text that will be written to the output file
	 */
	private final StringBuilder buffer = new StringBuilder();
	
	/**
	 * Current size of the buffer, i.e. number of event elements it holds
	 */
	private int bufferCurrentSize = 0;
	
	/**
	 * When the buffer size {@link EnsembleLogger#bufferCurrentSize} grows to 
	 * this size, the buffer contents are written to the output file.
	 */
	private final int bufferCapacity = 16;
	
	/**
	 * Buffered write to the output file.
	 * @param text Text to be written to the output file.
	 * @throws IOException When the text could not be written to the output file for some reason
	 */
	private void bufferedWrite(String text) throws IOException{
		buffer.append(text);
		bufferCurrentSize += 1;
		if (bufferCurrentSize >= bufferCapacity){
			buffer.append(bottomElement);
			long newPosition = out.length() - footerLength;
			out.seek(newPosition);
			out.write(buffer.toString().getBytes());
			buffer.setLength(0);
			bufferCurrentSize = 0;
		}
	}
	
	/**
	 * Builds a textual representation of an XML element defined by the parameters.
	 * A newline is added to the end of the element.
	 * @param name Name of the element
	 * @param attributeNames Names of the attributes, in the order as they will appear
	 * @param attributeValues Values of the attributes, in the same order as the names
	 * @return A textual representation of an XML element defined by the parameters 
	 */
	private String getElementText(String name, String[] attributeNames, String[] attributeValues){
		if ((name == null) || name.isEmpty() || (attributeNames == null) ||
				(attributeValues == null) || (attributeNames.length != attributeValues.length)){
			return null;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("<");
		sb.append(name);
		sb.append(" ");
		sb.append("type=\"ensemble\" ");
		for (int i = 0; i < attributeNames.length; i++){
			String attrName = attributeNames[i];
			String attrValue = attributeValues[i];
			sb.append(attrName);
			sb.append("=\"");
			sb.append(attrValue);
			sb.append("\" ");
		}
		sb.append("/>");
		sb.append("\n");
		return sb.toString();
	}	
}
