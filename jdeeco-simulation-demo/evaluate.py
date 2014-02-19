import os, sys
from generator.simple import generateConfig
from generator.complex import generateComplexRandomConfig
from analysis.analyze_demo import *
from analysis.analyze_log import *
from analysis.analyze_neighbors import *
from exceptions import OSError
from subprocess import *
import atexit
import sys
import time
from shutil import copyfile
import numpy as np
import pylab
from numpy import average
from multiprocessing import *
from math import ceil
from pylab import plot, show, savefig, xlim, figure, \
                hold, ylim, legend, boxplot, setp, axes
from collections import namedtuple


root = os.path.dirname(os.path.realpath(__file__))
cpus = 19

class Scenario():
    def __init__(self, nodeCnt, othersCnt, iterationCnt, boundaryEnabled, generator, start = 0):
        self.nodeCnt = nodeCnt
        self.othersCnt = othersCnt
        self.iterationCnt = iterationCnt
        self.boundaryEnabled = boundaryEnabled
        self.generator = generator
        self.iterations = []
        self.start = start
        for i in range(iterationCnt):
            self.iterations.append(ScenarioIteration(self, nodeCnt, othersCnt, start + i, boundaryEnabled, generator)) 
    def folder(self):
        return 'simulation-results\\%d' % (self.nodeCnt)
    def folderPath(self):
        return root + '\\simulation-results\\%d' % (self.nodeCnt)
    def genericResultsPath(self): 
        return root + '\\simulation-results\\results-generic-%d-%s-%s.csv' % (self.nodeCnt, 't' if self.boundaryEnabled else 'f', self.generator[0])
    def demoResultsPath(self): 
        return root + '\\simulation-results\\results-demo-%d-%s-%s.csv' % (self.nodeCnt, 't' if self.boundaryEnabled else 'f', self.generator[0])
    def neighborResultsPath(self): 
        return root + '\\simulation-results\\results-neighbors-%d-%s-%s.csv' % (self.nodeCnt, 't' if self.boundaryEnabled else 'f', self.generator[0])
    def tickLabel(self):
        if self.generator == 'simple':
            return '%d/%d' % (self.nodeCnt, self.othersCnt)  
        else:
            return '%d/%d' % (4*self.nodeCnt, 2*self.othersCnt)
       
    
class ScenarioIteration:
    def __init__(self, scenario, nodeCnt, othersCnt, iteration, boundaryEnabled, generator):
        self.scenario = scenario
        self.nodeCnt = nodeCnt
        self.othersCnt = othersCnt
        self.iteration = iteration
        self.boundaryEnabled = boundaryEnabled
        self.generator = generator    
        self.stdOut = None
        self.simulation = None
        self.demoAnalysis = None
        self.genericAnalysis = None
        self.neighborAnalysis = None
    def folder(self):
        return self.scenario.folder()
    def prefix(self):
        return '%d-%d-%s-%s-' % (self.nodeCnt, self.iteration, 't' if self.boundaryEnabled else 'f', self.generator[0])
    def prefixPath(self):
        return root + '\\' + self.folder() + '\\' + self.prefix()
    def genericAnalysisStdoutPath(self):
        return self.prefixPath() + 'analysis-generic.txt'
    def demoAnalysisStdoutPath(self):
        return self.prefixPath() + 'analysis-demo.txt'
    def logPath(self):
        return self.prefixPath() + 'jdeeco.log.0'
    def logTemplatePath(self):
        return self.prefixPath() + 'jdeeco.log'
    def stdOutPath(self):
        return self.prefixPath() + 'stdout.log'
    def loggingPropertiesPath(self):
        # needs to be relative path
        return self.prefix() + 'logging.properties'
    def baseCfgPath(self):
        # config files are shared between boundary and non-boundary scenarios
        return root + '\\' + self.folder() + '\\' + '%d-%d-%s-' % (self.nodeCnt, self.iteration, self.generator[0])
    def componentCfgPath(self):
        return self.baseCfgPath() + 'component.cfg'
    def siteCfgPath(self):
        return self.baseCfgPath() + 'site.cfg'
    def omnetppPath(self):
        # needs to be relative path
        return 'omnetpp-' + self.prefix() + 'conf'    
    def name(self):
        return self.prefix() + 'scenario'



# main list of scenarios
scenarios = []
scenariosWithBoundary = []
scenariosWithoutBoundary = []




# generic part
######################################################################
generators = []
def finalizeOldestGenerator():
    if len(generators) == 0:
        return
    g = generators[0]
    g.join()
    generators.pop(0)
    
def generate():
    generated = {}
    print 'Generating configurations...'
    for s in scenarios:
        try:
            os.makedirs(s.folderPath())
        except OSError as e:
            pass        
        if s.nodeCnt not in generated:
            generated[s.nodeCnt] = {}
            
        for it in s.iterations:
            print 'Generating ', it.name()
            # reuse the same configuration if it was already generated for 
            # the scenario with same node cnt and iteration number 
            # (but different bundaryEnabled)
            if it.iteration in generated[s.nodeCnt]:
                print 'Reusing', generated[s.nodeCnt][it.iteration].name()
                continue
            
            if len(generators) >= cpus:
                finalizeOldestGenerator()
                
                
            if it.generator == 'simple':
                p = Process(target=generateConfig, args=(1, it.nodeCnt-1, it.othersCnt, it.baseCfgPath(), 0,))
            elif it.generator == 'complex':
                IP_FACTOR = 0.25
                p = Process(target=generateComplexRandomConfig,
                            args=(
                                            100, #area size 
                                            120, #external area size 
                                            10, #scale
                                            [[0, 1], [1, 2]], # distribution of teams
                                            [[1, 1, 0], [1, 0, 1]], # distribution of leaders 
                                            [[it.nodeCnt-1,nodeCnt-1,0],[it.nodeCnt-1,0,it.nodeCnt-1]], #distribution of members 
                                            [it.othersCnt, it.othersCnt], # distribution of others 
                                            it.baseCfgPath(), 
                                            [int(ceil(it.nodeCnt*IP_FACTOR)), int(ceil(it.nodeCnt*IP_FACTOR))], # distribution of IP-enabled nodes
                                            ))
            else:
                raise Error('Unsupported generator: ' + it.generator)
            generated[s.nodeCnt][it.iteration] = it
            generators.append(p)
            p.start()
    while len(generators) > 0:
        finalizeOldestGenerator()
    print 'Generating done'


simulated = []


#command = "C:/Program Files (x86)/Java/jdk7/bin/java.exe"
command = 'java'

def cleanup():
    timeout_sec = 5
    for p in [s.simulation for s in simulated]:  # list of your processes
        p_sec = 0
        for second in range(timeout_sec):
            if p.poll() == None:
                time.sleep(1)
                p_sec += 1
        if p_sec >= timeout_sec:
            p.kill()  # supported from python 2.6
  

atexit.register(cleanup)

def finalizeSimulation(iteration):
    iteration.simulation.wait() 
    os.remove(iteration.omnetppPath() + '.ini')
    os.remove(iteration.loggingPropertiesPath())
    iteration.stdOut.flush()
    iteration.stdOut.close()
    iteration.stdOut = None
    iteration.simulation = None
    simulated.remove(iteration)


def simulateScenario(iteration):
    #folder = root + '\simulation-results\%d\\' % (nodeCnt)        
    #prefix = '%d-%d-' % (nodeCnt, iteration)
    classpath = root + '\\..\\dist\\*;.'
    
    #logPropsFile = prefix + 'logging.properties'
    #logFile = folder + prefix + 'jdeeco.log'
    #logFile = logFile.replace('\\', '/')
    
    #stdoutName = folder + prefix + 'stdout.log'
    
    
    copyfile(root + '\\analysis\\logging.properties', iteration.loggingPropertiesPath())
    with open(iteration.loggingPropertiesPath() , 'a') as f:
        print>>f, '\n\njava.util.logging.FileHandler.pattern=' + iteration.logTemplatePath().replace('\\', '/')
   
    cmd = [command, '-cp', classpath,
           '-Ddeeco.receive.cache.deadline=500',
           '-Ddeeco.publish.individual=true',
           '-Ddeeco.boundary.disable=%s' % ('false' if iteration.boundaryEnabled else 'true'),
           '-Ddeeco.publish.packetsize=3000',
           '-Ddeeco.publish.period=1000',
           '-Ddeeco.rebroadcast.delay=1000',
           '-Djava.util.logging.config.file=%s' % (iteration.loggingPropertiesPath().replace('\\', '/')),
           'cz.cuni.mff.d3s.jdeeco.simulation.demo.Main',
           iteration.componentCfgPath(), iteration.siteCfgPath(), iteration.omnetppPath() ]
    
    if len(simulated) >= cpus:
        finalizeSimulation(simulated[0])
    
    print 'Evaluating', iteration.name() 
    print 'Executing: ', ' '.join(cmd)
    
    iteration.stdOut = open(iteration.stdOutPath(), 'w')
    iteration.simulation = Popen(cmd, stderr=STDOUT, stdout=iteration.stdOut)
     
    simulated.append(iteration)


def simulate():
    print 'Simulating...'
    
    for s in scenarios:       
        for it in s.iterations:    
            simulateScenario(it)
    
    # finalize the rest    
    while len(simulated) > 0:
        finalizeSimulation(simulated[0])
    print 'Simulation done'



def analyzeScenario(iteration):    
   
    
    with open(iteration.genericAnalysisStdoutPath(), 'w') as genericStdout:
        oldStdOut = sys.stdout
        sys.stdout = genericStdout
        a = GenericAnalysis()
        a.analyze(iteration.logPath())        
        sys.stdout = oldStdOut
        iteration.genericAnalysis = a 
    
    with open(iteration.demoAnalysisStdoutPath(), 'w') as demoStdout:
        oldStdOut = sys.stdout
        sys.stdout = demoStdout
        
        a = DemoAnalysis()
        a.analyze(iteration.logPath(), iteration.componentCfgPath())                   
        sys.stdout = oldStdOut
        iteration.demoAnalysis = a 

    a = NeighborAnalysis()
    a.analyze(iteration.componentCfgPath())
    iteration.neighborAnalysis = a

def parallelAnalyze(qin, qout):
    iteration = qin.get()
    analyzeScenario(iteration)
    qout.put(iteration)

analyses = []
def callParallelAnalyze(iteration):
    qin = Queue()
    qout = Queue()
    p = Process(target=parallelAnalyze, args=(qin, qout,))
    Analysis = namedtuple('Analysis', 'p qin qout iteration')
    analyses.append(Analysis(p, qin, qout, iteration))
    p.start()
    qin.put(iteration)

def finalizeOldestParallelAnalyze():
    if len(analyses) == 0:
        return
    a = analyses[0]
    iteration = a.iteration    
    it = a.qout.get()
    a.p.join()
    iteration.genericAnalysis = it.genericAnalysis
    iteration.demoAnalysis = it.demoAnalysis
    iteration.neighborAnalysis = it.neighborAnalysis
    analyses.pop(0)


def analyze():
    print 'Analyzing...'
    
    for s in scenarios:    
        for it in s.iterations:   
            print 'Analyzing', it.name()         
            #analyzeScenario(it)
            if len(analyses) > cpus:
                finalizeOldestParallelAnalyze()
            callParallelAnalyze(it)
        while len(analyses) > 0:
            finalizeOldestParallelAnalyze()
                
        mode = 'w'
        # if this is a continuation of a previous run, append
        if s.start > 0:
            mode = 'a'
        # demo analysis
        with open(s.demoResultsPath(), mode) as results: 
            for it in s.iterations:
                a = it.demoAnalysis
                np.savetxt(results, zip(a.resTimes, a.resTimesNetwork, a.hops, a.versionDifs), fmt='%d')
                          
        
        # generic analysis
        with open(s.genericResultsPath(), mode) as results: 
            genericStats = [[it.genericAnalysis.sentMessagesCnt, it.genericAnalysis.receivedMessagesCnt,
                             it.demoAnalysis.shouldDiscover, it.demoAnalysis.reallyDiscovered, it.genericAnalysis.boundaryHits] for it in s.iterations]           
            np.savetxt(results, genericStats, fmt='%d')
            
        
        # neighbor analysis
        with open(s.neighborResultsPath(), mode) as results: 
            neighbors = [cnt for it in s.iterations for cnt in it.neighborAnalysis.neighborCnts]            
            np.savetxt(results, neighbors, fmt='%d')
            
            
        for it in s.iterations:
            it.demoAnalysis = None  
            it.genericAnalysis = None
            it.neighborAnalysis = None
                
    print 'Analysis done'

def colorBoxplot(bp, isSecond):
    mycolor = '#E24A33'
    if isSecond:
        mycolor = '#348ABD'
    pylab.setp(bp['boxes'], color=mycolor)
    pylab.setp(bp['whiskers'], color=mycolor)
    pylab.setp(bp['fliers'], marker='None')
    


def plotMessageCounts():
    import pandas as pd

    dataWithoutBoundary = [
        ['F', s.messageStats[1], s.messageStats[0] - s.messageStats[1]] for s in scenariosWithoutBoundary]
    dataWithBoundary = [
        ['T', s.messageStats[1], s.messageStats[0] - s.messageStats[1]] for s in scenariosWithBoundary]
    df = pd.DataFrame(dataWithoutBoundary + dataWithBoundary, columns=['boundary', 'received', 'dropped'])    



    fig = pylab.figure(2, facecolor='white')
    ax = fig.add_subplot(111)
    axes = [fig.add_subplot(121), fig.add_subplot(122)]
    
    
    yticks = range(0, 185000, 5000)
    xticksLabels = [s.tickLabel() for s in scenariosWithoutBoundary]
    
    #ax.set_yticks(yticks)
    #ax.set_yticklabels(map(lambda x: x/1000, yticks))
    ax.set_frame_on(False)
    ax.set_ylabel('number of messages [in thousands]')
    ax.set_xlabel('total number of nodes [firefighters/others]')
    ax.set_xticklabels([])
    ax.tick_params(axis='x', pad=20)
    
    
    plt1 = df.loc[df['boundary'] == 'F'].plot(kind='bar', stacked=True, ax=axes[0]);
    axes[0].set_title('Boundary disabled')    
    axes[0].set_yticklabels([])
    axes[0].set_xticklabels(xticksLabels)
    #axes[0].set_yticks(yticks)
    
    pylab.setp(axes[0].xaxis.get_majorticklabels(), rotation=0 )
    
    plt2 = df.loc[df['boundary'] == 'T'].plot(kind='bar', stacked=True, ax=axes[1], legend=False);
    axes[1].set_title('Boundary enabled')
    #axes[1].set_yticks(yticks)
    axes[1].set_yticklabels([])
    axes[1].set_xticklabels(xticksLabels)    
    pylab.setp(axes[1].xaxis.get_majorticklabels(), rotation=0 )    

    
def setBoxColors(pylab, bp, color):
    pylab.setp(bp['boxes'], color=color)
    pylab.setp(bp['caps'], color=color)
    pylab.setp(bp['whiskers'], color=color)
    pylab.setp(bp['fliers'], marker='None')
    pylab.setp(bp['medians'], color=color)
    
    pylab.setp(bp['boxes'], linewidth=2)
    pylab.setp(bp['caps'], linewidth=2)
    pylab.setp(bp['whiskers'], linewidth=2)
    pylab.setp(bp['fliers'], linewidth=2)
    pylab.setp(bp['medians'], linewidth=2)
    
    

def plotBoundaryBoxplot(scenarios, valuesAttribute, split):    
    xGapWidth = 0
    xTicks = [0]
    nodeCnts = []
    for s in scenarios:
        nodeCnts.append(s.nodeCnt)
    uniqueList = list(set(nodeCnts))
    uniqueList.sort()
    xLabels = ['' for x in range(len(uniqueList))]
    for cnt in uniqueList:
        xGapWidth += cnt
    xGapWidth = xGapWidth / len(xTicks)
    partialSum = xGapWidth
    for cnt in uniqueList:
        xTicks.append(partialSum)
        partialSum += xGapWidth
    width = xGapWidth / 5
    for s in scenarios:
        positionOffset = 0
        if split:
            if s.boundaryEnabled:
                positionOffset = width/1.5
            else:
                positionOffset = -width/1.5
        bp = pylab.boxplot(getattr(s, valuesAttribute), positions = [(xGapWidth*(uniqueList.index(s.nodeCnt) + 1))+positionOffset], widths = width) 
        if s.boundaryEnabled:
            color = '#348ABD'
        else: 
            color = '#E24A33'
            xLabels[uniqueList.index(s.nodeCnt)] = s.tickLabel() #s.4*str(s.nodeCnt) + '/' + 2*str(s.othersCnt)
        setBoxColors(pylab, bp, color)
        
    xTicks.append(xTicks[1] + xTicks[len(xTicks) - 1])
    xLabels = [''] + xLabels
    
    pylab.axes().set_xticks(xTicks)
    pylab.axes().set_xticklabels(xLabels)
    
    if split:
        hB, = pylab.plot([1,1],'#348ABD')
        hR, = pylab.plot([1,1],'#E24A33')
        pylab.legend((hB, hR),('Boundary Condition enabled', 'Boundary Condition disabled'), loc='upper left')
    
def plotResponseTimes(scenarios):
    pylab.figure(0).set_facecolor('white')    
    plotBoundaryBoxplot(scenarios, 'node2nodeResponseTimes', False)     
    pylab.axes().set_ylabel("time [s]");
    pylab.axes().set_xlabel("total number of nodes [firefighters/others]");
    pylab.axes().set_yticks(range(0, 60000, 5000))
    pylab.axes().set_yticklabels(range(0, 60, 5))   
    
def plotNeighborCounts():
    pylab.figure(1).set_facecolor('white')    
    plotBoundaryBoxplot(scenarios, 'neighbors', True)    
    pylab.axes().set_ylabel("number of neighbors");
    pylab.axes().set_xlabel("total number of nodes [firefighters/others]");       

def plotDiscoveryRate():
    pylab.figure(3).set_facecolor('white')    
    plotBoundaryBoxplot(scenarios, 'discoveryRatio', True)    
    pylab.axes().set_ylabel("discovery ratio");
    pylab.axes().set_xlabel("total number of nodes [firefighters/others]");       
    
def plotBoundaryHits():    
    pylab.figure(4).set_facecolor('white')    
    plotBoundaryBoxplot(scenarios, 'boundaryHits', True)    
    pylab.axes().set_ylabel("boundary hits");
    pylab.axes().set_xlabel("total number of nodes [firefighters/others]");       
    
def plot():    
    print 'Plotting...'
        
    pylab.hold(True)

    for s in scenarios:        
        with open(s.demoResultsPath() , 'r') as resultsFile: 
            contents = np.loadtxt(resultsFile)
            if len(contents) == 0:
                s.node2nodeResponseTimes = []
            else:
                # if there is only one row, duplicate it so that the selectors don't fail
                if len(contents.shape) == 1:
                    contents = np.vstack((contents, contents))
                s.node2nodeResponseTimes = map(int, contents[:, 1])
        with open(s.genericResultsPath(), 'r') as resultsFile: 
            contents = np.loadtxt(resultsFile)
            # if there is only one row, duplicate it so that the selectors don't fail
            if len(contents.shape) == 1:
                contents = np.vstack((contents, contents))
            sent = map(int, contents[:, 0])            
            received = map(int, contents[:, 1])            
            s.messageStats = [average(sent), average(received), average(received)*1.0/average(sent)]
            s.discoveryRatio = map(lambda should, did: did * 1.0 / should, map(int, contents[:, 2]), map(int, contents[:, 3]))
            s.boundaryHits = map(int, contents[:, 4])
        with open(s.neighborResultsPath() , 'r') as resultsFile:
            contents = np.loadtxt(resultsFile)
            s.neighbors = map(int, contents)        

    
    plotResponseTimes(scenarios)
    plotMessageCounts()
    plotNeighborCounts()
    plotDiscoveryRate()
    plotBoundaryHits()
    
    pylab.show()

    print 'Plotting done'
     

    
    
def duplicateScenariosForBoundary():
    oldScenarios = scenarios[:]
    for s in oldScenarios:
        s2 = Scenario(s.nodeCnt, s.othersCnt, s.iterationCnt, not s.boundaryEnabled, s.generator, s.start)
        scenarios.append(s2)
        if s.boundaryEnabled:
            scenariosWithBoundary.append(s)
            scenariosWithoutBoundary.append(s2)
        else:
            scenariosWithBoundary.append(s2)
            scenariosWithoutBoundary.append(s)
    scenarios.sort(key=lambda x: x.nodeCnt)
    scenariosWithBoundary.sort(key=lambda x: x.nodeCnt)
    scenariosWithoutBoundary.sort(key=lambda x: x.nodeCnt)

def backupResults():
    from itertools import ifilter
    from fnmatch import fnmatch

    ext = '.csv'
    fnPattern = '*'+ext
    source_dir = 'simulation-results'
    dest_dir = source_dir + '\\backup'

    for dirName, subdirList, fileList in os.walk(source_dir):

        # generate list of files in directory with desired extension
        matches = ifilter(lambda fname: fnmatch(fname, fnPattern), fileList)

        # skip subdirectory if it does not contain any files of interest
        if not matches:
            continue
        try:
            os.makedirs(dest_dir)
        except OSError as e:
            pass  
        #  copy each file to destination directory
        for fname in matches:
          copyfile(source_dir + '\\' + fname, dest_dir + '\\' + fname)
          
if __name__ == '__main__':
    #evaluations = {4:10, 8:10, 12: 10, 16:10, 20:10}
    #evaluations = {8:10, 12: 10, 16:10, 20:10, 24:10, 28:10}
    #evaluations = {2:10, 4:10, 8:10, 12: 10, 16:10, 20:10}

    
    #simple
    evaluations = {}    
    for i in range(4,30,4): #30
        evaluations[i] = 5*cpus
    for nodeCnt in evaluations.keys():    
        scenarios.append(Scenario(nodeCnt, nodeCnt/2, evaluations[nodeCnt], False, 'simple'))
    #duplicateScenariosForBoundary()   
    plot()


    try:
        generate()
        simulate()    
        analyze()
    except Exception:
        print 'Step error'     
          
    plot()
    

    scenarios = []
    scenariosWithBoundary = []
    scenariosWithoutBoundary = []
    
    #complex
    evaluations = {}    
    for i in range(2,20,2): #20
        evaluations[i] = 5*cpus
    # init with only scenarios with disabled boundary (they enbaled counterparts will be created automatically after the generation step)
    for nodeCnt in evaluations.keys():    
        scenarios.append(Scenario(nodeCnt, nodeCnt, evaluations[nodeCnt], False, 'complex'))
    duplicateScenariosForBoundary()   

    try:
        generate()
        simulate()    
        analyze()
    except Exception:
        print 'Step error'
        
    #plot()

    scenarios = []
    scenariosWithBoundary = []
    scenariosWithoutBoundary = []

    # move analysis results
    backupResults()

    #further simple iterations
    evaluations = {}    
    for i in range(4,30,4):#30
        evaluations[i] = 5*cpus
    # init with only scenarios with disabled boundary (they enbaled counterparts will be created automatically after the generation step)
    for nodeCnt in evaluations.keys():
        # continue after the previous iterations
        scenarios.append(Scenario(nodeCnt, nodeCnt/2, evaluations[nodeCnt], False, 'simple', 5*cpus)) #5*cpus
    duplicateScenariosForBoundary()   

    try:
        generate()
        simulate()    
        analyze()
    except Exception:
        print 'Step error'
        
    #plot()

    scenarios = []
    scenariosWithBoundary = []
    scenariosWithoutBoundary = []


    #further complex evaluations
    evaluations = {}    
    for i in range(20,29,2): #20-28
        evaluations[i] = 5*cpus
    # init with only scenarios with disabled boundary (they enbaled counterparts will be created automatically after the generation step)
    for nodeCnt in evaluations.keys():    
        scenarios.append(Scenario(nodeCnt, nodeCnt, evaluations[nodeCnt], False, 'complex'))
    duplicateScenariosForBoundary()   

    try:
        generate()
        simulate()    
        analyze()
    except Exception:
        print 'Step error'
              