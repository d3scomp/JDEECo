package cz.cuni.mff.d3s.jdeeco.ros.test;

import geometry_msgs.Point;
import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.Local;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.jdeeco.ros.Actuators;
import cz.cuni.mff.d3s.jdeeco.ros.Sensors;
import cz.cuni.mff.d3s.jdeeco.ros.datatypes.Bumper;
import cz.cuni.mff.d3s.jdeeco.ros.datatypes.Button;
import cz.cuni.mff.d3s.jdeeco.ros.datatypes.ButtonState;
import cz.cuni.mff.d3s.jdeeco.ros.datatypes.LedColor;
import cz.cuni.mff.d3s.jdeeco.ros.datatypes.LedId;
import cz.cuni.mff.d3s.jdeeco.ros.datatypes.MotorPower;
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
	@PeriodicScheduling(period = 500)
	public static void checkMessage(@In("sensors") Sensors sensors,
			@In("actuators") Actuators actuators) {
		/*
		 * System.out.println(message.value); if
		 * (Sensors.getInstance().getPosition() != null) { message.value =
		 * String.format("[%f, %f]", Sensors.getInstance()
		 * .getPosition().getX(), Sensors.getInstance().getPosition() .getY());
		 * }
		 */

		actuators.setLed(LedId.LED1, LedColor.BLACK);
		
		ButtonState b0 = sensors.getButtonState(Button.B0);
		if(b0 == ButtonState.PRESSED){
			actuators.setLed(LedId.LED1, LedColor.RED);
		}
		ButtonState b1 = sensors.getButtonState(Button.B1);
		if(b1 == ButtonState.PRESSED){
			actuators.setLed(LedId.LED1, LedColor.ORANGE);
		}
		ButtonState b2 = sensors.getButtonState(Button.B2);
		if(b2 == ButtonState.PRESSED){
			actuators.setLed(LedId.LED1, LedColor.GREEN);
		}
		
		/*Point p = sensors.getOdometry(); 
		if(p != null){
			System.out.println(String.format("[%f, %f]", p.getX(), p.getY()));
		}
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
		}*/
	}

	/*@Process
	@PeriodicScheduling(period = 5000)
	public static void switchPower(@In("sensors") Sensors sensors,
			@In("actuators") Actuators actuators) {
		actuators.resetOdometry();
	}*/
}
