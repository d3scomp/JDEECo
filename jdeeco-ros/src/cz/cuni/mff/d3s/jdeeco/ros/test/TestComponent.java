package cz.cuni.mff.d3s.jdeeco.ros.test;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.Local;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;
import cz.cuni.mff.d3s.jdeeco.ros.Bumper;
import cz.cuni.mff.d3s.jdeeco.ros.Buttons;
import cz.cuni.mff.d3s.jdeeco.ros.DockIR;
import cz.cuni.mff.d3s.jdeeco.ros.LEDs;
import cz.cuni.mff.d3s.jdeeco.ros.Position;
import cz.cuni.mff.d3s.jdeeco.ros.SHT1x;
import cz.cuni.mff.d3s.jdeeco.ros.Speeker;
import cz.cuni.mff.d3s.jdeeco.ros.Wheels;
import cz.cuni.mff.d3s.jdeeco.ros.datatypes.ButtonID;
import cz.cuni.mff.d3s.jdeeco.ros.datatypes.ButtonState;
import cz.cuni.mff.d3s.jdeeco.ros.datatypes.LedColor;
import cz.cuni.mff.d3s.jdeeco.ros.datatypes.LedID;
import cz.cuni.mff.d3s.jdeeco.ros.datatypes.SoundValue;

@Component
public class TestComponent {

	public String id;

	@Local
	public Bumper bumper;

	@Local
	public Buttons buttons;

	@Local
	public DockIR dockIR;

	@Local
	public LEDs leds;

	@Local
	public Position position;

	@Local
	public Speeker speeker;

	@Local
	public Wheels wheels;

	@Local
	public SHT1x sht1x;

	public Integer lastPressedButton;

	public TestComponent(final String id) {
		this.id = id;
		lastPressedButton = -1;
	}

	@Process
	@PeriodicScheduling(period = 500)
	public static void buttonControlledLEDs(@In("buttons") Buttons buttons,
			@In("leds") LEDs leds, @In("position") Position position,
			@InOut("lastPressedButton") ParamHolder<Integer> lastPressedButton) {

		int pressedButton = -1;

		leds.setLed(LedID.LED1, LedColor.BLACK);
		leds.setLed(LedID.LED2, LedColor.BLACK);

		ButtonState b0 = buttons.getButtonState(ButtonID.B0);
		if (b0 == ButtonState.PRESSED) {
			leds.setLed(LedID.LED1, LedColor.RED);
			pressedButton = 0;
		}
		ButtonState b1 = buttons.getButtonState(ButtonID.B1);
		if (b1 == ButtonState.PRESSED) {
			leds.setLed(LedID.LED1, LedColor.ORANGE);
			pressedButton = 1;
		}
		ButtonState b2 = buttons.getButtonState(ButtonID.B2);
		if (b2 == ButtonState.PRESSED) {
			leds.setLed(LedID.LED1, LedColor.GREEN);
			pressedButton = 2;
		}
		
		System.out.println(String.format("pressed: %d", pressedButton));

		if (pressedButton == -1) {

			switch (lastPressedButton.value) {
			case 0:
				//position.setSimpleGoal(12.901, 1.707, 0.000, 0.000, 0.000, 0.714, 0.700);
				leds.setLed(LedID.LED2, LedColor.RED);
				break;
			case 1:
				//position.setSimpleGoal(12.637, 8.438, 0.000, 0.000, 0.000, -0.708, 0.706);
				leds.setLed(LedID.LED2, LedColor.ORANGE);
				break;
			case 2:
				//position.setSimpleGoal(12.689, 4.529, 0.000, 0.000, 0.000, -0.700, 0.714);
				leds.setLed(LedID.LED2, LedColor.GREEN);
				break;
			}
		}

		lastPressedButton.value = pressedButton;

		// Position 1 :
		// pos: 12.901, 1.707, 0.000
		// ori: 0.000, 0.000, 0.714, 0.700
		// ang: 1.590

		// Position 2 :
		// pos: 12.637, 8.438, 0.000
		// ori: 0.000, 0.000, -0.708, 0.706
		// ang: -1.573

		// Position 3 :
		// pos: 12.689, 4.529, 0.000
		// ori: 0.000, 0.000, -0.700, 0.714
		// ang: -1.551
	}

	@Process
	@PeriodicScheduling(period = 500)
	public static void bumperControlledSounds(@In("bumper") Bumper bumper,
			@In("speeker") Speeker speeker) {
		switch (bumper.getBumper()) {
		case CENTER:
			speeker.playSound(SoundValue.RECHARGE);
			break;
		case LEFT:
			speeker.playSound(SoundValue.CLEANING_START);
			break;
		case RIGHT:
			speeker.playSound(SoundValue.CLEANING_END);
			break;
		default:
			break;
		}
	}

	//@Process
	@PeriodicScheduling(period = 500)
	public static void temperatureHumidity(@In("sht1x") SHT1x sht1x) {
		System.out.println(String.format("Temperature: %f",
				sht1x.getTemperature()));
		System.out.println(String.format("Humidity: %f", sht1x.getHumidity()));
	}

}
