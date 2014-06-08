/*
 * SocketSequentialScheduler.h
 *
 *  Created on: 3 Feb 2014
 *      Author: Michal
 */

#ifndef SOCKETSEQUENTIALSCHEDULER_H_
#define SOCKETSEQUENTIALSCHEDULER_H_

#include <platdep/sockets.h>

#include "SocketDataProvider.h"

class cSocketSequentialScheduler: public cSequentialScheduler, public cSocketDataProvider {

protected:
  // config
  int port;

  cModule *module;
  cMessage *notificationMsg;
  char *recvBuffer;
  int recvBufferSize;
  int *numBytesPtr;
  int currentSocketCycleCount;

  SOCKET listenerSocket;
  SOCKET connSocket;

  int socketCycleCount;

  virtual void setupListener();
  virtual bool receiveWithTimeout(long usec);

public:
  cSocketSequentialScheduler();
  virtual ~cSocketSequentialScheduler();

  /**
   * Called at the beginning of a simulation run.
   */
  virtual void startRun();

  /**
   * To be called from the module which wishes to receive data from the
   * socket. The method must be called from the module's initialize()
   * function.
   */
  virtual void setInterfaceModule(cModule *module, cMessage *notificationMsg,
                                  char *recvBuffer, int recvBufferSize, int *numBytesPtr);

  /**
   * Called at the end of a simulation run.
   */
  virtual void endRun() {};

  /**
   * Recalculates "base time" from current wall clock time.
   */
  virtual void executionResumed() {};

  /**
   * Scheduler function -- it comes from cScheduler interface.
   */
  virtual cMessage *getNextEvent();

  /**
   * Send on the currently open connection
   */
  virtual void sendBytes(const char *buf, size_t numBytes);

  /**
   * Close the currently open connection
   */
  virtual void close();
};

#endif /* SOCKETSEQUENTIALSCHEDULER_H_ */
