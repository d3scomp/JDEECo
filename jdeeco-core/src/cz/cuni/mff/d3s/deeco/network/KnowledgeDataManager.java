
package cz.cuni.mff.d3s.deeco.network;

import java.util.List;

import cz.cuni.mff.d3s.deeco.integrity.RatingsManager;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManagerContainer;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;
import cz.cuni.mff.d3s.deeco.security.SecurityKeyManager;
import cz.cuni.mff.d3s.deeco.timer.CurrentTimeProvider;


/**
 * This class represents the layer between the network and the
 * {@link KnowledgeManagerContainer}.
 * 
 * <p>
 * It is responsible for preparing knowledge for publication on the network and
 * also for processing the knowledge coming from the network and updating the
 * {@link KnowledgeManagerContainer} (see {@link #prepareKnowledgeData()} and
 * {@link #receive(List)}). A single "snapshot" of a knowledge repository (i.e.,
 * a belief) is represented by the {@link KnowledgeData} class, which contains
 * both the knowledge values and all the metadata necessary for knowledge
 * propagation over network.
 * </p>
 * <p>
 * It uses the interfaces {@link KnowledgeDataSender} and
 * {@link KnowledgeDataReceiver} for interfacing with the network layer.
 * </p>
 *  
 * @author Jaroslav Keznikl <keznikl@d3s.mff.cuni.cz>
 * 
 * @see KnowledgeManagerContainer
 * @see KnowledgeData
 * @see KnowledgeDataSender
 * @see KnowledgeDataReceiver 
 */
@SuppressWarnings("rawtypes")
public abstract class KnowledgeDataManager extends DataReceiver<List> implements
KnowledgeDataPublisher {
	
	public KnowledgeDataManager() {
		super(List.class);
	}

	/** For scheduling rebroadcasts. */
	protected Scheduler scheduler;
	/** Service for convenient time retrieval. */
	protected CurrentTimeProvider timeProvider;
	/** The identification of the node on which the manager is running. */
	protected String host;	
	/** The local knowledge container that is connected by this object to the network. */
	protected KnowledgeManagerContainer kmContainer;	
	/** Object used for sending {@link dataSender} over the network. */
	protected DataSender dataSender;		
	/** handles public keys for security roles */
	protected SecurityKeyManager keyManager;
	/** Handles rating data*/
	protected RatingsManager ratingsManager;
	
	/**
	 * Initializes instance.
	 * 
	 * @param kmContainer	the local knowledge container that should be connected by this manager to the network
	 * @param knowledgeDataSender	object used for sending {@link KnowledgeData} over the network
	 * @param host	identification of the node on which this manager is running
	 * @param scheduler	scheduler
	 */	
	public void initialize(
			KnowledgeManagerContainer kmContainer,
			DataSender dataSender,
			String host,
			Scheduler scheduler,
			SecurityKeyManager keyManager,
			RatingsManager ratingsManager) {
		this.host = host;		
		this.scheduler = scheduler;
		this.timeProvider = scheduler.getTimer();
		this.kmContainer = kmContainer;
		this.dataSender = dataSender;
		this.keyManager = keyManager;
		this.ratingsManager = ratingsManager;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void receive(List data, double rssi) {
		if (data == null) {
			Log.w("KnowledgeDataManager.receive: Received null data.");
		} else if (data.isEmpty()) {
			Log.w("KnowledgeDataManager.receive: Received empty data.");
		} else {
			Object firstElement = data.get(0);
			if (firstElement.getClass().isAssignableFrom(KnowledgeData.class)) {
				List<KnowledgeData> knowledgeData = (List<KnowledgeData> ) data;
				for (KnowledgeData kd: knowledgeData) {
					kd.getMetaData().rssi = rssi;
				}
				receiveKnowledge(knowledgeData);
			} else if (firstElement.getClass().isAssignableFrom(RatingsData.class)) {
				List<RatingsData> ratingsData = (List<RatingsData> ) data;
				for (RatingsData rd : ratingsData) {
					rd.getRatingsMetaData().rssi = rssi;
				}
				receiveRatings(ratingsData);
			} else {
				Log.e("Unkown data received: " + firstElement.getClass());
			}
		}
	}
		
	public abstract void receiveKnowledge(List<KnowledgeData> data);
	public abstract void receiveRatings(List<RatingsData> ratingsData);
}
