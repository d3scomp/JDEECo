/*
 * jDEECoModule.h
 *
 *  Created on: 24 Dec 2013
 *      Author: Michal Kit <kit@d3s.mff.cuni.cz>
 */

#ifndef JDEECOMODULE_H
#define JDEECOMODULE_H

#include <jni.h>
#include <limits>

#include "csimplemodule.h"
#include "JDEECoPacket_m.h"

#define JDEECO_TIMER_MESSAGE "@jDEECoTimerMessage@"
#define JDEECO_DATA_MESSAGE "@jDEECoPacketMessage@"

class JDEECoModule {
	double currentCallAtTime;
	void *currentCallAtMessage;

public:
	JDEECoModule() {
		currentCallAtTime = std::numeric_limits<double>::min();
		currentCallAtMessage = NULL;
		initialized = false;
	}
	virtual ~JDEECoModule() {
	}

	void callAt(double absoluteTime);

	virtual const char * getModuleId() = 0;
	virtual void sendPacket(JDEECoPacket *packet, const char *recipient) = 0;
	virtual void registerCallbackAt(double absoluteTime, cMessage *msg) = 0;
	virtual bool isPositionInfoAvailable() = 0;
	virtual double getPositionX() = 0;
	virtual double getPositionY() = 0;
	virtual double getPositionZ() = 0;
	virtual void setPosition(double valX, double valY, double valZ) = 0;

	static JDEECoModule* findModule(JNIEnv *env, jstring id);
	static void clear();

protected:
	bool initialized;

	//Needs to be called at the module initialization
	void initialize();
	//Needs to be called from the handleMessage method
	void onHandleMessage(cMessage *msg, double rssi);

private:
	// XXX: This should be a hash map. Having it in a vector will be too slow when we have many nodes.
	// XXX: We would better map java classes to C++ classes by reference, if possible
	static std::vector<JDEECoModule*> jDEECoModules;
};

#endif /* JDEECOMODULE_H */
