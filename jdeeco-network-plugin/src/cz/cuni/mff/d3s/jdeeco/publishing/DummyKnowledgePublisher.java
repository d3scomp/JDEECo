package cz.cuni.mff.d3s.jdeeco.publishing;

import java.util.Arrays;
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
public class DummyKnowledgePublisher implements DEECoPlugin, TimerTaskListener {
	private Network network;
	private KnowledgeManagerContainer knowledgeManagerContainer;
	private CurrentTimeProvider timeProvider;
	private DEECoContainer container;
	private List<IPAddress> infrastructurePeers;
	
	private final static long PUBLISH_PERIOD = Integer.getInteger(DeecoProperties.PUBLISHING_PERIOD,
			PublisherTask.DEFAULT_PUBLISHING_PERIOD);

	@Override
	public List<Class<? extends DEECoPlugin>> getDependencies() {
		return Arrays.asList(Network.class);
	}

	public DummyKnowledgePublisher() {
		this(new LinkedList<IPAddress>());
	}

	public DummyKnowledgePublisher(List<IPAddress> peers) {
		infrastructurePeers = peers;
		RuntimeMetadataFactory factory = RuntimeMetadataFactoryExt.eINSTANCE;
		KnowledgePath empty = factory.createKnowledgePath();
		emptyPath = new LinkedList<>();
		emptyPath.add(empty);
	}

	// NOTE: Taken from DefaultKnowledgeDataManager
	protected final List<KnowledgePath> emptyPath;

	// NOTE: Taken from DefaultKnowledgeDataManager
	protected List<KnowledgeData> prepareLocalKnowledgeData() {
		List<KnowledgeData> result = new LinkedList<>();
		for (KnowledgeManager km : knowledgeManagerContainer.getLocals()) {
			try {
				KnowledgeData kd = prepareLocalKnowledgeData(km);
				result.add(filterLocalKnowledgeForKnownEnsembles(kd));
			} catch (Exception e) {
				Log.e("prepareKnowledgeData error", e);
			}
		}
		return result;
	}

	// NOTE: Taken from DefaultKnowledgeDataManager
	protected KnowledgeData prepareLocalKnowledgeData(KnowledgeManager km) throws KnowledgeNotFoundException {

		// TODO: We are ignoring security, and host
		// TODO: version is implemented by current time
		long time = timeProvider.getCurrentMilliseconds();
		return new KnowledgeData(getNonLocalKnowledge(km.get(emptyPath), km), new ValueSet(), new ValueSet(),
				new KnowledgeMetaData(km.getId(), time, String.valueOf(container.getId()), time, 1));
	}

	// NOTE: Taken from DefaultKnowledgeDataManager
	protected ValueSet getNonLocalKnowledge(ValueSet toFilter, KnowledgeManager km) {
		ValueSet result = new ValueSet();
		for (KnowledgePath kp : toFilter.getKnowledgePaths()) {
			if (!km.isLocal(kp)) {
				result.setValue(kp, toFilter.getValue(kp));
			}
		}
		return result;
	}

	// NOTE: Taken from DefaultKnowledgeDataManager
	protected KnowledgeData filterLocalKnowledgeForKnownEnsembles(KnowledgeData kd) {
		// FIXME: make this generic
		// now we hardcode our demo (we filter the Leader knowledge to only
		// publish id, team, and position.
		if (kd.getMetaData().componentId.startsWith("L")) {
			ValueSet values = kd.getKnowledge();
			ValueSet newValues = new ValueSet();
			for (KnowledgePath kp : values.getKnowledgePaths()) {
				newValues.setValue(kp, values.getValue(kp));
			}
			return new KnowledgeData(newValues, kd.getSecuritySet(), kd.getAuthors(), kd.getMetaData());
		} else {
			return kd;
		}
	}

	@Override
	public void at(long time, Object triger) {
		System.out.println("Publisher called at: " + time);

		// Get knowledge and distribute it
		for (KnowledgeData data : prepareLocalKnowledgeData()) {
			L2Packet packet = new L2Packet(new PacketHeader(L2PacketType.KNOWLEDGE), data);

			// Distribute via broadcast
			network.getL2().sendL2Packet(packet, MANETBroadcastAddress.BROADCAST);

			// Distribute via infrastructure network
			for (IPAddress address : getPeers()) {
				network.getL2().sendL2Packet(packet, address);
			}
		}

		Scheduler scheduler = container.getRuntimeFramework().getScheduler();
		scheduler.addTask(new CustomStepTask(scheduler, this, PUBLISH_PERIOD));
	}

	@Override
	public TimerTask getInitialTask(Scheduler scheduler) {
		return new CustomStepTask(scheduler, this, Integer.getInteger(DeecoProperties.PUBLISHING_PERIOD,
				PublisherTask.DEFAULT_PUBLISHING_PERIOD));
	}

	/**
	 * Gets list of infrastructure peers
	 */
	protected List<IPAddress> getPeers() {
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
		long offset = new Random(container.getId()).nextInt((int) PUBLISH_PERIOD);
		scheduler.addTask(new CustomStepTask(scheduler, this, offset));
	}
}
