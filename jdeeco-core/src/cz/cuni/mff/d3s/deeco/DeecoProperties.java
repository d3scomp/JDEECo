package cz.cuni.mff.d3s.deeco;

public final class DeecoProperties {
	public static final String PACKET_SIZE = "deeco.publish.packetsize";
	public static final String PUBLISHING_PERIOD = "deeco.publish.period";
	public static final String USE_INDIVIDUAL_KNOWLEDGE_PUBLISHING = "deeco.publish.individual";	
	public static final String DISABLE_BOUNDARY_CONDITIONS = "deeco.boundary.disable";	
	public static final String DISABLE_GOSSIP_CONDITION = "deeco.gossipcnd.disable";
	public static final String GOSSIP_CONDITION_RSSI = "deeco.gossipcnd.rssi";
	public static final String IP_REBROADCAST_DELAY = "deeco.rebroadcast.ipdelay";
	public static final String MESSAGE_CACHE_DEADLINE = "deeco.receive.cache.deadline";
	public static final String MESSAGE_CACHE_WIPE_PERIOD = "deeco.receive.cache.period";
	public static final String MAXIMUM_REBROADCAST_DELAY = "deeco.rebroadcast.delay";
	public static final String SIGN_PLAINTEXT_MESSAGES = "deeco.security.sign_plaintext_messages";
	public static final String VERIFY_JARS = "deeco.security.verify_secured_component_jars";
}
