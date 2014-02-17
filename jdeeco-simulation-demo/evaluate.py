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

processes = []
processInit = {}
logProps = {}
stdoutFiles = {}

cpus = 3

java_command = 'C:/Program Files (x86)/Java/jdk7/bin/java.exe'

def cleanup():
    timeout_sec = 5
    for p in processes:  # list of your processes
        p_sec = 0
        for second in range(timeout_sec):
            if p.poll() == None:
                time.sleep(1)
                p_sec += 1
        if p_sec >= timeout_sec:
            p.kill()  # supported from python 2.6
  


atexit.register(cleanup)
root = os.path.dirname(os.path.realpath(__file__))
#evaluations = {4:10, 8:10, 12: 10, 16:10, 20:10}
evaluations = {8:10, 12: 10, 16:10, 20:10, 24:10, 28:10}

def generate():
    print 'Generating configurations...'
    for nodeCnt in evaluations.keys():
        iterations = evaluations[nodeCnt]
        try:
            os.makedirs('simulation-results/%d' % (nodeCnt) )
        except OSError as e:
            pass
        
        for iteration in range(iterations):
            prefix = 'simulation-results/%d/%d-%d-' % (nodeCnt, nodeCnt, iteration)
            print 'Generating %d-%d-scenario' % (nodeCnt, iteration)
            generateConfig(1, nodeCnt-1, 0, prefix, 0)
    
    print 'Generating done'


def finalizeProcess(p):
    p.wait() 
    os.remove(processInit[p])
    os.remove(logProps[p])
    stdoutFiles[p].flush()
    stdoutFiles[p].close()
    processes.remove(p)


def simulateScenario(nodeCnt, iteration):
    folder = root + '\simulation-results\%d\\' % (nodeCnt)        
    prefix = '%d-%d-' % (nodeCnt, iteration)
    classpath = root + '\..\dist\*;.'
    
    logPropsFile = prefix + 'logging.properties'
    logFile = folder + prefix + 'jdeeco.log'
    logFile = logFile.replace('\\', '/')
    
    stdoutName = folder + prefix + 'stdout.log'
    stdoutFile = open(stdoutName, 'w')
    
    copyfile(root + '\simulation-results\logging.properties', logPropsFile)
    with open(logPropsFile , 'a') as f:
        print>>f, '\n\njava.util.logging.FileHandler.pattern=' + logFile
    
    #cmd = 'cd %s; java -cp %s cz.cuni.mff.d3s.jdeeco.simulation.demo.Main %s %s' % \
    #    (root + folder, classpath, prefix + 'component.cfg', prefix + 'site.cfg')
    cmd = [java_command, '-cp', classpath,
           '-Ddeeco.receive.cache.deadline="500"',
           '-Ddeeco.publish.individual="true"',
           '-Ddeeco.boundary.disable="false"',
           '-Ddeeco.publish.packetsize="3000"',
           '-Ddeeco.publish.period="1000"',
           '-Ddeeco.rebroadcast.delay="1000"',
           '-Djava.util.logging.config.file=%s' % (logPropsFile.replace('\\', '/')),
           'cz.cuni.mff.d3s.jdeeco.simulation.demo.Main',
           folder + prefix + 'component.cfg', folder + prefix + 'site.cfg', prefix + 'omnetpp.ini' ]
    
    if len(processes) >= cpus:
        p = processes[0]
        finalizeProcess(p)
    
    print 'Evaluating', prefix + "scenario" 
    print 'Executing: ', ' '.join(cmd)
    p = Popen(cmd, stderr=STDOUT, stdout=stdoutFile)
    
    processes.append(p)
    processInit[p] = prefix + 'omnetpp.ini'
    logProps[p] = logPropsFile
    stdoutFiles[p] = stdoutFile


def simulate():
    print 'Simulating...'
    
    for nodeCnt in evaluations.keys():
        for iteration in range(evaluations[nodeCnt]):
            simulateScenario(nodeCnt, iteration)
    
    # finalize the rest    
    while len(processes) > 0:
        finalizeProcess(processes[0])
    print 'Simulation done'

demoAnalyses = {}
genericAnalyses = {}
neighborAnalyses = {}
def analyzeScenario(nodeCnt, iteration):
    folder = root + '\simulation-results\%d\\' % (nodeCnt)        
    prefix = '%d-%d-' % (nodeCnt, iteration)
    stdoutNameGeneric = folder + prefix + 'analysis-generic.txt'
    stdoutNameDemo = folder + prefix + 'analysis-demo.txt'
    logName =  folder + prefix + 'jdeeco.log.0'
    componentConfigName = folder + prefix + 'component.cfg'
   
    print 'Analyzing', prefix + "scenario"
  
    with open(stdoutNameGeneric, 'w') as genericStdout:
        oldStdOut = sys.stdout
        sys.stdout = genericStdout
        a = GenericAnalysis()
        a.analyze(logName)        
        sys.stdout = oldStdOut
        genericAnalyses[nodeCnt].append(a)       
         
        #pGeneric = Popen(['python', root + '\\analysis\\analyze_log.py', logName], stderr=STDOUT, stdout=genericStdout)
        #pGeneric.wait()
    
    with open(stdoutNameDemo, 'w') as demoStdout:
        oldStdOut = sys.stdout
        sys.stdout = demoStdout
        
        a = DemoAnalysis()
        a.analyze(logName, componentConfigName)                   
        sys.stdout = oldStdOut
        demoAnalyses[nodeCnt].append(a)
        #pDemo = Popen(['python', root + '\\analysis\\analyze_demo.py', logName, componentConfigName], stderr=STDOUT, stdout=demoStdout)
        #pDemo.wait()


    a = NeighborAnalysis()
    a.analyze(componentConfigName)
    neighborAnalyses[nodeCnt].append(a)

def analyze():
    print 'Analyzing...'
    
    runningAnalyses = []
    
    for nodeCnt in evaluations.keys():
        demoAnalyses[nodeCnt] = []
        genericAnalyses[nodeCnt] = []
        neighborAnalyses[nodeCnt] = []
        for iteration in range(evaluations[nodeCnt]):
            analyzeScenario(nodeCnt, iteration)
            
        demoResultsFile = root + '\\simulation-results\\results-demo-%d.csv' % nodeCnt
        with open(demoResultsFile, 'w') as results: 
            for a in demoAnalyses[nodeCnt]:
                np.savetxt(results, zip(a.resTimes, a.resTimesNetwork, a.hops, a.versionDifs), fmt='%d')
            demoAnalyses[nodeCnt] = []
        genericResultsFile = root + '\\simulation-results\\results-generic-%d.csv' % nodeCnt
        with open(genericResultsFile, 'w') as results: 
            messageStats = [[a.sentMessagesCnt, a.receivedMessagesCnt] for a in genericAnalyses[nodeCnt]]           
            np.savetxt(results, messageStats, fmt='%d')
            genericAnalyses[nodeCnt] = []
            
        neighborsResultsFile = root + '\\simulation-results\\results-neighbors-%d.csv' % nodeCnt
        with open(neighborsResultsFile, 'w') as results: 
            neighbors = [cnt for a in neighborAnalyses[nodeCnt] for cnt in a.neighborCnts]            
            np.savetxt(results, neighbors, fmt='%d')
            neighborAnalyses[nodeCnt] = []
    print 'Analysis done'

def colorBoxplot(bp):
    pylab.setp(bp['boxes'], color='black')
    pylab.setp(bp['whiskers'], color='black')
    pylab.setp(bp['fliers'], marker='None')
def plot():    
    print 'Plotting...'
    results = {}
    neighbors = {}
    messages = {}
    
    pylab.hold(True)
   
    
    for nodeCnt in evaluations.keys():       
        results[nodeCnt] = []
        neighbors[nodeCnt] = []
        messages[nodeCnt] = []
        
        with open(root + '\\simulation-results\\results-demo-%d.csv' % nodeCnt , 'r') as resultsFile: 
            contents = np.loadtxt(resultsFile)
            results[nodeCnt] = map(int, contents[:, 1])
        with open(root + '\\simulation-results\\results-generic-%d.csv' % nodeCnt , 'r') as resultsFile: 
            contents = np.loadtxt(resultsFile)
            sent = map(int, contents[:, 0])            
            received = map(int, contents[:, 1])
            messages[nodeCnt] = [average(sent), average(received), average(received)*1.0/average(sent)]
        with open(root + '\\simulation-results\\results-neighbors-%d.csv' % nodeCnt , 'r') as resultsFile: 
            contents = np.loadtxt(resultsFile)
            neighbors[nodeCnt] = map(int, contents)
    
        pylab.figure(0)
        bp = pylab.boxplot(results[nodeCnt], positions = [nodeCnt], widths = 2)
        colorBoxplot(bp)
        pylab.figure(1)
        bp = pylab.boxplot(neighbors[nodeCnt], positions = [nodeCnt], widths = 2)
        colorBoxplot(bp)
    
    pylab.figure(0)
    pylab.axes().set_yticks(range(0, 60000, 5000))
    pylab.axes().set_yticklabels(range(0, 60, 5))        
    
    
    for fig in range(2):
        pylab.figure(fig)     
        ax = pylab.axes()
        ax.set_xticks(evaluations.keys())       
        pylab.xlim(min(evaluations.keys())-3,max(evaluations.keys()) +3)
        
        
    pylab.show()
    
    print 'Plotting done'
     
    
    
    
    
if __name__ == '__main__':
    #generate()
    simulate()
    analyze()
    plot()
