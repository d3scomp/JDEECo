package cz.cuni.mff.d3s.jdeeco.network.l2.strategy;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import cz.cuni.mff.d3s.deeco.timer.CurrentTimeProvider;
import cz.cuni.mff.d3s.jdeeco.network.Network;
import cz.cuni.mff.d3s.jdeeco.network.l2.L2Packet;
import cz.cuni.mff.d3s.jdeeco.network.l2.L2Strategy;

/**
 * L2 Strategy and DEECo plug-in for incorporating received knowledge data to knowledge managers
 * 
 * This is implementation is not complete, it does not implement security
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public class KnowledgeInsertingStrategy implements L2Strategy, DEECoPlugin {
	private KnowledgeManagerContainer knowledgeManagerContainer;
	private CurrentTimeProvider timeProvider;

	/**
	 * Keeps track of versions of the knowledge currently stored
	 */
	private Map<String, Long> currentVersions = new HashMap<String, Long>();

	@Override
	public void processL2Packet(L2Packet packet) {
		Object data = packet.getObject();

		// TODO: Type safety
		if (!(data instanceof KnowledgeData)) {
			throw new UnsupportedOperationException(data.getClass().getName() + " is not a knowledge data");
		}

		receiveKnowledge((KnowledgeData) data);
	}

	/**
	 * Incorporates knowledge into knowledge managers
	 * 
	 * @param knowledgeData
	 *            Knowledge data to be incorporated
	 */
	public void receiveKnowledge(KnowledgeData knowledgeData) {
		KnowledgeMetaData newMetadata = knowledgeData.getMetaData();
		if (knowledgeManagerContainer.hasLocal(newMetadata.componentId)) {
			if (Log.isDebugLoggable())
				Log.d("KnowledgeDataManager.receive: Dropping KnowledgeData for local component "
						+ newMetadata.componentId);
			return;
		}

		// Accept only fresh knowledge data (drop if we have already a newer value)
		Long currentVersion = currentVersions.get(newMetadata.componentId);
		if ((currentVersion == null) || (currentVersion < newMetadata.versionId)) {
			for (KnowledgeManager replica : knowledgeManagerContainer.createReplica(newMetadata.componentId,
					toRoleArray(knowledgeData.getRoleClasses(), newMetadata.componentId))) {
				try {
					replica.update(toChangeSet(knowledgeData.getKnowledge()));
				} catch (KnowledgeUpdateException e) {
					Log.w(String.format("KnowledgeDataManager.receive: Could not update replica of %s.",
							newMetadata.componentId), e);
				}

				// Update current version
				currentVersions.put(newMetadata.componentId, newMetadata.versionId);

				if (Log.isDebugLoggable()) {
					Log.d(String.format("Receive (%d) at %s got %sv%d after %dms and %d hops\n",
							timeProvider.getCurrentMilliseconds(), null, newMetadata.componentId,
							newMetadata.versionId, timeProvider.getCurrentMilliseconds() - newMetadata.createdAt,
							newMetadata.hopCount));
				}
			}
		}
	}

	private Class<?>[] toRoleArray(List<String> roleClasses, String componentId) {
		Class<?>[] result = new Class<?>[roleClasses.size()];
		int i = 0;
		for(String roleClassName : roleClasses) {
			try {
				result[i++] = Class.forName(roleClassName);
			} catch (ClassNotFoundException e) {
				Log.w(String.format("Role class '%s' declared by component with id '%s' does not exist.",
						roleClassName, componentId));
			}
		}
		
		return result;
	}

	/**
	 * Converts value set to change set
	 * 
	 * @param knowledge
	 *            Knowledge
	 * @return Change set composed from input knowledge
	 */
	private ChangeSet toChangeSet(ValueSet knowledge) {
		ChangeSet result = new ChangeSet();

		for (KnowledgePath kp : knowledge.getKnowledgePaths()) {
			result.setValue(kp, knowledge.getValue(kp));
		}

		return result;
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
		timeProvider = container.getRuntimeFramework().getScheduler().getTimer();

		// Register as network L2 strategy
		network.getL2().registerL2Strategy(this);
	}
}
