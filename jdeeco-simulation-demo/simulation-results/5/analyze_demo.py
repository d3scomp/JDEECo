import re
from numpy import average, median, inf, sqrt
from collections import namedtuple
from __builtin__ import range

class InDanger:
    def __init__(self, dangertime, publishtime, version):
        self.dangertime = dangertime
        self.publishtime = publishtime
        self.version = version
    def __str__(self):
        return 'dangertime: ' + str(self.dangertime) + ', publishtime: ' + str(self.publishtime) + ', version: ' + str(self.version)
class Discovery:
    def __init__(self, discoverytime, receivetime, hops, version):
        self.discoverytime = discoverytime
        self.receivetime = receivetime
        self.hops = hops
        self.version = version
    def __str__(self):
        return 'discoverytime: ' + str(self.discoverytime) + ', receivetime: ' + str(self.receivetime) + ', version: ' +  str(self.version) + ', hops: ' + str(self.hops)  

lines = []
with open('jdeeco.log.0', 'r') as f:
    lines = f.readlines()
    
pattern = re.compile('Simulation parameters: (.+)\n')
config = ""
for line in lines:
    if 'Simulation parameters' in line:
        m = pattern.search(line)
        config = m.group(1)               
        break
    
print 'Config:', config                
print '--------------------------------------------'

if 'boundary disabled' in config:
    rTimesName = 'result-times-no-bc.txt'
else:
    rTimesName = 'result-times-bc.txt'
r = open(rTimesName, 'w')
r.write(config + "\n")

teamLeaders = {}
teamMembers = {}
memberTeam = {}
leaders = set()
members = set()
posx = {}
posy = {}

components = []
with open('../../configurations/5/component5.cfg', 'r') as f:
    components = f.readlines()

memberInDanger = {}
pattern = re.compile('^(.) (.\d+) (.\d+)')
pattern2 = re.compile('^(.) (.\d+) (.\d+) (\d+) (\d+)')
for line in components:
    m = pattern.search(line)
    type = m.group(1)
    if type == 'O':
        continue
    m = pattern2.search(line)
    id = m.group(2)
    team = m.group(3) 
    x = m.group(4)
    y = m.group(5)
    posx[id] = float(x)
    posy[id] = float(y)
    if not team in teamLeaders:
        teamLeaders[team] = set()
    if not team in teamMembers:
        teamMembers[team] = set()
    if type == 'L':
        teamLeaders[team].add(id)
        leaders.add(id)
    if type == 'M':
        teamMembers[team].add(id)
        memberTeam[id] = team
        memberInDanger[id] = InDanger(dangertime=inf, publishtime=inf, version=-1)
        members.add(id)

dangerDiscovered = {}
for leader in leaders:
    dangerDiscovered[leader] = {}
    for member in members:
        dangerDiscovered[leader][member] = Discovery(discoverytime=inf, receivetime=inf, hops=-1, version=-1)

pInDanger = re.compile('Member (.+) got in danger at (\d+)')
pSendVersionTempl = '%sv(\d+),'
pPublish = re.compile('Publish \((\d+)\) at (M\d+), sending \[(.+)\]')
pReceive = re.compile('Receive \((\d+)\) at (L\d+) got (M\d+)v(\d+) after (\d+)ms and (\d+)')
pDiscover = re.compile('Leader (.\d+) discovered at (\d+) that (.\d+) got in danger')

for line in lines:
    inDanger = pInDanger.search(line)
    if inDanger is not None:
        mid = inDanger.group(1)
        dangertime = inDanger.group(2)
        memberInDanger[mid].dangertime = int(dangertime)
        continue
    publish = pPublish.search(line)
    if publish is not None:
        mid = publish.group(2)      
        # get the version of the sender data
        version = re.search(pSendVersionTempl % mid, publish.group(3))
        # is this publishing of local data?
        if version is None:
            continue
          
        # is this publish following the danger situation?
        if  memberInDanger[mid].dangertime == inf:
            continue;       
        # is this the first publish following the danger situation?
        if  memberInDanger[mid].publishtime != inf:
            continue;     
         
        memberInDanger[mid].publishtime = int(publish.group(1))  
        memberInDanger[mid].version = int(version.group(1));
        continue
    discovery = pDiscover.search(line)
    if discovery is not None:
        lid = discovery.group(1)
        time = int(discovery.group(2))
        mid = discovery.group(3)
        dangerDiscovered[lid][mid].discoverytime = time
        continue
    receive = pReceive.search(line)
    if receive is not None:
        lid = receive.group(2)
        mid = receive.group(3)
        #is this the receive immediately preceding the discovery of this member's danger?
        if dangerDiscovered[lid][mid].discoverytime != inf:
            continue #already discovered (i.e., this receive follows the discovery)
        time = int(receive.group(1))
        version = int(receive.group(4))
        duration = int(receive.group(5))
        hops = int(receive.group(6))
        dangerDiscovered[lid][mid].version = version
        dangerDiscovered[lid][mid].hops = hops
        dangerDiscovered[lid][mid].receivetime = time
        continue
   
    
#gotInDangerLines = filter(lambda x: 'Member' in x and 'got in danger at' in x, lines)

#for line in gotInDangerLines:
#    m = pInDanger.search(line)
#    memberInDanger[m.group(1)].time = int(m.group(2))
    
    
#discoveredDangerLines = filter(lambda x: 'Leader' in x and 'discovered at' in x, lines)
#pattern = re.compile('Leader (.+) discovered at (\d+) that (.+) got in danger')
#for line in discoveredDangerLines:
#    m = pattern.search(line)
#    leader = m.group(1)
#    time = m.group(2)
#    member = m.group(3)   
#    dangerDiscovered[leader][member].time = int(time)

def in_range(id1, id2):
    return sqrt((posx[id1] - posx[id2])**2 - (posy[id1] - posy[id2])**2) <= 250

resTimes = []
resTimesNetwork = []
hops = []
versionDifs = []
shouldDiscover = 0;
reallyDiscovered = 0
membersInDanger = set()
membersDiscovered = set()
for team in teamLeaders.keys():
    for leader in teamLeaders[team]:
        for member in teamMembers[team]:
            inDanger = memberInDanger[member]
            if inDanger.dangertime is not inf:
                shouldDiscover+=1
                membersInDanger.add(member)
                discovery = dangerDiscovered[leader][member]
                if discovery.discoverytime is not inf:
                    resTimes.append(discovery.discoverytime - inDanger.dangertime);
                    resTimesNetwork.append(discovery.receivetime - inDanger.publishtime)
                    versionDifs.append(discovery.version - inDanger.version)
                    hops.append(discovery.hops) 
                    reallyDiscovered += 1
                    membersDiscovered.add(member)
                    print 'leader %s of team %s discovered at %f that %s is in danger (after %dms proces-to-process, %dms node-to-node, %dhops, %dversions (%d first, %d discovered)' % \
                        (leader, team, discovery.discoverytime, member, resTimes[-1], resTimesNetwork[-1], hops[-1], versionDifs[-1],  inDanger.version, discovery.version)
                    r.write(str(resTimesNetwork[-1]) + "\n")
                else:
                    print 'leader %s of team %s never discovered that %s is in danger from %d.' % (leader, team, member, inDanger.dangertime)
                    
def printStats(description, values):
    print description, 'avg=%f, min=%d, max=%d, median=%d' %(average(values), min(values), max(values), median(values))

print '\nResults: \n-----------------------------'
printStats('Process-2-process response time:', resTimes)
printStats('Node-2-node response time:', resTimesNetwork)
printStats('Hops:', hops)
printStats('Process-2-process response time / 1 hop:', map(lambda time, hop: time/hop, resTimes, hops))
printStats('Node-2-node response time / 1 hop:', map(lambda time, hop: time/hop, resTimesNetwork, hops))
printStats('Version difference:', versionDifs)
 
print 'Ratio of leader discoveries: %d of %d (%f%%)' %(reallyDiscovered, shouldDiscover, reallyDiscovered*100.0/shouldDiscover)
print 'Ratio of discoveries regardless the number of leaders: %d of %d (%f%%)' %(len(membersDiscovered), len(memberInDanger), len(membersDiscovered)*100.0/len(memberInDanger))
            
r.close()            
