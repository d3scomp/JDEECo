package cz.cuni.mff.d3s.jdeeco.publishing;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.DeecoProperties;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManagerContainer;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeNotFoundException;
import cz.cuni.mff.d3s.deeco.knowledge.ValueSet;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.TimeTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;
import cz.cuni.mff.d3s.deeco.model.runtime.custom.RuntimeMetadataFactoryExt;
import cz.cuni.mff.d3s.deeco.model.runtime.custom.TimeTriggerExt;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataFactory;
import cz.cuni.mff.d3s.deeco.network.KnowledgeData;
import cz.cuni.mff.d3s.deeco.network.KnowledgeMetaData;
import cz.cuni.mff.d3s.deeco.network.PublisherTask;
import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;
import cz.cuni.mff.d3s.deeco.scheduler.CurrentTimeProvider;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;
import cz.cuni.mff.d3s.deeco.task.Task;
import cz.cuni.mff.d3s.deeco.task.TaskInvocationException;
import cz.cuni.mff.d3s.jdeeco.network.L2PacketType;
import cz.cuni.mff.d3s.jdeeco.network.MANETBroadcastAddress;
import cz.cuni.mff.d3s.jdeeco.network.Network;
import cz.cuni.mff.d3s.jdeeco.network.l2.L2Packet;
import cz.cuni.mff.d3s.jdeeco.network.l2.PacketHeader;

/**
 * Dummy implementation of knowledge publishing
 * 
 * This uses only broadcast and ignores security
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public class DummyKnowledgePublisher implements DEECoPlugin {

	class PublishTask extends Task {
		private final TimeTrigger trigger;

		public PublishTask(Scheduler scheduler, TimeTrigger trigger) {
			super(scheduler);
			this.trigger = trigger;
		}

		@Override
		public void invoke(Trigger trigger) throws TaskInvocationException {
			DummyKnowledgePublisher.this.publish();
		}

		@Override
		protected void registerTriggers() {
		}

		@Override
		protected void unregisterTriggers() {
		}

		@Override
		public TimeTrigger getTimeTrigger() {
			return trigger;
		}
	}

	private Network network;
	private KnowledgeManagerContainer knowledgeManagerContainer;
	private CurrentTimeProvider timeProvider;

	@Override
	public List<Class<? extends DEECoPlugin>> getDependencies() {
		return Arrays.asList(Network.class);
	}

	public DummyKnowledgePublisher() {
		RuntimeMetadataFactory factory = RuntimeMetadataFactoryExt.eINSTANCE;
		KnowledgePath empty = factory.createKnowledgePath();
		emptyPath = new LinkedList<>();
		emptyPath.add(empty);
	}

	protected final List<KnowledgePath> emptyPath;

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

	protected KnowledgeData prepareLocalKnowledgeData(KnowledgeManager km) throws KnowledgeNotFoundException {

		// TODO: We are ignoring security, version and host
		return new KnowledgeData(getNonLocalKnowledge(km.get(emptyPath), km), null, null, new KnowledgeMetaData(
				km.getId(), 0xfa4e, "host here", timeProvider.getCurrentMilliseconds(), 1));
	}

	protected KnowledgeMetaData createKnowledgeMetaData(KnowledgeManager km) {

		// TODO: Fake values here
		return new KnowledgeMetaData(km.getId(), 0, "ID", timeProvider.getCurrentMilliseconds(), 1);
	}

	protected ValueSet getNonLocalKnowledge(ValueSet toFilter, KnowledgeManager km) {
		ValueSet result = new ValueSet();
		for (KnowledgePath kp : toFilter.getKnowledgePaths()) {
			if (!km.isLocal(kp)) {
				result.setValue(kp, toFilter.getValue(kp));
			}
		}
		return result;
	}

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

	protected void publish() {
		System.out.println("PUBLISHER CALLED");

		Object data = prepareLocalKnowledgeData();
		network.getL2().sendL2Packet(new L2Packet(new PacketHeader(L2PacketType.KNOWLEDGE), data),
				MANETBroadcastAddress.INSTANCE);
	}

	@Override
	public void init(DEECoContainer container) {
		// Resolve dependencies
		network = container.getPluginInstance(Network.class);
		knowledgeManagerContainer = container.getRuntimeFramework().getContainer();

		timeProvider = container.getRuntimeFramework().getScheduler();

		// Start publishing task
		TimeTrigger publisherTrigger = new TimeTriggerExt();
		publisherTrigger.setPeriod(Integer.getInteger(DeecoProperties.PUBLISHING_PERIOD,
				PublisherTask.DEFAULT_PUBLISHING_PERIOD));
		PublishTask publisher = this.new PublishTask(container.getRuntimeFramework().getScheduler(), publisherTrigger);
		container.getRuntimeFramework().getScheduler().addTask(publisher);
	}
}
