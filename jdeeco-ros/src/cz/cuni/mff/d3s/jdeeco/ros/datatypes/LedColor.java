package cz.cuni.mff.d3s.jdeeco.ros.datatypes;

import kobuki_msgs.Led;

public enum LedColor {
	RED(Led.RED),
	GREEN(Led.GREEN),
	ORANGE(Led.ORANGE),
	BLACK(Led.BLACK);
	
	public final byte value;
	
	private LedColor(byte value){
		this.value = value;
	}
}
