import re
from numpy import average


lines = []
with open('../logs/no-boundary.log', 'r') as f:
    lines = f.readlines()
    
hoplines = filter(lambda x: x.endswith('hops\n'), lines)
hopData = []
p = re.compile('after (\d+)ms and (\d+) hops')
for line in hoplines:
    m = p.search(line)
    time = m.group(1)
    hops = m.group(2)
    hopData.append({'time': int(time), 'hops': int(hops)})


    
timesPerHop = map(lambda x: x['time'] / x['hops'], hopData)
hopCounts = map(lambda x: x['hops'], hopData)
print "Average time per hop: ", average(timesPerHop), 'ms'
print "Max number of hops: ", max(hopCounts)
print "Average number of hops: ", average(hopCounts)


boundaryFailedLines = filter(lambda x: 'Boundary failed' in x, lines)
print 'Boundary failure occurred:', len(boundaryFailedLines)

publishLines = filter(lambda x: 'Publish' in x, lines)
# on each publish line, each sent KD is followed by a ',' + there is one more in the message
dataPublished = sum(map(lambda l: l.count(',') - 1, publishLines)) 
print 'KnowledgeData published:', dataPublished


receiveLines = filter(lambda x: ', received [' in x, lines)
# on each receive line, each received KD is followed by a ',' + there is one more in the message
dataReceived = sum(map(lambda l: l.count(',') - 1, receiveLines)) 
print 'KnowledgeData received:', dataReceived