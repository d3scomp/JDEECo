package cz.cuni.mff.d3s.deeco.performance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import cz.cuni.mff.d3s.deeco.invokable.SchedulableComponentProcess;
import cz.cuni.mff.d3s.deeco.invokable.SchedulableEnsembleProcess;
import cz.cuni.mff.d3s.deeco.invokable.SchedulableProcess;
import cz.cuni.mff.d3s.deeco.knowledgeAnalysis.KnowledgeOldnessAnalysis;
import cz.cuni.mff.d3s.deeco.processor.ComponentParser;
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
			//component
			System.out.println("component id : "+ids.get(i));
			//knowledge
			Object ck = rt.getComponentKnowledge(ids.get(i));
			System.out.println("knowledge :"+ck);
		
			ArrayList<KnowledgeInfo> kArr = ComponentKnowledgeDecomposer.getComponentKnowledgeArray(ids.get(i), rt.km);
			Iterator<KnowledgeInfo> kIt = kArr.iterator();
			while (kIt.hasNext()) {
				 KnowledgeInfo kElem = kIt.next();
				 // path + current value
				 System.out.println(" string : "+kElem.getKnowPath()+" ,  value : "+ kElem.getCurrentValue()+"  creation :"+kElem.getValueCreationTimeStamp().finish+"  , currentTime : "+kElem.getValueCurrentTimeStamp().finish+"  ,  oldness : "+kElem.getOldness());
				 //writer
				 SchedulableProcess wObj = kElem.getWriter().getWriterObject();
				 if(wObj instanceof SchedulableComponentProcess)
					 System.out.println("              writer : "+Printer.printComponentProcess((SchedulableComponentProcess) wObj));
				 else if(wObj instanceof SchedulableEnsembleProcess)
					 System.out.println("              writer : "+Printer.printEnsemble((SchedulableEnsembleProcess) wObj));
					 
				 HashMap<TimeStamp, Object> times = kElem.getWriter().getWriterHistory();
				 System.out.println("                      w size:"+times.size());
				 List<TimeStamp> sortedList = Util.asSortedList(times.keySet());
				 for (TimeStamp timeStamp : sortedList) {
						System.out.println("                w time:   "+timeStamp.finish + " , value : "+times.get(timeStamp));
				 }
				
				 //readers
				 List<Reader> rs = kElem.getReaders();
				 for (int j = 0; j < rs.size(); j++) {
					 SchedulableProcess rObj = rs.get(j).getReaderObject();
					 if(rObj instanceof SchedulableComponentProcess)
						 System.out.println("              reader : "+Printer.printComponentProcess((SchedulableComponentProcess) rObj));
					 else if(rObj instanceof SchedulableEnsembleProcess)
						 System.out.println("              reader : "+Printer.printEnsemble((SchedulableEnsembleProcess) rObj));
					 HashMap<TimeStamp, Object> times1 = rs.get(j).getReaderTimeStamps();
					 sortedList = Util.asSortedList(times1.keySet());
					 for (TimeStamp timeStamp : sortedList) {
							System.out.println("              r  time:   "+timeStamp.finish+"  ,  value : "+times1.get(timeStamp));
						 }
				 }
				 System.err.println("summary : "+kElem.getKnowPath()+",  value : "+kElem.getCurrentValue()+"  creation :"+kElem.getValueCreationTimeStamp().finish+"  , currentTime : "+kElem.getValueCurrentTimeStamp().finish+"  ,  oldness : "+kElem.getOldness());
				 System.out.println("");
			}
		}
	
	}
	
	public void order(TimeStamp[] t){
		TimeStamp min=t[0];
		for (int i = 0; i < t.length-1; i++) {
			for (int j = t.length-1; j > 0; j--) {
				if(min.compareTo(t[j]) > 0){
					TimeStamp temp = min;
					min = t[j];
					t[j] = temp;
				}
			}
		}	
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
