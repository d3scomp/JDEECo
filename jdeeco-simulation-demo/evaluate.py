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


root = os.path.dirname(os.path.realpath(__file__))

class Scenario():
    def __init__(self, nodeCnt, othersCnt, iterationCnt, boundaryEnabled, generator):
        self.nodeCnt = nodeCnt
        self.othersCnt = othersCnt
        self.iterationCnt = iterationCnt
        self.boundaryEnabled = boundaryEnabled
        self.generator = generator
        self.iterations = []
        for i in range(iterationCnt):
            self.iterations.append(ScenarioIteration(self, nodeCnt, othersCnt, i, boundaryEnabled, generator)) 
    def folder(self):
        return 'simulation-results\\%d' % (self.nodeCnt)
    def folderPath(self):
        return root + '\\simulation-results\\%d' % (self.nodeCnt)
    def genericResultsPath(self): 
        return root + '\\simulation-results\\results-generic-%d-%s.csv' % (self.nodeCnt, 't' if self.boundaryEnabled else 'f')
    def demoResultsPath(self): 
        return root + '\\simulation-results\\results-demo-%d-%s.csv' % (self.nodeCnt, 't' if self.boundaryEnabled else 'f')
    def neighborResultsPath(self): 
        return root + '\\simulation-results\\results-neighbors-%d-%s.csv' % (self.nodeCnt, 't' if self.boundaryEnabled else 'f')
    def plotTick(self):
        return str(self.nodeCnt) + 't' if self.boundaryEnabled else 'f'
       
    
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
        return self.prefix() + 'omnetpp.ini'    
    def name(self):
        return self.prefix() + 'scenario'



# main list of scenarios
scenarios = []
scenariosWithBoundary = []
scenariosWithoutBoundary = []




# generic part
######################################################################
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
                break
                
            if it.generator == 'simple':
                generateConfig(1, it.nodeCnt-1, it.othersCnt, it.baseCfgPath(), 0)
            elif it.generator == 'complex':
                IP_FACTOR = 0.25
                generateComplexRandomConfig(
                                            100, #area size 
                                            120, #external area size 
                                            10, #scale
                                            [[0, 1], [0]], # distribution of teams
                                            [[1, 1], [1,0]], # distribution of leaders 
                                            [[it.nodeCnt-1,it.nodeCnt-1],[it.nodeCnt-1]], #distribution of members 
                                            [it.othersCnt, it.othersCnt], # distribution of others 
                                            it.baseCfgPath(), 
                                            [int(ceil(it.nodeCnt*IP_FACTOR)), int(ceil(it.nodeCnt*IP_FACTOR))] # distribution of IP-enabled nodes
                                            )
            else:
                raise Error('Unsupported generator: ' + it.generator)
            generated[s.nodeCnt][it.iteration] = it
    print 'Generating done'


simulated = []
cpus = 3

command = "java"

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
    os.remove(iteration.omnetppPath())
    os.remove(iteration.loggingPropertiesPath())
    iteration.stdOut.flush()
    iteration.stdOut.close()
    simulated.remove(iteration)


def simulateScenario(iteration):
    #folder = root + '\simulation-results\%d\\' % (nodeCnt)        
    #prefix = '%d-%d-' % (nodeCnt, iteration)
    classpath = root + '\..\dist\*;.'
    
    #logPropsFile = prefix + 'logging.properties'
    #logFile = folder + prefix + 'jdeeco.log'
    #logFile = logFile.replace('\\', '/')
    
    #stdoutName = folder + prefix + 'stdout.log'
    
    
    copyfile(root + '\simulation-results\logging.properties', iteration.loggingPropertiesPath())
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
   
    print 'Analyzing', iteration.name()  
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

def analyze():
    print 'Analyzing...'
    
    for s in scenarios:    
        for it in s.iterations:           
            analyzeScenario(it)
       
        # demo analysis
        with open(s.demoResultsPath(), 'w') as results: 
            for it in s.iterations:
                a = it.demoAnalysis
                np.savetxt(results, zip(a.resTimes, a.resTimesNetwork, a.hops, a.versionDifs), fmt='%d')
                it.demoAnalysis = None            
        
        # generic analysis
        with open(s.genericResultsPath(), 'w') as results: 
            messageStats = [[it.genericAnalysis.sentMessagesCnt, it.genericAnalysis.receivedMessagesCnt] for it in s.iterations]           
            np.savetxt(results, messageStats, fmt='%d')
            for it in s.iterations:
                it.genericAnalysis = None
        
        # neighbor analysis
        with open(s.neighborResultsPath(), 'w') as results: 
            neighbors = [cnt for it in s.iterations for cnt in it.neighborAnalysis.neighborCnts]            
            np.savetxt(results, neighbors, fmt='%d')
            for it in s.iterations:
                it.neighborAnalysis = None
                
    print 'Analysis done'

def colorBoxplot(bp, isSecond):
    mycolor = '#E24A33'
    if isSecond:
        mycolor = '#348ABD'
    pylab.setp(bp['boxes'], color=mycolor)
    pylab.setp(bp['whiskers'], color=mycolor)
    pylab.setp(bp['fliers'], marker='None')
    

def plotPandas():
    import pandas as pd
    
    dataWithoutBoundary = [[s.messageStats[1], s.messageStats[0] - s.messageStats[1]] for s in scenariosWithoutBoundary]
    dataWithBoundary = [[s.messageStats[1], s.messageStats[0] - s.messageStats[1]] for s in scenariosWithBoundary]
    df = pd.DataFrame(dataWithoutBoundary, columns=['received', 'dropped'])
    df.plot(kind='bar', stacked=True);
    
    
    dataWithoutBoundary = [x for s in scenariosWithoutBoundary for x in s.node2nodeResponseTimes]
    dataWithBoundary = [x for s in scenariosWithBoundary for x in s.node2nodeResponseTimes]
    nodeCounts = [s.nodeCnt for s in scenariosWithoutBoundary for x in s.node2nodeResponseTimes]
    df = pd.DataFrame(zip(dataWithoutBoundary, dataWithBoundary), columns=['No boundary', 'Boundary'] )
    df['Node count'] = pd.Series(nodeCounts)
    df.boxplot(by='Node count')
    
def plot():    
    print 'Plotting...'
    
    
    pylab.hold(True)

    width = 1

   
    counts = []
    aggSent = []
    aggReceived = []
    aggRatio = []

    for s in scenarios:        
        with open(s.demoResultsPath() , 'r') as resultsFile: 
            contents = np.loadtxt(resultsFile)
            s.node2nodeResponseTimes = map(int, contents[:, 1])
        with open(s.genericResultsPath(), 'r') as resultsFile: 
            contents = np.loadtxt(resultsFile)
            sent = map(int, contents[:, 0])            
            received = map(int, contents[:, 1])
            s.messageStats = [average(sent), average(received), average(received)*1.0/average(sent)]
            aggSent.extend([average(sent)])
            aggReceived.extend([average(received)])
            aggRatio.extend([average(received)*1.0/average(sent)])
            
        with open(s.neighborResultsPath() , 'r') as resultsFile: 
            contents = np.loadtxt(resultsFile)
            s.neighbors = map(int, contents)
    
       
        positionOffset = -width/1.5
        if s.boundaryEnabled:
            positionOffset = width/1.5
            
        pylab.figure(0)
        bp = pylab.boxplot(s.node2nodeResponseTimes, positions = [s.nodeCnt+positionOffset], widths = width)        
        colorBoxplot(bp, s.boundaryEnabled)
        pylab.figure(1)
        bp = pylab.boxplot(s.neighbors, positions = [s.nodeCnt+positionOffset], widths = width)

        colorBoxplot(bp, s.boundaryEnabled)        
        counts.extend([s.nodeCnt])
        
    plotPandas()
    
    pylab.figure(2)
    lp = pylab.plot(counts, aggSent)
    lp = pylab.plot(counts, aggReceived)
    
    pylab.figure(0)
    pylab.title('End-to-end response')    
    pylab.axes().set_yticks(range(0, 60000, 5000))
    pylab.axes().set_yticklabels(range(0, 60, 5))    
    pylab.figure(1)
    pylab.title('Number of neighbors')
    
    nodeTicks = []
    nodeCounts = []
    offset = width/1.5
    for s in scenariosWithoutBoundary:
        nodeCounts.append(s.nodeCnt - offset)
        nodeCounts.append(s.nodeCnt + offset)
        nodeTicks.append('%df' %(s.nodeCnt))
        nodeTicks.append('%dt' %(s.nodeCnt))
        
    #nodeTicks = [s.plotTick() for s in scenariosWithoutBoundary] + [s.plotTick() for s in scenariosWithBoundary]
    #nodeCounts = [s.nodeCnt - 1 for s in scenariosWithoutBoundary] + [s.nodeCnt + 1 for s in scenariosWithBoundary]
    for fig in range(2):
        pylab.figure(fig)      
        pylab.axes().set_xticks(nodeCounts)       
        pylab.axes().set_xticklabels(nodeTicks)
        pylab.xlim(min(nodeCounts)-3,max(nodeCounts) +3)
    
    pylab.figure(0)
    pylab.savefig("simulation-results\\result-n2n-response.png")
    pylab.figure(1)
    pylab.savefig("simulation-results\\result-neighbors.png")    
    
    pylab.show()
  

    print 'Plotting done'
     

    
    
def duplicateScenariosForBoundary():
    oldScenarios = scenarios[:]
    for s in oldScenarios:
        s2 = Scenario(s.nodeCnt, s.othersCnt, s.iterationCnt, not s.boundaryEnabled, s.generator)
        scenarios.append(s2)
        if s.boundaryEnabled:
            scenariosWithBoundary.append(s)
            scenariosWithoutBoundary.append(s2)
        else:
            scenariosWithBoundary.append(s2)
            scenariosWithoutBoundary.append(s)
    
    
if __name__ == '__main__':
    #evaluations = {4:10, 8:10, 12: 10, 16:10, 20:10}
    #evaluations = {8:10, 12: 10, 16:10, 20:10, 24:10, 28:10}
    evaluations = {8:3, 12: 3}
    # init with only scenarios with disabled boundary (they enbaled counterparts will be created automatically after the generation step)
    for nodeCnt in evaluations.keys():    
        scenarios.append(Scenario(nodeCnt, nodeCnt/2, evaluations[nodeCnt], False, 'complex'))
    duplicateScenariosForBoundary()

        
    generate()
    simulate()
    analyze()
    plot()
