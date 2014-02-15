import os, sys
from generator.simple import generateConfig
from exceptions import OSError
from subprocess import *
import atexit
import sys
import time
from shutil import copyfile

processes
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
    print 'cleaned up!'


atexit.register(cleanup)

root = os.path.dirname(os.path.realpath(__file__))
evaluations = {10: 10}

print 'Generating configurations...'
for nodeCnt in evaluations.keys():
    iterations = evaluations[nodeCnt]
    try:
        os.makedirs('simulation-results/%d' % (nodeCnt) )
    except OSError as e:
        pass
    
    for iteration in range(iterations):
        prefix = 'simulation-results/%d/%d-%d-' % (nodeCnt, nodeCnt, iteration)
        #generateConfig(1, nodeCnt-1, 0, prefix)

print 'Done'


print 'Simulating...'
processes = []
processInit = {}
logProps = {}
for nodeCnt in evaluations.keys():
    for iteration in range(evaluations[nodeCnt]):
        folder = root + '\simulation-results\%d\\' % (nodeCnt)        
        prefix = '%d-%d-' % (nodeCnt, iteration)
        classpath = root + '\..\dist\*;.'
        
        logPropsFile = prefix + 'logging.properties'
        logFile = folder + prefix + 'jdeeco.log'
        logFile = logFile.replace('\\', '/')
        
        copyfile(root + '\simulation-results\logging.properties', logPropsFile)
        with open(logPropsFile , 'a') as f:
            print>>f, '\n\njava.util.logging.FileHandler.pattern=' + logFile
    
        #cmd = 'cd %s; java -cp %s cz.cuni.mff.d3s.jdeeco.simulation.demo.Main %s %s' % \
        #    (root + folder, classpath, prefix + 'component.cfg', prefix + 'site.cfg')
        cmd = ['java', '-cp', classpath,
               '-Djava.util.logging.config.file=%s' % (logPropsFile.replace('\\', '/')),
               'cz.cuni.mff.d3s.jdeeco.simulation.demo.Main',
               folder + prefix + 'component.cfg', folder + prefix + 'site.cfg', prefix + 'omnetpp.ini' ]
        print 'Executing: ', ' '.join(cmd)
        p = Popen(cmd)
        
        processes.append(p)
        processInit[p] = prefix + 'omnetpp.ini'
        logProps[p] = logPropsFile
        
    
for p in processes:
    p.wait() 
    os.remove(processInit[p])
    os.remove(logProps[p])
    
    

