from ffConfiguration import *

aHQ = RectanguralArea('HQ',200,200,300,200,range(0,1))
aHQExtended = RectanguralArea('HQExt',200,200,300,200,[])

areas = [aHQ, aHQExtended]

SCALE_FACTOR = 2

fig = figure()
area = fig.add_subplot(111, aspect='equal')
xSize = 700 * SCALE_FACTOR
ySize = 700 * SCALE_FACTOR
area.set_xlim(0, xSize)
area.set_ylim(0, ySize)

for a in areas:
    a.scale(SCALE_FACTOR)

area.add_artist(aHQ.getPlotObject(color='k'))
area.add_artist(aHQExtended.getPlotObject(color='k', linestyle='dashed'))


leaders = []
leaders.extend(aHQ.generateLeaders(1)) 



members = []
members.extend(aHQ.generateMembers(4))



others = []
#others.extend(aHQExtended.generateOthers(1))



for cmp in leaders:
    cmp.plot(area, 'L'+str(leaders.index(cmp)))
    
for cmp in members:
    cmp.plot(area, 'M'+str(members.index(cmp)))
    
for cmp in others:
    cmp.plot(area, 'O'+str(others.index(cmp)))
   
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
for area in [aHQ]:
    print>>f, area.toString();
f.close()

savefig("../configurations/cfg.png")
show()