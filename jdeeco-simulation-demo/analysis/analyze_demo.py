import re
from numpy import average, median, inf, sqrt


lines = []
with open('../logs/jdeeco.log.0', 'r') as f:
    lines = f.readlines()
    
pattern = re.compile('Simulation parameters: (.+)\n')
config = ""
for line in lines:
    if 'Simulation parameters' in line:
        m = pattern.search(line)
        config = m.group(1)               
        break
    
print config                
print '--------------------------------------------'


teamLeaders = {}
teamMembers = {}
memberTeam = {}
leaders = set()
members = set()
posx = {}
posy = {}

components = []
with open('../configurations/component.cfg', 'r') as f:
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
        memberInDanger[id] = inf
        members.add(id)

dangerDiscovered = {}
for leader in leaders:
    dangerDiscovered[leader] = {}
    for member in members:
        dangerDiscovered[leader][member] = inf

gotInDangerLines = filter(lambda x: 'Member' in x and 'got in danger at' in x, lines)
pattern = re.compile('Member (.+) got in danger at (\d+)')

for line in gotInDangerLines:
    m = pattern.search(line)
    memberInDanger[m.group(1)] = int(m.group(2))
    
discoveredDangerLines = filter(lambda x: 'Leader' in x and 'discovered at' in x, lines)
pattern = re.compile('Leader (.+) discovered at (\d+) that (.+) got in danger')
for line in discoveredDangerLines:
    m = pattern.search(line)
    leader = m.group(1)
    time = m.group(2)
    member = m.group(3)   
    dangerDiscovered[leader][member] = int(time)

def in_range(id1, id2):
    return sqrt((posx[id1] - posx[id2])**2 - (posy[id1] - posy[id2])**2) <= 250

resTimes = []
shouldDiscover = 0;
reallyDiscovered = 0
for team in teamLeaders.keys():
    for leader in teamLeaders[team]:
        for member in teamMembers[team]:
            inDanger = memberInDanger[member]
            if inDanger is not inf:
                shouldDiscover+=1
                discovered = dangerDiscovered[leader][member]
                if discovered is not inf:
                    resTimes.append(discovered - inDanger);
                    reallyDiscovered += 1
                print 'leader %s of team %s discovered at %f that %s is in danger (after %f)' % (leader, team, discovered, member, discovered - inDanger)
            
print 'Avg res time:', average(resTimes)  
print 'Median res time:', median(resTimes)  
print 'Ratio of discovered: %d of %d (%f%%)' %(reallyDiscovered, shouldDiscover, reallyDiscovered*100.0/shouldDiscover)
            
            
