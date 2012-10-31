package gov.nasa.jpf.listener;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import gov.nasa.jpf.ListenerAdapter;
import gov.nasa.jpf.jvm.ChoiceGenerator;
import gov.nasa.jpf.jvm.JVM;
import gov.nasa.jpf.jvm.ThreadInfo;
import gov.nasa.jpf.jvm.bytecode.INVOKECLINIT;
import gov.nasa.jpf.jvm.bytecode.Instruction;
import gov.nasa.jpf.jvm.bytecode.InvokeInstruction;
import gov.nasa.jpf.search.Search;

public class CGLocations extends ListenerAdapter {

  // Ignores yields
  // Instructions where CG has been created (and county how many times)
  private Map<Object, CGInfo> cgLocs = new HashMap<Object, CGInfo>();
  
  private int countYields = 0; 

  private static class CGInfo {
    public Object identified; // It is either instruction or if case of [clinit] it is name of loaded class
    
    public Instruction inst;
    public int cgInstanceCount;
    
    public Set<String> cgNames;
    
    public String stackTrace; // Stack trace when first CG is registered (useful to find what caused the clinit call)
    
    public CGInfo(Object identifier, Instruction inst) {
      this.inst = inst;
      this.cgInstanceCount = 0;
      this.cgNames = new HashSet<String>();
      this.identified = identifier;
    }
    
    public void addCG(ChoiceGenerator<?> cg, ThreadInfo lastThread) {
      if (cgInstanceCount == 0) {
        stackTrace = lastThread.getStackTrace();
      }
      cgNames.add(cg.getId());
      cgInstanceCount++;
    }
    
    
    
    @Override
    public String toString() {
      String result = cgInstanceCount + " - " + inst.getFileLocation() + " : " + identified + " : " + cgNames.toString() + " : " + inst.getSourceLine(); 
      //if (inst instanceof INVOKECLINIT) {
        result += "\n\t\tStackTrace=" + stackTrace.replace("\n", "\n\t\t");
      //}
      
      return result;  
    }

    public static Object getIdentifier(Instruction inst) {
      //inst instanceof INVOKECLINIT ? null : inst;
      if (inst instanceof INVOKECLINIT) {
        INVOKECLINIT ic = (INVOKECLINIT)inst;
        return ic.getInvokedClassName();
      }
      
      return inst;
    }
  }
  
  int counterChoiceGeneratorRegistered = 0;
  @Override
  public void choiceGeneratorRegistered(JVM vm) {
    counterChoiceGeneratorRegistered++;
    //System.out.println("CGLocations.choiceGeneratorRegistered() ... counter=" + counterChoiceGeneratorRegistered);

    Instruction lastInst = vm.getLastInstruction();
    if (lastInst == null) {
      return;
    }
    
    ChoiceGenerator<?> cg = vm.getLastChoiceGenerator();
//    if (cg.getTotalNumberOfChoices() <= 1) {
//      // Uninteresting CG -> not source of State space explosion
//      return;
//    }

    if ("yield".equals(cg.getId())) {
      countYields++;
      // Ignored CG
      return;
    }
    

    if (lastInst instanceof INVOKECLINIT) {
      System.out.println("CGLocations.choiceGeneratorRegistered() ... counter=" + counterChoiceGeneratorRegistered);
      System.out.println(((InvokeInstruction)lastInst).getInvokedMethodClassName());
      
    }

    Object id = CGInfo.getIdentifier(lastInst);
    CGInfo cgInfo = cgLocs.get(id);
    if (cgInfo == null) {
      cgInfo = new CGInfo(id, lastInst);
      cgLocs.put(id, cgInfo);
    }
    cgInfo.addCG(cg, vm.getLastThreadInfo());
    
  }


  @Override
  public void searchFinished(Search search) {
    // Sort records according count
    Comparator<Map.Entry<Object, CGInfo>> comp = new MapEntryInverseComparator<Map.Entry<Object, CGInfo>, CGInfo>( new CGInfoCountComparator());
    SortedSet<Map.Entry<Object, CGInfo>> ss = new TreeSet<Map.Entry<Object, CGInfo>>(comp);
    ss.addAll(cgLocs.entrySet());

    System.out.println("Dumping where most CG has been created");
    System.out.println("CG created due to yields =" + countYields);
    for (Map.Entry<Object, CGInfo> e : ss) {
      System.out.println("\t" + e.getValue().toString());
    }
  }

  private static class CGInfoCountComparator implements Comparator<CGInfo> {

    @Override
    public int compare(CGInfo o1, CGInfo o2) {
      if (o1 == null) {
        return -1;
      }
      if (o2 == null) {
        return 1;
      }
      
      return o1.cgInstanceCount - o2.cgInstanceCount;
    }
    
  }
  private static class MapEntryInverseComparator<T extends Map.Entry<?, V>, V> implements Comparator<T> {

    private Comparator<V> comp;
    
    public MapEntryInverseComparator(Comparator<V> comp) {
      this.comp = comp;
    }

    @Override
    public int compare(T o1, T o2) {
      V o1v = o1.getValue();
      V o2v = o2.getValue();

      
      int valCmp = comp.compare(o1v, o2v);

      if (valCmp != 0) {
        return valCmp;
      }

      Object o1k = o1.getKey();
      Object o2k = o2.getKey();

      if (o1k == null) {
        if (o2k != null) {
          valCmp = 1;
        }
      } else {
        if (o2k == null) {
          valCmp = -1;
        } else {
          valCmp = o1k.hashCode() - o2k.hashCode();
        }
      }
      
      return valCmp;
    }
  }
}
