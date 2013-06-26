package cz.cuni.mff.d3s.deeco.performance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.cuni.mff.d3s.deeco.invokable.SchedulableComponentProcess;
import cz.cuni.mff.d3s.deeco.invokable.SchedulableEnsembleProcess;
import cz.cuni.mff.d3s.deeco.invokable.SchedulableProcess;

public class ProcessKnowledgeDecomposer extends KnowledgeDecomposer {
	
	SchedulableProcess sp;
	IPerformanceInfo pInfo;
	//list of timestamp to compare if there is any later times should not be included
	HashMap<KnowledgeInfo, List<TimeStamp>> inOld;
	
	protected void addKnowInfo(String st, TimeStamp time){
		// there is a problem with the values : I am always getting for the "name" field Object[] when decomposing from any process
		int exist=0;
		TimeStamp t=new TimeStamp();
		List<KnowledgeInfo> arrElemDelete=new ArrayList<KnowledgeInfo>();
		for (int j = arrPath.size()-1; j >= 0; j--) {
			arrElemDelete=new ArrayList<KnowledgeInfo>();
			exist=0;
			Object path = arrPath.get(j);
			for (KnowledgeInfo arrElem : km.arrayKnow) {
				if(arrElem.getKnowPath().equals(path.toString())){
					t=arrElem.getValueCreationTimeStamp();
					if( !(arrValue.get(j) instanceof Object[]))
						chooseAdd(arrElem, st, time, arrValue.get(j));
					else
						chooseAdd(arrElem, st, time, arrElem.getCurrentValue());
					exist=1;
				} else if(path.toString().contains(arrElem.getKnowPath())){
					arrElemDelete.add(arrElem);
					exist=2;
				}
			}
			
			if(exist == 2){
				KnowledgeInfo ki=new KnowledgeInfo();
				ki.setKnowPath(arrPath.get(j).toString());
				ki.setCurrentValue(arrValue.get(j));
				ki.setValueCurrentTimeStamp(time);
				addValueCreationTimeStamp(ki,time);
				km.arrayKnow.add(ki);
				chooseAdd(ki, st, time, arrValue.get(j));
				
			}else if(exist == 0){
				arrPath.remove(j);
				arrValue.remove(j);
			}
		}
		
		for (KnowledgeInfo knowledgeInfo : arrElemDelete) {
			for (int i = 0; i < km.arrayKnow.size(); i++) {
				KnowledgeInfo k = km.arrayKnow.get(i);
				KnowledgeInfo ki = knowledgeInfo;
				if(k.getKnowPath().equals(ki.getKnowPath())){
					t=ki.getValueCreationTimeStamp();
					k.setValueCreationTimeStamp(t);
				}
			}
			km.arrayKnow.remove(knowledgeInfo);
		}
		
	}
	
		
	protected void chooseAdd(KnowledgeInfo arrElem,String st, TimeStamp time, Object value){
			if(st.equals("in")){
				addReaderKnowledge(arrElem, time, value);
			}else if(st.equals("out")){
					addProcessWriteKnowledge(arrElem, time, value);
			}else if(st.equals("inOut")){
					addReaderKnowledge(arrElem, time, value);
					addProcessWriteKnowledge(arrElem, time, value);
			}
	}
	
	
	protected void addReaderKnowledge(KnowledgeInfo arrElem, TimeStamp time, Object value){
		 List<Reader> r = arrElem.getReaders();
		 boolean exist=false;
		 int index=-1;
		for (int i = 0; i < r.size(); i++) {
			SchedulableProcess ele = r.get(i).getReaderObject();
			if(ele.equals(sp)){
				exist=true;
				index=i;
			}
		}
		if(!exist){
			Reader newR=new Reader();
			newR.setReaderObject(sp);
			newR.addTimeStampValue(time, value);
			arrElem.addReader(newR);
		}else{
			Reader rHsp = r.get(index);
			rHsp.addTimeStampValue(time, value);
			arrElem.setReader(index, rHsp);
		}
		
		if( sp instanceof SchedulableEnsembleProcess){
			List<TimeStamp> iT=null;
			if ( inOld.containsKey(arrElem)){
				 iT = inOld.get(arrElem);
			}else{
				 iT =new ArrayList<TimeStamp>();
			}
			iT.add(time);
			inOld.put(arrElem, iT);

		}
		
	}
	
	protected void addProcessWriteKnowledge(KnowledgeInfo arrElem, TimeStamp time, Object value){
		boolean exist=false;
		SchedulableProcess ele = arrElem.getWriter().getWriterObject();
		if(ele != null && ele.equals(sp) )
				exist=true;
		if(!exist){
				if(ele != null ) 
					System.err.println("there are more than one writer the new one is added" + ele);
				arrElem.getWriter().setWriterObject(sp);
				ArrayList<TimeStamp> timeList=new ArrayList<TimeStamp>();
				timeList.add(time);
				arrElem.getWriter().addTimeStampValue(time, value);
		}else{ 
				arrElem.getWriter().addTimeStampValue(time, value);
		}
		
		arrElem.setCurrentValue(value);
		arrElem.setValueCurrentTimeStamp(time);
		addValueCreationTimeStamp(arrElem, time);
	}
	
	public void addValueCreationTimeStamp(KnowledgeInfo arrElem, TimeStamp time){
		if(sp instanceof SchedulableComponentProcess){
			arrElem.setValueCreationTimeStamp(time);
		}else if (sp instanceof SchedulableEnsembleProcess){
					TimeStamp minReaders=new TimeStamp();
					for (KnowledgeInfo ki : inOld.keySet()) {
						if( minReaders.finish > ki.getValueCreationTimeStamp().finish || minReaders.finish == 0 ){
							minReaders = ki.getValueCreationTimeStamp();
						}
					}
					arrElem.setValueCreationTimeStamp(minReaders);
		}
	}
}