/* $Id$ 
 * 
 */

package cz.cuni.mff.d3s.deeco.demo.cloud;

import java.util.Random;

import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;
import cz.cuni.mff.d3s.deeco.task.ProcessContext;
import cz.cuni.mff.d3s.deeco.annotations.Process;



/**
 *
 * @author Petr Hnetynka
 */
@Component
public class NodeC {
        public final static long serialVersionUID = 1L;
	
	public Float loadRatio;
	public Float maxLoadRatio;
	public Integer networkId;
	public String targetNode;
    public Integer counter;

	public String id;

	public NodeC() {
		this.id = "NodeC";
		this.loadRatio = 0.0f;
		this.maxLoadRatio = 0.2f;
		this.networkId = 1;
		this.targetNode = null;
                this.counter = 0;
	}

	@Process
	@PeriodicScheduling(6000)
	public static void process(@Out("loadRatio") ParamHolder<Float> loadRatio) {
		loadRatio.value = new Random().nextFloat();
		
		System.out.println("Node C new load ratio: " + Math.round(loadRatio.value * 100) + "%");
	}
        
    @Process
	@PeriodicScheduling(6000)
	public static void process2(@InOut("counter") ParamHolder<Integer> counter) throws Exception {
          if (counter.value.intValue() == 0) {
            counter.value = new Integer(1);
          } else if (counter.value.intValue() == 1) {
        	ProcessContext.startComponent(new NodeD());
            System.out.println("New component successfully introduced ;-)");
            counter.value = new Integer(2);
          }
        }
}
