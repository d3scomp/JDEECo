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

#include "jDEECoApplication.h"
#include "MacToNetwControlInfo.h"
#include "NetwToMacControlInfo.h"
#include "IPv4ControlInfo.h"
#include "IPvXAddressResolver.h"
#include "UDPControlInfo.h"
#include "CustomMobility.h"

Define_Module(JDEECoApplication);

void JDEECoApplication::initialize(int stage) {
    if (stage == 1) {
        id = par("id").stringValue();

        if (simulation.getSystemModule()->par("visualize").boolValue()) {
            color = par("color").stringValue();
            modelURL = par("modelURL").str();
            modelScale = par("modelScale").doubleValue();
            showTxRange = par("showTxRange").boolValue();
            txRange = par("txRange").doubleValue();

            playgroundLat = simulation.getSystemModule()->par("playgroundLatitude");
            playgroundLon = simulation.getSystemModule()->par("playgroundLongitude");
            playgroundHeight = simulation.getSystemModule()->par("playgroundSizeY");
            playgroundWidth = simulation.getSystemModule()->par("playgroundSizeX");

            if (color.empty()) {
                // pick a color with a random hue
                char buf[16];
                double red, green, blue;
                KmlUtil::hsbToRgb(dblrand(), 1.0, 1.0, red, green, blue);
                sprintf(buf, "%2.2x%2.2x%2.2x", int(blue * 255), int(green * 255),
                int(red * 255));
                color = buf;
            }

            KmlHttpServer::getInstance()->addKmlFragmentProvider(this);
            VisualizationController::getInstance()->addMobileNode(this);
        }

        lowerLayerIn = findGate("lowerLayerIn");
        lowerLayerOut = findGate("lowerLayerOut");

        lower802154LayerIn = findGate("lower802154LayerIn");
        lower802154LayerOut = findGate("lower802154LayerOut");

        //UDP conf
        port = par("port");
        socket.setOutputGate(gate("lowerLayerOut"));
        socket.bind(port);
        socket.setBroadcast(true);
        socket.joinLocalMulticastGroups();

        packet80211ByteLength = par("packet80211ByteLength");
        packet802154ByteLength = par("packet802154ByteLength");

        jDEECoInitialize();
    }
}

void JDEECoApplication::handleMessage(cMessage *msg) {
    double rssi = -1.0;
    if (!msg->isSelfMessage()) {
        MacToNetwControlInfo* cInfo = dynamic_cast<MacToNetwControlInfo*>(msg->getControlInfo());
        if (cInfo != NULL) {
            rssi = cInfo->getRSSI();
        }
    }
    jDEECoOnHandleMessage(msg, rssi);
    delete (msg);
}

void JDEECoApplication::jDEECoScheduleAt(double absoluteTime, cMessage *msg) {
    scheduleAt(absoluteTime, msg);
}

void JDEECoApplication::jDEECoSendPacket(JDEECoPacket *packet,
        const char *recipient) {
    if (opp_strcmp("", recipient) == 0) {
        if (gate(lower802154LayerOut)->isConnected()) {
            NetwToMacControlInfo::setControlInfo(packet, LAddress::L2BROADCAST);
            packet->setByteLength(packet802154ByteLength);
            send(packet, lower802154LayerOut);
        }
    } else {
        packet->setByteLength(packet80211ByteLength);
        socket.sendTo(packet, IPvXAddressResolver().resolve(recipient).get4(), port);
    }
}

const char * JDEECoApplication::jDEECoGetModuleId() {
    return id;
}

bool JDEECoApplication::jDEECoIsPositionInfoAvailable() {
    IMobility *mobility = getMobilityModule();
    return mobility != NULL;
}

double JDEECoApplication::jDEECoGetPositionX() {
    IMobility *mobility = getMobilityModule();
    if (mobility != NULL) {
        return mobility->getCurrentPosition().x;
    } else
        return -1.0;
}

double JDEECoApplication::jDEECoGetPositionY() {
    IMobility *mobility = getMobilityModule();
    if (mobility != NULL) {
        return mobility->getCurrentPosition().y;
    } else
        return -1.0;
}

double JDEECoApplication::jDEECoGetPositionZ() {
    IMobility *mobility = getMobilityModule();
    if (mobility != NULL) {
        return mobility->getCurrentPosition().z;
    } else
        return -1.0;
}

void JDEECoApplication::jDEECoSetPositionX(double value) {
    CustomMobility *mobility = dynamic_cast<CustomMobility*>(getMobilityModule());
    if (mobility != NULL) {
        Coord currentPosition = mobility->getCurrentPosition();
        Coord newPosition = Coord::ZERO;
        newPosition.x = value;
        newPosition.y = currentPosition.y;
        newPosition.z = currentPosition.z;
        mobility->setCurrentPosition(newPosition);
    }
}

void JDEECoApplication::jDEECoSetPositionY(double value) {
    CustomMobility *mobility = dynamic_cast<CustomMobility*>(getMobilityModule());
    if (mobility != NULL) {
        Coord currentPosition = mobility->getCurrentPosition();
        Coord newPosition = Coord::ZERO;
        newPosition.x = currentPosition.x;
        newPosition.y = value;
        newPosition.z = currentPosition.z;
        mobility->setCurrentPosition(newPosition);
    }
}

void JDEECoApplication::jDEECoSetPositionZ(double value) {
    CustomMobility *mobility = dynamic_cast<CustomMobility*>(getMobilityModule());
    if (mobility != NULL) {
        Coord currentPosition = mobility->getCurrentPosition();
        Coord newPosition = Coord::ZERO;
        newPosition.x = currentPosition.x;
        newPosition.y = currentPosition.y;
        newPosition.z = value;
        mobility->setCurrentPosition(newPosition);
    }
}

double JDEECoApplication::getTxRange() {
    return 249.0;
}

IMobility *JDEECoApplication::getMobilityModule() {
    IMobility *mobility = check_and_cast<IMobility *>(
            getParentModule()->getSubmodule("mobility"));
    return mobility;
}

std::string JDEECoApplication::getKmlFragment() {
    double longitude = x2lon(getPositionX());
    double latitude = y2lat(getPositionY());
    char buf[16];
    sprintf(buf, "%d", getIndex());
    std::string fragment;
    fragment += KmlUtil::folderHeader((std::string("folder_") + buf).c_str(),
            getFullName());

    fragment += KmlUtil::placemark((std::string("placemark_") + buf).c_str(),
            longitude, latitude, 2 * modelScale, jDEECoGetModuleId(), NULL);
    if (showTxRange)
        fragment += KmlUtil::disk((std::string("disk_") + buf).c_str(),
                longitude, latitude, txRange, "transmission range", NULL,
                (std::string("40") + color).c_str());
    if (!modelURL.empty()) {
        fragment += KmlUtil::model((std::string("model_") + buf).c_str(),
                longitude, latitude, 360, modelScale, modelURL.c_str(),
                "3D model", NULL);
    }

    fragment += "</Folder>\n";
    return fragment;
}

// utility function: converts playground-relative y coordinate (meters) to latitude
double JDEECoApplication::y2lat(double y) {
    return KmlUtil::y2lat(playgroundLat, y);
}

// utility function: converts playground-relative x coordinate (meters) to longitude
double JDEECoApplication::x2lon(double x) {
    return KmlUtil::x2lon(playgroundLat, playgroundLon, x);
}
