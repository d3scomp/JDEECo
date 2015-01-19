
package cz.cuni.mff.d3s.deeco.network;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import cz.cuni.mff.d3s.deeco.DeecoProperties;
import cz.cuni.mff.d3s.deeco.integrity.RatingsChangeSet;
import cz.cuni.mff.d3s.deeco.integrity.RatingsManager;
import cz.cuni.mff.d3s.deeco.knowledge.ChangeSet;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManagerContainer;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeNotFoundException;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeUpdateException;
import cz.cuni.mff.d3s.deeco.knowledge.ValueSet;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.custom.RuntimeMetadataFactoryExt;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataFactory;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;
import cz.cuni.mff.d3s.deeco.security.KnowledgeEncryptor;
import cz.cuni.mff.d3s.deeco.security.RatingsEncryptor;
import cz.cuni.mff.d3s.deeco.security.SecurityKeyManager;


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
@SuppressWarnings({"rawtypes", "unchecked"})
public class DefaultKnowledgeDataManager extends KnowledgeDataManager {
	
	// this rssi corresponds to roughly 20m distance
	public static final double RSSI_20m = 5.52e-8; 
	// this rssi corresponds to roughly 10m distance
	public static final double RSSI_10m = 3.12e-7;
	// this rssi corresponds to roughly 50m distance
	public static final double RSSI_50m = 5.59e-9;
	// this rssi corresponds to max (roughly 250m) distance
	public static final double RSSI_MIN = 1.11e-10;
	
	
	/** Stores received KnowledgeMetaData for replicas (received ValueSet is deleted) */
	protected final Map<String, KnowledgeMetaData> replicaMetadata;
	/** Stores role names of accepted replica versions */
	protected final Map<String, Set<Integer>> replicaRoles;
	/** Global version counter for all outgoing local knowledge. */
	protected long localVersion;
	
	
	/** Empty knowledge path enabling convenient query for all knowledge */  
	protected final List<KnowledgePath> emptyPath;
	/** List of ensemble definitions whose boundary conditions should be considered. */
	protected final List<EnsembleDefinition> ensembleDefinitions;
	
	protected final Map<String, KnowledgeData> dataToRebroadcastOverMANET;
	protected final Map<String, KnowledgeData> dataToRebroadcastOverIP;
	
	protected final boolean useIndividualPublishing;
	protected final boolean checkGossipCondition;
	protected final boolean checkBoundaryCondition;
	protected final double rssiLimit;
	protected final Random random;
	
	public static final int DEFAULT_MAX_REBROADCAST_DELAY = (int) (PublisherTask.DEFAULT_PUBLISHING_PERIOD);
	public static final int DEFAULT_IP_DELAY = 100;
	protected final int maxRebroadcastDelay;	
	protected final int ipDelay;
	
	protected final IPGossipStrategy ipGossipStrategy;
	protected KnowledgeEncryptor knowledgeEncryptor;
	protected RatingsEncryptor ratingsEncryptor;
	
	/**
	 * Creates an initialized instance.
	 * 
	 * @param kmContainer	the local knowledge container that should be connected by this manager to the network
	 * @param knowledgeDataSender	object used for sending {@link KnowledgeData} over the network
	 * @param host	identification of the node on which this manager is running
	 * @param scheduler	scheduler
	 */	
	public DefaultKnowledgeDataManager(
			List<EnsembleDefinition> ensembleDefinitions,
			IPGossipStrategy ipGossipStrategy) {
		this.ensembleDefinitions = ensembleDefinitions;
		this.localVersion = 0;
		this.replicaMetadata = new HashMap<>();
		this.ipGossipStrategy = ipGossipStrategy;
		this.replicaRoles = new HashMap<>();
		
		dataToRebroadcastOverMANET = new HashMap<>();
		dataToRebroadcastOverIP = new HashMap<>();
		
		RuntimeMetadataFactory factory = RuntimeMetadataFactoryExt.eINSTANCE;
		KnowledgePath empty = factory.createKnowledgePath();
		emptyPath = new LinkedList<>();
		emptyPath.add(empty);
		
		useIndividualPublishing = Boolean.getBoolean(DeecoProperties.USE_INDIVIDUAL_KNOWLEDGE_PUBLISHING);
		checkGossipCondition = !Boolean.getBoolean(DeecoProperties.DISABLE_GOSSIP_CONDITION);
		checkBoundaryCondition = !Boolean.getBoolean(DeecoProperties.DISABLE_BOUNDARY_CONDITIONS);
		maxRebroadcastDelay = Integer.getInteger(DeecoProperties.MAXIMUM_REBROADCAST_DELAY, DEFAULT_MAX_REBROADCAST_DELAY);
		ipDelay = Integer.getInteger(DeecoProperties.IP_REBROADCAST_DELAY, DEFAULT_IP_DELAY);

		double rssi = 0;
		try {
			rssi = Double.parseDouble(System.getProperty(DeecoProperties.GOSSIP_CONDITION_RSSI));
		} catch (Exception e) {
			rssi = RSSI_50m;
		}
		rssiLimit = rssi;
		random = new Random();
	}
	
	@Override
	public void initialize(
			KnowledgeManagerContainer kmContainer,
			DataSender dataSender,
			String host,
			Scheduler scheduler,
			SecurityKeyManager keyManager,
			RatingsManager ratingsManager) {
		super.initialize(kmContainer, dataSender, host, scheduler, keyManager, ratingsManager);
		long seed = 0;
		for (char c: host.toCharArray())
			seed += c;
		random.setSeed(seed);
		
		this.knowledgeEncryptor = new KnowledgeEncryptor(keyManager);
		this.ratingsEncryptor = new RatingsEncryptor(keyManager);
		
		Log.d(String.format("KnowledgeDataManager at %s uses %s publishing", host, useIndividualPublishing ? "individual" : "list"));
		Log.d(String.format("KnowledgeDataManager at %s uses checkGossipCondition = %b", host, checkGossipCondition));
		Log.d(String.format("KnowledgeDataManager at %s uses checkBoundaryCondition = %b", host, checkBoundaryCondition));
		Log.d(String.format("KnowledgeDataManager at %s uses maxRebroadcastDelay = %d", host, maxRebroadcastDelay));
		Log.d(String.format("KnowledgeDataManager at %s uses ipDelay = %d", host, ipDelay));
		Log.d(String.format("KnowledgeDataManager at %s uses rssiLimit = %g", host, rssiLimit));
	}
	
	@Override
	public void publish() {
		// we re-publish periodically only local data
		List<KnowledgeData> knowledgeData = prepareLocalKnowledgeData();
		if (!knowledgeData.isEmpty()) {
			
			logPublish(knowledgeData);
			
			if (useIndividualPublishing) {
				// broadcast each kd individually to minimize the message size and
				// thus reduce network collisions.
				for (KnowledgeData kd: knowledgeData) {
					//System.out.println("Broadcasting data at " + host + kd);
					dataSender.broadcastData(Arrays.asList(kd));
				}
			} else {
				//System.out.println("Broadcasting data at " + host + data);
				dataSender.broadcastData(knowledgeData);
			}
			if (ipGossipStrategy != null) {
				sendDirect(knowledgeData);
			}
			localVersion++;
		}
		
		// publish ratings data
		RatingsData ratingsData = prepareRatingsData();
		if (!ratingsData.getRatings().isEmpty()) {
			dataSender.broadcastData(Arrays.asList(ratingsData));
			// TODO sending directly via sendDirect()?
		}
	}	

	@Override
	public void rebroacast(KnowledgeMetaData metadata, NICType nicType) {
		String sig = metadata.getSignatureWithRole();
		KnowledgeData data;
		if (nicType.equals(NICType.MANET) && dataToRebroadcastOverMANET.containsKey(sig)) {	
			data = prepareForRebroadcast(dataToRebroadcastOverMANET.get(sig));
			logPublish(Arrays.asList(data));
			dataSender.broadcastData(Arrays.asList(data));
			dataToRebroadcastOverMANET.remove(sig);
			
			if (Log.isDebugLoggable())
				Log.d(String.format("Rebroadcast finished (%d) at %s, data %s", timeProvider.getCurrentMilliseconds(), host, sig));
			
		} else if (dataToRebroadcastOverIP.containsKey(sig)) {
			data = prepareForRebroadcast(dataToRebroadcastOverIP.get(sig));
			logPublish(Arrays.asList(data));
			sendDirect(Arrays.asList(data));
			dataToRebroadcastOverIP.remove(sig);
			
			if (Log.isDebugLoggable())
				Log.d(String.format("Rebroadcast finished (%d) at %s, data %s", timeProvider.getCurrentMilliseconds(), host, sig));
		}
	}
	
	@Override
	public void receiveRatings(List<RatingsData> ratingsDataList) {
		for (RatingsData ratingsData : ratingsDataList) {
			List<RatingsChangeSet> changeSets = ratingsEncryptor.decryptRatings(ratingsData.getRatings(), ratingsData.getRatingsMetaData());
			ratingsManager.update(changeSets);
		}		
	}

	@Override
	public void receiveKnowledge(List<KnowledgeData> data) {	
		logReceive(data);
		for (KnowledgeData kd : data) {
			KnowledgeMetaData newMetadata = kd.getMetaData();
			if (kmContainer.hasLocal(newMetadata.componentId)) {
				if (Log.isDebugLoggable()) 
					Log.d("KnowledgeDataManager.receive: Dropping KnowledgeData for local component " + newMetadata.componentId);
				
				continue;
			} 
			
			KnowledgeMetaData currentMetadata = replicaMetadata.get(newMetadata.componentId);
			
			if (!replicaRoles.containsKey(newMetadata.getSignature())) {
				replicaRoles.put(newMetadata.getSignature(), new HashSet<>());
			}
			
			// if we get the same or newer data before we manage to rebroadcast, abort the rebroadcast 
			if ((currentMetadata != null)
					&& (currentMetadata.versionId <= newMetadata.versionId)
					&& dataToRebroadcastOverMANET.containsKey(currentMetadata.getSignatureWithRole())) {
				dataToRebroadcastOverMANET.remove(currentMetadata.getSignatureWithRole());
				if (Log.isDebugLoggable()) {
					Log.d(String.format(
							"MANET: Rebroadcast aborted (%d) at %s, data %s, because of %s",
							timeProvider.getCurrentMilliseconds(), host,
							currentMetadata.getSignatureWithRole(),
							newMetadata.getSignatureWithRole()));
				}
			}
			
			if ((currentMetadata != null)
					&& (currentMetadata.versionId < newMetadata.versionId)
					&& dataToRebroadcastOverIP.containsKey(currentMetadata.getSignatureWithRole())) {
				dataToRebroadcastOverIP.remove(currentMetadata.getSignatureWithRole());
				if (Log.isDebugLoggable()) {
					Log.d(String.format(
							"IP: Rebroadcast aborted (%d) at %s, data %s, because of %s",
							timeProvider.getCurrentMilliseconds(), host,
							currentMetadata.getSignatureWithRole(),
							newMetadata.getSignatureWithRole()));
				}
			}
			
			// accept only fresh knowledge data (drop if we have already a newer value) or data for not yet processed role
			boolean haveOlder = (currentMetadata == null) || (currentMetadata.versionId < newMetadata.versionId); 
			
			if (!haveOlder) {
				Set<Integer> newRoles = replicaRoles.get(newMetadata.getSignature());
				Set<Integer> currentRoles = replicaRoles.get(currentMetadata.getSignature());
				
				boolean haveAllRoles = currentRoles.containsAll(newRoles) && currentRoles.contains(newMetadata.targetRole == null ? null : newMetadata.targetRole.hashCode());
				haveOlder = !haveAllRoles;
			}
			
			if (haveOlder) {
				for (KnowledgeManager replica : kmContainer.createReplica(newMetadata.componentId)) {
												
					try {
						ChangeSet changeSet = toChangeSet(kd.getKnowledge());
						knowledgeEncryptor.decryptChangeSet(changeSet, replica, kd.getMetaData());
						replica.update(changeSet, kd.getMetaData().componentId);			
					} catch (KnowledgeUpdateException e) {
						Log.w(String
								.format("KnowledgeDataManager.receive: Could not update replica of %s.",
										newMetadata.componentId), e);
					}					
				}
				
				// store the metadata without the knowledge values
				replicaMetadata.put(newMetadata.componentId, newMetadata);
				replicaRoles.get(newMetadata.getSignature()).add(newMetadata.targetRole == null ? null : newMetadata.targetRole.hashCode());
				
				// rebroadcast only data that is of some value (i.e., newer than we have)
				queueForRebroadcast(kd);
				
				if (Log.isDebugLoggable()) {
					Log.d(String.format("Receive (%d) at %s got %sv%d after %dms and %d hops\n", 
							timeProvider.getCurrentMilliseconds(), 
							host, 
							newMetadata.componentId, 
							newMetadata.versionId,
							timeProvider.getCurrentMilliseconds() - newMetadata.createdAt,
							newMetadata.hopCount));
				}
			
			}
			
		}
	}
	
	protected void sendDirect(List<KnowledgeData> data) {
		// Publishing to IP
		// For IP part we are using individual publishing only

		for (KnowledgeData kd : data) {
			Collection<String> recipients = ipGossipStrategy.getRecipients(kd,
					getNodeKnowledge());
			for (String recipient : recipients) {
				logPublish(data, recipient);
				dataSender.sendData(Arrays.asList(kd),
						recipient);
			}
		}
	}
	
	protected void queueForRebroadcast(KnowledgeData kd) {
		if (checkBoundaryCondition && !isInSomeBoundary(kd, getNodeKnowledge())) {
			if (Log.isDebugLoggable()) { 
				Log.d(String.format("Boundary failed (%d) at %s for %sv%d\n", 
					timeProvider.getCurrentMilliseconds(), host, kd.getMetaData().componentId, kd.getMetaData().versionId));
			}
			return;
		} 
		
		KnowledgeMetaData kmd = kd.getMetaData();
		int delay = getManetRebroadcastDelay(kmd);
		
		
		// delay < 0 indicates not rebroadcasting
		if (delay < 0) {			
			return;
		}
		
		if (Log.isDebugLoggable()) {
			Log.d(String.format(
					"Gossip rebroadcast (%d) at %s for %sv%d from %s with rssi %g with delay %d\n",
					timeProvider.getCurrentMilliseconds(), host,
					kmd.componentId,
					kmd.versionId, kmd.sender,
					kmd.rssi, delay));
		}
		
		// schedule a task for rebroadcast
		dataToRebroadcastOverMANET.put(kmd.getSignature(), kd);
		RebroadcastTask task = new RebroadcastTask(scheduler, this, delay, kmd, NICType.MANET);
		scheduler.addTask(task);
		
		if (ipGossipStrategy != null && ipDelay > 0) {
			dataToRebroadcastOverIP.put(kmd.getSignature(), kd);
			task = new RebroadcastTask(scheduler, this, ipDelay, kmd, NICType.IP);
			scheduler.addTask(task);
		}
		
	}
	

	protected boolean isInSomeBoundary(KnowledgeData data, KnowledgeManager sender) {
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
	
	protected int getManetRebroadcastDelay(KnowledgeMetaData metaData) {
		// rssi < 0 means received from IP
		if (metaData.rssi < 0) {
			if (Log.isDebugLoggable()) 
				Log.d("Got data from IP. Gossip condition does not apply, rebroadcasting automatically.");
			
			return ipDelay;
		}
		
		// the further further from the source (i.e. smaller rssi) the bigger
		// probability to of rebroadcast (i.e., the smaller delay)
		double rssi = Math.max(RSSI_MIN, metaData.rssi);
		double ratio = Math.abs(Math.log(rssi)/Math.log(RSSI_MIN));	
		
		int maxDelay = (int) ((1-ratio) * maxRebroadcastDelay);
		
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

	protected RatingsData prepareRatingsData() {
		List<RatingsChangeSet> changeSets = ratingsManager.getPendingChangeSets();
		RatingsMetaData metaData = createRatingsMetaData();
		RatingsData ratingsData = new RatingsData(ratingsEncryptor.encryptRatings(changeSets, metaData), metaData);
		
		ratingsManager.getPendingChangeSets().clear();
		return ratingsData;
	}
	
	protected List<KnowledgeData> prepareLocalKnowledgeData() {
		List<KnowledgeData> result = new LinkedList<>();
		for (KnowledgeManager km : kmContainer.getLocals()) {
			try {
				List<KnowledgeData> kds = prepareLocalKnowledgeData(km).stream().map(this::filterLocalKnowledgeForKnownEnsembles).collect(Collectors.toList());				
				result.addAll(kds);				
			} catch (Exception e) {
				Log.e("prepareKnowledgeData error", e);
			}
		}
		return result;
	}
	

	protected List<KnowledgeData> prepareLocalKnowledgeData(KnowledgeManager km)
			throws KnowledgeNotFoundException {
		// extract local knowledge
		ValueSet basicValueSet = getNonLocalKnowledge(km.get(emptyPath), km);
		KnowledgeMetaData metaData = createKnowledgeMetaData(km);
		return knowledgeEncryptor.encryptValueSet(basicValueSet, km, metaData);
	}

	protected KnowledgeMetaData createKnowledgeMetaData(KnowledgeManager km) {
		return new KnowledgeMetaData(km.getId(), localVersion, host, timeProvider.getCurrentMilliseconds(), 1);
	}
	
	protected RatingsMetaData createRatingsMetaData() {
		return new RatingsMetaData(timeProvider.getCurrentMilliseconds(), 1);
	}
	
	protected ValueSet getNonLocalKnowledge(ValueSet toFilter, KnowledgeManager km) {
		ValueSet result = new ValueSet();
		for (KnowledgePath kp: toFilter.getKnowledgePaths()) {
			if (!km.isLocal(kp)) {
				result.setValue(kp, toFilter.getValue(kp));
			}
		}
		return result;
	}
	
	protected KnowledgeData prepareForRebroadcast(KnowledgeData receivedData) {		
		KnowledgeMetaData kmdCopy = receivedData.getMetaData().clone();
		kmdCopy.sender = host;
		kmdCopy.hopCount++;
		return new KnowledgeData(receivedData.getKnowledge(), kmdCopy);
	}
	
	protected KnowledgeData filterLocalKnowledgeForKnownEnsembles(KnowledgeData kd) {
		// FIXME: make this generic
		// now we hardcode our demo (we filter the Leader knowledge to only
		// publish id, team, and position.
		if (kd.getMetaData().componentId.startsWith("L")) {
			ValueSet values = kd.getKnowledge();
			ValueSet newValues = new ValueSet();
			for (KnowledgePath kp: values.getKnowledgePaths()) {
				newValues.setValue(kp, values.getValue(kp));
			}
			return new KnowledgeData(newValues, kd.getMetaData());
		} else {
			return kd;
		}
	}

	protected KnowledgeManager getNodeKnowledge() {
		// FIXME: in the future, we need to unify the knowledge of all the local KMs.
		return kmContainer.getLocals().iterator().next();
	}

	protected ChangeSet toChangeSet(ValueSet valueSet) {
		if (valueSet != null) {
			ChangeSet result = new ChangeSet();
			for (KnowledgePath kp : valueSet.getKnowledgePaths())
				result.setValue(kp, valueSet.getValue(kp));
			return result;
		} else {
			return null;
		}
	}
	
	protected void logPublish(List<? extends KnowledgeData> data) {
		logPublish(data, "");
	}
	
	protected void logPublish(List<? extends KnowledgeData> data, String recipient) {
		if (Log.isDebugLoggable()) {
			StringBuilder sb = new StringBuilder();
			for (KnowledgeData kd: data) {
				sb.append(kd.getMetaData().componentId + "v" + kd.getMetaData().versionId);
				sb.append(", ");			
			}		
			if (recipient != null && !recipient.isEmpty())
				if (Log.isDebugLoggable()) {
					Log.d(String.format("Publish (%d) at %s, sending [%s] directly to %s\n", 
							timeProvider.getCurrentMilliseconds(), host, sb.toString(), recipient));
				}

			else
				if (Log.isDebugLoggable()) {
					Log.d(String.format("Publish (%d) at %s, sending [%s]\n", 
							timeProvider.getCurrentMilliseconds(), host, sb.toString()));
				}
		}
	}
	
	protected void logReceive(List<? extends KnowledgeData> knowledgeData) {
		if (Log.isDebugLoggable()) {
			StringBuilder sb = new StringBuilder();
			for (KnowledgeData kd: knowledgeData) {
				sb.append(kd.getMetaData().componentId + "v" + kd.getMetaData().versionId + "<-" + kd.getMetaData().sender);
				sb.append(", ");			
			}

			if (Log.isDebugLoggable()) {
				Log.d(String.format("Receive (%d) at %s, received [%s]\n", 
						timeProvider.getCurrentMilliseconds(), host, sb.toString()));
			}
		}
	}

}
