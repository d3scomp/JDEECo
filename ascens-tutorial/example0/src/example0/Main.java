package example0;

import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.runtime.DEECoException;
import cz.cuni.mff.d3s.deeco.runtime.DEECoNode;
import cz.cuni.mff.d3s.deeco.timer.WallTimeTimer;

public class Main {
	public static void main(String[] args) throws AnnotationProcessorException, DEECoException, InstantiationException,
			IllegalAccessException {
		WallTimeTimer timer = new WallTimeTimer();

		// Node setup
		DEECoContainer node = new DEECoNode(0, timer);
		node.deployComponent(new HelloWorld("HELLO"));

		timer.start();
	}
}
