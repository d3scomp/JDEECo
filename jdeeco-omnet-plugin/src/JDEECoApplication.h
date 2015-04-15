//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU Lesser General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
// 
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public License
// along with this program.  If not, see http://www.gnu.org/licenses/.
// 

#ifndef __MANET_JDEECOAPPLICATION_H_
#define __MANET_JDEECOAPPLICATION_H_

#include <omnetpp.h>
#include "BaseModule.h"
#include "JDEECoModule.h"
#include "IMobility.h"

#include "UDPSocket.h"

/**
 * TODO - Generated class
 */
class JDEECoApplication: public cSimpleModule, public JDEECoModule {
    NodeId id;
    std::string color;
    std::string modelURL;
    double modelScale;

    int lowerLayerIn;
    int lowerLayerOut;

    int lower802154LayerIn;
    int lower802154LayerOut;

    IMobility *getMobilityModule();
    double y2lat(double y);
    double x2lon(double x);

    UDPSocket socket;
    int port;

    int packet80211ByteLength;
    int packet802154ByteLength;

protected:
    virtual void initialize(int stage);
    virtual int numInitStages() const { return 2; }
    virtual void handleMessage(cMessage *msg);
public:
    JDEECoApplication() {}
    virtual const NodeId getModuleId();
    virtual void sendPacket(JDEECoPacket *packet, const char *recipient);
    virtual void registerCallbackAt(double absoluteTime, cMessage *msg);

    //Position
    virtual bool isPositionInfoAvailable();
    virtual double getPositionX();
    virtual double getPositionY();
    virtual double getPositionZ();

    virtual void setPosition(double valX, double valY, double valZ);
};

#endif
