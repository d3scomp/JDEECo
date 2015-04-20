package cz.cuni.mff.d3s.jdeeco.publishing;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import cz.cuni.mff.d3s.deeco.DeecoProperties;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManagerContainer;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeNotFoundException;
import cz.cuni.mff.d3s.deeco.knowledge.ValueSet;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.custom.RuntimeMetadataFactoryExt;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataFactory;
import cz.cuni.mff.d3s.deeco.network.KnowledgeData;
import cz.cuni.mff.d3s.deeco.network.KnowledgeMetaData;
import cz.cuni.mff.d3s.deeco.network.PublisherTask;
import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;
import cz.cuni.mff.d3s.deeco.task.CustomStepTask;
import cz.cuni.mff.d3s.deeco.task.TimerTask;
import cz.cuni.mff.d3s.deeco.task.TimerTaskListener;
import cz.cuni.mff.d3s.deeco.timer.CurrentTimeProvider;
import cz.cuni.mff.d3s.jdeeco.network.Network;
import cz.cuni.mff.d3s.jdeeco.network.address.IPAddress;
import cz.cuni.mff.d3s.jdeeco.network.address.MANETBroadcastAddress;
import cz.cuni.mff.d3s.jdeeco.network.l2.L2Packet;
import cz.cuni.mff.d3s.jdeeco.network.l2.L2PacketType;
import cz.cuni.mff.d3s.jdeeco.network.l2.PacketHeader;

/**
 * Dummy implementation of knowledge publishing
 * 
 * This uses only broadcast and static list of IP peers, ignores security
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public class DefaultKnowledgePublisher implements DEECoPlugin, TimerTaskListener {
	public static final int DEFAULT_PUBLISHING_PERIOD = 1000;

	private final int publishingPeriod;
	private Network network;
	private KnowledgeManagerContainer knowledgeManagerContainer;
	private CurrentTimeProvider timeProvider;
	private DEECoContainer container;
	private List<IPAddress> infrastructurePeers;
	private final List<KnowledgePath> empty;

	/**
	 * Constructs DefaultKnowledgePublisher with broadcast only publishing and default publishing period
	 */
	public DefaultKnowledgePublisher() {
		this(Collections.emptyList(), DEFAULT_PUBLISHING_PERIOD);
	}

	/**
	 * Constructs DefaultKnowledgePublisher with default publishing period
	 */
	public DefaultKnowledgePublisher(List<IPAddress> peers) {
		this(peers, DEFAULT_PUBLISHING_PERIOD);
	}

	/**
	 * Constructs DefaultKnowledgePublisher with broadcast only publishing
	 */
	public DefaultKnowledgePublisher(int publishingPeriod) {
		this(Collections.emptyList(), publishingPeriod);
	}

	/**
	 * Constructs DefaultKnowledgePublisher with broadcast and static infrastructure publishing
	 * 
	 * @param peers
	 *            Infrastructure peers
	 */
	public DefaultKnowledgePublisher(List<IPAddress> peers, int publishingPeriod) {
		this.infrastructurePeers = peers;
		this.publishingPeriod = publishingPeriod;
		RuntimeMetadataFactory factory = RuntimeMetadataFactoryExt.eINSTANCE;
		empty = new LinkedList<>(Arrays.asList(factory.createKnowledgePath()));
	}

	/**
	 * Gets local knowledge data from all knowledge managers
	 * 
	 * @return Knowledge data set
	 */
	protected List<KnowledgeData> getLocalKnowledgeData() {
		List<KnowledgeData> result = new LinkedList<>();
		for (KnowledgeManager km : knowledgeManagerContainer.getLocals()) {
			try {
				result.add(getLocalKnowledgeData(km));
			} catch (Exception e) {
				Log.e("prepareKnowledgeData error", e);
			}
		}
		return result;
	}

	/**
	 * Gets local knowledge data from specific knowledge manager
	 * 
	 * @param km
	 *            KnowledgeManager to get data from
	 * @return Knowledge data
	 * @throws KnowledgeNotFoundException
	 */
	protected KnowledgeData getLocalKnowledgeData(KnowledgeManager km) throws KnowledgeNotFoundException {
		// TODO: version is implemented by current time
		long time = timeProvider.getCurrentMilliseconds();
		ValueSet knowledge = getTransferableKnowledge(km.get(empty), km);
		String id = String.valueOf(container.getId());
		return new KnowledgeData(knowledge, new ValueSet(), new ValueSet(), new KnowledgeMetaData(km.getId(), time, id,
				time, 1));
	}

	/**
	 * Gets knowledge that can be send via network (is not @Local annotated)
	 * 
	 * @param source
	 *            Knowledge values to be stripped of non-transferable knowledge
	 * @param knowledgeManager
	 *            Knowledge manager containing the source knowledge
	 * @return Source knowledge stripped of @Local annotated knowledge
	 */
	protected ValueSet getTransferableKnowledge(ValueSet source, KnowledgeManager knowledgeManager) {
		ValueSet result = new ValueSet();
		for (KnowledgePath kp : source.getKnowledgePaths()) {
			if (!(knowledgeManager.isLocal(kp) || knowledgeManager.isOfSystemComponent())) {
				result.setValue(kp, source.getValue(kp));
			}
		}
		return result;
	}

	@Override
	public void at(long time, Object triger) {
		Log.d("Publisher called at: " + time);

		// Get knowledge and distribute it
		for (KnowledgeData data : getLocalKnowledgeData()) {
			L2Packet packet = new L2Packet(new PacketHeader(L2PacketType.KNOWLEDGE), data);

			// Distribute via broadcast
			network.getL2().sendL2Packet(packet, MANETBroadcastAddress.BROADCAST);

			// Distribute via infrastructure network
			for (IPAddress address : getPeers(data)) {
				network.getL2().sendL2Packet(packet, address);
			}
		}

		Scheduler scheduler = container.getRuntimeFramework().getScheduler();
		scheduler.addTask(new CustomStepTask(scheduler, this, publishingPeriod));
	}

	@Override
	public TimerTask getInitialTask(Scheduler scheduler) {
		return new CustomStepTask(scheduler, this, Integer.getInteger(DeecoProperties.PUBLISHING_PERIOD,
				PublisherTask.DEFAULT_PUBLISHING_PERIOD));
	}

	@Override
	public List<Class<? extends DEECoPlugin>> getDependencies() {
		return Arrays.asList(Network.class);
	}

	/**
	 * Gets list of infrastructure peers
	 */
	protected List<IPAddress> getPeers(KnowledgeData data) {
		return infrastructurePeers;
	}

	@Override
	public void init(DEECoContainer container) {
		// Resolve dependencies
		network = container.getPluginInstance(Network.class);
		knowledgeManagerContainer = container.getRuntimeFramework().getContainer();
		this.container = container;

		timeProvider = container.getRuntimeFramework().getScheduler().getTimer();

		// Start publishing task
		Scheduler scheduler = container.getRuntimeFramework().getScheduler();
		long offset = new Random(container.getId()).nextInt(publishingPeriod);
		scheduler.addTask(new CustomStepTask(scheduler, this, offset));
	}
}
