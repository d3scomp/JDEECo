package cz.cuni.mff.d3s.deeco.performance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.cuni.mff.d3s.deeco.invokable.SchedulableComponentProcess;
import cz.cuni.mff.d3s.deeco.invokable.SchedulableEnsembleProcess;
import cz.cuni.mff.d3s.deeco.runtime.Runtime;


public class InfoSerializable implements Serializable{
	
	private Runtime rt;

	public InfoSerializable(Runtime rt) {
		// TODO Auto-generated constructor stub
		this.rt = rt;
	}

	public void write(){
		
	}
	
	public void read(){
		
	}
	
	public void getProcess(){
		
	}
	
	public void getEnsemble(){
		
	}
	
	public void getPeriodicProcess(){
		getProcess();
	}
	
	public void getPeriodicEnsemble(){
		getEnsemble();
	}
	
	public void getTriggeredProcess(){
		getProcess();
	}
	
	public void getTriggeredEnsemble(){
		getEnsemble();
	}
	
}
