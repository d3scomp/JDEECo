from math import pi, cos, sin, sqrt
from random import random
from pylab import *
import matplotlib.pyplot as plt

def point_in_circle(x, y, r):
    a = 2 * pi * random()
    r1 = sqrt(random())
    x1 = (r1 * r) * cos(a) + x
    y1 = (r1 * r) * sin(a) + y
    return x1, y1
    
def points_in_circle(x, y, r, cnt):
    rx = []
    ry = []
    for i in range(cnt):
        x1, y1 = point_in_circle(x, y, r)
        rx.append(x1)
        ry.append(y1)
    return rx, ry

teamColors = ['b','r','g','y','c'];
teamcnt = 4

class Component:
    def __init__(self, x, y):
        self.x = x
        self.y = y
    def toString(self, idx):
        raise NotImplementedError()
    def plotType(self):
        raise NotImplementedError()
    def plot(self, area):
        area.plot(self.x, self.y, self.plotType())
        
class Leader(Component):
    def __init__(self, team, x, y):
        Component.__init__(self, x, y)
        self.team = team
    def toString(self, idx):
        return "L L%d T%d %d %d" % (idx, self.team, self.x, self.y) 
    def plotType(self):
        return 'o' + teamColors[self.team]
    
class Member(Component):
    def __init__(self, team, x, y):
        Component.__init__(self, x, y)
        self.team = team
    def toString(self, idx):
        return "M M%d T%d %d %d" % (idx, self.team, self.x, self.y)
    def plotType(self):
        return '*' + teamColors[self.team]
    
class Other(Component):
    def __init__(self, x, y):
        Component.__init__(self, x, y)
    def toString(self, idx):
        return "O O%d %d %d" % (idx, self.x, self.y) 
    def plotType(self):
        return 'xm'
                

class Area:
    def generatePositions(self, cnt):
        raise NotImplementedError()
    def getPlotObject(self, **plotargs):
        raise NotImplementedError()
    def generateLeaders(self, cnt):
        ret = []
        lx, ly = self.generatePositions(cnt)
        for (x, y) in zip(lx, ly):
            team = randint(1, teamcnt)
            ret.append(Leader(team, x, y))
        return ret
    def generateMembers(self, cnt):
        ret = []
        lx, ly = self.generatePositions(cnt)
        for (x, y) in zip(lx, ly):
            team = randint(1, teamcnt)
            ret.append(Member(team, x, y))
        return ret
    def generateOthers(self, cnt):
        ret = []
        lx, ly = self.generatePositions(cnt)
        for (x, y) in zip(lx, ly):
            ret.append(Other(x, y))
        return ret
        
    
class RectanguralArea(Area):
    def __init__(self, x, y, width, height):
        self.x = x
        self.y = y
        self.width = width
        self.height = height
        
    def generatePositions(self, cnt):
        rx = []
        ry = []
        for i in range(cnt):
            rx.append(self.x + random()*self.width)
            ry.append(self.y + random()*self.height)
        return rx, ry
    
    def getPlotObject(self, **kwargs):
        return plt.Rectangle((self.x, self.y), self.width, self.height, fill=False, **kwargs)

class CircularArea(Area):
    def __init__(self, x, y, r):
        self.x = x
        self.y = y
        self.r = r        
        
    def generatePositions(self, cnt):
        rx = []
        ry = []
        for i in range(cnt):
            a = 2 * pi * random()
            r1 = sqrt(random())
            rx.append((r1 * self.r) * cos(a) + self.x)
            ry.append((r1 * self.r) * sin(a) + self.y)
        return rx, ry
    
    def getPlotObject(self, **kwargs):
        return plt.Circle((self.x, self.y), self.r, fill=False, **kwargs)

aHQ = RectanguralArea(50,50,300,200)
aSite = CircularArea(700,700,200)
aHQExtended = RectanguralArea(0,0,400,300)
aSiteExtended = CircularArea(700,700,300)


fig = figure()
area = fig.add_subplot(111, aspect='equal')
area.set_xlim(0, 1000)
area.set_ylim(0, 1000)


area.add_artist(aHQ.getPlotObject(color='k'))
area.add_artist(aSite.getPlotObject(color='r'))
area.add_artist(aHQExtended.getPlotObject(color='k', linestyle='dashed'))
area.add_artist(aSiteExtended.getPlotObject(color='r', linestyle='dashed'))


leaders = []
leaders.extend(aHQ.generateLeaders(10))
leaders.extend(aSite.generateLeaders(20))


members = []
members.extend(aHQ.generateMembers(50))
members.extend(aSite.generateMembers(100))

others = []
others.extend(aHQExtended.generateOthers(20))
others.extend(aSiteExtended.generateOthers(50))


for cmp in leaders + members + others:
    cmp.plot(area)
   
f = open('../component.cfg', 'w') 

for idx in range(size(leaders)):
    print>>f, leaders[idx].toString(idx)
for idx in range(size(members)):
    print>>f, members[idx].toString(idx)
for idx in range(size(others)):
    print>>f, others[idx].toString(idx)
f.close()
show()



