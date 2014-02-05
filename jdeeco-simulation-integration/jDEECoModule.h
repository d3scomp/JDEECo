/*
 * jDEECoModule.h
 *
 *  Created on: 24 Dec 2013
 *      Author: Michal Kit <kit@d3s.mff.cuni.cz>
 */

#ifndef JDEECOMODULE_H_
#define JDEECOMODULE_H_

#include "jDEECoConstants.h"
#include "cSimpleModule.h"
#include "jDEECoPacket_m.h"

#define JDEECO_TIMER_MESSAGE "@jDEECoTimerMessage@"
#define JDEECO_DATA_MESSAGE "@jDEECoPacketMessage@"

class DLLEXPORT_OR_IMPORT jDEECoModule {

	double currentCallAtTime;
	void *currentCallAtMessage;

public:
	jDEECoModule() {currentCallAtTime = -1.0; currentCallAtMessage = NULL;}
	virtual ~jDEECoModule() {}

	void jDEECoCallAt(double absoluteTime);

	//Needs to be implemented by the module
	virtual const char * jDEECoGetModuleId() {return NULL;};
	//Needs to be implemented by the module
	virtual void jDEECoSendPacket(JDEECoPacket *packet, const char *recipient) {};
	//Needs to be implemented by the module
	virtual void jDEECoScheduleAt(double absoluteTime, cMessage *msg) {};
	//Needs to be implemented by the module
	virtual bool jDEECoIsPositionInfoAvailable() {return false;};
	//Needs to be implemented by the module
	virtual double jDEECoGetPositionX() {return 0;};
	//Needs to be implemented by the module
	virtual double jDEECoGetPositionY() {return 0;};
	//Needs to be implemented by the module
	virtual double jDEECoGetPositionZ() {return 0;};

protected:
	//Needs to be called at the module initialisation
	void jDEECoInitialize();
	//Needs to be called from the handleMessage method
	void jDEECoOnHandleMessage(cMessage *msg, double rssi);

};

#endif /* JDEECOMODULE_H_ */
