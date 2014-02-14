
package cz.cuni.mff.d3s.deeco.network;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cz.cuni.mff.d3s.deeco.DeecoProperties;
import cz.cuni.mff.d3s.deeco.knowledge.ChangeSet;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManagerContainer;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeNotFoundException;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeUpdateException;
import cz.cuni.mff.d3s.deeco.knowledge.ValueSet;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeField;
import cz.cuni.mff.d3s.deeco.model.runtime.custom.RuntimeMetadataFactoryExt;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataFactory;
import cz.cuni.mff.d3s.deeco.scheduler.CurrentTimeProvider;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;


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
public class KnowledgeDataManager implements KnowledgeDataReceiver,
KnowledgeDataPublisher {
	
	// this rssi corresponds to roughly 20m distance
	public static final double RSSI_20m = 5.52e-8; 
	// this rssi corresponds to roughly 10m distance
	public static final double RSSI_10m = 3.12e-7;
	// this rssi corresponds to roughly 50m distance
	public static final double RSSI_50m = 5.59e-9;
	
	// this rssi corresponds to max (roughly 250m) distance
	public static final double RSSI_MAX = 1.11e-10;
	
	/** Global version counter for all outgoing local knowledge. */
	protected long localVersion;	
	/** For scheduling rebroadcasts. */
	protected final Scheduler scheduler;
	/** Service for convenient time retrieval. */
	protected final CurrentTimeProvider timeProvider;
	/** The identification of the node on which the manager is running. */
	protected final String host;	
	/** The local knowledge container that is connected by this object to the network. */
	protected final KnowledgeManagerContainer kmContainer;	
	/** Object used for sending {@link KnowledgeData} over the network. */
	protected final KnowledgeDataSender knowledgeDataSender;	
	/** Stores received KnowledgeMetaData for replicas (received ValueSet is deleted) */
	protected final Map<KnowledgeManager, KnowledgeMetaData> replicaMetadata;
	/** Empty knowledge path enabling convenient query for all knowledge */  
	private final List<KnowledgePath> emptyPath;
	/** List of ensemble definitions whose boundary conditions should be considered. */
	private final List<EnsembleDefinition> ensembleDefinitions;
	
	private final Map<String, KnowledgeData> dataToRebroadcast;
	
	private final boolean useIndividualPublishing;
	private final boolean checkGossipCondition;
	private final boolean checkBoundaryCondition;
	private final double rssiLimit;
	private final Random random;
	
	public static final int DEFAULT_MAX_REBROADCAST_DELAY = (int) (PublisherTask.DEFAULT_PUBLISHING_PERIOD / 2.0);
	private final int maxRebroadcastDelay;
	
	
	//TODO This needs to be changed
	private final Collection<DirectRecipientSelector> recipientSelectors;
	private final DirectGossipStrategy directGossipStrategy;

	
	/**
	 * Creates an initialized instance.
	 * 
	 * @param kmContainer	the local knowledge container that should be connected by this manager to the network
	 * @param knowledgeDataSender	object used for sending {@link KnowledgeData} over the network
	 * @param host	identification of the node on which this manager is running
	 * @param scheduler	scheduler
	 */	
	public KnowledgeDataManager(
			KnowledgeManagerContainer kmContainer,
			KnowledgeDataSender knowledgeDataSender,
			List<EnsembleDefinition> ensembleDefinitions,
			String host,
			Scheduler scheduler,
			Collection<DirectRecipientSelector> recipientSelectors, DirectGossipStrategy directGossipStrategy) {
		this.host = host;		
		this.scheduler = scheduler;
		this.timeProvider = scheduler;
		this.kmContainer = kmContainer;
		this.knowledgeDataSender = knowledgeDataSender;
		this.ensembleDefinitions = ensembleDefinitions;
		this.localVersion = 0;
		this.replicaMetadata = new HashMap<>();
		this.recipientSelectors = recipientSelectors;
		this.directGossipStrategy = directGossipStrategy;
		
		dataToRebroadcast = new HashMap<>();
		
		RuntimeMetadataFactory factory = RuntimeMetadataFactoryExt.eINSTANCE;
		KnowledgePath empty = factory.createKnowledgePath();
		emptyPath = new LinkedList<>();
		emptyPath.add(empty);
		
		useIndividualPublishing = Boolean.getBoolean(DeecoProperties.USE_INDIVIDUAL_KNOWLEDGE_PUBLISHING);
		Log.d(String.format("KnowledgeDataManager at %s uses %s publishing", host, useIndividualPublishing ? "individual" : "list"));
		
		checkGossipCondition = !Boolean.getBoolean(DeecoProperties.DISABLE_GOSSIP_CONDITION);
		Log.d(String.format("KnowledgeDataManager at %s uses checkGossipCondition = %b", host, checkGossipCondition));
		
		checkBoundaryCondition = !Boolean.getBoolean(DeecoProperties.DISABLE_BOUNDARY_CONDITIONS);
		Log.d(String.format("KnowledgeDataManager at %s uses checkBoundaryCondition = %b", host, checkBoundaryCondition));
		
		maxRebroadcastDelay = Integer.getInteger(DeecoProperties.MAXIMUM_REBROADCAST_DELAY, DEFAULT_MAX_REBROADCAST_DELAY);
		Log.d(String.format("KnowledgeDataManager at %s uses maxRebroadcastDelay = %d", host, maxRebroadcastDelay));

		double rssi = 0;
		try {
			rssi = Double.parseDouble(System.getProperty(DeecoProperties.GOSSIP_CONDITION_RSSI));
		} catch (Exception e) {
			rssi = RSSI_50m;
		}
		rssiLimit = rssi;
		Log.d(String.format("KnowledgeDataManager at %s uses rssiLimit = %g", host, rssiLimit));
		
		long seed = 0;
		for (char c: host.toCharArray())
			seed += c;
		random = new Random(seed);
	}
	
	
	@Override
	public void publish() {
		// we re-publish periodically only local data
		List<KnowledgeData> data = prepareLocalKnowledgeData();
		
//		int origCnt = dataToRebroadcast.size();
//		filterBasedOnBoundaryCondition(dataToRebroadcast, getNodeKnowledge());
//		filterBasedOnGossipCondition(dataToRebroadcast);
//		
//		Log.d(String.format("Rebroadcasting %d out of %d received messages", dataToRebroadcast.size(), origCnt));
//		
//		for (KnowledgeData kd: dataToRebroadcast) {
//			data.add(prepareForRebroadcast(kd));
//		}
//		dataToRebroadcast.clear();
		
		
		//TODO
		if (!data.isEmpty()) {
			
			logPublish(data);
			
			if (useIndividualPublishing) {
				// broadcast each kd individually to minimize the message size and
				// thus reduce network collisions.
				for (KnowledgeData kd: data) {
					knowledgeDataSender.broadcastKnowledgeData(Arrays.asList(kd));
				}
			} else {
				knowledgeDataSender.broadcastKnowledgeData(data);
			}
			
			sendDirect(data);
			localVersion++;
		}
	}


	private void sendDirect(List<KnowledgeData> data) {
		if (recipientSelectors != null && !recipientSelectors.isEmpty()) {
			//Publishing to IP
			Collection<String> recipients;
			//For IP part we are using individual publishing only
			for (KnowledgeData kd : data) {
				recipients = getRecipients(kd, getNodeKnowledge());
				for (String recipient: recipients) {
					if (directGossipStrategy.gossipTo(recipient)) {
						logPublish(data, recipient);
						knowledgeDataSender.sendKnowledgeData(Arrays.asList(kd), recipient);
					}
				}
			}
		}
	}
	

	@Override
	public void rebroacast(KnowledgeMetaData metadata) {
		String sig = metadata.getSignature();
		// if the data was marked as not to be sent anymore (e.g., it was received again in the mean time)
		if (!dataToRebroadcast.containsKey(sig))  {			
			return;
		}
		
		KnowledgeData data = prepareForRebroadcast(dataToRebroadcast.get(sig));
		
		logPublish(Arrays.asList(data));
				
		knowledgeDataSender.broadcastKnowledgeData(Arrays.asList(data));
		dataToRebroadcast.remove(sig);
		Log.d(String.format("Rebroadcast finished (%d) at %s, data %s", timeProvider.getCurrentTime(), host, sig));
		
		sendDirect(Arrays.asList(data));
	}

	@Override
	public void receive(List<? extends KnowledgeData> knowledgeData) {
		if (knowledgeData == null) 
			Log.w("KnowledgeDataManager.receive: Received null KnowledgeData.");
		
		logReceive(knowledgeData);
		
		for (KnowledgeData kd : knowledgeData) {
			KnowledgeMetaData newMetadata = kd.getMetaData();
			if (kmContainer.hasLocal(newMetadata.componentId)) {
				Log.i("KnowledgeDataManager.receive: Dropping KnowledgeData for local component " + newMetadata.componentId);
				continue;
			} 
			KnowledgeManager replica = kmContainer.createReplica(newMetadata.componentId);			
			KnowledgeMetaData currentMetadata = replicaMetadata.get(replica);
						
			// if we get the same or newer data before we manage to rebroadcast, abort the rebroadcast 
			if ((currentMetadata != null)
					&& (currentMetadata.versionId <= newMetadata.versionId)
					&& dataToRebroadcast.containsKey(currentMetadata.getSignature())) {
				dataToRebroadcast.remove(currentMetadata.getSignature());
				Log.d(String.format(
						"Rebroadcast aborted (%d) at %s, data %s, because of %s",
						timeProvider.getCurrentTime(), host,
						currentMetadata.getSignature(),
						newMetadata.getSignature()));
			}
			
			// accept only fresh knowledge data (drop if we have already a newer value)
			boolean haveOlder = (currentMetadata == null) || (currentMetadata.versionId < newMetadata.versionId);			
			if (haveOlder) {	
				try {
				replica.update(toChangeSet(kd.getKnowledge()));			
				} catch (KnowledgeUpdateException e) {
					Log.w(String
							.format("KnowledgeDataManager.receive: Could not update replica of %s.",
									newMetadata.componentId), e);
				}
				//	store the metadata without the knowledge values
				replicaMetadata.put(replica, newMetadata);
				
				// rebroadcast only data that is of some value (i.e., newer than we have)
				queueForRebroadcast(kd);
				
				Log.d(String.format("Receive (%d) at %s got %sv%d after %dms and %d hops\n", 
						timeProvider.getCurrentTime(), 
						host, 
						newMetadata.componentId, 
						newMetadata.versionId,
						timeProvider.getCurrentTime() - newMetadata.createdAt,
						newMetadata.hopCount));
			} 
			
		}
		
	}
	
	void queueForRebroadcast(KnowledgeData kd) {
		if (checkBoundaryCondition && !isInSomeBoundary(kd, getNodeKnowledge())) {
			Log.d(String.format("Boundary failed (%d) at %s for %sv%d\n", 
					timeProvider.getCurrentTime(), host, kd.getMetaData().componentId, kd.getMetaData().versionId));
			return;
		} 
		
		KnowledgeMetaData kmd = kd.getMetaData();
		int delay = getRebroadcastDelay(kmd);
		
		
		// delay < 0 indicates not rebroadcasting
		if (delay < 0) {			
			return;
		}
		
		Log.d(String.format(
				"Gossip rebroadcast (%d) at %s for %sv%d from %s with rssi %g with delay %d\n",
				timeProvider.getCurrentTime(), host,
				kmd.componentId,
				kmd.versionId, kmd.sender,
				kmd.rssi, delay));
		
		dataToRebroadcast.put(kmd.getSignature(), kd);
		
		// schedule a task for rebroadcast
		RebroadcastTask task = new RebroadcastTask(scheduler, this, delay, kmd);
		scheduler.addTask(task);
	}
	

	private boolean isInSomeBoundary(KnowledgeData data, KnowledgeManager sender) {
		boolean isInSomeBoundary = false;
		for (EnsembleDefinition ens: ensembleDefinitions) {
			// null boundary condition counts as a satisfied one
			if (ens.getCommunicationBoundary() == null 
					|| ens.getCommunicationBoundary().eval(data, sender)) {
				isInSomeBoundary = true;
				break;
			}
		}
		return isInSomeBoundary;
	}
	
	private Collection<String> getRecipients(KnowledgeData data, KnowledgeManager sender) {
		List<String> result = new LinkedList<>();
		for (DirectRecipientSelector selector: recipientSelectors) {
			result.addAll(selector.getRecipients(data, sender));
		}
		return result;
	}
	

//	private boolean satisfiesGossipCondition(KnowledgeMetaData kmd) {
//		// rssi < 0 means received from IP
//		if (kmd.rssi < 0) {
//			Log.d("Got data from IP. Gossip condition does not apply.");
//			return true;
//		}
//		
//		// the further further from the source (i.e. smaller rssi) the bigger
//		// probability to satisfy the condition
//		double rssi = Math.log(Math.max(RSSI_MAX, kmd.rssi));
//		double ratio = rssi / Math.log(RSSI_MAX) ;	
//		return random.nextDouble() < ratio;		
//	}
//	
	private int getRebroadcastDelay(KnowledgeMetaData metaData) {
		// rssi < 0 means received from IP
		if (metaData.rssi < 0) {
			Log.d("Got data from IP. Gossip condition does not apply, rebroadcasting automatically.");
			return 1;
		}
		
		// the further further from the source (i.e. smaller rssi) the bigger
		// probability to of rebroadcast (i.e., the smaller delay)
		double rssi = Math.log(Math.max(RSSI_MAX, metaData.rssi));
		double ratio = rssi/Math.log(RSSI_MAX);	
		
		int maxDelay = (int) (ratio * maxRebroadcastDelay);
		
		// special case: rebroadcast immediately
		if (maxDelay == 0)
			return 1;
		
		// if received invalid rssi, then do not rebroadcast
		if (maxDelay < 0 || maxDelay > maxRebroadcastDelay) {
			Log.w(String.format("Invalid RSSI: %d leading to invalid rebroadcast delay max: ", metaData.rssi, maxDelay));
			return -1;
		}
		
		int result = random.nextInt(maxDelay);		
		// the delay must be > 0
		result++;
		return result;
	}



//	
//	void filterBasedOnGossipCondition(Collection<? extends KnowledgeData> data) {		
//		if (!checkGossipCondition)
//			return;
//		
//		Iterator<? extends KnowledgeData> iterator = data.iterator();
//		while(iterator.hasNext()) {
//			KnowledgeMetaData kmd = iterator.next().getMetaData();
//			if (!satisfiesGossipCondition(kmd)) { 
//				Log.d(String.format("Gossip condition failed (%d) at %s for %sv%d from %s with rssi %g\n", 
//						timeProvider.getCurrentTime(), host, kmd.componentId, kmd.versionId, kmd.sender, kmd.rssi));
//				iterator.remove();
//			} else {
//				Log.d(String.format("Gossip condition succeeded (%d) at %s for %sv%d from %s with rssi %g\n", 
//						timeProvider.getCurrentTime(), host, kmd.componentId, kmd.versionId, kmd.sender, kmd.rssi));
//			}
//		}
//	}
//	
//	void filterBasedOnBoundaryCondition(Collection<? extends KnowledgeData> data, KnowledgeManager nodeKnowledge) {
//		if (!checkBoundaryCondition)
//			return;
//		
//		Iterator<? extends KnowledgeData> iterator = data.iterator();
//		while(iterator.hasNext()) {
//			KnowledgeData kd = iterator.next();
//			if (!isInSomeBoundary(kd, nodeKnowledge)) {
//				Log.d(String.format("Boundary failed (%d) at %s for %sv%d\n", 
//						timeProvider.getCurrentTime(), host, kd.getMetaData().componentId, kd.getMetaData().versionId));
//				iterator.remove();
//			} 
//		}
//	}
		
		
	
	List<KnowledgeData> prepareLocalKnowledgeData() {
		List<KnowledgeData> result = new LinkedList<>();
		for (KnowledgeManager km : kmContainer.getLocals()) {
			try {
				KnowledgeData kd = prepareLocalKnowledgeData(km);				
				result.add(filterLocalKnowledgeForKnownEnsembles(kd));				
			} catch (Exception e) {
				Log.e("prepareKnowledgeData error", e);
			}
		}
		return result;
	}
	

	KnowledgeData prepareLocalKnowledgeData(KnowledgeManager km)
			throws KnowledgeNotFoundException {
		return new KnowledgeData(
				km.get(emptyPath), 
				new KnowledgeMetaData(km.getId(), localVersion, host, timeProvider.getCurrentTime(), 1));
	}
	
	KnowledgeData prepareForRebroadcast(KnowledgeData receivedData) {		
		KnowledgeMetaData kmdCopy = receivedData.getMetaData().clone();
		kmdCopy.sender = host;
		kmdCopy.hopCount++;
		return new KnowledgeData(receivedData.getKnowledge(), kmdCopy);
	}
	
	KnowledgeData filterLocalKnowledgeForKnownEnsembles(KnowledgeData kd) {
		//FIXME: make this generic
		// now we hardcode our demo (we filter the Leader knowledge to only
		// publish id, team, and position.
		if (kd.getMetaData().componentId.startsWith("L")) {
			ValueSet values = kd.getKnowledge();
			ValueSet newValues = new ValueSet();
			for (KnowledgePath kp: values.getKnowledgePaths()) {
				String name = ((PathNodeField) kp.getNodes().get(0)).getName();
				if (name.equals("id") || name.equals("teamId") || name.equals("position")) {
					newValues.setValue(kp, values.getValue(kp));
				}
			}
			return new KnowledgeData(newValues, kd.getMetaData());
		} else {
			return kd;
		}
	}



	private KnowledgeManager getNodeKnowledge() {
		// FIXME: in the future, we need to unify the knowledge of all the local KMs.
		return kmContainer.getLocals().iterator().next();
	}

	private ChangeSet toChangeSet(ValueSet valueSet) {
		if (valueSet != null) {
			ChangeSet result = new ChangeSet();
			for (KnowledgePath kp : valueSet.getKnowledgePaths())
				result.setValue(kp, valueSet.getValue(kp));
			return result;
		} else {
			return null;
		}
	}
	
	private void logPublish(List<? extends KnowledgeData> data) {
		logPublish(data, "");
	}
	
	private void logPublish(List<? extends KnowledgeData> data, String recipient) {
		StringBuilder sb = new StringBuilder();
		for (KnowledgeData kd: data) {
			sb.append(kd.getMetaData().componentId + "v" + kd.getMetaData().versionId);
			sb.append(", ");			
		}		
		if (recipient != null && !recipient.isEmpty())
			Log.d(String.format("Publish (%d) at %s, sending [%s] directly to %s\n", 
				timeProvider.getCurrentTime(), host, sb.toString(), recipient));
		else
			Log.d(String.format("Publish (%d) at %s, sending [%s]\n", 
					timeProvider.getCurrentTime(), host, sb.toString()));
	}
	
	private void logReceive(List<? extends KnowledgeData> knowledgeData) {
		StringBuilder sb = new StringBuilder();
		for (KnowledgeData kd: knowledgeData) {
			sb.append(kd.getMetaData().componentId + "v" + kd.getMetaData().versionId + "<-" + kd.getMetaData().sender);
			sb.append(", ");			
		}
		
		Log.d(String.format("Receive (%d) at %s, received [%s]\n", 
				timeProvider.getCurrentTime(), host, sb.toString()));
	}
	
}
