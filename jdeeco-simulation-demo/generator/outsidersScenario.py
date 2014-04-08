from base import *

import random

def randomPoint(x, y, width, height):
    result = []
    result.append(x + random.random()*width)
    result.append(y + random.random()*height)
    return result

def memberToLeader(member):
    leader = Leader(member.team, member.x, member.y)
    leader.ip = member.ip
    return leader

def overlapingAreas(density, cellSize, areaCount, areaSize, overlap, radioDistance, leaderNumber, ipCountPerTeam, prefix):
    scale = 250 / radioDistance
    
    cellCountX = areaCount*areaSize - overlap*(areaCount-1)
    sizeX = cellCountX*cellSize*scale
    sizeY = scale*cellSize*areaSize
    fig = figure()
    plot = fig.add_subplot(111, aspect='equal')
    plot.set_xlim(0, sizeX)
    plot.set_ylim(0, sizeY)
    
    areas = []
    teams = []
    for i in range(areaCount):
        area = RectanguralArea('A'+str(i), i*(areaSize-overlap)*cellSize, 0, areaSize*cellSize, areaSize*cellSize, [i])
        area.scale(scale)
        plot.add_artist(area.getPlotObject(color='k'))
        areas.append(area)
        teams.append([])
    
    totalDensity = density // 1
    totalDensity = int(totalDensity)
    
    #Generate members
    m = []
    if totalDensity > 0:
        for i in range(cellCountX):
            for j in range(areaSize):
                for d in range(totalDensity):
                    point = randomPoint(cellSize*i, cellSize*j, cellSize, cellSize)
                    component = Member(-1, point[0]*scale, point[1]*scale);
                    m.append(component)
            
    fracDensity = density - totalDensity
    if fracDensity > 0:
        toAdd = random.sample(range(cellCountX*areaSize), int(cellCountX*areaSize*fracDensity))
        for i in range(cellCountX):
            for j in range(areaSize):
                if (j*cellCountX + i) in toAdd:
                    point = randomPoint(cellSize*i, cellSize*j, cellSize, cellSize)
                    component = Member(-1, point[0]*scale, point[1]*scale);
                    m.append(component)
    #Assign teams               
    for ti in range(len(teams)):
        area = areas[ti]
        for member in m:
            if member.x >= area.x and member.x <= area.x + area.width:
                if member.team > -1:
                    probability = random.random()
                    teams[member.team].remove(member)
                else:
                    probability = 1
                if probability > 0.5:
                    member.team = ti
                teams[member.team].append(member)
                
    #Assign leaders
    leaders = []
    for ti in range(len(teams)):
        toLeader = random.sample(teams[ti], leaderNumber)
        for l in toLeader:
            m.remove(l)
            teams[ti].remove(l)
            leader = memberToLeader(l)
            teams[ti].append(leader)
            leaders.append(leader)
        
    #IP support
    for team in teams:
        ips = random.sample(team, int(len(team)*ipCountPerTeam))
        for i in ips:
            i.ip = True 
    
    f = open(prefix + 'component.cfg', 'w') 

    for i in range(len(leaders)):
        leaders[i].plot(plot, 'L'+str(i))
        print>>f, leaders[i].toString(i)
       
    for i in range(len(m)):
        m[i].plot(plot, 'M'+str(i))  
        print>>f, m[i].toString(i)
               
    f.close()
    
    f = open(prefix + 'site.cfg', 'w') 
    print>>f, sizeX, sizeY
    for a in areas:
        print>>f, a.toString();
    f.close()    
    
    
    savefig(prefix + "cfg.png")
    if __name__ == '__main__':
        show()
        
    
    close()

def crossAreas(density, cellSize, thickness, xSize, ySize, radioDistance, leaderNumber, ipCount, prefix):
    scale = 250 / radioDistance
    
    sizeX = scale*cellSize*xSize
    sizeY = scale*cellSize*ySize
    fig = figure()
    plot = fig.add_subplot(111, aspect='equal')
    plot.set_xlim(0, sizeX)
    plot.set_ylim(0, sizeY)
    
    areaH = RectanguralArea('H',0,0,xSize*cellSize,thickness*cellSize,[0])
    areaV = RectanguralArea('V',0,0,thickness*cellSize,ySize*cellSize,[1])
    areaV.scale(scale)
    areaH.scale(scale)
    plot.add_artist(areaH.getPlotObject(color='k'))
    plot.add_artist(areaV.getPlotObject(color='k'))
    
    totalDensity = density // 1
    totalDensity = int(totalDensity)
    v = []
    h = []
    vh = []
    #Generate components
    if totalDensity > 0:
        for i in range(0, xSize):
            for j in range(0, ySize):
                if ((j >= 0) and (j < thickness)) or ((i >= 0) and (i < thickness)):
                    for k in range(0, totalDensity):
                        point = randomPoint(cellSize*i, cellSize*j, cellSize, cellSize)
                        component = Member(0, point[0]*scale, point[1]*scale);
                        if ((j >= 0) and (j < thickness)) and ((i >= 0) and (i < thickness)):
                            component.team = random.randint(0,1)
                            vh.append(component)
                        elif (j >= 0) and (j < thickness):
                            h.append(component)
                        else:
                            component.team = 1
                            v.append(component)
                        
   
    fracDensity = density - totalDensity
    totalInAreaCellCount = (xSize + ySize - thickness)*thickness
    
    if fracDensity > 0:
        currentIndex = -1
        toAdd = random.sample(range(totalInAreaCellCount), int(totalInAreaCellCount*fracDensity))
        for i in range(0, xSize):
            for j in range(0, ySize):
                if ((j >= 0) and (j < thickness)) or ((i >= 0) and (i < thickness)):
                    currentIndex += 1
                    if currentIndex in toAdd:
                        point = randomPoint(cellSize*i, cellSize*j, cellSize, cellSize)
                        component = Member(0, point[0]*scale, point[1]*scale);
                        if ((j >= 0) and (j < thickness)) and ((i >= 0) and (i < thickness)):
                            component.team = random.randint(0,1)
                            vh.append(component)
                        elif (j >= 0) and (j < thickness):
                            h.append(component)
                        else:
                            component.team = 1
                            v.append(component)
                        
    possibleLeaders = v + vh
    l = []
    toLeader = random.sample(possibleLeaders, leaderNumber)
    for i in toLeader:
        if i in v:
            target = v
        else:
            target = vh
        target.remove(i)
        l.append(memberToLeader(i))
                   
    m = vh + v + h           

    #IP support
    all = m + l
    for ip in random.sample(all, int(len(all)*ipCount)):
        ip.ip = True         
    
    f = open(prefix + 'component.cfg', 'w') 

    for i in range(len(l)):
        l[i].plot(plot, 'L'+str(i))
        print>>f, l[i].toString(i)
       
    for i in range(len(m)):
        m[i].plot(plot, 'M'+str(i))  
        print>>f, m[i].toString(i)
               
    f.close()
    
    f = open(prefix + 'site.cfg', 'w') 
    print>>f, sizeX, sizeY
    for a in [areaH, areaV]:
        print>>f, a.toString();
    f.close()    
    
    
    savefig(prefix + "cfg.png")
    if __name__ == '__main__':
        show()
    close()    

def twoAreasPlayground(density, cellSize,  areaSizeX, areaSizeY, margin, radioDistance, leadersDistribution, ipCount, prefix):
    scale = 250 / radioDistance # scale the scenario to match the required radio distance
    
    totalCellCountX = 3*margin + 2*areaSizeX  
    totalCellCountY = 2*margin + areaSizeY
    sizeX = scale*cellSize*totalCellCountX
    sizeY = scale*cellSize*totalCellCountY
    fig = figure()
    plot = fig.add_subplot(111, aspect='equal')
    plot.set_xlim(0, sizeX)
    plot.set_ylim(0, sizeY)
    
    totalDensity = density // 1
    totalDensity = int(totalDensity)
    #Generate areas
    
    areaL = RectanguralArea('L',margin*cellSize,margin*cellSize,areaSizeX*cellSize,areaSizeY*cellSize,[0])
    areaR = RectanguralArea('R',cellSize*(2*margin+areaSizeX),margin*cellSize,areaSizeX*cellSize,areaSizeY*cellSize,[0])
    
    areaL.scale(scale)
    areaR.scale(scale)
    
    plot.add_artist(areaL.getPlotObject(color='k'))
    plot.add_artist(areaR.getPlotObject(color='k'))
    
    m = [[],[],[]]
    l = [[],[],[]]
    
    #Distribute members
    
    areaLXIndices = range(margin, margin + areaSizeX) 
    areaRXIndices = range(2*margin + areaSizeX, 2*margin + 2*areaSizeX)
    areaYIndices = range(margin, margin + areaSizeY) 

    if totalDensity > 0:
        for i in range(0, totalCellCountX):
            for j in range(0, totalCellCountY):
                if (i in areaLXIndices) and (j in areaYIndices):
                    for k in range(0, totalDensity):
                        point = randomPoint(cellSize*i, cellSize*j, cellSize, cellSize)
                        m[0].append(Member(0, point[0]*scale, point[1]*scale))  
                elif (i in areaRXIndices) and (j in areaYIndices):
                    for k in range(0, totalDensity):
                        point = randomPoint(cellSize*i, cellSize*j, cellSize, cellSize)
                        m[1].append(Member(0, point[0]*scale, point[1]*scale))
                else:
                    for k in range(0, totalDensity):
                        point = randomPoint(cellSize*i, cellSize*j, cellSize, cellSize)
                        m[2].append(Member(1, point[0]*scale, point[1]*scale))
                    
    fracDensity = density - totalDensity
     
    if fracDensity > 0:
        totalCellCount = totalCellCountX*totalCellCountY 
        for ci in random.sample(range(totalCellCount), int(totalCellCount*fracDensity)):
            i = ci % totalCellCountX
            j = ci // totalCellCountX
            if (i in areaLXIndices) and (j in areaYIndices):
                point = randomPoint(cellSize*i, cellSize*j, cellSize, cellSize)
                m[0].append(Member(0, point[0]*scale, point[1]*scale))  
            elif (i in areaRXIndices) and (j in areaYIndices):
                point = randomPoint(cellSize*i, cellSize*j, cellSize, cellSize)
                m[1].append(Member(0, point[0]*scale, point[1]*scale))
            else:
                point = randomPoint(cellSize*i, cellSize*j, cellSize, cellSize)
                m[2].append(Member(1, point[0]*scale, point[1]*scale))
                    
    #Distribute leaders
    
    for i in range(3):
        leaders = random.sample(m[i], leadersDistribution[i])
        for c in leaders:
            l[i].append(memberToLeader(c))    
            m[i].remove(c)
    
    cCount = 0
    
    #Add ip support
    
    for i in range(3):
        cs = l[i] + m[i]
        cCount += len(cs)
        for c in random.sample(cs, int(len(cs)*ipCount[i])):
            c.ip = True
            
    

    f = open(prefix + 'component.cfg', 'w') 

    idx = 0
    for i in range(len(l)):
        for c in l[i]:
            c.plot(plot, 'L'+str(idx))
            print>>f, c.toString(idx)
            idx += 1
      
    idx = 0  
    for i in range(len(m)):
        for c in m[i]:
            c.plot(plot, 'M'+str(idx))  
            print>>f, c.toString(idx)
            idx += 1
    
    f.close()
    
    f = open(prefix + 'site.cfg', 'w') 
    print>>f, sizeX, sizeY
    for a in [areaL, areaR]:
        print>>f, a.toString();
    f.close()
    
    savefig(prefix + "cfg.png")
    if __name__ == '__main__':
        show()
    close()
    
    return cCount
    
    (density, cellSize,  areaSizeX, areaSizeY, margin, radioDistance, leadersDistribution, ipCount, prefix)
if __name__ == '__main__':
    #generate2AreasPlayground(1.5, 20, 4, 4, 2, 25, [2,2,0], [0.2, 0.2, 0.2], '')
    #crossAreas(1, 20, 4, 10, 10, 25, 2, 0.25, '')
    overlapingAreas(1, 20, 4, 6, 2, 25, 2, 0.25, '')