from ffConfiguration import *

aHQ = RectanguralArea('HQ',100,100,300,200,range(0,4))
aSite = CircularArea('Site1',1200,1000,150,range(4,7))
aSite2 = CircularArea('Site2',300,1000,150,range(7,10))
aSite3 = CircularArea('Site3',1200,450,150,range(10,13))
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




