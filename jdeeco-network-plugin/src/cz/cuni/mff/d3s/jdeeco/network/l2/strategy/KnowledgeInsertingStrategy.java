package cz.cuni.mff.d3s.jdeeco.network.l2.strategy;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cz.cuni.mff.d3s.deeco.knowledge.ChangeSet;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManagerContainer;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeUpdateException;
import cz.cuni.mff.d3s.deeco.knowledge.ValueSet;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.network.KnowledgeData;
import cz.cuni.mff.d3s.deeco.network.KnowledgeMetaData;
import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;
import cz.cuni.mff.d3s.deeco.scheduler.CurrentTimeProvider;
import cz.cuni.mff.d3s.jdeeco.network.Network;
import cz.cuni.mff.d3s.jdeeco.network.l2.L2Packet;
import cz.cuni.mff.d3s.jdeeco.network.l2.L2Strategy;

/**
 * L2 Strategy and DEECo plug-in for incorporating received knowledge data to knowledge managers
 * 
 * This is implementation is not complete, it just imports old behavior and do not implement security
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public class KnowledgeInsertingStrategy implements L2Strategy, DEECoPlugin {
	private KnowledgeManagerContainer knowledgeManagerContainer;
	private CurrentTimeProvider timeProvider;

	@Override
	public void processL2Packet(L2Packet packet) {

	}

	public KnowledgeInsertingStrategy() {
		// NOTE: Taken from DefaultKnowledgeDataManager
		replicaMetadata = new HashMap<>();
	}

	// NOTE: Taken from DefaultKnowledgeDataManager
	/** Stores received KnowledgeMetaData for replicas (received ValueSet is deleted) */
	protected final Map<String, KnowledgeMetaData> replicaMetadata;

	// NOTE: Taken from DefaultKnowledgeDataManager
	public void receiveKnowledge(List<KnowledgeData> data) {
		for (KnowledgeData kd : data) {
			KnowledgeMetaData newMetadata = kd.getMetaData();
			if (knowledgeManagerContainer.hasLocal(newMetadata.componentId)) {
				if (Log.isDebugLoggable())
					Log.d("KnowledgeDataManager.receive: Dropping KnowledgeData for local component "
							+ newMetadata.componentId);
				continue;
			}

			KnowledgeMetaData currentMetadata = replicaMetadata.get(newMetadata.componentId);

			// Accept only fresh knowledge data (drop if we have already a newer value)
			if ((currentMetadata == null) || (currentMetadata.versionId < newMetadata.versionId)) {
				for (KnowledgeManager replica : knowledgeManagerContainer.createReplica(newMetadata.componentId)) {
					try {
						Map<String, ChangeSet> changeSets = toChangeSets(kd.getKnowledge(), null, null);
						for (Entry<String, ChangeSet> entry : changeSets.entrySet()) {
							replica.update(entry.getValue(), entry.getKey());
						}
					} catch (KnowledgeUpdateException e) {
						Log.w(String.format("KnowledgeDataManager.receive: Could not update replica of %s.",
								newMetadata.componentId), e);
					}

					// store the metadata without the knowledge values
					replicaMetadata.put(newMetadata.componentId, newMetadata);

					if (Log.isDebugLoggable()) {
						Log.d(String.format("Receive (%d) at %s got %sv%d after %dms and %d hops\n",
								timeProvider.getCurrentMilliseconds(), null, newMetadata.componentId,
								newMetadata.versionId, timeProvider.getCurrentMilliseconds() - newMetadata.createdAt,
								newMetadata.hopCount));
					}
				}
			}
		}
	}

	// NOTE: Taken from DefaultKnowledgeDataManager
	private Map<String, ChangeSet> toChangeSets(ValueSet knowledge, ValueSet authors, KnowledgeMetaData metaData) {
		if (knowledge != null) {
			Map<String, ChangeSet> result = new HashMap<>();

			for (KnowledgePath kp : knowledge.getKnowledgePaths()) {
				String author = (String) authors.getValue(kp);
				if (author == null)
					author = metaData.componentId;

				if (!result.containsKey(author)) {
					result.put(author, new ChangeSet());
				}

				result.get(author).setValue(kp, knowledge.getValue(kp));
			}

			return result;
		} else {
			return null;
		}
	}

	@Override
	public List<Class<? extends DEECoPlugin>> getDependencies() {
		return Arrays.asList(Network.class);
	}

	@Override
	public void init(DEECoContainer container) {
		// Resolve dependencies
		knowledgeManagerContainer = container.getRuntimeFramework().getContainer();
		Network network = container.getPluginInstance(Network.class);
		timeProvider = container.getRuntimeFramework().getScheduler();

		// Register as network L2 strategy
		network.getL2().registerL2Strategy(this);
	}
}
