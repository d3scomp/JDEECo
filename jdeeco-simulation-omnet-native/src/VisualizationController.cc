//
// This file is part of an OMNeT++/OMNEST simulation example.
//
// Copyright (C) 2010 OpenSim Ltd.
//
// This file is distributed WITHOUT ANY WARRANTY. See the file
// `license' for details on this and other legal matters.
//

#include "VisualizationController.h"

Define_Module(VisualizationController);

VisualizationController *VisualizationController::instance = NULL;

VisualizationController::VisualizationController()
{
    if (instance)
        error("There can be only one ChannelController instance in the network");
    instance = this;
}

VisualizationController::~VisualizationController()
{
    instance = NULL;
}

VisualizationController *VisualizationController::getInstance()
{
    if (!instance)
        throw cRuntimeError("ChannelController::getInstance(): there is no ChannelController module in the network");
    return instance;
}

int VisualizationController::findMobileNode(IMobileApplication* p)
{
    for (int i=0; i<(int)nodeList.size(); i++)
        if (nodeList[i] == p)
            return i;
    return -1;
}

void VisualizationController::addMobileNode(IMobileApplication* p)
{
    if (findMobileNode(p) == -1)
        nodeList.push_back(p);
}

void VisualizationController::removeMobileNode(IMobileApplication* p)
{
    int k = findMobileNode(p);
    if (k != -1)
        nodeList.erase(nodeList.begin()+k);
}

void VisualizationController::initialize()
{
    playgroundLat = simulation.getSystemModule()->par("playgroundLatitude");
    playgroundLon = simulation.getSystemModule()->par("playgroundLongitude");
    KmlHttpServer::getInstance()->addKmlFragmentProvider(this);
}

void VisualizationController::handleMessage(cMessage *msg)
{
    error("This module does not process messages");
}

std::string VisualizationController::getKmlFragment()
{
    std::vector<KmlUtil::Pt2D> connections;

    std::string fragment;

    for (int i=0; i<(int)nodeList.size(); ++i)
    {
        for (int j=i+1; j<(int)nodeList.size(); ++j)
        {
            IMobileApplication *pi = nodeList[i];
            IMobileApplication *pj = nodeList[j];
            double ix = pi->getPositionX(), iy = pi->getPositionY(), jx = pj->getPositionX(), jy = pj->getPositionY();
            if (pi->getTxRange()*pi->getTxRange() > (ix-jx)*(ix-jx)+(iy-jy)*(iy-jy)) {
                double ilat = KmlUtil::y2lat(playgroundLat, iy);
                double ilon = KmlUtil::x2lon(ilat, playgroundLon, ix);
                connections.push_back(KmlUtil::Pt2D(ilon, ilat));

                double jlat = KmlUtil::y2lat(playgroundLat, jy);
                double jlon = KmlUtil::x2lon(jlat, playgroundLon, jx);
                connections.push_back(KmlUtil::Pt2D(jlon, jlat));
            }
        }
    }
    fragment += KmlUtil::lines("connectivity_1", connections, "connectivity graph", NULL, "60FFFFFF");

    return fragment;
}


