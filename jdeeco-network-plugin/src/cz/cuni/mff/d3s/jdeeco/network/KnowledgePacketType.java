package cz.cuni.mff.d3s.jdeeco.network;

public class KnowledgePacketType implements PacketType {
	public String name() {
		return KnowledgePacketType.class.toString();
	}

	public int ordinal() {
		// TODO: This is dummy, we want to use some registry for assigning number to KnowledgePacketType
		return 0;
	}
}
