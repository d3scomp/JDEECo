package cz.cuni.mff.d3s.jdeeco.ros;

import org.ros.node.ConnectedNode;

public class Communication extends TopicSubscriber {
	
	Communication(){}
	
	@Override
	void subscribe(ConnectedNode connectedNode) {
		// TODO Auto-generated method stub
		
	}

}

/* TODO:
 
* /joint_states
* /mobile_base/commands/controller_info
+ /mobile_base/commands/reset_odometry
* /mobile_base/controller_info
+ /mobile_base/events/button
+ /mobile_base/events/cliff
* /mobile_base/events/power_system
* /mobile_base/events/robot_state
* /mobile_base/sensors/core
* /mobile_base/sensors/imu_data
+ /mobile_base/version_info
* /mobile_base_nodelet_manager/bond
* /rosout
* /rosout_agg
* /tf
 
*/