import re
import sys
from numpy import average, median, inf, sqrt, abs
from collections import namedtuple
from __builtin__ import range

def printStats(description, values):
    if len(values) > 0:
        print description, 'avg=%f, min=%d, max=%d, median=%d' %(average(values), min(values), max(values), median(values))
    else:
        print description, 'N/A'
                
class Component:
    def __init__(self, x, y):
        self.x = x
        self.y = y
        

class NeighborAnalysis:
    def __init__(self):
        self.neighborCnts = []  
        self.range = 250 
    
    def inRange(self, src, tar):        
        return sqrt(abs((src.x - tar.x)**2 - (src.y - tar.y)**2)) <= self.range
        
    def analyze(self, componentConfig):
        
        
        componentLines = []
        with open(componentConfig, 'r') as f:
            componentLines = f.readlines()
            
        
        components = []
        pattern = re.compile('^(.) (.\d+) (.?\d+) (\d+)')
        pattern2 = re.compile('^(.) (.\d+) (.\d+) (\d+) (\d+)')
        for line in componentLines:
            m = pattern.search(line)
            type = m.group(1)
            if type == 'O':
                c = Component(int(m.group(3)), int(m.group(4)))                
            else:
                m = pattern2.search(line)
                c = Component(int(m.group(4)), int(m.group(5)))
            components.append(c)                
        
        
        for src in components:
            cnt = 0
            for tar in components:
                if src == tar:
                    continue;
                if self.inRange(src, tar):
                    cnt+=1
            self.neighborCnts.append(cnt)
        
        #printStats('Average neighbor count:', self.neighborCnts)
                
                
        
if __name__ == '__main__':      
    configname = '../component.cfg'

    if len(sys.argv) >= 2:        
        configname = sys.argv[1]
    a = NeighborAnalysis()
    a.analyze(configname)