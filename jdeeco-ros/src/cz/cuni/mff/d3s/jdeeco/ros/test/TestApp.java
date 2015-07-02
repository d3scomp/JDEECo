package cz.cuni.mff.d3s.jdeeco.ros.test;

import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.runtime.DEECoNode;
import cz.cuni.mff.d3s.deeco.timer.WallTimeTimer;
import cz.cuni.mff.d3s.jdeeco.ros.Bumper;
import cz.cuni.mff.d3s.jdeeco.ros.Buttons;
import cz.cuni.mff.d3s.jdeeco.ros.DockIR;
import cz.cuni.mff.d3s.jdeeco.ros.LEDs;
import cz.cuni.mff.d3s.jdeeco.ros.Position;
import cz.cuni.mff.d3s.jdeeco.ros.RosServices;
import cz.cuni.mff.d3s.jdeeco.ros.SHT1x;
import cz.cuni.mff.d3s.jdeeco.ros.Speeker;
import cz.cuni.mff.d3s.jdeeco.ros.Wheels;

public class TestApp {

	public static void main(String[] args) {
		try {
			
			WallTimeTimer t = new WallTimeTimer();
			RosServices services = new RosServices();
			DEECoNode node = new DEECoNode(0, t, services);
			
			TestComponent testComponent = new TestComponent("testComponent");
			testComponent.bumper = services.getService(Bumper.class);
			testComponent.buttons = services.getService(Buttons.class);
			testComponent.dockIR = services.getService(DockIR.class);
			testComponent.leds = services.getService(LEDs.class);
			testComponent.position = services.getService(Position.class);
			testComponent.speeker = services.getService(Speeker.class);
			testComponent.wheels = services.getService(Wheels.class);
			testComponent.sht1x = services.getService(SHT1x.class);
			
			node.deployComponent(testComponent);

			Log.i("Simulation started.");
			t.start();
			Log.i("Simulation finished.");
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
