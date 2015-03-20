package example0;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;

@Component
public class HelloWorld {

	/**
	 * Id of the vehicle component.
	 */
	public String id;

	/**
	 * Output of count process
	 */
	public int counter;
	
	public HelloWorld(String id) {
		this.id = id;
		this.counter = 1;
	}
	
	/**
	 * Periodically prints "Hello world!"
	 */
	@Process
	@PeriodicScheduling(period=1000)
	public static void sayHello(
			@In("id") String id
			) {
		
		System.out.println("Hello world!");
	}
	
	/**
	 * Periodically increments the counter.
	 */
	@Process
	@PeriodicScheduling(period=500)
	public static void updateCounter(
			@InOut("counter") ParamHolder<Integer> counter
			) {
		
		counter.value ++;
		System.out.println("... counter incremented to " + counter.value);
	}
}
