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

#ifndef SOCKETDATAPROVIDER_H_
#define SOCKETDATAPROVIDER_H_

#include <omnetpp.h>

class cSocketDataProvider {
public:
    /**
     * To be called from the module which wishes to receive data from the
     * socket. The method must be called from the module's initialize()
     * function.
     */
    virtual void setInterfaceModule(cModule *module, cMessage *notificationMsg,
            char *recvBuffer, int recvBufferSize, int *numBytesPtr) = 0;

    /**
     * Send on the currently open connection
     */
    virtual void sendBytes(const char *buf, size_t numBytes) = 0;

    /**
     * Close the currently open connection
     */
    virtual void close() = 0;
};

#endif /* SOCKETDATAPROVIDER_H_ */
