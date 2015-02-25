package cz.cuni.mff.d3s.jdeeco.network;

import java.util.LinkedList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;
import cz.cuni.mff.d3s.jdeeco.network.l1.DefaultDataIDSource;
import cz.cuni.mff.d3s.jdeeco.network.l1.Layer1;
import cz.cuni.mff.d3s.jdeeco.network.l2.L2PacketType;
import cz.cuni.mff.d3s.jdeeco.network.l2.Layer2;
import cz.cuni.mff.d3s.jdeeco.network.marshaller.MarshallerRegistry;
import cz.cuni.mff.d3s.jdeeco.network.marshaller.SerializingMarshaller;

/**
 * Provides network plug-in for jDEECO
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public class Network implements DEECoPlugin {
	private Layer1 l1;
	private Layer2 l2;
	private MarshallerRegistry registery = new MarshallerRegistry();

	public Layer1 getL1() {
		return l1;
	}

	public Layer2 getL2() {
		return l2;
	}

	@Override
	public List<Class<? extends DEECoPlugin>> getDependencies() {
		return new LinkedList<Class<? extends DEECoPlugin>>();
	}

	@Override
	public void init(DEECoContainer container) {
		// Initialize Layer 1
		// TODO: Data id source and node id should have been set properly
		l1 = new Layer1(l2, (byte) 0, DefaultDataIDSource.getInstance());

		// Initialize Layer 2
		l2 = new Layer2(l1, registery);

		// Add default marshaler for knowledge
		registery.registerMarshaller(L2PacketType.KNOWLEDGE, new SerializingMarshaller());
	}
}
