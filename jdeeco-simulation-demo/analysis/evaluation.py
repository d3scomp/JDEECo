import os, sys
from simple import generateConfig
from complex import generateComplexRandomConfig
from analyze_demo import *
from analyze_log import *
from analyze_neighbors import *
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

color2 = '#C4CC35'
color1 = '#423F8C'
defaultRoot = os.path.dirname(os.path.realpath(__file__))

Analysis = namedtuple('Analysis', 'p qin qout iteration')
def parallelAnalyze(qin, qout):
    iteration = qin.get()
    Evaluation.analyzeScenario(iteration)
    qout.put(iteration)

simulated = []        
generators = []
analyses = []

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
      
class Scenario():
    def __init__(self, nodeCnt, othersCnt, iterationCnt, boundaryEnabled, generator, start = 0, evaluation=None):
        self.nodeCnt = nodeCnt
        self.othersCnt = othersCnt
        self.iterationCnt = iterationCnt
        self.boundaryEnabled = boundaryEnabled
        self.generator = generator
        self.iterations = []
        self.start = start
        self.evaluation = evaluation
        for i in range(iterationCnt):
            self.iterations.append(ScenarioIteration(self, nodeCnt, othersCnt, start + i, boundaryEnabled, generator)) 
    def folder(self):
        return 'simulation-results\\%d-%s' % (self.nodeCnt, self.generator[0])
    def folderPath(self):
        return self.evaluation.root + '\\simulation-results\\%d-%s' % (self.nodeCnt, self.generator[0])
    def genericResultsPath(self): 
        return self.evaluation.root + '\\simulation-results\\results-generic-%d-%s-%s.csv' % (self.nodeCnt, 't' if self.boundaryEnabled else 'f', self.generator[0])
    def demoResultsPath(self): 
        return self.evaluation.root + '\\simulation-results\\results-demo-%d-%s-%s.csv' % (self.nodeCnt, 't' if self.boundaryEnabled else 'f', self.generator[0])
    def neighborResultsPath(self): 
        return self.evaluation.root + '\\simulation-results\\results-neighbors-%d-%s-%s.csv' % (self.nodeCnt, 't' if self.boundaryEnabled else 'f', self.generator[0])
    def tickLabel(self):
        if self.generator == 'simple':
            return '%d/%d' % (self.nodeCnt, self.othersCnt)  
        else:
            return '%d/%d' % (2*self.nodeCnt, 1*self.othersCnt)
       
    
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
        return self.scenario.evaluation.root + '\\' + self.folder() + '\\' + self.prefix()
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
        return self.scenario.evaluation.root + '\\' + self.folder() + '\\' + '%d-%d-%s-' % (self.nodeCnt, self.iteration, self.generator[0])
    def componentCfgPath(self):
        return self.baseCfgPath() + 'component.cfg'
    def siteCfgPath(self):
        return self.baseCfgPath() + 'site.cfg'
    def omnetppPath(self):
        # needs to be relative path
        return 'omnetpp-' + self.prefix() + 'conf'    
    def name(self):
        return self.prefix() + 'scenario'


class Evaluation:
    def __init__(self, scenarios, generator, root=defaultRoot, cpus=3, javaCmd = 'java', classpathRoot = None):        
        self.root = os.path.dirname(os.path.realpath(root))
        self.generator = generator
        if classpathRoot is None:
            self.classpathRoot = self.root
        else:
            self.classpathRoot = classpathRoot
        self.cpus = cpus    
        # main list of scenarios
        self.scenarios = scenarios
        for s in self.scenarios: #: :type s: Scenario
            s.evaluation = self    
        self.scenariosWithBoundary =  [s for s in self.scenarios if s.boundaryEnabled] #: :type s: Scenario
        self.scenariosWithoutBoundary = [s for s in self.scenarios if s.boundaryEnabled] #: :type s: Scenario    
          
        self.javaCmd = javaCmd  
    
    def finalizeOldestGenerator(self):
        if len(generators) == 0:
            return
        g = generators[0]
        g.join()
        generators.pop(0)
        
    def generate(self):
        generated = {}
        print 'Generating configurations...'
        for s in self.scenarios:
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
                
                if len(generators) >= self.cpus:
                    self.finalizeOldestGenerator()
                    
                    
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
                                                [[it.nodeCnt-1,it.nodeCnt-1,0],[it.nodeCnt-1,0,it.nodeCnt-1]], #distribution of members 
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
            self.finalizeOldestGenerator()
        
        print 'Generating done'
    
    
    
    

      
    
    

    def finalizeOldestSimulation(self):
        iteration = simulated[0]
        iteration.simulation.wait() 
        simulated.pop(0)
        os.remove(iteration.omnetppPath() + '.ini')
        os.remove(iteration.loggingPropertiesPath())
        iteration.stdOut.flush()
        iteration.stdOut.close()
        iteration.stdOut = None
        iteration.simulation = None
        
    
    
    def simulateScenario(self, iteration):        
        classpath = self.classpathRoot + '\\..\\dist\\*;.'  
        
        copyfile(self.root + '\\analysis\\logging.properties', iteration.loggingPropertiesPath())
        with open(iteration.loggingPropertiesPath() , 'a') as f:
            print>>f, '\n\njava.util.logging.FileHandler.pattern=' + iteration.logTemplatePath().replace('\\', '/')
       
        cmd = [self.javaCmd, '-cp', classpath,
               '-Ddeeco.receive.cache.deadline=500',
               '-Ddeeco.publish.individual=true',
               '-Ddeeco.boundary.disable=%s' % ('false' if iteration.boundaryEnabled else 'true'),
               '-Ddeeco.publish.packetsize=3000',
               '-Ddeeco.publish.period=1000',
               '-Ddeeco.rebroadcast.delay=1000',
               '-Djava.util.logging.config.file=%s' % (iteration.loggingPropertiesPath().replace('\\', '/')),
               'cz.cuni.mff.d3s.jdeeco.simulation.demo.Main',
               iteration.componentCfgPath(), iteration.siteCfgPath(), iteration.omnetppPath() ]
        
        if len(simulated) >= self.cpus:
            self.finalizeOldestSimulation()
        
        print 'Evaluating', iteration.name() 
        print 'Executing: ', ' '.join(cmd)
        
        iteration.stdOut = open(iteration.stdOutPath(), 'w')
        iteration.simulation = Popen(cmd, stderr=STDOUT, stdout=iteration.stdOut)
         
        simulated.append(iteration)
    
    
    def simulate(self):
        print 'Simulating...'
        
        for s in self.scenarios:       
            for it in s.iterations:    
                self.simulateScenario(it)
        
        # finalize the rest    
        while len(simulated) > 0:
            self.finalizeOldestSimulation()
        print 'Simulation done'
    
    @staticmethod
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
    
    def callParallelAnalyze(self, iteration):
        qin = Queue()
        qout = Queue()
        p = Process(target=parallelAnalyze, args=(qin, qout,))
        analyses.append(Analysis(p, qin, qout, iteration))
        p.start()
        qin.put(iteration)
    
    def finalizeOldestParallelAnalyze(self):
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
    
    
    def analyze(self):
        print 'Analyzing...'
        
        for s in self.scenarios:    
            for it in s.iterations:   
                print 'Analyzing', it.name()         
                #analyzeScenario(it)
                if len(analyses) > self.cpus:
                    self.finalizeOldestParallelAnalyze()
                self.callParallelAnalyze(it)
            while len(analyses) > 0:
                self.finalizeOldestParallelAnalyze()
                    
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
    
    @staticmethod
    def colorBoxplot(bp, isSecond):
        mycolor = '#E24A33'
        if isSecond:
            mycolor = '#348ABD'
        pylab.setp(bp['boxes'], color=mycolor)
        pylab.setp(bp['whiskers'], color=mycolor)
        pylab.setp(bp['fliers'], marker='None')
        
    
    
    def plotMessageCounts(self, fig):
        import pandas as pd
    
        dataWithoutBoundary = [
            ['F', s.messageStats[1], s.messageStats[0] - s.messageStats[1]] for s in self.scenariosWithoutBoundary]
        dataWithBoundary = [
            ['T', s.messageStats[1], s.messageStats[0] - s.messageStats[1]] for s in self.scenariosWithBoundary]
        df = pd.DataFrame(dataWithoutBoundary + dataWithBoundary, columns=['boundary', 'delivered', 'lost'])    
    
    
    
        fig = pylab.figure(fig, facecolor='white')
        ax = fig.add_subplot(111)
        axes = [fig.add_subplot(121), fig.add_subplot(122)]
        
    
        yticks = range(0, 350000, 100000)
    
        xticksLabels = [s.tickLabel() for s in self.scenariosWithoutBoundary]
        
        ax.set_yticks(yticks)
        ax.set_yticklabels(map(lambda x: x/1000, yticks))
        ax.set_frame_on(False)
        ax.set_ylabel('replicas disseminated [in hundreds of thousands]')
        ax.set_xlabel('total number of nodes per area [firefighters/others]')
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
    
    
    @staticmethod
    def setBoxColors(pylab, bp, color):
        pylab.setp(bp['boxes'], color=color)
        pylab.setp(bp['caps'], color=color)
        pylab.setp(bp['whiskers'], color=color)
        pylab.setp(bp['fliers'], marker='None')
        pylab.setp(bp['medians'], color=color)
        
        pylab.setp(bp['boxes'], linewidth=1)
        pylab.setp(bp['caps'], linewidth=1)
        pylab.setp(bp['whiskers'], linewidth=1)
        pylab.setp(bp['fliers'], linewidth=1)
        pylab.setp(bp['medians'], linewidth=1)    
        
    @staticmethod
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
                color = color1 #'#348ABD'
            else: 
                color = color2 #'#E24A33'
                xLabels[uniqueList.index(s.nodeCnt)] = s.tickLabel() #s.4*str(s.nodeCnt) + '/' + 2*str(s.othersCnt)
            Evaluation.setBoxColors(pylab, bp, color)
            
        xTicks.append(xTicks[1] + xTicks[len(xTicks) - 1])
        xLabels = [''] + xLabels
        
        pylab.axes().set_xticks(xTicks)
        pylab.axes().set_xticklabels(xLabels)
        pylab.axes().yaxis.grid(True, linestyle=':', which='major', color='black',alpha=0.5)
        if split:
            hB, = pylab.plot([1,1],color1) #'#348ABD')
            hR, = pylab.plot([1,1],color2) #'#E24A33')
            pylab.legend((hB, hR),('Boundary Condition enabled', 'Boundary Condition disabled'), loc='upper left')
        
    @staticmethod
    def plotResponseTimes(fig, scenarios, splitBoundary):
        pylab.figure(fig).set_facecolor('white')    
        Evaluation.plotBoundaryBoxplot(scenarios, 'node2nodeResponseTimes', splitBoundary)     
        pylab.axes().set_ylabel("time [s]");
        pylab.axes().set_xlabel("total number of nodes [firefighters/others]");
        pylab.axes().set_yticks(range(0, 60000, 5000))
        pylab.axes().set_yticklabels(range(0, 60, 5))   
    
    @staticmethod    
    def plotNeighborCounts(fig, scenarios, splitBoundary):
        pylab.figure(fig).set_facecolor('white')    
        Evaluation.plotBoundaryBoxplot(scenarios, 'neighbors', splitBoundary)    
        pylab.axes().set_ylabel("number of neighbors");
        pylab.axes().set_xlabel("total number of nodes [firefighters/others]");       
    
    @staticmethod
    def plotDiscoveryRate(fig, scenarios, splitBoundary):
        pylab.figure(fig).set_facecolor('white')    
        Evaluation.plotBoundaryBoxplot(scenarios, 'discoveryRatio', splitBoundary)    
        pylab.axes().set_ylabel("discovery ratio");
        pylab.axes().set_xlabel("total number of nodes [firefighters/others]");       
    
    @staticmethod    
    def plotBoundaryHits(fig, scenarios, splitBoundary):    
        pylab.figure(fig).set_facecolor('white')    
        Evaluation.plotBoundaryBoxplot(scenarios, 'boundaryHits', splitBoundary)    
        pylab.axes().set_ylabel("boundary hits");
        pylab.axes().set_xlabel("total number of nodes [firefighters/others]");       
        
    def plot(self):    
        print 'Plotting...'
            
        pylab.hold(True)
    
        for s in self.scenarios:        
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
        
        if self.generator == 'simple':
            scenairosToPlot = self.scenariosWithoutBoundary
            split = False
        else:        
            scenairosToPlot = self.scenarios
            split = True
        
        self.plotResponseTimes(0, scenairosToPlot, split)
        self.plotMessageCounts(1)
        self.plotNeighborCounts(2, scenairosToPlot, split)
        self.plotDiscoveryRate(3, scenairosToPlot, split)
        self.plotBoundaryHits(4, scenairosToPlot, split)
        
        pylab.show()
    
        print 'Plotting done'
         
    
        
        
    def duplicateScenariosForBoundary(self):
        oldScenarios = self.scenarios[:]
        self.scenariosWithBoundary = []
        self.scenariosWithoutBoundary = []
        for s in oldScenarios:
            s2 = Scenario(s.nodeCnt, s.othersCnt, s.iterationCnt, not s.boundaryEnabled, s.generator, s.start, self)
            self.scenarios.append(s2)
            if s.boundaryEnabled:
                self.scenariosWithBoundary.append(s)
                self.scenariosWithoutBoundary.append(s2)
            else:
                self.scenariosWithBoundary.append(s2)
                self.scenariosWithoutBoundary.append(s)
        self.scenarios.sort(key=lambda x: x.nodeCnt)
        self.scenariosWithBoundary.sort(key=lambda x: x.nodeCnt)
        self.scenariosWithoutBoundary.sort(key=lambda x: x.nodeCnt)
    
    
              
