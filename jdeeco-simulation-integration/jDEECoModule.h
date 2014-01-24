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
	//Java host reference
	void *host;
	//JavaVM reference
	void *jvm;

	jDEECoModule() {host = NULL; jvm = NULL; currentCallAtTime = -1.0; currentCallAtMessage = NULL;}
	virtual ~jDEECoModule() {};

	void jDEECoCallAt(double absoluteTime);

	//Needs to be implemented by the module
	virtual const char * jDEECoGetModuleId() {return NULL;};
	//Needs to be implemented by the module
	virtual void jDEECoSendPacket(JDEECoPacket *packet, const char *recipient) {};

protected:
	//Needs to be called at the module initialisation
	void jDEECoInitialize();
	//Needs to be called from the handleMessage method
	void jDEECoOnHandleMessage(cMessage *msg);

};

#endif /* JDEECOMODULE_H_ */
