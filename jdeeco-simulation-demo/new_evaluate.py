from analysis.evaluation import *
root = os.path.dirname(os.path.realpath(__file__))
if __name__ == '__main__':  
    cpus = 3
    evaluations = {}    
    for i in range(2,4,2): #20
        evaluations[i] = 1*cpus
    scenarios = []
    # init with only scenarios with disabled boundary (they enbaled counterparts will be created automatically after the generation step)
    for nodeCnt in evaluations.keys():    
        scenarios.append(Scenario(nodeCnt, nodeCnt, evaluations[nodeCnt], False, 'complex'))
    
    e = Evaluation(scenarios, 'complex') #: :type e: Evaluation
    e.duplicateScenariosForBoundary()   
    
    
    #generate()
    #simulate()    
    #analyze()
    #e.generate()
    #e.simulate()
    #e.analyze()
    e.plot()