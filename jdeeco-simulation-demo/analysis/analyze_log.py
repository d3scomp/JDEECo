import re
import sys
from numpy import average, median


lines = []
logname = '../logs/jdeeco.log.0'
if len(sys.argv) > 1:
    logname = sys.argv[1]
    
with open(logname, 'r') as f:
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

avgTimePerHop = average(timesPerHop)
maxNumOfHops = max(hopCounts)
avgNumOfHops = average(hopCounts)
print "Average time per hop: ", avgTimePerHop, 'ms'
print "Max number of hops: ", maxNumOfHops
print "Average number of hops: ", avgNumOfHops


boundaryFailedLines = filter(lambda x: 'Boundary failed' in x, lines)
boundaryFailedCnt = len(boundaryFailedLines)
print 'Boundary prevented sending of:', boundaryFailedCnt

def extract_published_signatures(line):
    p = re.compile('\[([^\]]*)\]')
    m = p.search(line)   
    ids = m.group(1)
    if ids is not None:
        return ids.split(', ')[:-1]
    else:
        return []

def flatten(list):
    return [item for sublist in list for item in sublist]

publishLines = filter(lambda x: 'Publish' in x and ', sending [' in x, lines)
# on each publish line, each sent KD is followed by a ',' + there is one more in the message
#knowledgeDataPublished = sum(map(lambda l: l.count(',') - 1, publishLines))
publishedSignatures = flatten(map(extract_published_signatures, publishLines))
knowledgeDataPublished = len(publishedSignatures)
print 'KnowledgeData published:', knowledgeDataPublished, 'unique:', len(set(publishedSignatures))


def extract_received_signatures(line):
    p = re.compile('\[([^\]]*)\]')
    m = p.search(line)  
    sigs = m.group(1)
    if sigs is not None:        
        return [re.search('(.+)<-.+', sig).group(1) for sig in sigs.split(', ')[:-1]]            
    else:
        return []
receiveLines = filter(lambda x: 'Receive' in x and ', received [' in x, lines)
# on each receive line, each received KD is followed by a ',' + there is one more in the message
receivedSignatures = flatten(map(extract_received_signatures, receiveLines))
knowledgeDataReceived = len(receivedSignatures) #sum(map(lambda l: l.count(',') - 1, receiveLines)) 
print 'KnowledgeData received:', knowledgeDataReceived, 'unique:', len(set(receivedSignatures))


def extract_id(line):
    p = re.compile('with messageid (-?\d+)')
    m = p.search(line)
    return m.group(1)

sentMsgLines = filter(lambda x: 'PacketSender: Sending MSG' in x, lines)
sentIds = set(map(extract_id, sentMsgLines))
sentIdsCnt = len(sentIds)
print 'Sent messages: ', sentIdsCnt


receivedMsgLines = filter(lambda x: 'PacketReceiver: Message completed' in x, lines)
receivedIds = set(map(extract_id, receivedMsgLines))
receivedIdsCnt = len(receivedIds)
print 'Received messages: ', receivedIdsCnt

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
droppedIds = droppedIds - receivedIds
droppedIdsCnt = len(droppedIds)
print 'Dropped messages: ', droppedIdsCnt

recSendRatio = len(receivedIds) * 1.0 /len(sentIds)
print 'Received/Sent ratio:', recSendRatio

def printStats(description, values):
    print description, 'avg=%f, min=%f, max=%f, median=%f' %(average(values), min(values), max(values), median(values))

def extract_sent_length(line):
    p = re.compile('and size (.\d+)')
    m = p.search(line)
    return int(m.group(1))
sentSizes = map(extract_sent_length, sentMsgLines);
printStats('Message size:', sentSizes)

rebroadcastsPlanned = len(filter(lambda x: 'Gossip rebroadcast' in x, lines))
print 'Rebroadcasts planned: ', rebroadcastsPlanned  
rebroadcastsAborted = len(filter(lambda x: 'Rebroadcast aborted' in x, lines));
print 'Rebroadcasts aborted: ', rebroadcastsAborted
print 'Rebroadcasts finished: ', len(filter(lambda x: 'Rebroadcast finished' in x, lines))
print 'Average abort ratio: ', rebroadcastsAborted*1.0/rebroadcastsPlanned

#rebroadcastLines = filter(lambda x: 'Rebroadcasting' in x, lines)
#def extract_rebroadcast_cnt(line):
    #p = re.compile('Rebroadcasting (\d+)')
    #m = p.search(line)
    #return int(m.group(1))
#def extract_rebroadcast_ratio(line):
    #p = re.compile('Rebroadcasting (\d+) out of (\d+)')
    #m = p.search(line)
    #sent = float(m.group(1))
    #total = float(m.group(2))
    #if total == 0:
        #return -1
    #else:
        #return  sent*1.0/total 
#rebroadcastCounts = map(extract_rebroadcast_cnt, rebroadcastLines);
#printStats('Rebroadcast count:', rebroadcastCounts)
#rebroadcastRatios = filter(lambda x: x >= 0, map(extract_rebroadcast_ratio, rebroadcastLines));
#printStats('Rebroadcast ratio:', rebroadcastRatios)


csvLine = [config, 
           avgTimePerHop, 
           maxNumOfHops, 
           avgNumOfHops, 
           boundaryFailedCnt, 
           knowledgeDataPublished, 
           knowledgeDataReceived,
           sentIdsCnt,
           receivedIdsCnt,
           droppedIdsCnt,
           recSendRatio ]
#print '\nCSV:'
#print ';'.join(map(str, csvLine))