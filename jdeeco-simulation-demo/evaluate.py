import os, sys
from generator.outsidersScenario import generate2AreasPlayground
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
cpus = 3

class Scenario():
    IP_FACTOR = 0.25
    BUILDING_SIZE = 5
    RADIO_DISTANCE = 25
    
    def __init__(self, margin, density, iterationCnt, boundaryEnabled, start = 0):
        self.density = density
        self.margin = margin
        self.iterationCnt = iterationCnt
        self.boundaryEnabled = boundaryEnabled
        self.iterations = []
        self.start = start
        self.insideNodes = self.density * self.BUILDING_SIZE * self.BUILDING_SIZE * 2
        self.totalNodes = self.density * (2*self.margin + self.BUILDING_SIZE) * (3*self.margin+2*self.BUILDING_SIZE)  
        for i in range(iterationCnt):
            self.iterations.append(ScenarioIteration(self, margin, density, start + i, boundaryEnabled)) 
    def folder(self):
        return 'simulation-results\\%d' % (self.margin)
    def folderPath(self):
        return root + '\\simulation-results\\%d' % (self.margin)
    def genericResultsPath(self): 
        return root + '\\simulation-results\\results-generic-%d-%s.csv' % (self.margin, 't' if self.boundaryEnabled else 'f')
    def demoResultsPath(self): 
        return root + '\\simulation-results\\results-demo-%d-%s.csv' % (self.margin, 't' if self.boundaryEnabled else 'f')
    def neighborResultsPath(self): 
        return root + '\\simulation-results\\results-neighbors-%d-%s.csv' % (self.margin, 't' if self.boundaryEnabled else 'f')
    def tickLabel(self):
        return '%d/%d\n(%d)' % (self.insideNodes, self.totalNodes, self.margin)
       
    
class ScenarioIteration:
    def __init__(self, scenario, margin, density, iteration, boundaryEnabled):
        self.scenario = scenario
        self.margin = margin
        self.density = density
        self.iteration = iteration
        self.boundaryEnabled = boundaryEnabled       
        self.stdOut = None
        self.simulation = None
        self.demoAnalysis = None
        self.genericAnalysis = None
        self.neighborAnalysis = None
    def folder(self):
        return self.scenario.folder()
    def prefix(self):
        return '%d-%d-%s-' % (self.margin, self.iteration, 't' if self.boundaryEnabled else 'f')
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
        return root + '\\' + self.folder() + '\\' + '%d-%d-' % (self.margin, self.iteration)
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
generatorQueues = {}
generator2Iteration = {}
def finalizeOldestGenerator():
    if len(generators) == 0:
        return
    g = generators[0]
    g.join()
    
    assert generator2Iteration[g].scenario.totalNodes == generatorQueues[g].get()
    generators.pop(0)
    generator2Iteration.pop(g)
    generatorQueues.pop(g)
    
def generate():
    generated = {}
    print 'Generating configurations...'
    for s in scenarios:
        try:
            os.makedirs(s.folderPath())
        except OSError as e:
            pass        
        if s.margin not in generated:
            generated[s.margin] = {}
            
        for it in s.iterations:
            print 'Generating ', it.name()
            # reuse the same configuration if it was already generated for 
            # the scenario with same node cnt and iteration number 
            # (but different bundaryEnabled)
            if it.iteration in generated[s.margin]:
                print 'Reusing', generated[s.margin][it.iteration].name()
                continue
            
            if len(generators) >= cpus:
                finalizeOldestGenerator()
                
            
            
            q = Queue()
            p = Process(target=generate2AreasPlayground, 
                        args=(s.density, 20, s.BUILDING_SIZE, s.BUILDING_SIZE, s.margin, s.RADIO_DISTANCE, 
                              [2,2,0], [s.IP_FACTOR, s.IP_FACTOR, s.IP_FACTOR], it.baseCfgPath(), q))
            
             
            generated[s.margin][it.iteration] = it
            generators.append(p)
            generatorQueues[p] = q
            generator2Iteration[p] = it
           
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

def finalizeOldestSimulation():
    iteration = simulated[0]
    iteration.simulation.wait()
    simulated.pop(0)
    os.remove(iteration.omnetppPath() + '.ini')
    os.remove(iteration.loggingPropertiesPath())
    iteration.stdOut.flush()
    iteration.stdOut.close()
    iteration.stdOut = None
    iteration.simulation = None
    


def simulateScenario(iteration):
    classpath = root + '\\..\\dist\\*;.'
    
    copyfile(root + '\\analysis\\logging.properties', iteration.loggingPropertiesPath())
    with open(iteration.loggingPropertiesPath() , 'a') as f:
        print>>f, '\n\njava.util.logging.FileHandler.pattern=' + iteration.logTemplatePath().replace('\\', '/')
   
    cmd = [command, '-cp', classpath,
           '-Ddeeco.receive.cache.deadline=1500',
           '-Ddeeco.publish.individual=true',
           '-Ddeeco.boundary.disable=%s' % ('false' if iteration.boundaryEnabled else 'true'),
           '-Ddeeco.publish.packetsize=6000',
           '-Ddeeco.publish.period=2000',
           '-Ddeeco.rebroadcast.delay=1000',
           '-Djava.util.logging.config.file=%s' % (iteration.loggingPropertiesPath().replace('\\', '/')),
           'cz.cuni.mff.d3s.jdeeco.simulation.demo.Main',
           iteration.componentCfgPath(), iteration.siteCfgPath(), iteration.omnetppPath() ]
    
    if len(simulated) >= cpus:
        finalizeOldestSimulation()
    
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
        finalizeOldestSimulation()
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
    analyses.pop(0)
    iteration.genericAnalysis = it.genericAnalysis
    iteration.demoAnalysis = it.demoAnalysis
    iteration.neighborAnalysis = it.neighborAnalysis


def analyze():
    print 'Analyzing...'
    
    for s in scenarios:    
        for it in s.iterations:   
            print 'Analyzing', it.name()         
            #analyzeScenario(it)
            if len(analyses) >= cpus:
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
    


def plotMessageCounts(fig, scenarios):
    import pandas as pd

    dataWithoutBoundary = [
        ['F', s.messageStats[1], s.messageStats[0] - s.messageStats[1]] for s in scenariosWithoutBoundary]
    dataWithBoundary = [
        ['T', s.messageStats[1], s.messageStats[0] - s.messageStats[1]] for s in scenariosWithBoundary]
    df = pd.DataFrame(dataWithoutBoundary + dataWithBoundary, columns=['boundary', 'delivered', 'lost'])    



    fig = pylab.figure(fig, facecolor='white')
    ax = fig.add_subplot(111)
    axes = [fig.add_subplot(121), fig.add_subplot(122)]
    

    maxMessageCount = max([s.messageStats[0] for s in scenariosWithoutBoundary])
    STEP = 20000
    yticks = range(0, int(maxMessageCount - maxMessageCount % STEP + STEP + 1), STEP)

    xticksLabels = [s.tickLabel() for s in scenariosWithoutBoundary]
    
    ax.set_yticks(yticks)
    ax.set_yticklabels(map(lambda x: x/1000, yticks))
    ax.set_frame_on(False)
    ax.set_ylabel('replicas disseminated [in thousands]')
    ax.set_xlabel('total number of nodes [firefighters/others]')
    ax.set_xticklabels([])
    ax.tick_params(axis='x', pad=20)
    
    
    plt1 = df.loc[df['boundary'] == 'F'].plot(kind='bar', stacked=True, ax=axes[0]);
    axes[0].set_title('without boundary')    
    axes[0].set_yticklabels([])
    axes[0].set_xticklabels(xticksLabels)
    axes[0].set_yticks(yticks)
    
    pylab.setp(axes[0].xaxis.get_majorticklabels(), rotation=0 )
    
    plt2 = df.loc[df['boundary'] == 'T'].plot(kind='bar', stacked=True, ax=axes[1], legend=False);
    axes[1].set_title('with boundary')
    axes[1].set_yticks(yticks)
    axes[1].set_yticklabels([])
    axes[1].set_xticklabels(xticksLabels)    
    pylab.setp(axes[1].xaxis.get_majorticklabels(), rotation=0 )    

color2 = '#C4CC35'
color1 = '#423F8C'
lineThickness = 3
    
def setBoxColors(pylab, bp, color):
    pylab.setp(bp['boxes'], color=color)
    pylab.setp(bp['caps'], color=color)
    pylab.setp(bp['whiskers'], color=color)
    pylab.setp(bp['fliers'], marker='None')
    pylab.setp(bp['medians'], color=color)
    
    pylab.setp(bp['boxes'], linewidth=lineThickness)
    pylab.setp(bp['caps'], linewidth=lineThickness)
    pylab.setp(bp['whiskers'], linewidth=lineThickness)
    pylab.setp(bp['fliers'], linewidth=lineThickness)
    pylab.setp(bp['medians'], linewidth=lineThickness)    
    

def plotBoundaryBoxplot(scenarios, valuesAttribute, split):    
    xGapWidth = 0
    xTicks = [0]
    margins = []
    if split:
        boundaryEnabledColor = color1
        boundaryDisabledColor = color2
    else:
        boundaryDisabledColor = color1
        boundaryEnabledColor = color2
    for s in scenarios:
        margins.append(s.margin)
    uniqueList = list(set(margins))
    uniqueList.sort()
    xLabels = ['' for x in range(len(uniqueList))]
    for cnt in uniqueList:
        xGapWidth += cnt
    xGapWidth = xGapWidth / len(xTicks)
    partialSum = xGapWidth
    for cnt in uniqueList:
        xTicks.append(partialSum)
        partialSum += xGapWidth
    width = xGapWidth / (len(scenarios)/2)
    for s in scenarios:
        positionOffset = 0
        if split:
            if s.boundaryEnabled:
                positionOffset = width/1.5
            else:
                positionOffset = -width/1.5
        bp = pylab.boxplot(getattr(s, valuesAttribute), positions = [(xGapWidth*(uniqueList.index(s.margin) + 1))+positionOffset], widths = width) 
        if s.boundaryEnabled:
            color = boundaryEnabledColor #'#348ABD'
        else: 
            color = boundaryDisabledColor #'#E24A33'
            xLabels[uniqueList.index(s.margin)] = s.tickLabel()
        setBoxColors(pylab, bp, color)
        
    xTicks.append(xTicks[1] + xTicks[len(xTicks) - 1])
    xLabels = [''] + xLabels
    
    pylab.axes().set_xticks(xTicks)
    pylab.axes().set_xticklabels(xLabels)
    pylab.axes().yaxis.grid(True, linestyle=':', which='major', color='lightgrey',alpha=0.8)
    if split:
        hB, = pylab.plot([0,0],boundaryEnabledColor) #'#348ABD')
        hR, = pylab.plot([0,0],boundaryDisabledColor) #'#E24A33')
        pylab.legend((hB, hR),('Boundary Condition enabled', 'Boundary Condition disabled'), loc='upper left')
    
def plotResponseTimes(fig, scenarios, splitBoundary):
    pylab.figure(fig).set_facecolor('white')    
    plotBoundaryBoxplot(scenarios, 'node2nodeResponseTimes', splitBoundary)     
    pylab.axes().set_ylabel("time [s]");
    pylab.axes().set_xlabel("total number of nodes [firefighters/others]");
    pylab.axes().set_yticks(range(0, 60000, 5000))
    pylab.axes().set_yticklabels(range(0, 60, 5))   
    
def plotNeighborCounts(fig, scenarios, splitBoundary):
    pylab.figure(fig).set_facecolor('white')    
    plotBoundaryBoxplot(scenarios, 'neighbors', splitBoundary)    
    pylab.axes().set_ylabel("number of neighbors");
    pylab.axes().set_xlabel("total number of nodes [firefighters/others]");       

def plotDiscoveryRate(fig, scenarios, splitBoundary):
    pylab.figure(fig).set_facecolor('white')    
    plotBoundaryBoxplot(scenarios, 'discoveryRatio', splitBoundary)    
    pylab.axes().set_ylabel("discovery ratio");
    pylab.axes().set_xlabel("total number of nodes [firefighters/others]");       
    
def plotBoundaryHits(fig, scenarios, splitBoundary):    
    pylab.figure(fig).set_facecolor('white')    
    plotBoundaryBoxplot(scenarios, 'boundaryHits', splitBoundary)    
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

    pylab.rc('axes', color_cycle=[color1, color2])
      
    scenairosToPlot = scenarios
    split = True
    
    plotResponseTimes(0, scenairosToPlot, split)
    plotMessageCounts(1, scenairosToPlot)
    plotNeighborCounts(2, scenairosToPlot, split)
    plotDiscoveryRate(3, scenairosToPlot, split)
    plotBoundaryHits(4, scenairosToPlot, split)
    
    pylab.show()

    print 'Plotting done'
     

    
    
def duplicateScenariosForBoundary():
    oldScenarios = scenarios[:]
    for s in oldScenarios:
        s2 = Scenario(s.margin, s.density, s.iterationCnt, not s.boundaryEnabled, s.start)
        scenarios.append(s2)
        if s.boundaryEnabled:
            scenariosWithBoundary.append(s)
            scenariosWithoutBoundary.append(s2)
        else:
            scenariosWithBoundary.append(s2)
            scenariosWithoutBoundary.append(s)
    scenarios.sort(key=lambda x: x.margin)
    scenariosWithBoundary.sort(key=lambda x: x.margin)
    scenariosWithoutBoundary.sort(key=lambda x: x.margin)
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
 
    scenarios = []
    scenariosWithBoundary = []
    scenariosWithoutBoundary = []
        
    evaluations = {}    
    for i in range(1,5): 
        evaluations[i] = 10#*cpus
    # init with only scenarios with disabled boundary (they enbaled counterparts will be created automatically after the generation step)
    for margin in evaluations.keys():    
        scenarios.append(Scenario(margin, 1, evaluations[margin], False))
    duplicateScenariosForBoundary()   

    
    
    #generate()
    #simulate()    
    #analyze()
    
    plot()

