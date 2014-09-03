import re
import sys
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

class DemoAnalysis:
    def __init__(self):
        self.config = ''
        self.memberInDanger = {}
        self.dangerDiscovered = {}
        self.resTimes = []
        self.resTimesNetwork = []
        self.hops = []
        self.versionDifs = []
        
    def analyze(self, log, componentConfig):
        lines = []
        with open(log, 'r') as f:
            lines = f.readlines()
        components = []
        with open(componentConfig, 'r') as f:
            components = f.readlines()
            
        pattern = re.compile('Simulation parameters: (.+)\n')
        self.config = ""
        for line in lines:
            if 'Simulation parameters' in line:
                m = pattern.search(line)
                config = m.group(1)               
                break
            
        print 'Config:', self.config                
        print '--------------------------------------------'
        
        
        teamLeaders = {}
        teamMembers = {}
        memberTeam = {}
        leaders = set()
        members = set()
        posx = {}
        posy = {}
        
        
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
                self.memberInDanger[id] = InDanger(dangertime=inf, publishtime=inf, version=-1)
                members.add(id)
        
        
        for leader in leaders:
            self.dangerDiscovered[leader] = {}
            for member in members:
                self.dangerDiscovered[leader][member] = Discovery(discoverytime=inf, receivetime=inf, hops=-1, version=-1)
        
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
                self.memberInDanger[mid].dangertime = int(dangertime)
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
                if  self.memberInDanger[mid].dangertime == inf:
                    continue;       
                # is this the first publish following the danger situation?
                if  self.memberInDanger[mid].publishtime != inf:
                    continue;     
                 
                self.memberInDanger[mid].publishtime = int(publish.group(1))  
                self.memberInDanger[mid].version = int(version.group(1));
                continue
            discovery = pDiscover.search(line)
            if discovery is not None:
                lid = discovery.group(1)
                time = int(discovery.group(2))
                mid = discovery.group(3)
                self.dangerDiscovered[lid][mid].discoverytime = time
                continue
            receive = pReceive.search(line)
            if receive is not None:
                lid = receive.group(2)
                mid = receive.group(3)
                #is this the receive immediately preceding the discovery of this member's danger?
                if self.dangerDiscovered[lid][mid].discoverytime != inf:
                    continue #already discovered (i.e., this receive follows the discovery)
                time = int(receive.group(1))
                version = int(receive.group(4))
                duration = int(receive.group(5))
                hops = int(receive.group(6))
                self.dangerDiscovered[lid][mid].version = version
                self.dangerDiscovered[lid][mid].hops = hops
                self.dangerDiscovered[lid][mid].receivetime = time
                continue
        
        shouldDiscover = 0;
        reallyDiscovered = 0
        membersInDanger = set()
        membersDiscovered = set()
        for team in teamLeaders.keys():
            for leader in teamLeaders[team]:
                for member in teamMembers[team]:
                    inDanger = self.memberInDanger[member]
                    if inDanger.dangertime is not inf:
                        shouldDiscover+=1
                        membersInDanger.add(member)
                        discovery = self.dangerDiscovered[leader][member]
                        if discovery.discoverytime is not inf:
                            self.resTimes.append(discovery.discoverytime - inDanger.dangertime);
                            self.resTimesNetwork.append(discovery.receivetime - inDanger.publishtime)
                            self.versionDifs.append(discovery.version - inDanger.version)
                            self.hops.append(discovery.hops) 
                            reallyDiscovered += 1
                            membersDiscovered.add(member)
                            print 'leader %s of team %s discovered at %f that %s is in danger (after %dms proces-to-process, %dms node-to-node, %dhops, %dversions (%d first, %d discovered)' % \
                                (leader, team, discovery.discoverytime, member, self.resTimes[-1], \
                                 self.resTimesNetwork[-1], self.hops[-1], self.versionDifs[-1],  \
                                 inDanger.version, discovery.version)
                        else:
                            print 'leader %s of team %s never discovered that %s is in danger from %d.' % (leader, team, member, inDanger.dangertime)
                            
        def printStats(description, values):
            if len(values) > 0:
                print description, 'avg=%f, min=%d, max=%d, median=%d' %(average(values), min(values), max(values), median(values))
            else:
                print description, 'N/A'
        
        self.shouldDiscover = len(membersInDanger)
        self.reallyDiscovered = len(membersDiscovered)
        
        print '\nResults: \n-----------------------------'
        printStats('Process-2-process response time:', self.resTimes)
        printStats('Node-2-node response time:', self.resTimesNetwork)
        printStats('Hops:', self.hops)
        printStats('Process-2-process response time / 1 hop:', map(lambda time, hop: time/hop, self.resTimes, self.hops))
        printStats('Node-2-node response time / 1 hop:', map(lambda time, hop: time/hop, self.resTimesNetwork, self.hops))
        printStats('Version difference:', self.versionDifs)
         
        print 'Ratio of leader discoveries: %d of %d (%f%%)' %(reallyDiscovered, shouldDiscover, reallyDiscovered*100.0/max(1, shouldDiscover))
        print 'Ratio of discoveries regardless the number of leaders: %d of %d (%f%%)' % \
            (len(membersDiscovered), len(membersInDanger), len(membersDiscovered)*100.0/max(1, len(membersInDanger)))
                    
if __name__ == '__main__':      
    logname = '../logs/jdeeco.log.0'
    configname = '../component.cfg'

    if len(sys.argv) >= 3:
        logname = sys.argv[1]
        configname = sys.argv[2]
    a = DemoAnalysis()
    a.analyze(logname, configname)
