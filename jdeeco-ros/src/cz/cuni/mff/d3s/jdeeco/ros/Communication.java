package cz.cuni.mff.d3s.jdeeco.ros;

import org.ros.node.ConnectedNode;

public class Communication extends TopicSubscriber {

	private static Communication INSTANCE;
	
	private Communication(){}
	
	public static Communication getInstance(){
		if(INSTANCE == null){
			INSTANCE = new Communication();
		}
		return INSTANCE;
	}
	
	@Override
	void subscribe(ConnectedNode connectedNode) {
		// TODO Auto-generated method stub
		
	}

}
