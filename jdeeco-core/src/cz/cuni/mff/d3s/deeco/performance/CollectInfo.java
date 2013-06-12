package cz.cuni.mff.d3s.deeco.performance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import cz.cuni.mff.d3s.deeco.invokable.SchedulableComponentProcess;
import cz.cuni.mff.d3s.deeco.invokable.SchedulableEnsembleProcess;
import cz.cuni.mff.d3s.deeco.knowledgeAnalysis.KnowledgeOldnessAnalysis;
import cz.cuni.mff.d3s.deeco.runtime.Runtime;

public class CollectInfo {

	Runtime rt;
	List<String> ids;
	HashMap<String, List<SchedulableComponentProcess>> proc = new HashMap<String, List<SchedulableComponentProcess>>();
	List<SchedulableEnsembleProcess> ens;
	
	

	public CollectInfo(Runtime rt) {
		// TODO Auto-generated constructor stub
		this.rt = rt;
		ids=rt.getComponentsIds();
		ens=rt.getEnsembleProcesses();
	}

	public void print() {
		System.out.println("*************************************   start   ****************************************");
		ids = rt.getComponentsIds();
		for (int i = 0; i < ids.size(); i++) {
			proc.put(ids.get(i), rt.getComponentProcesses(ids.get(i)));
			System.out.println("component id= "+ids.get(i));
		}
		System.out.println("                            -------------------------                             ");
//		printProcesses();
//		printEnsembles();
//		printKnowledge();
		printKnowledgeInfo();
		System.out.println("*************************************   end   ****************************************");
	}

	private void printKnowledgeInfo() {
		for (int i = 0; i < ids.size(); i++) {
			System.out.println("component id : "+ids.get(i));
			List<SchedulableComponentProcess> cp = rt.getComponentProcesses(ids.get(i));
			for (int j = 0; j < cp.size(); j++) {
				System.out.println("        process : "+Printer.printComponentProcess(cp.get(j), 10));
				for (int j2 = 0; j2 < cp.get(j).kInfoArr.size(); j2++) {
						KnowledgeInfo ck = cp.get(j).kInfoArr.get(j2);
						System.out.print("                 knowledge path : "+ck.knowPath+" , knowledge value : "+ck.value);
						System.out.println();
						//I have to consider the Triggered processes and ensembles
						HashMap<SchedulableComponentProcess, TimeStamp> ite = ck.registerTimeStamp.processHistoryReader;
						for (SchedulableComponentProcess key : ite.keySet()) {
							System.out.println("     process reader : "+Printer.printComponentProcess(key, 10));
							System.out.println("             time : "+ite.get(key).start);
						}
						HashMap<SchedulableComponentProcess, TimeStamp> itew = ck.registerTimeStamp.processHistoryWriter;
						for (SchedulableComponentProcess key : itew.keySet()) {
							System.out.println("     process writer : "+Printer.printComponentProcess(key, 10));
							System.out.println("             time : "+itew.get(key).start);
						}

				}

			}
		}
		
		List<SchedulableEnsembleProcess> ce = rt.getEnsembleProcesses();
		for (int j = 0; j < ce.size(); j++) {
			System.out.println("        ensemble : "+Printer.printEnsemble(ce.get(j), 10));
			for (int j2 = 0; j2 < ce.get(j).kInfoArr.size(); j2++) {
					KnowledgeInfo ck = ce.get(j).kInfoArr.get(j2);
					System.out.print("                 knowledge path : "+ck.knowPath+" , knowledge value : "+ck.value);
					System.out.println();
					//I have to consider the Triggered processes and ensembles
					HashMap<SchedulableEnsembleProcess, TimeStamp> ite = ck.registerTimeStamp.ensembleHistoryReader;
					for (SchedulableEnsembleProcess key : ite.keySet()) {
						System.out.println("     ensemble reader : "+Printer.printEnsemble(key, 10));
						System.out.println("             time : "+ite.get(key));
					}
					HashMap<SchedulableEnsembleProcess, TimeStamp> itew = ck.registerTimeStamp.ensembleHistoryWriter;
					for (SchedulableEnsembleProcess key : itew.keySet()) {
						System.out.println("     ensemble writer : "+Printer.printEnsemble(key, 10));
						System.out.println("             time : "+itew.get(key).start);
					}

			}

		}		


		
//		System.out.println("==============================  end  of knowledge ================================================");
//
//		KnowledgeOldnessAnalysis know=new KnowledgeOldnessAnalysis();
//		know.visitAllNodes();

//		HashMap<String, KnowledgeInfo> arrK=rt.km.arrayKnow;
//		for (int j = 0; j < arrK.size(); j++) {
//			System.out.println(" path "+arrK.get(j)+"   value Info : "+arrK.values().iterator().next());
//		}
		
	}

	public void print(List<SchedulableComponentProcess> l) {
		System.out.println("size " + l.size());
		for (int j = 0; j < l.size(); j++) {
			// we have to distinguish between triggered and periodic
			PeriodicProcessInfo pp = ((PeriodicProcessInfo) (l.get(j).pInfo));
			System.out.println("     - start periods : " + pp.startPeriods);
			ArrayList<TimeStamp> lp = pp.runningPeriods;
			print(lp);
		}
	}

	public void print(ArrayList<TimeStamp> lp) {
		System.out.println("size of timestamp :" + lp.size());
		for (int j2 = 0; j2 < lp.size(); j2++) {
			System.out.println("                   @ running " + j2
					+ " release : " + lp.get(j2).release + "  start: "
					+ lp.get(j2).start + "  finish : " + lp.get(j2).finish);
		}
	}
	

	public void printKnowledge() {
		System.out.println("---%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%---");
		System.out.println("-------------------------------------------------------");
		System.out.println("print Knowledge ...  ");
		for (int i = 0; i < ids.size(); i++) {
			Object obj = rt.getComponentKnowledge(ids.get(i));
			System.out.println(" know id " + obj + "  : ");
		}

	}
	
	
	public void printProcesses() {
		System.out.println("-------------------------------------------------------");
		System.out.println("-------------------------------------------------------");
		System.out.println("print processes ...  ");
		for (int i = 0; i < ids.size(); i++) {
			System.out.println(" proc id " + proc.get(ids.get(i)) + "  : ");
			List<SchedulableComponentProcess> l = proc.get(ids.get(i));
			print(l);
		}

	}

	public void printEnsembles() {
		System.out.println("-------------------------------------------------------");
		System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
		System.out.println("print ensembles ...  ");
		ens = rt.getEnsembleProcesses();
		for (int i = 0; i < ens.size(); i++) {
			// we have to distinguish between triggered and periodic

			if (ens.get(i).pInfo instanceof PeriodicEnsembleInfo) {
				PeriodicEnsembleInfo pe = ((PeriodicEnsembleInfo) (ens.get(i).pInfo));
				System.out.println("ens : "+ens.get(i)+" start co :" + pe.startPeriodsCoord +"   start mem : " + pe.startPeriodsMem);
				System.out.println("   coordinator ...  ");
				ArrayList<TimeStamp> lep = pe.runningPeriodsCoord;
				print(lep);
				System.out.println("   member ...  ");
				lep = pe.runningPeriodsMem;
				print(lep);
			} 
//			else if (ens.get(i).pInfo instanceof TriggeredEnsembleInfo) {
//				TriggeredEnsembleInfo pe = ((TriggeredEnsembleInfo) (ens.get(i).pInfo));
//				System.out.println("   ens : D :" + pe.D);
//				ArrayList<TimeStamp> lep = pe.timeStampsCoord;
//				print(lep);
//				lep = pe.timeStampsMem;
//				print(lep);
//			}

		}
	}

}
