package cz.cuni.mff.d3s.jdeeco.ros;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.ros.exception.RosRuntimeException;
import org.ros.internal.loader.CommandLineLoader;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.DefaultNodeMainExecutor;
import org.ros.node.Node;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMain;
import org.ros.node.NodeMainExecutor;

import com.google.common.base.Preconditions;

import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;

public class RosServices extends AbstractNodeMain implements DEECoPlugin {
	
	
	@Override
	public List<Class<? extends DEECoPlugin>> getDependencies() {
		return new ArrayList<>();
	}

	@Override
	public void init(DEECoContainer container) {
		CommandLineLoader loader = new CommandLineLoader(Arrays.asList(this.getClass().getName()));
		String nodeClassName = loader.getNodeClassName();
		System.out.println("Loading node class: " + nodeClassName);
		NodeConfiguration nodeConfiguration = loader.build();

		NodeMain nodeMain = null;
		try {
			nodeMain = loader.loadClass(nodeClassName);
		} catch (ClassNotFoundException e) {
			throw new RosRuntimeException("Unable to locate node: "
					+ nodeClassName, e);
		} catch (InstantiationException e) {
			throw new RosRuntimeException("Unable to instantiate node: "
					+ nodeClassName, e);
		} catch (IllegalAccessException e) {
			throw new RosRuntimeException("Unable to instantiate node: "
					+ nodeClassName, e);
		}

		Preconditions.checkState(nodeMain != null);
		NodeMainExecutor nodeMainExecutor = DefaultNodeMainExecutor.newDefault();
		nodeMainExecutor.execute(nodeMain, nodeConfiguration);
	}

	@Override
	public void onError(Node node, Throwable error) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onShutdown(Node node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onShutdownComplete(Node node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStart(ConnectedNode node) {
		Dynamics.getInstance().subscribe(node);
		
	}

	@Override
	public GraphName getDefaultNodeName() {
		return GraphName.of("RosServices");
	}
	
}
