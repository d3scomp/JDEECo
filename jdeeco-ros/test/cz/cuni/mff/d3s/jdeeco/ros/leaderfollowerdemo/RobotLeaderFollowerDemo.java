package cz.cuni.mff.d3s.jdeeco.ros.leaderfollowerdemo;

import java.io.IOException;

import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.runners.DEECoSimulation;
import cz.cuni.mff.d3s.deeco.runtime.DEECoException;
import cz.cuni.mff.d3s.deeco.runtime.DEECoNode;
import cz.cuni.mff.d3s.jdeeco.network.Network;
import cz.cuni.mff.d3s.jdeeco.network.l2.strategy.KnowledgeInsertingStrategy;
import cz.cuni.mff.d3s.jdeeco.position.PositionPlugin;
import cz.cuni.mff.d3s.jdeeco.publishing.DefaultKnowledgePublisher;
import cz.cuni.mff.d3s.jdeeco.ros.BeeClick;
import cz.cuni.mff.d3s.jdeeco.ros.Positioning;
import cz.cuni.mff.d3s.jdeeco.ros.sim.ROSSimulation;

/**
 * Example of vehicles traveling across the map
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public class RobotLeaderFollowerDemo {
	public static void main(String[] args) throws AnnotationProcessorException, InterruptedException, DEECoException,
			InstantiationException, IllegalAccessException, IOException {
		new RobotLeaderFollowerDemo().testTravel();
	}

	public void testTravel() throws AnnotationProcessorException, InterruptedException, DEECoException,
			InstantiationException, IllegalAccessException, IOException {
	
		ROSSimulation rosSim = new ROSSimulation("192.168.56.101", 11311, "192.168.56.1", "corridor", 0.02, 100);

		// Create main application container
		DEECoSimulation realm = new DEECoSimulation(rosSim.getTimer());
		
		// Configure loop-back networking for all nodes
		realm.addPlugin(Network.class);
		realm.addPlugin(DefaultKnowledgePublisher.class);
		realm.addPlugin(KnowledgeInsertingStrategy.class);
		realm.addPlugin(BeeClick.class);
		
		Positioning robot0Pos = new Positioning();
		DEECoNode robot0 = realm.createNode(0, robot0Pos, rosSim.createROSServices("red"), new PositionPlugin(12, 12));
		robot0.deployComponent(new LeaderRobot("robot_0", robot0Pos, rosSim.getTimer()));
		robot0.deployEnsemble(LeaderFollowerEnsemble.class);
		
		Positioning robot1Pos = new Positioning();
		DEECoNode robot1 = realm.createNode(1, robot1Pos, rosSim.createROSServices("blue"), new PositionPlugin(26, 12));
		robot1.deployComponent(new FollowerRobot("robot_1", robot1Pos, rosSim.getTimer()));
		robot1.deployEnsemble(LeaderFollowerEnsemble.class);
		
		// Simulate for specified time
		realm.start(3000_000);
			
		System.out.println("!#!@!#!@!#@!@#!@#!@#!#!@!#!@!#@!@#!@#!@#!#!@!#!@!#@!@#!@#!@#!#!@!#!@!#@!@#!@#!@#!#!@!#!@!#@!@#!@#!@#!#!@!#!@!#@!@#");
		System.out.println("!@!#!@!#@!@#!@#!@# As we cannot make ROS exit nicely we are now going to terminate the whole JVM !@#!@#!@#!@#!@#!@#");
		System.out.println("!#!@!#!@!#@!@#!@#!@#!#!@!#!@!#@!@#!@#!@#!#!@!#!@!#@!@#!@#!@#!#!@!#!@!#@!@#!@#!@#!#!@!#!@!#@!@#!@#!@#!#!@!#!@!#@!@#");
		System.exit(0);
	}
}
