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

package backbone;

simple PriorityTrafficGenerator
{
    parameters:
        string id = default("not-assigned");
        bool dynamicAPConnections = default(false);
        int maxPriority = default(7);
        double responseDelay @unit(s) = default(-1s);
        string destAddress = default("");  // destination MAC address, or module path name of destination station; empty means off
        int etherType = default(0);  // etherType to set on outgoing packets
        double startTime @unit(s) = default(this.sendInterval);  // time of sending the first packet
        double stopTime @unit(s) = default(0);  // time of finish sending, 0 means forever
        volatile double sendInterval @unit(s) = default(0.007s);  // interval between sending bursts
        volatile int numPacketsPerBurst = default(1);  // number of packets to send per burst (packets within a burst are sent at the same simulation time)
        volatile int packetLength @unit(B) = default(128B);  // length of packets to send
        @display("i=block/app");
        @signal[sentPk](type=cPacket);
        @signal[rcvdPk](type=cPacket);
        @statistic[rcvdPk](title="packets received"; source=rcvdPk; record=count,"sum(packetBytes)","vector(packetBytes)"; interpolationmode=none);
        @statistic[sentPk](title="packets sent"; source=sentPk; record=count,"sum(packetBytes)","vector(packetBytes)"; interpolationmode=none);
        @statistic[endToEndDelay](title="end-to-end delay"; source="messageAge(rcvdPk)"; unit=s; record=histogram,vector; interpolationmode=none);
    gates:
        input in @labels(Generator/up);
        output out @labels(Generator/down);
}
