package cz.cuni.mff.d3s.jdeeco.ros.test;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;
import cz.cuni.mff.d3s.jdeeco.ros.Actuators;
import cz.cuni.mff.d3s.jdeeco.ros.Sensors;
import cz.cuni.mff.d3s.jdeeco.ros.datatypes.LedColor;
import cz.cuni.mff.d3s.jdeeco.ros.datatypes.LedId;

@Component
public class TestComponent {

	public String id;

	public String msg;
	
	private static LedColor led1 = LedColor.GREEN;
	private static LedColor led2 = LedColor.RED;

	public TestComponent(final String id) {
		this.id = id;
		msg = "initial msg";
	}

	@Process
	@PeriodicScheduling(period = 1000)
	public static void checkMessage(@InOut("msg") ParamHolder<String> message) {
		System.out.println(message.value);
		if (Sensors.getInstance().getPosition() != null) {
			message.value = String.format("[%f, %f]", Sensors.getInstance()
					.getPosition().getX(), Sensors.getInstance().getPosition()
					.getY());
		}

		//Actuators.getInstance().setVelocity(0.5,0);
		Actuators.getInstance().setLed(LedId.LED1, led1);
		Actuators.getInstance().setLed(LedId.LED2, led2);
		if(led1 == LedColor.GREEN) led1 = LedColor.RED;
		else led1 = LedColor.GREEN;
		if(led2 == LedColor.GREEN) led2 = LedColor.RED;
		else led2 = LedColor.GREEN;
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
