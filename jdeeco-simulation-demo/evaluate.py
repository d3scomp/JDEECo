import os, sys
from generator.simple import generateConfig
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

simulated = []

cpus = 3

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
root = os.path.dirname(os.path.realpath(__file__))

class Scenario():
    def __init__(self, nodeCnt, iterationCnt, boundaryEnabled, generator):
        self.nodeCnt = nodeCnt
        self.iterationCnt = iterationCnt
        self.boundaryEnabled = boundaryEnabled
        self.generator = generator
        self.iterations = []
        for i in range(iterationCnt):
            self.iterations.append(ScenarioIteration(self, nodeCnt, i, boundaryEnabled, generator)) 
    def folder(self):
        return 'simulation-results\\%d' % (self.nodeCnt)
    def folderPath(self):
        return root + '\\simulation-results\\%d' % (self.nodeCnt)
    def genericResultsPath(self): 
        return root + '\\simulation-results\\results-generic-%d.csv' % self.nodeCnt
    def demoResultsPath(self): 
        return root + '\\simulation-results\\results-demo-%d.csv' % self.nodeCnt
    def neighborResultsPath(self): 
        return root + '\\simulation-results\\results-neighbors-%d.csv' % self.nodeCnt
    def plotTick(self):
        return str(self.nodeCnt)
       
    
class ScenarioIteration:
    def __init__(self, scenario, nodeCnt, iteration, boundaryEnabled, generator):
        self.scenario = scenario
        self.nodeCnt = nodeCnt
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
    def componentCfgPath(self):
        return self.prefixPath() + 'component.cfg'
    def siteCfgPath(self):
        return self.prefixPath() + 'site.cfg'
    def omnetppPath(self):
        # needs to be relative path
        return self.prefix() + 'omnetpp.ini'    
    def name(self):
        return self.prefix() + 'scenario'

#evaluations = {4:10, 8:10, 12: 10, 16:10, 20:10}
#evaluations = {8:10, 12: 10, 16:10, 20:10, 24:10, 28:10}
evaluations = {8:3, 12: 3}

# GENERATE SCENARIOS
scenarios = []
for nodeCnt in evaluations.keys():    
    scenarios.append(Scenario(nodeCnt, evaluations[nodeCnt], False, 'simple'))

def generate():
    print 'Generating configurations...'
    for s in scenarios:
        try:
            os.makedirs(s.folderPath())
        except OSError as e:
            pass
        for it in s.iterations:
            print 'Generating ', it.name()
            if it.generator == 'simple':
                generateConfig(1, it.nodeCnt-1, 0, it.prefixPath(), 0)
            else:
                raise Error('Unsupported generator: ' + it.generator)
    print 'Generating done'


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
   
    cmd = ['java', '-cp', classpath,
           '-Ddeeco.receive.cache.deadline="500"',
           '-Ddeeco.publish.individual="true"',
           '-Ddeeco.boundary.disable="false"',
           '-Ddeeco.publish.packetsize="3000"',
           '-Ddeeco.publish.period="1000"',
           '-Ddeeco.rebroadcast.delay="1000"',
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
    #folder = root + '\simulation-results\%d\\' % (nodeCnt)        
    #prefix = '%d-%d-' % (nodeCnt, iteration)
    #stdoutNameGeneric = folder + prefix + 'analysis-generic.txt'
    #stdoutNameDemo = folder + prefix + 'analysis-demo.txt'
    #logName =  folder + prefix + 'jdeeco.log.0'
    #componentConfigName = folder + prefix + 'component.cfg'
   
    print 'Analyzing', iteration.name()  
    with open(iteration.genericAnalysisStdoutPath(), 'w') as genericStdout:
        oldStdOut = sys.stdout
        sys.stdout = genericStdout
        a = GenericAnalysis()
        a.analyze(iteration.logPath())        
        sys.stdout = oldStdOut
        iteration.genericAnalysis = a 
        #genericAnalyses[nodeCnt].append(a)       
         
        #pGeneric = Popen(['python', root + '\\analysis\\analyze_log.py', logName], stderr=STDOUT, stdout=genericStdout)
        #pGeneric.wait()
    
    with open(iteration.demoAnalysisStdoutPath(), 'w') as demoStdout:
        oldStdOut = sys.stdout
        sys.stdout = demoStdout
        
        a = DemoAnalysis()
        a.analyze(iteration.logPath(), iteration.componentCfgPath())                   
        sys.stdout = oldStdOut
        iteration.demoAnalysis = a 
        #demoAnalyses[nodeCnt].append(a)
        #pDemo = Popen(['python', root + '\\analysis\\analyze_demo.py', logName, componentConfigName], stderr=STDOUT, stdout=demoStdout)
        #pDemo.wait()


    a = NeighborAnalysis()
    a.analyze(iteration.componentCfgPath())
    iteration.neighborAnalysis = a
    #neighborAnalyses[nodeCnt].append(a)

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

def colorBoxplot(bp):
    pylab.setp(bp['boxes'], color='black')
    pylab.setp(bp['whiskers'], color='black')
    pylab.setp(bp['fliers'], marker='None')
def plot():    
    print 'Plotting...'
    
    
    pylab.hold(True)
   
    for s in scenarios:        
        with open(s.demoResultsPath() , 'r') as resultsFile: 
            contents = np.loadtxt(resultsFile)
            s.node2nodeResponseTimes = map(int, contents[:, 1])
        with open(s.genericResultsPath(), 'r') as resultsFile: 
            contents = np.loadtxt(resultsFile)
            sent = map(int, contents[:, 0])            
            received = map(int, contents[:, 1])
            s.messageStats = [average(sent), average(received), average(received)*1.0/average(sent)]
        with open(s.neighborResultsPath() , 'r') as resultsFile: 
            contents = np.loadtxt(resultsFile)
            s.neighbors = map(int, contents)
    
        pylab.figure(0)
        bp = pylab.boxplot(s.node2nodeResponseTimes, positions = [s.nodeCnt], widths = 2)        
        colorBoxplot(bp)
        pylab.figure(1)
        bp = pylab.boxplot(s.neighbors, positions = [s.nodeCnt], widths = 2)
        colorBoxplot(bp)
    
    pylab.figure(0)
    pylab.title('End-to-end response')    
    pylab.axes().set_yticks(range(0, 60000, 5000))
    pylab.axes().set_yticklabels(range(0, 60, 5))    
    pylab.figure(1)
    pylab.title('Number of neighbors')
    
    
    nodeTicks = [s.plotTick() for s in scenarios]
    nodeCounts = [s.nodeCnt for s in scenarios]
    for fig in range(2):
        pylab.figure(fig)      
        pylab.axes().set_xticks(nodeCounts)       
        pylab.axes().set_xticklabels(nodeTicks)
        pylab.xlim(min(nodeCounts)-3,max(nodeCounts) +3)
        
        
    pylab.show()
    
    print 'Plotting done'
     
    
    
    
    
if __name__ == '__main__':
    #generate()
    #simulate()
    #analyze()
    plot()
