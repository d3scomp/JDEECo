package cz.cuni.mff.d3s.jdeeco.turtlebot.simpleexchange;

import java.net.InetAddress;
import java.util.Random;

import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.runtime.DEECoNode;
import cz.cuni.mff.d3s.deeco.timer.WallTimeTimer;
import cz.cuni.mff.d3s.jdeeco.network.Network;
import cz.cuni.mff.d3s.jdeeco.publishing.DefaultKnowledgePublisher;
import cz.cuni.mff.d3s.jdeeco.ros.BeeClickComm;
import cz.cuni.mff.d3s.jdeeco.ros.Bumper;
import cz.cuni.mff.d3s.jdeeco.ros.Buttons;
import cz.cuni.mff.d3s.jdeeco.ros.DockIR;
import cz.cuni.mff.d3s.jdeeco.ros.FloorDistance;
import cz.cuni.mff.d3s.jdeeco.ros.Info;
import cz.cuni.mff.d3s.jdeeco.ros.LEDs;
import cz.cuni.mff.d3s.jdeeco.ros.Position;
import cz.cuni.mff.d3s.jdeeco.ros.RosServices;
import cz.cuni.mff.d3s.jdeeco.ros.SHT1x;
import cz.cuni.mff.d3s.jdeeco.ros.Speeker;
import cz.cuni.mff.d3s.jdeeco.ros.Wheels;

public class Run {

	private static final String SENSE_SWITCH = "sense";
	private static final String RECEIVE_SWITCH = "receive";

	public static void main(String[] args) {
		if (args.length != 1
				|| (!SENSE_SWITCH.equals(args[0]) 
						&& !RECEIVE_SWITCH.equals(args[0]))) {
			Log.e("Turtlebot Simple Exchange run without a correct parameter.");
			System.out
					.println(String
							.format("The Run class takes one argument with a value from {\"%s\", \"%s\"}",
									SENSE_SWITCH, RECEIVE_SWITCH));
			System.exit(1);
		}

		Random rand = new Random();
		WallTimeTimer t = new WallTimeTimer();
		try {
			DEECoNode node;
			RosServices services = new RosServices(
					 System.getenv("ROS_MASTER_URI"),
					 InetAddress.getLocalHost().getHostName());
			switch (args[0]) {
			case SENSE_SWITCH:
				node = new DEECoNode(rand.nextInt(), t, services, new Network(),
						new BeeClickComm(), new DefaultKnowledgePublisher());

				SensingComponent snsComponent = new SensingComponent(
						"testComponent", SENSE_SWITCH);
				snsComponent.bumper = services.getService(Bumper.class);
				snsComponent.buttons = services.getService(Buttons.class);
				snsComponent.dockIR = services.getService(DockIR.class);
				snsComponent.floorDistance = services.getService(FloorDistance.class);
				snsComponent.info = services.getService(Info.class);
				snsComponent.leds = services.getService(LEDs.class);
				snsComponent.position = services.getService(Position.class);
				snsComponent.sht1x = services.getService(SHT1x.class);
				snsComponent.speeker = services.getService(Speeker.class);
				snsComponent.wheels = services.getService(Wheels.class);

				node.deployComponent(snsComponent);
				
				break;
			case RECEIVE_SWITCH:
				node = new DEECoNode(rand.nextInt(), t, services, new Network(), new BeeClickComm(),
						new DefaultKnowledgePublisher());

				ReceivingComponent recvComponent = new ReceivingComponent(
						"testComponent", RECEIVE_SWITCH);

				node.deployComponent(recvComponent);
				
				break;
			default:
				Log.e(String.format("The parameter \"%s\" not recognized.",
						args[0]));
				System.out
						.println(String
								.format("The Run class takes one argument with a value from {\"%s\", \"%s\"}",
										SENSE_SWITCH, RECEIVE_SWITCH));
				System.exit(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(2);
		}

		Log.i("DEECo started.");
		t.start();
		Log.i("DEECo finished.");
	}

}
