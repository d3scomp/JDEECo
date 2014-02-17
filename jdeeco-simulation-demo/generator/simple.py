from base import *

from optparse import OptionParser
from matplotlib.pyplot import close




def generateConfig(numLeaders, numMembers, numOthers, prefix):
    aHQ = RectanguralArea('HQ',100,100,100,100,range(0,1))
    aHQExtended = RectanguralArea('HQExt',90,90,120,120,[])
    
    areas = [aHQ, aHQExtended]
    
    SCALE_FACTOR = 10
    
    fig = figure()
    area = fig.add_subplot(111, aspect='equal')
    xSize = 300 * SCALE_FACTOR
    ySize = 300 * SCALE_FACTOR
    area.set_xlim(0, xSize)
    area.set_ylim(0, ySize)
    
    for a in areas:
        a.scale(SCALE_FACTOR)
    
    area.add_artist(aHQ.getPlotObject(color='k'))
    area.add_artist(aHQExtended.getPlotObject(color='k', linestyle='dashed'))
    
    
    leaders = []
    leaders.extend(aHQ.generateLeaders(numLeaders)) 
    
    
    
    members = []
    members.extend(aHQ.generateMembers(numMembers))
    
    
    
    others = []
    others.extend(aHQExtended.generateOthersEdgy(numOthers,10*SCALE_FACTOR))
    
    
    
    for cmp in leaders:
        cmp.plot(area, 'L'+str(leaders.index(cmp)))
        
    for cmp in members:
        cmp.plot(area, 'M'+str(members.index(cmp)))
        
    for cmp in others:
        cmp.plot(area, 'O'+str(others.index(cmp)))
       
    f = open(prefix + 'component.cfg', 'w') 
    
    for idx in range(size(leaders)):
        print>>f, leaders[idx].toString(idx)
    for idx in range(size(members)):
        print>>f, members[idx].toString(idx)
    for idx in range(size(others)):
        print>>f, others[idx].toString(idx)
    f.close()
    
    f = open(prefix + 'site.cfg', 'w') 
    print>>f, xSize, ySize
    for area in [aHQ]:
        print>>f, area.toString();
    f.close()
    
    savefig(prefix + "cfg.png")
    close()
    if __name__ == '__main__':
        show()
    
    
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
    (options, args) = parser.parse_args()
    
    
    prefix = ''
    numLeaders = 1
    numMembers = 9
    numOthers = 0
    
    if options.numLeaders is not None:
        numLeaders = options.numLeaders
    if options.numMembers is not None:
        numMembers = options.numMembers
    if options.numOthers is not None:
        numOthers = options.numOthers
    if options.prefix is not None:
        prefix = options.prefix
    generateConfig(numLeaders, numMembers, numOthers, prefix)