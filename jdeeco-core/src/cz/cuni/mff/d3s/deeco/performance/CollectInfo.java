package cz.cuni.mff.d3s.deeco.performance;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.cuni.mff.d3s.deeco.invokable.SchedulableComponentProcess;
import cz.cuni.mff.d3s.deeco.invokable.SchedulableEnsembleProcess;
import cz.cuni.mff.d3s.deeco.runtime.Runtime;

public class CollectInfo {
	
	Runtime rt;
	List<String> ids;
	HashMap<String, List<SchedulableComponentProcess>> proc =new HashMap<String, List<SchedulableComponentProcess>>();
	List<SchedulableEnsembleProcess> ens;
	
	public CollectInfo(Runtime rt) {
		// TODO Auto-generated constructor stub
		this.rt = rt;
	}
	
	
	public void print(){
		ids = rt.getComponentsIds();
		for (int i = 0; i < ids.size(); i++) {
			proc.put(ids.get(i), rt.getComponentProcesses(ids.get(i)));
			System.out.println( ids.get(i) + " ... " + rt.getComponentProcesses(ids.get(i)).size()  );
		}

		try{
				rt.stopRuntime();
			}catch(Exception ex){
				System.out.println("stoping...");
			}finally{
				System.out.println("==============================================================================");
				printProcesses();
				printEnsembles();
			}
	}
	
	
	public void print(List<SchedulableComponentProcess> l){
		System.out.println("size "+l.size());
		for (int j = 0; j < l.size(); j++) {
			//we have to distinguish between triggered and periodic
			PeriodicProcessInfo pp = ((PeriodicProcessInfo)(l.get(j).pInfo));
			System.out.println("                - start periods : "+pp.startPeriods);
			ArrayList<TimeStamp> lp = pp.runningPeriods;
			print(lp);
		}
	}
	
	
	public void print(ArrayList<TimeStamp> lp){
		System.out.println("size rps :"+lp.size());
		for (int j2 = 0; j2 < lp.size(); j2++) {
			System.out.println("                   @ running "+j2+" release : " +lp.get(j2).release + "  start: "+lp.get(j2).start+ "  finish"+lp.get(j2).finish);
		}
	}
	
	public void printProcesses(){
		for (int i = 0; i < ids.size(); i++) {
			System.out.println(" proc id "+proc.get(ids.get(i))+"  : ");
			List<SchedulableComponentProcess> l = proc.get(ids.get(i));
			print(l);
		}

	}
	
	public void printEnsembles(){
		ens = rt.getEnsembleProcesses();
		for (int i = 0; i < ens.size(); i++) {
			//we have to distinguish between triggered and periodic
			
			if(ens.get(i).pInfo instanceof PeriodicEnsembleInfo){
				PeriodicEnsembleInfo pe = ((PeriodicEnsembleInfo)(ens.get(i).pInfo));
				System.out.println("   ens : start co :"+pe.startPeriodsCoord+"   start mem : "+pe.startPeriodsMem);
				ArrayList<TimeStamp> lep = pe.runningPeriodsCoord;
				print(lep);
				lep=pe.runningPeriodsMem;
				print(lep);
			}else if(ens.get(i).pInfo instanceof TriggeredEnsembleInfo){
				TriggeredEnsembleInfo pe = ((TriggeredEnsembleInfo)(ens.get(i).pInfo));
				System.out.println("   ens : D :"+pe.D);
				ArrayList<TimeStamp> lep = pe.timeStampsCoord;
				print(lep);
				lep=pe.timeStampsMem;
				print(lep);
			}
			
		}
	}
	
}
