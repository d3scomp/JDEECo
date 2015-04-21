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

#include "JDEECoApplication.h"
#include "MacToNetwControlInfo.h"
#include "NetwToMacControlInfo.h"
#include "IPv4ControlInfo.h"
#include "IPvXAddressResolver.h"
#include "UDPControlInfo.h"
#include "CustomMobility.h"

Define_Module(JDEECoApplication);

void JDEECoApplication::initialize(int stage) {
    if (stage == 1) {
        id = par("id").longValue();
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

        JDEECoModule::initialize();
    }
}

void JDEECoApplication::handleMessage(cMessage *msg) {
	//std::cout << "jDEECoApplication: handleMessage:" << msg->getName() << std::endl;
    double rssi = -1.0;
    if (!msg->isSelfMessage()) {
        MacToNetwControlInfo* cInfo = dynamic_cast<MacToNetwControlInfo*>(msg->getControlInfo());
        if (cInfo != NULL) {
            rssi = cInfo->getRSSI();
        }
    }
    onHandleMessage(msg, rssi);
    delete (msg);
}

void JDEECoApplication::registerCallbackAt(double absoluteTime, cMessage *msg) {
	//std::cout << "jDEECoApplication: registerCallbackAt" << std::endl;
    scheduleAt(absoluteTime, msg);
}

void JDEECoApplication::sendPacket(JDEECoPacket *packet, const char *recipient) {
	if (opp_strcmp("", recipient) == 0) {
		if (gate(lower802154LayerOut)->isConnected()) {
			NetwToMacControlInfo::setControlInfo(packet, LAddress::L2BROADCAST);
			packet->setByteLength(packet802154ByteLength);
			send(packet, lower802154LayerOut);
		}
	} else {
		packet->setByteLength(packet80211ByteLength);

		// Obtain target IP address
		IPvXAddress ip;
		ip.set(recipient);
//		std::cout << std::endl << "IP address:" << ip.str() << std::endl << std::endl;

		socket.sendTo(packet, ip, port);
    }
}

const NodeId JDEECoApplication::getModuleId() {
    return id;
}

bool JDEECoApplication::isPositionInfoAvailable() {
    IMobility *mobility = getMobilityModule();
    return mobility != NULL;
}

double JDEECoApplication::getPositionX() {
    IMobility *mobility = getMobilityModule();
    if (mobility != NULL) {
        return mobility->getCurrentPosition().x;
    } else
        return -1.0;
}

double JDEECoApplication::getPositionY() {
    IMobility *mobility = getMobilityModule();
    if (mobility != NULL) {
        return mobility->getCurrentPosition().y;
    } else
        return -1.0;
}

double JDEECoApplication::getPositionZ() {
    IMobility *mobility = getMobilityModule();
    if (mobility != NULL) {
        return mobility->getCurrentPosition().z;
    } else
        return -1.0;
}

void JDEECoApplication::setPosition(double valX, double valY, double valZ) {
    CustomMobility *mobility = dynamic_cast<CustomMobility*>(getMobilityModule());
    if (mobility != NULL) {
        Coord currentPosition = mobility->getCurrentPosition();
        Coord newPosition = Coord::ZERO;
        newPosition.x = valX;
        newPosition.y = valY;
        newPosition.z = valZ;
        mobility->setCurrentPosition(newPosition);
    } else {
    	std::cout << "Attempt to set position with custom mobility == NULL" << std::endl;
    }
}

IMobility *JDEECoApplication::getMobilityModule() {
    IMobility *mobility = check_and_cast<IMobility *>(
            getParentModule()->getSubmodule("mobility"));
    return mobility;
}
