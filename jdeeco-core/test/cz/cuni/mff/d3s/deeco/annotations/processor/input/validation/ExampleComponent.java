package cz.cuni.mff.d3s.deeco.annotations.processor.input.validation;

import java.util.Collections;
import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;

/**
 * Valid component definition.
 * 
 * @author Jaroslav Keznikl <keznikl@d3s.mff.cuni.cz>
 *
 */
@Component
// TODO: add explicit (declaration of) roles
//@HasSecurityRole(EnsembleDef1.class, Role.COORDINATOR)
//@HasSecurityRole(EnsembleDef2.class, Role.MEMBER)
public class ExampleComponent {

	public Integer in = 0;
	public String inout = "";
	public Boolean out = false;
	
	String inoutInvalid = "";
	
	public List<String> typeMismatch = Collections.emptyList();
	
	@Process
	@PeriodicScheduling(period=1000)
	public static void process1(
			@In("in") Integer p1,
			@InOut("inout") String p2,
			@Out("out") Boolean p3) {
		// business code
	}

}
