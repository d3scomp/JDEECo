package cz.cuni.mff.d3s.jdeeco.ros.test;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.Local;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.jdeeco.ros.Actuators;
import cz.cuni.mff.d3s.jdeeco.ros.Sensors;
import cz.cuni.mff.d3s.jdeeco.ros.datatypes.Bumper;
import cz.cuni.mff.d3s.jdeeco.ros.datatypes.LedColor;
import cz.cuni.mff.d3s.jdeeco.ros.datatypes.LedId;
import cz.cuni.mff.d3s.jdeeco.ros.datatypes.Sound;

@Component
public class TestComponent {

	public String id;

	@Local
	public Sensors sensors;

	@Local
	public Actuators actuators;

	public TestComponent(final String id, Sensors sensors, Actuators actuators) {
		this.id = id;
		this.sensors = sensors;
		this.actuators = actuators;
	}

	@Process
	@PeriodicScheduling(period = 200)
	public static void checkMessage(@In("sensors") Sensors sensors,
			@In("actuators") Actuators actuators) {
		/*
		 * System.out.println(message.value); if
		 * (Sensors.getInstance().getPosition() != null) { message.value =
		 * String.format("[%f, %f]", Sensors.getInstance()
		 * .getPosition().getX(), Sensors.getInstance().getPosition() .getY());
		 * }
		 */

		actuators.setVelocity(0.5, 0);
		Bumper bumper = sensors.getBumper();
		// System.out.println(bumper);
		switch (bumper) {
		case CENTER:
			actuators.setLed(LedId.LED1, LedColor.RED);
			actuators.setLed(LedId.LED2, LedColor.RED);
			actuators.playSound(Sound.RECHARGE);
			break;
		case LEFT:
			actuators.setLed(LedId.LED1, LedColor.RED);
			actuators.setLed(LedId.LED2, LedColor.BLACK);
			actuators.playSound(Sound.CLEANING_END);
			break;
		case RIGHT:
			actuators.setLed(LedId.LED1, LedColor.BLACK);
			actuators.setLed(LedId.LED2, LedColor.RED);
			actuators.playSound(Sound.CLEANING_START);
			break;
		default:
			actuators.setLed(LedId.LED1, LedColor.BLACK);
			actuators.setLed(LedId.LED2, LedColor.BLACK);
			break;
		}
	}

}
