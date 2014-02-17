import os, sys
from generator.simple import generateConfig
from analysis.analyze_demo import *
from exceptions import OSError
from subprocess import *
import atexit
import sys
import time
from shutil import copyfile
import numpy as np
import pylab

processes = []
processInit = {}
logProps = {}
stdoutFiles = {}

cpus = 3

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
evaluations = {8:10, 12: 10, 16:10, 20:10, 30:10, 40:10, 50:10, 60:10}

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
            generateConfig(1, nodeCnt-1, 0, prefix)
    
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
    cmd = ['java', '-cp', classpath,
           '-Ddeeco.receive.cache.deadline="500"',
           '-Ddeeco.publish.individual="true"',
           '-Ddeeco.boundary.disable="true"',
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
def analyzeScenario(nodeCnt, iteration):
    folder = root + '\simulation-results\%d\\' % (nodeCnt)        
    prefix = '%d-%d-' % (nodeCnt, iteration)
    stdoutNameGeneric = folder + prefix + 'analysis-generic.txt'
    stdoutNameDemo = folder + prefix + 'analysis-demo.txt'
    logName =  folder + prefix + 'jdeeco.log.0'
    componentConfigName = folder + prefix + 'component.cfg'
   
    print 'Analyzing', prefix + "scenario"
  
    genericStdout = open(stdoutNameGeneric, 'w')
    pGeneric = Popen(['python', root + '\\analysis\\analyze_log.py', logName], stderr=STDOUT, stdout=genericStdout)
   
    demoStdout = open(stdoutNameDemo, 'w')
    oldStdOut = sys.stdout
    sys.stdout = demoStdout
    #pDemo = Popen(['python', root + '\\analysis\\analyze_demo.py', logName, componentConfigName], stderr=STDOUT, stdout=demoStdout)
    a = DemoAnalysis()
    a.analyze(logName, componentConfigName)
    if not nodeCnt in demoAnalyses:
        demoAnalyses[nodeCnt] = []
    demoAnalyses[nodeCnt].append(a)
    sys.stdout = oldStdOut
    
    pGeneric.wait()
    genericStdout.close()
    demoStdout.close()
    demoAnalyses.clear()


def analyze():
    print 'Analyzing...'
    for nodeCnt in evaluations.keys():
        for iteration in range(evaluations[nodeCnt]):
            analyzeScenario(nodeCnt, iteration)
        resultsFile = root + '\\simulation-results\\results-%d.csv' % nodeCnt
        with open(resultsFile, 'w') as results: 
            for a in demoAnalyses[nodeCnt]:
                np.savetxt(results, zip(a.resTimes, a.resTimesNetwork, a.hops, a.versionDifs), fmt='%d')
    print 'Analysis done'

def plot():    
    print 'Plotting...'
    results = {}
    
    pylab.hold(True)
    ax = pylab.axes()
    
    for nodeCnt in evaluations.keys():     
        resultsFileName = root + '\\simulation-results\\results-%d.csv' % nodeCnt        
        results[nodeCnt] = []
        
        with open(resultsFileName, 'r') as resultsFile: 
            contents = np.loadtxt(resultsFile)
            results[nodeCnt] = map(int, contents[:, 1])
    
        bp = pylab.boxplot(results[nodeCnt], positions = [nodeCnt], widths = 2)
    ax.set_xticks(evaluations.keys())
    pylab.xlim(min(evaluations.keys())-3,max(evaluations.keys()) +3)
    pylab.savefig("results.png")
    pylab.show()
    print 'Plotting done'
 #   pylab.setp(bp['boxes'], color='black')
  #  pylab.setp(bp['whiskers'], color='black')
   # pylab.setp(bp['fliers'], marker='None')    
    #pylab.show()
    
    
    
if __name__ == '__main__':
    generate()
    simulate()
    analyze()
    plot()
