import collections
import ast
from simple import *


def flatten(l):
    for el in l:
        if isinstance(el, collections.Iterable) and not isinstance(el, basestring):
            for sub in flatten(el):
                yield sub
        else:
            yield el

def generateComplexRandomConfig(areaSize, extSize, scale, teamDistribution, leadersDistribution, membersDistribution, othersDistribution, prefix, ipCount):
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
    offset = 0;
    for i in range(0, len(teamDistribution)):
        generateSimpleConfig("a", str(i), diff+i*areaSize*2, diff+i*areaSize*2, areaSize, extSize, scale, teamDistribution[i], leadersDistribution[i], membersDistribution[i], othersDistribution[i], prefix, ipCount[i], offset, area)
        offset = offset + sum(flatten([leadersDistribution[i], membersDistribution[i], othersDistribution[i]]))
    savefig(prefix + "cfg.png")
    if __name__ == '__main__':
        show()
    close()
# call this as python gen_for_gossip_eval.py -l <num leaders> -m <num members> -o <num others>
if __name__ == '__main__':
    parser = OptionParser()
    parser.add_option("-l", "--leaders", dest="numLeaders",
                      help="leaders distribution")
    parser.add_option("-m", "--members", dest="numMembers",
                      help="members distribution")
    parser.add_option("-o", "--others", dest="numOthers",
                      help="others distribution")
    parser.add_option("-p", "--path", dest="prefix",
                      help="output file prefix (inxluding path)")
    parser.add_option("-i", "--ip", dest="ip",
                      help="ip enabled nodes distribution")
    (options, args) = parser.parse_args()
    
    
    prefix = ''
    numLeaders = [[1,1], [0,1]]
    numMembers = [[5,1], [1,5]]
    numOthers = [5, 5]
    ip = [5,5]
    
    if options.numLeaders is not None:
        numLeaders = list(ast.literal_eval(options.numLeaders))
    if options.numMembers is not None:
        numMembers = list(ast.literal_eval(options.numMembers))
    if options.numOthers is not None:
        numOthers = list(ast.literal_eval(options.numOthers))
    if options.ip is not None:
        ip = list(ast.literal_eval(options.ip))
    if options.prefix is not None:
        prefix = options.prefix
    generateComplexRandomConfig(100, 120, 10, [range(0, 2), range(0, 1)], numLeaders, numMembers, numOthers, prefix, ip)