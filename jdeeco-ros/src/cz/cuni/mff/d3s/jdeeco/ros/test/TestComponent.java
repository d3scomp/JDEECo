package cz.cuni.mff.d3s.jdeeco.ros.test;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;
import cz.cuni.mff.d3s.jdeeco.ros.Sensors;

@Component
public class TestComponent {

	public String id;

	public String msg;

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
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
