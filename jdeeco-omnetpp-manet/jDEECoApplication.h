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
#include "jDEECoModule.h"
#include "IMobility.h"

#include "KmlHttpServer.h"
#include "KmlUtil.h"
#include "VisualizationController.h"
#include "UDPSocket.h"

/**
 * TODO - Generated class
 */
class JDEECoApplication: public cSimpleModule,
        public jDEECoModule,
        public IMobileApplication,
        public IKmlFragmentProvider {
    const char * id;
    double playgroundLat, playgroundLon;  // NW corner of playground, in degrees
    double playgroundHeight, playgroundWidth;
    std::string color;
    std::string modelURL;
    double modelScale;
    bool showTxRange;
    double txRange;

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
    virtual void jDEECoScheduleAt(double absoluteTime, cMessage *msg);
public:
    JDEECoApplication() {}
    virtual const char *jDEECoGetModuleId();
    virtual void jDEECoSendPacket(JDEECoPacket *packet, const char *recipient);

    //Position
    virtual bool jDEECoIsPositionInfoAvailable();
    virtual double jDEECoGetPositionX();
    virtual double jDEECoGetPositionY();
    virtual double jDEECoGetPositionZ();

    virtual void jDEECoSetPositionX(double value);
    virtual void jDEECoSetPositionY(double value);
    virtual void jDEECoSetPositionZ(double value);

    virtual double getPositionX() {
        return jDEECoGetPositionX();
    }
    ;
    virtual double getPositionY() {
        return jDEECoGetPositionY();
    }
    ;
    virtual double getTxRange();

    virtual std::string getKmlFragment();
};

#endif
