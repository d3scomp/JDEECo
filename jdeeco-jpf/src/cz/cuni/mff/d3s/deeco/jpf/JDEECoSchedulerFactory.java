package cz.cuni.mff.d3s.deeco.jpf;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.jvm.ChoiceGenerator;
import gov.nasa.jpf.jvm.DefaultSchedulerFactory;
import gov.nasa.jpf.jvm.JVM;
import gov.nasa.jpf.jvm.SystemState;
import gov.nasa.jpf.jvm.ThreadInfo;


// disables thread choices at unpark
public class JDEECoSchedulerFactory extends DefaultSchedulerFactory 
{
	public JDEECoSchedulerFactory(Config config, JVM vm, SystemState ss) 
	{
		super(config, vm, ss);
	}

	public ChoiceGenerator<ThreadInfo> createUnparkCG (ThreadInfo tiUnparked) 
	{
		return null;
	}
}
