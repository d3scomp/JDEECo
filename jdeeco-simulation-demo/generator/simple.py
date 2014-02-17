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

def generateConfig(numLeaders, numMembers, numOthers, prefix, ipCount=0):
    f = open(prefix + 'site.cfg', 'w') 
    print>>f, 400, 400
    f.close()
    f = open(prefix + 'component.cfg', 'w') 
    f.close()
    fig = figure()
    area = fig.add_subplot(111, aspect='equal')
    area.set_xlim(0, 4000)
    area.set_ylim(0, 4000)
    generateSimpleConfig("a", "HQ", 100, 100, 100, 120, 10, range(0,1), numLeaders, numMembers, numOthers, prefix, ipCount, 0, area)
    savefig(prefix + "cfg.png")
    close()
    if __name__ == '__main__':
        show()

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
    leaders.extend(HQ.generateLeaders(numLeaders)) 
    
    members = []
    members.extend(HQ.generateMembers(numMembers))
    
    others = []
    others.extend(HQExtended.generateOthersEdgy(numOthers,exDiff*scale))
    
    for cmp in random.sample(leaders + members + others, ipCount):
        cmp.ip = True
    
    for cmp in leaders:
        cmp.plot(area, 'L'+str(leaders.index(cmp)))
        
    for cmp in members:
        cmp.plot(area, 'M'+str(members.index(cmp)))
        
    for cmp in others:
        cmp.plot(area, 'O'+str(others.index(cmp)))
       
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