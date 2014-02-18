from simple import *

def generateComplexRandomConfig(areaSize, extSize, scale, teamDistribution, numLeaders, numMembers, numOthers, prefix, ipCount):
    f = open(prefix + 'site.cfg', 'w') 
    sizeX = areaSize*len(teamDistribution)*2*scale
    sizeY = areaSize*len(teamDistribution)*2*scale
    print>>f, sizeX, sizeY
    f.close()
    f = open(prefix + 'component.cfg', 'w') 
    f.close()
    fig = figure()
    area = fig.add_subplot(111, aspect='equal')
    area.set_xlim(0, sizeX)
    area.set_ylim(0, sizeY)
    diff = extSize - areaSize
    for i in range(0, len(teamDistribution)):
        generateSimpleConfig("a", str(i), diff+i*areaSize*2, diff+i*areaSize*2, areaSize, extSize, scale, teamDistribution[i], numLeaders, numMembers, numOthers, prefix, ipCount, i*max([numLeaders, numMembers, numOthers]), area)
    savefig(prefix + "cfg.png")
    if __name__ == '__main__':
        show()
    close()
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
    ip = 10
    
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
    generateComplexRandomConfig(100, 120, 10, [range(0, 2), range(0, 1)], numLeaders, numMembers, numOthers, prefix, ip)