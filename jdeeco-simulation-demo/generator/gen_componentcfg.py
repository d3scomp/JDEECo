from math import pi, cos, sin, sqrt
from random import random, choice
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

teamColors = ['b','r','g','y','c','m','k'];
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
        area.plot(self.x, self.y, self.plotType(), zorder=1)
        area.add_artist(plt.Circle((self.x, self.y), 250, fill=False, color='#eeeeee', zorder=0))
        
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
    def __init__(self, id, teams):
        self.teams = teams
        self.id = id
    def generatePositions(self, cnt):
        raise NotImplementedError()
    def getPlotObject(self, **plotargs):
        raise NotImplementedError()
    def generateLeaders(self, cnt):
        ret = []
        lx, ly = self.generatePositions(cnt)
        for (x, y) in zip(lx, ly):
            team = choice(self.teams)
            ret.append(Leader(team, x, y))
        return ret
    def generateMembers(self, cnt):
        ret = []
        lx, ly = self.generatePositions(cnt)
        for (x, y) in zip(lx, ly):
            team = choice(self.teams)
            ret.append(Member(team, x, y))
        return ret
    def generateOthers(self, cnt):
        ret = []
        lx, ly = self.generatePositions(cnt)
        for (x, y) in zip(lx, ly):
            ret.append(Other(x, y))
        return ret
    def toString(self, idx):
        raise NotImplementedError()
    def scale(self, factor):
        raise NotImplementedError()
        
    
class RectanguralArea(Area):
    def __init__(self, id, x, y, width, height, teams):
        Area.__init__(self, id, teams)
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
    
    def toString(self):
        return "R %s %d %d %d %d %s" % (self.id, self.x, self.y, self.width, self.height, ' '.join('T'+str(x) for x in self.teams))
    def scale(self, factor):
        self.x *= factor
        self.y *= factor
        self.width *= factor
        self.height *= factor

class CircularArea(Area):
    def __init__(self, id, x, y, r, teams):
        Area.__init__(self, id, teams)
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
    
    def toString(self):
        return "C %s %d %d %d %s" % (self.id, self.x, self.y, self.r, ' '.join('T'+str(x) for x in self.teams))
    
    def scale(self, factor):
        self.x *= factor
        self.y *= factor
        self.r *= factor


aHQ = RectanguralArea('HQ',100,100,300,200,range(7))
aSite = CircularArea('Site1',1200,1000,150,range(0,4))
aSite2 = CircularArea('Site2',300,1000,150,range(0,4))
aSite3 = CircularArea('Site3',1200,450,150,range(4,7))
aHQExtended = RectanguralArea('HQExt', 0,0,500,400,[])
aSiteExtended = CircularArea('Site1Ext', 1200,1000,300,[])
aSite2Extended = CircularArea('Site2Ext', 300,1000,300,[])
aSite3Extended = CircularArea('Site3Ext', 1200,450,300,[])

areas = [aHQ, aSite, aSite2, aSite3, aHQExtended, aSiteExtended, aSite2Extended, aSite3Extended]

SCALE_FACTOR = 2

fig = figure()
area = fig.add_subplot(111, aspect='equal')
xSize = 1500 * SCALE_FACTOR
ySize = 1500 * SCALE_FACTOR
area.set_xlim(0, xSize)
area.set_ylim(0, ySize)

for a in areas:
    a.scale(SCALE_FACTOR)

area.add_artist(aHQ.getPlotObject(color='k'))
area.add_artist(aSite.getPlotObject(color='r'))
area.add_artist(aSite2.getPlotObject(color='r'))
area.add_artist(aSite3.getPlotObject(color='r'))
area.add_artist(aHQExtended.getPlotObject(color='k', linestyle='dashed'))
area.add_artist(aSiteExtended.getPlotObject(color='r', linestyle='dashed'))
area.add_artist(aSite2Extended.getPlotObject(color='r', linestyle='dashed'))
area.add_artist(aSite3Extended.getPlotObject(color='r', linestyle='dashed'))


leaders = []
leaders.extend(aHQ.generateLeaders(10)) 
leaders.extend(aSite.generateLeaders(5))
leaders.extend(aSite2.generateLeaders(5))
leaders.extend(aSite3.generateLeaders(5))



members = []
members.extend(aHQ.generateMembers(25))
members.extend(aSite.generateMembers(30))
members.extend(aSite2.generateMembers(30))
members.extend(aSite3.generateMembers(30))



others = []
others.extend(aHQExtended.generateOthers(20))
others.extend(aSiteExtended.generateOthers(20))
others.extend(aSite2Extended.generateOthers(20))
others.extend(aSite3Extended.generateOthers(20))



for cmp in leaders + members + others:
    cmp.plot(area)
   
f = open('../configurations/component.cfg', 'w') 

for idx in range(size(leaders)):
    print>>f, leaders[idx].toString(idx)
for idx in range(size(members)):
    print>>f, members[idx].toString(idx)
for idx in range(size(others)):
    print>>f, others[idx].toString(idx)
f.close()

f = open('../configurations/site.cfg', 'w') 
print>>f, xSize, ySize
for area in [aHQ, aSite, aSite2, aSite3]:
    print>>f, area.toString();
f.close()

savefig("../configurations/cfg.png")
show()




