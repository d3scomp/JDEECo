package cz.cuni.mff.d3s.jdeeco.ros.datatypes;

public enum LedId {
	LED1("/mobile_base/commands/led1"),
	LED2("/mobile_base/commands/led2");
	
	public final String topic;
	
	private LedId(String topic){
		this.topic = topic;
	}
}
