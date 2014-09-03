from base import *
from optparse import OptionParser
from matplotlib.pyplot import close

import random

class AreaConfiguration:
    def __init__(posX, posY, xSize, ySize, extXSize, extYSize, scale, teams, numLeaders=0, numMembers=0, numOthers=0, prefix="", ipCount=0):
        self.posX = posX
        self.posY = posY
        self.xSize = xSize
        self.ySize = ySize
        self.extXSize = extXSize
        self.extYSize = extYSize
        self.scale = scale
        self.teams = teams
        self.numLeaders = numLeaders
        self.numMembers = numMembers
        self.numOthers = numOthers
        self.prefix = prefix
        self.ipCount = ipCount        

def generateNonArealTeams(teams, sizeX, sizeY, scale, excludeRectangularAreas, numLeaders, numMember, numOthers, prefix, ipCount, idCounter, area): 
    leaders = []
    members = []
    others = []
    for t in teams:
        for i in range(numLeaders[t]):
            point = generatePositionBetweenSpots(sizeX, sizeY, excludeRectangularAreas)
            leaders.append(Leader(t, point[0]*scale, point[1]*scale))
        for i in range(numMember[t]):
            point = generatePositionBetweenSpots(sizeX, sizeY, excludeRectangularAreas)
            members.append(Member(t, point[0]*scale, point[1]*scale))
    for i in range(numOthers):
        point = generatePositionBetweenSpots(sizeX, sizeY, excludeRectangularAreas)
        others.append(Other(point[0]*scale, point[1]*scale))
            
    if ipCount < len(leaders + members):
        for c in random.sample(leaders + members, ipCount):
            c.ip = True
        
    for c in leaders:
        c.plot(area, 'L'+str(leaders.index(c) + idCounter))
        
    for c in members:
        c.plot(area, 'M'+str(members.index(c) + idCounter))
        
    for c in others:
        c.plot(area, 'O'+str(others.index(c) + idCounter))
       
    f = open(prefix + 'component.cfg', 'a') 
    
    for idx in range(size(leaders)):
        print>>f, leaders[idx].toString(idx + idCounter)
    for idx in range(size(members)):
        print>>f, members[idx].toString(idx + idCounter)
    for idx in range(size(others)):
        print>>f, others[idx].toString(idx + idCounter)
    f.close()
        
def generatePositionBetweenSpots(sizeX, sizeY, spots):
    x = random.random()*sizeX
    y = random.random()*sizeY
    if random.random() < 0.5:
        gaps = generateHorizontalSpots(spots)
        for gap in gaps:
            if x >= gap[0] and x <= gap[1]:
                x = getRandomCoordBetweenSpots(gaps, sizeX)
                break
    else:
        gaps = generateVerticalSpots(spots)
        for gap in gaps:
            if y >= gap[0] and y <= gap[1]:
                y = getRandomCoordBetweenSpots(gaps, sizeY)
                break
    return [x, y]
    
    
def getRandomCoordBetweenSpots(spots, size):
    index = randint(0, len(spots))
    flat = [y for x in spots for y in x]
    flat.sort()
    if index == 0:
        coord = randint(0, flat[0])
    elif index == len(spots):
        coord = randint(flat[index*2], size)
    else:
        coord = randint(flat[index*2-1], flat[index*2])
    return coord
        
def generateHorizontalSpots(excludeRectangularAreas):    
    horizontalSpots = []
    for rect in excludeRectangularAreas:
        spot = [rect[0], rect[0] + rect[2]]
        for hSpot in horizontalSpots:
            if spot[0] >= hSpot[0] and spot[1] <= hSpot[1]:
                horizontalSpots.remove(hSpot)
                spot = hSpot
            elif spot[0] <= hSpot[0] and spot[1] >= hSpot[1]:
                horizontalSpots.remove(hSpot)
            elif spot[1] >= hSpot[0] and spot[1] <= hSpot[1]:
                spot = [spot[0], hSpot[1]]
                horizontalSpots.remove(hSpot)
            elif spot[0] >= hSpot[0] and spot[0] <= hSpot[1]:
                spot = [hSpot[0], spot[1]]
                horizontalSpots.remove(hSpot)
        horizontalSpots.append(spot)
    return horizontalSpots

def generateVerticalSpots(excludeRectangularAreas):    
    verticalSpots = []
    for rect in excludeRectangularAreas:
        spot = [rect[1], rect[1] + rect[3]]
        for vSpot in verticalSpots:
            if spot[0] >= vSpot[0] and spot[1] <= vSpot[1]:
                verticalSpots.remove(vSpot)
                spot = vSpot
            elif spot[0] <= vSpot[0] and spot[1] >= vSpot[1]:
                verticalSpots.remove(vSpot)
            elif spot[1] >= vSpot[0] and spot[1] <= vSpot[1]:
                spot = [spot[0], vSpot[1]]
                verticalSpots.remove(vSpot)
            elif spot[0] >= vSpot[0] and spot[0] <= vSpot[1]:
                spot = [vSpot[0], spot[1]]
                verticalSpots.remove(vSpot)
        verticalSpots.append(spot)
    return verticalSpots
         

def generateConfig(numLeaders, numMembers, numOthers, prefix, ipCount=0):
    f = open(prefix + 'site.cfg', 'w') 
    SCALE_FACTOR = 10
    sizeX = 300*SCALE_FACTOR
    sizeY = 300*SCALE_FACTOR
    print>>f, sizeX, sizeY
    f.close()
    f = open(prefix + 'component.cfg', 'w') 
    f.close()
    fig = figure()
    area = fig.add_subplot(111, aspect='equal')
    area.set_xlim(0, sizeX)
    area.set_ylim(0, sizeY)
    generateSimpleConfig("a", "HQ", 100, 100, 100, 120, SCALE_FACTOR, range(0,1), numLeaders, numMembers, numOthers, prefix, ipCount, 0, area)
    savefig(prefix + "cfg.png")
    if __name__ == '__main__':
        show()
    close()

def generateSimpleConfig(writeMode, areaId, posX, posY, areaSize, extSize, scale, teams, numLeaders, numMembers, numOthers, prefix, ipCount, idCounter, area):
    HQ = RectanguralArea(areaId,posX,posY,areaSize,areaSize,teams)
    exDiff = (extSize - areaSize)/2
    HQExtended = RectanguralArea(areaId+'Ext',posX - exDiff, posY - exDiff,extSize,extSize,[])
    
    areas = [HQ, HQExtended]
    
    scaledSize = areaSize * scale
    
    for a in areas:
        a.scale(scale)
    
    area.add_artist(HQ.getPlotObject(color='k'))
    area.add_artist(HQExtended.getPlotObject(color='k', linestyle='dashed'))
    
    leaders = []
    if isinstance(numLeaders, list):
        for i in range(0, len(numLeaders)):
            leaders.extend(HQ.generateLeadersForTeam(numLeaders[i], i))        
    else:
        leaders.extend(HQ.generateLeaders(numLeaders))
        
    members = []
    if isinstance(numMembers, list):
        for i in range(0, len(numMembers)):
            members.extend(HQ.generateMembersForTeam(numMembers[i], i))        
    else:
        members.extend(HQ.generateMembers(numMembers))
    
    others = []
    others.extend(HQExtended.generateOthersEdgy(numOthers,exDiff*scale))
    
    for cmp in random.sample(leaders + members, ipCount):
        cmp.ip = True
    
    for cmp in leaders:
        cmp.plot(area, 'L'+str(leaders.index(cmp) + idCounter))
        
    for cmp in members:
        cmp.plot(area, 'M'+str(members.index(cmp) + idCounter))
        
    for cmp in others:
        cmp.plot(area, 'O'+str(others.index(cmp) + idCounter))

    f = open(prefix + 'component.cfg', writeMode) 
    
    for idx in range(size(leaders)):
        print>>f, leaders[idx].toString(idx + idCounter)
    for idx in range(size(members)):
        print>>f, members[idx].toString(idx + idCounter)
    for idx in range(size(others)):
        print>>f, others[idx].toString(idx + idCounter)
    f.close()
    
    f = open(prefix + 'site.cfg', writeMode) 
    for a in [HQ]:
        print>>f, a.toString();
    f.close()
    
    
# call this as python gen_for_gossip_eval.py -l <num leaders> -m <num members> -o <num others>
if __name__ == '__main__':
    parser = OptionParser()
    parser.add_option("-l", "--leaders", dest="numLeaders",
                      help="number of leaders")
    parser.add_option("-m", "--members", dest="numMembers",
                      help="number of members")
    parser.add_option("-o", "--others", dest="numOthers",
                      help="number of others")
    parser.add_option("-p", "--path", dest="prefix",
                      help="output file prefix (inxluding path)")
    parser.add_option("-i", "--ip", dest="ip",
                      help="IP enabled nodes count")
    (options, args) = parser.parse_args()
    
    
    prefix = ''
    numLeaders = 1
    numMembers = 9
    numOthers = 0
    ip = 0
    
    if options.numLeaders is not None:
        numLeaders = int(options.numLeaders)
    if options.numMembers is not None:
        numMembers = int(options.numMembers)
    if options.numOthers is not None:
        numOthers = int(options.numOthers)
    if options.ip is not None:
        ip = int(options.ip)
    if options.prefix is not None:
        prefix = options.prefix
    generateConfig(numLeaders, numMembers, numOthers, prefix, ip)
