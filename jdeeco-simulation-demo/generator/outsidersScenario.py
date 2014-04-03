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

def generate2AreasPlayground(leadersDistribution, density, cellSize,  areaSizeX, areaSizeY, margin, scale, prefix, ipCount):
    totalCellCountX = 3*margin + 2*areaSizeX  
    totalCellCountY = 2*margin + areaSizeY
    sizeX = cellSize*totalCellCountX
    sizeY = cellSize*totalCellCountY
    fig = figure()
    plot = fig.add_subplot(111, aspect='equal')
    plot.set_xlim(0, sizeX*scale)
    plot.set_ylim(0, sizeY*scale)
    
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
        l[i] = random.sample(m[i], leadersDistribution[0])
        for lii in range(len(l[i])):
            l[i].append(memberToLeader(l[i][lii]))    
            l[i].remove(l[i][lii])
    
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
    
    print cCount
    
    return cCount
    
if __name__ == '__main__':
    generate2AreasPlayground([2,2,0], 1.5, 20, 4, 4, 2, 10, '', [0.2, 0.2, 0.2])