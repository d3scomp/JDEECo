package cz.cuni.mff.d3s.jdeeco.network.PacketTypes;

/**
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public class KnowledgePacketType extends PacketType {
	public KnowledgePacketType() {
		// Knowledge packet type is special, it uses "privileged" value of 0
		super(0);
	}
	
	public static KnowledgePacketType instance = new KnowledgePacketType();
}
