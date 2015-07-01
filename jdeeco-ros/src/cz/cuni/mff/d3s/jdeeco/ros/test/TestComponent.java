package cz.cuni.mff.d3s.jdeeco.ros.test;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.Local;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.jdeeco.ros.Bumper;
import cz.cuni.mff.d3s.jdeeco.ros.Buttons;
import cz.cuni.mff.d3s.jdeeco.ros.DockIR;
import cz.cuni.mff.d3s.jdeeco.ros.LEDs;
import cz.cuni.mff.d3s.jdeeco.ros.Position;
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


	public TestComponent(final String id) {
		this.id = id;
	}

	@Process
	@PeriodicScheduling(period = 500)
	public static void buttonControlledLEDs(@In("buttons") Buttons buttons,
			@In("leds") LEDs leds) {
		
		leds.setLed(LedID.LED1, LedColor.BLACK);
		
		ButtonState b0 = buttons.getButtonState(ButtonID.B0);
		if(b0 == ButtonState.PRESSED){
			leds.setLed(LedID.LED1, LedColor.RED);
		}
		ButtonState b1 = buttons.getButtonState(ButtonID.B1);
		if(b1 == ButtonState.PRESSED){
			leds.setLed(LedID.LED1, LedColor.ORANGE);
		}
		ButtonState b2 = buttons.getButtonState(ButtonID.B2);
		if(b2 == ButtonState.PRESSED){
			leds.setLed(LedID.LED1, LedColor.GREEN);
		}
	}

	@Process
	@PeriodicScheduling(period = 500)
	public static void bumperControlledSounds(@In("bumper") Bumper bumper,
	@In("speeker") Speeker speeker){
		switch(bumper.getBumper()){
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
}
