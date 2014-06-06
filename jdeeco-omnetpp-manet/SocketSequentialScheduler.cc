/*
 * SocketSequentialScheduler.cc
 *
 *  Created on: 3 Feb 2014
 *      Author: Michal
 */

#include "SocketSequentialScheduler.h"

#ifndef _WIN32
#define closesocket(x) ::close(x)
#endif


Register_Class(cSocketSequentialScheduler);
Register_GlobalConfigOption(CFGID_SOCKETSEQUENTIALSCHEDULER_PORT, "socketsequentialscheduler-port", CFG_INT, "4242", "When cSocketSequentialScheduler is selected as scheduler class: the port number cSocketSequentialScheduler listens on.");

inline std::ostream& operator<<(std::ostream& out, const timeval& tv)
{
    return out << (unsigned long)tv.tv_sec << "s" << tv.tv_usec << "us";
}

//---

cSocketSequentialScheduler::cSocketSequentialScheduler() : cSequentialScheduler()
{
    listenerSocket = INVALID_SOCKET;
    connSocket = INVALID_SOCKET;
}

cSocketSequentialScheduler::~cSocketSequentialScheduler()
{
}

void cSocketSequentialScheduler::startRun()
{
    if (initsocketlibonce()!=0)
        throw cRuntimeError("cSocketRTScheduler: Cannot initialize socket library");

    module = NULL;
    notificationMsg = NULL;
    recvBuffer = NULL;
    recvBufferSize = 0;
    numBytesPtr = NULL;

    port = ev.getConfig()->getAsInt(CFGID_SOCKETSEQUENTIALSCHEDULER_PORT);
    setupListener();
}

void cSocketSequentialScheduler::setupListener()
{
    listenerSocket = socket(AF_INET, SOCK_STREAM, 0);
    if (listenerSocket==INVALID_SOCKET)
        throw cRuntimeError("cSocketRTScheduler: cannot create socket");

    sockaddr_in sinInterface;
    sinInterface.sin_family = AF_INET;
    sinInterface.sin_addr.s_addr = INADDR_ANY;
    sinInterface.sin_port = htons(port);
    if (bind(listenerSocket, (sockaddr*)&sinInterface, sizeof(sockaddr_in))==SOCKET_ERROR)
        throw cRuntimeError("cSocketRTScheduler: socket bind() failed");

    listen(listenerSocket, SOMAXCONN);
}

void cSocketSequentialScheduler::setInterfaceModule(cModule *mod, cMessage *notifMsg, char *buf, int bufSize, int *nBytesPtr)
{
    if (module)
        throw cRuntimeError("cSocketRTScheduler: setInterfaceModule() already called");
    if (!mod || !notifMsg || !buf || !bufSize || !nBytesPtr)
        throw cRuntimeError("cSocketRTScheduler: setInterfaceModule(): arguments must be non-NULL");

    module = mod;
    notificationMsg = notifMsg;
    recvBuffer = buf;
    recvBufferSize = bufSize;
    numBytesPtr = nBytesPtr;
    *numBytesPtr = 0;
    socketCycleCount = 2000;
    currentSocketCycleCount = socketCycleCount;
}

bool cSocketSequentialScheduler::receiveWithTimeout(long usec)
{
    // prepare sets for select()
    fd_set readFDs, writeFDs, exceptFDs;
    FD_ZERO(&readFDs);
    FD_ZERO(&writeFDs);
    FD_ZERO(&exceptFDs);

    // if we're connected, watch connSocket, otherwise accept new connections
    if (connSocket!=INVALID_SOCKET)
        FD_SET(connSocket, &readFDs);
    else
        FD_SET(listenerSocket, &readFDs);

    timeval timeout;
    timeout.tv_sec = 0;
    timeout.tv_usec = usec;

    if (select(FD_SETSIZE, &readFDs, &writeFDs, &exceptFDs, &timeout) > 0)
    {
        // Something happened on one of the sockets -- handle them
        if (connSocket!=INVALID_SOCKET && FD_ISSET(connSocket, &readFDs))
        {
            // receive from connSocket
            char *bufPtr = recvBuffer + (*numBytesPtr);
            int bufLeft = recvBufferSize - (*numBytesPtr);
            if (bufLeft<=0)
                throw cRuntimeError("cSocketSequentialScheduler: interface module's recvBuffer is full");
            int nBytes = recv(connSocket, bufPtr, bufLeft, 0);
            if (nBytes==SOCKET_ERROR)
            {
                EV << "cSocketSequentialScheduler: socket error " << sock_errno() << "\n";
                closesocket(connSocket);
                connSocket = INVALID_SOCKET;
            }
            else if (nBytes == 0)
            {
                EV << "cSocketSequentialScheduler: socket closed by the client\n";
                closesocket(connSocket);
                connSocket = INVALID_SOCKET;
            }
            else
            {
                // schedule notificationMsg for the interface module
                EV << "cSocketSequentialScheduler: received " << nBytes << " bytes\n";
                (*numBytesPtr) += nBytes;
                notificationMsg->setArrival(module,-1,simulation.msgQueue.peekFirst()->getArrivalTime());
                simulation.msgQueue.insert(notificationMsg);
                return true;
            }
        }
        else if (FD_ISSET(listenerSocket, &readFDs))
        {
            // accept connection, and store FD in connSocket
            sockaddr_in sinRemote;
            int addrSize = sizeof(sinRemote);
            connSocket = accept(listenerSocket, (sockaddr*)&sinRemote, (socklen_t*)&addrSize);
            if (connSocket==INVALID_SOCKET)
                throw cRuntimeError("cSocketSequentialScheduler: accept() failed");
            EV << "cSocketSequentialScheduler: connected!\n";
        }
    }
    return false;
}

cMessage *cSocketSequentialScheduler::getNextEvent()
{
    if (!module)
        throw cRuntimeError("cSocketRTScheduler: setInterfaceModule() not called: it must be called from a module's initialize() function");

   if (currentSocketCycleCount == 0) {
       receiveWithTimeout(50);
       currentSocketCycleCount = socketCycleCount;
   } else
       currentSocketCycleCount--;
   return cSequentialScheduler::getNextEvent();
}

void cSocketSequentialScheduler::sendBytes(const char *buf, size_t numBytes)
{
    if (connSocket==INVALID_SOCKET)
        throw cRuntimeError("cSocketRTScheduler: sendBytes(): no connection");

    send(connSocket, buf, numBytes, 0);
    // TBD check for errors
}

void cSocketSequentialScheduler::close()
{
    if (connSocket!=INVALID_SOCKET)
    {
        closesocket(connSocket);
        connSocket = INVALID_SOCKET;
    }
}

