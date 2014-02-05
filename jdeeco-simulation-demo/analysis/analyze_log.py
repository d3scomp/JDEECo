import re
from numpy import average


lines = []
with open('../logs/jdeeco.log.0', 'r') as f:
    lines = f.readlines()
    
hoplines = filter(lambda x: x.endswith('hops\n'), lines)
hopData = []
pattern = re.compile('after (\d+)ms and (\d+) hops')
for line in hoplines:
    m = pattern.search(line)
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


def extract_sent_id(line):
    p = re.compile('with messageid (.\d+)')
    m = p.search(line)
    return m.group(1)
sentMsgLines = filter(lambda x: 'PacketSender: Sending MSG' in x, lines)
sentIds = set(map(extract_sent_id, sentMsgLines))
print 'Sent messages: ', len(sentIds)

def extract_dropped_ids(line):
    p = re.compile('dropped messageids \[(.*)\]')
    m = p.search(line)
    ids = m.group(1)
    if ids is not None:
        return set(ids.split(', '))
    else:
        return set() 
dropMsgLines = filter(lambda x: 'dropped messageids' in x, lines)
droppedIds = set.union(*map(extract_dropped_ids, dropMsgLines))
print 'Dropped messages: ', len(droppedIds)

 
def extract_received_id(line):
    p = re.compile('with messageid (.\d+)')
    m = p.search(line)
    return m.group(1)
receivedMsgLines = filter(lambda x: 'PacketReceiver: Message completed' in x, lines)
receivedIds = set(map(extract_received_id, receivedMsgLines))
print 'Received messages: ', len(receivedIds)

print 'Received/Sent ratio:', len(receivedIds) * 1.0 /len(sentIds)

