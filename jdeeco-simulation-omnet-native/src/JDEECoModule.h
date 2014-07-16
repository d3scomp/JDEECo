/*
 * jDEECoModule.h
 *
 *  Created on: 24 Dec 2013
 *      Author: Michal Kit <kit@d3s.mff.cuni.cz>
 */

#ifndef JDEECOMODULE_H_
#define JDEECOMODULE_H_

#include "csimplemodule.h"
#include "JDEECoPacket_m.h"

#define JDEECO_TIMER_MESSAGE "@jDEECoTimerMessage@"
#define JDEECO_DATA_MESSAGE "@jDEECoPacketMessage@"

class JDEECoModule {

	double currentCallAtTime;
	void *currentCallAtMessage;

public:
	JDEECoModule() {currentCallAtTime = -1.0; currentCallAtMessage = NULL; initialized = false; }
	virtual ~JDEECoModule() {}

	void callAt(double absoluteTime);

	//Needs to be implemented by the module
	virtual const char * getModuleId() {return NULL;};
	//Needs to be implemented by the module
	virtual void sendPacket(JDEECoPacket *packet, const char *recipient) {};
	//Needs to be implemented by the module
	virtual void registerCallbackAt(double absoluteTime, cMessage *msg) {};
	//Needs to be implemented by the module
	virtual bool isPositionInfoAvailable() {return false;};
	//Needs to be implemented by the module
	virtual double getPositionX() {return 0;};
	//Needs to be implemented by the module
	virtual double getPositionY() {return 0;};
	//Needs to be implemented by the module
	virtual double getPositionZ() {return 0;};
	//Needs to be implemented by the module
	virtual void setPosition(double valX, double valY, double valZ) {};

protected:
	bool initialized;

	//Needs to be called at the module initialisation
	void initialize();
	//Needs to be called from the handleMessage method
	void onHandleMessage(cMessage *msg, double rssi);

};

#endif /* JDEECOMODULE_H_ */
